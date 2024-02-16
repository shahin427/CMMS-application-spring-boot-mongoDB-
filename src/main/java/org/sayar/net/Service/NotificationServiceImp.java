package org.sayar.net.Service;

import org.sayar.net.Dao.NotificationDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Enum.WorkOrderStatusEnum;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImp extends GeneralServiceImpl<Notification> implements NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private UserService userService;

    @Override
    public void makeIdAndCreationDate(Notification notification) {
        notificationDao.makeIdAndCreationDate(notification);
    }

    @Override
    public boolean updateNotification(Notification notification) {
        return super.updateResultStatus(notificationDao.updateNotification(notification));
    }

    @Override
    public Notification getOneNotification(String notificationId) {
        return notificationDao.getOneNotification(notificationId);
    }

    @Override
    public List<NotificationDTO> getAllRecipientUserInformation(String userId) {
        List<Notification> notificationList = notificationDao.getAllRecipientUserNotification(userId);
        List<String> stringList = new ArrayList<>();
        notificationList.forEach(notification -> {
            stringList.add(notification.getSenderUserId());
        });
        List<User> userList = userService.getAllRecipientUserInformation(stringList);
        return NotificationDTO.map(userList, notificationList);
    }

    @Override
    public Page<NotificationDTO> getAllSenderUserInformation(Pageable pageable, SenderDTO senderDTO, String userId, boolean sort) {
        List<Notification> notificationList = notificationDao.getAllSenderUserInformation(pageable, senderDTO, userId, sort);
        List<String> stringList = new ArrayList<>();
        notificationList.forEach(notification -> {
            stringList.add(notification.getRecipientUserId());
        });
        List<User> userList = userService.getAllRecipientUserInformation(stringList);
        long number = notificationDao.numberOfNotificationList(senderDTO, userId);
        return new PageImpl<>(
                NotificationDTO.map1(userList, notificationList)
                , pageable
                , number
        );
    }

    @Override
    public List<Notification> getAllCreationDateOfNotification() {
        return userService.getAllCreationDateOfNotification();
    }

    @Override
    public Notification getCreationDate() {
        return notificationDao.getCreationDate();
    }

    @Override
    public NotificationSenderAndReceiverDTO getOneNotificationSenderAndReceiverInformation(String notificationId) {
        Notification notification = notificationDao.getOneNotification(notificationId);
        if (notification != null && notification.getSenderUserId() != null && notification.getRecipientUserId() != null) {
            User sender = userService.getSenderById(notification.getSenderUserId());
            User recipient = userService.getSenderById(notification.getRecipientUserId());
            return NotificationSenderAndReceiverDTO.map(notification, sender, recipient);
        } else
            return null;
    }

    @Override
    public Page<NotificationDeleteDTO> getAllDeletedNotification(String userId, DeletedNotificationDTO deletedNotificationDTO, Pageable pageable, boolean sort) {
        List<Notification> notificationList = notificationDao.getAllDeletedNotification(userId, deletedNotificationDTO, pageable, sort);
        List<String> sendersId = new ArrayList<>();
        List<String> recipientsId = new ArrayList<>();

        notificationList.forEach(notification -> {
            sendersId.add(notification.getSenderUserId());
            recipientsId.add(notification.getRecipientUserId());
        });
        List<User> senderLists = userService.getAllRecipientUserInformation(sendersId);
        List<User> recipientLists = userService.getAllRecipientUserInformation(recipientsId);
        long numberOfDeletedNotifications = notificationDao.numberOfDeletedNotifications(userId, deletedNotificationDTO);
        return new PageImpl<>(
                NotificationDeleteDTO.map(notificationList, senderLists, recipientLists)
                , pageable
                , numberOfDeletedNotifications
        );
    }

    @Override
    public void createNotificationForWorkOrderAssignedUser(String userAssignedId, String name, String code, String userName, boolean status) {
        notificationDao.createNotificationForWorkOrderAssignedUser(userAssignedId, name, code, userName, status);
    }

    @Override
    public void createDraftNotificationForWorkOrderAssignedUser(User user, String name, String code, String name1, WorkOrderStatusEnum status) {
        notificationDao.createDraftNotificationForWorkOrderAssignedUser(user, name, code, status);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfWorkOrder(User user, boolean assetStatus, String assetName, String assetCode, WorkOrder workOrder) {
        notificationDao.createNotificationForSpecifiedUsersOfWorkOrder(user, assetStatus, assetName, assetCode, workOrder);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfWorkOrder(User user, String name, WorkOrderStatusEnum status, String code, WorkOrder workOrder) {
        notificationDao.createNotificationForSpecifiedUsersOfWorkOrder(user, name, status, code, workOrder);
    }

    @Override
    public void createNotificationForAssetAssignedUserInScheduleMaintenance(User user, Asset asset) {
        notificationDao.createNotificationForAssetAssignedUserInScheduleMaintenance(user, asset);
    }

    @Override
    public void createDraftNotificationForAssignedUserOfScheduledAsset(User user, Asset scheduledAsset) {
        notificationDao.createDraftNotificationForAssignedUserOfScheduledAsset(user, scheduledAsset);
    }

    @Override
    public void createClosedNotificationForAssignedUserOfScheduledAsset(User user, Asset scheduledAsset) {
        notificationDao.createClosedNotificationForAssignedUserOfScheduledAsset(user, scheduledAsset);
    }

    @Override
    public void createOpenNotificationForParentOfScheduledAsset(User user, Asset scheduledAsset) {
        notificationDao.createOpenNotificationForParentOfScheduledAsset(user, scheduledAsset);
    }

    @Override
    public void createDraftNotificationForParentOfScheduledAsset(User user, Asset scheduledAsset) {
        notificationDao.createDraftNotificationForParentOfScheduledAsset(user, scheduledAsset);
    }

    @Override
    public void createCloseNotificationForParentOfScheduledAsset(User user, Asset scheduledAsset) {
        notificationDao.createCloseNotificationForParentOfScheduledAsset(user, scheduledAsset);
    }

    @Override
    public void createNotificationForAssetAssignedUserInUpdateOfScheduleMaintenance(User user, Asset scheduledAsset) {
        notificationDao.createNotificationForAssetAssignedUserInUpdateOfScheduleMaintenance(user, scheduledAsset);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfScheduleMaintenance(User user, boolean status, String name, String code) {
        notificationDao.createNotificationForSpecifiedUsersOfScheduleMaintenance(user, status, name, code);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfScheduleMaintenanceInPost(User user, boolean status, String name, String code) {
        notificationDao.createNotificationForSpecifiedUsersOfScheduleMaintenanceInPost(user, status, name, code);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfScheduleMaintenanceInPostForWorkOrderStatusChange(User user, String name, WorkOrderStatusEnum status, String code) {
        notificationDao.createNotificationForSpecifiedUsersOfScheduleMaintenanceInPostForWorkOrderStatusChange(user, name, status, code);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfScheduleMaintenanceInUpdate(User user, String name, WorkOrderStatusEnum status, String code) {
        notificationDao.createNotificationForSpecifiedUsersOfScheduleMaintenanceInUpdate(user, name, status, code);
    }

    @Override
    public Page<Notification> getAllSystemMessagesOfTheUser(SystemMessageDTO systemMessageDTO, Pageable pageable, String userId, boolean sort) {
        List<Notification> notificationList = notificationDao.getAllSystemMessagesOfTheUser(systemMessageDTO, pageable, userId, sort);
        long numberOfNotifications = notificationDao.getNumberOfSystemNotification(systemMessageDTO, userId);
        return new PageImpl<>(
                notificationList
                , pageable
                , numberOfNotifications
        );
    }

    @Override
    public Page<ReceiverNotificationDTO> getAllPrivateMessagesOfTheUser(Pageable pageable, SenderInfoDTO senderInfoDTO, String userId, boolean sort) {
        List<Notification> notificationList = notificationDao.getAllPrivateMessagesOfTheUser(pageable, senderInfoDTO, userId, sort);
        Print.print("notifList", notificationList);
        List<String> stringList = new ArrayList<>();
        notificationList.forEach(notification -> {
            stringList.add(notification.getSenderUserId());
        });
        Print.print("stringList", stringList);
        List<User> userList = userService.getAllRecipientUserInformation(stringList);
        Print.print("userList", userList);
        long number = notificationDao.numberOfNotificationListInPrivateMessage(senderInfoDTO, userId);
        System.out.println(number + "   dncvdubvurgvuyrwg");
        return new PageImpl<>(
                ReceiverNotificationDTO.map(notificationList, userList)
                , pageable
                , number
        );
    }

    @Override
    public NotificationSenderAndReceiverDTO getOneSystemAndReceiverInformation(String notificationId) {
        System.out.println("idididididi" + notificationId);
        Notification notification = notificationDao.getOneSystemAndReceiverInformation(notificationId);
        Print.print("notification", notification);
        User recipient = userService.getSenderById(notification.getRecipientUserId());
        Print.print("rrrrr", recipient);
        return NotificationSenderAndReceiverDTO.systemMap(notification, recipient);
    }

    @Override
    public void sendNotificationToTheTechnician(String workOrderCode, String assetCode, String name, String username, String userId) {
        notificationDao.sendNotificationToTheTechnician(workOrderCode, assetCode, name, username, userId);
    }

    @Override
    public boolean deleteNotification(String notificationId, String senderOrReceiver) {
        return this.updateResultStatus(notificationDao.deleteNotification(notificationId, senderOrReceiver));
    }

    @Override
    public boolean removeDeletedNotification(String notificationId) {
        return this.deleteResultStatus(notificationDao.removeDeletedNotification(notificationId));
    }

    @Override
    public GetOneDeletedNotificationDTO getOneDeletedNotification(String notificationId) {
        return notificationDao.getOneDeletedNotification(notificationId);
    }

}
