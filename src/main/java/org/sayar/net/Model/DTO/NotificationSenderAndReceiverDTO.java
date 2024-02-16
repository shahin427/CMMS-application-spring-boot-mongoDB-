package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Model.newModel.NotificationUpload;
import org.sayar.net.Tools.Print;

import java.util.Date;
import java.util.List;

@Data
public class NotificationSenderAndReceiverDTO {
    private String id;
    private Date creationDate;
    private String subject;
    private String userNameRecipient;
    private String userFamilyRecipient;
    private String userIdRecipient;
    private String userNameSender;
    private String userFamilySender;
    private String userIdSender;
    private String message;
    private List<NotificationUpload> notificationUploadList;

    public static NotificationSenderAndReceiverDTO map(Notification notification, User sender, User recipient) {
        NotificationSenderAndReceiverDTO notification1 = new NotificationSenderAndReceiverDTO();
        notification1.setId(notification.getId());
        notification1.setCreationDate(notification.getCreationDate());
        notification1.setMessage(notification.getMessage());
        notification1.setSubject(notification.getSubject());
        notification1.setNotificationUploadList(notification.getNotificationUploadList());

        notification1.setUserFamilyRecipient(recipient.getFamily());
        notification1.setUserFamilySender(sender.getFamily());

        notification1.setUserNameRecipient(recipient.getName());
        notification1.setUserNameSender(sender.getName());
        notification1.setUserIdSender(sender.getId());
        notification1.setUserIdRecipient(recipient.getId());
        return notification1;
    }

    public static NotificationSenderAndReceiverDTO systemMap(Notification notification, User recipient) {
        NotificationSenderAndReceiverDTO notificationSenderAndReceiverDTO = new NotificationSenderAndReceiverDTO();
        notificationSenderAndReceiverDTO.setCreationDate(new Date());
        notificationSenderAndReceiverDTO.setMessage(notification.getMessage());
        notificationSenderAndReceiverDTO.setSubject(notification.getSubject());
        notificationSenderAndReceiverDTO.setUserFamilyRecipient(recipient.getFamily());
        notificationSenderAndReceiverDTO.setUserNameRecipient(recipient.getName());
        notificationSenderAndReceiverDTO.setUserIdRecipient(recipient.getId());
        notificationSenderAndReceiverDTO.setUserNameSender("حامی");
        return notificationSenderAndReceiverDTO;
    }
}
