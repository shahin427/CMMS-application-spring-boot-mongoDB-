package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.dao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.ScheduleMaintenanceCreateDTO;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.*;

import java.util.List;

public interface ScheduleMaintenanceBackupDao extends GeneralDao<ScheduleMaintenanceBackup> {


    List<ScheduleMaintenanceBackup> getAllScheduleMaintenanceBackup(List<ScheduleMaintenance> scheduleMaintenanceList);

    List<ScheduleMaintenanceBackup> checkIfScheduleMaintenanceBackupStartTimeArrived();

    List<ScheduleMaintenanceBackup> generateNewScheduleMaintenanceBackup(List<ScheduleMaintenanceBackup> scheduleMaintenanceBackup);

    ScheduleMaintenanceBackup createScheduleMaintenanceBackUpByScheduleMaintenance(ScheduleMaintenance scheduleMaintenance);

    UpdateResult updateScheduleMaintenanceBackUp(String scheduleMaintenanceId, ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO);

    void updateScheduleMaintenanceBackUpTimeAndMeteringByScheduleMaintenance(String scheduleMaintenanceId, ScheduleWithTimeAndMetering scheduleWithTimeAndMetering, long numberOfDayForEndingEachScheduleMaintenance);

    List<String> checkScheduleMaintenanceBackupForGeneratingNewOne(List<ScheduleMaintenanceBackup> scheduleMaintenanceBackupList);

    List<ScheduleMaintenanceBackup> getAllScheduleMaintenanceBackUp();

    UpdateResult updateScheduleMaintenanceBackUpByBasicInformation(String scheduleMaintenanceId, ScheduleMaintenanceBasicInformation basicInformation);

    UpdateResult updateScheduleMaintenanceBackUpByCompletionDetail(String scheduleMaintenanceId, ScheduleMaintenanceCompletionDetail scheduleMaintenanceCompletionDetail);

    UpdateResult updateScheduleMaintenanceBackUpTaskGroupIdList(String scheduleMaintenanceId, List<String> taskGroupIdList);

    UpdateResult updateScheduleMaintenanceBackUpTaskPartByTask(String taskId, String referenceId);

    void checkIfAmountArrivedToNextTriggerThresholdOfScheduleMaintenance(String referenceId, long amount, UnitOfMeasurement unitOfMeasurement);

    void findAndDeleteScheduleMaintenanceBackup(String scheduleMaintenanceId);

    void deleteScheduleMaintenanceBackUpTimeScheduling(String scheduleMaintenanceId);

    void deleteScheduleMaintenanceBackupDistanceScheduling(String scheduleMaintenanceId);

    void updateScheduleMaintenanceBackupScheduleTime(ScheduleWithTimeAndMetering scheduleWithTimeAndMetering, String scheduleMaintenanceId);

    void updateScheduleMaintenanceBackupMetering(ScheduleWithTimeAndMetering scheduleWithTimeAndMetering, String scheduleMaintenanceId);

    void updateScheduleTimeOfScheduleMaintenanceBackup(ScheduledTime scheduledTime, String scheduleMaintenanceId);

    void updateScheduleDistance(ScheduledMeteringCycle scheduledMeteringCycle, String scheduleMaintenanceId);

    List<ScheduleMaintenanceBackup> deleteExpiredScheduleMaintenance();

    void deleteRelatedTaskOfScheduleBackup(String taskId, String scheduleMaintenanceId);

    void addUserPartToScheduleBackup(String id, String scheduleMaintenanceId);

    void deleteUsedPartOfScheduleBackup(String partWithUsageCountId, String scheduleMaintenanceId);

    void addDocumentIdToBackupSchedule(String referenceId, String id);

    void deleteDocumentFromBackupSchedule(String documentId, String scheduleMaintenanceId);
}
