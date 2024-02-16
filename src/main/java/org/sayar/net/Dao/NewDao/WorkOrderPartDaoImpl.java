package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.WorkOrder.PartWithUsageCount;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WorkOrderPartDaoImpl extends GeneralDaoImpl<PartWithUsageCount> implements WorkOrderPartDao {


    @Autowired
    private MongoOperations mongoOperations;

    public WorkOrderPartDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public List<PartWithUsageCount> getAllWorkOrderPartListByWorkOrderId(List<String> workOrderParts) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(workOrderParts));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, PartWithUsageCount.class);
    }

    @Override
    public List<PartWithUsageCount> getWorkOrderPartListByReferenceId(String referenceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("referenceId").is(referenceId));
        return mongoOperations.find(query, PartWithUsageCount.class);
    }

    @Override
    public PartWithUsageCount postWorkOrderPart(PartWithUsageCount partWithUsageCount) {
        return mongoOperations.save(partWithUsageCount);

    }

    @Override
    public List<PartWithUsageCount> getAllWorkOrderPart() {
        Query query = new Query();
        query.addCriteria(Criteria.where("'deleted").ne(true));
        return mongoOperations.find(query, PartWithUsageCount.class);
    }

    @Override
    public PartWithUsageCount getOneWorkOrderPart(String partWithUsageCountId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partWithUsageCountId));
        return mongoOperations.findOne(query, PartWithUsageCount.class);
    }

    @Override
    public UpdateResult updateWorkOrderPart(PartWithUsageCount partWithUsageCount) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partWithUsageCount.getId()));
        Update update = new Update();
        update.set("planedQuantity", partWithUsageCount.getPlanedQuantity());
        update.set("actualQuantity", partWithUsageCount.getActualQuantity());
        update.set("partId", partWithUsageCount.getPartId());
        update.set("referenceId", partWithUsageCount.getReferenceId());
        return mongoOperations.updateFirst(query, update, PartWithUsageCount.class);
    }

    @Override
    public boolean checkIfInventoryExistsInWorkOrder(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("partId").is(partId));
        Print.print(mongoOperations.exists(query, PartWithUsageCount.class));
        return mongoOperations.exists(query, PartWithUsageCount.class);
    }

    @Override
    public PartWithUsageCount getPartWithUsageCountListByReferenceId(String referenceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("referenceId").is(referenceId));
        return mongoOperations.findOne(query, PartWithUsageCount.class);
    }

    @Override
    public UpdateResult updatePArtWithUsageCount(String partWithUsageCountId, PartWithUsageCount partWithUsageCount) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(partWithUsageCountId));
        Update update = new Update();
        update.set("planedQuantity", partWithUsageCount.getPlanedQuantity());
        update.set("actualQuantity", partWithUsageCount.getActualQuantity());
        update.set("PartId", partWithUsageCount.getPartId());
        update.set("partName", partWithUsageCount.getPartName());
//        update.set("referenceId", partWithUsageCount.getReferenceId());
        return mongoOperations.updateFirst(query, update, PartWithUsageCount.class);
    }

    @Override
    public boolean ifThePartUsedBeforeInTheWorkOrderPart(PartWithUsageCount partWithUsageCount) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("partId").is(partWithUsageCount.getPartId()));
        query.addCriteria(Criteria.where("referenceId").is(partWithUsageCount.getReferenceId()));
        return mongoOperations.exists(query, PartWithUsageCount.class);
    }

    @Override
    public List<PartWithUsageCount> getPartOfRepository(List<String> usedParts) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(usedParts));
        return mongoOperations.find(query, PartWithUsageCount.class);
    }

    @Override
    public List<PartWithUsageCount> getPartWithUsageCountOfWorkOrder(List<String> usedParts) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(usedParts));
        return mongoOperations.find(query, PartWithUsageCount.class);
    }
}
