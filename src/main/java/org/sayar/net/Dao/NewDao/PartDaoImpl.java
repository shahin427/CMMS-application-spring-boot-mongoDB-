package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Part.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class PartDaoImpl extends GeneralDaoImpl<Part> implements PartDao {
    @Autowired
    private MongoOperations mongoOperations;

    public PartDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public Part postPart(Part part) {
        part.setRegistrationDate(new Date());
        return mongoOperations.save(part);
    }


    @Override
    public Part getPrivateSideOfPart(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(partId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("id").include("name").include("description").include("image");
        return mongoOperations.findOne(query, Part.class);
    }

    @Override
    public List<Part> getAllPrivateSideOfPart() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("name");
        query.fields().include("partCode");
        return mongoOperations.find(query, Part.class);
    }

    @Override
    public List<Part> getAllGeneralSideOfPart() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().exclude("name").exclude("description").exclude("image");
        return mongoOperations.find(query, Part.class);
    }

    @Override
    public Part getOneGeneralSideOfPart(String partId) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("id").is(partId),
                        Criteria.where("deleted").ne(true)
                )
        );
        query.fields().exclude("name").exclude("description").exclude("image").exclude("partCode");
        return mongoOperations.findOne(query, Part.class);
    }

    @Override
    public boolean checkIfCodeExists(String partCode) {
        System.out.println(partCode);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("partCode").is(partCode));
        Part part = mongoOperations.findOne(query, Part.class);
        if (part == null) {
            System.out.println("کد تکراری نیست");
            return false;
        } else {
            System.out.println("کد تکراری است");
            return true;
        }
    }

    @Override
    public Part postPartInventory(PartDTO partDTO, String PartId) {
        return null;
    }

    @Override
    public boolean checkIfInventoryCodeExists(String inventoryCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("inventoryCode").is(inventoryCode));
        Part part = mongoOperations.findOne(query, Part.class);
        if (part == null) {
            System.out.println("کد تکراری نیست");
            return false;
        } else
            System.out.println("کد تکراری است");
        return true;
    }

    @Override
    public List<Part> getAllPartsByFilterAndPagination(String term, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals(""))
            criteria.and("name").regex(term);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
                , skipOperation
                , limitOperation
        );
        return this.aggregate(aggregation, "part", Part.class).getMappedResults();
    }

    @Override
    public long getAllCount(String term) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals(""))
            criteria.and("name").regex(term);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return this.aggregate(aggregation, "part", Part.class).getMappedResults().size();
    }

    @Override
    public List<Part> getAllPart(Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("deleted").ne(true))
                , skipOperation
        );
        return this.aggregate(aggregation, "part", Part.class).getMappedResults();
    }

    @Override
    public List<Part> getAllPartsById(List<String> parts) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(parts));
        return mongoOperations.find(query, Part.class);
    }

    @Override
    public Part getOnePart(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partId));
        query.fields()
                .include("partCode")
                .include("name")
                .include("image")
                .include("description");
        return mongoOperations.findOne(query, Part.class);
    }

    @Override
    public UpdateResult updateUpSide(PartPrivateDTO partPrivateDTO, String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(partId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        Update update = new Update();
        update.set("name", partPrivateDTO.getName());
        update.set("description", partPrivateDTO.getDescription());
        update.set("partCode", partPrivateDTO.getPartCode());
        update.set("image", partPrivateDTO.getImage());
        update.set("numberOfInventory", partPrivateDTO.getNumberOfInventory());
        return mongoOperations.updateFirst(query, update, Part.class);
    }

    @Override
    public UpdateResult updateDownSide(PartGeneralDTO partGeneralDTO, String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(partId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().exclude("name").include("description").include("partCode").include("image");

        Update update = new Update();
        update.set("quantity", partGeneralDTO.getQuantity());
        update.set("minQuantity", partGeneralDTO.getMinQuantity());
        update.set("chargeDepartment", partGeneralDTO.getChargeDepartment());
        update.set("budget", partGeneralDTO.getBudget());
        update.set("corridor", partGeneralDTO.getCorridor());
        update.set("row", partGeneralDTO.getRow());
        update.set("warehouse", partGeneralDTO.getWarehouse());
        update.set("price", partGeneralDTO.getPrice());
        update.set("user", partGeneralDTO.getUser());
        update.set("metering", partGeneralDTO.getMetering());
        update.set("uploadFile", partGeneralDTO.getUploadFile());
        update.set("company", partGeneralDTO.getCompany());
        update.set("inventoryCode", partGeneralDTO.getInventoryCode());

        return mongoOperations.updateFirst(query, update, Part.class);

    }

    @Override
    public UpdateResult updateUserListByPartId(List<String> user, String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partId));
        Update update = new Update();
        update.set("usersIdList", user);
        return mongoOperations.updateFirst(query, update, Part.class);
    }

    @Override
    public Part getUsersListByPartId(String partId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partId));
        query.fields()
                .include("usersIdList");
        return mongoOperations.findOne(query, Part.class);
    }

    @Override
    public long getAllCountWithOutTerm() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.count(query, Part.class);
    }

    @Override
    public List<Part> getPartsByPartIdList(List<String> bomPartIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(bomPartIdList));
        query.fields().include("id")
                .include("name")
                .include("partCode");
        return mongoOperations.find(query, Part.class);
    }

    @Override
    public List<PartGetAllDTO> getAllAssignedUsersOfPartByUserId(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("partId")
                .andExpression("name").as("partName")
                .andExpression("partCode").as("partCode");

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        Criteria criteria1 = new Criteria();
        criteria1.and("assignedToPersonList.userId").is(partDtoForSearch.getUserId());

        if (partDtoForSearch.getPartName() != null && !partDtoForSearch.getPartName().equals("")) {
            criteria.and("name").regex(partDtoForSearch.getPartName());
        }

        if (partDtoForSearch.getPartCode() != null && !partDtoForSearch.getPartCode().equals("")) {
            criteria.and("partCode").regex(partDtoForSearch.getPartCode());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                unwind("assignedToPersonList", true),
                match(criteria1),
                projectionOperation,
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Part.class, PartGetAllDTO.class).getMappedResults();
    }

    @Override
    public List<Part> getAssignedPartListsOfUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("users").elemMatch(Criteria.where("id").is(userId)));
        return mongoOperations.find(query, Part.class);
    }

    @Override
    public List<PartGetAllDTO> getAllPartsWithOutInventory(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer element) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("partId")
                .andExpression("name").as("partName")
                .andExpression("partCode").as("partCode")
                .andExpression("currentQuantity").as("currentQuantity");

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (partDtoForSearch.getPartName() != null && !partDtoForSearch.getPartName().equals("")) {
            criteria.and("name").regex(partDtoForSearch.getPartName());
        }
        if (partDtoForSearch.getPartCode() != null && !partDtoForSearch.getPartCode().equals("")) {
            criteria.and("partCode").regex(partDtoForSearch.getPartCode());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sort(Sort.Direction.DESC, "registrationDate"),
                projectionOperation,
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Part.class, PartGetAllDTO.class).getMappedResults();
    }

    @Override
    public boolean checkIfUserExistsInPart(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("users").elemMatch(Criteria.where("id").is(userId)));
        return mongoOperations.exists(query, Part.class);
    }

    @Override
    public boolean ifCompanyExistsInPart(String companyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("company.id").is(companyId));
        return mongoOperations.exists(query, Part.class);
    }

    @Override
    public boolean ifWareHouseExistsInPart(String storageId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("warehouse").is(storageId));
        return mongoOperations.exists(query, Part.class);
    }

    @Override
    public long numberOfParts(PartDtoForSearch partDtoForSearch) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (partDtoForSearch.getPartName() != null && !partDtoForSearch.getPartName().equals("")) {
            criteria.and("name").regex(partDtoForSearch.getPartName());
        }
        if (partDtoForSearch.getPartCode() != null && !partDtoForSearch.getPartCode().equals("")) {
            criteria.and("partCode").regex(partDtoForSearch.getPartCode());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Part.class, Part.class).getMappedResults().size();
    }

    @Override
    public List<Part> getAllParts() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("numberOfInventory").is(0));
        return mongoOperations.find(query, Part.class);
    }

    @Override
    public long numberOfUserParts(PartDtoForSearch partDtoForSearch) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        Criteria criteria1 = new Criteria();
        criteria1.and("assignedToPersonList.userId").is(partDtoForSearch.getUserId());

        if (partDtoForSearch.getPartName() != null && !partDtoForSearch.getPartName().equals("")) {
            criteria.and("name").regex(partDtoForSearch.getPartName());
        }

        if (partDtoForSearch.getPartCode() != null && !partDtoForSearch.getPartCode().equals("")) {
            criteria.and("partCode").regex(partDtoForSearch.getPartCode());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                unwind("assignedToPersonList", true),
                match(criteria1)
        );
        return mongoOperations.aggregate(aggregation, Part.class, PartGetAllDTO.class).getMappedResults().size();
    }

    @Override
    public void decreaseNumberOfInventoryInPart(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partId));
        Update update = new Update();
        update.inc("numberOfInventory", -1);
        mongoOperations.updateFirst(query, update, Part.class);
    }

    @Override
    public List<Part> getAllBOMPart(Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("id")
                .include("name")
                .include("partCode");
        query.with(pageable);
        return mongoOperations.find(query, Part.class);
    }

    @Override
    public long countBOMPart() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.count(query, Part.class);
    }

    @Override
    public Part getPersonsOfPart(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partId));
        query.fields()
                .include("assignedToPersonList");
        return mongoOperations.findOne(query, Part.class);
    }

    @Override
    public Part getGroupPersonnelOfPart(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partId));
        query.fields()
                .include("assignedToGroupList");
        return mongoOperations.findOne(query, Part.class);
    }

    @Override
    public boolean addPersonTypePersonnel(String partId, List<AssignedToPerson> assignedToPersonList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partId));
        Update update = new Update();
        update.set("assignedToPersonList", assignedToPersonList);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, Part.class);
        if (updateResult.getModifiedCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addGroupTypePersonnel(String partId, List<AssignedToGroup> assignedToGroupList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partId));
        Update update = new Update();
        update.set("assignedToGroupList", assignedToGroupList);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, Part.class);
        if (updateResult.getModifiedCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<PartGetAllDTO> getAllAssignedPartsOfGroup(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("partId")
                .andExpression("name").as("partName")
                .andExpression("partCode").as("partCode");

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        Criteria criteria1 = new Criteria();
        criteria1.and("assignedToGroupList.userTypeId").is(partDtoForSearch.getUserTypeId());

        if (partDtoForSearch.getPartName() != null && !partDtoForSearch.getPartName().equals("")) {
            criteria.and("name").regex(partDtoForSearch.getPartName());
        }

        if (partDtoForSearch.getPartCode() != null && !partDtoForSearch.getPartCode().equals("")) {
            criteria.and("partCode").regex(partDtoForSearch.getPartCode());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                unwind("assignedToGroupList", true),
                match(criteria1),
                projectionOperation,
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Part.class, PartGetAllDTO.class).getMappedResults();
    }

    @Override
    public long countAllAssignedPartsOfGroup(PartDtoForSearch partDtoForSearch) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        Criteria criteria1 = new Criteria();
        criteria1.and("assignedToGroupList.userTypeId").is(partDtoForSearch.getUserTypeId());

        if (partDtoForSearch.getPartName() != null && !partDtoForSearch.getPartName().equals("")) {
            criteria.and("name").regex(partDtoForSearch.getPartName());
        }

        if (partDtoForSearch.getPartCode() != null && !partDtoForSearch.getPartCode().equals("")) {
            criteria.and("partCode").regex(partDtoForSearch.getPartCode());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                unwind("assignedToGroupList", true),
                match(criteria1)
        );
        return mongoOperations.aggregate(aggregation, Part.class, PartGetAllDTO.class).getMappedResults().size();
    }

    @Override
    public List<Part> getUsedPartInWorkOrder(List<String> partIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(partIdList));
        query.fields()
                .include("id")
                .include("name")
                .include("partCode");
        return mongoOperations.find(query, Part.class);
    }
}