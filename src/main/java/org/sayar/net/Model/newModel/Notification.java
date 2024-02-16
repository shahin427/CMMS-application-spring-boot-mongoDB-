package org.sayar.net.Model.newModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Enum.SenderReceiver;
import org.springframework.data.annotation.Id;

import javax.swing.text.Document;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {
    @Id
    private String id;
    private Date creationDate;
    private String subject;
    private String message;
    private String abstractMessage;
    private String recipientUserId;
    private String senderUserId;
    private SenderReceiver senderReceiver;
    private String deleted;
    private List<NotificationUpload> notificationUploadList;
}