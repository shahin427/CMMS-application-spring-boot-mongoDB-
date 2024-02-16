package org.sayar.net.Model.newModel.Task.dao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.General.db.util.CommonQuery;
import org.sayar.net.Model.newModel.Task.model.Task;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("taskDaoImpl")
@Transactional
public class TaskDaoImpl extends GeneralDaoImpl<Task> implements TaskDao {
    @Autowired
    private MongoOperations mongoOperations;
    private final Class<Task> entityClass = Task.class;

    protected CommonQuery<Task> entityQuery() {
        return super.mongoQuery(entityClass);
    }

    public TaskDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public Task taskService(Task task) {
        if (task.getTaskGroupId() == null) {
            task.setForSchedule(true);
        }
        return mongoOperations.save(task);
    }

    @Override
    public List<Task> getAllTask(String term, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term == null && !term.equals("")) {
            criteria.and("term").regex(term);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
                , skipOperation
        );
        return mongoOperations.aggregate(aggregation, Task.class, Task.class).getMappedResults();
    }

    @Override
    public long countAllTask(String term) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(term));
        return mongoOperations.count(query, Task.class);
    }

    @Override
    public UpdateResult updateTask(Task task) {
        return entityQuery()
                .is(Task.FN.id, task.getId())
                .ne("deleted", true)
                .set(Task.FN.title, task.getTitle())
                .set(Task.FN.code, task.getCode())
                .set(Task.FN.timeEstimate, task.getTimeEstimate())
                .set(Task.FN.description, task.getDescription())
                .set(Task.FN.price, task.getPrice())
                .set(Task.FN.taskType, task.getTaskType())
                .set(Task.FN.users, task.getUsers())
                .set(Task.FN.referenceId, task.getReferenceId())
                .set(Task.FN.taskGroupId, task.getTaskGroupId())
                .updateFirst();
    }

    @Override
    public Task getOneTask(String taskId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(taskId));
        return mongoOperations.findOne(query, Task.class);
    }

    @Override
    public boolean checkTaskCode(String taskCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(taskCode));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.exists(query, Task.class);
    }

    @Override
    public List<Task> getAllTasksByTaskList(List<String> tasks) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(tasks));
        return mongoOperations.find(query, Task.class);
    }

    @Override
    public List<Task> getTaskListByReferenceId(String referenceId) {
        System.out.println(referenceId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("referenceId").is(referenceId));
        query.fields().exclude("description");
        query.fields().exclude("users");
        return mongoOperations.find(query, Task.class);
    }

    @Override
    public List<Task> getTaskListByTaskGroupId(String taskGroupId) {
        Print.print("taskGroupId", taskGroupId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("taskGroupId").is(taskGroupId));
        query.fields()
                .include("id")
                .include("title")
                .include("code")
                .include("taskType")
                .include("timeEstimate");
        return mongoOperations.find(query, Task.class);
    }

    @Override
    public boolean ifTaskExistsInTaskGroup(String taskId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(taskId));
        query.addCriteria(Criteria.where("taskGroupId").exists(true));
        return mongoOperations.exists(query, Task.class);
    }

    @Override
    public List<Task> getTasksOfWorkOrder(List<String> tasks) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(tasks));
        query.fields()
                .include("id")
                .include("users")
                .include("code")
                .include("title")
                .include("timeEstimate")
                .include("forSchedule")
                .include("taskType");
        return mongoOperations.find(query, Task.class);
    }

    @Override
    public Task saveTaskOfWorkOrder(Task task) {
        return mongoOperations.save(task);
    }
}
