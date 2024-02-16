package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.WorkOrderMiscCostDTO;
import org.sayar.net.Model.newModel.MiscCost;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

import java.util.List;

public interface MiscCostDao extends GeneralDao<MiscCost> {


    MiscCost getOneById(String id);

    MiscCost postMiscCostController(MiscCost miscCost);

    UpdateResult updateMiscCost(MiscCost miscCost);

    List<MiscCost> getAllMiscCostByMiscCost(List<String> miscCosts);

    List<MiscCost> getMiscCostListByIdList(WorkOrderMiscCostDTO workOrderMiscCostDTO);

    boolean updateMiscCostListByMiscCostIdList(WorkOrder workOrder,WorkOrderMiscCostDTO workOrderMiscCostDTO);

    List<MiscCost> getMiscCostListByReferenceId(String referenceId);
}
