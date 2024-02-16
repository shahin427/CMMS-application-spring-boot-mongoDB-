package org.sayar.net.Dao.WorkCategoty;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.WorkCategory;
import org.sayar.net.Model.WorkOrderSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.skip;


@Repository("WorkCategoryDaoImpl")
public class WorkCategoryDaoImpl extends GeneralDaoImpl<WorkCategory> implements WorkCategoryDao {

    @Autowired
    private MongoOperations mongoOperations;

    public WorkCategoryDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


    @Override
    public WorkCategory createWorkCategory(WorkCategory workCategory) {
        return mongoOperations.save(workCategory);
    }

    @Override
    public WorkCategory getOneWorkCategory(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoOperations.findOne(query, WorkCategory.class);
    }

    @Override
    public Boolean updateWorkCategory(WorkCategory userType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userType.getId()));
        Update update = new Update();
        update.set("name", userType.getName());
        UpdateResult res = mongoOperations.updateFirst(query, update, WorkCategory.class);
        if (res != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteWorkCategory(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        DeleteResult res = mongoOperations.remove(query, WorkCategory.class);
        if (res != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean checkUsed(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("workCategoryId").is(id));
        return  mongoOperations.exists(query, WorkOrderSchedule.class);
    }

    @Override
    public List<WorkCategory> getAllType() {
        Query query = new Query();
        return mongoOperations.find(query, WorkCategory.class);
    }


    @Override
    public Page<WorkCategory> getPage(String term, Pageable pageable, Integer total) {
        Criteria criteria = new Criteria();
        if (term !=null &&!term.isEmpty()) {
            criteria.and("name").regex(term);
        }
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria)
                , skip(pageable.getPageNumber() * pageable.getPageSize())
                , limit(pageable.getPageSize())
        );
        AggregationResults<WorkCategory> groupResults
                = super.aggregate(agg, WorkCategory.class, WorkCategory.class);
        List<WorkCategory> result = groupResults.getMappedResults();

        if (total <= 0) {
            Aggregation aggCount = Aggregation.newAggregation(
                    Aggregation.match(criteria)
            );
            AggregationResults<WorkCategory> groupResultsCount
                    = super.aggregate(aggCount, WorkCategory.class, WorkCategory.class);
            List<WorkCategory> resultcount = groupResultsCount.getMappedResults();
            total = resultcount.size();
        }
        Page<WorkCategory> res = new PageImpl<WorkCategory>(result, pageable, total);
        return res;
    }

}
