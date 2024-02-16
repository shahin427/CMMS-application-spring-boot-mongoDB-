package org.sayar.net.Dao.NewDao;

import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Mongo.UserTask;
import org.sayar.net.Model.newModel.Task.model.LaborTask;
import org.sayar.net.Security.TokenPrinciple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("luberTaskDaoImpl")
@Transactional
public class LaborTaskDaoImpl extends GeneralDaoImpl<LaborTask> implements LaborTaskDao {


    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private TokenPrinciple tokenPrinciple;

    public LaborTaskDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


//    public UserTask(String id, LaborTaskType taskType, String description, String startTime, long estimate,
//                    long userComplete, String endTime, long actualTime,
//                    String descriptionComplete, long workOrderId, TaskStatus taskStatus) {
    @Override
    public LaborTask addTask(LaborTask task, String workOrderId) {
//        UserTask userTask=new UserTask(
//                new ObjectId().toString(),
//                task.getTaskType(),
//                task.getDescription(),
//                task.getStartTime(),
//                task.getEstimate(),
//                task.getUserComplete(),
//                workOrderId,
//                TaskStatus,
//                tokenPrinciple.getOrganCode());
//        mongoOperations.save(userTask);
//        super.add(tasks,tokenPrinciple.getOrganCode());

        return null;
    }

    @Override
    public List<UserTask> myTask() {
        Criteria criteria=new Criteria();
//        criteria.and("organId").is(tokenPrinciple.getOrganCode());
        criteria.andOperator(
//                Criteria.where("organId").is(tokenPrinciple.getOrganCode()),
                Criteria.where("userComplete").is(tokenPrinciple.getUserId())
        );
        Query query=new Query();
        query.addCriteria(criteria);
        List<UserTask> userTaskList=mongoOperations.find(query,UserTask.class);
        return userTaskList;
    }

    @Override
    public List<UserTask> workOrderTask(String workOrderId) {
        Criteria criteria=new Criteria();
//        criteria.and("organId").is(tokenPrinciple.getOrganCode());
        criteria.andOperator(
//                Criteria.where("organId").is(tokenPrinciple.getOrganCode()),
                Criteria.where("workOrderId").is(workOrderId)
        );
        Query query=new Query();
        query.addCriteria(criteria);
        List<UserTask> userTaskList=mongoOperations.find(query,UserTask.class);
        return userTaskList;
    }

    @Override
    public boolean updateTask(UserTask userTask){

        if(userTask.getUserComplete()==tokenPrinciple.getUserId()){
            mongoOperations.save(userTask);
            return true;
        }
        return false;
    }
}
