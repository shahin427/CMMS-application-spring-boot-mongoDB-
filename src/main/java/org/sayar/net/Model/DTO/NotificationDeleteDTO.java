package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Tools.Print;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class NotificationDeleteDTO {

    private String notificationId;
    private Date creationDate;
    private String subject;
    private String senderUserName;
    private String senderUserFamily;
    private String recipientUserName;
    private String recipientUserFamily;

    public static List<NotificationDeleteDTO> map(List<Notification> notificationList, List<User> senderLists, List<User> recipientLists) {

        List<NotificationDeleteDTO> notificationDeleteDTOList = new ArrayList<>();
        notificationList.forEach(notification -> {
            NotificationDeleteDTO notificationDeleteDTO = new NotificationDeleteDTO();
            notificationDeleteDTO.setNotificationId(notification.getId());
            notificationDeleteDTO.setCreationDate(notification.getCreationDate());
            notificationDeleteDTO.setSubject(notification.getSubject());
            senderLists.forEach(user -> {
                if (user.getId().equals(notification.getSenderUserId())) {
                    notificationDeleteDTO.setSenderUserName(user.getName());
                    notificationDeleteDTO.setSenderUserFamily(user.getFamily());
                    Print.print("dto", notificationDeleteDTO);
                }
            });
            recipientLists.forEach(user -> {
                if (user.getId().equals(notification.getRecipientUserId())) {
                    notificationDeleteDTO.setRecipientUserName(user.getName());
                    notificationDeleteDTO.setRecipientUserFamily(user.getFamily());
                }
            });
            notificationDeleteDTOList.add(notificationDeleteDTO);
        });
        notificationDeleteDTOList.forEach(notificationDeleteDTO -> {
            if (notificationDeleteDTO.getSenderUserName() == null || notificationDeleteDTO.getSenderUserName().equals("")) {
                notificationDeleteDTO.setSenderUserName("حامی");
            }
        });
        return notificationDeleteDTOList;
    }
}
