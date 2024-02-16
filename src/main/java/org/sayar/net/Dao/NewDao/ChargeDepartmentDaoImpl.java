package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Asset.ChargeDepartment;
import org.sayar.net.Tools.Print;
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

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Repository("chargeDepartmentDaoImpl")
@Transactional
public class ChargeDepartmentDaoImpl extends GeneralDaoImpl<ChargeDepartment> implements ChargeDepartmentDao {
    public ChargeDepartmentDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Autowired
    private MongoOperations mongoOperations;

    public ChargeDepartment saveDepartment(ChargeDepartment chargeDepartment) {
        return mongoOperations.save(chargeDepartment);
    }

    public UpdateResult updateCharge(ChargeDepartment chargeDepartment) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(chargeDepartment.getId()));
        Update update = new Update();
        update.set("id", chargeDepartment.getId());
        update.set("title", chargeDepartment.getTitle());
        update.set("code", chargeDepartment.getCode());
        update.set("description", chargeDepartment.getDescription());
        update.set("categories", chargeDepartment.getCategories());
        update.set("assetLocationId", chargeDepartment.getAssetLocationId());
        return mongoOperations.updateFirst(query, update, ChargeDepartment.class);
    }

    @Override
    public List<ChargeDepartment> findAllBudgetByFilterAndPagination(String term, String code, Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        } else {
            criteria.and("title").ne(null);
        }
        if (code != null && !code.equals("")) {
            criteria.and("code").regex(code);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                skipOperation,
                limitOperation,
                project()
                        .andExclude("description")
        );
        return this.aggregate(aggregation, ChargeDepartment.class, ChargeDepartment.class).getMappedResults();
    }

    @Override
    public long getAllCount(String term, String code) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        } else {
            criteria.and("title").ne(null);
        }
        if (code != null && !code.equals("")) {
            criteria.and("code").regex(code);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return this.aggregate(aggregation, ChargeDepartment.class, ChargeDepartment.class).getMappedResults().size();
    }

    @Override
    public List<ChargeDepartment> findAllBudget(Pageable pageable, Integer totalElement) {
        System.out.println("3333333");
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("deleted").ne(true))
                , skipOperation
        );
        Print.print(mongoOperations.aggregate(agg, ChargeDepartment.class, ChargeDepartment.class).getMappedResults());
        return mongoOperations.aggregate(agg, ChargeDepartment.class, ChargeDepartment.class).getMappedResults();
    }

    @Override
    public List<ChargeDepartment> getAllChargeDepartment() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("title").ne(null));
        query.fields().include("title");
        query.fields().include("code");
        return mongoOperations.find(query, ChargeDepartment.class);
    }

    @Override
    public boolean checkIfCodeIsUnique(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(code));
        return mongoOperations.exists(query, ChargeDepartment.class);
    }

    @Override
    public ChargeDepartment getChargeDepartmentTitle(String chargeDepartmentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(chargeDepartmentId));
        query.fields()
                .include("id")
                .include("title");
        return mongoOperations.findOne(query, ChargeDepartment.class);
    }

}
