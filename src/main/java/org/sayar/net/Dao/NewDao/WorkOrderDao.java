package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Controller.newController.MtbfDTO;
import org.sayar.net.Controller.newController.dto.*;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.CompletionDetail;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.MtbfReturn;
import org.sayar.net.Model.WorkOrderSchedule;
import org.sayar.net.Model.WorkRequest;
import org.sayar.net.Model.newModel.BasicInformation;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenanceBackup;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface WorkOrderDao extends GeneralDao<WorkOrder> {

    WorkOrder postCreateWorkOrder(WorkOrderCreateDTO workOrderCreateDTO);

    UpdateResult updateCreateWorkOrder(WorkOrderCreateDTO workOrderCreateDTO, String workOrderId);

    WorkOrder getOneCreateWorkOrder(String workOrderId);

    boolean postBasicInformation(WorkOrderDTOBasicInformation workOrderDTOBasicInformation);

    WorkOrder getOneBasicInformation(String workOrderId);

    List<WorkOrderDTO> getAllByFilterAndPagination(WorkOrderDTO workOrderDTO, Pageable pageable, Integer totalElement);

    Page<ResWorkOrderPmGetPageDTO> getPageWorkOrderCreatedBySchedule(ReqWorkOrderPmGetPageDTO entity, Pageable pageable, Integer totalElement);

    List<ResWorkOrderPmGetPageDTO> getAllWorkOrderCreatedBySchedule(ReqWorkOrderPmGetPageDTO entity);

    List<ResWorkOrderForCalendarGetListDTO> getListWorkOrderForCalendar(ReqWorkOrderForCalendarGetListDTO entity);

    Map<String, Object> getOne(String id);

    long getAllCount(WorkOrderDTO workOrderDTO);

    WorkOrder findOneWorkOrder(String workOrderId);

    boolean checkWorkOrderCode(String workOrderCode);

    WorkOrder getCompletionDetailByWorkOrderId(String workOrderId);

    BasicInformationDTO getBasicInformationByWorkOrderId(String workOrderId);

    WorkOrder getOneWorkOrder(WorkOrderMiscCostDTO workOrderMiscCostDTO);

    UpdateResult updateBasicInformationByWorkOrderId(String workOrderId, BasicInformation basicInformation);

    UpdateResult updateCompletionByWorkOrderId(String workOrderId, CompletionDetail completionDetail);

    WorkOrder getTaskGroupListByWorkOrderId(String workOrderId);

    boolean getWorkOrderByWorkOrderId(List<String> taskGroup, String wokOrderId);

    List<WorkOrder> getWorkOrderByProjectId(String projectId);

    List<WorkOrder> getAllWorkOrdersByProjectId(String projectId);

    List<WorkOrder> generateWorkOrderByArrivedScheduleMaintenance(List<ScheduleMaintenance> scheduleMaintenanceList);

    long countAllWorkOrderByUserId(String userAssignedId);

    long countOpenWorkOrderByStatusId(String userAssignedId, List<String> workOrderStatusIdList);

    long countHighPriorityWorkOrders(String userAssignedId);

    long countAllClosedWorkOrdersByUserId(String userAssignedId, List<String> workOrderStatusIdList);

    long countAllClosedNotCompletedWorkOrdersByUserId(String userAssignedId, List<String> workOrderStatusIdList);

    long onTimeCompletedWorkOrders(List<WorkOrder> workOrderListExceptClosedOnes);

    long overDueAndCompletedWorkOrders(List<WorkOrder> closedWorkOrderList);

    long overDueAndNotCompletedWorkOrders(List<String> stringList);

    long getAllWorkOrders(List<String> stringList);

    long onTimeCompletionRate(long onTimeCompletedWorkOrders, long totalWorkOrdersExceptCloseOnes);

    long getAllOpenWorkOrdersByUserIdBySpecifiedCurrentMonth(int month, String userAssignedId, List<String> workOrderStatusIdList);

    List<WorkOrder> getAllWorkOrdersList();

    long getAllWorkOrdersBySpecifiedMonth(int month, String userAssignedId);

    long getHighPriorityWorkOrdersBySpecifiedMonth(long month, String userAssignedId);

    long getClosedWorkOrdersByUserIdBySpecifiedMonth(int month, String userAssignedId, List<String> workOrderStatusIdList);

    long getClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth(int month, String userAssignedId, List<String> workOrderStatusIdList);

    List<WorkOrder> getOnTimeCompletedWorkOrdersBySpecifiedMonth(int month, String userAssignedId, List<WorkOrder> workOrderList);

    List<WorkOrder> getOverDueAndNotCompletedWorkOrdersBySpecifiedTime(int month, String userId, List<String> stringList);

    List<WorkOrder> getOverDueAndCompletedWorkOrders(int month, String userId, List<WorkOrder> workOrderListOfUserInSpecifiedMonth);

    long onTimeCompletedWorkOrdersInSpecifiedTime(int month, List<WorkOrder> workOrderListExceptClosedOnes);

    long onTimeCompletedWorkOrdersInSpecifiedTimeRate(long totalWorkOrdersInSpecifiedTime, long onTimeCompletedWorkOrders);

    List<WorkOrder> generateWorkOrdersForTodayByTodayScheduleMaintenanceLists(List<ScheduleMaintenanceBackup> todayScheduleMaintenanceBackupsForGeneratingWorkOrder);

    long countOnTimeCompletedWorkOrdersBySpecifiedMonth(List<WorkOrder> workOrderListOfUserInSpecificMonth);

    long countOverDueAndNotCompletedWorkOrdersBySpecifiedTime(int month, String userId, List<String> stringList);

    long countOverDueAndCompletedWorkOrders(int month, String userAssignedId, List<WorkOrder> numberOfTotalWorkOrdersAfterSpecifiedMonth);

    List<AssignedWorkOrderFilterDTO> getAllByFilterAndPaginationByUserId(String userId, WorkOrderDTO workOrderDTO, Pageable pageable, Integer totalElement);

    long countAllFilteredWorkOrderByUserId(String userId, WorkOrderDTO workOrderDTO);

    long getAllWorkOrdersInSpecifiedMonth(int month, List<String> stringList);

    long countTotalWorkOrdersAfterSpecifiedMonth(int month, String userId, List<String> stringList);

    List<WorkOrder> getAllWorkOrderListOfUserInSpecifiedMonth(int month, String userId);

    List<WorkOrder> getOnTimeCompletedWorkOrders(List<WorkOrder> workOrderListOfUserInSpecificMonth);

    List<WorkOrder> getAllWorkOrderOfUser(String userAssignedId);

    List<WorkOrder> getAllWorkOrdersExceptClosedOne(List<String> stringList);

    List<WorkOrder> getAllWorkOrderListExceptClosedOnesOfUserInSpecifiedMonth(int month, String userId, List<String> stringList);

    List<WorkOrder> getAllClosedWorkOrders(List<String> stringList);

    List<WorkOrder> getAllClosedWorkOrdersOfUserInSpecificMonth(int month, String userAssignedId, List<String> stringList);

    long getAllClosedWorkOrdersInSpecifiedMonth(int month, List<String> workOrderStatusIdList);

    long getNumberOfOnTimeClosedWorkOrders(List<WorkOrder> closedWorkOrderList);

    List<WorkOrder> getAllOpenWorkOrderList(List<String> stringList);

    long numberOfOpenAndOverDueWorkOrderOfUserInSpecifiedTime(int month, String userId, List<String> stringList);

    List<WorkOrder> getAllOpenWorkOrdersListByUserIdBySpecifiedCurrentMonth(int month, String userId, List<String> stringList);

    List<WorkOrder> getAllClosedWorkOrdersOfUserInSpecifiedMonth(int month, String userAssignedId, List<String> stringList);

    List<WorkOrder> getAllWorkOrderListExceptDraftForUser(List<String> stringList, String userAssignedId);

    List<WorkOrder> getAllWorkOrdersExceptDraftOfUser(int month, String userAssignedId, List<String> stringList);

    List<WorkOrder> getAllHighPriorityWorkOrders(List<String> stringList1);

    List<WorkOrder> getAllHighPriorityWorkOrderOfUser(String userAssignedId, List<String> stringList1);

    List<WorkOrder> getPendingWorkOrdersOfUserInSpecificMonth(int month, String userAssignedId, List<String> stringList);

    long getAllOpenAndOverDueWorkOrderList(List<String> workOrderStatusIdList);

    WorkOrder getOneWorkOrderForNotification(String workOrderId);

    WorkOrder getWorkOrderByNotifyReferenceId(String referenceId);

    void generatingNewWorkOrderByTriggeredScheduleMaintenanceInDistanceRecord(ScheduleMaintenanceBackup newScheduleMaintenanceBackup);

    WorkOrder getWorkOrderStatusId(String workOrderId);

    boolean checkIfUserExistWorkOrder(String userId);

    void createWorkOrderForWorkRequest(String workRequestId, String assetId);

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

    List<WorkOrder> getInCompleteWorkOrders(List<String> workOrderStatusIdList);

    List<WorkOrder> getRealAllWorkOrdersInSpecifiedMonth(int month, String userAssignedId);

    List<WorkOrder> getAllRealOpenWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> workOrderStatusListString);

    List<WorkOrder> getHighestPriorityWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> workOrderStatusListString);

    List<WorkOrder> getOpenWorkOrderListWithUserAssignedIdAndStatus(String userAssignedId, List<String> openWorkOrderStatusStringList);

    List<WorkOrder> getRealHighPriorityWorkOrders(String userAssignedId, int month, List<String> allWorkOrderStatusExceptClosedString);

    List<WorkOrder> getRealAveragePriorityWorkOrders(String userAssignedId, int month, List<String> allWorkOrderStatusExceptClosedString);

    List<WorkOrder> getRealLowPriorityWorkOrders(String userAssignedId, int month, List<String> allWorkOrderStatusExceptClosedString);

    List<WorkOrder> getRealVeryLowPriorityWorkOrders(String userAssignedId, int month, List<String> allWorkOrderStatusExceptClosedString);

    List<WorkOrder> getRealMaintenanceTypeWorkOrders(String userAssignedId, int month, MaintenanceType maintenanceType, List<String> allWorkOrderStatusExceptClosedString);

    long countRealHighestPriorityWorkOrders(String userAssignedId, int month, List<String> allWorkOrderStatusExceptClosedString);

    long countRealHighPriorityWorkOrders(String userAssignedId, int month, List<String> allWorkOrderStatusExceptClosedString);

    long countRealAveragePriorityWorkOrders(String userAssignedId, int month, List<String> allWorkOrderStatusExceptClosedString);

    long countRealLowPriorityWorkOrders(String userAssignedId, int month, List<String> allWorkOrderStatusExceptClosedString);

    long countRealVeryLowPriorityWorkOrders(String userAssignedId, int month, List<String> allWorkOrderStatusExceptClosedString);

    List<WorkOrder> getRealLateAndOpenWorkOrders(String userId, List<String> openWorkOrderStatusStringList);

    List<WorkOrder> getRealPendingWorkOrder(String userAssignedId, List<String> pendingWorkOrderStatusStringList);

    List<WorkOrder> getRealCurrentWeekOpenWorkOrders(String userAssignedId, List<String> openWorkOrderStatusStringList);

    long countRealOpenWorkOrders(String userAssignedId, int month, List<String> openWorkOrderStatusStringList);

    List<WorkOrder> getRealClosedWorkOrders(String userAssignedId, int month, List<String> closedWorkOrderStatusStringList);

    long countRealClosedWorkOrders(String userAssignedId, int month, List<String> closedWorkOrderStatusStringList);

    List<WorkOrder> getRealPendingWorkOrders(String userAssignedId, int month, List<String> pendingWorkOrderStatusStringList);

    List<WorkOrder> getRealDraftWorkOrders(String userAssignedId, int month, List<String> draftWorkOrderStatusStringList);

    long countRealDraftWorkOrders(String userAssignedId, int month, List<String> draftWorkOrderStatusStringList);

    List<WorkOrder> getRealOverDueWorkOrders(String userAssignedId, int month, List<String> openWorkOrderStatusStringList);

    long countRealOverDueWorkOrders(String userAssignedId, int month, List<String> openWorkOrderStatusStringList);

    long realPlannedMaintenance(int month, List<String> closedWorkOrderStatusStringList);

    long realUnplannedWorkOrders(int month, List<String> closedWorkOrderStatusStringList);

    long countGeneralRealClosedWorkOrders(int month, List<String> closedWorkOrderStatusStringList);

    long countGeneralRealOnTimeClosedWorkOrders(int month, List<String> closedWorkOrderStatusStringList);

    long countRealPendingWorkOrdersOfUserInSpecificMonth(int month, String userAssignedId, List<String> stringList);

    List<WorkOrder> managerDashboardGetOverDueWorkOrders(List<String> openWorkOrderStatusStringList, int month);

    List<WorkOrder> getHighestPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> exceptClosedWorkOrderStatusStringList);

    long countHighestPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> exceptClosedWorkOrderStatusStringList);

    List<WorkOrder> getHighPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> exceptClosedWorkOrderStatusStringList);

    long countHighPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> exceptClosedWorkOrderStatusStringList);

    List<WorkOrder> getAveragePriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> exceptClosedWorkOrderStatusStringList);

    long countAveragePriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> exceptClosedWorkOrderStatusStringList);

    List<WorkOrder> getLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> exceptClosedWorkOrderStatusStringList);

    long countLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> exceptClosedWorkOrderStatusStringList);

    List<WorkOrder> getVeryLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> exceptClosedWorkOrderStatusStringList);

    long countVeryLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<String> exceptClosedWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetHighestPriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountHighestPriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetHighPriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountHighPriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetAveragePriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountAveragePriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetLowPriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountLowPriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetLowestPriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountLowestPriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetOpenWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountOpenWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetClosedWorkOrder(int month, List<String> closedWorkOrderStatusStringList);

    long managerDashboardCountClosedWorkOrder(int month, List<String> closedWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetPendingWorkOrders(int month, List<String> pendingWorkOrderStatusStringList);

    long managerDashboardCountPendingWorkOrders(int month, List<String> pendingWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetDraftWorkOrders(int month, List<String> draftWorkOrderStatusStringList);

    long managerDashboardCountDraftWorkOrders(int month, List<String> draftWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardHighestPriorityPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountHighestPriorityPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetHighPriorityPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountHighPriorityPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetAveragePriorityPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountAveragePriorityPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetLowPriorityPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountLowPriorityPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetLowestPriorityPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountLowestPriorityPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetOpenPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long managerDashboardCountOpenPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetClosedPlannedWorkOrders(int month, List<String> closedWorkOrderStatusStringList);

    long managerDashboardCountClosedPlannedWorkOrders(int month, List<String> closedWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetPendingPlannedWorkOrders(int month, List<String> pendingWorkOrderStatusStringList);

    long managerDashboardCountPendingPlannedWorkOrders(int month, List<String> pendingWorkOrderStatusStringList);

    List<DashboardWorkOrderDTO> managerDashboardGetDraftPlannedWorkOrders(int month, List<String> draftWorkOrderStatusStringList);

    long managerDashboardCountDraftPlannedWorkOrders(int month, List<String> draftWorkOrderStatusStringList);

    List<DashboardOverDueWorkOrderDTO> managerDashboardCountOverDueWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    List<DashboardOverDueWorkOrderDTO> managerDashboardGetOverDuePlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList);

    long numberOfPlannedWorkOrders(int month);

    long numberOfAllWorkOrders(int month);

    List<UnScheduledWorkOrderDTO> managerDashboardUnscheduledWorkOrdersForBar();

    List<AggregateResultPendingBarDTO> getPendingWorkOrdersForDashboardBar(List<String> pendingWorkOrderStatusStringList);

    List<LateBarDTO> managerDashboardLateWorkOrdersForBar(List<String> openWorkOrderStatusStringList);

    List<CurrentWeekWorkOrderDTO> managerDashboardCurrentWeekWorkOrdersBar(List<String> openWorkOrderStatusStringList);

    List<CurrentWeekPlannedWorkOrderDTO> managerDashboardCurrentPlannedWorkOrdersBar();

    List<WorkOrder> getWorkOrderPriorityAndMaintenanceType(List<String> workRequestIdList);

    void workOrderAcceptedByManager(String workOrderId, boolean workOrderAccepted);

    WorkOrder getBasicInformationOfWorkOrder(String workOrderId);

    WorkOrder getTasksByWorkOrderId(String workOrderId);

    void addTaskId(String id, String workOrderId);

    void deleteTaskFromWorkOrder(String workOrderId, String taskId);

    void addUsedPartToWorkOrder(String workOrderId, String partId);

    void deleteUsedPartFromWorkOrder(String workOrderId, String partWithUsageCountId);

    void deleteDocumentFromWorkOrder(String documentFileId, String workOrderId);

    void addDocumentToWorkOrder(String workOrderId, String documentId);

    WorkOrder getDocumentIdListOfWorkOrder(String workOrderId);

    WorkOrder getWorkOrderForRepository(String workOrderId);

    WorkOrder getPartWithUsageCountOfWorkOrder(String workOrderId);

    boolean newSaveWorkOrder(NewSaveDTO newSaveDTO);

    boolean newUpdate(NewSaveDTO newSaveDTO);

    boolean scheduleWorkOrderUpdate(ReqWorkOrderScheduleUpdateDTO entity);

    WorkOrderPrimaryDTO newGetOne(String workOrderId);

    List<UsedPartReport> usedPartOfWOrkOrder(UsedPartOfWorkOrder usedPartOfWorkOrder, Pageable pageable, Integer totalElement);

    long countUsedPart(UsedPartOfWorkOrder usedPartOfWorkOrder);

    CountUsedPartDTO countUsedPartOfWorkOrder(UsedPartOfWorkOrder usedPartOfWorkOrder);

    List<PersonnelFunctionGetAllDTO> personnelFunction(PersonnelFunctionDTO personnelFunctionDTO, Pageable pageable, Integer totalElement);

    long countPersonnelFunction(PersonnelFunctionDTO personnelFunctionDTO, Pageable pageable, Integer totalElement);

    TotalWorkedTimeDTO TotalWorkedTimeOfPersonnel(PersonnelFunctionDTO personnelFunctionDTO);

    List<MtbfReturn> mtbfCalculation(MtbfDTO mtbfDTO);

    List<MttrReturn> mttrCalculation(MttrDTO mttrDTO);

    List<MtbfTableReturn> mtbfTable(MtbfTableDTO mtbfTableDTO);

    List<MttrTableReturn> mttrTable(MttrTableDTO mttrTableDTO);

    WorkOrder isAcceptedByManager(String workOrderId);

    WorkOrder getWorkOrderTechnicians(String workRequestId);

    List<WorkOrderDTO> getAllWorkOrderForExcel(WorkOrderDTO workOrderDTO);

    List<MdtReturn> mdtCalculation(MdtDTO mdtDTO);

    List<MdtTableReturn> mdtTable(MdtTableDTO mdtTableDTO);

    WorkOrder createWorkOrderAccordingToAssociatedWorkRequest(WorkOrderSchedule entity);

    WorkOrderScheduleDTO getOneScheduleWorkOrder(String workOrderId);

    boolean updateScheduleWorkOrder(WorkOrderScheduleDTO workOrderScheduleDTO);

    void deleteWorkOrderSchedule(String id);

    void setEndDateOfWorkOrderSchedule(String workOrderId);

}
