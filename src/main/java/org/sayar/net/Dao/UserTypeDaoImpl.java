package org.sayar.net.Dao;

import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.sayar.net.Enumes.Type;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.AssetTemplateGroupPersonnelDTO;
import org.sayar.net.Model.DTO.OrganizationTermDTO;
import org.sayar.net.Model.DTO.ProjectGroupPersonnelDTO;
import org.sayar.net.Model.UserType;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Repository("userTypeDaoImp")
public class UserTypeDaoImpl extends GeneralDaoImpl<UserType> implements UserTypeDao {

    @Autowired
    private MongoOperations mongoOperations;

    public UserTypeDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


    @Override
    public List<UserType> filter(String name, String type, Pageable pageable, Integer totalElement) {
        long skip = pageable.getPageSize() * pageable.getPageNumber();
        if (skip < 0) {
            skip = 0L;
        }
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (name != null && !name.isEmpty()) {
            criteria.and("name").regex(name);
        }
        if (type != null) {
            criteria.and("type").is(type);
        }
//        Criteria criteria;
//        List<Criteria> criteriaList = new ArrayList<>();
//        criteriaList.add(Criteria.where("deleted").ne(true));
//        if (name != null) {
//            String nameTrim = name.trim();
//            System.out.println("entered");
//            System.out.println(nameTrim);
//            criteriaList.add(Criteria.where("name").regex(nameTrim));
//        }
//        if (type != null) {
//            criteriaList.add(Criteria.where("type").is(type));
//        }
//
//        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
//        Print.print("criteria", criteria);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.skip(skip),
                Aggregation.limit(pageable.getPageSize()),
                Aggregation.project()
                        .andExclude("privilege")
        );
        return mongoOperations.aggregate(aggregation, UserType.class, UserType.class).getMappedResults();
    }

    @Override
    public UserType createUserType(UserType userType) {
        return mongoOperations.save(userType);
    }

    @Override
    public UserType getOneUserType(String userTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userTypeId));
        return mongoOperations.findOne(query, UserType.class);
    }

    @Override
    public UpdateResult updateUserType(UserTypeDTO userType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userType.getId()));
        Update update = new Update();
        update.set("id", userType.getId());
        update.set("name", userType.getName());
//        update.set("type", userType.getType());
        update.set("privilege", userType.getPrivilege());
        return mongoOperations.updateFirst(query, update, UserType.class);
    }

    @Override
    public List<UserType> getAllUserTypeByIdList(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public List<UserType> getAllType() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().exclude("privilege");
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public UserType getOneUserTypeByAssetId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userId").is(id));
        return mongoOperations.findOne(query, UserType.class);
    }

    @Override
    public List<UserType> getAllUserTypeByOfOrganization(List<String> userTypeIdList) {
        Print.print("idList", userTypeIdList);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        Print.print("result", mongoOperations.find(query, UserType.class));
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public UserType getUserTypeOfUser(String userTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userTypeId));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.findOne(query, UserType.class);
    }

    @Override
    public UserType getMainInformationOfUser(String userTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userTypeId));
        return mongoOperations.findOne(query, UserType.class);
    }

    @Override
    public UserType getUserTypeByUSerTypeId(String userTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userTypeId));
        query.fields().include("name");
        return mongoOperations.findOne(query, UserType.class);
    }

    @Override
    public long countUserTypes(String name, String type) {

        Criteria criteria;
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("deleted").ne(true));
        if (name != null && !name.equals(" ")) {
            criteriaList.add(Criteria.where("name").regex(name));
        }
        if (type != null) {
            criteriaList.add(Criteria.where("type").is(type));
        }

        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, UserType.class, UserType.class).getMappedResults().size();
    }

    @Override
    public List<UserType> getUserTypeListOfUsernames(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public List<UserType> getUserTypeOfActivity(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        query.fields()
                .include("name")
                .include("id");
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public UserType getUserTypeOfTheUser(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(id));
        query.fields()
                .include("name")
                .include("family");
        return mongoOperations.findOne(query, UserType.class);
    }

    @Override
    public UserType getRequesterUserType(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(id));
        query.fields()
                .include("name");
        return mongoOperations.findOne(query, UserType.class);
    }

    @Override
    public List<UserType> getAllUserTypesByTerm(String term, String organizationId) {
        return null;
    }

    @Override
    public List<String> getUserTypeOfOrganizationByTerm(OrganizationTermDTO organizationTermDTO, String term) {
        return organizationTermDTO.getUserTypeName().stream().filter(s -> s.contains(term)).collect(Collectors.toList());
    }

    @Override
    public List<UserType> getAllUserTypeOfChildUsers(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public List<UserType> getUserTypeList(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public List<UserType> getRelevantUserType(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public List<UserType> getNeededUserTypeList(List<String> userTypeList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeList));
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public List<UserType> getChildrenUserTypeList(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public List<UserType> getUserTypeNameOfUser(List<String> parentUserTypeList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(parentUserTypeList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public List<UserType> getUserTypeTitleList(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public UserType createFirstUserType() {

        UserType userType = new UserType();
        userType.setId(new ObjectId().toString());
        userType.setName("admin");
        userType.setType(Type.ADMIN);
        List<String> privilege = new ArrayList<>();
        privilege.add("User_create");
        privilege.add("User_update");
        privilege.add("User_see");
        privilege.add("User_delete");
        privilege.add("UserType_create");
        privilege.add("UserType_update");
        privilege.add("UserType_see");
        privilege.add("UserType_delete");
        privilege.add("City_create");
        privilege.add("City_update");
        privilege.add("City_see");
        privilege.add("City_delete");
        privilege.add("Province_create");
        privilege.add("Province_update");
        privilege.add("Province_see");
        privilege.add("Province_delete");
        privilege.add("Currency_create");
        privilege.add("Currency_delete");
        privilege.add("Currency_update");
        privilege.add("Currency_see");
        privilege.add("Budget_create");
        privilege.add("Budget_update");
        privilege.add("Budget_see");
        privilege.add("Budget_delete");
        privilege.add("WorkOrder_see");
        privilege.add("WorkOrder_create");
        privilege.add("WorkOrder_delete");
        privilege.add("WorkOrder_update");
        privilege.add("WorkOrderStatus_create");
        privilege.add("WorkOrderStatus_update");
        privilege.add("WorkOrderStatus_see");
        privilege.add("WorkOrderStatus_delete");
        privilege.add("Measurement_create");
        privilege.add("Measurement_update");
        privilege.add("Measurement_see");
        privilege.add("Measurement_delete");
        privilege.add("Property_create");
        privilege.add("Property_update");
        privilege.add("Property_see");
        privilege.add("Property_delete");
        privilege.add("Company_create");
        privilege.add("Company_update");
        privilege.add("Company_see");
        privilege.add("Company_delete");
        privilege.add("organization_create");
        privilege.add("organization_update");
        privilege.add("organization_see");
        privilege.add("organization_delete");
        privilege.add("Storage_create");
        privilege.add("Storage_update");
        privilege.add("Storage_see");
        privilege.add("Storage_delete");
        privilege.add("ChargeDepartment_create");
        privilege.add("ChargeDepartment_update");
        privilege.add("ChargeDepartment_see");
        privilege.add("ChargeDepartment_delete");
        privilege.add("Category_create");
        privilege.add("Category_update");
        privilege.add("Category_see");
        privilege.add("Category_delete");
        privilege.add("AssetTemplate_create");
        privilege.add("AssetTemplate_update");
        privilege.add("AssetTemplate_see");
        privilege.add("Asset_create");
        privilege.add("Asset_update");
        privilege.add("Asset_see");
        privilege.add("Asset_delete");
        privilege.add("Project_create");
        privilege.add("Project_update");
        privilege.add("Project_see");
        privilege.add("Project_delete");
        privilege.add("Scheduling_create");
        privilege.add("Scheduling_update");
        privilege.add("Scheduling_see");
        privilege.add("Scheduling_delete");
        privilege.add("TaskGroup_create");
        privilege.add("TaskGroup_update");
        privilege.add("TaskGroup_see");
        privilege.add("TaskGroup_delete");
        privilege.add("Part_create");
        privilege.add("Part_update");
        privilege.add("Part_see");
        privilege.add("Part_delete");
        privilege.add("Inventory_create");
        privilege.add("Inventory_see");
        privilege.add("Inventory_delete");
        privilege.add("AdjustInventory_create");
        privilege.add("AdjustInventory_see");
        privilege.add("AdjustInventory_delete");
        privilege.add("noticeBoard_see");
        privilege.add("noticeBoard_create");
        privilege.add("noticeBoard_update");
        privilege.add("workRequest_create");
        privilege.add("workRequest_see");
        privilege.add("workRequest_delete");
        privilege.add("formCategory_create");
        privilege.add("formCategory_update");
        privilege.add("formCategory_see");
        privilege.add("formCategory_delete");
        privilege.add("BOM_create");
        privilege.add("BOM_update");
        privilege.add("BOM_see");
        privilege.add("BOM_delete");
        privilege.add("formBuilder_create");
        privilege.add("formBuilder_edit");
        privilege.add("formBuilder_detail");
        privilege.add("formBuilder_delete");
        privilege.add("processBuilder_create");
        privilege.add("processBuilder_edit");
        privilege.add("processBuilder_detail");
        privilege.add("processBuilder_delete");
        privilege.add("metering_create");
        privilege.add("metering_detail");
        privilege.add("message_private_Detail");
        privilege.add("message_private_Delete");
        privilege.add("message_sender_Detail");
        privilege.add("message_sender_Delete");
        privilege.add("message_system_Detail");
        privilege.add("message_system_Delete");
        privilege.add("message_send");
        privilege.add("AssignedAsset_update");
        privilege.add("AssignedAsset_see");
        privilege.add("AssignedPart_update");
        privilege.add("AssignedPart_see");
        privilege.add("AssignedWorkOrder_update");
        privilege.add("AssignedWorkOrder_see");
        userType.setPrivilege(privilege);
        return mongoOperations.save(userType);
    }

    @Override
    public UserType getRelatedUserTypeByUserTypeId(String userTypeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userTypeId));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.findOne(query, UserType.class);
    }

    @Override
    public List<UserType> getUserTypeName(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public List<UserType> getAssetTemplatePersonnelUserType(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, UserType.class);
    }

    @Override
    public List<ProjectGroupPersonnelDTO> getProjectPersonnelUserType(List<String> userTypeIdList) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").in(userTypeIdList);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project()
                        .and("id").as("userTypeId")
                        .and("name").as("userTypeName")
        );
        return mongoOperations.aggregate(aggregation, UserType.class, ProjectGroupPersonnelDTO.class).getMappedResults();
    }

    @Override
    public List<AssetTemplateGroupPersonnelDTO> getAssetTemplateGroupPersonnel(List<String> userTypeIdList) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").in(userTypeIdList);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project()
                        .and("id").as("userTypeId")
                        .and("name").as("userTypeName")
        );

        return mongoOperations.aggregate(aggregation, UserType.class, AssetTemplateGroupPersonnelDTO.class).getMappedResults();
    }

    @Override
    public List<UserType> getUserTypeListOfNewWorkOrder(List<String> userTypeIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(userTypeIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, UserType.class);
    }
}
