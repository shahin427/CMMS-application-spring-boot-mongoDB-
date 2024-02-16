package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.newModel.Enum.WorkOrderStatusEnum;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkOrderStatusDao extends GeneralDao<WorkOrderStatus> {

    boolean checkWorkOrderStatusExistence(WorkOrderStatusEnum statusEnum, String persianName);

    WorkOrderStatus getWorkOrderStatusDefault(WorkOrderStatusEnum workOrderStatusEnum);

    WorkOrderStatus saveStatus(WorkOrderStatus workOrderStatus);

    WorkOrderStatus updateStatus(WorkOrderStatus workOrderStatus);

    long getAllCount(String term,String status);

    List<WorkOrderStatus> findAllWorkOrderStatus(String term,String status, Pageable pageable, Integer totalElement);

    WorkOrderStatus getWorkOrderStatusByAssetId(String workOrderStatusId);

    List<WorkOrderStatus> getWorkOrderStatus();

    List<WorkOrderStatus> getWorkOrderStatusListByWorkOrderStatusId(List<String> statusIdList);

    List<WorkOrderStatus> getAllWorkOrderStatusByWorkOrderStatusId(List<String> statusIdList);

    WorkOrderStatus getWorkOrderStatusByWorkOrderStatusId(String workOrderStatusId);

    List<WorkOrderStatus> getOpenWorkOrderStatus();

    List<WorkOrderStatus> getClosedWorkOrderStatus();

    List<WorkOrderStatus> getClosedNotCompletedWorkOrdersStatus();

    List<WorkOrderStatus> getAllOpenWorkOrderStatus();

    List<WorkOrderStatus> getAllClosedCompleteAndUnCompleteWorkOrders();

    List<WorkOrderStatus> getTotalWorkOrdersExceptClosed();

    List<WorkOrderStatus> getWorkOrderStatusListExceptDraft();

    List<WorkOrderStatus> numberOfPendingWorkOrderOfUserInSpecificTime();

    List<WorkOrderStatus> getWorkOrderStatusListExceptDraftAndClose();

    List<WorkOrderStatus> getClosedCompletedWorkOrdersStatus();

    WorkOrderStatus getWorkOrderStatusById(String statusId);

    WorkOrderStatus getAssociatedWorkOrderStatusToTheSchedule(String statusId);

    List<WorkOrderStatus> getAllWorkOrderStatus();

    List<WorkOrderStatus> getOpenedDraftAndPendingWorOrderStatus();

    List<WorkOrderStatus> getAllWorkOrderStatusExceptClose();

    List<WorkOrderStatus> getPendingWorkOrderStatus();

    List<WorkOrderStatus> getDraftWorkOrderStatus();

    boolean checkIfNameExists(String name);
}
