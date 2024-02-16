package org.sayar.net.Service.newService;


import org.sayar.net.Dao.NewDao.TaskGroupDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.TaskGroupDTO;
import org.sayar.net.Model.newModel.Task.model.TaskGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskGroupServiceImpl extends GeneralServiceImpl<TaskGroup> implements TaskGroupService {

    @Autowired
    private TaskGroupDao taskGroupDao;

    @Override
    public TaskGroup postTaskGroup(TaskGroup taskGroup) {
        return taskGroupDao.postTaskGroup(taskGroup);
    }

    @Override
    public TaskGroup getOne(String taskGroupId) {
        return taskGroupDao.getOne(taskGroupId);
    }

    @Override
    public Page<TaskGroup> getAllByPagination(String term, String code, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                taskGroupDao.getAll(term, code, pageable, totalElement)
                , pageable
                , taskGroupDao.countAllTaskGroupByTerm(term, code)
        );
    }

    @Override
    public boolean updateTaskGroup(TaskGroup taskGroup) {
        return super.updateResultStatus(taskGroupDao.updateTaskGroup(taskGroup));
    }

    @Override
    public boolean checkTaskGroupCode(String taskGroupCode) {
        return taskGroupDao.checkTaskGroupCode(taskGroupCode);
    }

    @Override
    public List<TaskGroup> getAllTaskGroupByTaskGroupList(List<String> taskGroups) {
        return taskGroupDao.getAllTaskGroupByTaskGroupList(taskGroups);
    }

    @Override
    public List<TaskGroupDTO> getAllTaskGroupListByIdList(List<String> taskGroupList) {
        return taskGroupDao.getAllTaskGroupListByIdList(taskGroupList);
    }

    @Override
    public boolean checkIfTaskGroupCodeIsUnique(String code) {
        return taskGroupDao.checkIfTaskGroupCodeIsUnique(code);
    }

    @Override
    public List<TaskGroupDTO> getAllTaskGroupForNoticeBoard() {
        return taskGroupDao.getAllTaskGroupForNoticeBoard();
    }

    @Override
    public void pushTaskIdToTaskGroup(String taskId, String taskGroupId) {
        taskGroupDao.pushTaskIdToTaskGroup(taskId, taskGroupId);
    }

    @Override
    public List<TaskGroupDTO> getAllTaskGroupOfWorkOrder(List<String> taskGroups) {
        return taskGroupDao.getAllTaskGroupOfWorkOrder(taskGroups);
    }


}
