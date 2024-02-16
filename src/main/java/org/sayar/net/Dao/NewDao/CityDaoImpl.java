package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.Tools.Print;
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

@Repository("cityDaoImpl")
@Transactional

public class CityDaoImpl extends GeneralDaoImpl<City> implements CityDao {
    @Autowired
    private MongoOperations mongoOperations;

    public CityDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public List<Organization> getCityOrganization(String cityId, String parentOrgan) {

        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("cityId").is(cityId),
                Criteria.where("deleted").ne(true)
        );
        query.addCriteria(criteria);
        return mongoOperations.find(query, Organization.class);
    }

    @Override
    public List<City> getAllCityNameIdAndLocation(long provinceId) {
        return null;
    }

    @Override
    public boolean existenceByProvinceId(String provinceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("provinceId").is(provinceId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return this.exists(query, City.class);
    }

    @Override
    public boolean postOne(City city) throws ApiRepetitiveException {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(city.getName())
                .andOperator(Criteria.where("deleted").ne(true)));
        City city1 = mongoOperations.findOne(query, City.class);
        if (city1 == null) {
            mongoOperations.save(city);
            System.out.println("The_city_is_not_repeated");
            return true;

        } else {
            System.out.println("The_city_is_repeated");
            throw new ApiRepetitiveException();
        }
    }

    @Override
    public UpdateResult updateCity(City city) {
        Print.print("cityyyyyyyyy", city);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(city.getId()));
        Update update = new Update();
        update.set("name", city.getName());
        update.set("location", city.getLocation());
        update.set("provinceId", city.getProvinceId());
        return mongoOperations.updateFirst(query, update, City.class);
    }

    @Override
    public List<City> getAllCityByProvinceId(String provinceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("provinceId").is(provinceId));
        query.addCriteria(Criteria.where("name").ne(true));
        query.addCriteria(Criteria.where("deleted").ne(true)).fields().exclude("location");
        return mongoOperations.find(query, City.class);
    }

    @Override
    public City findOneById(String cityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(cityId));
        return mongoOperations.findOne(query, City.class);
    }

    @Override
    public boolean ifProvinceExistsInCity(String provinceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("provinceId").is(provinceId));
        return mongoOperations.exists(query, City.class);
    }

    @Override
    public List<City> getCityList(List<String> cityIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(cityIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, City.class);
    }

    @Override
    public List<City> getAllByPaginationByProvinceId(String provinceId, String term, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("provinceId").is(provinceId);

        if (term != null && !term.equals("")) {
            criteria.and("name").regex(term);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                skipOperation,
                limitOperation
        );

        return mongoOperations.aggregate(aggregation, City.class, City.class).getMappedResults();
    }

    @Override
    public long getAllCount(String provinceId, String term) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("provinceId").is(provinceId);

        if (term != null && !term.equals("")) {
            criteria.and("name").regex(term);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );

        return mongoOperations.aggregate(aggregation, City.class, City.class).getMappedResults().size();
    }
}