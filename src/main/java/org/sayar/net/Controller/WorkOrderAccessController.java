package org.sayar.net.Controller;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.WorkOrderAccess;
import org.sayar.net.Model.DTO.WorkOrderAccessDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RequestMapping("work-order-access")
@RestController

public class WorkOrderAccessController extends GeneralServiceImpl<WorkOrderAccess> {
    @Autowired
    private MongoOperations mongoOperations;

    @PostMapping("save")
    public ResponseEntity<?> createWorkOrderAccessInActivity(@RequestBody WorkOrderAccess workOrderAccess) {
        WorkOrderAccess createdWorkOrderAccess = mongoOperations.save(workOrderAccess);
        return ResponseEntity.ok().body(WorkOrderAccessDTO.map(createdWorkOrderAccess.getId()));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getWorkOrderAccessInActivity(@PathParam("workOrderAccessId") String workOrderAccessId) {
        if (workOrderAccessId == null) {
            return null;
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("deleted").ne(true));
            query.addCriteria(Criteria.where("id").is(workOrderAccessId));
            return ResponseEntity.ok().body(mongoOperations.findOne(query, WorkOrderAccess.class));
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> updateWorkOrderAccess(@PathParam("workOrderAccessId") String workOrderAccessId, @RequestBody WorkOrderAccess workOrderAccess) {

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderAccessId));
        Update update = new Update();
        update.set("code", workOrderAccess.isCode());
        update.set("title", workOrderAccess.isTitle());
        update.set("requiredCompletionDate", workOrderAccess.isRequiredCompletionDate());
        update.set("image", workOrderAccess.isImage());
        update.set("assetId", workOrderAccess.isAssetId());
        update.set("projectId", workOrderAccess.isProjectId());
        update.set("startDate", workOrderAccess.isStartDate());
        update.set("endDate", workOrderAccess.isEndDate());
        update.set("statusId", workOrderAccess.isStatusId());
        update.set("priority", workOrderAccess.isPriority());
        update.set("maintenanceType", workOrderAccess.isProjectId());
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, WorkOrderAccess.class);
        return ResponseEntity.ok().body(super.updateResultStatus(updateResult));
    }
}
