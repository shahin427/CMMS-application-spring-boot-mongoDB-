package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Inventory;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class InventoryDaoImp extends GeneralDaoImpl implements InventoryDao {
    @Autowired
    private MongoOperations mongoOperations;

    public InventoryDaoImp(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public List<InventoryGetAllInPartDTO> getAllByPagination(Pageable pageable, Integer count, String partId) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("sameDocumentsDeleted").ne(true);
        criteria.and("deleted").ne(true);
        criteria.and("partId").is(partId);
        Aggregation aggregation = newAggregation(
                Aggregation.match(criteria)
                , Aggregation.sort(Sort.Direction.ASC, "creationDate")
                , Aggregation.group("row", "warehouse", "corridor", "inventoryLocationId")
                        .last("currentQuantity").as("currentQuantity")
                        .last("minQuantity").as("minQuantity")
                        .last("previousQuantity").as("previousQuantity")
                        .last("inventoryLocationId").as("inventoryLocationId")
                        .last("id").as("inventoryId")
                        .last("row").as("row")
                        .last("corridor").as("corridor")
                        .last("warehouse").as("warehouse")
                        .last("inventoryCode").as("inventoryCode")
                , Aggregation.project(
                        "row",
                        "warehouse",
                        "corridor",
                        "previousQuantity",
                        "currentQuantity",
                        "inventoryLocationId",
                        "minQuantity",
                        "inventoryId",
                        "inventoryCode"
                ).and(ConvertOperators.ToObjectId.toObjectId("$inventoryLocationId")).as("inventoryLocationId"),
                lookup("storage", "inventoryLocationId", "_id", "inventoryLocation")
                , project()
                        .and("row").as("row")
                        .and("warehouse").as("warehouse")
                        .and("corridor").as("corridor")
                        .and("previousQuantity").as("previousQuantity")
                        .and("currentQuantity").as("currentQuantity")
                        .and("minQuantity").as("minQuantity")
                        .and("inventoryId").as("inventoryId")
                        .and("inventoryCode").as("inventoryCode")
                        .and("inventoryLocation.title").as("inventoryLocationName")
                , skipOperation
                , limitOperation
        );
        return this.aggregate(aggregation, Inventory.class, InventoryGetAllInPartDTO.class).getMappedResults();
    }

    @Override
    public long getAllInventoryCount(String partId) {
        Criteria criteria = new Criteria();
        criteria.and("sameDocumentsDeleted").ne(true);
        criteria.and("deleted").ne(true);
        criteria.and("partId").is(partId);
        Aggregation aggregation = newAggregation(
                Aggregation.match(criteria)
        );
        return this.aggregate(aggregation, Inventory.class, InventoryDTO.class).getMappedResults().size();
    }

    @Override
    public Inventory postInventory(Inventory inventory) {
        inventory.setCreationDate(new Date());
        return mongoOperations.save(inventory);
    }

    @Override
    public boolean checkInventoryCode(String inventoryCode) {
        Query query = new Query();
        query.addCriteria(where("inventoryCode").is(inventoryCode));
        query.addCriteria(where("deleted").ne(true));
        return mongoOperations.exists(query, Inventory.class);
    }

    @Override
    public UpdateResult updateInventory(Inventory inventory) {
        Query query = new Query();
        query.addCriteria(where("deleted").ne(true));
        query.addCriteria(where("id").is(inventory.getId()));

        Update update = new Update();
        update.set("currentQuantity", inventory.getCurrentQuantity());
        update.set("previousQuantity", inventory.getPreviousQuantity());
        update.set("minQuantity", inventory.getMinQuantity());
        update.set("chargeDepartmentId", inventory.getChargeDepartmentId());
        update.set("inventoryCode", inventory.getInventoryCode());
        update.set("budgetId", inventory.getBudgetId());
        update.set("corridor", inventory.getCorridor());
        update.set("row", inventory.getRow());
        update.set("partId", inventory.getPartId());
        update.set("warehouse", inventory.getWarehouse());
        update.set("price", inventory.getPrice());
        return mongoOperations.updateFirst(query, update, Inventory.class);
    }

    @Override
    public InventoryGetOneDTO getOneInventory(String inventoryId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(inventoryId);
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project("currentQuantity", "previousQuantity", "minQuantity", "corridor", "row", "warehouse", "price", "inventoryCode")
                        .and(ConvertOperators.ToObjectId.toObjectId("$chargeDepartmentId")).as("chargeDepartmentId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$budgetId")).as("budgetId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$inventoryLocationId")).as("inventoryLocationId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$partId")).as("partId"),
                lookup("chargeDepartment", "chargeDepartmentId", "_id", "chargeDepartment"),
                lookup("budget", "budgetId", "_id", "budget"),
                lookup("storage", "inventoryLocationId", "_id", "inventoryLocation"),
                lookup("part", "partId", "_id", "part"),
                project("currentQuantity", "previousQuantity", "minQuantity", "corridor", "row", "warehouse", "price", "inventoryCode")
                        .and("chargeDepartment._id").as("chargeDepartmentId")
                        .and("chargeDepartment.title").as("chargeDepartmentName")
                        .and("budget._id").as("budgetId")
                        .and("budget.title").as("budgetName")
                        .and("inventoryLocation._id").as("inventoryLocationId")
                        .and("inventoryLocation.title").as("inventoryLocationName")
                        .and("part._id").as("partId")
                        .and("part.name").as("partName")
                        .and("part.partCode").as("partCode")
        );
        return mongoOperations.aggregate(aggregation, Inventory.class, InventoryGetOneDTO.class).getUniqueMappedResult();
    }

    @Override
    public boolean checkInventoryLocation(PartDTO3 partDTO3) {
        Query query = new Query();
//        query.addCriteria(where("partId").is(partDTO3.getPartId()));
        query.addCriteria(where("inventoryLocationId").is(partDTO3.getInventoryLocationId()));
        query.addCriteria(where("row").is(partDTO3.getRow()));
        query.addCriteria(where("warehouse").is(partDTO3.getWarehouse()));
        query.addCriteria(where("corridor").is(partDTO3.getCorridor()));
        return mongoOperations.exists(query, Inventory.class);
    }

    @Override
    public List<ChangedInventoryDTO> getAllPartDTO3(inventoryDTO1 inventoryDTO1, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("inventoryLocationId").is(inventoryDTO1.getInventoryLocationId());
        criteria.and("row").is(inventoryDTO1.getRow());
        criteria.and("warehouse").is(inventoryDTO1.getWarehouse());
        criteria.and("corridor").is(inventoryDTO1.getCorridor());
        Aggregation aggregation = newAggregation(
                match(criteria),
                project("currentQuantity", "previousQuantity", "creationDate")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userId")).as("userId"),
                lookup("user", "userId", "_id", "user"),
                unwind("user", true),
                project("currentQuantity", "previousQuantity", "creationDate")
                        .and("user.userTypeId").as("userTypeId")
                        .and("user.name").as("userName")
                        .and("user.family").as("userFamily")
                        .and(ConvertOperators.ToObjectId.toObjectId("$user.userTypeId")).as("userTypeId"),
                lookup("userType", "userTypeId", "_id", "userType"),
                unwind("userType", true),
                project("currentQuantity", "previousQuantity", "creationDate")
                        .and("userName").as("userName")
                        .and("userFamily").as("userFamily")
                        .and("userType.name").as("userTypeName")
                , skipOperation
                , limitOperation
        );
        Print.print("AAA", mongoOperations.aggregate(aggregation, Inventory.class, Object.class).getMappedResults());
        return mongoOperations.aggregate(aggregation, Inventory.class, ChangedInventoryDTO.class).getMappedResults();
    }

    @Override
    public List<Inventory> getAllPartDTO4(PartDTO4 partDTO4) {
        return null;
    }

    @Override
    public DeleteResult deleteInventory(inventoryDTO1 inventoryDTO1) {
        Query query = new Query();
        query.addCriteria(where("row").is(inventoryDTO1.getRow()));
        query.addCriteria(where("corridor").is(inventoryDTO1.getCorridor()));
        query.addCriteria(where("partId").is(inventoryDTO1.getPartId()));
        query.addCriteria(where("warehouse").is(inventoryDTO1.getWarehouse()));
        query.addCriteria(where("inventoryLocationId").is(inventoryDTO1.getInventoryLocationId()));
        query.addCriteria(where("deleted").ne(true));
        return mongoOperations.remove(query, Inventory.class);
    }

    @Override
    public List<Inventory> getAllInventoryByTermAndPagination(Pageable pageable, String term) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("deleted").exists(false);
        Aggregation aggregation = newAggregation(
                match(criteria)
                , skipOperation
                , limitOperation
        );
        return mongoOperations.aggregate(aggregation, Inventory.class, Inventory.class).getMappedResults();
    }

    @Override
    public long getAllInventoryTermCount(String term) {
        Query query = new Query();
        query.addCriteria(where("deleted").ne(true));
        query.addCriteria(where(Inventory.FN.partCode.toString()).is(term));
        query.addCriteria(where(Inventory.FN.partName.toString()).is(term));
        return mongoOperations.count(query, Inventory.class);
    }

    @Override
    public boolean updateListOfInventory(List<Inventory> inventory) {
        BulkOperations bulkOperations = mongoOperations.bulkOps(BulkOperations.BulkMode.UNORDERED, Inventory.class);
        for (Inventory inventory1 : inventory) {
            Update update = new Update();
            bulkOperations.updateOne(new Query(Criteria.where("id").is(inventory1.getId())), update.set("currentQuantity", inventory1.getCurrentQuantity()));
            bulkOperations.updateOne(new Query(Criteria.where("id").is(inventory1.getId())), update.set("previousQuantity", inventory1.getPreviousQuantity()));
            bulkOperations.updateOne(new Query(Criteria.where("id").is(inventory1.getId())), update.set("minQuantity", inventory1.getMinQuantity()));
            bulkOperations.updateOne(new Query(Criteria.where("id").is(inventory1.getId())), update.set("partName", inventory1.getPartName()));
            bulkOperations.updateOne(new Query(Criteria.where("id").is(inventory1.getId())), update.set("partCode", inventory1.getPartCode()));
        }
        bulkOperations.execute();
        return true;
    }

    @Override
    public void postListOfInventory(Inventory inventory, InventoryDTOForCurrentQuantity inventoryDTOForCurrentQuantity) {
        inventory.setId(new ObjectId().toString());
        inventory.setInventoryId(inventory.getId());
        inventory.setUserId(inventoryDTOForCurrentQuantity.getUserId());
        inventory.setPreviousQuantity(inventory.getCurrentQuantity());
        inventory.setCurrentQuantity(inventoryDTOForCurrentQuantity.getCurrentQuantity());
        inventory.setCreationDate(new Date());
        Print.print("inventory", inventory);
        mongoOperations.save(inventory);
    }

    @Override
    public boolean checkIfPartHasInventory(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("partId").in(partId));
        Inventory inventory = mongoOperations.findOne(query, Inventory.class);
        return inventory != null;
    }

    @Override
    public List<InventoryGetAllDTO> getInventoryByPartNameAndInventoryLocationTitle(InventorySearchDTO inventorySearchDTO,
                                                                                    Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("sameDocumentsDeleted").ne(true);

        if (inventorySearchDTO.getPartName() != null && !inventorySearchDTO.getPartName().equals("")) {
            criteria.and("partName").regex(inventorySearchDTO.getPartName());
        }
        if (inventorySearchDTO.getPartCode() != null && !inventorySearchDTO.getPartCode().equals("")) {
            criteria.and("partCode").regex(inventorySearchDTO.getPartCode());
        }
        if (inventorySearchDTO.getCorridor() != null && !inventorySearchDTO.getCorridor().equals("")) {
            criteria.and("corridor").regex(inventorySearchDTO.getCorridor());
        }
        if (inventorySearchDTO.getRow() != null && !inventorySearchDTO.getRow().equals("")) {
            criteria.and("row").regex(inventorySearchDTO.getRow());
        }
        if (inventorySearchDTO.getWarehouse() != null && !inventorySearchDTO.getWarehouse().equals("")) {
            criteria.and("warehouse").regex(inventorySearchDTO.getWarehouse());
        }
        if (inventorySearchDTO.getInventoryCode()!= null && !inventorySearchDTO.getInventoryCode().equals("")){
            criteria.and("inventoryCode").regex(inventorySearchDTO.getInventoryCode());
        }
        if (inventorySearchDTO.getInventoryLocation() != null && !inventorySearchDTO.getInventoryLocation().equals("")) {
            criteria.and("inventoryLocationId").is(inventorySearchDTO.getInventoryLocation());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("row", "warehouse", "corridor", "inventoryLocationId")
                        .last("creationDate").as("creationDate")
                        .last("currentQuantity").as("currentQuantity")
                        .last("minQuantity").as("minQuantity")
                        .last("partId").as("partId")
                        .last("previousQuantity").as("previousQuantity")
                        .last("inventoryLocationId").as("inventoryLocationId")
                        .last("inventoryCode").as("inventoryCode")
                        .last("id").as("inventoryId")
                , Aggregation.project()
                        .and("inventoryCode").as("inventoryCode")
                        .and("row").as("row")
                        .and("warehouse").as("warehouse")
                        .and("corridor").as("corridor")
                        .and("previousQuantity").as("previousQuantity")
                        .and("creationDate").as("creationDate")
                        .and("inventoryId").as("inventoryId")
                        .and("currentQuantity").as("currentQuantity")
                        .and("minQuantity").as("minQuantity")
                        .and(ConvertOperators.ToObjectId.toObjectId("$partId")).as("partId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$inventoryLocationId")).as("inventoryLocationId")
                , lookup("part", "partId", "_id", "part")
                , lookup("storage", "inventoryLocationId", "_id", "inventoryLocation")
                , Aggregation.project()
                        .and("inventoryCode").as("inventoryCode")
                        .and("row").as("row")
                        .and("warehouse").as("warehouse")
                        .and("corridor").as("corridor")
                        .and("previousQuantity").as("previousQuantity")
                        .and("creationDate").as("creationDate")
                        .and("inventoryId").as("inventoryId")
                        .and("currentQuantity").as("currentQuantity")
                        .and("minQuantity").as("minQuantity")
                        .and("inventoryLocation").as("inventoryLocation")
                        .and("part._id").as("partId")
                        .and("part.name").as("partName")
                        .and("part.partCode").as("partCode")
                , skipOperation
                , limitOperation
        );
        System.out.println(aggregation.toString());
        return this.aggregate(aggregation, Inventory.class, InventoryGetAllDTO.class).getMappedResults();
    }

    @Override
    public long countGetInventoryByPartNameAndInventoryLocationTitle(InventorySearchDTO inventorySearchDTO) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("sameDocumentsDeleted").ne(true);

        if (inventorySearchDTO.getPartName() != null && !inventorySearchDTO.getPartName().equals("")) {
            criteria.and("partName").regex(inventorySearchDTO.getPartName());
        }
        if (inventorySearchDTO.getPartCode() != null && !inventorySearchDTO.getPartCode().equals("")) {
            criteria.and("partCode").regex(inventorySearchDTO.getPartCode());
        }
        if (inventorySearchDTO.getCorridor() != null && !inventorySearchDTO.getCorridor().equals("")) {
            criteria.and("corridor").regex(inventorySearchDTO.getCorridor());
        }
        if (inventorySearchDTO.getRow() != null && !inventorySearchDTO.getRow().equals("")) {
            criteria.and("row").regex(inventorySearchDTO.getRow());
        }
        if (inventorySearchDTO.getWarehouse() != null && !inventorySearchDTO.getWarehouse().equals("")) {
            criteria.and("warehouse").regex(inventorySearchDTO.getWarehouse());
        }
        if (inventorySearchDTO.getInventoryLocation() != null && !inventorySearchDTO.getInventoryLocation().equals("")) {
            criteria.and("inventoryLocationId").is(inventorySearchDTO.getInventoryLocation());
        }
        if (inventorySearchDTO.getInventoryCode()!= null && !inventorySearchDTO.getInventoryCode().equals("")){
            criteria.and("inventoryCode").regex(inventorySearchDTO.getInventoryCode());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("row", "warehouse", "corridor", "inventoryLocationId")
                        .last("creationDate").as("creationDate")
                        .last("currentQuantity").as("currentQuantity")
                        .last("minQuantity").as("minQuantity")
                        .last("partCode").as("partCode")
                        .last("previousQuantity").as("previousQuantity")
                        .last("inventoryLocationId").as("inventoryLocationId")
                        .last("userId").as("userId")
                        .last("inventoryCode").as("inventoryCode")
                        .last("partName").as("partName")
                        .last("id").as("inventoryId")
        );
        return this.aggregate(aggregation, Inventory.class, InventoryGetAllDTO.class).getMappedResults().size();
    }

    @Override
    public List<InventoryDTO> getAllInventoryByGroup(Pageable pageable, Integer totalElement) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("sameDocumentsDeleted").ne(true);
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria)
                , Aggregation.sort(Sort.Direction.ASC, "creationDate")
                , Aggregation.group("row", "warehouse", "corridor", "inventoryLocationId")
                        .last("partId").as("partId")
                        .last("currentQuantity").as("currentQuantity")
                        .last("minQuantity").as("minQuantity")
                        .last("partCode").as("partCode")
                        .last("previousQuantity").as("previousQuantity")
                        .last("inventoryLocationId").as("inventoryLocationId")
                        .last("partName").as("partName")

                , Aggregation.project(
                        "partId",
                        "partName",
                        "row",
                        "warehouse",
                        "corridor",
                        "previousQuantity",
                        "currentQuantity",
                        "inventoryLocationId",
                        "minQuantity",
                        "partCode"
                ).and(ConvertOperators.ToObjectId.toObjectId("$inventoryLocationId")).as("inventoryLocationId"),
                lookup("storage", "inventoryLocationId", "_id", "inventoryLocation")
                , project()
                        .and("row").as("row")
                        .and("warehouse").as("warehouse")
                        .and("corridor").as("corridor")
                        .and("previousQuantity").as("previousQuantity")
                        .and("currentQuantity").as("currentQuantity")
                        .and("minQuantity").as("minQuantity")
                        .and("partId").as("partId")
                        .and("partCode").as("partCode")
                        .and("partName").as("partName")
                        .and("inventoryLocation.title").as("inventoryLocationName")
                , skipOperation
                , limitOperation
        );
        return this.aggregate(aggregation, Inventory.class, InventoryDTO.class).getMappedResults();
    }

    @Override
    public long inventoryDaoCountAllInventoryByGroup() {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("sameDocumentsDeleted").ne(true);
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria)
        );
        return this.aggregate(aggregation, Inventory.class, Inventory.class).getMappedResults().size();
    }

    @Override
    public List<Inventory> getLowStockItems(List<Inventory> inventoryList) {
        List<Inventory> inventoryList1 = new ArrayList<>();
        inventoryList.forEach(inventory -> {
            if (inventory.getCurrentQuantity() < inventory.getMinQuantity()) {
                inventoryList1.add(inventory);
            }
        });
        return inventoryList1;
    }

    @Override
    public List<Inventory> getAllInventory() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, Inventory.class);
    }

    @Override
    public long countLowStockItems(List<Inventory> inventoryList) {
        List<Inventory> inventoryList1 = new ArrayList<>();
        inventoryList.forEach(inventory -> {
            if (inventory.getCurrentQuantity() < inventory.getMinQuantity()) {
                inventoryList1.add(inventory);
            }
        });
        return inventoryList1.size();
    }

    @Override
    public List<Inventory> getAllLowStocksItemOfAUser(List<String> partNameList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("partName").in(partNameList));
        return mongoOperations.find(query, Inventory.class);
    }

    @Override
    public List<Inventory> getAllInventoriesWithStorage() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("inventoryLocation").exists(true));
        query.fields()
                .include("inventoryLocation");
        return mongoOperations.find(query, Inventory.class);
    }

    @Override
    public List<Inventory> getAllInventoriesOfStorageByStorageId(String storageId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("inventoryLocation.id").is(storageId));
        return mongoOperations.find(query, Inventory.class);
    }

    @Override
    public boolean ifBudgetExistsInInventory(String budgetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("budget.id").is(budgetId));
        return mongoOperations.exists(query, Inventory.class);
    }

    @Override
    public boolean ifChargeDepartmentExistsInInventory(String chargeDepartmentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("chargeDepartment.id").is(chargeDepartmentId));
        return mongoOperations.exists(query, Inventory.class);
    }

    @Override
    public Inventory getDeletedInventory(String inventoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(inventoryId));
        query.fields()
                .include("id")
                .include("corridor")
                .include("row")
                .include("warehouse")
                .include("inventoryLocationId")
                .include("partId");
        return mongoOperations.findOne(query, Inventory.class);
    }

    @Override
    public void deleteSameDocumentsFromDatabase(Inventory deletedInventory) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("row").is(deletedInventory.getRow()));
        query.addCriteria(Criteria.where("corridor").is(deletedInventory.getCorridor()));
        query.addCriteria(Criteria.where("warehouse").is(deletedInventory.getWarehouse()));
        query.addCriteria(Criteria.where("inventoryLocationId").is(deletedInventory.getInventoryLocationId()));
        Update update = new Update();
        update.set("sameDocumentsDeleted", true);
        mongoOperations.updateMulti(query, update, Inventory.class);
    }

    @Override
    public boolean ifWarehouseHasPart(String storageId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("inventoryLocationId").is(storageId));
        return mongoOperations.exists(query, Inventory.class);
    }

    @Override
    public List<Inventory> InventoryListByPartCode(List<String> partCodeList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("partCode").in(partCodeList));
        return mongoOperations.find(query, Inventory.class);
    }

    @Override
    public long countList(inventoryDTO1 inventoryDTO1) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("inventoryLocationId").is(inventoryDTO1.getInventoryLocationId());
        criteria.and("row").is(inventoryDTO1.getRow());
        criteria.and("warehouse").is(inventoryDTO1.getWarehouse());
        criteria.and("corridor").is(inventoryDTO1.getCorridor());
        Aggregation aggregation = newAggregation(
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Inventory.class, Object.class).getMappedResults().size();
    }

    @Override
    public Inventory getInventoryForInventoryAdjustment(String inventoryId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(inventoryId));
        return mongoOperations.findOne(query, Inventory.class);
    }

    @Override
    public long countInventory(String partId) {
        Criteria criteria = new Criteria();
        criteria.and("sameDocumentsDeleted").ne(true);
        criteria.and("deleted").ne(true);
        criteria.and("partId").is(partId);
        Aggregation aggregation = newAggregation(
                Aggregation.match(criteria)
                , Aggregation.group("row", "warehouse", "corridor", "inventoryLocationId")
                        .last("currentQuantity").as("currentQuantity")
                        .last("minQuantity").as("minQuantity")
                        .last("previousQuantity").as("previousQuantity")
                        .last("inventoryLocationId").as("inventoryLocationId")
                        .last("id").as("inventoryId")
                        .last("row").as("row")
                        .last("corridor").as("corridor")
                        .last("warehouse").as("warehouse")
                        .last("inventoryCode").as("inventoryCode")
        );
        return mongoOperations.aggregate(aggregation, Inventory.class, Inventory.class).getMappedResults().size();
    }
}