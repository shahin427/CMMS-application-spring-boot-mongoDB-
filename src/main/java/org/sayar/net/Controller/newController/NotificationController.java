package org.sayar.net.Controller.newController;

import org.sayar.net.Model.DTO.DeletedNotificationDTO;
import org.sayar.net.Model.DTO.SenderDTO;
import org.sayar.net.Model.DTO.SenderInfoDTO;
import org.sayar.net.Model.DTO.SystemMessageDTO;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @PostMapping("save")
    public void createCreationDate(@RequestBody Notification notification) {
        notificationService.makeIdAndCreationDate(notification);
    }

    @PutMapping("update")
    public ResponseEntity<?> updateNotification(@RequestBody Notification notification) {
        return ResponseEntity.ok().body(notificationService.updateNotification(notification));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneNotification(@PathParam("notificationId") String notificationId) {
        return ResponseEntity.ok().body(notificationService.getOneNotification(notificationId));
    }

    @GetMapping("get-all-recipient-user-information")
    public ResponseEntity<?> getAllRecipientUserInformation(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(notificationService.getAllRecipientUserInformation(userId));
    }

    @PostMapping("get-all-sender-user-information")
    public ResponseEntity<?> getAllSenderUserInformation(Pageable pageable,
                                                         @RequestBody SenderDTO senderDTO,
                                                         @PathParam("userId") String userId,
                                                         @PathParam("sort") boolean sort) {
        return ResponseEntity.ok().body(notificationService.getAllSenderUserInformation(pageable, senderDTO, userId, sort));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteNotification(@PathParam("notificationId") String notificationId, @PathParam("senderOrReceiver") String senderOrReceiver) {
        return ResponseEntity.ok().body(notificationService.deleteNotification(notificationId, senderOrReceiver));
    }

    @GetMapping("get-all-notification")
    public ResponseEntity<?> getAllCreationDateOfNotification() {
        return ResponseEntity.ok().body(notificationService.getAllCreationDateOfNotification());
    }

    @GetMapping("get-creation-date")
    public ResponseEntity<?> getCreationDate() {
        return ResponseEntity.ok().body(notificationService.getCreationDate());
    }

    @GetMapping("get-one-sender-and-receiver-information")
    public ResponseEntity<?> getOneNotificationSenderAndReceiverInformation(@PathParam("notificationId") String notificationId) {
        return ResponseEntity.ok().body(notificationService.getOneNotificationSenderAndReceiverInformation(notificationId));
    }

    @PostMapping("get-all-deleted-notification")
    public ResponseEntity<?> getAllDeletedNotification(@PathParam("useId") String userId, @RequestBody DeletedNotificationDTO deletedNotificationDTO, Pageable pageable, @PathParam("sort") boolean sort) {
        return ResponseEntity.ok().body(notificationService.getAllDeletedNotification(userId, deletedNotificationDTO, pageable, sort));
    }

    @PostMapping("get-all-system-messages-of-the-user")
    public ResponseEntity<?> getAllSystemMessagesOfTheUser(@RequestBody SystemMessageDTO systemMessageDTO, Pageable pageable, @PathParam("userId") String userId, @PathParam("sort") boolean sort) {
        return ResponseEntity.ok().body(notificationService.getAllSystemMessagesOfTheUser(systemMessageDTO, pageable, userId, sort));
    }

    @PostMapping("get-all-private-messages-of-the-user")
    public ResponseEntity<?> getAllPrivateMessagesOfTheUser(Pageable pageable, @RequestBody SenderInfoDTO senderInfoDTO, @PathParam("userId") String userId, @PathParam("sort") boolean sort) {
        return ResponseEntity.ok().body(notificationService.getAllPrivateMessagesOfTheUser(pageable, senderInfoDTO, userId, sort));
    }

    @GetMapping("get-one-system-and-receiver-information")
    public ResponseEntity<?> getOneSystemAndReceiverInformation(@PathParam("notificationId") String notificationId) {
        return ResponseEntity.ok().body(notificationService.getOneSystemAndReceiverInformation(notificationId));
    }

    @DeleteMapping("remove-deleted-notification")
    public ResponseEntity<?> removeDeletedNotification(@PathParam("notificationId") String notificationId) {
        return ResponseEntity.ok(notificationService.removeDeletedNotification(notificationId));
    }

    @GetMapping("get-one-deleted-notification")
    public ResponseEntity<?> getOneDeletedNotification(@PathParam("notificationId") String notificationId) {
        return ResponseEntity.ok().body(notificationService.getOneDeletedNotification(notificationId));
    }
}
