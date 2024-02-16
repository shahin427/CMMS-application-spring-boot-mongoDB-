package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Tools.Print;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class NotificationDTO {
    private String notificationId;
    private String userId;
    private String userName;
    private String userFamily;
    private Date creationDate;
    private String subject;
    private String abstractMessage;

    public static List<NotificationDTO> map(List<User> userList, List<Notification> notificationList) {
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        notificationList.forEach(notification -> {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setNotificationId(notification.getId());
            notificationDTO.setSubject(notification.getSubject());
            notificationDTO.setCreationDate(notification.getCreationDate());
            userList.forEach(user -> {
                if (user.getId().equals(notification.getSenderUserId())) {
                    notificationDTO.setUserId(user.getId());
                    notificationDTO.setUserName(user.getName());
                    notificationDTO.setUserFamily(user.getFamily());
                }
            });
            notificationDTOList.add(notificationDTO);
        });
        return notificationDTOList;
    }

    public static List<NotificationDTO> map1(List<User> userList, List<Notification> notificationList) {
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        notificationList.forEach(notification -> {
            NotificationDTO notificationDTO = new NotificationDTO();
            notificationDTO.setNotificationId(notification.getId());
            notificationDTO.setSubject(notification.getSubject());
            notificationDTO.setCreationDate(notification.getCreationDate());
            notificationDTO.setAbstractMessage(notification.getAbstractMessage());
            userList.forEach(user -> {
                if (user.getId().equals(notification.getRecipientUserId())) {
                    notificationDTO.setUserId(user.getId());
                    notificationDTO.setUserName(user.getName());
                    notificationDTO.setUserFamily(user.getFamily());
                }
            });
            notificationDTOList.add(notificationDTO);
        });
        return notificationDTOList;
    }
}
