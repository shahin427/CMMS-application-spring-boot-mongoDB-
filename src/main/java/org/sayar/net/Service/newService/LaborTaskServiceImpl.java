package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.LaborTaskDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Mongo.UserTask;
import org.sayar.net.Model.newModel.Task.model.LaborTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("luberTaskServiceImpl")
public class LaborTaskServiceImpl extends GeneralServiceImpl<LaborTask> implements LaborTaskService {
    @Autowired
    private LaborTaskDao laborTaskDao;

    @Override
    public LaborTask addTask(LaborTask task, String workOrderId) {
        return laborTaskDao.addTask(task,workOrderId);
    }

    @Override
    public List<UserTask> myTask() {
        return laborTaskDao.myTask();
    }

    @Override
    public List<UserTask> workOrderTask(String workOrderId) {
        return laborTaskDao.workOrderTask(workOrderId);
    }

    @Override
    public boolean updateTask(UserTask userTask) {
        return laborTaskDao.updateTask(userTask);
    }
}
