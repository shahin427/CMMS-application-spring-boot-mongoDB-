package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.Mongo.UserTask;
import org.sayar.net.Model.newModel.Task.model.LaborTask;

import java.util.List;

public interface LaborTaskDao extends GeneralDao<LaborTask> {
    LaborTask addTask(LaborTask task, String workOrderId);
    List<UserTask> myTask();
    List<UserTask> workOrderTask(String workOrderId);
    boolean updateTask(UserTask userTask);
}
