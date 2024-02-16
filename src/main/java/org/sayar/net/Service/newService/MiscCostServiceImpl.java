package org.sayar.net.Service.newService;


import org.sayar.net.Dao.NewDao.MiscCostDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.WorkOrderMiscCostDTO;
import org.sayar.net.Model.newModel.MiscCost;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("miscCostServiceImpl")
public class MiscCostServiceImpl extends GeneralServiceImpl<MiscCost> implements MiscCostService {

    @Autowired
    private MiscCostDao miscCostDao;

    @Override
    public MiscCost getOneById(String id) {
        return miscCostDao.getOneById(id);
    }

    @Override
    public MiscCost postMiscCostController(MiscCost miscCost) {
        return miscCostDao.postMiscCostController(miscCost);
    }

    @Override
    public boolean updateMiscCost(MiscCost miscCost) {
        return super.updateResultStatus(miscCostDao.updateMiscCost(miscCost));
    }

    @Override
    public List<MiscCost> getAllMiscCostByMiscCostId(List<String> miscCosts) {
        return miscCostDao.getAllMiscCostByMiscCost(miscCosts);
    }

    @Override
    public List<MiscCost> getMiscCostListByIdList(WorkOrderMiscCostDTO workOrderMiscCostDTO) {
        return miscCostDao.getMiscCostListByIdList(workOrderMiscCostDTO);
    }

    @Override
    public boolean updateMiscCostListByMiscCostIdList(WorkOrder workOrder, WorkOrderMiscCostDTO workOrderMiscCostDTO) {
        return miscCostDao.updateMiscCostListByMiscCostIdList(workOrder,workOrderMiscCostDTO);
    }

    @Override
    public List<MiscCost> getMiscCostListByReferenceId(String referenceId) {
        return miscCostDao.getMiscCostListByReferenceId(referenceId);
    }


}
