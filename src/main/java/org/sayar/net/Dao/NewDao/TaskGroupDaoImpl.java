package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.TaskGroupDTO;
import org.sayar.net.Model.newModel.Task.model.TaskGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskGroupDaoImpl extends GeneralDaoImpl<TaskGroup> implements TaskGroupDao {


    @Autowired
    private MongoOperations mongoOperations;
    private Criteria criteria;

    public TaskGroupDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

//    @Autowired
//    private Class<TaskGroup> entityClass = TaskGroup.class;

//    @Autowired
//    protected CommonQuery<TaskGroup> entityQuery() {
//        return super.mongoQuery(entityClass);
//    }

    @Override
    public TaskGroup postTaskGroup(TaskGroup taskGroup) {
        return mongoOperations.save(taskGroup);
    }

    @Override
    public TaskGroup getOne(String taskGroupId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(taskGroupId));
        return mongoOperations.findOne(query, TaskGroup.class);
    }

    @Override
    public List<TaskGroup> getAll(String term, String code, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("name").regex(term);
        }
        if (code != null && !code.equals("")) {
            criteria.and("code").regex(code);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
                , skipOperation
                , limitOperation
        );
        return mongoOperations.aggregate(aggregation, TaskGroup.class, TaskGroup.class).getMappedResults();
    }

    @Override
    public UpdateResult updateTaskGroup(TaskGroup taskGroup) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(taskGroup.getId()));
        Update update = new Update();
        update.set("code", taskGroup.getCode());
        update.set("name", taskGroup.getName());
        update.set("document", taskGroup.getDocuments());
        return mongoOperations.updateFirst(query, update, TaskGroup.class);
    }

    @Override
    public boolean checkTaskGroupCode(String taskGroupCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("code").is(taskGroupCode));
        query.addCriteria(Criteria.where("deleted").ne(true));
        TaskGroup taskGroup = mongoOperations.findOne(query, TaskGroup.class);
        return taskGroup != null;
    }

    @Override
    public List<TaskGroup> getAllTaskGroupByTaskGroupList(List<String> taskGroups) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(taskGroups));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, TaskGroup.class);
    }

    @Override
    public List<TaskGroupDTO> getAllTaskGroupListByIdList(List<String> taskGroupList) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").in(taskGroupList);
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("task")
                .localField("tasksObj")
                .foreignField("_id")
                .as("taskList");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.unwind("tasks", true),
                Aggregation.project()
                        .and((ConvertOperators.ToObjectId.toObjectId("$tasks"))).as("tasksObj")
                        .and("id").as("id")
                        .and("code").as("code")
                        .and("name").as("name"),
                Aggregation.group("id")
                        .first("code").as("code")
                        .first("name").as("name")
                        .push("tasksObj").as("tasksObj"),
                lookupOperation
        );
        return mongoOperations.aggregate(aggregation, TaskGroup.class, TaskGroupDTO.class).getMappedResults();
    }

    @Override
    public long countAllTaskGroupByTerm(String term, String code) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (term != null && !term.equals("")) {
            criteria.and("name").regex(term);
        }
        if (code != null && !code.equals("")) {
            criteria.and("code").is(code);
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, TaskGroup.class, TaskGroup.class).getMappedResults().size();
    }

    @Override
    public boolean checkIfTaskGroupCodeIsUnique(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(code));
        return mongoOperations.exists(query, TaskGroup.class);
    }

    @Override
    public List<TaskGroupDTO> getAllTaskGroupForNoticeBoard() {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("task")
                .localField("tasksObj")
                .foreignField("_id")
                .as("taskList");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.unwind("tasks", true),
                Aggregation.project()
                        .and((ConvertOperators.ToObjectId.toObjectId("$tasks"))).as("tasksObj")
                        .and("id").as("id")
                        .and("code").as("code")
                        .and("name").as("name"),
                Aggregation.group("id")
                        .first("code").as("code")
                        .first("name").as("name")
                        .push("tasksObj").as("tasksObj"),
                lookupOperation
        );
        return mongoOperations.aggregate(aggregation, TaskGroup.class, TaskGroupDTO.class).getMappedResults();
    }

    @Override
    public void pushTaskIdToTaskGroup(String taskId, String taskGroupId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(taskGroupId));
        Update update = new Update();
        update.push("tasks", taskId);
        mongoOperations.updateFirst(query, update, TaskGroup.class);
    }

    @Override
    public List<TaskGroupDTO> getAllTaskGroupOfWorkOrder(List<String> taskGroups) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").in(taskGroups);
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("task")
                .localField("tasksObj")
                .foreignField("_id")
                .as("taskList");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.unwind("tasks", true),
                Aggregation.project()
                        .and((ConvertOperators.ToObjectId.toObjectId("$tasks"))).as("tasksObj")
                        .and("id").as("id")
                        .and("code").as("code")
                        .and("name").as("name"),
                Aggregation.group("id")
                        .first("code").as("code")
                        .first("name").as("name")
                        .push("tasksObj").as("tasksObj"),
                lookupOperation
        );
        return mongoOperations.aggregate(aggregation, TaskGroup.class, TaskGroupDTO.class).getMappedResults();
    }
}
