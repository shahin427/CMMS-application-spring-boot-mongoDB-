package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.newModel.WorkOrder.PartWithUsageCount;

import java.util.List;


public interface WorkOrderPartDao extends GeneralDao<PartWithUsageCount> {

    List<PartWithUsageCount> getAllWorkOrderPartListByWorkOrderId(List<String> workOrderParts);

    List<PartWithUsageCount> getWorkOrderPartListByReferenceId(String referenceId);

    PartWithUsageCount postWorkOrderPart(PartWithUsageCount partWithUsageCount);

    List<PartWithUsageCount> getAllWorkOrderPart();

    PartWithUsageCount getOneWorkOrderPart(String partWithUsageCountId);

    UpdateResult updateWorkOrderPart(PartWithUsageCount partWithUsageCount);

    boolean checkIfInventoryExistsInWorkOrder(String partId);

    PartWithUsageCount getPartWithUsageCountListByReferenceId(String referenceId);

    UpdateResult updatePArtWithUsageCount(String partWithUsageCountId, PartWithUsageCount partWithUsageCount);

    boolean ifThePartUsedBeforeInTheWorkOrderPart(PartWithUsageCount partWithUsageCount);

    List<PartWithUsageCount> getPartOfRepository(List<String> usedParts);

    List<PartWithUsageCount> getPartWithUsageCountOfWorkOrder(List<String> usedParts);
}
