package org.sayar.net.Model.newModel.Task.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Enum.TaskType;
import org.springframework.data.annotation.Id;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Task {
    @Id
    private String id;
    private String code;
    private String title;
    private Long price;
    private String description;
    private TaskType taskType;
    private List<String> users;
    private String referenceId;
    private String taskGroupId;
    private String timeEstimate;
    private String status;
    private boolean forSchedule;

    public enum FN {
        id, code, title, price, description, taskType,
        status, workOrderId, users, timeEstimate, referenceId, taskGroupId
    }
}
