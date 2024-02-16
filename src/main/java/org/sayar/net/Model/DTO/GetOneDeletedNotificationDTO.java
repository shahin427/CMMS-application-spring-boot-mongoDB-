package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Document.DocumentFile;

import java.util.Date;

@Data
public class GetOneDeletedNotificationDTO {
    private String id;
    private Date creationDate;
    private String subject;
    private String userNameRecipient;
    private String userFamilyRecipient;
    private String userIdRecipient;
    private String userNameSender;
    private String message;
    private DocumentFile documentFile;
}
