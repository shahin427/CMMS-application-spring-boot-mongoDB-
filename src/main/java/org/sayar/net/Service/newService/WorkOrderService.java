package org.sayar.net.Service.newService;


import org.sayar.net.Controller.newController.MtbfDTO;
import org.sayar.net.Controller.newController.dto.*;
import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.CompletionDetail;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.MtbfReturn;
import org.sayar.net.Model.WorkOrderSchedule;
import org.sayar.net.Model.WorkRequest;
import org.sayar.net.Model.newModel.BasicInformation;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.MiscCost;
import org.sayar.net.Model.newModel.Notify;
import org.sayar.net.Model.newModel.Task.model.Task;
import org.sayar.net.Model.newModel.WorkOrder.PartWithUsageCount;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenanceBackup;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface WorkOrderService extends GeneralService<WorkOrder> {

    WorkOrder postCreateWorkOrder(WorkOrderCreateDTO workOrderCreateDTO);

    boolean updateCreateWorkOrder(WorkOrderCreateDTO workOrderCreateDTO, String workOrderId);

    WorkOrderCreateDTO getOneCreateWorkOrder(String workOrderId);

    boolean postBasicInformation(WorkOrderDTOBasicInformation workOrderDTOBasicInformation);

    BasicInformation getOneBasicInformation(String workOrderId);

    Page<WorkOrderFilterDTO> getAllByFilterAndPagination(WorkOrderDTO workOrderDTO, Pageable pageable, Integer totalElement);

    Map<String, Object> getOne(String id);

    Page<ResWorkOrderPmGetPageDTO> getPageWorkOrderCreatedBySchedule(ReqWorkOrderPmGetPageDTO entity, Pageable pageable, Integer totalElement);

    List<ResWorkOrderPmGetPageDTO> getAllWorkOrderCreatedBySchedule(ReqWorkOrderPmGetPageDTO entity);

    Map<String, Object> getListWorkOrderForCalendar(ReqWorkOrderForCalendarGetListDTO entity);

    List<MiscCost> getMiscCostListByWorkOrderId(String workOrderId);

    List<Notify> getNotifyListByWorkOrderId(String workOrderId);

    List<Task> getTaskListByWorkOrderId(String workOrderId);

    WorkOrder getTaskGroupListByWorkOrderId(String workOrderId);

    List<PartWithUsageCount> getPartListByWorkOrderId(String workOrderId);

    boolean checkWorkOrderCode(String workOrderCode);

    CompletionDetailDTO getCompletionDetailByWorkOrderId(String workOrderId);

    boolean updateMiscCostListByWorkOrderId(WorkOrderMiscCostDTO workOrderMiscCostDTO);

    BasicInformationDTO getBasicInformationByWorkOrderId(String workOrderId);

    boolean updateBasicInformationByWorkOrderId(String workOrderId, BasicInformation basicInformation);

    boolean updateCompletionByWorkOrderId(String workOrderId, CompletionDetail completionDetail);

    boolean updateTaskGroupListByWorkOrderId(List<String> taskGroup, String workOrderId);

    List<WorkOrder> getWorkOrderByProjectId(String projectId);

    List<WorkOrderAndAssetDTO> getAllWorkOrdersByProjectId(String projectId);

    List<WorkOrder> generateWorkOrderByArrivedScheduleMaintenance(List<ScheduleMaintenance> scheduleMaintenanceList);

    long countAllWorkOrderByUserId(String userAssignedId);

    long getAllOpenWorkOrdersOfUsersByUserId(String userAssignedId);

    long countHighPriorityWorkOrders(String userAssignedId);

    CountAllClosedWorkOrdersByUserId countAllClosedWorkOrdersByUserId(String userAssignedId);

    ClosedNotCompletedWorkOrdersAndTotalWorkOrdersDTO countClosedNotCompletedWorkOrdersByUserId(String userAssignedId);

    OnTimeCompletedWorkOrderDTO onTimeCompletedWorkOrders();

    OverDueAndCompletedWorkOrdersDTO overDueAndCompletedWorkOrders();

    OverDueAndNotCompletedWorkOrdersDTO overDueAndNotCompletedWorkOrders();

    long onTimeCompletionRate();

    long getAllOpenWorkOrdersByUserIdBySpecifiedCurrentMonth(int month, String userAssignedId);

    long getAllWorkOrdersBySpecifiedMonth(int month, String userAssignedId);

    long getHighPriorityWorkOrdersBySpecifiedMonth(int month, String userAssignedId);

    long getClosedWorkOrdersByUserIdBySpecifiedMonth(int month, String userAssignedId);

    ClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonthAndTotalWorkOrderListDTO getClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth(int month, String userAssignedId);

    List<WorkOrder> getOnTimeCompletedWorkOrdersBySpecifiedMonth(int month, String userAssignedId);

    List<WorkOrder> getOverDueAndNotCompletedWorkOrdersBySpecifiedTime(int month, String userId);

    TotalAndOverDueCompletedWorkOrders getOverDueAndCompletedWorkOrders(int month, String userId);

    long getOnTimeCompletionRateBySpecifiedTime(int month);

    List<WorkOrder> generateWorkOrdersForTodayByTodayScheduleMaintenanceLists(List<ScheduleMaintenanceBackup> todayScheduleMaintenanceBackupsForGeneratingWorkOrder);

    CountOnTimeCompletedWorkOrdersBySpecifiedMonthDTO countOnTimeCompletedWorkOrdersBySpecifiedMonth(int month, String userAssignedId);

    CountOverDueAndNotCompletedWorkOrdersBySpecifiedTimeDTO countOverDueAndNotCompletedWorkOrdersBySpecifiedTime(int month, String userId);

    CountOverDueAndCompletedWorkOrdersDTO countOverDueAndCompletedWorkOrders(int month, String userAssignedId);

    Page<AssignedWorkOrderFilterDTO> getAllByFilterAndPaginationByUserId(String userId, WorkOrderDTO workOrderDTO, Pageable pageable, Integer totalElement);

    long numberOfPendingWorkOrderOfUserInSpecificTime(int month, String userAssignedId);

    WorkOrder getWorkOrderByNotifyReferenceId(String referenceId);

    void generatingNewWorkOrderByTriggeredScheduleMaintenanceInDistanceRecord(ScheduleMaintenanceBackup newScheduleMaintenanceBackup);

    boolean checkIfUserExistWorkOrder(String userId);

    void createWorkOrderForWorkRequest(String id, String assetId);

    WorkOrder createWorkOrderAccordingToAssociatedWorkRequest(WorkRequest workRequest);

    List<WorkOrder> generateWorkOrdersForDistanceMeasurementForTodayByTodayScheduleMaintenanceLists(List<ScheduleMaintenanceBackup> ArrivedTimeScheduleMaintenanceBackupList, long amount);

    WorkOrder getRelevantWorkOrderOfFaultyAsset(String workOrderId);

    void setTheUserIdAsAssignedUserForWorkOrder(String userId, String workOrderId);

    boolean ifProjectExistsInWorkOrder(String projectId);

    boolean ifBudgetExistsInWorkOrder(String budgetId);

    boolean ifWorkStatusExistsInWorkOrder(String workOrderStatusId);

    boolean ifChargeDepartmentExistInWorkOrder(String chargeDepartmentId);

    boolean ifAssetExistsInWorkOrder(String assetId);

    boolean ifPartExistsInWorkOrder(String partId);

    boolean ifTaskGroupExistsInWorkOrder(String taskGroupId);

    WorkOrder getRelevantWorkOrder(String workOrderId);

    List<WorkOrder> getWorkOrderListOfScheduledActivitySample(List<String> workOrderIdList);

    List<WorkOrder> getAllWorkOrdersInSpecifiedMonth(int month, String userAssignedId);

    List<WorkOrder> getAllRealOpenWorkOrdersBySpecifiedMonth(String userAssignedId, int month);

    List<WorkOrder> getHighestPriorityWorkOrdersBySpecifiedMonth(String userAssignedId, int month);

    OnTimeCompletedWorkOrdersAgainstAllCompletedWorkOrdersDTO getOnTimeCompletedWorkOrdersAgainstAllCompletedWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getAllRealWorkOrdersByUserAssignedId(String userAssignedId);

    List<WorkOrder> getRealHighPriorityWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealAverageWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealLowPriorityWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealVeryLowPriorityWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealMaintenanceTypeWorkOrders(String userAssignedId, int month, MaintenanceType maintenanceType);

    long countRealHighestPriorityWorkOrders(String userAssignedId, int month);

    long countRealHighPriorityWorkOrders(String userAssignedId, int month);

    long countRealAveragePriorityWorkOrders(String userAssignedId, int month);

    long countRealLowPriorityWorkOrders(String userAssignedId, int month);

    long countRealVeryLowPriorityWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealLateAndOpenWorkOrders(String userId);

    List<WorkOrderWithColorDTO> getRealPendingWorkOrdersWithColorStatus(String userAssignedId);

    List<WorkOrderWithColorDTO> getRealCurrentWeekOpenWorkOrdersWithColorStatus(String userAssignedId);

    long countRealOpenWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealClosedWorkOrders(String userAssignedId, int month);

    long countRealClosedWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealPendingWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealDraftWorkOrders(String userAssignedId, int month);

    long countRealDraftWorkOrders(String userAssignedId, int month);

    List<DashboardWorkOrderDTO> managerDashboardGetOverDueWorkOrders(int month);

    long countRealOverDueWorkOrders(String userAssignedId, int month);

    PlannedMaintenanceDTO realPlannedMaintenanceRatio(int month);

    long realOnTimeCompletionRate(int month);

    List<WorkOrder> getHighestPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month);

    long countRealHighestPriorityNotPlannedWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealHighPriorityNotPlannedWorkOrders(String userAssignedId, int month);

    long countRealHighPriorityNotPlannedWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealAveragePriorityNotPlannedWorkOrders(String userAssignedId, int month);

    long countRealAveragePriorityNotPlannedWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealLowPriorityNotPlannedWorkOrders(String userAssignedId, int month);

    long countRealLowPriorityNotPlannedWorkOrders(String userAssignedId, int month);

    List<WorkOrder> getRealVeryLowPriorityNotPlannedWorkOrders(String userAssignedId, int month);

    long countRealVeryLowPriorityNotPlannedWorkOrders(String userAssignedId, int month);

    List<DashboardWorkOrderDTO> managerDashboardGetHighestPriorityWorkOrders(int month);

    long managerDashboardCountHighestPriorityWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetHighPriorityWorkOrders(int month);

    long managerDashboardCountHighPriorityWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetAveragePriorityWorkOrders(int month);

    long managerDashboardCountAveragePriorityWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetLowPriorityWorkOrders(int month);

    long managerDashboardCountLowPriorityWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetLowestPriorityWorkOrders(int month);

    long managerDashboardCountLowestPriorityWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetOpenWorkOrders(int month);

    long managerDashboardCountOpenWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetClosedWorkOrder(int month);

    long managerDashboardCountClosedWorkOrder(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetPendingWorkOrders(int month);

    long managerDashboardCountPendingWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetDraftWorkOrders(int month);

    long managerDashboardCountDraftWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardHighestPriorityPlannedWorkOrders(int month);

    long managerDashboardCountHighestPriorityPlannedWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetHighPriorityPlannedWorkOrders(int month);

    long managerDashboardCountHighPriorityPlannedWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetAveragePriorityPlannedWorkOrders(int month);

    long managerDashboardCountAveragePriorityPlannedWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetLowPriorityPlannedWorkOrders(int month);

    long managerDashboardCountLowPriorityPlannedWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetLowestPriorityPlannedWorkOrders(int month);

    long managerDashboardCountLowestPriorityPlannedWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetOpenPlannedWorkOrders(int month);

    long managerDashboardCountOpenPlannedWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetClosedPlannedWorkOrders(int month);

    long managerDashboardCountClosedPlannedWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetPendingPlannedWorkOrders(int month);

    long managerDashboardCountPendingPlannedWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetDraftPlannedWorkOrders(int month);

    long managerDashboardCountDraftPlannedWorkOrders(int month);

    long managerDashboardCountOverDueWorkOrders(int month);

    List<DashboardWorkOrderDTO> managerDashboardGetOverDuePlannedWorkOrders(int month);

    long managerDashboardCountOverDuePlannedWorkOrders(int month);

    PlannedWorkOrdersRatioDTO managerDashboardPlannedWorkOrdersRatio(int month);

    List<UnScheduledWorkOrderDTO> managerDashboardUnscheduledWorkOrdersForBar();

    List<PendingBarDTO> managerDashboardPendingWorkOrdersForBar();

    List<LateBarDTO> managerDashboardLateWorkOrdersForBar();

    List<CurrentWeekWorkOrderDTO> managerDashboardCurrentWeekWorkOrdersBar();

    List<CurrentWeekPlannedWorkOrderDTO> managerDashboardCurrentPlannedWorkOrdersBar();

    List<WorkOrder> getWorkOrderPriorityAndMaintenanceType(List<String> workRequestIdList);

    void workOrderAcceptedByManager(String workOrderId, boolean workOrderAccepted);

    List<Task> getTasksByWorkOrderId(String workOrderId);

    List<TaskGroupDTO> getTaskGroupOfWorkOrder(String workOrderId);

    Task saveTaskOfWorkOrder(Task task);

    void deleteTaskFromWorkOrder(String workOrderId, String taskId);

    void addUsedPartToWorkOrder(String workOrderId, String partId);

    void deleteUsedPartFromWorkOrder(String workOrderId, String partWithUsageCountId);

    void deleteDocumentFromWorkOrder(String documentFileId, String workOrderId);

    void addDocumentToWorkOrder(String workOrderId, String documentId);

    WorkOrder getDocumentIdListOfWorkOrder(String workOrderId);

    WorkOrder getWorkOrderForRepository(String workOrderId);

    WorkOrder getPartWithUsageCountOfWorkOrder(String workOrderId);

    boolean checkIfWorkOrderIsInProcess(String workOrderId);

    boolean newSaveWorkOrder(NewSaveDTO newSaveDTO);

    boolean newUpdate(NewSaveDTO newSaveDTO);

    boolean scheduleWorkOrderUpdate(ReqWorkOrderScheduleUpdateDTO entity);

    NewGetOneٔWorkOrderDTO newGetOne(String workOrderId);

    Page<UsedPartReport> usedPartOfWOrkOrder(UsedPartOfWorkOrder usedPartOfWorkOrder, Pageable pageable, Integer totalElement);

    CountUsedPartDTO countUsedPartOfWorkOrder(UsedPartOfWorkOrder usedPartOfWorkOrder);

    Page<PersonnelFunctionGetAllDTO> personnelFunction(PersonnelFunctionDTO personnelFunctionDTO, Pageable pageable, Integer totalElement);

    TotalWorkedTimeDTO TotalWorkedTimeOfPersonnel(PersonnelFunctionDTO personnelFunctionDTO);

    List<MtbfReturn> mtbfCalculation(MtbfDTO mtbfDTO);

    List<MttrReturn> mttrCalculation(MttrDTO mttrDTO);

    List<MtbfTableReturn> mtbfTable(MtbfTableDTO mtbfTableDTO);

    List<MttrTableReturn> mttrTable(MttrTableDTO mttrTableDTO);

    boolean isAcceptedByManager(String workOrderId);

    WorkOrder getWorkOrderTechnicians(String workRequestId);

    List<WorkOrderDTO> getAllWorkOrderForExcel(WorkOrderDTO workOrderDTO);

    List<MdtReturn> mdtCalculation(MdtDTO mdtDTO);

    List<MdtTableReturn> mdtTable(MdtTableDTO mdtTableDTO);

    WorkOrder createWorkOrderAccordingToAssociatedWorkRequest(WorkOrderSchedule entity);

    Map<String, Object> getOneScheduleWorkOrder(String workOrderId);

    boolean updateScheduleWorkOrder(WorkOrderScheduleDTO workOrderScheduleDTO);

    void deleteWorkOrderSchedule(String id);

    void setEndDateOfWorkOrderSchedule(String workOrderId);

}
