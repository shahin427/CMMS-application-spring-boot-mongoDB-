package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.WorkOrderMiscCostDTO;
import org.sayar.net.Model.newModel.MiscCost;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

import java.util.List;

public interface MiscCostService extends GeneralService<MiscCost> {

    MiscCost getOneById(String id);

    MiscCost postMiscCostController(MiscCost miscCost);

    boolean updateMiscCost(MiscCost miscCost);

    List<MiscCost> getAllMiscCostByMiscCostId(List<String> miscCosts);

    List<MiscCost> getMiscCostListByIdList(WorkOrderMiscCostDTO workOrderMiscCostDTO);

    boolean updateMiscCostListByMiscCostIdList(WorkOrder workOrder, WorkOrderMiscCostDTO workOrderMiscCostDTO);

    List<MiscCost> getMiscCostListByReferenceId(String referenceId);

//    boolean updateMiscCostList(workOrder);
}
