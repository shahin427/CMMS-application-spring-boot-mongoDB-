package org.sayar.net.Dao.activityType;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.ActivityType;
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


@Repository("ActivityTypeDaoImpl")
public class ActivityTypeDaoImpl extends GeneralDaoImpl<ActivityType> implements ActivityTypeDao {

    @Autowired
    private MongoOperations mongoOperations;

    public ActivityTypeDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


    @Override
    public ActivityType createActivityType(ActivityType activityType) {
        return mongoOperations.save(activityType);
    }

    @Override
    public ActivityType getOneActivityType(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        return mongoOperations.findOne(query, ActivityType.class);
    }

    @Override
    public Boolean updateActivityType(ActivityType userType) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(userType.getId()));
        Update update = new Update();
        update.set("name", userType.getName());
        UpdateResult res = mongoOperations.updateFirst(query, update, ActivityType.class);
        if (res != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteActivityType(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        DeleteResult res = mongoOperations.remove(query, ActivityType.class);
        if (res != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean checkUsed(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("activityTypeId").is(id));
        return mongoOperations.exists(query, WorkOrderSchedule.class);
    }

    @Override
    public List<ActivityType> getAllType() {
        Query query = new Query();
        return mongoOperations.find(query, ActivityType.class);
    }

    @Override
    public Page<ActivityType> getPage(String term, Pageable pageable, Integer total) {
        Criteria criteria = new Criteria();
        if (term != null && !term.isEmpty()) {
            criteria.and("name").regex(term);
        }
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria)
                , skip(pageable.getPageNumber() * pageable.getPageSize())
                , limit(pageable.getPageSize())
        );
        AggregationResults<ActivityType> groupResults
                = super.aggregate(agg, ActivityType.class, ActivityType.class);
        List<ActivityType> result = groupResults.getMappedResults();
        if (total <= 0) {
            Aggregation aggCount = Aggregation.newAggregation(
                    Aggregation.match(criteria)
            );
            AggregationResults<ActivityType> groupResultsCount
                    = super.aggregate(aggCount, ActivityType.class, ActivityType.class);
            List<ActivityType> resultcount = groupResultsCount.getMappedResults();
            total = resultcount.size();
        }
        Page<ActivityType> res = new PageImpl<ActivityType>(result, pageable, total);
        return res;
    }


}
