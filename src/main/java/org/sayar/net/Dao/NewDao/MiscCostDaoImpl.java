package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Enumes.MiscCostType;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.WorkOrderMiscCostDTO;
import org.sayar.net.Model.newModel.MiscCost;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("miscCostDaoImpl")
@Transactional
public class MiscCostDaoImpl extends GeneralDaoImpl<MiscCost> implements MiscCostDao {

    @Autowired
    private MongoOperations mongoOperations;

    public MiscCostDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public MiscCost getOneById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.findOne(query, MiscCost.class);
    }

    @Override
    public MiscCost postMiscCostController(MiscCost miscCost) {
        return mongoOperations.save(miscCost);
    }

    @Override
    public UpdateResult updateMiscCost(MiscCost miscCost) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(miscCost.getId()));
        query.addCriteria(Criteria.where("deleted").ne(true));
        Update update = new Update();
        update.set("title", miscCost.getTitle());
        update.set("estimatedQuantity", miscCost.getEstimatedQuantity());
        update.set("estimatedUnitCost", miscCost.getEstimatedQuantity());
        update.set("estimatedTotalCost", miscCost.getEstimatedTotalCost());
        update.set("quantity", miscCost.getQuantity());
        update.set("actualUnitCost", miscCost.getActualUnitCost());
        update.set("actualTotalCost", miscCost.getActualTotalCost());
        update.set("description", miscCost.getDescription());
        update.set("miscCostType", miscCost.getMiscCostType());
        return mongoOperations.updateFirst(query, update, MiscCost.class);
    }

    @Override
    public List<MiscCost> getAllMiscCostByMiscCost(List<String> miscCosts) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(miscCosts));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, MiscCost.class);
    }

    @Override
    public List<MiscCost> getMiscCostListByIdList(WorkOrderMiscCostDTO workOrderMiscCostDTO) {
        return null;
    }

    @Override
    public boolean updateMiscCostListByMiscCostIdList(WorkOrder workOrder, WorkOrderMiscCostDTO workOrderMiscCostDTO) {
       return true;
    }

    @Override
    public List<MiscCost> getMiscCostListByReferenceId(String referenceId) {
        Query query=new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("referenceId").is(referenceId));
        return mongoOperations.find(query,MiscCost.class);
    }
}

