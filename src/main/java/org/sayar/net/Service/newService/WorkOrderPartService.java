package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.newModel.WorkOrder.PartWithUsageCount;

import java.util.List;

public interface WorkOrderPartService extends GeneralService<PartWithUsageCount> {

    List<PartWithUsageCount> getWorkOrderPartListByReferenceId(String referenceId);

    PartWithUsageCount postWorkOrderPart(PartWithUsageCount partWithUsageCount);

    List<PartWithUsageCount> getAllWorkOrderPart();

    PartWithUsageCount getOneWorkOrderPart(String partWithUsageCountId);

    boolean updateWorkOrderPart(PartWithUsageCount partWithUsageCount);

    boolean checkIfInventoryExistsInWorkOrder(String partId);

    PartWithUsageCount getPartWithUsageCountListByReferenceId(String referenceId);

    boolean updatePArtWithUsageCount(String partWithUsageCountId,PartWithUsageCount partWithUsageCount);

    boolean ifThePartUsedBeforeInTheWorkOrderPart(PartWithUsageCount partWithUsageCount);

    List<PartWithUsageCount> getPartOfRepository(List<String> usedParts);

    List<PartWithUsageCount> getPartWithUsageCountOfWorkOrder(List<String> usedParts);

    PartWithUsageCount postWorkOrderPartOfNoticeBoard(PartWithUsageCount partWithUsageCount);
}
