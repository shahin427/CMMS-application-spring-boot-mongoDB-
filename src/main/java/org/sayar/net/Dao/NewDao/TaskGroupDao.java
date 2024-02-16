package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.TaskGroupDTO;
import org.sayar.net.Model.newModel.Task.model.TaskGroup;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskGroupDao extends GeneralDao<TaskGroup> {


    TaskGroup postTaskGroup(TaskGroup taskGroup);

    TaskGroup getOne(String taskGroupId);

    List<TaskGroup> getAll(String term, String code, Pageable pageable, Integer totalElement);

    UpdateResult updateTaskGroup(TaskGroup taskGroup);

    boolean checkTaskGroupCode(String taskGroupCode);

    List<TaskGroup> getAllTaskGroupByTaskGroupList(List<String> taskGroups);

    List<TaskGroupDTO> getAllTaskGroupListByIdList(List<String> taskGroupList);

    long countAllTaskGroupByTerm(String term, String code);

    boolean checkIfTaskGroupCodeIsUnique(String code);

    List<TaskGroupDTO> getAllTaskGroupForNoticeBoard();

    void pushTaskIdToTaskGroup(String taskId, String taskGroupId);

    List<TaskGroupDTO> getAllTaskGroupOfWorkOrder(List<String> taskGroups);
}
