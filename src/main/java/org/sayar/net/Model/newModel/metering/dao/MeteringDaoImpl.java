package org.sayar.net.Model.newModel.metering.dao;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.AssetMeteringDTO;
import org.sayar.net.Model.DTO.LongDTO;
import org.sayar.net.Model.newModel.metering.model.Metering;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@Transactional
public class MeteringDaoImpl extends GeneralDaoImpl<Metering> implements MeteringDao {
    public MeteringDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public Metering postOneMetering(Metering metering) {
        return mongoOperations.save(metering);
    }

    @Override
    public Metering getOneMetering(String meteringId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(meteringId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.findOne(query, Metering.class);
    }

    //
    @Override
    public List<Metering> getAllMetering() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, Metering.class);
    }

    @Override
    public boolean updateMetering(Metering metering) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(metering.getId())
                .andOperator(Criteria.where("deleted").ne(true)));
        Metering metering1 = mongoOperations.findOne(query, Metering.class);
        if (metering1 != null) {
            Update update = new Update();
            update.set("amount", metering.getAmount());
            update.set("description", metering.getDescription());
            update.set("creationDate", metering.getCreationDate());
            update.set("unitOfMeasurement", metering.getUnitOfMeasurement());
            mongoOperations.updateFirst(query, update, Metering.class);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Metering> getMeteringByAssetId(List<String> meterings) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(meterings));
        Print.print(mongoOperations.find(query, Metering.class));
        return mongoOperations.find(query, Metering.class);
    }

    @Override
    public Page<Metering> getAllMeteringByAssetId(String assetId, Pageable pageable, Integer totalCount) {

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetId").is(assetId));
        List<Metering> meteringList = mongoOperations.find(query, Metering.class);
        long count = mongoOperations.count(query, Metering.class);
        return new PageImpl<>(
                meteringList
                , pageable
                , count
        );
    }

    @Override
    public List<Metering> getMeteringAssetI(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("partId").is(partId));
        return mongoOperations.find(query, Metering.class);
    }

    @Override
    public List<AssetMeteringDTO> getMeteringListOfMeasurementUnitOfAsset(String assetId, String unitId, Pageable pageable) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("referenceId").is(assetId);
        criteria.and("unitOfMeasurement.id").is(unitId);
        Aggregation aggregation = newAggregation(
                match(criteria),
                project("amount", "creationDate", "unitOfMeasurement")
                        .and("id").as("meteringId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$referenceId")).as("referenceId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userId")).as("userId"),
                lookup("asset", "referenceId", "_id", "asset"),
                lookup("user", "userId", "_id", "user"),
                project("amount", "creationDate")
                        .and("meteringId").as("meteringId")
                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")
                        .and("user._id").as("userId")
                        .and("user.name").as("userName")
                        .and("user.family").as("userFamily")
                        .and("unitOfMeasurement._id").as("unitOfMeasurementId")
                        .and("unitOfMeasurement.title").as("unitOfMeasurementName"),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Metering.class, AssetMeteringDTO.class).getMappedResults();
    }

    @Override
    public Metering getOneMeteringWithAssociatedUser(String meteringId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(meteringId));
        return mongoOperations.findOne(query, Metering.class);
    }

    @Override
    public long countNumberOfMetering(String assetId, String unitId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("referenceId").is(assetId);
        criteria.and("unitOfMeasurement.id").is(unitId);
        Aggregation aggregation = newAggregation(
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Metering.class, Metering.class).getMappedResults().size();
    }

    @Override
    public boolean ifUnitExistsInMetering(String unitOfMeasurementId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("unitOfMeasurement.id").is(unitOfMeasurementId));
        return mongoOperations.exists(query, Metering.class);
    }

    @Override
    public long getMaxMeteringOfAsset(String assetId, String unitOfMeasurementId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("referenceId").is(assetId);
        criteria.and("unitOfMeasurement.id").is(unitOfMeasurementId);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sort(Sort.Direction.DESC, "amount"),
                Aggregation.group()
                        .first("amount").as("amount"),
                Aggregation.project().andExclude("_id")
        );
        LongDTO longDTO = mongoOperations.aggregate(aggregation, Metering.class, LongDTO.class).getUniqueMappedResult();
        if (longDTO != null) {
            return longDTO.getAmount();
        } else
            return 0;
    }
}
