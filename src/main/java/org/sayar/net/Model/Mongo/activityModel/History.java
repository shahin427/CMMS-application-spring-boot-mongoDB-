package org.sayar.net.Model.Mongo.activityModel;

import lombok.Data;
import org.sayar.net.Model.DTO.UserDetailDTO;
import org.sayar.net.Model.Mongo.MyModel.ActivityLevel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document
@Data
public class History implements Serializable {
    @Id
    private String id;
    private String  senderId;
    private ActivityLevel receiver;
    private ActionType actionType;
    private String description;
    private String formDataId;
    private UserDetailDTO sender;
    private Date creationDate;


    public History() {
    }


    public History(String id, ActivityLevel receiver, ActionType actionType, String description, Date creationDate) {
        this.id = id;
        this.receiver = receiver;
        this.actionType = actionType;
        this.description = description;
        this.creationDate = creationDate;
    }
}
