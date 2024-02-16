package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.Mongo.UserTask;
import org.sayar.net.Model.newModel.Task.model.LaborTask;

import java.util.List;

public interface LaborTaskService extends GeneralService<LaborTask> {
    LaborTask addTask(LaborTask task, String workOrderId);
    List<UserTask> myTask();
    List<UserTask> workOrderTask(String workOrderId);
    boolean updateTask(UserTask userTask);


}
