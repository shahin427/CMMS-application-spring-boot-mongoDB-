package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.controller;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintenanceBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("schedule-maintenance-backup-controller")
public class ScheduleMaintenanceBackupController {
    @Autowired
    private ScheduleMaintenanceBackupService scheduleMaintenanceBackupService;

    @GetMapping("get-all")
    public ResponseEntity<?> getAllScheduleMaintenanceBackup() {
        return ResponseEntity.ok().body(scheduleMaintenanceBackupService.getAllScheduleMaintenanceBackup());
    }

    @GetMapping("check-if-schedule-maintenance-start-time-arrived")
    public ResponseEntity<?> checkIfScheduleMaintenanceBackupStartTimeArrived() {
        return ResponseEntity.ok().body(scheduleMaintenanceBackupService.checkIfScheduleMaintenanceBackupStartTimeArrived());
    }

    @GetMapping("test")
//    @Scheduled(cron = "0 0 12 * * ?")
    public void checkTheScheduleMaintenanceTimeAndProduceNewWorkOrder() {
        scheduleMaintenanceBackupService.checkTheScheduleMaintenanceTimeAndProduceNewWorkOrder();
    }
}
