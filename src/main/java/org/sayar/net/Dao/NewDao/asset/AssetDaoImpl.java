package org.sayar.net.Dao.NewDao.asset;

import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.sayar.net.Dao.NewDao.asset.aggregationResult.ParentAndSubActivityId;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Asset.*;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.newModel.Asset.Logs.AssetConsumablePartLog;
import org.sayar.net.Model.newModel.Asset.Logs.AssetRepairSchedulingLog;
import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.Enum.StatusOfAsset;
import org.sayar.net.Model.newModel.Image;
import org.sayar.net.Model.newModel.Node.Node;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.sayar.net.Model.newModel.metering.model.Metering;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.sayar.net.Model.newModel.CategoryType.BUILDING;
import static org.sayar.net.Model.newModel.CategoryType.FACILITY;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository("assetDaoImpl")
@Transactional

public class AssetDaoImpl extends GeneralDaoImpl<Asset> implements AssetDao {
    @Autowired
    private MongoOperations mongoOperations;


    @Autowired
    public AssetDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public List<AssetDTO> getAllByFilterAndPagination(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("_id")
                .andExpression("name").as("name")
                .andExpression("code").as("code")
                .andExpression("status").as("status")
                .andExpression("assetTemplateId").as("assetTemplateId")
                .andExpression("categoryId").as("categoryId")
                .andExpression("assetPriority").as("assetPriority")
                .andExpression("categoryType").as("categoryType");

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("deleted").ne(true));

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.ACTIVE)) {
            criteriaList.add(Criteria.where("status").is(true));
        }

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.INACTIVE)) {
            criteriaList.add(Criteria.where("status").is(false));
        }

        if (assetDTO.getCategoryId() != null && !assetDTO.getCategoryId().equals("")) {
            criteriaList.add(Criteria.where("categoryId").is(assetDTO.getCategoryId()));
        }

//        if (assetDTO.getStatus().equals(AssetStatus.ALL)) {
//            criteriaList.add(Criteria.where("status").is(false).orOperator(
//                    Criteria.where("status").is(true)
//            ));
//        }

        if (assetDTO.getAssetTemplateId() != null && !assetDTO.getAssetTemplateId().equals(""))
            criteriaList.add(Criteria.where("assetTemplateId").regex(assetDTO.getAssetTemplateId()));

        if (assetDTO.getCategoryType() != null)
            criteriaList.add(Criteria.where("categoryType").is(assetDTO.getCategoryType()));

        if (assetDTO.getName() != null && !assetDTO.getName().equals(""))
            criteriaList.add(Criteria.where("name").regex(assetDTO.getName()));

        if (assetDTO.getCode() != null && !assetDTO.getCode().equals(""))
            criteriaList.add(Criteria.where("code").regex(assetDTO.getCode()));

        if (assetDTO.getAssetPriority() != null)
            criteriaList.add(Criteria.where("assetPriority").is(assetDTO.getAssetPriority()));

        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
        Aggregation agg = newAggregation(
                match(criteria)
                , projectionOperation
                , skipOperation
                , limitOperation
        );
        return mongoOperations.aggregate(agg, Asset.class, AssetDTO.class).getMappedResults();
    }

    @Override
    public Asset postAsset(Asset asset) {
        return mongoOperations.save(asset);
    }

    @Override
    public long countAllAssets(AssetSearchDTO assetDTO) {
        Criteria criteria;
        List<Criteria> criteriaList = new ArrayList<>();

        criteriaList.add(Criteria.where("deleted").ne(true));

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.ACTIVE)) {
            criteriaList.add(Criteria.where("status").is(true));
        }

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.INACTIVE)) {
            criteriaList.add(Criteria.where("status").is(false));
        }

        if (assetDTO.getAssetTemplateId() != null && !assetDTO.getAssetTemplateId().equals(""))
            criteriaList.add(Criteria.where("assetTemplateId").regex(assetDTO.getAssetTemplateId()));

        if (assetDTO.getCategoryType() != null)
            criteriaList.add(Criteria.where("categoryType").is(assetDTO.getCategoryType()));

        if (assetDTO.getName() != null && !assetDTO.getName().equals(""))
            criteriaList.add(Criteria.where("name").regex(assetDTO.getName()));

        if (assetDTO.getCode() != null && !assetDTO.getCode().equals(""))
            criteriaList.add(Criteria.where("code").regex(assetDTO.getCode()));

        if (assetDTO.getAssetTemplateId() != null && !assetDTO.getAssetTemplateId().equals(""))
            criteriaList.add(Criteria.where("assetTemplateId").is(assetDTO.getAssetTemplateId()));

        if (assetDTO.getAssetPriority() != null)
            criteriaList.add(Criteria.where("assetPriority").is(assetDTO.getAssetPriority()));

        if (assetDTO.getCategoryId() != null && !assetDTO.getCategoryId().equals(""))
            criteriaList.add(Criteria.where("categoryId").is(assetDTO.getCategoryId()));


        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
        return mongoOperations.count(Query.query(criteria), Asset.class);
    }

    @Override
    public Asset updateAsset(Asset asset) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(asset.getId())
                .andOperator(Criteria.where("deleted").ne(true)));
        Update update = new Update();
        update.set("name", asset.getName());
        update.set("description", asset.getDescription());
        update.set("assetTemplateId", asset.getAssetTemplateId());
        update.set("image", asset.getImage());
        update.set("status", asset.getStatus());
        update.set("code", asset.getCode());
        update.set("activityIdList", asset.getActivityIdList());
        update.set("categoryType", asset.getCategoryType());
        update.set("isPartOfAsset", asset.getIsPartOfAsset());
        update.set("rootId", asset.getRootId());
        update.set("firstParentBuilding", asset.getFirstParentBuilding());
        update.set("assetPriority", asset.getAssetPriority());
        update.set("categoryId", asset.getCategoryId());
        return mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Asset.class);
    }

    @Override
    public boolean checkIfCodeExists(String assetCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(assetCode));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public UpdateResult updateAssetBasicInformation(String assetId, AssetBasicInformation assetBasicInformation) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(assetId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        Update update = new Update();
        update.set("unitIdList", assetBasicInformation.getUnitIdList());
        update.set("note", assetBasicInformation.getNote());
        update.set("budgetId", assetBasicInformation.getBudgetId());
        update.set("chargeDepartmentId", assetBasicInformation.getChargeDepartmentId());
        update.set("address", assetBasicInformation.getAddress());
        return mongoOperations.updateFirst(query, update, Asset.class);
    }

    @Override
    public AssetBasicInformation getOneByAssetId(String assetId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(assetId);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                unwind("unitIdList", true),
                project()
                        .and("id").as("id")
                        .and("note").as("note")
                        .and("address.id").as("address.id")
                        .and("address.description").as("address.description")
                        .and("address.location").as("address.location")
                        .and(ConvertOperators.ToObjectId.toObjectId("$unitIdList")).as("unitIdList")
                        .and(ConvertOperators.ToObjectId.toObjectId("$chargeDepartmentId")).as("chargeDepartmentId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$budgetId")).as("budgetId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$address.cityId")).as("address.cityId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$address.provinceId")).as("address.provinceId"),
                Aggregation.group("id")
                        .first("note").as("note")
                        .first("address.id").as("addressId")
                        .first("address.description").as("addressDescription")
                        .first("address.location").as("addressLocation")
                        .first("address.cityId").as("cityId")
                        .first("address.provinceId").as("provinceId")
                        .first("chargeDepartmentId").as("chargeDepartmentId")
                        .first("budgetId").as("budgetId")
                        .push("unitIdList").as("unitIdList"),
//                        .push(ConditionalOperators.IfNull.ifNull("unitIdList")).as("unitIdList"),
                lookup("unitOfMeasurement", "unitIdList", "_id", "unitOfMeasurement"),
                lookup("budget", "budgetId", "_id", "budget"),
                lookup("chargeDepartment", "chargeDepartmentId", "_id", "chargeDepartment"),
                lookup("city", "cityId", "_id", "city"),
                lookup("province", "provinceId", "_id", "province"),
                project()
                        .and("note").as("note")
                        .and("unitIdList").as("unitIdList")
                        .and("unitOfMeasurement").as("unitOfMeasurementName")
                        .and("addressId").as("address.id")
                        .and("addressDescription").as("address.description")
                        .and("addressLocation").as("address.location")
                        .and("budget._id").as("budgetId")
                        .and("budget.title").as("budgetName")
                        .and("chargeDepartment._id").as("chargeDepartmentId")
                        .and("chargeDepartment.title").as("chargeDepartmentName")
                        .and("city._id").as("address.cityId")
                        .and("city.name").as("address.cityName")
                        .and("province._id").as("address.provinceId")
                        .and("province.name").as("address.provinceName")
        );
        return mongoOperations.aggregate(aggregation, Asset.class, AssetBasicInformation.class).getUniqueMappedResult();
    }

    @Override
    public boolean deleteCreateAsset(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(assetId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("name")
                .include("assetTemplateId")
                .include("status")
                .include("code")
                .include("image")
                .include("deleted");
        Update update = new Update();
        update.set("deleted", true);
        mongoOperations.updateFirst(query, update, Asset.class);
        return true;
    }

    @Override
    public UpdateResult updateCreateAsset(CreateAsset createAsset) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(createAsset.getId()));
        query.addCriteria(Criteria.where("deleted").ne(true));
        Update update = new Update();
        update.set("name", createAsset.getName());
        update.set("status", createAsset.isStatus());
        update.set("code", createAsset.getCode());
        update.set("image", createAsset.getImage());
        return mongoOperations.updateFirst(query, update, Asset.class);
    }

    @Override
    public Asset getAllCompanyByAssetId(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields().include("companyList");
        Print.print(mongoOperations.find(query, Asset.class));
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getAllUsersByAssetId(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields().include("users");
        Print.print(mongoOperations.find(query, Asset.class));
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getAssetByAssetId(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(assetId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("assetTemplateId");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public List<Asset> getAllWarrantyByAssetId(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(assetId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("warranties");
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public Asset getAllMeteringByAssetId(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(assetId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getAllPartByAssetId(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(assetId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("parts");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public boolean updatePartListIdByAssetId(String assetId, List<String> partsId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));

        Update update = new Update();
        update.set("parts", partsId);

        mongoOperations.updateFirst(query, update, Asset.class);
        return true;
    }

    @Override
    public boolean updateCompanyListByAssetId(String assetId, List<String> companyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));

        Update update = new Update();
        update.set("companyList", companyId);

        mongoOperations.updateFirst(query, update, Asset.class);
        return true;

    }

    @Override
    public UpdateResult updatePropertyListByAssetId(String assetId, List<Property> propertyList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        Update update = new Update();
        update.set("propertyList", propertyList);
        return mongoOperations.updateFirst(query, update, Asset.class);
    }

    @Override
    public boolean updateUserListByAssetId(String assetId, List<String> usersId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));

        Update update = new Update();
        update.set("users", usersId);

        mongoOperations.updateFirst(query, update, Asset.class);
        return true;
    }

    @Override
    public UpdateResult updateWarrantyListByAssetId(String assetId, List<String> warrantyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));

        Update update = new Update();
        update.set("warranties", warrantyId);

        return mongoOperations.updateFirst(query, update, Asset.class);
    }

    @Override
    public UpdateResult updateMeteringListByAssetId(String assetId, List<String> meteringId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        Update update = new Update();
        update.set("meterings", meteringId);
        return mongoOperations.updateFirst(query, update, Asset.class);

    }

    @Override
    public Asset getOneAsset(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(assetId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getOneAssetName(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(assetId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("name");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getUserByAssetIdList(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields().include("users");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getPartByAssetIdList(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields().include("parts");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getPropertyListByAssetId(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields().include("propertyList");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getDocumentListByAssetId(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields().include("documentList");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public UpdateResult updateDocumentListByAssetId(String assetId, List<Document> documents) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields().include("documentList");

        Update update = new Update();
        update.set("documentList", documents);
        return mongoOperations.updateFirst(query, update, Asset.class);

    }

    @Override
    public List<Asset> getAllAssetByCategoryType(CategoryType categoryType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("categoryType").is(categoryType));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getAllNameAndCode() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("name")
                .include("categoryType")
                .include("id")
                .include("code");
        return mongoOperations.find(query, Asset.class);
    }

//    @Override
//    public Asset getAllAssetByAssetId(String assetId) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("deleted").ne(true));
//        query.addCriteria(Criteria.where("id").is(assetId));
//        return mongoOperations.findOne(query, Asset.class);
//    }

    @Override
    public List<Asset> getAllAsset() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("categoryType").is(FACILITY));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getAssetByAssetIdList(List<String> assetIdListForSendingNotification) {
        Print.print("input", assetIdListForSendingNotification);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(assetIdListForSendingNotification));
        query.fields()
                .include("name")
                .include("id")
                .include("users");
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getAllAssetByAssetIdList(List<String> assetIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(assetIdList));
        query.fields()
                .include("name")
                .include("id");
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public long countAllOfflineAssets() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(false));
        return mongoOperations.count(query, Asset.class);
    }

    @Override
    public long countAllOnlineAssets() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(true));
        return mongoOperations.count(query, Asset.class);
    }

    @Override
    public Page<Asset> getAllAssetForBOM(Pageable pageable, Integer totalElement) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.with(pageable);
        query.fields()
                .include("id")
                .include("name")
                .include("code")
                .include("isPartOfAsset")
                .include("categoryType");
        List<Asset> assetList = mongoOperations.find(query, Asset.class);
        long total = assetList.size();
        return new PageImpl<>(assetList, pageable, total);
    }

    @Override
    public List<AssetDTO> getAllAssetOfAssignedToUserByUserId(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        Criteria criteria1 = new Criteria();

        criteria.and("deleted").ne(true);

        if (assetDTO.getUserId() != null && !assetDTO.getUserId().equals("")) {
            criteria1.and("assignedToPersonList.userId").is(assetDTO.getUserId());
        }

        if (assetDTO.getName() != null && !assetDTO.getName().equals("")) {
            criteria.and("name").regex(assetDTO.getName());
        }

        if (assetDTO.getCode() != null && !assetDTO.getCode().equals("")) {
            criteria.and("code").regex(assetDTO.getCode());
        }

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.ACTIVE)) {
            criteria.and("status").is(true);
        }

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.INACTIVE)) {
            criteria.and("status").is(false);
        }

        if (assetDTO.getAssetTemplateId() != null && !assetDTO.getAssetTemplateId().equals("")) {
            criteria.and("assetTemplateId").is(assetDTO.getAssetTemplateId());
        }

        if (assetDTO.getCategoryType() != null) {
            criteria.and("categoryType").is(assetDTO.getCategoryType());
        }

        if (assetDTO.getParentLocationId() != null && !assetDTO.getParentLocationId().equals("")) {
            criteria.and("firstParentBuilding").is(assetDTO.getParentLocationId());
        }

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("assetTemplate")
                .localField("assetTemplateObjId")
                .foreignField("_id")
                .as("assetTemplate");

        LookupOperation lookupOperation2 = LookupOperation.newLookup()
                .from("asset")
                .localField("firstParentBuilding")
                .foreignField("_id")
                .as("asset");

        Aggregation aggregation = newAggregation(
                match(criteria),
                unwind("assignedToPersonList", true),
                match(criteria1),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("status").as("status")
                        .and("categoryType").as("categoryType")
                        .and("assignedToPersonList").as("assignedToPersonList")
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetTemplateId"))).as("assetTemplateObjId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$firstParentBuilding"))).as("firstParentBuilding"),
                lookupOperation,
                lookupOperation2,
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("status").as("status")
                        .and("categoryType").as("categoryType")
                        .and("assetTemplate._id").as("assetTemplateId")
                        .and("assetTemplate.name").as("assetTemplateName")
                        .and("asset._id").as("parentLocationId")
                        .and("asset.name").as("parentLocationName"),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Asset.class, AssetDTO.class).getMappedResults();
    }

    @Override
    public List<InDependentAssetDTO> getAllIndependentAssets(Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.orOperator(
                Criteria.where("isPartOfAsset").is(null),
                Criteria.where("isPartOfAsset").is("")
        );

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("status").as("status")
                        .and("hasChild").as("hasChild")
                        .and("categoryType").as("categoryType")
                        .and("assetPriority").as("assetPriority")
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetTemplateId"))).as("assetTemplateIdObj")
                        .and((ConvertOperators.ToObjectId.toObjectId("$categoryId"))).as("categoryId"),
                lookup("assetTemplate", "assetTemplateIdObj", "_id", "assetTemplate"),
                lookup("category", "categoryId", "_id", "category"),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("status").as("status")
                        .and("hasChild").as("hasChild")
                        .and("categoryType").as("categoryType")
                        .and("assetTemplate._id").as("assetTemplateId")
                        .and("assetTemplate.name").as("assetTemplateName")
                        .and("category._id").as("categoryId")
                        .and("category.title").as("categoryTitle")
                        .and("assetPriority").as("assetPriority"),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Asset.class, InDependentAssetDTO.class).getMappedResults();
    }

    @Override
    public List<AssetDTOForChildren> getAllChildrenOfAUser(String parentId) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("isPartOfAsset").is(parentId);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("status").as("status")
                        .and("categoryType").as("categoryType")
                        .and("hasChild").as("hasChild")
                        .and("isPartOfAsset").as("isPartOfAsset")
                        .and("assetPriority").as("assetPriority")
                        .and(ConvertOperators.ToObjectId.toObjectId("$assetTemplateId")).as("assetTemplateId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$categoryId")).as("categoryId"),
                lookup("assetTemplate", "assetTemplateId", "_id", "assetTemplate"),
                lookup("category", "categoryId", "_id", "category"),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("status").as("status")
                        .and("hasChild").as("hasChild")
                        .and("categoryType").as("categoryType")
                        .and("assetTemplate._id").as("assetTemplateId")
                        .and("assetTemplate.name").as("assetTemplateName")
                        .and("category._id").as("categoryId")
                        .and("category.title").as("categoryTitle")
                        .and("assetPriority").as("assetPriority")
        );
        return mongoOperations.aggregate(aggregation, Asset.class, AssetDTOForChildren.class).getMappedResults();
    }

    @Override
    public List<Asset> getAssetsByCategoryType(String categoryType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("categoryType").is(categoryType));
        query.addCriteria(new Criteria().orOperator(
//                Criteria.where("categoryType").is(categoryType),
                Criteria.where("isPartOfAsset").is(null),
                Criteria.where("isPartOfAsset").is("")
        ));
        query.fields()
                .include("id")
                .include("name")
                .include("code")
                .include("rootId")
                .include("hasChild")
                .include("categoryType")
                .include("isPartOfAsset")
                .include("status");
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public void makeParentHasChildTrue(String isPartOfAsset) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(isPartOfAsset));
        Update update = new Update();
        update.set("hasChild", true);
        mongoOperations.updateFirst(query, update, Asset.class);
    }


    @Override
    public void getParentAssetAndMakeChildHasFalse(String isPartOfAsset) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(isPartOfAsset));
        Update update = new Update();
        update.set("hasChild", false);
        mongoOperations.updateFirst(query, update, Asset.class);
    }

    @Override
    public Asset getAssetByWorkOrderAssetId(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getParentAssetByIsPartOfId(String isPartOfAsset) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(isPartOfAsset));
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public void checkIfOldAssetIsParent(Asset parentAssetOfOldAsset) {
        Print.print("XXXXXX", parentAssetOfOldAsset);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("isPartOfAsset").is(parentAssetOfOldAsset.getId()));
        boolean exist = mongoOperations.exists(query, Asset.class);

        if (!exist) {
            parentAssetOfOldAsset.setHasChild(false);
            mongoOperations.save(parentAssetOfOldAsset);
        } else {
            parentAssetOfOldAsset.setHasChild(true);
            mongoOperations.save(parentAssetOfOldAsset);
        }
        Print.print("parentAssetOfOldAssettttt", parentAssetOfOldAsset);
    }

    @Override
    public void checkIfNewAssetIsParent(Asset newAsset1) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("isPartOfAsset").is(newAsset1.getId()));
        boolean exist = mongoOperations.exists(query, Asset.class);
        if (!exist) {
            newAsset1.setHasChild(false);
            mongoOperations.save(newAsset1);
        } else {
            newAsset1.setHasChild(true);
            mongoOperations.save(newAsset1);
        }
        Print.print("22222", newAsset1);

    }

    @Override
    public Asset MakeParentOfNewAssetHasChildTrue(String isPartOfAsset) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(isPartOfAsset));
        Update update = new Update();
        update.set("hasChild", true);
        FindAndModifyOptions options = FindAndModifyOptions.options();
        options.returnNew(true);
        return mongoOperations.findAndModify(query, update, options, Asset.class);
    }

    @Override
    public boolean ifDeletedAssetHasChild(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("isPartOfAsset").is(id));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public void ifDeletedAssetParentIsStillParen(Asset deletedAssetParent) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("isPartOfAsset").is(deletedAssetParent.getId()));
        boolean exist = mongoOperations.exists(query, Asset.class);
        if (!exist) {
            deletedAssetParent.setHasChild(false);
            mongoOperations.save(deletedAssetParent);
        }
    }

    @Override
    public List<String> getParentsOfAsset(Asset asset) {
        List<String> parentAssetIdList = new ArrayList<>();
        returnMethodToFindParentAsset(asset, parentAssetIdList);
        Collections.reverse(parentAssetIdList);
        return parentAssetIdList;
    }

    @Override
    public List<Asset> getAllAssetsThatHaveStorage(List<String> assetIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(assetIdList));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getAllAssetsOfStorageList(List<String> storageListAssetId) {
        Print.print("storageListAssetId", storageListAssetId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(storageListAssetId));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getAssetsByAssetIdListOfWorkOrders(List<String> workOrdersAssetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(workOrdersAssetId));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public Asset getAssetOfScheduleMaintenanceByAssetId(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getAssetOfMetering(String referenceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(referenceId));
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public boolean checkAssetCodeIsUnique(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(code));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public Asset getAssetOfScheduleByScheduleAssetId(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public boolean checkIfUserExistsAsset(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("users").is(userId));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public List<Asset> getAssetNameOfWorkRequest(List<String> assetIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(assetIdList));
        query.fields()
                .include("name")
                .include("id");
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public Asset getAssetNameAndCodeOfTheWorkOrder(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields()
                .include("name")
                .include("code");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public List<Asset> newGetAllRootFacility() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("CategoryType").is(FACILITY));
        query.fields()
                .include("id")
                .include("code")
                .include("name")
                .include("status")
                .include("isPartOfAsset")
                .include("rootId")
                .include("hasChild")
                .include("categoryType");
        List<Asset> assetList = mongoOperations.find(query, Asset.class);
        List<Asset> consideredAssets = new ArrayList<>();
        List<String> rootIdList = new ArrayList<>();
        assetList.forEach(asset -> {
            if (asset.getRootId() != null && asset.getRootId().equals("ROOT")) {
                consideredAssets.add(asset);
            } else {
                rootIdList.add(asset.getRootId());
            }
        });
        List<Asset> rootAssetList = this.getRootAsset(rootIdList);
        consideredAssets.addAll(rootAssetList);
        return consideredAssets;
    }

    @Override
    public List<Asset> getAllRooAsset() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("CategoryType").is(FACILITY));
        query.addCriteria(Criteria.where("rootId").is("ROOT"));
        query.fields()
                .include("id")
                .include("code")
                .include("name");
        return mongoOperations.find(query, Asset.class);
    }

    private List<Asset> getRootAsset(List<String> rootIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(rootIdList));
        query.addCriteria(Criteria.where("CategoryType").is(BUILDING));
        query.fields()
                .include("id")
                .include("code")
                .include("name")
                .include("status")
                .include("isPartOfAsset")
                .include("categoryType")
                .include("hasChild");
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public Asset getParentOfCreatedAsset(String isPartOfAsset) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(isPartOfAsset));
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset findAssetOfWorkRequest(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields().include("activityId");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getActivitiesOfAsset(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields().include("activityIdList");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getRelevantAssetOfTheSchedule(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields().include("activityIdList");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public List<Activity> getActivityListOfTheAsset(List<String> activityIdList) {
        Print.print("222222", activityIdList);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(activityIdList));
        query.fields()
                .include("id")
                .include("title");
        return mongoOperations.find(query, Activity.class);
    }

    @Override
    public void makeRootIdAsRoot(Asset createdAsset) {
        createdAsset.setRootId("ROOT");
        mongoOperations.save(createdAsset);
    }

    @Override
    public Asset findFaultyAsset(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields()
                .include("id")
                .include("code")
                .include("name");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public List<Asset> getAllRelevantAssetOfWorkRequest(List<String> assetIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(assetIdList));
        query.fields()
                .include("id")
                .include("name")
                .include("code");
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public Asset getAllUnitOfMeasurementOfTheAsset(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields()
                .include("unitIdList");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public boolean ifProvinceExistsInAsset(String provinceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("address.province.id").is(provinceId));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public boolean ifCityExistsInAsset(String cityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("address.city.id").is(cityId));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public boolean ifBudgetExistsInAsset(String budgetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("budgetId").is(budgetId));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public boolean ifUnitExistsInAsset(String unitOfMeasurementId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("unitIdList").is(unitOfMeasurementId));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public boolean ifPropertyExistsInAsset(String propertyId) {
        Print.print("propertyId", propertyId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("propertyList").elemMatch(Criteria.where("id").is(propertyId)));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public boolean ifCompanyExistsInAsset(String companyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("companyList").is(companyId));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public boolean ifChargeDepartmentExistsInAsset(String chargeDepartmentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("chargeDepartmentId").is(chargeDepartmentId));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public boolean ifAssetTemplateExistsInAsset(String assetTemplateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetTemplateId").is(assetTemplateId));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public void makeRootId(String id, Asset createdAsset) {
        createdAsset.setRootId(id);
        mongoOperations.save(createdAsset);
    }

    @Override
    public void makeNewVersionAssetAsRoot(Asset newVersionAsset) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(newVersionAsset.getId()));
        Update update = new Update();
        update.set("rootId", "ROOT");
        mongoOperations.updateFirst(query, update, Asset.class);
    }

    @Override
    public void fillRootIdOfNewVersionAsset(Asset newVersionAsset, String root) {

        if (!root.equals("ROOT")) {
            newVersionAsset.setRootId(root);
            Print.print("ppppppp", newVersionAsset);
            mongoOperations.save(newVersionAsset);
        } else {
            newVersionAsset.setRootId(newVersionAsset.getIsPartOfAsset());
            mongoOperations.save(newVersionAsset);
            Print.print("ggggggg", newVersionAsset);
        }
    }

    @Override
    public List<Asset> getFirstLayerChildrenOfNewVersionAsset(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("isPartOfAsset").in(id));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public void getNextLayerOfNewVersionAssetChildren(List<Asset> firstLayerChildrenOfTheNewVersionAsset, List<Asset> childrenOfTheNewVersionAsset) {
        List<String> firstLayerChildrenAssetId = new ArrayList<>();
        firstLayerChildrenOfTheNewVersionAsset.forEach(asset -> firstLayerChildrenAssetId.add(asset.getId()));
        Print.print("firstLayerChildrenAssetId", firstLayerChildrenAssetId);
        List<Asset> thisLayerChildAssets = this.findChildAssetsOfThisLayer(firstLayerChildrenAssetId);
        Print.print("thisLayerChildAssets", thisLayerChildAssets);
        childrenOfTheNewVersionAsset.addAll(thisLayerChildAssets);
//        getNextLayerOfNewVersionAssetChildren(thisLayerChildAssets, childrenOfTheNewVersionAsset);
    }

    private List<Asset> findChildAssetsOfThisLayer(List<String> thisLayerAssetIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("isPartOfAsset").in(thisLayerAssetIds));
//        Print.print("result2121", mongoOperations.find(query, Asset.class));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public void updateTheRootIdsOfChildrenAssetOfNewVersionAsset(List<Asset> childrenOfTheNewVersionAsset, Asset newVersionAsset) {

        for (Asset asset : childrenOfTheNewVersionAsset) {
            if (newVersionAsset.getRootId().equals("ROOT")) {
                asset.setRootId(newVersionAsset.getId());
                mongoOperations.save(asset);
            } else {
                asset.setRootId(newVersionAsset.getRootId());
                mongoOperations.save(asset);
            }
        }
        Print.print("ppppppppppp", childrenOfTheNewVersionAsset);
    }

    @Override
    public List<Asset> getAssetOfWorkOrder(List<String> assetIdListOfWorkOrder) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(assetIdListOfWorkOrder));
        query.fields()
                .include("id")
                .include("name")
                .include("code");
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getAllAssetsOfUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("users").is(userId));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getParentAssets(List<String> parentAssetIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(parentAssetIdList));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getOnlineAssets() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(true));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getOfflineAssets() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(false));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getAssetsName(List<String> assetIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(assetIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getUserOnlineAsset(String assignedUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("users").is(assignedUserId));
        query.addCriteria(Criteria.where("status").is(true));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public long countUserOnlineAsset(String userAssignedId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("users").is(userAssignedId));
        query.addCriteria(Criteria.where("status").is(true));
        return mongoOperations.count(query, Asset.class);
    }

    @Override
    public List<Asset> getUserOfflineAsset(String userAssignedId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("users").is(userAssignedId));
        query.addCriteria(Criteria.where("status").is(false));
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public long countUserOfflineAsset(String userAssignedId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("users").is(userAssignedId));
        query.addCriteria(Criteria.where("status").is(false));
        return mongoOperations.count(query, Asset.class);
    }

    @Override
    public Asset getAssetWorkRequest(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields()
                .include("id")
                .include("name")
                .include("code");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public boolean ifActivityExistInAsset(String activityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityIdList").is(activityId));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public long countAllAssetOfAssignedToUserByUserId(AssetSearchDTO assetDTO) {

        Criteria criteria = new Criteria();
        Criteria criteria1 = new Criteria();

        criteria.and("deleted").ne(true);
        if (assetDTO.getUserId() != null && !assetDTO.getUserId().equals("")) {
            criteria1.and("assignedToPersonList.userId").is(assetDTO.getUserId());
        }

        if (assetDTO.getName() != null && !assetDTO.getName().equals("")) {
            criteria.and("name").regex(assetDTO.getName());
        }

        if (assetDTO.getCode() != null && !assetDTO.getCode().equals("")) {
            criteria.and("code").regex(assetDTO.getCode());
        }

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.ACTIVE)) {
            criteria.and("status").is(true);
        }

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.INACTIVE)) {
            criteria.and("status").is(false);
        }

        if (assetDTO.getAssetTemplateId() != null && !assetDTO.getAssetTemplateId().equals("")) {
            criteria.and("assetTemplateId").is(assetDTO.getAssetTemplateId());
        }

        if (assetDTO.getCategoryType() != null) {
            criteria.and("categoryType").is(assetDTO.getCategoryType());
        }

        if (assetDTO.getParentLocationId() != null && !assetDTO.getParentLocationId().equals("")) {
            criteria.and("firstParentBuilding").is(assetDTO.getParentLocationId());
        }

        Aggregation aggregation = newAggregation(
                match(criteria),
                unwind("assignedToPersonList", true),
                match(criteria1)
        );
        return mongoOperations.aggregate(aggregation, Asset.class, UserAssetFilterDTO.class).getMappedResults().size();
    }

    @Override
    public Asset getPersonsOfAsset(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields()
                .include("assignedToPersonList");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getGroupPersonnelOfAsset(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields()
                .include("assignedToGroupList");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public boolean addPersonTypePersonnel(String assetId, List<AssignedToPerson> assignedToPersonList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        Update update = new Update();
        update.set("assignedToPersonList", assignedToPersonList);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, Asset.class);
        return updateResult.getModifiedCount() > 0;
    }

    @Override
    public boolean addGroupTypePersonnel(String assetId, List<AssignedToGroup> assignedToGroupList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        Update update = new Update();
        update.set("assignedToGroupList", assignedToGroupList);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, Asset.class);
        return updateResult.getModifiedCount() > 0;
    }

    @Override
    public boolean checkIfAssetTemplateUsedInAsset(String assetTemplateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetTemplateId").is(assetTemplateId));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public boolean deleteChildAssets(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.addCriteria(Criteria.where("hasChild").is(true));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public long countIndependentAssets(Pageable pageable, Integer totalElement) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.orOperator(
                Criteria.where("isPartOfAsset").is(null),
                Criteria.where("isPartOfAsset").is("")
        );

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Asset.class, Asset.class).getMappedResults().size();
    }

    @Override
    public List<AssetNameAndId> getAllAssetByTerm(String term, Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("name").is(term);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("name").as("name"),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Asset.class, AssetNameAndId.class).getMappedResults();
    }

    @Override
    public String getParentBuildingAsset(String isPartOfAsset) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(isPartOfAsset));
        Asset parentAsset = mongoOperations.findOne(query, Asset.class);
        if (parentAsset.getCategoryType() == BUILDING) {
            return parentAsset.getId();
        } else {
            return null;
        }
//        else if (parentAsset.getCategoryType() != BUILDING && parentAsset.getIsPartOfAsset() != null && !parentAsset.getIsPartOfAsset().equals("")) {
//            return getParentBuildingAsset(parentAsset.getIsPartOfAsset());
//        } else {
//            return null;
//        }
    }

    @Override
    public void updateAssetChildren(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("isPartOfAsset").in(id));
        List<Asset> children = mongoOperations.find(query, Asset.class);
        List<Asset> aggregateChildAssets = new ArrayList<>(children);
        children.forEach(child -> {
            if (child.getIsPartOfAsset() != null && !child.getIsPartOfAsset().equals("")) {
                updateAssetChildren(child.getId());
            }
        });
    }

    @Override
    public long countAllAssetsByTerm(String term) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("name").is(term);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Asset.class, AssetNameAndId.class).getMappedResults().size();
    }

    @Override
    public List<AssetDTO> getAllAssignedAssetsOfGroup(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        Criteria criteria1 = new Criteria();

        criteria.and("deleted").ne(true);

        if (assetDTO.getUserTypeId() != null && !assetDTO.getUserTypeId().equals("")) {
            criteria1.and("assignedToGroupList.userTypeId").is(assetDTO.getUserTypeId());
        }

        if (assetDTO.getName() != null && !assetDTO.getName().equals("")) {
            criteria.and("name").regex(assetDTO.getName());
        }

        if (assetDTO.getCode() != null && !assetDTO.getCode().equals("")) {
            criteria.and("code").regex(assetDTO.getCode());
        }

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.ACTIVE)) {
            criteria.and("status").is(true);
        }

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.INACTIVE)) {
            criteria.and("status").is(false);
        }

        if (assetDTO.getAssetTemplateId() != null && !assetDTO.getAssetTemplateId().equals("")) {
            criteria.and("assetTemplateId").is(assetDTO.getAssetTemplateId());
        }

        if (assetDTO.getCategoryType() != null) {
            criteria.and("categoryType").is(assetDTO.getCategoryType());
        }

        if (assetDTO.getParentLocationId() != null && !assetDTO.getParentLocationId().equals("")) {
            criteria.and("firstParentBuilding").is(assetDTO.getParentLocationId());
        }

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("assetTemplate")
                .localField("assetTemplateObjId")
                .foreignField("_id")
                .as("assetTemplate");

        LookupOperation lookupOperation2 = LookupOperation.newLookup()
                .from("asset")
                .localField("firstParentBuilding")
                .foreignField("_id")
                .as("asset");

        Aggregation aggregation = newAggregation(
                match(criteria),
                unwind("assignedToGroupList", true),
                match(criteria1),
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("status").as("status")
                        .and("categoryType").as("categoryType")
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetTemplateId"))).as("assetTemplateObjId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$firstParentBuilding"))).as("firstParentBuilding"),
                lookupOperation,
                lookupOperation2,
                project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("code").as("code")
                        .and("status").as("status")
                        .and("categoryType").as("categoryType")
                        .and("assetTemplate._id").as("assetTemplateId")
                        .and("assetTemplate.name").as("assetTemplateName")
                        .and("asset._id").as("parentLocationId")
                        .and("asset.name").as("parentLocationName"),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Asset.class, AssetDTO.class).getMappedResults();
    }

    @Override
    public long countAllAssignedAssetsOfGroup(AssetSearchDTO assetDTO) {
        Criteria criteria = new Criteria();
        Criteria criteria1 = new Criteria();

        criteria.and("deleted").ne(true);

        if (assetDTO.getUserTypeId() != null && !assetDTO.getUserTypeId().equals("")) {
            criteria1.and("assignedToGroupList.userTypeId").is(assetDTO.getUserTypeId());
        }

        if (assetDTO.getName() != null && !assetDTO.getName().equals("")) {
            criteria.and("name").regex(assetDTO.getName());
        }

        if (assetDTO.getCode() != null && !assetDTO.getCode().equals("")) {
            criteria.and("code").regex(assetDTO.getCode());
        }

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.ACTIVE)) {
            criteria.and("status").is(true);
        }

        if (assetDTO.getStatus() != null && assetDTO.getStatus().equals(StatusOfAsset.INACTIVE)) {
            criteria.and("status").is(false);
        }

        if (assetDTO.getAssetTemplateId() != null && !assetDTO.getAssetTemplateId().equals("")) {
            criteria.and("assetTemplateId").is(assetDTO.getAssetTemplateId());
        }

        if (assetDTO.getCategoryType() != null) {
            criteria.and("categoryType").is(assetDTO.getCategoryType());
        }

        if (assetDTO.getParentLocationId() != null && !assetDTO.getParentLocationId().equals("")) {
            criteria.and("firstParentBuilding").is(assetDTO.getParentLocationId());
        }

        Aggregation aggregation = newAggregation(
                match(criteria),
                unwind("assignedToGroupList", true),
                match(criteria1)
        );
        return mongoOperations.aggregate(aggregation, Asset.class, AssetDTO.class).getMappedResults().size();
    }

    @Override
    public Asset getAssetName(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields()
                .include("name");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public Asset getParent(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields()
                .include("isPartOfAsset");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public boolean isAssetStillParent(String isPartOfAsset) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("isPartOfAsset").is(isPartOfAsset));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public void updateHasChild(String isPartOfAsset) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(isPartOfAsset));
        Update update = new Update();
        update.set("hasChild", false);
        mongoOperations.updateFirst(query, update, Asset.class);
    }

    @Override
    public boolean checkIfCategoryUsedInAsset(String categoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("categoryId").is(categoryId));
        return mongoOperations.exists(query, Asset.class);
    }

    @Override
    public List<Category> NewGetAllCategory() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, Category.class);
    }

    @Override
    public List<Asset> getAllAssetsName() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("id")
                .include("code")
                .include("name");
        return mongoOperations.find(query, Asset.class);
    }

    @Override
    public List<Asset> getAllFacility() {
        Aggregation aggregation = newAggregation(
                project()
                        .and(ConvertOperators.ToObjectId.toObjectId("$isPartOfAsset")).as("isPartOfAsset")
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("categoryType").as("categoryType")
                        .and("deleted").as("deleted")
                ,match(Criteria.where("deleted").ne(true))
                , lookup("asset", "isPartOfAsset", "_id", "asset")
                , project()
                        .and("id").as("id")
                        .and("name").as("name")
                        .and("categoryType").as("categoryType")
                        .and("asset.categoryType").as("parentCategoryType")
                        .and("asset.deleted").as("deleted")
                , match(
                        Criteria.where("parentCategoryType").ne(FACILITY)
                         .orOperator(  Criteria.where("isPartOfAsset").is(null))
                        .andOperator(
                                Criteria.where("deleted").ne(true),
                                Criteria.where("categoryType").is(FACILITY)
                        )
                ))
                ;
        return  mongoOperations.aggregate(aggregation, Asset.class, Asset.class).getMappedResults();

    }

    @Override
    public List<Activity> getListActivityListByAssetId(String assetId) {
        System.out.println("assrtId "+assetId);
        Query queryAsset = new Query();
        queryAsset.addCriteria(Criteria.where("id").is(new ObjectId(assetId)));
        queryAsset.addCriteria(Criteria.where("deleted").ne(true));
        Asset res= mongoOperations.findOne(queryAsset, Asset.class);

        Print.print("res",res.getActivityIdList());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(res.getActivityIdList()));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("id").include("title");
        return mongoOperations.find(query, Activity.class);
    }

    private void returnMethodToFindParentAsset(Asset asset, List<String> parentAssetIdList) {

        if (asset != null && asset.getIsPartOfAsset() != null) {
            String parentId = asset.getIsPartOfAsset();
            parentAssetIdList.add(parentId);
            Query query = new Query();
            query.addCriteria(Criteria.where("deleted").ne(true));
            query.addCriteria(Criteria.where("id").is(parentId));
            Asset parentAsset = mongoOperations.findOne(query, Asset.class);
            if (parentAsset != null) {
                returnMethodToFindParentAsset(parentAsset, parentAssetIdList);
            }
        }
    }

    private void getAllRootOfChildFacility(Asset asset1, List<Asset> rootFacilityAndBuildingList) {
        if (asset1 != null && asset1.getIsPartOfAsset() == null) {
            rootFacilityAndBuildingList.add(asset1);
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("deleted").ne(true));
            if (asset1 != null) {
                query.addCriteria(Criteria.where("id").is(asset1.getIsPartOfAsset()));
            }
            Asset consideredAsset = mongoOperations.findOne(query, Asset.class);
            if (!rootFacilityAndBuildingList.contains(consideredAsset)) {
                getAllRootOfChildFacility(consideredAsset, rootFacilityAndBuildingList);
            }
        }
    }

//    _____________________________________________________

    @Override
    public List<Asset> findAllInTreeNode() {
        return null;
    }

    @Override
    public List<Asset> getAssetByCategory(String catId) {
        return null;
    }

    @Override
    public List<AssetDTO> getCompressAllAsset() {
        ProjectionOperation projectionOperation =
                project()
                        .andExpression("id").as("id")
                        .andExpression("code").as("code")
                        .andExpression("title").as("title")
                        .andExpression("status").as("status");
        Aggregation aggregation = newAggregation(
                projectionOperation
        );

        AggregationResults<AssetDTO> results = this.aggregate(
                aggregation, "asset", AssetDTO.class
        );
        return results.getMappedResults();
    }

    @Override
    public List<Node> getTreeNodeBase() {
        return null;
    }

    @Override
    public List<Node> getTreeNodeLayer(String id) {
        return null;
    }

    @Override
    public List<Node> getTreeNodeBaseByCategory(String catId) {
        return null;
    }

    public static void main(String[] args) {
        List<String> students = new ArrayList<>();
        ArrayList<Double> grades = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        String newStudent;
        System.out.println("Enter student name {ENTER for finish}:");
        do {
            newStudent = in.nextLine();
            if (!newStudent.equals("")) {
                students.add(newStudent);
            }
        } while (!newStudent.equals(""));


        for (String student : students) {
            System.out.println("Enter grade for " + student + ": ");
            Double grade = in.nextDouble();
            grades.add(grade);

        }
        double sum = 0.0;
        for (int x = 0; x < students.size(); x++) {
            System.out.println(students.get(x) + " {grade: " + grades.get(x) + "}");
            sum = grades.get(x);
        }

        double avg = sum / students.size();
        System.out.println("avg is " + avg);
    }

    @Override
    public List<Node> getTreeNodeLayerByCategory(String id, String catId) {
        return null;
    }

    @Override
    public List<Node> getTreeNodeSearch(String searchTerm) {
        return null;
    }

    @Override
    public List<Node> getTreeNodeSearchByCategory(String searchTerm, String catId) {
        return null;
    }

    @Override
    public List<Node> getAssetChildrenInTreeNode(String assetId) {
        return null;
    }

    @Override
    public List<Asset> getAssetForLocationCategory(String organId) {
        return null;
    }

    @Override
    public long getAssetForLocationCategoryCount(String organId) {
        System.out.println("location");
        return getAssetCategoryCount(organId, "");
    }

    @Override
    public long getAssetForEquipmentCategoryCount(String organId) {
        System.out.println("eq");
        return getAssetCategoryCount(organId, "");
    }

    @Override
    public long getAssetForToolsCategoryCount(String organId) {
        return getAssetCategoryCount(organId, "asdsad");
    }

    private long getAssetCategoryCount(String organId, String categoryBaseId) {
        return 1;
    }

    @Override
    public long getOrganAssetCount(String organId) {
        return 1;

    }

    @Override
    public Image getAssetImage(String assetId) {
        return null;
    }

    @Override
    public boolean detachImage(String assetId) {
        return true;
    }


    @Override
    public void addAssetConsumablePartLog(String assetId, String partId, String workOrderId, AssetConsumablePartLog log) {
    }

    @Override
    public void addAssetOnlineOfflineLog(String assetId, boolean online, String userId) {
    }

    @Override
    public void addAssetOpenWorkOrderLog(String assetId, String description) {
    }

    @Override
    public void addAssetRepairSchedulingLog(String assetId, AssetRepairSchedulingLog log) {
    }

    @Override
    public void addAssetWorkOrderDateLog(String assetId, String logDescription) {
    }

    @Override
    public void addAssetMeterReadingLog(String asset) {
    }

    @Override
    public void updateAssetMeterReadingLog(long amount, String meteringId) {
    }

    @Override
    public boolean assetHaveLogForMetering(String meteringId) {
        return false;
    }

    @Override
    public void addAssetSchedulteMaintananceLog(ScheduleMaintenance scheduleMaintanance, String userId) {

    }

    @Override
    public boolean getAssetStatus(String assetId) {
        return false;
    }

    @Override
    public double calculateMTTR(String assetId) {
        return 1;
    }

    @Override
    public double calculateMTBF(String assetId) {
        return 1;
    }

    @Override
    public List<AssetDTO> getByIdList(List<String> idList) {
        return null;
    }

    @Override
    public List<purchaseAssetDTO> getAssetDTOForPurchase() {
        return null;
    }

    @Override
    public ParentAndSubActivityId findOneByIdCategoriesIncluded(String assetId) {

        ProjectionOperation p = project()
                .andExpression("id").as("id")
                .andExpression("parentCategory.activityId").as("parentCategoryId")
                .andExpression("subCategory.activityId").as("subCategoryId");

        Aggregation aggregation = newAggregation(
                match(Criteria.where("_id").is(assetId))

                , lookup("category", "parentCategoryId", "_id", "parentCategory")
                , lookup("category", "subCategoryId", "_id", "subCategory")
                , unwind("parentCategory")
                , unwind("subCategory")
                , p
        );
        AggregationResults<ParentAndSubActivityId> results = this.aggregate(aggregation, "asset", ParentAndSubActivityId.class);
        return results.getUniqueMappedResult();

    }

    @Override
    public void updateMetering(String assetId, Metering metering) {
        System.out.println("assetId: " + assetId);
        Print.print(metering);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(assetId));
        Update update = new Update();
        update.inc(Asset.ME.meterAmount.toString(), metering.getAmount());
        update.push(Asset.ME.meterings.toString(), metering);
        this.updateFirst(query, update, Asset.class);
    }

    @Override
    public void incMeterAmount(Asset asset, int incAmount) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(asset.getId()));
        Update update = new Update();
        update.inc(Asset.ME.meterAmount.toString(), incAmount);
        this.updateFirst(query, update, Asset.class);
    }

    @Override
    public GetOneAgg getOne(String id) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("_id").is(id))
                , lookup("category", "parentCategoryId", "_id", "parentCategory")
                , lookup("category", "subCategoryId", "_id", "subCategory")
                , lookup("assetTemplate", "assetTemplate", "_id", "assetTemplate")
                , Aggregation.unwind("parentCategory")
                , Aggregation.unwind("subCategory")
                , Aggregation.unwind("assetTemplate")
        );


        AggregationResults<GetOneAgg> groupResults
                = this.aggregate(aggregation, "asset", GetOneAgg.class);
        if (groupResults.getMappedResults().size() > 0) return groupResults.getMappedResults().get(0);
        return null;
    }


    public class GetOneAgg {
        private String id;
        private String title;
        private String description;
        private List<Property> properties;
        private Category parentCategory;
        private Category subCategory;
        private AssetTemplate assetTemplate;
        private String orgId;
        private boolean status;
        private String chargeDepartmentId;
        private String budgetId;
        private Long budgetUsed;
        private List<String> businesses;//from company collection
        private List<String> backupCompanies;//from company collection
        private List<String> users;
        private String code;
        private String note;
        private String termsOfUse;
        private String maintenance;
        private String parent;//lookup
        private List<String> assets;
        private List<String> warranties;
        private List<String> purchases;
        private List<String> calibrations;//from company
        private List<Metering> meterings;//from company collection
        private Long minQuantity;
        private Long quantity;
        private Long creationTime;
        private long meterAmount;
        private Long lastWorkOrderDate;
        private UnitOfMeasurement unit;

        public GetOneAgg() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Property> getProperties() {
            return properties;
        }

        public void setProperties(List<Property> properties) {
            this.properties = properties;
        }

        public Category getParentCategory() {
            return parentCategory;
        }

        public void setParentCategory(Category parentCategory) {
            this.parentCategory = parentCategory;
        }

        public Category getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(Category subCategory) {
            this.subCategory = subCategory;
        }

        public AssetTemplate getAssetTemplate() {
            return assetTemplate;
        }

        public void setAssetTemplate(AssetTemplate assetTemplate) {
            this.assetTemplate = assetTemplate;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getChargeDepartmentId() {
            return chargeDepartmentId;
        }

        public void setChargeDepartmentId(String chargeDepartmentId) {
            this.chargeDepartmentId = chargeDepartmentId;
        }

        public String getBudgetId() {
            return budgetId;
        }

        public void setBudgetId(String budgetId) {
            this.budgetId = budgetId;
        }

        public Long getBudgetUsed() {
            return budgetUsed;
        }

        public void setBudgetUsed(Long budgetUsed) {
            this.budgetUsed = budgetUsed;
        }

        public List<String> getBusinesses() {
            return businesses;
        }

        public void setBusinesses(List<String> businesses) {
            this.businesses = businesses;
        }

        public List<String> getBackupCompanies() {
            return backupCompanies;
        }

        public void setBackupCompanies(List<String> backupCompanies) {
            this.backupCompanies = backupCompanies;
        }

        public List<String> getUsers() {
            return users;
        }

        public void setUsers(List<String> users) {
            this.users = users;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getTermsOfUse() {
            return termsOfUse;
        }

        public void setTermsOfUse(String termsOfUse) {
            this.termsOfUse = termsOfUse;
        }

        public String getMaintenance() {
            return maintenance;
        }

        public void setMaintenance(String maintenance) {
            this.maintenance = maintenance;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public List<String> getAssets() {
            return assets;
        }

        public void setAssets(List<String> assets) {
            this.assets = assets;
        }

        public List<String> getWarranties() {
            return warranties;
        }

        public void setWarranties(List<String> warranties) {
            this.warranties = warranties;
        }

        public List<String> getPurchases() {
            return purchases;
        }

        public void setPurchases(List<String> purchases) {
            this.purchases = purchases;
        }

        public List<String> getCalibrations() {
            return calibrations;
        }

        public void setCalibrations(List<String> calibrations) {
            this.calibrations = calibrations;
        }

        public List<Metering> getMeterings() {
            return meterings;
        }

        public void setMeterings(List<Metering> meterings) {
            this.meterings = meterings;
        }

        public Long getMinQuantity() {
            return minQuantity;
        }

        public void setMinQuantity(Long minQuantity) {
            this.minQuantity = minQuantity;
        }

        public Long getQuantity() {
            return quantity;
        }

        public void setQuantity(Long quantity) {
            this.quantity = quantity;
        }

        public Long getCreationTime() {
            return creationTime;
        }

        public void setCreationTime(Long creationTime) {
            this.creationTime = creationTime;
        }

        public long getMeterAmount() {
            return meterAmount;
        }

        public void setMeterAmount(long meterAmount) {
            this.meterAmount = meterAmount;
        }

        public Long getLastWorkOrderDate() {
            return lastWorkOrderDate;
        }

        public void setLastWorkOrderDate(Long lastWorkOrderDate) {
            this.lastWorkOrderDate = lastWorkOrderDate;
        }

        public UnitOfMeasurement getUnit() {
            return unit;
        }

        public void setUnit(UnitOfMeasurement unit) {
            this.unit = unit;
        }
    }

}
