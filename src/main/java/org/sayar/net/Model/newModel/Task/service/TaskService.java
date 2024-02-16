package org.sayar.net.Model.newModel.Task.service;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.TaskDTO;
import org.sayar.net.Model.newModel.Task.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService extends GeneralService<Task> {

    Task taskService(Task task);

    Page<Task> getAllTask(String term, Pageable pageable, Integer totalElement);

    boolean updateTask(Task task);

    Task getOneTask(String taskId);

    boolean checkTaskCode(String taskCode);

    List<Task> getAllTasksByTaskList(List<String> tasks);

    List<Task> getTaskListByReferenceId(String referenceId);

    List<Task> getTaskListByTaskGroupId(String taskGroupId);

    boolean ifTaskExistsInTaskGroup(String taskId);

    TaskDTO getTaskByTaskId(String taskId);

    List<Task> getTasksOfWorkOrder(List<String> tasks);

    Task saveTaskOfWorkOrder(Task task);
}
