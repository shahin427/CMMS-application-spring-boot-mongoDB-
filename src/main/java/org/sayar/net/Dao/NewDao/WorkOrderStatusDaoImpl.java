package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.Enum.WorkOrderStatusEnum;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;
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
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.sayar.net.Model.newModel.Enum.WorkOrderStatusEnum.*;


@Repository
public class WorkOrderStatusDaoImpl extends GeneralDaoImpl<WorkOrderStatus> implements WorkOrderStatusDao {
    public WorkOrderStatusDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public boolean checkWorkOrderStatusExistence(WorkOrderStatusEnum statusEnum, String persianName) {
        return false;
    }

    @Override
    public WorkOrderStatus getWorkOrderStatusDefault(WorkOrderStatusEnum workOrderStatusEnum) {
        Query query = new Query();
//        query.addCriteria(Criteria.where("status").is(WorkOrderStatusEnum.REQUESTED));
        return this.findOne(query, WorkOrderStatus.class);
    }

    @Override
    public WorkOrderStatus saveStatus(WorkOrderStatus workOrderStatus) throws ApiRepetitiveException {
        return mongoOperations.save(workOrderStatus);
    }

    @Override
    public WorkOrderStatus updateStatus(WorkOrderStatus workOrderStatus) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("name").is(workOrderStatus.getName()),
                        Criteria.where("deleted").ne(true),
                        Criteria.where("id").ne(workOrderStatus.getId())
                )
        );
        WorkOrderStatus workOrderStatus1 = mongoOperations.findOne(query, WorkOrderStatus.class);
        if (workOrderStatus1 == null) {
            System.out.println("Not_exist");
            mongoOperations.save(workOrderStatus);
            return workOrderStatus;
        } else {
            System.out.println("Exist");
            throw new ApiRepetitiveException();
        }
    }

    @Override
    public long getAllCount(String term, String status) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (status != null) {
            criteria.and("status").is(status);
        }
        if (term != null && !term.equals("")) {
            criteria.and("name").is(term);
        }
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(agg, WorkOrderStatus.class, WorkOrderStatus.class).getMappedResults().size();
    }

    @Override
    public List<WorkOrderStatus> findAllWorkOrderStatus(String term, String status, Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        if (status != null) {
            criteria.and("status").is(status);
        }
        if (term != null && !term.equals("")) {
            criteria.and("name").regex(term);
        }
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria)
                , skipOperation
                , limitOperation
        );
        return mongoOperations.aggregate(agg, WorkOrderStatus.class, WorkOrderStatus.class).getMappedResults();
    }

    @Override
    public WorkOrderStatus getWorkOrderStatusByAssetId(String workOrderStatusId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderStatusId));
        return mongoOperations.findOne(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getWorkOrderStatus() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getWorkOrderStatusListByWorkOrderStatusId(List<String> statusIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(statusIdList));
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getAllWorkOrderStatusByWorkOrderStatusId(List<String> statusIdList) {
        Print.print("dfcwer3g", statusIdList);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(statusIdList));
        query.fields().include("id").include("name");
        Print.print("dcdcd", mongoOperations.find(query, WorkOrderStatus.class));
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public WorkOrderStatus getWorkOrderStatusByWorkOrderStatusId(String workOrderStatusId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderStatusId));
        return mongoOperations.findOne(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getOpenWorkOrderStatus() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(OPENED));
        query.fields().include("id");
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getClosedWorkOrderStatus() {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("deleted").ne(true),
                Criteria.where("status").is(CLOSED)
        ));
        query.fields().include("id");
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getClosedNotCompletedWorkOrdersStatus() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(CLOSED));
        query.fields().include("id");
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getAllOpenWorkOrderStatus() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(OPENED));
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getAllClosedCompleteAndUnCompleteWorkOrders() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(CLOSED));
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getTotalWorkOrdersExceptClosed() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").ne(CLOSED));
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getWorkOrderStatusListExceptDraft() {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("deleted").ne(true),
                Criteria.where("status").ne(DRAFT)
        ));
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> numberOfPendingWorkOrderOfUserInSpecificTime() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(PENDING));
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getWorkOrderStatusListExceptDraftAndClose() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("status").ne(DRAFT),
                Criteria.where("status").ne(CLOSED)
        ));
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getClosedCompletedWorkOrdersStatus() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(CLOSED));
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public WorkOrderStatus getWorkOrderStatusById(String statusId) {
        System.out.println(statusId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(statusId));
        Print.print("test test", mongoOperations.findOne(query, WorkOrderStatus.class));
        return mongoOperations.findOne(query, WorkOrderStatus.class);
    }

    @Override
    public WorkOrderStatus getAssociatedWorkOrderStatusToTheSchedule(String statusId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(statusId));
        query.fields()
                .include("id")
                .include("name");
        return mongoOperations.findOne(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getAllWorkOrderStatus() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("id");
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getOpenedDraftAndPendingWorOrderStatus() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(new Criteria().orOperator(
                (Criteria.where("status").is(OPENED))
                , (Criteria.where("status").is(PENDING))
                , (Criteria.where("status").is(DRAFT))
        ));

        query.fields()
                .include("id");
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getAllWorkOrderStatusExceptClose() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(new Criteria().orOperator(
                (Criteria.where("status").is(OPENED))
                , (Criteria.where("status").is(PENDING))
                , (Criteria.where("status").is(DRAFT))
        ));
        query.fields()
                .include("id");
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getPendingWorkOrderStatus() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(PENDING));
        query.fields()
                .include("id");
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public List<WorkOrderStatus> getDraftWorkOrderStatus() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("status").is(DRAFT));
        query.fields()
                .include("id");
        return mongoOperations.find(query, WorkOrderStatus.class);
    }

    @Override
    public boolean checkIfNameExists(String name) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("name").is(name));
        return mongoOperations.exists(query, WorkOrderStatus.class);
    }
}
