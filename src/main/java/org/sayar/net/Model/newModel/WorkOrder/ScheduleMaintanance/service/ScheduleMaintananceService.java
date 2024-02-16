package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface ScheduleMaintananceService extends GeneralService<ScheduleMaintenance> {

    ScheduleMaintenance postScheduleMaintenance(ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO);

    ScheduleMaintenanceBasicInformation getAllBasicInformation(String scheduleMaintenanceId);

    List<ScheduleMaintenanceAndProjectFilterDTO> getAllScheduleMaintenanceByProjectId(String projectId);

    ScheduleMaintenanceCompletionDetail getCompletionDetailByScheduleMaintenanceId(String scheduleMaintenanceId);

    boolean updateBasicInformationByScheduleMaintenanceId(String scheduleMaintenanceId, ScheduleMaintenanceBasicInformation basicInformation);

    boolean updateScheduleMaintenanceCreateByScheduleId(String scheduleMaintenanceId, ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO);

    boolean updateCompletionDetailByAssetId(String scheduleMaintenanceId, ScheduleMaintenanceCompletionDetail scheduleMaintenanceCompletionDetail);

    Page<ScheduleMaintenanceCreateDTO> getAllByFilterAndPagination(ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO, Pageable pageable, Integer totalElement);

    ScheduleMaintenanceCreateDTO getOneScheduleMaintenance(String scheduleMaintenanceId);

    ScheduleWithTimeAndMetering getScheduleWithTimeAndMetering(String scheduleMaintenanceId);

    ScheduleWithTimeAndMeteringDTO updateScheduleWithTimeAndMeteringByScheduleId(String scheduleMaintenanceId, ScheduleWithTimeAndMetering scheduleWithTimeAndMetering);

    List<TaskGroupDTO> getTaskGroupListByScheduleMaintenanceId(String scheduleMaintenanceId);

    boolean updateTaskGroupListByScheduleMaintenanceId(String scheduleMaintenanceId, List<String> taskGroupIdList);

    Page<ScheduleMaintenanceCreateDTOWithObject> getAllByPagination(ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO, Pageable pageable, Integer totalElement);

    List<ScheduleMaintenance> getAllScheduleMaintenance();

    List<ScheduleMaintenance> getAllScheduleMaintenanceByScheduleMaintenanceBackup(List<String> scheduledMaintenanceBackupInventoryIdList);

    List<ScheduleMaintenance> getScheduleMaintenanceListById(List<String> scheduleMaintenanceId);

    ScheduleMaintenance getScheduleMaintenanceById(String scheduleMaintenanceId);

    ScheduleMaintenanceBackup getScheduleMaintenanceByAssetId(String id);

    List<Date> getAllFutureDatesOfScheduleMaintenance(String scheduleMaintenanceId);

    boolean deleteScheduleMaintenanceTimeScheduling(String scheduleMaintenanceId);

    boolean deleteScheduleMaintenanceDistanceScheduling(String scheduleMaintenanceId);

    List<Long> getAllFutureDistanceOfScheduleMaintenance(long per, long startDistance, long endDistance);

    ScheduleMaintenance getScheduleMaintenanceOfNotify(String referenceId);

    boolean checkIfCodeIsUnique(String code);

    boolean checkIfUserExistsScheduleMaintenance(String userId);

    ScheduleMaintenance updateScheduleTime(ScheduledTime scheduledTime, String scheduleMaintenanceId);

    ScheduleMaintenance updateScheduleDistance(ScheduledMeteringCycle scheduledMeteringCycle, String scheduleMaintenanceId);

    ScheduledTime getScheduleTime(String scheduleMaintenanceId);

    ScheduledMeteringCycle getScheduleDistance(String scheduleMaintenanceId);

    List<Activity> getActivityOfScheduleMaintenanceByRelevantAsset(String assetId);

    void inactiveRelevantScheduleMaintenance(List<String> scheduleMaintenanceIdList);

    void inActiveExpiredScheduleMaintenance(List<String> expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups);

    List<UnitOfMeasurement> getAllUnitOfMeasurementOfTheAsset(String assetId);

    boolean ifProjectExistsInScheduleMaintenance(String projectId);

    boolean ifWorkStatusExistsInScheduleMaintenance(String workOrderStatusId);

    boolean ifAssetExistsInScheduleMaintenance(String assetId);

    boolean ifTaskGroupExistsInScheduleMaintenance(String taskGroupId);

    boolean deleteAndInActiveScheduleMaintenanceById(String scheduleMaintenanceId);

    List<ScheduleMaintenance> getProjectScheduleMaintenance(String projectId);
}
