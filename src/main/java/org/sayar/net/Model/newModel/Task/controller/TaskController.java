package org.sayar.net.Model.newModel.Task.controller;

import org.sayar.net.Model.newModel.Task.model.Task;
import org.sayar.net.Model.newModel.Task.service.TaskService;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintenanceBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ScheduleMaintenanceBackupService scheduleMaintenanceBackupService;

    @PostMapping("save")
    public ResponseEntity<?> postTask(@RequestBody Task task) {
        return ResponseEntity.ok().body(taskService.taskService(task));
    }

    @GetMapping("get-all-pagination")
    public ResponseEntity<?> getAllTask(@PathParam("term") String term, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(taskService.getAllTask(term, pageable, totalElement));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateTask(@RequestBody Task task) {
        return ResponseEntity.ok().body(taskService.updateTask(task));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneTask(@PathParam("taskId") String taskId) {
        return ResponseEntity.ok().body(taskService.getOneTask(taskId));
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteTask(@PathParam("taskId") String taskId) {
        return ResponseEntity.ok().body(taskService.logicDeleteById(taskId, Task.class));
    }

    @GetMapping("check-task-code")
    public ResponseEntity<?> checkTaskCode(@PathParam("taskCode") String taskCode) {
        return ResponseEntity.ok().body(taskService.checkTaskCode(taskCode));
    }

    @GetMapping("get-task-list-by-reference-id")
    public ResponseEntity<?> getTaskListByReferenceId(@PathParam("referenceId") String referenceId) {
        return ResponseEntity.ok().body(taskService.getTaskListByReferenceId(referenceId));
    }

    @GetMapping("get-tasks-of-task-group")
    public ResponseEntity<?> getTaskListByTaskGroupId(@PathParam("taskGroupId") String taskGroupId) {
        return ResponseEntity.ok().body(taskService.getTaskListByTaskGroupId(taskGroupId));
    }

    @GetMapping("get-task-with-associated-user")
    public ResponseEntity<?> getTaskByTaskId(@PathParam("taskId") String taskId) {
        return ResponseEntity.ok().body(taskService.getTaskByTaskId(taskId));
    }

    @DeleteMapping("delete-schedule-maintenance")
    public ResponseEntity<?> deleteScheduleTask(@PathParam("taskId") String taskId, @PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        scheduleMaintenanceBackupService.deleteRelatedTaskOfScheduleBackup(taskId,scheduleMaintenanceId);
        return ResponseEntity.ok().body(taskService.logicDeleteById(taskId, Task.class));
    }

}
