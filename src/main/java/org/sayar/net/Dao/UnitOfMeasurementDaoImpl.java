package org.sayar.net.Dao;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.UnitOfMeasurementDTO;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.exceptionHandling.ApiRepetitiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("unitOfMeasurementDaoImpl")
@Transactional
public class UnitOfMeasurementDaoImpl extends GeneralDaoImpl<UnitOfMeasurement> implements UnitOfMeasurementDao {
    @Autowired
    private MongoOperations mongoOperations;

    public UnitOfMeasurementDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public UnitOfMeasurement saveMeasure(UnitOfMeasurement unit) throws ApiRepetitiveException {
        return mongoOperations.save(unit);
    }

    @Override
    public UnitOfMeasurement updateUnit(UnitOfMeasurement unit) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(unit.getId()));
        Update update = new Update();
        update.set("unit", unit.getUnit());
        update.set("title", unit.getTitle());
        mongoOperations.updateFirst(query, update, UnitOfMeasurement.class);
        return unit;
    }

    @Override
    public List<UnitOfMeasurement> getAllUnitOfMeasurementById(List<String> unitIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(unitIdList));
        query.fields()
                .include("id")
                .include("unit")
                .include("title");
        return mongoOperations.find(query, UnitOfMeasurement.class);
    }

    @Override
    public UnitOfMeasurement getUnitOfMeasurementNameById(String unitOfMeasurementId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(unitOfMeasurementId));
        query.fields()
                .include("id")
                .include("title");
        return mongoOperations.findOne(query, UnitOfMeasurement.class);
    }

    @Override
    public UnitOfMeasurement getUnitOfMeasurementByScheduleMaintenanceMeasurementId(String unitOfMeasurementId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(unitOfMeasurementId));
        return mongoOperations.findOne(query, UnitOfMeasurement.class);
    }

    @Override
    public boolean checkIfUnitOfMeasurementIsUnique(String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("title").is(title));
        return mongoOperations.exists(query, UnitOfMeasurement.class);
    }

    @Override
    public List<UnitOfMeasurement> getAllUnitOfMeasurementOfTheAsset(List<String> assetUnitIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(assetUnitIdList));
        return mongoOperations.find(query, UnitOfMeasurement.class);
    }

    @Override
    public List<UnitOfMeasurement> getUnitOfMeasurementTitle(List<String> unitIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(unitIdList));
        return mongoOperations.find(query, UnitOfMeasurement.class);
    }

    @Override
    public List<UnitOfMeasurement> getAllByPagination(UnitOfMeasurementDTO unitOfMeasurementDTO, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (unitOfMeasurementDTO.getTitle() != null && !unitOfMeasurementDTO.getTitle().equals("")) {
            criteria.and("title").regex(unitOfMeasurementDTO.getTitle());
        }

        if (unitOfMeasurementDTO.getUnit() != null && !unitOfMeasurementDTO.getUnit().equals("")) {
            criteria.and("unit").regex(unitOfMeasurementDTO.getUnit());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, UnitOfMeasurement.class, UnitOfMeasurement.class).getMappedResults();
    }

    @Override
    public long getAllCount(UnitOfMeasurementDTO unitOfMeasurementDTO) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (unitOfMeasurementDTO.getTitle() != null && !unitOfMeasurementDTO.getTitle().equals("")) {
            criteria.and("title").regex(unitOfMeasurementDTO.getTitle());
        }

        if (unitOfMeasurementDTO.getUnit() != null && !unitOfMeasurementDTO.getUnit().equals("")) {
            criteria.and("unit").regex(unitOfMeasurementDTO.getUnit());
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, UnitOfMeasurement.class, UnitOfMeasurement.class).getMappedResults().size();
    }

    @Override
    public boolean checkIfUnitAndTitleExist(UnitOfMeasurementDTO unitOfMeasurementDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("unit").is(unitOfMeasurementDTO.getUnit()));
        query.addCriteria(Criteria.where("title").is(unitOfMeasurementDTO.getTitle()));
        return mongoOperations.exists(query, UnitOfMeasurement.class);
    }
}
