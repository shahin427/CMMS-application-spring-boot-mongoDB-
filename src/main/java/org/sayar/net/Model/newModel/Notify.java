package org.sayar.net.Model.newModel;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Enum.NotifyEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;


@Data
public class Notify {
    @Id
    private String id;
    @DBRef
    private User user;
    private String userId;
    private List<NotifyEvent> events;
    private String referenceId;
}
