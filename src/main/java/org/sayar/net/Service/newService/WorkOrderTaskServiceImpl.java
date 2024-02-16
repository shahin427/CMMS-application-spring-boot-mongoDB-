package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderTasks;
import org.springframework.stereotype.Service;

@Service("workOrderTaskServiceImpl")
public class WorkOrderTaskServiceImpl extends GeneralServiceImpl<WorkOrderTasks> implements WorkOrderTaskService{


}
