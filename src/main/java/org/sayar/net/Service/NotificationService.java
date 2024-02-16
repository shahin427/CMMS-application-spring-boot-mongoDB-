package org.sayar.net.Service;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Enum.WorkOrderStatusEnum;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.websocket.server.PathParam;
import java.util.List;

public interface NotificationService extends GeneralService<Notification> {

    void makeIdAndCreationDate(Notification notification);

    boolean updateNotification(Notification notification);

    Notification getOneNotification(String notificationId);

    List<NotificationDTO> getAllRecipientUserInformation(String userId);

    Page<NotificationDTO> getAllSenderUserInformation(Pageable pageable, SenderDTO senderDTO, String userId, boolean sort);

    List<Notification> getAllCreationDateOfNotification();

    Notification getCreationDate();

    NotificationSenderAndReceiverDTO getOneNotificationSenderAndReceiverInformation(String notificationId);

    Page<NotificationDeleteDTO> getAllDeletedNotification(String userId, DeletedNotificationDTO deletedNotificationDTO, Pageable pageable, boolean sort);

    void createNotificationForWorkOrderAssignedUser(String userAssignedId, String name, String code, String userName, boolean status);

    void createDraftNotificationForWorkOrderAssignedUser(User user, String name, String code, String name1, WorkOrderStatusEnum status);

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

    Page<Notification> getAllSystemMessagesOfTheUser(SystemMessageDTO systemMessageDTO, Pageable pageable, String userId, boolean sort);

    Page<ReceiverNotificationDTO> getAllPrivateMessagesOfTheUser(Pageable pageable, SenderInfoDTO senderInfoDTO, String userId, boolean sort);

    NotificationSenderAndReceiverDTO getOneSystemAndReceiverInformation(String notificationId);

    void sendNotificationToTheTechnician(String workOrderCode, String assetCode, String name, String username, String userId);

    boolean deleteNotification(String notificationId, String senderOrReceiver);

    boolean removeDeletedNotification(String notificationId);

    GetOneDeletedNotificationDTO getOneDeletedNotification(String notificationId);
}
