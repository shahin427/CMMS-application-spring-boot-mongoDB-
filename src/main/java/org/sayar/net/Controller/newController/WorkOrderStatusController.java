package org.sayar.net.Controller.newController;

import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintananceService;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;
import org.sayar.net.Service.newService.WorkOrderService;
import org.sayar.net.Service.newService.WorkOrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("work-order-status")
public class WorkOrderStatusController {

    private final WorkOrderStatusService service;
    private final WorkOrderService workOrderService;
    private final ScheduleMaintananceService scheduleMaintananceService;

    @Autowired
    public WorkOrderStatusController(WorkOrderStatusService service, WorkOrderService workOrderService, ScheduleMaintananceService scheduleMaintananceService) {
        this.service = service;
        this.workOrderService = workOrderService;
        this.scheduleMaintananceService = scheduleMaintananceService;
    }

    @PostMapping("save")
    public ResponseEntity<?> saveStatus(@RequestBody WorkOrderStatus workOrderStatus) {
        return ResponseEntity.ok().body(service.saveStatus(workOrderStatus));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateStatus(@RequestBody WorkOrderStatus workOrderStatus) {
        return new ResponseContent().sendOkResponseEntity("", service.updateStatus(workOrderStatus));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteOne(@RequestParam("workOrderStatusId") String workOrderStatusId) {
        if (workOrderService.ifWorkStatusExistsInWorkOrder(workOrderStatusId)) {
            return ResponseEntity.ok().body("\"برای حذف این حالت کار ابتدا آن را از دستور کارها پاک کنید\"");
        } else if (scheduleMaintananceService.ifWorkStatusExistsInScheduleMaintenance(workOrderStatusId)) {
            return ResponseEntity.ok().body("\"برای حذف این حالت کار ابتدا آن را از زمانبندی برنامه ریزی شده پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(service.logicDeleteById(workOrderStatusId, WorkOrderStatus.class));
        }
    }

    @GetMapping("get-one")
    public ResponseEntity<?> gteOne(@RequestParam("id") String id) {
        return ResponseEntity.ok().body(service.findOneById(id, WorkOrderStatus.class));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(service.findAllNotLogicDeleted(WorkOrderStatus.class));
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllByTermAndPagination(@PathParam("term") String term, @PathParam("status") String status, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllByTermAndPagination(term, status, pageable, totalElement));
    }

    @GetMapping("check-if-name-exists")
    public ResponseEntity<?> checkIfNameExists(@PathParam("name") String name) {
        return ResponseEntity.ok().body(service.checkIfNameExists(name));
    }
}

