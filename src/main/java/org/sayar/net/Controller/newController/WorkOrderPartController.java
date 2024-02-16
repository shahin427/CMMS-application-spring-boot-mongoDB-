package org.sayar.net.Controller.newController;

import org.sayar.net.Model.newModel.WorkOrder.PartWithUsageCount;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintenanceBackupService;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Service.newService.WorkOrderPartService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("part-with-usage-count")
public class WorkOrderPartController {
    @Autowired
    private WorkOrderPartService workOrderPartService;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private ScheduleMaintenanceBackupService scheduleMaintenanceBackupService;

    @GetMapping("get-part-with-usage-count-list-by-reference-id")
    public ResponseEntity<?> getWorkOrderPartListByReferenceId(@PathParam("referenceId") String referenceId) {
        return ResponseEntity.ok().body(workOrderPartService.getWorkOrderPartListByReferenceId(referenceId));
    }

    @PostMapping("save")
    public ResponseEntity<?> postWorkOrderPart(@RequestBody PartWithUsageCount partWithUsageCount) {
        return ResponseEntity.ok().body(workOrderPartService.postWorkOrderPart(partWithUsageCount));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllWorkOrderPart() {
        return ResponseEntity.ok().body(workOrderPartService.getAllWorkOrderPart());
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneWorkOrderPart(@PathParam("partWithUsageCountId") String partWithUsageCountId) {
        return ResponseEntity.ok().body(workOrderPartService.getOneWorkOrderPart(partWithUsageCountId));
    }

    @DeleteMapping()
    public ResponseEntity<?> deletePartWithUsageCountById(@PathParam("partWithUsageCountId") String partWithUsageCountId) {
        return ResponseEntity.ok().body(workOrderPartService.logicDeleteById(partWithUsageCountId, PartWithUsageCount.class));
    }

    @PutMapping("update")
    public ResponseEntity<?> updatePArtWithUsageCount(@PathParam("partWithUsageCountId") String partWithUsageCountId,
                                                      @RequestBody PartWithUsageCount partWithUsageCount) {
        return ResponseEntity.ok().body(workOrderPartService.updatePArtWithUsageCount(partWithUsageCountId, partWithUsageCount));
    }

    @DeleteMapping("delete-schedule-used-part")
    public ResponseEntity<?> deleteScheduleUsedPart(@PathParam("partWithUsageCountId") String partWithUsageCountId, @PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        scheduleMaintenanceBackupService.deleteUsedPartOfScheduleBackup(partWithUsageCountId, scheduleMaintenanceId);
        return ResponseEntity.ok().body(workOrderPartService.logicDeleteById(partWithUsageCountId, PartWithUsageCount.class));
    }

    @GetMapping("get-part-with-usage-count-of-work-order")
    public ResponseEntity<?> getPartWithUsageCountOfWorkOrder(@PathParam("workOrderId") String workOrderId) {
        WorkOrder workOrder = workOrderService.getPartWithUsageCountOfWorkOrder(workOrderId);
        if (workOrder.getUsedParts() != null) {
            return ResponseEntity.ok().body(workOrderPartService.getPartWithUsageCountOfWorkOrder(workOrder.getUsedParts()));
        } else {
            return null;
        }
    }
}
