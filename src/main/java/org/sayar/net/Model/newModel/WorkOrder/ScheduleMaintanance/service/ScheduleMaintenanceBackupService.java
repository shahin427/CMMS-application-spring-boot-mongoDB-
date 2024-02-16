package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.ScheduleMaintenanceCreateDTO;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.*;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

import java.util.List;

public interface ScheduleMaintenanceBackupService extends GeneralService<ScheduleMaintenanceBackup> {

    List<ScheduleMaintenanceBackup> getAllScheduleMaintenanceBackup();

    List<WorkOrder> checkIfScheduleMaintenanceBackupStartTimeArrived();

    ScheduleMaintenanceBackup createScheduleMaintenanceBackUpByScheduleMaintenance(ScheduleMaintenance scheduleMaintenance);

    boolean updateScheduleMaintenanceBackUp(String scheduleMaintenanceId, ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO);

    void getScheduleMaintenanceBackUpTimeAndMeteringByScheduleMaintenance(String scheduleMaintenanceId, ScheduleWithTimeAndMetering scheduleWithTimeAndMetering);

    void checkTheScheduleMaintenanceTimeAndProduceNewWorkOrder();

    void deleteScheduleMaintenanceBackupByScheduleMaintenanceId(String scheduleMaintenanceId);

    boolean updateScheduleMaintenanceBackUpByBasicInformation(String scheduleMaintenanceId, ScheduleMaintenanceBasicInformation basicInformation);

    boolean updateScheduleMaintenanceBackUpByCompletionDetail(String scheduleMaintenanceId, ScheduleMaintenanceCompletionDetail scheduleMaintenanceCompletionDetail);

    boolean updateScheduleMaintenanceBackUpTaskGroupIdList(String scheduleMaintenanceId, List<String> taskGroupIdList);

    boolean updateScheduleMaintenanceBackUpTaskPartByTask(String taskId, String referenceId);

    void checkIfAmountArrivedToNextTriggerThresholdOfScheduleMaintenance(String referenceId, long amount, UnitOfMeasurement unitOfMeasurement);

    void deleteScheduleMaintenanceBackUpTimeScheduling(String scheduleMaintenanceId);

    void deleteScheduleMaintenanceBackupDistanceScheduling(String scheduleMaintenanceId);

    void updateScheduleMaintenanceBackupScheduleTime(ScheduleWithTimeAndMetering scheduleWithTimeAndMetering, String scheduleMaintenanceId);

    void updateScheduleMaintenanceBackupMetering(ScheduleWithTimeAndMetering scheduleWithTimeAndMetering, String scheduleMaintenanceId);

    void updateScheduleTimeOfScheduleMaintenanceBackup(ScheduledTime scheduledTime, String scheduleMaintenanceId);

    void updateScheduleDistance(ScheduledMeteringCycle scheduledMeteringCycle, String scheduleMaintenanceId);

    void deleteRelatedTaskOfScheduleBackup(String taskId, String scheduleMaintenanceId);

    void addUserPartToScheduleBackup(String id,String scheduleMaintenanceId);

    void deleteUsedPartOfScheduleBackup(String partWithUsageCountId, String scheduleMaintenanceId);

    void addDocumentIdToBackupSchedule(String referenceId, String id);

    void deleteDocumentFromBackupSchedule(String documentId, String scheduleMaintenanceId);
}
