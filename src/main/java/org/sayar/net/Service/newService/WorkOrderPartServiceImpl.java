package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.WorkOrderPartDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.Part.Part;
import org.sayar.net.Model.newModel.WorkOrder.PartWithUsageCount;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintenanceBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkOrderPartServiceImpl extends GeneralServiceImpl<PartWithUsageCount> implements WorkOrderPartService {
    @Autowired
    private WorkOrderPartDao workOrderPartDao;

    @Autowired
    private PartService partService;

    @Autowired
    private ScheduleMaintenanceBackupService scheduleMaintenanceBackupService;

    @Override
    public List<PartWithUsageCount> getWorkOrderPartListByReferenceId(String referenceId) {
        return workOrderPartDao.getWorkOrderPartListByReferenceId(referenceId);
    }

    @Override
    public PartWithUsageCount postWorkOrderPart(PartWithUsageCount partWithUsageCount) {
        Part part = partService.getPartCode(partWithUsageCount.getPartId());
        partWithUsageCount.setPartCode(part.getPartCode());
        PartWithUsageCount partWithUsageCount1 = workOrderPartDao.postWorkOrderPart(partWithUsageCount);
        scheduleMaintenanceBackupService.addUserPartToScheduleBackup(partWithUsageCount1.getId(), partWithUsageCount.getReferenceId());
        return partWithUsageCount1;
    }

    @Override
    public List<PartWithUsageCount> getAllWorkOrderPart() {
        return workOrderPartDao.getAllWorkOrderPart();
    }

    @Override
    public PartWithUsageCount getOneWorkOrderPart(String partWithUsageCountId) {
        return workOrderPartDao.getOneWorkOrderPart(partWithUsageCountId);
    }

    @Override
    public boolean updateWorkOrderPart(PartWithUsageCount partWithUsageCount) {
        return super.updateResultStatus(workOrderPartDao.updateWorkOrderPart(partWithUsageCount));
    }

    @Override
    public boolean checkIfInventoryExistsInWorkOrder(String partId) {
        return workOrderPartDao.checkIfInventoryExistsInWorkOrder(partId);
    }

    @Override
    public PartWithUsageCount getPartWithUsageCountListByReferenceId(String referenceId) {
        return workOrderPartDao.getPartWithUsageCountListByReferenceId(referenceId);
    }

    @Override
    public boolean updatePArtWithUsageCount(String partWithUsageCountId, PartWithUsageCount partWithUsageCount) {
        return super.updateResultStatus(workOrderPartDao.updatePArtWithUsageCount(partWithUsageCountId, partWithUsageCount));
    }

    @Override
    public boolean ifThePartUsedBeforeInTheWorkOrderPart(PartWithUsageCount partWithUsageCount) {
        return workOrderPartDao.ifThePartUsedBeforeInTheWorkOrderPart(partWithUsageCount);
    }

    @Override
    public List<PartWithUsageCount> getPartOfRepository(List<String> usedParts) {
        return workOrderPartDao.getPartOfRepository(usedParts);
    }

    @Override
    public List<PartWithUsageCount> getPartWithUsageCountOfWorkOrder(List<String> usedParts) {
        return workOrderPartDao.getPartWithUsageCountOfWorkOrder(usedParts);
    }

    @Override
    public PartWithUsageCount postWorkOrderPartOfNoticeBoard(PartWithUsageCount partWithUsageCount) {
        Part part = partService.getPartCode(partWithUsageCount.getPartId());
        partWithUsageCount.setPartCode(part.getPartCode());
        //        scheduleMaintenanceBackupService.addUserPartToScheduleBackup(partWithUsageCount1.getId(), partWithUsageCount.getReferenceId());
        return workOrderPartDao.postWorkOrderPart(partWithUsageCount);
    }

}
