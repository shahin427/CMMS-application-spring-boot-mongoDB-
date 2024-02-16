package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Model.DTO.StorageGetAllDTO;
import org.sayar.net.Model.newModel.Storage;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.lookup;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Repository
public class StorageDaoImp implements StorageDao {
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public boolean postStorage(Storage storage) {
        mongoOperations.save(storage);
        return true;
    }

    @Override
    public List<Storage> getAllStorage() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("id")
                .include("title")
                .include("code");
        return mongoOperations.find(query, Storage.class);
    }

    @Override
    public Storage getOneStorage(String storageId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(storageId)
                .andOperator(Criteria.where("deleted").ne(true)));
        return mongoOperations.findOne(query, Storage.class);
    }

    @Override
    public UpdateResult updateStorage(Storage storage, String storageId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(storageId));

        Update update = new Update();
        update.set("code", storage.getCode());
        update.set("title", storage.getTitle());
        update.set("address", storage.getAddress());
        update.set("assetId", storage.getAssetId());
        return mongoOperations.updateFirst(query, update, Storage.class);
    }

    @Override
    public List<StorageGetAllDTO> getAllByFilterAndPagination(String term, String code, String assetId, Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        }

        if (code != null && !code.equals("")) {
            criteria.and("code").regex(code);
        }

        if (assetId != null) {
            criteria.and("assetId").is(assetId);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("code").as("code")
                        .and(ConvertOperators.ToObjectId.toObjectId("$assetId")).as("assetId")
                , lookup("asset", "assetId", "_id", "asset"),
                project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("code").as("code")
                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")
                , skipOperation
                , limitOperation
        );
        return mongoOperations.aggregate(aggregation, Storage.class, StorageGetAllDTO.class).getMappedResults();
    }

    @Override
    public long getAllStorageCount(String term, String code, String assetId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        }

        if (code != null && !code.equals("")) {
            criteria.and("code").regex(code);
        }

        if (assetId != null) {
            criteria.and("assetId").is(assetId);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Storage.class, Storage.class).getMappedResults().size();
    }

    @Override
    public List<Storage> getAllByPagination(String term, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("deleted").ne(true))
                , skipOperation
                , limitOperation
        );
        return mongoOperations.aggregate(aggregation, Storage.class, Storage.class).getMappedResults();
    }

    @Override
    public boolean checkIfStorageCodeExists(String storageCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(storageCode));
        return mongoOperations.exists(query, Storage.class);
    }

    @Override
    public List<Storage> getAllStorageThatHasAsset() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetId").exists(true));
        return mongoOperations.find(query, Storage.class);
    }

    @Override
    public List<Storage> getAllStorageOfAssetHasParts(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetId").is(assetId));
        return mongoOperations.find(query, Storage.class);
    }

    @Override
    public boolean ifProvinceExistsInStorage(String provinceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("address.province.id").is(provinceId));
        return mongoOperations.exists(query, Storage.class);
    }

    @Override
    public boolean ifCityExistsInStorage(String cityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("address.city.id").is(cityId));
        return mongoOperations.exists(query, Storage.class);
    }

    @Override
    public boolean ifAssetExistsInStorage(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetId").is(assetId));
        return mongoOperations.exists(query, Storage.class);
    }

    @Override
    public boolean checkIfCodeIsUnique(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(code));
        return mongoOperations.exists(query, Storage.class);
    }

    @Override
    public void addInventoryIdToStorage(String inventoryId, String storageId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(inventoryId));
        Update update = new Update();
        update.push("storage.inventoryList", storageId);
        mongoOperations.updateFirst(query, update, Storage.class);
    }

    @Override
    public Storage getStorageNameById(String inventoryLocationId) {
        Print.print("AAA", inventoryLocationId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(inventoryLocationId));
        query.fields()
                .include("title");
        return mongoOperations.findOne(query, Storage.class);
    }
}
