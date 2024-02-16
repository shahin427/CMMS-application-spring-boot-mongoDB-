package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.WorkOrderStatusDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class WorkOrderStatusServiceImpl extends GeneralServiceImpl<WorkOrderStatus> implements WorkOrderStatusService {
    @Autowired
    private WorkOrderStatusDao dao;

    public WorkOrderStatusServiceImpl(WorkOrderStatusDao dao) {
        this.dao = dao;
    }

    @Override
    public WorkOrderStatus saveStatus(WorkOrderStatus workOrderStatus) {
        return dao.saveStatus(workOrderStatus);
    }

    @Override
    public WorkOrderStatus updateStatus(WorkOrderStatus workOrderStatus) {
        return dao.updateStatus(workOrderStatus);
    }

    @Override
    public Page<WorkOrderStatus> getAllByTermAndPagination(String term, String status, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.findAllWorkOrderStatus(term, status, pageable, totalElement)
                , pageable
                , dao.getAllCount(term,status)
        );
    }

    @Override
    public WorkOrderStatus getWorkOrderStatusByAssetId(String workOrderStatusId) {
        return dao.getWorkOrderStatusByAssetId(workOrderStatusId);
    }

    @Override
    public List<WorkOrderStatus> getWorkOrderStatus() {
        return dao.getWorkOrderStatus();
    }

    @Override
    public List<WorkOrderStatus> getWorkOrderStatusListByWorkOrderStatusId(List<String> statusIdList) {
        return dao.getWorkOrderStatusListByWorkOrderStatusId(statusIdList);
    }

    @Override
    public List<WorkOrderStatus> getAllWorkOrderStatusByWorkOrderStatusId(List<String> statusIdList) {
        return dao.getAllWorkOrderStatusByWorkOrderStatusId(statusIdList);
    }

    @Override
    public WorkOrderStatus getWorkOrderStatusByWorkOrderStatusId(String workOrderStatusId) {
        return dao.getWorkOrderStatusByWorkOrderStatusId(workOrderStatusId);
    }

    @Override
    public List<WorkOrderStatus> getOpenWorkOrderStatus() {
        return dao.getOpenWorkOrderStatus();
    }

    @Override
    public List<WorkOrderStatus> getClosedWorkOrdersStatus() {
        return dao.getClosedWorkOrderStatus();
    }

    @Override
    public List<WorkOrderStatus> getClosedNotCompletedWorkOrdersStatus() {
        return dao.getClosedNotCompletedWorkOrdersStatus();
    }

    @Override
    public List<WorkOrderStatus> getAllOpenWorkOrderStatus() {
        return dao.getAllOpenWorkOrderStatus();
    }

    @Override
    public List<WorkOrderStatus> getAllClosedWorkOrderStatus() {
        return dao.getClosedWorkOrderStatus();
    }

    @Override
    public List<WorkOrderStatus> getAllClosedCompleteAndUnCompleteWorkOrders() {
        return dao.getAllClosedCompleteAndUnCompleteWorkOrders();
    }

    @Override
    public List<WorkOrderStatus> getTotalWorkOrdersExceptClosed() {
        return dao.getTotalWorkOrdersExceptClosed();
    }

    @Override
    public List<WorkOrderStatus> getWorkOrderStatusListExceptDraft() {
        return dao.getWorkOrderStatusListExceptDraft();
    }

    @Override
    public List<WorkOrderStatus> numberOfPendingWorkOrderOfUserInSpecificTime() {
        return dao.numberOfPendingWorkOrderOfUserInSpecificTime();
    }

    @Override
    public List<WorkOrderStatus> getWorkOrderStatusListExceptDraftAndClose() {
        return dao.getWorkOrderStatusListExceptDraftAndClose();
    }

    @Override
    public List<WorkOrderStatus> getClosedCompletedWorkOrdersStatus() {
        return dao.getClosedCompletedWorkOrdersStatus();
    }

    @Override
    public WorkOrderStatus getWorkOrderStatusById(String statusId) {
        return dao.getWorkOrderStatusById(statusId);
    }

    @Override
    public WorkOrderStatus getAssociatedWorkOrderStatusToTheSchedule(String statusId) {
        return dao.getAssociatedWorkOrderStatusToTheSchedule(statusId);
    }

    @Override
    public List<WorkOrderStatus> getAllWorkOrderStatus() {
        return dao.getAllWorkOrderStatus();
    }

    @Override
    public List<WorkOrderStatus> getOpenedDraftAndPendingWorOrderStatus() {
        return dao.getOpenedDraftAndPendingWorOrderStatus();
    }

    @Override
    public List<WorkOrderStatus> getAllWorkOrderStatusExceptClose() {
        return dao.getAllWorkOrderStatusExceptClose();
    }

    @Override
    public List<WorkOrderStatus> getPendingWorkOrderStatus() {
        return dao.getPendingWorkOrderStatus();
    }

    @Override
    public List<WorkOrderStatus> getDraftWorkOrderStatus() {
        return dao.getDraftWorkOrderStatus();
    }

    @Override
    public boolean checkIfNameExists(String name) {
        return dao.checkIfNameExists(name);
    }


}
