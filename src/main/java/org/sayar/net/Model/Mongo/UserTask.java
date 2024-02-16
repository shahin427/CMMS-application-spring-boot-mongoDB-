package org.sayar.net.Model.Mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Enumes.LaborTaskType;
import org.sayar.net.Enumes.TaskStatus;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

//import javax.persistence.*;


@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@NoArgsConstructor
public class UserTask implements Serializable {
    @Id
    private String id;
    private LaborTaskType taskType;
    private TaskStatus taskStatus;
    private String description;
    private String startTime;
    private long estimate;
    private String organId;
    private String userComplete;
    private String endTime;
    private long actualTime;
    private String descriptionComplete;
    private String workOrderId;

    public UserTask(String id, LaborTaskType taskType, String description, String startTime, long estimate, String userComplete,String workOrderId, TaskStatus taskStatus,String organId) {
        this.id = id;
        this.taskType = taskType;
        this.description = description;
        this.startTime = startTime;
        this.estimate = estimate;
        this.userComplete = userComplete;
        this.workOrderId=workOrderId;
        this.taskStatus=taskStatus;
        this.organId=organId;
    }

}
