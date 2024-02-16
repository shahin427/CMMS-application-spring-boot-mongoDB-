package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.controller;

import org.sayar.net.Enumes.ResponseKeyWord;
import org.sayar.net.Model.DTO.ScheduleMaintenanceCreateDTO;
import org.sayar.net.Model.DTO.ScheduleMaintenanceFilterDTO;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.*;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintananceService;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintenanceBackupService;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("schedule-maintenance")
public class ScheduleMaintananceController {

    @Autowired
    private ScheduleMaintananceService service;
    @Autowired
    private ScheduleMaintenanceBackupService scheduleMaintenanceBackupService;


    @PostMapping("save")
    public ResponseEntity<?> postScheduleMaintenance(@RequestBody ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO) {
        return ResponseEntity.ok().body(service.postScheduleMaintenance(scheduleMaintenanceCreateDTO));
    }

    @GetMapping("get-basic-information-by-schedule-maintenance-id")
    public ResponseEntity<?> getAllBasicInformation(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        return ResponseEntity.ok().body(service.getAllBasicInformation(scheduleMaintenanceId));
    }

    @GetMapping("get-schedule-maintenance-list-by-project-id")
    public ResponseEntity<?> getAllScheduleMaintenanceByProjectId(@PathParam("projectId") String projectId) {
        return ResponseEntity.ok().body(service.getAllScheduleMaintenanceByProjectId(projectId));
    }

    @GetMapping("get-completion-detail-by-schedule-maintenance-id")
    public ResponseEntity<?> getCompletionDetailByScheduleMaintenanceId(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        return ResponseEntity.ok().body(service.getCompletionDetailByScheduleMaintenanceId(scheduleMaintenanceId));
    }

    @PutMapping("update-basic-information-by-schedule-maintenance-id")
    public ResponseEntity<?> updateBasicInformationByScheduleMaintenanceId(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId,
                                                                           @RequestBody ScheduleMaintenanceBasicInformation basicInformation) {
        return ResponseEntity.ok().body(service.updateBasicInformationByScheduleMaintenanceId(scheduleMaintenanceId, basicInformation));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateScheduleMaintenanceCreateByScheduleId(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId,
                                                                         @RequestBody ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO) {
        return ResponseEntity.ok().body(service.updateScheduleMaintenanceCreateByScheduleId(scheduleMaintenanceId, scheduleMaintenanceCreateDTO));
    }

    @PutMapping("update-completion-detail-by-schedule-maintenance-id")
    public ResponseEntity<?> updateCompletionDetailByAssetId(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId,
                                                             @RequestBody ScheduleMaintenanceCompletionDetail scheduleMaintenanceCompletionDetail) {
        return ResponseEntity.ok().body(service.updateCompletionDetailByAssetId(scheduleMaintenanceId, scheduleMaintenanceCompletionDetail));
    }

    @PostMapping()
    public ResponseEntity<?> getAllByFilterAndPagination(@RequestBody ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO,
                                                         Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllByFilterAndPagination(scheduleMaintenanceFilterDTO, pageable, totalElement));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneScheduleMaintenance(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        return ResponseEntity.ok().body(service.getOneScheduleMaintenance(scheduleMaintenanceId));
    }

    @DeleteMapping("delete-create-schedule-maintenance")
    public ResponseEntity<?> deleteScheduleMaintenanceById(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        scheduleMaintenanceBackupService.deleteScheduleMaintenanceBackupByScheduleMaintenanceId(scheduleMaintenanceId);
        return ResponseEntity.ok().body(service.deleteAndInActiveScheduleMaintenanceById(scheduleMaintenanceId));
    }

    @GetMapping("get-schedule-with-time-and-metering-by-schedule-maintenance-id")
    public ResponseEntity<?> getScheduleWithTimeAndMetering(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        return ResponseEntity.ok().body(service.getScheduleWithTimeAndMetering(scheduleMaintenanceId));
    }

    @PutMapping("update-schedule-with-time-and-metering-by-schedule-maintenance-id")
    public ResponseEntity<?> updateScheduleWithTimeAndMeteringByScheduleId(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId, @RequestBody ScheduleWithTimeAndMetering scheduleWithTimeAndMetering) {
        return ResponseEntity.ok().body(service.updateScheduleWithTimeAndMeteringByScheduleId(scheduleMaintenanceId, scheduleWithTimeAndMetering));
    }

    @GetMapping("get-task-group-list-by-schedule-maintenance-id")
    public ResponseEntity<?> getTaskGroupListByScheduleMaintenanceId(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        return ResponseEntity.ok().body(service.getTaskGroupListByScheduleMaintenanceId(scheduleMaintenanceId));
    }

    @PutMapping("update-task-group-list-by-schedule-maintenance-id")
    public ResponseEntity<?> updateTaskGroupListByScheduleMaintenanceId(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId, @RequestBody List<String> taskGroupIdList) {
        return ResponseEntity.ok().body(service.updateTaskGroupListByScheduleMaintenanceId(scheduleMaintenanceId, taskGroupIdList));
    }

    @PostMapping("get-all-by-filter-and-pagination")
    public ResponseEntity<?> getAllByPagination(@RequestBody ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO,
                                                Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllByPagination(scheduleMaintenanceFilterDTO, pageable, totalElement));
    }

    @GetMapping("get-all-future-dates-of-schedule-maintenance")
    public ResponseEntity<?> getAllFutureDatesOfScheduleMaintenance(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        return ResponseEntity.ok().body(service.getAllFutureDatesOfScheduleMaintenance(scheduleMaintenanceId));
    }

    @PutMapping("delete-schedule-maintenance-time-scheduling")
    public ResponseEntity<?> deleteScheduleMaintenanceTimeScheduling(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        return ResponseEntity.ok().body(service.deleteScheduleMaintenanceTimeScheduling(scheduleMaintenanceId));
    }

    @PutMapping("delete-schedule-maintenance-metering-scheduling")
    public ResponseEntity<?> deleteScheduleMaintenanceDistanceScheduling(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        return ResponseEntity.ok(service.deleteScheduleMaintenanceDistanceScheduling(scheduleMaintenanceId));
    }

    @GetMapping("get-all-future-metering-of-schedule-maintenance")
    public ResponseEntity<?> getAllFutureDistanceOfScheduleMaintenance(@PathParam("per") long per,
                                                                       @PathParam("startDistance") long startDistance,
                                                                       @PathParam("endDistance") long endDistance) {
        return ResponseEntity.ok().body(service.getAllFutureDistanceOfScheduleMaintenance(per, startDistance, endDistance));
    }

    @GetMapping("check-if-code-is-unique")
    public ResponseEntity<?> checkIfCodeIsUnique(@PathParam("code") String code) {
        return ResponseEntity.ok().body(service.checkIfCodeIsUnique(code));
    }

    @PutMapping("update-schedule-time")
    public ResponseEntity<?> updateScheduleTime(@RequestBody ScheduledTime scheduledTime, @PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        return ResponseEntity.ok().body(service.updateScheduleTime(scheduledTime, scheduleMaintenanceId));
    }

    @PutMapping("update-schedule-distance")
    public ResponseEntity<?> updateScheduleDistance(@RequestBody ScheduledMeteringCycle scheduledMeteringCycle, @PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        return ResponseEntity.ok().body(service.updateScheduleDistance(scheduledMeteringCycle, scheduleMaintenanceId));
    }

    @GetMapping("get-schedule-time")
    public ResponseEntity<?> getScheduleTime(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        if (scheduleMaintenanceId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("ورودی فرستاده شده صحیح نمیباشد ", httpStatus);
        } else {
            return ResponseEntity.ok().body(service.getScheduleTime(scheduleMaintenanceId));
        }
    }

    @GetMapping("get-schedule-distance")
    public ResponseEntity<?> getScheduleDistance(@PathParam("scheduleMaintenanceId") String scheduleMaintenanceId) {
        return ResponseEntity.ok().body(service.getScheduleDistance(scheduleMaintenanceId));
    }

    @GetMapping("get-activity-of-schedule-maintenance-by-relevant-asset")
    public ResponseEntity<?> getActivityOfScheduleMaintenanceByRelevantAsset(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(service.getActivityOfScheduleMaintenanceByRelevantAsset(assetId));
    }

    @GetMapping("get-all-unit-of-measurement-of-the-asset")
    public ResponseEntity<?> getAllUnitOfMeasurementOfTheAsset(@PathParam("assetId") String assetId) {
        if (assetId == null) {
            return new ResponseContent().sendErrorResponseEntity("ورودی فرستاده شده از سمت کلاینت درست نمیباشد", ResponseKeyWord.INVALID_INPUT, 406);
        } else {
            return ResponseEntity.ok().body(service.getAllUnitOfMeasurementOfTheAsset(assetId));
        }
    }
}
