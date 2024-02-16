package org.sayar.net.Model.newModel.Task.dao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.newModel.Task.model.Task;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskDao extends GeneralDao<Task> {

    Task taskService(Task task);

    List<Task> getAllTask(String term, Pageable pageable, Integer totalElement);

    long countAllTask(String term);

    UpdateResult updateTask(Task task);

    Task getOneTask(String taskId);

    boolean checkTaskCode(String taskCode);

    List<Task> getAllTasksByTaskList(List<String> tasks);

    List<Task> getTaskListByReferenceId(String referenceId);

    List<Task> getTaskListByTaskGroupId(String taskGroupId);

    boolean ifTaskExistsInTaskGroup(String taskId);

    List<Task> getTasksOfWorkOrder(List<String> tasks);

    Task saveTaskOfWorkOrder(Task task);
}
