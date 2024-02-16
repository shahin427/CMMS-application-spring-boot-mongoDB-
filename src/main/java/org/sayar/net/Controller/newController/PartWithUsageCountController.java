package org.sayar.net.Controller.newController;

import org.sayar.net.Model.newModel.WorkOrder.PartWithUsageCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PartWithUsageCountController {
    @Autowired
    private MongoOperations mongoOperations;

    public boolean getUsedPartsInWorkOrders(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("partId").is(partId));
        query.addCriteria(Criteria.where("referenceId").ne(null));
        return mongoOperations.exists(query, PartWithUsageCount.class);
    }
}
