package org.sayar.net.Model.newModel.Task.model;

import lombok.Data;
import org.sayar.net.Enumes.LaborTaskType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

//import javax.persistence.*;

//@Entity
@Document
@Data
public class LaborTask implements Serializable {

    @Id
    private String id;
    private LaborTaskType taskType;
    private String description;
    private String startTime;
    private long estimate;
    private String userComplete;
    private String endTime;
    private long actualTime;
    private String descriptionComplete;
    private List<String> users;
    public LaborTask() {
    }
}
