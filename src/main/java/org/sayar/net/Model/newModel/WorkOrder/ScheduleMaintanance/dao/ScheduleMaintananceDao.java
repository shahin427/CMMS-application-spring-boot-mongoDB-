package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.dao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.ScheduleMaintenanceCreateDTO;
import org.sayar.net.Model.DTO.ScheduleMaintenanceFilterDTO;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScheduleMaintananceDao extends GeneralDao<ScheduleMaintenance> {

    ScheduleMaintenance postScheduleMaintenance(ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO);

    ScheduleMaintenance getAllBasicInformation(String scheduleMaintenanceId);

    List<ScheduleMaintenance> getAllScheduleMaintenanceByProjectId(String projectId);

    ScheduleMaintenance ScheduleMaintenanceCompletionDetail(String scheduleMaintenanceId);

    boolean updateBasicInformationByScheduleMaintenanceId(String scheduleMaintenanceId, ScheduleMaintenanceBasicInformation basicInformation);

    UpdateResult updateScheduleMaintenanceCreateByScheduleId(String scheduleMaintenanceId, ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO);

    UpdateResult updateCompletionDetailByAssetId(String scheduleMaintenanceId, ScheduleMaintenanceCompletionDetail scheduleMaintenanceCompletionDetail);

    List<ScheduleMaintenanceCreateDTO> getAllByFilterAndPagination(ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO, Pageable pageable, Integer totalElement);

    long countAllByFilterAndPagination(ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO);

    ScheduleMaintenance getOneScheduleMaintenance(String scheduleMaintenanceId);

    ScheduleMaintenance getScheduleWithTimeAndMetering(String scheduleMaintenanceId);

    ScheduleMaintenance updateScheduleWithTimeAndMeteringByScheduleId(String scheduleMaintenanceId, ScheduleWithTimeAndMetering scheduleWithTimeAndMetering);

    ScheduleMaintenance getTaskGroupListByScheduleMaintenanceId(String scheduleMaintenanceId);

    boolean updateTaskGroupListByScheduleMaintenanceId(String scheduleMaintenanceId,List<String> taskGroupIdList);

    List<ScheduleMaintenanceCreateDTO> getAllByPagination(ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO, Pageable pageable, Integer totalElement);

    long countAllFilteredScheduleMaintenance(ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO);

    List<ScheduleMaintenance> getAllScheduleMaintenance();

    List<ScheduleMaintenance> getAllScheduleMaintenanceByScheduleMaintenanceBackup(List<String> scheduledMaintenanceBackupInventoryIdList);

    List<ScheduleMaintenance> getScheduleMaintenanceListById(List<String> scheduleMaintenanceId);

    ScheduleMaintenance getScheduleMaintenanceById(String scheduleMaintenanceId);

    ScheduleMaintenanceBackup getScheduleMaintenanceByAssetId(String id);

    UpdateResult deleteScheduleMaintenanceTimeScheduling(String scheduleMaintenanceId);

    UpdateResult deleteScheduleMaintenanceDistanceScheduling(String scheduleMaintenanceId);

    List<Long> getAllFutureDistanceOfScheduleMaintenance(long per, long startDistance, long endDistance);

    ScheduleMaintenance getOldVersionOfScheduleMaintenance(String scheduleMaintenanceId);

    ScheduleMaintenance getScheduleMaintenanceOfNotify(String referenceId);

    boolean     checkIfCodeIsUnique(String code);

    boolean checkIfUserExistsScheduleMaintenance(String userId);

    ScheduleMaintenance updateScheduleTime(ScheduledTime scheduledTime, String scheduleMaintenanceId);

    ScheduleMaintenance updateScheduleDistance(ScheduledMeteringCycle scheduledMeteringCycle, String scheduleMaintenanceId);

    ScheduleMaintenance getScheduleMaintenanceScheduleTime(String scheduleMaintenanceId);

    ScheduleMaintenance getScheduleMaintenanceScheduleDistance(String scheduleMaintenanceId);

    Asset getActivityOfScheduleMaintenanceByRelevantAsset(String assetId);

    void inactiveRelevantScheduleMaintenance(List<String> scheduleMaintenanceIdList);

    void inActiveExpiredScheduleMaintenance(List<String> expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups);

    boolean ifProjectExistsInScheduleMaintenance(String projectId);

    boolean ifWorkStatusExistsInScheduleMaintenance(String workOrderStatusId);

    boolean ifAssetExistsInScheduleMaintenance(String assetId);

    boolean ifTaskGroupExistsInScheduleMaintenance(String taskGroupId);

    UpdateResult deleteAndInActiveScheduleMaintenanceById(String scheduleMaintenanceId);

    List<ScheduleMaintenance> getProjectScheduleMaintenance(String projectId);
}
