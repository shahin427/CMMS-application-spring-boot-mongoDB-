package org.sayar.net.Dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Controller.newController.BOM;
import org.sayar.net.Controller.newController.BOMAsset;
import org.sayar.net.Controller.newController.BOMPart;
import org.sayar.net.Model.DTO.AssetBOMDTO;
import org.sayar.net.Model.DTO.BOMAssetDTO;
import org.sayar.net.Model.DTO.BOMNameDTO;
import org.sayar.net.Model.DTO.PartBOMDTO;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class BOMDaoImp implements BOMDao {
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public String createBOM(BOMNameDTO bomdto) {
        BOM bom = new BOM();
        bom.setCode(bomdto.getCode());
        bom.setBomGroupName(bomdto.getBomGroupName());
        return mongoOperations.save(bom).getId();
    }

    @Override
    public List<BOMPartDTO> getAllPartByPagination(String bomId, Pageable pageable) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(bomId);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                skipOperation,
                limitOperation,
                project("bomPartList"),
                unwind("bomPartList"),
                project()
                        .and("bomPartList.partQuantity").as("partQuantity")
                        .and(ConvertOperators.ToObjectId.toObjectId("$bomPartList.partId")).as("partId"),
                lookup("part", "partId", "_id", "part"),
                project()
                        .and("partQuantity").as("partQuantity")
                        .and("part._id").as("partId")
                        .and("part.name").as("partName")
                        .and("part.partCode").as("partCode")
        );
        return mongoOperations.aggregate(aggregation, BOM.class, BOMPartDTO.class).getMappedResults();
    }

    @Override
    public List<BOMAssetDTO> getAllAssetByPagination(String bomId, Pageable pageable) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(bomId);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                skipOperation,
                limitOperation,
                project("bomAssetList"),
                unwind("bomAssetList"),
                project()
                        .and("bomAssetList.assetQuantity").as("assetQuantity")
                        .and(ConvertOperators.ToObjectId.toObjectId("$bomAssetList.assetId")).as("assetId"),
                lookup("asset", "assetId", "_id", "asset"),
                project()
                        .and("assetQuantity").as("assetQuantity")
                        .and("asset._id").as("assetId")
                        .and("asset.code").as("assetCode")
                        .and("asset.name").as("assetName")
        );
        Print.print("AAA", mongoOperations.aggregate(aggregation, BOM.class, Object.class).getMappedResults());
        return mongoOperations.aggregate(aggregation, BOM.class, BOMAssetDTO.class).getMappedResults();
    }

    @Override
    public UpdateResult updateBOM(List<BOMPart> partBOMDTOList, String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("bomPartList", partBOMDTOList);
        return mongoOperations.updateFirst(query, update, BOM.class);
    }

    @Override
    public BOM getOneBOM(String bomId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(bomId));
        query.fields()
                .include("id")
                .include("code")
                .include("bomGroupName");
        return mongoOperations.findOne(query, BOM.class);
    }

    @Override
    public List<BOM> getAllBOMByPagination(String name, String code, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (name != null && !name.equals("")) {
            criteria.and("bomGroupName").regex(name);
        }
        if (code != null && !code.equals("")) {
            criteria.and("code").regex(code);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria)
                , project("id", "code", "bomGroupName")
                , skipOperation
                , limitOperation
        );
        return mongoOperations.aggregate(aggregation, BOM.class, BOM.class).getMappedResults();
    }

    @Override
    public UpdateResult removePartOfBOM(String bomId, String bomPartId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(bomId));
        query.addCriteria(Criteria.where("bomPartList").elemMatch(Criteria.where("partId").is(bomPartId)));
        Update update = new Update();
        update.pull("bomPartList", new BasicDBObject("partId", bomPartId));
        return mongoOperations.updateFirst(query, update, BOM.class);
    }


    @Override
    public long countAllBOM(String name, String code) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (name != null && !name.equals("")) {
            criteria.and("bomGroupName").regex(name);
        }
        if (code != null && !code.equals("")) {
            criteria.and("code").regex(code);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, BOM.class, BOM.class).getMappedResults().size();
    }

    @Override
    public UpdateResult removeAssetOfBOM(String bomId, String bomAssetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(bomId));
        query.addCriteria(Criteria.where("bomAssetList").elemMatch(Criteria.where("assetId").is(bomAssetId)));
        Update update = new Update();
        update.pull("bomAssetList", new BasicDBObject("assetId", bomAssetId));
        return mongoOperations.updateFirst(query, update, BOM.class);
    }

    @Override
    public boolean ifAssetExistsInBOM(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("bomAssetList").elemMatch(Criteria.where("assetId").is(assetId)));
        return mongoOperations.exists(query, BOM.class);
    }

    @Override
    public boolean ifPartExistsInBOM(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("bomPartList").elemMatch(Criteria.where("partId").is(partId)));
        return mongoOperations.exists(query, BOM.class);
    }

    @Override
    public UpdateResult updateAssetBOM(List<AssetBOMDTO> assetBOMDTOList, String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        query.addCriteria(Criteria.where("deleted").ne(true));
        Update update = new Update();
        update.set("bomAssetList", assetBOMDTOList);
        return mongoOperations.updateFirst(query, update, BOM.class);
    }

    @Override
    public long countAllPartBom(String bomId) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(bomId);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project("bomPartList"),
                unwind("bomPartList")
        );
        return mongoOperations.aggregate(aggregation, BOM.class, BOMPartDTO.class).getMappedResults().size();
    }

    @Override
    public long countAssetBom(String bomId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(bomId);
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project("bomAssetList"),
                unwind("bomAssetList")
        );
        return mongoOperations.aggregate(aggregation, BOM.class, BOMAssetDTO.class).getMappedResults().size();
    }

    @Override
    public boolean checkIfBomExists(String bomCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(bomCode));
        return mongoOperations.exists(query, BOM.class);
    }

    @Override
    public UpdateResult updateFirstPage(BOMNameDTO bomNameDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(bomNameDTO.getId()));
        Update update = new Update();
        update.set("code", bomNameDTO.getCode());
        update.set("bomGroupName", bomNameDTO.getBomGroupName());
        return mongoOperations.updateFirst(query, update, BOM.class);
    }
}
