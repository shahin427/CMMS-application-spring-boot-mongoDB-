package org.sayar.net.Dao;

import org.sayar.net.Controller.newController.NewWorkRequestDTO;
import org.sayar.net.Model.DTO.WorkRequestSearchDTO;
import org.sayar.net.Model.WorkRequest;
import org.sayar.net.Model.WorkRequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WorkRequestDaoImpl implements WorkRequestDao {
    @Autowired
    private MongoOperations mongoOperations;

    public WorkRequest createWorkRequest(WorkRequest workRequest, String activityInstanceId) {
        workRequest.setActivityInstanceId(activityInstanceId);
        workRequest.setWorkRequestStatus(WorkRequestStatus.inProcess);
        return mongoOperations.save(workRequest);
    }

    @Override
    public NewWorkRequestDTO getOneWorkRequest(String workRequestId) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(workRequestId);

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("description").as("description")
                        .and("priority").as("priority")
                        .and("maintenanceType").as("maintenanceType")
                        .and("failureDate").as("failureDate")
                        .and(ConvertOperators.ToObjectId.toObjectId("$userId")).as("userId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$assetId")).as("assetId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$activityId")).as("activityId"),
                Aggregation.lookup("asset", "assetId", "_id", "asset"),
                Aggregation.lookup("user", "userId", "_id", "user"),
                Aggregation.lookup("activity", "activityId", "_id", "activity"),
                Aggregation.project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("description").as("description")
                        .and("priority").as("priority")
                        .and("maintenanceType").as("maintenanceType")
                        .and("failureDate").as("failureDate")
                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")
                        .and("asset.code").as("assetCode")
                        .and("activity._id").as("activityId")
                        .and("activity.title").as("activityName")
                        .and("user.name").as("userName")
                        .and("user.family").as("userFamily")
        );
        return mongoOperations.aggregate(aggregation, WorkRequest.class, NewWorkRequestDTO.class).getUniqueMappedResult();
    }

    @Override
    public List<WorkRequest> getAllWorkRequest(Pageable pageable, Integer element, WorkRequestSearchDTO workRequestSearchDTO, String userId) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria;
        List<Criteria> criteriaList = new ArrayList<>();

        if (workRequestSearchDTO.getName() != null && !workRequestSearchDTO.getName().equals(""))
            criteriaList.add(Criteria.where("title").regex(workRequestSearchDTO.getName()));

        if (workRequestSearchDTO.getWorkRequestStatus() != null)
            criteriaList.add(Criteria.where("workRequestStatus").is(workRequestSearchDTO.getWorkRequestStatus()));

        if (workRequestSearchDTO.getAssetId() != null)
            criteriaList.add(Criteria.where("assetId").is(workRequestSearchDTO.getAssetId()));

        criteriaList.add(Criteria.where("deleted").ne(true));
        criteriaList.add(Criteria.where("userId").is(userId));
        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.sort(Sort.Direction.DESC, "requestDate"),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, WorkRequest.class, WorkRequest.class).getMappedResults();
    }

    @Override
    public long getNumberOfWorkRequestPage(WorkRequestSearchDTO workRequestSearchDTO, String userId) {
        Criteria criteria;
        List<Criteria> criteriaList = new ArrayList<>();
        if (workRequestSearchDTO.getName() != null) {
            criteriaList.add(Criteria.where("title").regex(workRequestSearchDTO.getName()));
        }

        if (workRequestSearchDTO.getAssetId() != null) {
            criteriaList.add(Criteria.where("assetId").is(workRequestSearchDTO.getAssetId()));
        }
        criteriaList.add(Criteria.where("deleted").ne(true));
        criteriaList.add(Criteria.where("userId").is(userId));
        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, WorkRequest.class, WorkRequest.class).getMappedResults().size();
    }

    @Override
    public WorkRequest getWorkRequestForActivitySampleValidation(String workRequestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workRequestId));
        query.fields().include("activityInstanceId");
        return mongoOperations.findOne(query, WorkRequest.class);
    }

    @Override
    public WorkRequest getWorkRequesterSpecification(String instanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(instanceId));
        query.fields()
                .include("userId")
                .include("requestDate");
        return mongoOperations.findOne(query, WorkRequest.class);
    }

    @Override
    public List<WorkRequest> getRelevantWorkRequests(List<String> relevantWorkRequestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").in(relevantWorkRequestId));
        query.fields()
                .include("id")
                .include("requestDate")
                .include("assetId")
                .include("activityInstanceId");
        return mongoOperations.find(query, WorkRequest.class);
    }

    @Override
    public List<WorkRequest> getWorkRequestOfTheUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoOperations.find(query, WorkRequest.class);
    }

    @Override
    public boolean ifAssetExistsInWorkRequest(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetId").is(assetId));
        return mongoOperations.exists(query, WorkRequest.class);
    }

    @Override
    public WorkRequest getRequesterUser(String requestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(requestId));
        return mongoOperations.findOne(query, WorkRequest.class);
    }

    @Override
    public void changeWorkRequestInProcessStatusToFalse(String workRequestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workRequestId));
        Update update = new Update();
        update.set("workRequestStatus", WorkRequestStatus.finished);
        update.set("hasAssessment", true);
        mongoOperations.updateFirst(query, update, WorkRequest.class);
    }

    @Override
    public void changeWorkRequestStatus(String workRequestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workRequestId));
        Update update = new Update();
        update.set("workRequestStatus", WorkRequestStatus.rejected);
        mongoOperations.updateFirst(query, update, WorkRequest.class);
    }

    @Override
    public void workRequestStatusIsInProcess(String workRequestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workRequestId));
        Update update = new Update();
        update.set("workRequestStatus", WorkRequestStatus.inProcess);
        mongoOperations.updateFirst(query, update, WorkRequest.class);
    }

    @Override
    public void setAssessmentTrue(String workRequestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(workRequestId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        Update update = new Update();
        update.set("hasAssessment", true);
        mongoOperations.updateFirst(query, update, WorkRequest.class);
    }

    @Override
    public void setAssessmentFalse(String workRequestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(workRequestId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        Update update = new Update();
        update.set("hasAssessment", false);
        mongoOperations.updateFirst(query, update, WorkRequest.class);
    }
}
