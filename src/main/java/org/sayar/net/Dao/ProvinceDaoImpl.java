package org.sayar.net.Dao;


import com.mongodb.client.result.DeleteResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.Location.Province;
import org.sayar.net.exceptionHandling.ApiRepetitiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("provinceDaoImpl")
@Transactional
public class ProvinceDaoImpl extends GeneralDaoImpl<Province> implements ProvinceDao {
    public ProvinceDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Autowired
    private MongoOperations mongoOperations;

    public List<City> getCities(String provinceId) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("provinceId").is(provinceId),
                Criteria.where("deleted").ne(true)
        );
        query = query.addCriteria(criteria);

        return this.find(query, City.class);

    }

    @Override
    public Province getProvinceByOrganId(String id) {
        return null;
    }

    @Override
    public List<Province> getAllCityNameIdAndLocation() {
        return null;
    }

    @Override
    public String deleteOne(String provinceId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(provinceId));
        DeleteResult deleteResult = this.remove(query, Province.class);
        if (deleteResult.getDeletedCount() > 0) return "TRUE";
        else return "FALSE";
    }

    @Override
    public boolean saveOne(Province province) throws ApiRepetitiveException {
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(province.getName())
                .andOperator(Criteria.where("deleted").ne(true))
        );

        Province province1 = mongoOperations.findOne(query, Province.class);
        if (province1 == null) {
            System.out.println("Not_Exist");
            mongoOperations.save(province);
            return true;
        } else {
            System.out.println("Exist");
            throw new ApiRepetitiveException();
        }
    }

    @Override
    public Province updateProvince(Province province) {

        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("name").is(province.getName()),    //validation is on name
                        Criteria.where("deleted").ne(true),
                        Criteria.where("id").ne(province.getId())
                )
        );
        Province province1 = mongoOperations.findOne(query, Province.class);
        if (province1 == null) {
            mongoOperations.save(province);
            return province;

        } else {
            throw new ApiRepetitiveException();
        }
    }

    @Override
    public List<Province> getAllProvince(String term) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("name").regex(term);
        } else {
            criteria.and("name").ne(true);
        }
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project("name")
        );
        AggregationResults<Province> groupResults = this.aggregate(agg, "province", Province.class);
        return groupResults.getMappedResults();
    }

    @Override
    public List<Province> getProvinceList(List<String> provinceIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(provinceIdList));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.find(query, Province.class);
    }

    @Override
    public List<Province> getAllByPagination(String term, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("name").regex(term);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                skipOperation,
                limitOperation
        );

        return mongoOperations.aggregate(aggregation, Province.class, Province.class).getMappedResults();
    }

    @Override
    public long getAllCount(String term) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("name").regex(term);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
                );

        return mongoOperations.aggregate(aggregation, Province.class, Province.class).getMappedResults().size();
    }
}
