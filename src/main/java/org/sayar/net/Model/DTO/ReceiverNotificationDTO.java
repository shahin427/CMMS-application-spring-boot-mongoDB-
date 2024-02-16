package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Tools.Print;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ReceiverNotificationDTO {
    private Date creationDate;
    private String notificationId;
    private String subject;
    private String message;
    private String abstractMessage;
    private String recipientUserId;
    private String senderName;
    private String senderFamilyName;
    private String senderUserId;

    public static List<ReceiverNotificationDTO> map(List<Notification> notificationList, List<User> senderUserList) {
        Print.print("notificationList",notificationList);
        Print.print("senderUserList",senderUserList);
        List<ReceiverNotificationDTO> receiverNotificationDTOList = new ArrayList<>();
        notificationList.forEach(notification -> {
            ReceiverNotificationDTO receiverNotificationDTO = new ReceiverNotificationDTO();
            receiverNotificationDTO.setNotificationId(notification.getId());
            receiverNotificationDTO.setMessage(notification.getMessage());
            receiverNotificationDTO.setRecipientUserId(notification.getRecipientUserId());
            receiverNotificationDTO.setSubject(notification.getSubject());
            receiverNotificationDTO.setCreationDate(notification.getCreationDate());
            receiverNotificationDTO.setAbstractMessage(notification.getAbstractMessage());
            senderUserList.forEach(user -> {
                if (user.getId().equals(notification.getSenderUserId())) {
                    receiverNotificationDTO.setSenderUserId(user.getId());
                    receiverNotificationDTO.setSenderName(user.getName());
                    receiverNotificationDTO.setSenderFamilyName(user.getFamily());
                }
            });
            receiverNotificationDTOList.add(receiverNotificationDTO);
        });
        Print.print("rrrrrrrrrrrrrrr",receiverNotificationDTOList);
        return receiverNotificationDTOList;
    }
}
