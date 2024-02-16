package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.WarrantyDTO;
import org.sayar.net.Model.DTO.WarrantyGetAllDTO;
import org.sayar.net.Model.DTO.WarrantyGetOneDTO;
import org.sayar.net.Model.newModel.Warranty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

import java.util.List;

@Repository
public class WarrantyDaoImpl extends GeneralDaoImpl<Warranty> implements WarrantyDao {


    public WarrantyDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public List<Warranty> getByIdList(List<String> idList) {
        return this.getByIdList(idList, Warranty.class);
    }

    public List<Warranty> getWarrantyByIdList(List<String> warrantyIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(warrantyIdList));
        return mongoOperations.find(query, Warranty.class);
    }

    @Override
    public List<Warranty> getAllWarrantyByPagination(Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("deleted").ne(true)),
                skipOperation
        );
        return mongoOperations.aggregate(agg, Warranty.class, Warranty.class).getMappedResults();
    }

    @Override
    public long getAllCount() {
        Query query = new Query();
        query.addCriteria(
                Criteria.where("deleted").ne(true)
        );
        return mongoOperations.count(query, Warranty.class);
    }

    @Override
    public Warranty postOneWarranty(Warranty warranty) {
        return mongoOperations.save(warranty);
    }

    @Override
    public WarrantyGetOneDTO getOneWarranty(String warrantyId) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(warrantyId);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project("name", "companyId", "warrantyCode", "time", "expiry", "type", "kilometerWarranty", "description")
                        .and(ConvertOperators.ToObjectId.toObjectId("$unitOfMeasurementId")).as("unitOfMeasurementId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$companyId")).as("companyId"),
                Aggregation.lookup("unitOfMeasurement", "unitOfMeasurementId", "_id", "unitOfMeasurement"),
                Aggregation.lookup("company", "$companyId", "_id", "company"),
                Aggregation.project("name", "warrantyCode", "time", "expiry", "type", "kilometerWarranty", "description", "companyId", "unitOfMeasurementId")
                        .and("company.name").as("companyName")
                        .and("unitOfMeasurement.title").as("unitOfMeasurementName")
        );
        return mongoOperations.aggregate(aggregation, Warranty.class, WarrantyGetOneDTO.class).getUniqueMappedResult();
    }

    @Override
    public UpdateResult updateWarranty(Warranty warranty) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(warranty.getId()));
        Update update = new Update();
        update.set("warrantyCode", warranty.getWarrantyCode());
        update.set("name", warranty.getName());
        update.set("kilometerWarranty", warranty.getKilometerWarranty());
        update.set("time", warranty.getTime());
        update.set("expiry", warranty.getExpiry());
        update.set("companyId", warranty.getCompanyId());
        update.set("unitOfMeasurementId", warranty.getUnitOfMeasurementId());
        update.set("type", warranty.getType());
        update.set("description", warranty.getDescription());
        return mongoOperations.updateFirst(query, update, Warranty.class);
    }

    @Override
    public List<Warranty> getAllCompany() {

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("warrantyCompany");
        return mongoOperations.find(query, Warranty.class);
    }

    @Override
    public List<Warranty> getAllWarrantyType() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("id").include("warrantyType");
        return mongoOperations.find(query, Warranty.class);
    }

    @Override
    public List<WarrantyDTO> getAllWarrantyMeasurement() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("deleted").ne(true)),
                Aggregation.lookup("unitOfMeasurement", "unitOfMeasurementId", "_id", "unitOfMeasurement")
        );
        return this.aggregate(aggregation, Warranty.class, WarrantyDTO.class).getMappedResults();
    }

    @Override
    public List<Warranty> getAllPaginationByAssetId(String assetId, Pageable pageable, Integer totalElement) {

        Query query = new Query();
        query.with(pageable);
        query.addCriteria(Criteria.where("assetId").is(assetId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, Warranty.class);
    }

    @Override
    public long getAllWarrantyCount() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.count(query, Warranty.class);
    }

    @Override
    public List<WarrantyGetAllDTO> getAllWarrantyByPartId(String partId, String assetId, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (partId != null && !partId.equals(""))
            criteria.and("partId").is(partId);

        if (assetId != null && !assetId.equals(""))
            criteria.and("assetId").is(assetId);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                skipOperation,
                limitOperation,
                Aggregation.project("id", "name", "warrantyCode", "time", "expiry", "kilometerWarranty", "type")
                        .and(ConvertOperators.ToObjectId.toObjectId("$unitOfMeasurementId")).as("unitOfMeasurementId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$companyId")).as("companyId"),
                Aggregation.lookup("unitOfMeasurement", "unitOfMeasurementId", "_id", "unitOfMeasurement"),
                Aggregation.lookup("company", "companyId", "_id", "company"),
                Aggregation.project("id", "name", "warrantyCode", "time", "expiry", "kilometerWarranty", "type")
                        .and("company.name").as("companyName")
                        .and("unitOfMeasurement.title").as("unitOfMeasurementName")
        );
        return mongoOperations.aggregate(aggregation, Warranty.class, WarrantyGetAllDTO.class).getMappedResults();
    }

    @Override
    public long countAllWarrantyByPartId(String assetId, String partId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (partId != null && !partId.equals(""))
            criteria.and("partId").is(partId);

        if (assetId != null && !assetId.equals(""))
            criteria.and("assetId").is(assetId);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Warranty.class, WarrantyGetAllDTO.class).getMappedResults().size();
    }

    @Override
    public boolean checkIfCodeExists(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("warrantyCode").is(code));
        return mongoOperations.exists(query, Warranty.class);
    }

    @Override
    public boolean ifCompanyExistsInWarranty(String companyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("companyId").is(companyId));
        return mongoOperations.exists(query, Warranty.class);
    }

}
