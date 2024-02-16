package org.sayar.net.Model.newModel.Task.controller;

import org.sayar.net.Model.newModel.Task.model.TaskGroup;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintananceService;
import org.sayar.net.Service.newService.TaskGroupService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("task-group")
public class TaskGroupController {

    @Autowired
    private TaskGroupService taskGroupService;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private ScheduleMaintananceService scheduleMaintananceService;

    @GetMapping("get-all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(taskGroupService.findAllNotLogicDeleted(TaskGroup.class,"name","code"));
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@PathParam("taskGroupId") String taskGroupId) {
        if (workOrderService.ifTaskGroupExistsInWorkOrder(taskGroupId)) {
            return ResponseEntity.ok().body("\"برای خذف این مجموعه کار ابتدا آن را از قسمت دستور کارها پاک کنید\"");
        } else if (scheduleMaintananceService.ifTaskGroupExistsInScheduleMaintenance(taskGroupId)) {
            return ResponseEntity.ok().body("\"برای خذف این مجموعه کار ابتدا آن را از قسمت زمانبندی نت پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(taskGroupService.logicDeleteById(taskGroupId, TaskGroup.class));
        }
    }

    @PostMapping("save")
    public ResponseEntity<?> postTaskGroup(@RequestBody TaskGroup taskGroup) {
        return ResponseEntity.ok().body(taskGroupService.postTaskGroup(taskGroup));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOne(@PathParam(("taskGroup")) String taskGroupId) {
        return ResponseEntity.ok().body(taskGroupService.getOne(taskGroupId));
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAll(@PathParam("term") String term,@PathParam("code") String code, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(taskGroupService.getAllByPagination(term, code, pageable, totalElement));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateTaskGroup(@RequestBody TaskGroup taskGroup) {
        return ResponseEntity.ok().body(taskGroupService.updateTaskGroup(taskGroup));
    }

    @GetMapping("check-task-group-code")
    public ResponseEntity<?> checkTaskGroupCode(@PathParam("taskGroupCode") String taskGroupCode) {
        return ResponseEntity.ok().body(taskGroupService.checkTaskGroupCode(taskGroupCode));
    }

    @GetMapping("check-if-code-is-unique")
    public ResponseEntity<?> checkIfTaskGroupCodeIsUnique(@PathParam("code") String code) {
        return ResponseEntity.ok().body(taskGroupService.checkIfTaskGroupCodeIsUnique(code));
    }


    @GetMapping("get-all-task-group-for-notice-board")
    public ResponseEntity<?> getAllTaskGroupForNoticeBoard() {
        return ResponseEntity.ok().body(taskGroupService.getAllTaskGroupForNoticeBoard());
    }
}
