package org.sayar.net.Dao.Mongo.activityDao.activity;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.Mongo.MyModel.ActivityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("activityDaoImp")
public class ActivityDaoImpl extends GeneralDaoImpl<Activity> implements ActivityDao {
    public ActivityDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public List<Activity> getAllLight(String organCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("orgId").is(organCode));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("title")
                .include("description")
                .include("orgId");
        return this.find(query, Activity.class);
    }

    @Override
    public List<Activity> findAllActivity(String term, Pageable pageable, Integer totalElement) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        }
        Query query = new Query();
        query.addCriteria(criteria)
                .with(pageable);
        query.fields()
                .include("id")
                .include("title");
        return mongoOperations.find(query, Activity.class);
    }

    @Override
    public List<Activity> getAllLimit() {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("deleted").ne(true)

                )
        );
        query.fields()
                .include("id")
                .include("title");
        return mongoOperations.find(query, Activity.class);
    }

    @Override
    public List<Activity> getAllActivity() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, Activity.class);
    }

    @Override
    public Activity geFormIdByActivityId(String activityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(activityId));
        query.fields().include("activityLevelList.formId");
        return mongoOperations.findOne(query, Activity.class);
    }

    @Override
    public List<Activity> getAssociatedActivityOfWorkRequest(List<String> activityIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(activityIdList));
        query.fields()
                .include("id")
                .include("title");
        return mongoOperations.find(query, Activity.class);
    }

    @Override
    public List<Activity> getActivityOfPendingUser(List<String> activityIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(activityIdList));
        return mongoOperations.find(query, Activity.class);
    }

    @Override
    public Activity getNeededActivityByActivityId(String activityId) {
        System.out.println("id" + activityId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(activityId));
        return mongoOperations.findOne(query, Activity.class);
    }

    @Override
    public List<ActivityLevel> getEachActivityLevelForm(String activityId, String activityLevelId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(activityId));
        query.fields()
                .include("activityLevelList.form")
                .include("activityLevelList.id");
        Activity activity = mongoOperations.findOne(query, Activity.class);
        if (activity != null && activity.getActivityLevelList() != null) {
            return activity.getActivityLevelList();
        } else
            return null;
    }

    @Override
    public void changeMentionedActivityFirsStepStatus(String activityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(activityId));
        mongoOperations.findOne(query, Activity.class);
    }

    @Override
    public Activity getActivityByActivityId(String activityId) {
        Query query = new Query();
//        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(activityId));
        return mongoOperations.findOne(query, Activity.class);
    }

    @Override
    public UpdateResult saveWorkOrderIdInActivity(String activityInstanceId, String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(activityInstanceId));
        Update update = new Update();
        update.set("workOrderId", workOrderId);
        return mongoOperations.updateFirst(query, update, Activity.class);
    }

    @Override
    public List<Activity> getListOfActivityForArrivedTimeScheduleMaintenance(List<String> activityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(activityId));
        return mongoOperations.find(query, Activity.class);
    }

    @Override
    public List<Activity> getActivitiesOfAsset(List<String> activityIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(activityIdList));
        query.fields()
                .include("id")
                .include("title");
        return mongoOperations.find(query, Activity.class);
    }

    @Override
    public boolean checkIfUserExistsActivity(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("Active").is(true));
        query.addCriteria(Criteria.where("activityLevelList").elemMatch(Criteria.where("userId").is(userId)));
        return mongoOperations.exists(query, Activity.class);
    }

    @Override
    public Activity getWorkRequestActivity(String activityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(activityId));
        query.fields()
                .include("id")
                .include("title");
        return mongoOperations.findOne(query, Activity.class);
    }

    @Override
    public Activity getActivityTitle(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        query.fields()
                .include("id")
                .include("title");
        return mongoOperations.findOne(query, Activity.class);
    }

    @Override
    public long countAllActivity(String term) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("title").regex(term);
        }
        Query query = new Query();
        query.addCriteria(criteria);
        return mongoOperations.count(query, Activity.class);
    }
}

