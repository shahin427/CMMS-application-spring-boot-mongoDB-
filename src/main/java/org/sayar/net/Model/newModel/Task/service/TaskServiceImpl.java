package org.sayar.net.Model.newModel.Task.service;

import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.TaskDTO;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.Task.dao.TaskDao;
import org.sayar.net.Model.newModel.Task.model.Task;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintenanceBackupService;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.UserTypeService;
import org.sayar.net.Service.newService.TaskGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl extends GeneralServiceImpl<Task> implements TaskService {
    @Autowired
    private TaskDao taskDao;

    @Autowired
    private ScheduleMaintenanceBackupService scheduleMaintenanceBackupService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private TaskGroupService taskGroupService;

    @Override
    public Task taskService(Task task) {
        Task savedTask = taskDao.taskService(task);
        if (savedTask.getTaskGroupId() != null)
            taskGroupService.pushTaskIdToTaskGroup(savedTask.getId(), savedTask.getTaskGroupId());

        if (savedTask.getReferenceId() != null)
            scheduleMaintenanceBackupService.updateScheduleMaintenanceBackUpTaskPartByTask(savedTask.getId(), savedTask.getReferenceId());
        return savedTask;
    }

    @Override
    public Page<Task> getAllTask(String term, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                taskDao.getAllTask(term, pageable, totalElement)
                , pageable
                , taskDao.countAllTask(term)
        );
    }

    @Override
    public boolean updateTask(Task task) {
        return super.updateResultStatus(taskDao.updateTask(task));
    }

    @Override
    public Task getOneTask(String taskId) {
        return taskDao.getOneTask(taskId);
    }

    public boolean checkTaskCode(String taskCode) {
        return taskDao.checkTaskCode(taskCode);
    }

    @Override
    public List<Task> getAllTasksByTaskList(List<String> tasks) {
        return taskDao.getAllTasksByTaskList(tasks);
    }

    @Override
    public List<Task> getTaskListByReferenceId(String referenceId) {
        return taskDao.getTaskListByReferenceId(referenceId);
    }

    @Override
    public List<Task> getTaskListByTaskGroupId(String taskGroupId) {
        return taskDao.getTaskListByTaskGroupId(taskGroupId);
    }

    @Override
    public boolean ifTaskExistsInTaskGroup(String taskId) {
        return taskDao.ifTaskExistsInTaskGroup(taskId);
    }

    @Override
    public TaskDTO getTaskByTaskId(String taskId) {
        Task task = taskDao.getOneTask(taskId);
        if (task != null) {
            List<User> taskUserList = new ArrayList<>();
            List<UserType> taskUserTypeList = new ArrayList<>();

            if (task.getUsers() != null) {
                taskUserList = userService.getTaskUserList(task.getUsers());
                List<String> userTypeIdList = new ArrayList<>();
                taskUserList.forEach(user -> userTypeIdList.add(user.getUserTypeId()));
                taskUserTypeList = userTypeService.getUserTypeTitleList(userTypeIdList);
            }
            return TaskDTO.map(task, taskUserList, taskUserTypeList);
        } else {
            return null;
        }
    }

    @Override
    public List<Task> getTasksOfWorkOrder(List<String> tasks) {
        return taskDao.getTasksOfWorkOrder(tasks);
    }

    @Override
    public Task saveTaskOfWorkOrder(Task task) {
        return taskDao.saveTaskOfWorkOrder(task);
    }

}
