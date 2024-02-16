package org.sayar.net.Dao;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Enum.SenderReceiver;
import org.sayar.net.Model.newModel.Enum.WorkOrderStatusEnum;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.sayar.net.Model.newModel.Enum.SenderReceiver.RECEIVER;
import static org.sayar.net.Model.newModel.Enum.SenderReceiver.SENDER;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class NotificationDaoImp implements NotificationDao {
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void makeIdAndCreationDate(Notification notification) {

        List<Notification> notificationList = new ArrayList<>();
        Iterable<String> abstractMessage = Splitter.fixedLength(30).split(notification.getMessage());
        String[] abstractMessageList = Iterables.toArray(abstractMessage, String.class);
        Notification senderNotification = new Notification();
        senderNotification.setCreationDate(new Date());
        senderNotification.setMessage(notification.getMessage());
        senderNotification.setSubject(notification.getSubject());
        senderNotification.setSenderUserId(notification.getSenderUserId());
        senderNotification.setRecipientUserId(notification.getRecipientUserId());
        senderNotification.setNotificationUploadList(notification.getNotificationUploadList());
        senderNotification.setSenderReceiver(SENDER);
        senderNotification.setAbstractMessage(abstractMessageList[0]);

        Notification receiverNotification = new Notification();
        receiverNotification.setCreationDate(new Date());
        receiverNotification.setMessage(notification.getMessage());
        receiverNotification.setSubject(notification.getSubject());
        receiverNotification.setRecipientUserId(notification.getRecipientUserId());
        receiverNotification.setSenderUserId(notification.getSenderUserId());
        senderNotification.setNotificationUploadList(notification.getNotificationUploadList());
        receiverNotification.setSenderReceiver(RECEIVER);
        receiverNotification.setAbstractMessage(abstractMessageList[0]);

        notificationList.add(senderNotification);
        notificationList.add(receiverNotification);
        mongoOperations.insertAll(notificationList);
    }

    @Override
    public UpdateResult updateNotification(Notification notification) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(notification.getId()));
        Update update = new Update();
        update.set("id", notification.getId());
        update.set("creationDate", notification.getCreationDate());
        update.set("subject", notification.getSubject());
        update.set("message", notification.getMessage());
        update.set("recipientUserId", notification.getRecipientUserId());
        update.set("senderUserId", notification.getSenderUserId());
        return mongoOperations.updateFirst(query, update, Notification.class);
    }

    @Override
    public Notification getOneNotification(String notificationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(notificationId));
        return mongoOperations.findOne(query, Notification.class);
    }

    @Override
    public List<Notification> getAllRecipientUserNotification(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("recipientUserId").is(userId));
        return mongoOperations.find(query, Notification.class);
    }

    @Override
    public List<Notification> getAllSenderUserInformation(Pageable pageable, SenderDTO senderDTO, String userId, boolean sort) {
        System.out.println(sort);
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("senderUserId").is(userId);
        criteria.and("senderReceiver").is(SENDER);
        SortOperation sortOperation;
        if (sort) {
            sortOperation = sort(Sort.by(Sort.Direction.ASC, "creationDate"));
        } else {
            sortOperation = sort(Sort.by(Sort.Direction.DESC, "creationDate"));
        }
        if (senderDTO.getReceiverUserId() != null && !senderDTO.getReceiverUserId().equals("")) {
            criteria.and("recipientUserId").is(senderDTO.getReceiverUserId());
        }
        if (senderDTO.getFrom() != null && senderDTO.getTo() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(senderDTO.getFrom())
            );
        } else if (senderDTO.getTo() != null && senderDTO.getFrom() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").lte(senderDTO.getTo())
            );
        } else if (senderDTO.getTo() != null && senderDTO.getFrom() != null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(senderDTO.getFrom()),
                    Criteria.where("creationDate").lte(senderDTO.getTo())
            );
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                sortOperation,
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Notification.class, Notification.class).getMappedResults();
    }

    @Override
    public Notification getCreationDate() {
        Date date = new Date();
        Notification notification = new Notification();
        notification.setCreationDate(date);
        return notification;
    }

    @Override
    public List<Notification> getAllDeletedNotification(String userId, DeletedNotificationDTO deletedNotificationDTO, Pageable pageable, boolean sort) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        SortOperation sortOperation;
        if (sort) {
            sortOperation = sort(Sort.by(Sort.Direction.ASC, "creationDate"));
        } else {
            sortOperation = sort(Sort.by(Sort.Direction.DESC, "creationDate"));
        }
        Criteria criteria = new Criteria();
        criteria.and("deleted").is(true);
        criteria.orOperator(
                Criteria.where("recipientUserId").is(userId)
                , Criteria.where("senderUserId").is(userId)
        );
        if (deletedNotificationDTO.getRecipientUserId() != null && !deletedNotificationDTO.getRecipientUserId().equals("")) {
            criteria.and("recipientUserId").is(deletedNotificationDTO.getRecipientUserId());
        }
        if (deletedNotificationDTO.getSenderUserId() != null && !deletedNotificationDTO.getSenderUserId().equals("")) {
            criteria.and("senderUserId").is(deletedNotificationDTO.getSenderUserId());
        }
        if (deletedNotificationDTO.getSubject() != null && !deletedNotificationDTO.getSubject().equals("")) {
            criteria.and("subject").is(deletedNotificationDTO.getSubject());
        }
        if (deletedNotificationDTO.getFrom() != null && deletedNotificationDTO.getTo() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(deletedNotificationDTO.getFrom())
            );
        } else if (deletedNotificationDTO.getTo() != null && deletedNotificationDTO.getFrom() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").lte(deletedNotificationDTO.getTo())
            );
        } else if (deletedNotificationDTO.getTo() != null && deletedNotificationDTO.getFrom() != null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(deletedNotificationDTO.getFrom()),
                    Criteria.where("creationDate").lte(deletedNotificationDTO.getTo())
            );
        }
        Aggregation aggregation = newAggregation(
                match(criteria)
                , sortOperation
                , skipOperation
                , limitOperation
        );
        return mongoOperations.aggregate(aggregation, Notification.class, Notification.class).getMappedResults();
    }

    @Override
    public void createNotificationForWorkOrderAssignedUser(String userAssignedId, String name, String code, String
            userName, boolean status) {
        String assetStatus = "";
        if (status) {
            assetStatus = "فعال";
        }
        if (!status) {
            assetStatus = "غیرفعال";
        }
        System.out.println("CODE");
        System.out.println(code);
        Notification notification = new Notification();
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(userAssignedId);
        notification.setCreationDate(new Date());
        notification.setSubject("وضعیت دارایی در سفارش کار تخصیص یافته به شما");
        notification.setMessage(userName + " سلام " + "\n" + assetStatus + " در وضعیت" + code + " با کد " + name + "  دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public void createDraftNotificationForWorkOrderAssignedUser(User user, String name, String
            code, WorkOrderStatusEnum status) {

        String workOrderStatus = "";

        if (status == WorkOrderStatusEnum.DRAFT) {
            workOrderStatus = "پیشنویس";
        }
        if (status == WorkOrderStatusEnum.OPENED) {
            workOrderStatus = "باز";
        }
        if (status == WorkOrderStatusEnum.CLOSED) {
            workOrderStatus = "بسته";
        }
        if (status == WorkOrderStatusEnum.PENDING) {
            workOrderStatus = "انتظار";
        }

        Notification notification = new Notification();
        notification.setCreationDate(new Date());
        notification.setRecipientUserId(user.getName());
        notification.setSenderUserId("حامی");
        notification.setSubject("حالت دارایی در سفارش کار تخصیص یافته به شما");
        notification.setMessage(user.getName() + " سلام " + "\n" + workOrderStatus + " در حالت" + code + " با کد " + name + "  دارایی ");
        Print.print(mongoOperations.save(notification));
        mongoOperations.save(notification);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfWorkOrder(User user, boolean assetStatus, String
            assetName, String assetCode, WorkOrder workOrder) {
        String status = "";
        if (assetStatus) {
            status = "فعال";
        }
        if (!assetStatus) {
            status = "غیرفعال";
        }
        System.out.println("CODE");
        System.out.println(assetCode);
        Notification notification = new Notification();
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(user.getName());
        notification.setCreationDate(new Date());
        notification.setSubject("وضعیت دارایی در سفارش کار مرتبط به شما");
        notification.setMessage(user.getName() + " سلام " + "\n" + "قرار دارد." + status + " در حالت " + assetCode + " با کد " + assetName + " دارایی مرتبط به نام " + " قرار گرفته اید. " + workOrder.getCode() + "با کد " + workOrder.getTitle() + " شما در آگاه سازی سفارش کار ");
        mongoOperations.save(notification);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfWorkOrder(User user, String name, WorkOrderStatusEnum
            status, String code, WorkOrder workOrder) {
        String workOrderStatus = "";

        if (status == WorkOrderStatusEnum.DRAFT) {
            workOrderStatus = "پیشنویس";
        }
        if (status == WorkOrderStatusEnum.OPENED) {
            workOrderStatus = "باز";
        }
        if (status == WorkOrderStatusEnum.CLOSED) {
            workOrderStatus = "بسته";
        }
        if (status == WorkOrderStatusEnum.PENDING) {
            workOrderStatus = "انتظار";
        }

        Notification notification = new Notification();
        notification.setCreationDate(new Date());
        notification.setRecipientUserId(user.getName());
        notification.setSenderUserId("حامی");
        notification.setSubject("وضعیت دارایی در سفارش کار مرتبط به شما");
        notification.setMessage(user.getName() + " سلام " + "\n" + "قرار دارد." + status + " در وضعیت " + code + " با کد " + name + " دارایی مرتبط به نام " + " قرار گرفته اید. " + workOrder.getCode() + "با کد " + workOrder.getTitle() + " شما در آگاه سازی سفارش کار ");
        Print.print("save2222", mongoOperations.save(notification));
        mongoOperations.save(notification);
    }

    @Override
    public void createNotificationForAssetAssignedUserInScheduleMaintenance(User user, Asset asset) {
        Notification notification = new Notification();
        notification.setSubject("زمانبندی جهت تعمیرات و نگهداری");
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(user.getId());
        notification.setCreationDate(new Date());
        notification.setMessage(user.getName() + " سلام " + "\n" + " زمانبندی تعمیرات و نگهداری با حالت باز (برای سفارش کار) تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public void createDraftNotificationForAssignedUserOfScheduledAsset(User user, Asset scheduledAsset) {
        Notification notification = new Notification();
        notification.setSubject("زمانبندی جهت تعمیرات و نگهداری");
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(user.getId());
        notification.setCreationDate(new Date());
        notification.setMessage(user.getName() + " سلام " + "\n" + " زمانبندی تعمیرات و نگهداری با حالت پیشنویس (برای سفارش کار) تخصیص یافت " + scheduledAsset.getCode() + " با کد " + scheduledAsset.getName() + " برای دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public void createClosedNotificationForAssignedUserOfScheduledAsset(User user, Asset scheduledAsset) {
        Notification notification = new Notification();
        notification.setSubject("زمانبندی جهت تعمیرات و نگهداری");
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(user.getId());
        notification.setCreationDate(new Date());
        notification.setMessage(user.getName() + " سلام " + "\n" + " زمانبندی تعمیرات و نگهداری با حالت بسته (برای سفارش کار) تخصیص یافت " + scheduledAsset.getCode() + " با کد " + scheduledAsset.getName() + " برای دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public void createOpenNotificationForParentOfScheduledAsset(User user, Asset scheduledAsset) {
        Notification notification = new Notification();
        notification.setSubject("زمانبندی جهت تعمیرات و نگهداری");
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(user.getId());
        notification.setCreationDate(new Date());
        notification.setMessage(user.getName() + " سلام " + "\n" + " زمانبندی تعمیرات و نگهداری با حالت باز (برای سفارش کار) تخصیص یافت " + scheduledAsset.getCode() + " با کد " + scheduledAsset.getName() + " برای دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public void createDraftNotificationForParentOfScheduledAsset(User user, Asset scheduledAsset) {
        Notification notification = new Notification();
        notification.setSubject("زمانبندی جهت تعمیرات و نگهداری");
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(user.getId());
        notification.setCreationDate(new Date());
        notification.setMessage(user.getName() + " سلام " + "\n" + " زمانبندی تعمیرات و نگهداری با حالت پیشنویس (برای سفارش کار) تخصیص یافت " + scheduledAsset.getCode() + " با کد " + scheduledAsset.getName() + " برای دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public void createCloseNotificationForParentOfScheduledAsset(User user, Asset scheduledAsset) {
        Notification notification = new Notification();
        notification.setSubject("زمانبندی جهت تعمیرات و نگهداری");
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(user.getId());
        notification.setCreationDate(new Date());
        notification.setMessage(user.getName() + " سلام " + "\n" + " زمانبندی تعمیرات و نگهداری با حالت بسته (برای سفارش کار) تخصیص یافت " + scheduledAsset.getCode() + " با کد " + scheduledAsset.getName() + " برای دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public void createNotificationForAssetAssignedUserInUpdateOfScheduleMaintenance(User user, Asset scheduledAsset) {
        Notification notification = new Notification();
        notification.setSubject("زمانبندی جهت تعمیرات و نگهداری");
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(user.getId());
        notification.setCreationDate(new Date());
        notification.setMessage(user.getName() + " سلام " + "\n" + " به حالت باز (برای سفارش کار) تغییر یافت " + scheduledAsset.getCode() + " با کد " + scheduledAsset.getName() + " زمانبندی تعمیرات و نگهدار برای دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfScheduleMaintenance(User user, boolean status, String name, String code) {
        String status1 = "";
        if (status) {
            status1 = "فعال";
        }
        if (!status) {
            status1 = "غیرفعال";
        }
        Notification notification = new Notification();
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(user.getName());
        notification.setCreationDate(new Date());
        notification.setSubject(" وضعیت دارایی در هنگام تخصیص زمانبندی تعمیرات برنامه ریزی شده ");
        notification.setMessage(user.getName() + " سلام " + "\n" + " قرار دارد " + status1 + " در وضعیت " + code + " با کد " + name + "  دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfScheduleMaintenanceInPost(User user, boolean status, String
            name, String code) {
        String status1 = "";
        if (status) {
            status1 = "فعال";
        }
        if (!status) {
            status1 = "غیرفعال";
        }
        System.out.println("CODE");
        System.out.println(code);
        Notification notification = new Notification();
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(user.getName());
        notification.setCreationDate(new Date());
        notification.setSubject(" وضعیت دارایی در هنگام تخصیص زمانبندی تعمیرات برنامه ریزی شده ");
        notification.setMessage(user.getName() + " سلام " + "\n" + " قرار دارد " + status1 + " در وضعیت " + code + " با کد " + name + "  دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfScheduleMaintenanceInPostForWorkOrderStatusChange(User
                                                                                                               user, String name, WorkOrderStatusEnum status, String code) {
        String workOrderStatus = "";

        if (status == WorkOrderStatusEnum.DRAFT) {
            workOrderStatus = "پیشنویس";
        }
        if (status == WorkOrderStatusEnum.OPENED) {
            workOrderStatus = "باز";
        }
        if (status == WorkOrderStatusEnum.CLOSED) {
            workOrderStatus = "بسته";
        }
        if (status == WorkOrderStatusEnum.PENDING) {
            workOrderStatus = "انتظار";
        }

        Notification notification = new Notification();
        notification.setCreationDate(new Date());
        notification.setRecipientUserId(user.getName());
        notification.setSenderUserId("حامی");
        notification.setSubject("حالت دارایی در هنگام تخصیص زمانبندی تعمیرات برنامه ریزی شده");
        notification.setMessage(user.getName() + " سلام " + "\n" + " قرار دارد " + workOrderStatus + " در حالت " + code + " با کد " + name + "  دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public void createNotificationForSpecifiedUsersOfScheduleMaintenanceInUpdate(User user, String name, WorkOrderStatusEnum status, String code) {
        String workOrderStatus = "";

        if (status == WorkOrderStatusEnum.DRAFT) {
            workOrderStatus = "پیشنویس";
        }
        if (status == WorkOrderStatusEnum.OPENED) {
            workOrderStatus = "باز";
        }
        if (status == WorkOrderStatusEnum.CLOSED) {
            workOrderStatus = "بسته";
        }
        if (status == WorkOrderStatusEnum.PENDING) {
            workOrderStatus = "انتظار";
        }

        Notification notification = new Notification();
        notification.setCreationDate(new Date());
        notification.setRecipientUserId(user.getName());
        notification.setSenderUserId("حامی");
        notification.setSubject("حالت دارایی در هنگام تخصیص زمانبندی تعمیرات برنامه ریزی شده");
        notification.setMessage(user.getName() + " سلام " + "\n" + " قرار دارد " + workOrderStatus + " در حالت " + code + " با کد " + name + "  دارایی ");
        mongoOperations.save(notification);
    }

    @Override
    public List<Notification> getAllSystemMessagesOfTheUser(SystemMessageDTO systemMessageDTO, Pageable pageable, String userId, boolean sort) {
        Print.print("systemMessageDTO", systemMessageDTO);
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("recipientUserId").is(userId);
        criteria.and("senderUserId").is("حامی");
        SortOperation sortOperation;
        if (sort) {
            sortOperation = sort(Sort.by(Sort.Direction.ASC, "creationDate"));
        } else {
            sortOperation = sort(Sort.by(Sort.Direction.DESC, "creationDate"));
        }

        if (systemMessageDTO.getFrom() != null && systemMessageDTO.getTo() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(systemMessageDTO.getFrom())
            );
        } else if (systemMessageDTO.getTo() != null && systemMessageDTO.getFrom() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").lte(systemMessageDTO.getTo())
            );
        } else if (systemMessageDTO.getTo() != null && systemMessageDTO.getFrom() != null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(systemMessageDTO.getFrom()),
                    Criteria.where("creationDate").lte(systemMessageDTO.getTo())
            );
        }
        Aggregation aggregation = newAggregation(
                match(criteria)
                , sortOperation
                , skipOperation
                , limitOperation
        );
        return mongoOperations.aggregate(aggregation, Notification.class, Notification.class).getMappedResults();
    }

    @Override
    public List<Notification> getAllPrivateMessagesOfTheUser(Pageable pageable, SenderInfoDTO senderInfoDTO, String userId, boolean sort) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("recipientUserId").is(userId);
        criteria.and("senderReceiver").is(RECEIVER);
        SortOperation sortOperation;
        if (sort) {
            sortOperation = sort(Sort.by(Sort.Direction.ASC, "creationDate"));
        } else {
            sortOperation = sort(Sort.by(Sort.Direction.DESC, "creationDate"));
        }
        if (senderInfoDTO.getSenderUserId() != null && !senderInfoDTO.getSenderUserId().equals("")) {
            criteria.and("senderUserId").is(senderInfoDTO.getSenderUserId());
        }
        if (senderInfoDTO.getTo() == null && senderInfoDTO.getFrom() != null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(senderInfoDTO.getFrom())
            );
        } else if (senderInfoDTO.getTo() != null && senderInfoDTO.getFrom() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").lte(senderInfoDTO.getTo())
            );
        } else if (senderInfoDTO.getTo() != null && senderInfoDTO.getFrom() != null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(senderInfoDTO.getFrom()),
                    Criteria.where("creationDate").lte(senderInfoDTO.getTo())
            );
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                sortOperation,
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, Notification.class, Notification.class).getMappedResults();
    }

    @Override
    public Notification getOneSystemAndReceiverInformation(String notificationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(notificationId));
        return mongoOperations.findOne(query, Notification.class);
    }

    @Override
    public void sendNotificationToTheTechnician(String workOrderCode, String assetCode, String name, String username, String userId) {
        Notification notification = new Notification();
        notification.setCreationDate(new Date());
        notification.setSenderUserId("حامی");
        notification.setRecipientUserId(userId);
        notification.setSubject("تخصیص کار");
        notification.setSubject(" برای دارایی " + name + " با کد " + assetCode + " سفارش کار تخصیص یافته که انجام آن به شما محول گردیده است برای مشاهده جزئیات این سفارش کار به کارتابل خود مراجعه نمایید " + username + " سلام ");
        mongoOperations.save(notification);
    }

    @Override
    public long numberOfNotificationList(SenderDTO senderDTO, String userId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("senderUserId").is(userId);
        criteria.and("senderReceiver").is(SENDER);

        if (senderDTO.getReceiverUserId() != null && !senderDTO.getReceiverUserId().equals("")) {
            criteria.and("recipientUserId").is(senderDTO.getReceiverUserId());
        }
        if (senderDTO.getFrom() != null && senderDTO.getTo() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(senderDTO.getFrom())
            );
        } else if (senderDTO.getTo() != null && senderDTO.getFrom() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").lte(senderDTO.getTo())
            );
        } else if (senderDTO.getTo() != null && senderDTO.getFrom() != null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(senderDTO.getFrom()),
                    Criteria.where("creationDate").lte(senderDTO.getTo())
            );
        }
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Notification.class, Notification.class).getMappedResults().size();
    }

    @Override
    public UpdateResult deleteNotification(String notificationId, String senderOrReceiver) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(notificationId));
        query.addCriteria(Criteria.where("senderReceiver").is(senderOrReceiver));
        Update update = new Update();
        update.set("deleted", true);
        return mongoOperations.updateFirst(query, update, Notification.class);
    }

    @Override
    public long numberOfNotificationListInPrivateMessage(SenderInfoDTO senderInfoDTO, String userId) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("recipientUserId").is(userId);
        criteria.and("senderReceiver").is(RECEIVER);

        if (senderInfoDTO.getSenderUserId() != null && !senderInfoDTO.getSenderUserId().equals("")) {
            criteria.and("senderUserId").is(senderInfoDTO.getSenderUserId());
        }
        if (senderInfoDTO.getTo() == null && senderInfoDTO.getFrom() != null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(senderInfoDTO.getFrom())
            );
        } else if (senderInfoDTO.getTo() != null && senderInfoDTO.getFrom() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").lte(senderInfoDTO.getTo())
            );
        } else if (senderInfoDTO.getTo() != null && senderInfoDTO.getFrom() != null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(senderInfoDTO.getFrom()),
                    Criteria.where("creationDate").lte(senderInfoDTO.getTo())
            );
        }

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Notification.class, Notification.class).getMappedResults().size();
    }

    @Override
    public long getNumberOfSystemNotification(SystemMessageDTO systemMessageDTO, String userId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("recipientUserId").is(userId);
        criteria.and("senderUserId").is("حامی");

        if (systemMessageDTO.getFrom() != null && systemMessageDTO.getTo() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(systemMessageDTO.getFrom())
            );
        } else if (systemMessageDTO.getTo() != null && systemMessageDTO.getFrom() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").lte(systemMessageDTO.getTo())
            );
        } else if (systemMessageDTO.getTo() != null && systemMessageDTO.getFrom() != null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(systemMessageDTO.getFrom()),
                    Criteria.where("creationDate").lte(systemMessageDTO.getTo())
            );
        }
        Aggregation aggregation = newAggregation(
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Notification.class, Notification.class).getMappedResults().size();
    }

    @Override
    public long numberOfDeletedNotifications(String userId, DeletedNotificationDTO deletedNotificationDTO) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").is(true);
        criteria.orOperator(
                Criteria.where("recipientUserId").is(userId)
                , Criteria.where("senderUserId").is(userId)
        );
        if (deletedNotificationDTO.getRecipientUserId() != null && !deletedNotificationDTO.getRecipientUserId().equals("")) {
            criteria.and("recipientUserId").is(deletedNotificationDTO.getRecipientUserId());
        }
        if (deletedNotificationDTO.getSenderUserId() != null && !deletedNotificationDTO.getSenderUserId().equals("")) {
            criteria.and("senderUserId").is(deletedNotificationDTO.getSenderUserId());
        }
        if (deletedNotificationDTO.getSubject() != null && !deletedNotificationDTO.getSubject().equals("")) {
            criteria.and("subject").is(deletedNotificationDTO.getSubject());
        }
        if (deletedNotificationDTO.getFrom() != null && deletedNotificationDTO.getTo() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(deletedNotificationDTO.getFrom())
            );
        } else if (deletedNotificationDTO.getTo() != null && deletedNotificationDTO.getFrom() == null) {
            criteria.andOperator(
                    Criteria.where("creationDate").lte(deletedNotificationDTO.getTo())
            );
        } else if (deletedNotificationDTO.getTo() != null && deletedNotificationDTO.getFrom() != null) {
            criteria.andOperator(
                    Criteria.where("creationDate").gte(deletedNotificationDTO.getFrom()),
                    Criteria.where("creationDate").lte(deletedNotificationDTO.getTo())
            );
        }
        Aggregation aggregation = newAggregation(
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, Notification.class, Notification.class).getMappedResults().size();
    }

    @Override
    public DeleteResult removeDeletedNotification(String notificationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(notificationId));
        return mongoOperations.remove(query, Notification.class);
    }

    @Override
    public GetOneDeletedNotificationDTO getOneDeletedNotification(String notificationId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").is(true);
        criteria.and("id").is(notificationId);
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("creationDate").as("creationDate")
                        .and("subject").as("subject")
                        .and("message").as("message")
                        .and("senderUserId").as("sender")
                        .and("documentFile").as("documentFile")
                        .and(ConvertOperators.ToObjectId.toObjectId("$recipientUserId")).as("recipientUserId"),
                lookup("user", "recipientUserId", "_id", "recipient"),
                unwind("recipient", true),
                project()
                        .and("id").as("id")
                        .and("creationDate").as("creationDate")
                        .and("subject").as("subject")
                        .and("message").as("message")
                        .and("sender").as("userNameSender")
                        .and("recipient.name").as("userNameRecipient")
                        .and("recipient.family").as("userFamilyRecipient")
                        .and("documentFile").as("documentFile")
                        .and("recipient._id").as("userIdRecipient")
        );
        Print.print("AAA", mongoOperations.aggregate(aggregation, Notification.class, Object.class).getUniqueMappedResult());
        return mongoOperations.aggregate(aggregation, Notification.class, GetOneDeletedNotificationDTO.class).getUniqueMappedResult();
    }
}
