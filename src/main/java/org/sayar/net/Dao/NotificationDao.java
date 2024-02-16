package org.sayar.net.Dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Enum.WorkOrderStatusEnum;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationDao {

    void makeIdAndCreationDate(Notification notification);

    UpdateResult updateNotification(Notification notification);

    Notification getOneNotification(String notificationId);

    List<Notification> getAllRecipientUserNotification(String userId);

    List<Notification> getAllSenderUserInformation(Pageable pageable, SenderDTO senderDTO, String userId, boolean sort);

    Notification getCreationDate();

    List<Notification> getAllDeletedNotification(String userId, DeletedNotificationDTO deletedNotificationDTO, Pageable pageable, boolean sort);

    void createNotificationForWorkOrderAssignedUser(String userAssignedId, String name, String code, String userName, boolean status);

    void createDraftNotificationForWorkOrderAssignedUser(User user, String name, String code, WorkOrderStatusEnum status);

    void createNotificationForSpecifiedUsersOfWorkOrder(User user, boolean assetStatus, String assetName, String assetCode, WorkOrder workOrder);

    void createNotificationForSpecifiedUsersOfWorkOrder(User user, String name, WorkOrderStatusEnum status, String code, WorkOrder workOrder);

    void createNotificationForAssetAssignedUserInScheduleMaintenance(User user, Asset asset);

    void createDraftNotificationForAssignedUserOfScheduledAsset(User user, Asset scheduledAsset);

    void createClosedNotificationForAssignedUserOfScheduledAsset(User user, Asset scheduledAsset);

    void createOpenNotificationForParentOfScheduledAsset(User user, Asset scheduledAsset);

    void createDraftNotificationForParentOfScheduledAsset(User user, Asset scheduledAsset);

    void createCloseNotificationForParentOfScheduledAsset(User user, Asset scheduledAsset);

    void createNotificationForAssetAssignedUserInUpdateOfScheduleMaintenance(User user, Asset scheduledAsset);

    void createNotificationForSpecifiedUsersOfScheduleMaintenance(User user, boolean status, String name, String code);

    void createNotificationForSpecifiedUsersOfScheduleMaintenanceInPost(User user, boolean status, String name, String code);

    void createNotificationForSpecifiedUsersOfScheduleMaintenanceInPostForWorkOrderStatusChange(User user, String name, WorkOrderStatusEnum status, String code);

    void createNotificationForSpecifiedUsersOfScheduleMaintenanceInUpdate(User user, String name, WorkOrderStatusEnum status, String code);

    List<Notification> getAllSystemMessagesOfTheUser(SystemMessageDTO systemMessageDTO, Pageable pageable, String userId, boolean sort);

    List<Notification> getAllPrivateMessagesOfTheUser(Pageable pageable, SenderInfoDTO senderInfoDTO, String userId, boolean sort);

    Notification getOneSystemAndReceiverInformation(String notificationId);

    void sendNotificationToTheTechnician(String workOrderCode, String assetCode, String name, String username, String userId);

    long numberOfNotificationList(SenderDTO senderDTO, String userId);

    UpdateResult deleteNotification(String notificationId, String senderOrReceiver);

    long numberOfNotificationListInPrivateMessage(SenderInfoDTO senderInfoDTO, String userId);

    long getNumberOfSystemNotification(SystemMessageDTO systemMessageDTO, String userId);

    long numberOfDeletedNotifications(String userId, DeletedNotificationDTO deletedNotificationDTO);

    DeleteResult removeDeletedNotification(String notificationId);

    GetOneDeletedNotificationDTO getOneDeletedNotification(String notificationId);
}
