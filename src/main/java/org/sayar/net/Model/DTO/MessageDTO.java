package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class MessageDTO {
    private String id;
    private String userId;
    private boolean emailAllMessages;
    private boolean pushNotificationMessages;
    private boolean draft;
    private boolean closed;
    private boolean pending;
    private boolean open;
    private boolean allAssets;
    private boolean assetsIAmAssignedTo;
    private boolean assetsInTheFacilitiesThatIManage;
}
