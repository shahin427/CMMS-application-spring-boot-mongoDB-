package org.sayar.net.Model.newModel.WorkOrder;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.TaskType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.*;

//@Entity
@Document
@Data
public class WorkOrderTasks {

    @Id
    private String id;
    private String name;
    private int timeEstimatedHour;
    private int timeSpantHours;
    private String description;
    private String taskCompletion;
    private String dateCompleted;
    private String startDate;
    private TaskType type;
    private String assignToUserId;
    private String completedByUserId;

    public WorkOrderTasks() {
    }

}
