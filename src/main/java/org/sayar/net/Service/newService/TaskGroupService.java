package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.TaskGroupDTO;
import org.sayar.net.Model.newModel.Task.model.TaskGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskGroupService extends GeneralService<TaskGroup> {

    TaskGroup postTaskGroup(TaskGroup taskGroup);

    TaskGroup getOne(String taskGroupId);

    Page<TaskGroup> getAllByPagination(String term,String code, Pageable pageable, Integer totalElement);

    boolean updateTaskGroup(TaskGroup taskGroup);

    boolean checkTaskGroupCode(String taskGroupCode);

    List<TaskGroup> getAllTaskGroupByTaskGroupList(List<String> taskGroups);

    List<TaskGroupDTO> getAllTaskGroupListByIdList(List<String> taskGroupList);

    boolean checkIfTaskGroupCodeIsUnique(String code);

    List<TaskGroupDTO> getAllTaskGroupForNoticeBoard();

    void pushTaskIdToTaskGroup(String taskId,String taskGroupId);

    List<TaskGroupDTO> getAllTaskGroupOfWorkOrder(List<String> taskGroups);
}
