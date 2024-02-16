package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkOrderStatusService extends GeneralService<WorkOrderStatus> {

    WorkOrderStatus saveStatus(WorkOrderStatus workOrderStatus);

    WorkOrderStatus updateStatus(WorkOrderStatus workOrderStatus);

    Page<WorkOrderStatus> getAllByTermAndPagination(String term, String status, Pageable pageable, Integer totalElement);

    WorkOrderStatus getWorkOrderStatusByAssetId(String workOrderStatusId);

    List<WorkOrderStatus> getWorkOrderStatus();

    List<WorkOrderStatus> getWorkOrderStatusListByWorkOrderStatusId(List<String> statusIdList);

    List<WorkOrderStatus> getAllWorkOrderStatusByWorkOrderStatusId(List<String> statusIdList);

    WorkOrderStatus getWorkOrderStatusByWorkOrderStatusId(String workOrderStatusId);

    List<WorkOrderStatus> getOpenWorkOrderStatus();

    List<WorkOrderStatus> getClosedWorkOrdersStatus();

    List<WorkOrderStatus> getClosedNotCompletedWorkOrdersStatus();

    List<WorkOrderStatus> getAllOpenWorkOrderStatus();

    List<WorkOrderStatus> getAllClosedWorkOrderStatus();

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
