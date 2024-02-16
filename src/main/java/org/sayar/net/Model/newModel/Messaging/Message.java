package org.sayar.net.Model.newModel.Messaging;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Message {
    @Id
    private String id;
    private String userId;
    private boolean emailAllMessages;
    private boolean pushNotificationMessages;
    private boolean draft;
    private boolean pending;
    private boolean closed;
    private boolean open;
    private boolean allAssets;
    private boolean assetsIAmAssignedTo;
    private boolean assetsInTheFacilitiesThatIManage;
}
