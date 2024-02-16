package org.sayar.net.Model.ResponseModel;

import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

import java.util.List;

public class WorkOrderRes {

    private WorkOrder workOrder;
    private List<Long> ids;

    public WorkOrderRes() {
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
