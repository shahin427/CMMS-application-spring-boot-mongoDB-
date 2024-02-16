package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.Enum.TaskType;
import org.sayar.net.Model.newModel.Task.model.Task;
import org.sayar.net.Tools.Print;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskDTO {
    private String id;
    private String code;
    private String title;
    private Long price;
    private String description;
    private TaskType taskType;
    private List<UserInfo> users;
    private String referenceId;
    private String taskGroupId;
    private String timeEstimate;

    public static TaskDTO map(Task task, List<User> taskUserList, List<UserType> taskUserTypeList) {
        TaskDTO taskDTO = new TaskDTO();
        if (task.getId() != null)
            taskDTO.setId(task.getId());
        if (task.getCode() != null)
            taskDTO.setCode(task.getCode());
        if (task.getTitle() != null)
            taskDTO.setTitle(task.getTitle());
        if (task.getPrice() != null)
            taskDTO.setPrice(task.getPrice());
        if (task.getDescription() != null)
            taskDTO.setDescription(task.getDescription());
        if (task.getTaskType() != null)
            taskDTO.setTaskType(task.getTaskType());
        if (task.getReferenceId() != null)
            taskDTO.setReferenceId(task.getReferenceId());
        if (task.getTaskGroupId() != null)
            taskDTO.setTaskGroupId(task.getTaskGroupId());
        if (task.getTimeEstimate() != null)
            taskDTO.setTimeEstimate(task.getTimeEstimate());

        List<UserInfo> userInfos = new ArrayList<>();

        taskUserList.forEach(user -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(user.getId());
            userInfo.setName(user.getName());
            userInfo.setFamily(user.getFamily());
            taskUserTypeList.forEach(userType -> {
                if (userType.getId().equals(user.getUserTypeId())) {
                    userInfo.setUserTypeName(userType.getName());
                }
            });
            userInfos.add(userInfo);
        });
        taskDTO.setUsers(userInfos);
        return taskDTO;
    }
}
