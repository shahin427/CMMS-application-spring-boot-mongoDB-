package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service;

import org.sayar.net.Dao.NewDao.asset.AssetDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Project;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.*;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.dao.ScheduleMaintananceDao;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;
import org.sayar.net.Service.Mongo.activityServices.activity.ActivityService;
import org.sayar.net.Service.UnitOfMeasurementService;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.newService.*;
import org.sayar.net.Tools.Print;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service

public class ScheduleMaintananceServiceImpl extends GeneralServiceImpl<ScheduleMaintenance> implements ScheduleMaintananceService {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ScheduleMaintenanceBackupService scheduleMaintenanceBackupService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ScheduleMaintananceDao dao;

    @Autowired
    private AssetDao assetDao;

    @Autowired
    private UnitOfMeasurementService unitOfMeasurementService;

    @Autowired
    private WorkOrderStatusService workOrderStatusService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskGroupService taskGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private ScheduleMaintenanceCalenderController scheduleMaintenanceCalenderController;


    @Override
    public ScheduleMaintenance postScheduleMaintenance(ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO) {

        ScheduleMaintenance scheduleMaintenance = dao.postScheduleMaintenance(scheduleMaintenanceCreateDTO);
        scheduleMaintenanceBackupService.createScheduleMaintenanceBackUpByScheduleMaintenance(scheduleMaintenance);
//
//        /**
//         *new version notification part
//         */
//        Asset assetOfThisWorkOrder = assetDao.getAssetOfScheduleMaintenanceByAssetId(scheduleMaintenance.getAssetId());
//        Print.print("assetOfThisWorkOrder", assetOfThisWorkOrder);
//        List<User> personnelOfTheAsset = new ArrayList<>();
//        List<String> ancestorsIdListOfThisWorkOrderUsers = new ArrayList<>();
//        if (assetOfThisWorkOrder.getUsers() != null) {
//            personnelOfTheAsset = userService.getAllPersonnelOfAsset(assetOfThisWorkOrder.getUsers());
//            Print.print("personnelOfTheAsset", personnelOfTheAsset);
//            personnelOfTheAsset.forEach(user -> {
//                Print.print("userrr", user);
//                getAncestorOfEachUser(user, ancestorsIdListOfThisWorkOrderUsers);
//            });
//            Print.print("ancestorsIdListOfThisWorkOrderUsers", ancestorsIdListOfThisWorkOrderUsers);
//        }
//        List<String> parenAssetsId = new ArrayList<>();
//        getAllRelatedAssetToSendNotification(assetOfThisWorkOrder, parenAssetsId);
//        Print.print("parenAssetsId", parenAssetsId);
//        List<Asset> parentAssets = assetDao.getAssetByAssetIdList(parenAssetsId);
//        Print.print("parentAssets", parentAssets);
//        List<String> parentAssetsPersonnel = new ArrayList<>();
//        parentAssets.forEach(asset -> {
//            parentAssetsPersonnel.addAll(asset.getUsers());
//        });
//        Print.print("parentAssetsPersonnel", parentAssetsPersonnel);
//        List<String> usersListForSendingNotification = new ArrayList<>();
//        usersListForSendingNotification.addAll(parentAssetsPersonnel);
//        usersListForSendingNotification.addAll(ancestorsIdListOfThisWorkOrderUsers);
//        Print.print("usersListForSendingNotification", usersListForSendingNotification);
//        List<User> userListForSendingNotification = userService.getUserListForSendingNotification(usersListForSendingNotification);
//        List<Notification> notificationList = new ArrayList<>();
//        if (assetOfThisWorkOrder.getUsers() != null) {
//            List<Message> personnelOfAssetMessageList = messageService.getUsersMessageByUserIdList(assetOfThisWorkOrder.getUsers());
//            personnelOfTheAsset.forEach(user -> {
//                personnelOfAssetMessageList.forEach(message -> {
//                    if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                            && message.isAssetsIAmAssignedTo()) {
//                        //send notification to personnel of the asset of scheduleMaintenance
//                        Notification notification1 = new Notification();
//                        notification1.setRecipientUserId(user.getId());
//                        notification1.setCreationDate(new Date());
//                        notification1.setSubject("ایجاد نت برنامه ریزی شده در دارایی تخصیص یافته به شما");
//                        notification1.setMessage(user.getName() + " سلام " + "\n" + " ایجاد شد " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " برای دارایی " + scheduleMaintenance.getCode() + "با شماره کد " + scheduleMaintenance.getTitle() + " نت برنامه ریزی شده با عنوان ");
//                        notification1.setSenderUserId("حامی");
//                        notificationList.add(notification1);
//                        Print.print("notif1", notification1);
//                    }
//                });
//            });
//        }
//        if (userListForSendingNotification != null) {
//            List<Message> workOrdersUsersMessageList2 = messageService.getUsersMessageByUserIdList(usersListForSendingNotification);
//            if (workOrdersUsersMessageList2 != null) {
//                userListForSendingNotification.forEach(user -> {
//                    workOrdersUsersMessageList2.forEach(message -> {
//                        if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                                && message.isAssetsInTheFacilitiesThatIManage()) {
//                            //send notification to assets' personnel's parent &  assets' parents' personnel of scheduleMaintenance
//                            Notification notification1 = new Notification();
//                            notification1.setRecipientUserId(user.getId());
//                            notification1.setCreationDate(new Date());
//                            notification1.setSubject("ایجاد نت برنامه ریزی شده در دارایی تخصیص یافته به زیر مجموعه شما");
//                            notification1.setMessage(user.getName() + " سلام " + "\n" + " ایجاد شد " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " برای دارایی " + scheduleMaintenance.getCode() + "با شماره کد " + scheduleMaintenance.getTitle() + " نت برنامه ریزی شده با عنوان ");
//                            notification1.setSenderUserId("حامی");
//                            notificationList.add(notification1);
//                            Print.print("notif2", notification1);
//                        }
//                    });
//                });
//            }
//            mongoOperations.insertAll(notificationList);
//        }
        return scheduleMaintenance;


//            /**
//             *this is old version notification
//             */
//            Asset scheduledAsset = assetDao.getOneAsset(scheduleMaintenance.getAssetId());
//            if (scheduledAsset != null && scheduledAsset.getUsers() != null) {
//                List<User> userListOfScheduledAsset = userService.getAllPersonnelOfAsset(scheduledAsset.getUsers());
//                WorkOrderStatus workOrderStatusOfTheScheduleMaintenance = workOrderStatusService.getWorkOrderStatusById(scheduleMaintenance.getStatusId());
//                List<String> userIdListForSendingNotification = new ArrayList<>();
//                List<String> assetIdListForSendingNotification = new ArrayList<>();
//                if (scheduledAsset.getUsers() != null) {
//                    userListOfScheduledAsset.forEach(user -> {
//                        getAllRelatedUserToSendNotification(user, userIdListForSendingNotification);
//                    });
//                }
//                getAllRelatedAssetToSendNotification(scheduledAsset, assetIdListForSendingNotification);
//                List<Asset> assetList = assetDao.getAssetByAssetIdList(assetIdListForSendingNotification);
//                assetList.forEach(asset1 -> {
//                    if (asset1.getUsers() != null && !asset1.isDeleted()) {
//                        List<User> personnelOfEachAsset = userService.getAllPersonnelOfAsset(asset1.getUsers());
//                        personnelOfEachAsset.forEach(user -> {
//                            if (!user.isDeleted()) {
//                                userIdListForSendingNotification.add(user.getId());
//                                Print.print("finalUserList", userIdListForSendingNotification);
//                            }
//                        });
//                    }
//                });
//                /**
//                 this is for the personnel of the asset that has workOrder
//                 */
//                List<String> messageIdList = new ArrayList<>();
//                Print.print("1111", messageIdList);
//                userListOfScheduledAsset.forEach(user -> {
//                    messageIdList.add(user.getMessageId());
//                });
//                Print.print("2222", userListOfScheduledAsset);
//                if (messageIdList != null) {
//                    List<Message> messageList = messageService.getAllUsersMessage(messageIdList);
//                    Print.print("3333", messageList);
//                    if (workOrderStatusOfTheScheduleMaintenance != null) {
//                        if (workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                            userListOfScheduledAsset.forEach(user -> {
//                                messageList.forEach(message -> {
//                                    if (message != null && message.getId().equals(user.getMessageId())
//                                            && message.isOpen()
//                                            && message.isPushNotificationMessages()
//                                            && message.isAssetsIAmAssignedTo()) {
//                                        notificationService.createNotificationForAssetAssignedUserInUpdateOfScheduleMaintenance(user, scheduledAsset);
//                                    }
//                                });
//                            });
//                        }
//
//                        if (workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                            userListOfScheduledAsset.forEach(user -> {
//                                messageList.forEach(message -> {
//                                    if (message != null && message.getId().equals(user.getMessageId())
//                                            && message.isDraft()
//                                            && message.isPushNotificationMessages()
//                                            && message.isAssetsIAmAssignedTo()) {
//                                        notificationService.createDraftNotificationForAssignedUserOfScheduledAsset(user, scheduledAsset);
//                                    }
//                                });
//                            });
//                        }
//
//                        if (workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDCOMPLETE)
//                                || workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDINCOMPLETE)) {
//                            userListOfScheduledAsset.forEach(user -> {
//                                messageList.forEach(message -> {
//                                    if (message != null
//                                            && message.getId().equals(user.getMessageId())
//                                            && message.isClosed()
//                                            && message.isPushNotificationMessages()
//                                            && message.isAssetsIAmAssignedTo()) {
//                                        notificationService.createClosedNotificationForAssignedUserOfScheduledAsset(user, scheduledAsset);
//                                    }
//                                });
//                            });
//                        }
//
//                        /**
//                         this is for report to and parent building personnel users
//                         */
//
//                        List<Message> messageListOfScheduledAsset = messageService.getUsersMessageByUserIdList(userIdListForSendingNotification);
//                        Print.print("MessageLIst", messageListOfScheduledAsset);
//                        List<User> parentUserListOfScheduledAsset = userService.getAllParentUsersByUserIdList(userIdListForSendingNotification);
//                        Print.print("parentUserList", parentUserListOfScheduledAsset);
//                        if (workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                            Print.print("YYYYY", parentUserListOfScheduledAsset);
//                            if (messageListOfScheduledAsset != null && parentUserListOfScheduledAsset != null) {
//                                for (Message message : messageListOfScheduledAsset) {
//                                    for (User user : parentUserListOfScheduledAsset) {
//                                        if (message != null
//                                                && message.getUserId() != null
//                                                && user.getId().equals(message.getUserId())
//                                                && message.isPushNotificationMessages()
//                                                && message.isOpen()
//                                                && message.isAssetsInTheFacilitiesThatIManage()) {
//                                            System.out.println("entered");
//                                            notificationService.createOpenNotificationForParentOfScheduledAsset(user, scheduledAsset);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        if (workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                            System.out.println("entered draft");
//                            if (messageListOfScheduledAsset != null && parentUserListOfScheduledAsset != null) {
//                                for (Message message : messageListOfScheduledAsset) {
//                                    for (User user : parentUserListOfScheduledAsset) {
//                                        if (message != null
//                                                && message.getUserId() != null
//                                                && user.getId().equals(message.getUserId())
//                                                && message.isPushNotificationMessages()
//                                                && message.isDraft()
//                                                && message.isAssetsInTheFacilitiesThatIManage()) {
//                                            System.out.println("entered");
//                                            notificationService.createDraftNotificationForParentOfScheduledAsset(user, scheduledAsset);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        if (workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDCOMPLETE)
//                                || workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDINCOMPLETE)) {
//                            System.out.println("entered close");
//                            if (messageListOfScheduledAsset != null && parentUserListOfScheduledAsset != null) {
//                                for (Message message : messageListOfScheduledAsset) {
//                                    for (User user : parentUserListOfScheduledAsset) {
//                                        if (message != null
//                                                && message.getUserId() != null
//                                                && user.getId().equals(message.getUserId())
//                                                && message.isPushNotificationMessages()
//                                                && message.isClosed()
//                                                && message.isAssetsInTheFacilitiesThatIManage()) {
//                                            System.out.println("entered");
//                                            notificationService.createCloseNotificationForParentOfScheduledAsset(user, scheduledAsset);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }

    }

    private void getAncestorOfEachUser(User user, List<String> ancestorsIdListOfThisWorkOrderUsers) {
        if (user != null && user.getParentUserId() != null) {
            Print.print("user22", user);
            String parentId = user.getParentUserId();
            ancestorsIdListOfThisWorkOrderUsers.add(parentId);
            User parentUser = userService.getUserParenByParentId(parentId);
            getAncestorOfEachUser(parentUser, ancestorsIdListOfThisWorkOrderUsers);
        }
    }

    private void getAllRelatedAssetToSendNotification(Asset scheduledAsset, List<String> assetIdListForSendingNotification) {
        if (scheduledAsset != null && scheduledAsset.getIsPartOfAsset() != null && !scheduledAsset.getDeleted()) {
            String parentAssetId = scheduledAsset.getIsPartOfAsset();
            if (parentAssetId != null) {
                Asset parentAsset = assetDao.getOneAsset(parentAssetId);
                if (parentAsset != null && !parentAsset.getDeleted()) {
                    assetIdListForSendingNotification.add(parentAsset.getId());
                    getAllRelatedAssetToSendNotification(parentAsset, assetIdListForSendingNotification);
                }
            }
        }
    }

    private void getAllRelatedUserToSendNotification(User user, List<String> relatedUserForSendingNotification) {
        Print.print("related", relatedUserForSendingNotification);
        if (user.getParentUserId() != null && !user.getDeleted()) {
            String parentUserId = user.getParentUserId();
            User parentUser = userService.getOneByUserId(parentUserId);
            relatedUserForSendingNotification.add(parentUserId);
            getAllRelatedUserToSendNotification(parentUser, relatedUserForSendingNotification);
        }
    }

    @Override
    public ScheduleMaintenanceBasicInformation getAllBasicInformation(String scheduleMaintenanceId) {
        ScheduleMaintenance scheduleMaintenance = dao.getAllBasicInformation(scheduleMaintenanceId);
        if (scheduleMaintenance != null) {
            return scheduleMaintenance.getScheduleMaintenanceBasicInformation();
        } else
            return null;
    }

    @Override
    public List<ScheduleMaintenanceAndProjectFilterDTO> getAllScheduleMaintenanceByProjectId(String projectId) {
        List<String> assetIdList = new ArrayList<>();
        List<String> statusIdList = new ArrayList<>();
        List<ScheduleMaintenance> scheduleMaintenanceList = dao.getAllScheduleMaintenanceByProjectId(projectId);
        scheduleMaintenanceList.forEach(scheduleMaintenance -> {
            assetIdList.add(scheduleMaintenance.getAssetId());
            statusIdList.add(scheduleMaintenance.getStatusId());
        });
        List<Asset> assets = assetDao.getAllAssetByAssetIdList(assetIdList);
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getAllWorkOrderStatusByWorkOrderStatusId(statusIdList);
        return ScheduleMaintenanceAndProjectFilterDTO.map(scheduleMaintenanceList, assets, workOrderStatusList);
    }

    @Override
    public ScheduleMaintenanceCompletionDetail getCompletionDetailByScheduleMaintenanceId(String scheduleMaintenanceId) {
        ScheduleMaintenance scheduleMaintenance = dao.ScheduleMaintenanceCompletionDetail(scheduleMaintenanceId);
        if (scheduleMaintenance != null) {
            return scheduleMaintenance.getScheduleMaintenanceCompletionDetail();
        } else
            return null;
    }

    @Override
    public boolean updateBasicInformationByScheduleMaintenanceId(String scheduleMaintenanceId, ScheduleMaintenanceBasicInformation basicInformation) {
        scheduleMaintenanceBackupService.updateScheduleMaintenanceBackUpByBasicInformation(scheduleMaintenanceId, basicInformation);
        return dao.updateBasicInformationByScheduleMaintenanceId(scheduleMaintenanceId, basicInformation);
    }

    @Override
    public boolean updateScheduleMaintenanceCreateByScheduleId(String scheduleMaintenanceId, ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO) {

//        Asset assetOfThisSchedule = assetDao.getOneAsset(scheduleMaintenanceCreateDTO.getAssetId());
//        ScheduleMaintenance oldVersionScheduleMaintenance = dao.getOldVersionOfScheduleMaintenance(scheduleMaintenanceId);
        scheduleMaintenanceBackupService.updateScheduleMaintenanceBackUp(scheduleMaintenanceId, scheduleMaintenanceCreateDTO);
//        if (oldVersionScheduleMaintenance.getScheduleWithTimeAndMetering().getMeteringCycle().getUnitOfMeasurementId() != null) {
//
//                if (assetOfThisSchedule.getUnitIdList() == null ||
//                        !assetOfThisSchedule.getUnitIdList().contains(oldVersionScheduleMaintenance.getScheduleWithTimeAndMetering().getMeteringCycle().getUnitOfMeasurementId())) {
//                    HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
//                    throw new ApiInputIsInComplete("واحد اندازه گیری انتخاب شده در قسمت دوره بازدید کارکردی در دارایی انتخاب شده وجود ندارد لطفا واحد اندازه گیری را تغییر دهید ", httpStatus);
//                }
//        }
        boolean update = super.updateResultStatus(dao.updateScheduleMaintenanceCreateByScheduleId(scheduleMaintenanceId, scheduleMaintenanceCreateDTO));


//        /**
//         * send notification part
//         */
//        Print.print("assetOfThisSchedule", assetOfThisSchedule);
//        List<User> personnelOfTheAsset = new ArrayList<>();
//        if (assetOfThisSchedule.getUsers() != null) {
//            personnelOfTheAsset = userService.getAllPersonnelOfAsset(assetOfThisSchedule.getUsers());
//        }
//        Print.print("personnelOfTheAsset", personnelOfTheAsset);
//        List<String> ancestorsIdListOfThisWorkOrderUsers = new ArrayList<>();
//        personnelOfTheAsset.forEach(user -> {
//            getAncestorOfEachUser(user, ancestorsIdListOfThisWorkOrderUsers);
//        });
//        Print.print("ancestorsIdListOfThisWorkOrderUsers", ancestorsIdListOfThisWorkOrderUsers);
//        List<String> parenAssetsId = new ArrayList<>();
//        getAllRelatedAssetToSendNotification(assetOfThisSchedule, parenAssetsId);
//        Print.print("parenAssetsId", parenAssetsId);
//        List<Asset> parentAssets = assetDao.getAssetByAssetIdList(parenAssetsId);
//        Print.print("parentAssets", parentAssets);
//        List<String> parentAssetsPersonnel = new ArrayList<>();
//        parentAssets.forEach(asset -> {
//            parentAssetsPersonnel.addAll(asset.getUsers());
//        });
//        Print.print("parentAssetsPersonnel", parentAssetsPersonnel);
//        List<Notify> notifyListOfWorkOrder = notifyService.getAllNotifyOfTheWorkOrderByNotifyId(scheduleMaintenanceId);
//        Print.print("notifyListOfWorkOrder", notifyListOfWorkOrder);
//        WorkOrderStatus workOrderStatusOfTheNewVersionWorkOrder = workOrderStatusService.getWorkOrderStatusById(oldVersionScheduleMaintenance.getStatusId());
//        WorkOrderStatus workOrderStatusOfTheOldVersionWorkOrder = workOrderStatusService.getWorkOrderStatusById(scheduleMaintenanceCreateDTO.getStatusId());
//        List<String> usersListForSendingNotification = new ArrayList<>();
//        usersListForSendingNotification.addAll(parentAssetsPersonnel);
//        usersListForSendingNotification.addAll(ancestorsIdListOfThisWorkOrderUsers);
//        Print.print("usersListForSendingNotification", usersListForSendingNotification);
//        List<User> userListForSendingNotification = userService.getUserListForSendingNotification(usersListForSendingNotification);
//        List<Notification> notificationList = new ArrayList<>();
//
//
//        if (!oldVersionScheduleMaintenance.getAssetId().equals(scheduleMaintenanceCreateDTO.getAssetId())) {
//            List<Message> personnelOfAssetMessageList = messageService.getUsersMessageByUserIdList(assetOfThisSchedule.getUsers());
//            personnelOfTheAsset.forEach(user -> {
//                personnelOfAssetMessageList.forEach(message -> {
//                    if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                            && message.isAssetsIAmAssignedTo()) {
//                        //send notification to personnel of the asset (for workOrder status changes)
//                        Notification notification1 = new Notification();
//                        notification1.setRecipientUserId(user.getId());
//                        notification1.setCreationDate(new Date());
//                        notification1.setSubject("نت برنامه ریزی شده برای دارایی تخصیص یافته به شما");
//                        notification1.setMessage(user.getName() + " سلام " + "\n" + " ایجاد شد " + scheduleMaintenanceCreateDTO.getCode() + " نت برنامه ریزی شده با کد " + assetOfThisSchedule.getCode() + " با کد " + assetOfThisSchedule.getName() + " برای دارایی ");
//                        notification1.setSenderUserId("حامی");
//                        notificationList.add(notification1);
//                        Print.print("notif1", notification1);
//                    }
//                });
//            });
//
//            List<Message> workOrdersUsersMessageList2 = messageService.getUsersMessageByUserIdList(usersListForSendingNotification);
//            if (workOrdersUsersMessageList2 != null) {
//                userListForSendingNotification.forEach(user -> {
//                    workOrdersUsersMessageList2.forEach(message -> {
//                        if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                                && message.isAssetsInTheFacilitiesThatIManage()) {
//                            //send notification to assets' personnel's parent &  assets' parents' personnel (for  workOrderStatus changes)
//                            Notification notification1 = new Notification();
//                            notification1.setRecipientUserId(user.getId());
//                            notification1.setCreationDate(new Date());
//                            notification1.setSubject(" نت برنامه ریزی شده برای دارایی تخصیص یافته به پرسنل زیرمجموعه شما ");
//                            notification1.setMessage(user.getName() + " سلام " + "\n" + " ایجاد شد " + scheduleMaintenanceCreateDTO.getCode() + " نت برنامه ریزی شده با کد " + assetOfThisSchedule.getCode() + " با کد " + assetOfThisSchedule.getName() + " برای دارایی ");
//                            notification1.setSenderUserId("حامی");
//                            notificationList.add(notification1);
//                            Print.print("notif2", notification1);
//                        }
//                    });
//                });
//            }
//            if (notifyListOfWorkOrder != null) {
//                notifyListOfWorkOrder.forEach(notify -> {
//                    //send notification to notify list(workOrder status changes)
//                    if (notify.getEvents().contains(ONSTATUSCHANG)) {
//                        Notification notification = new Notification();
//                        notification.setRecipientUserId(notify.getUser().getId());
//                        notification.setCreationDate(new Date());
//                        notification.setSubject("وضعیت دارایی در نت برنامه ریزی شده مرتبط به شما");
//                        notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + " قرار گرفته اید " + assetOfThisSchedule.getCode() + " با کد " + assetOfThisSchedule.getName() + " برای دارایی " + scheduleMaintenanceCreateDTO.getCode() + "با کد " + scheduleMaintenanceCreateDTO.getTitle() + " شما در آگاه سازی نت برنامه ریزی شده ");
//                        notification.setSenderUserId("حامی");
//                        Print.print("notif3", notification);
//                        notificationList.add(notification);
//                    }
//                });
//            }
//        }
//
//        if (oldVersionScheduleMaintenance.getStatusId() != null
//                && scheduleMaintenanceCreateDTO.getStatusId() != null
//                && workOrderStatusOfTheNewVersionWorkOrder != null
//                && !oldVersionScheduleMaintenance.getStatusId().equals(scheduleMaintenanceCreateDTO.getStatusId())) {
//
//            String statusInPersianAfterUpdate = null;
//            if (workOrderStatusOfTheNewVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                statusInPersianAfterUpdate = "باز";
//            }
//            if (workOrderStatusOfTheNewVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.CLOSED)) {
//                statusInPersianAfterUpdate = "بسته";
//            }
//            if (workOrderStatusOfTheNewVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                statusInPersianAfterUpdate = "پیشنویس";
//            }
//            if (workOrderStatusOfTheNewVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.PENDING)) {
//                statusInPersianAfterUpdate = "انتظار";
//            }
//
//            String statusInPersianBeforeUpdates = null;
//            if (workOrderStatusOfTheOldVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                statusInPersianBeforeUpdates = "باز";
//            }
//            if (workOrderStatusOfTheOldVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.CLOSED)) {
//                statusInPersianBeforeUpdates = "بسته";
//            }
//            if (workOrderStatusOfTheOldVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                statusInPersianBeforeUpdates = "پیشنویس";
//            }
//            if (workOrderStatusOfTheOldVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.PENDING)) {
//                statusInPersianBeforeUpdates = "انتظار";
//            }
//            String finalStatusInPersianAfterUpdate = statusInPersianAfterUpdate;
//            String finalStatusInPersianBeforeUpdates = statusInPersianBeforeUpdates;
//
//            List<Message> personnelOfAssetMessageList = messageService.getUsersMessageByUserIdList(assetOfThisSchedule.getUsers());
//            personnelOfTheAsset.forEach(user -> {
//                personnelOfAssetMessageList.forEach(message -> {
//                    if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                            && message.isAssetsIAmAssignedTo()) {
//                        //send notification to personnel of the asset (for workOrder status changes)
//                        Notification notification1 = new Notification();
//                        notification1.setRecipientUserId(user.getId());
//                        notification1.setCreationDate(new Date());
//                        notification1.setSubject("تغییر وضعیت در نت برنامه ریزی شده برای دارایی تخصیص یافته به شما");
//                        notification1.setMessage(user.getName() + " سلام " + "\n" + " تغییر پیدا کرد " + finalStatusInPersianAfterUpdate + "به وضعیت " + finalStatusInPersianBeforeUpdates + " از وضعیت " + scheduleMaintenanceCreateDTO.getCode() + " نت برنامه ریزی شده با کد " + assetOfThisSchedule.getCode() + " با کد " + assetOfThisSchedule.getName() + " برای دارایی ");
//                        notification1.setSenderUserId("حامی");
//                        notificationList.add(notification1);
//                        Print.print("notif4", notification1);
//                    }
//                });
//            });
//
//            List<Message> workOrdersUsersMessageList2 = messageService.getUsersMessageByUserIdList(usersListForSendingNotification);
//            if (workOrdersUsersMessageList2 != null) {
//                userListForSendingNotification.forEach(user -> {
//                    workOrdersUsersMessageList2.forEach(message -> {
//                        if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                                && message.isAssetsInTheFacilitiesThatIManage()) {
//                            //send notification to assets' personnel's parent &  assets' parents' personnel (for  workOrderStatus changes)
//                            Notification notification1 = new Notification();
//                            notification1.setRecipientUserId(user.getId());
//                            notification1.setCreationDate(new Date());
//                            notification1.setSubject("تغییر وضعیت در نت برنامه ریزی شده برای دارایی تخصیص یافته به پرسنل زیرمجموعه شما ");
//                            notification1.setMessage(user.getName() + " سلام " + "\n" + " تغییر پیدا کرد " + finalStatusInPersianAfterUpdate + "به وضعیت " + finalStatusInPersianBeforeUpdates + " از وضعیت " + scheduleMaintenanceCreateDTO.getCode() + " نت برنامه ریزی شده با کد " + assetOfThisSchedule.getCode() + " با کد " + assetOfThisSchedule.getName() + " برای دارایی ");
//                            notification1.setSenderUserId("حامی");
//                            notificationList.add(notification1);
//                            Print.print("notif5", notification1);
//                        }
//                    });
//                });
//            }
//
//            if (notifyListOfWorkOrder != null) {
//                notifyListOfWorkOrder.forEach(notify -> {
//                    //send notification to notify list(workOrder status changes)
//                    if (notify.getEvents().contains(ONSTATUSCHANG)) {
//                        Notification notification = new Notification();
//                        notification.setRecipientUserId(notify.getUser().getId());
//                        notification.setCreationDate(new Date());
//                        notification.setSubject("وضعیت دارایی در سفارش کار مرتبط به شما");
//                        notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + " تغییر پیدا کرد " + finalStatusInPersianAfterUpdate + " به وضعیت " + finalStatusInPersianBeforeUpdates + " از وضعیت " + assetOfThisSchedule.getCode() + " با کد " + assetOfThisSchedule.getName() + " دارایی مرتبط به نام " + " قرار گرفته اید. " + scheduleMaintenanceCreateDTO.getCode() + "با کد " + scheduleMaintenanceCreateDTO.getTitle() + " شما در آگاه سازی نت برنامه ریزی شده ");
//                        notification.setSenderUserId("حامی");
//                        Print.print("notif6", notification);
//                        notificationList.add(notification);
//                    }
//                    //send notification to notify list(asset status changes)
////                        if (notify.getEvents().contains(ONONLINEOFFLINE)) {
////                            Notification notification = new Notification();
////                            notification.setRecipientUserId(notify.getUser().getId());
////                            notification.setCreationDate(new Date());
////                            notification.setSubject("تغییر وضعیت فعالیت دارایی در دستورکار مرتبط به شما");
////                            notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + "قرار دارد." + assetStatus + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " فعالیت دارایی مرتبط به نام " + " قرار گرفته اید. " + workOrder.getCode() + "با کد " + workOrder.getTitle() + " شما در آگاه سازی سفارش کار ");
////                            notification.setSenderUserId("حامی");
////                            Print.print("notif6", notification);
////                            notificationList.add(notification);
////                        }
//                });
//            }
//        }
//        mongoOperations.insertAll(notificationList);
        return update;
    }


    /**
     * old version notification
     */
//        if (oldVersionScheduleMaintenance.getStatusId() != null &&
//                scheduleMaintenanceCreateDTO.getStatusId() != null &&
//                !oldVersionScheduleMaintenance.getStatusId().equals(scheduleMaintenanceCreateDTO.getStatusId()) &&
//                !oldVersionScheduleMaintenance.getAssetId().equals(scheduleMaintenanceCreateDTO.getAssetId())) {
//
//            Print.print("123321", scheduleMaintenanceCreateDTO);
//            Asset scheduledAsset = assetDao.getOneAsset(scheduleMaintenanceCreateDTO.getAssetId());
//            Print.print("UUUU", scheduledAsset);
//            if (scheduledAsset != null && scheduledAsset.getUsers() != null) {
//                List<User> userListOfScheduledAsset = userService.getAllPersonnelOfAsset(scheduledAsset.getUsers());
//
//                WorkOrderStatus workOrderStatusOfTheScheduleMaintenance = workOrderStatusService.getWorkOrderStatusById(scheduleMaintenanceCreateDTO.getStatusId());
//                List<String> userIdListForSendingNotification = new ArrayList<>();
//                List<String> assetIdListForSendingNotification = new ArrayList<>();
//
//                if (workOrderStatusOfTheScheduleMaintenance != null && workOrderStatusOfTheScheduleMaintenance.getStatus() != null) {
//                    userListOfScheduledAsset.forEach(user -> {
//                        Print.print("user", user);
//                        getAllRelatedUserToSendNotification(user, userIdListForSendingNotification);
//                        Print.print("userList", userIdListForSendingNotification);
//                    });
//                    getAllRelatedAssetToSendNotification(scheduledAsset, assetIdListForSendingNotification);
//                    Print.print("assetListId", assetIdListForSendingNotification);
//                    List<Asset> assetList = assetDao.getAssetByAssetIdList(assetIdListForSendingNotification);
//                    Print.print("assetList", assetList);
//                    if (assetList != null) {
//                        assetList.forEach(asset1 -> {
//                            if (asset1 != null && asset1.getUsers() != null) {
//                                List<User> userListOfAsset = userService.getAllPersonnelOfAsset(asset1.getUsers());
//                                userListOfAsset.forEach(user -> {
//                                    if (!user.isDeleted()) {
//                                        userIdListForSendingNotification.add(user.getId());
//                                        Print.print("finalUserList", userIdListForSendingNotification);
//                                    }
//                                });
//                            }
//                        });
//                    }
//                }
//
//                /**
//                 this is for the personnel of the asset has workOrder
//                 */
//                List<String> messageIdList = new ArrayList<>();
//                userListOfScheduledAsset.forEach(user -> {
//                    messageIdList.add(user.getMessageId());
//                });
//                Print.print("2222", userListOfScheduledAsset);
//
//                List<Message> messageList = messageService.getAllUsersMessage(messageIdList);
//                Print.print("3333", messageList);
//
//                if (workOrderStatusOfTheScheduleMaintenance != null && workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                    userListOfScheduledAsset.forEach(user -> {
//                        messageList.forEach(message -> {
//                            if (message != null && message.getId().equals(user.getMessageId())
//                                    && message.isOpen()
//                                    && message.isPushNotificationMessages()
//                                    && message.isAssetsIAmAssignedTo()) {
//                                notificationService.createNotificationForAssetAssignedUserInScheduleMaintenance(user, scheduledAsset);
//                            }
//                        });
//                    });
//                }
//
//                if (workOrderStatusOfTheScheduleMaintenance != null && workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                    userListOfScheduledAsset.forEach(user -> {
//                        messageList.forEach(message -> {
//                            if (message != null && message.getId().equals(user.getMessageId())
//                                    && message.isDraft()
//                                    && message.isPushNotificationMessages()
//                                    && message.isAssetsIAmAssignedTo()) {
//                                System.out.println("passed all filters");
//                                notificationService.createDraftNotificationForAssignedUserOfScheduledAsset(user, scheduledAsset);
//                            }
//                        });
//                    });
//                }
//
//                if (workOrderStatusOfTheScheduleMaintenance != null && (workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDCOMPLETE)
//                        || workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDINCOMPLETE))) {
//                    userListOfScheduledAsset.forEach(user -> {
//                        messageList.forEach(message -> {
//                            if (message != null
//                                    && message.getId().equals(user.getMessageId())
//                                    && message.isClosed()
//                                    && message.isPushNotificationMessages()
//                                    && message.isAssetsIAmAssignedTo()) {
//                                notificationService.createClosedNotificationForAssignedUserOfScheduledAsset(user, scheduledAsset);
//                            }
//                        });
//                    });
//                }
//
//                /**
//                 this is for report to and parent building personnel users
//                 */
//
//                List<Message> messageListOfScheduledAsset = messageService.getUsersMessageByUserIdList(userIdListForSendingNotification);
//                Print.print("MessageLIst", messageListOfScheduledAsset);
//                List<User> parentUserListOfScheduledAsset = userService.getAllParentUsersByUserIdList(userIdListForSendingNotification);
//                Print.print("parentUserList", parentUserListOfScheduledAsset);
//                if (workOrderStatusOfTheScheduleMaintenance != null && workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                    Print.print("YYYYY", parentUserListOfScheduledAsset);
//                    if (messageListOfScheduledAsset != null && parentUserListOfScheduledAsset != null) {
//                        for (Message message : messageListOfScheduledAsset) {
//                            for (User user : parentUserListOfScheduledAsset) {
//                                if (message != null
//                                        && message.getUserId() != null
//                                        && user.getId().equals(message.getUserId())
//                                        && message.isPushNotificationMessages()
//                                        && message.isOpen()
//                                        && message.isAssetsInTheFacilitiesThatIManage()) {
//                                    System.out.println("entered");
//                                    notificationService.createOpenNotificationForParentOfScheduledAsset(user, scheduledAsset);
//                                }
//                            }
//                        }
//                    }
//                }
//
//                if (workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                    System.out.println("entered draft");
//                    if (messageListOfScheduledAsset != null && parentUserListOfScheduledAsset != null) {
//                        for (Message message : messageListOfScheduledAsset) {
//                            for (User user : parentUserListOfScheduledAsset) {
//                                if (message != null
//                                        && message.getUserId() != null
//                                        && user.getId().equals(message.getUserId())
//                                        && message.isPushNotificationMessages()
//                                        && message.isDraft()
//                                        && message.isAssetsInTheFacilitiesThatIManage()) {
//                                    System.out.println("entered");
//                                    notificationService.createDraftNotificationForParentOfScheduledAsset(user, scheduledAsset);
//                                }
//                            }
//                        }
//                    }
//                }
//
//                if (workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDCOMPLETE)
//                        || workOrderStatusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDINCOMPLETE)) {
//                    System.out.println("entered close");
//                    if (messageListOfScheduledAsset != null && parentUserListOfScheduledAsset != null) {
//                        for (Message message : messageListOfScheduledAsset) {
//                            for (User user : parentUserListOfScheduledAsset) {
//                                if (message != null
//                                        && message.getUserId() != null
//                                        && user.getId().equals(message.getUserId())
//                                        && message.isPushNotificationMessages()
//                                        && message.isClosed()
//                                        && message.isAssetsInTheFacilitiesThatIManage()) {
//                                    System.out.println("entered");
//                                    notificationService.createCloseNotificationForParentOfScheduledAsset(user, scheduledAsset);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
    @Override
    public boolean updateCompletionDetailByAssetId(String scheduleMaintenanceId, ScheduleMaintenanceCompletionDetail scheduleMaintenanceCompletionDetail) {
        scheduleMaintenanceBackupService.updateScheduleMaintenanceBackUpByCompletionDetail(scheduleMaintenanceId, scheduleMaintenanceCompletionDetail);
        return super.updateResultStatus(dao.updateCompletionDetailByAssetId(scheduleMaintenanceId, scheduleMaintenanceCompletionDetail));
    }

    @Override
    public Page<ScheduleMaintenanceCreateDTO> getAllByFilterAndPagination(ScheduleMaintenanceFilterDTO
                                                                                  scheduleMaintenanceFilterDTO, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllByFilterAndPagination(scheduleMaintenanceFilterDTO, pageable, totalElement),
                pageable,
                dao.countAllByFilterAndPagination(scheduleMaintenanceFilterDTO)
        );
    }

    @Override
    public ScheduleMaintenanceCreateDTO getOneScheduleMaintenance(String scheduleMaintenanceId) {
        System.out.println(scheduleMaintenanceId);
        ScheduleMaintenance scheduleMaintenance = dao.getOneScheduleMaintenance(scheduleMaintenanceId);
        Print.print("schedule", scheduleMaintenance);
        if (scheduleMaintenance != null) {
            Asset associatedAssetToTheSchedule = null;
            if (scheduleMaintenance.getAssetId() != null) {
                associatedAssetToTheSchedule = assetDao.getAssetOfScheduleByScheduleAssetId(scheduleMaintenance.getAssetId());
            }
            Activity activity = null;
            if (scheduleMaintenance.getActivityId() != null) {
                activity = activityService.getActivityTitle(scheduleMaintenance.getActivityId());
            }

            Project associatedProjectToTheSchedule = null;
            if (scheduleMaintenance.getProjectId() != null) {
                associatedProjectToTheSchedule = projectService.getAssociatedProjectToTheSchedule(scheduleMaintenance.getProjectId());
            }
            WorkOrderStatus associatedWorkOrderStatusToTheSchedule = null;
            if (scheduleMaintenance.getStatusId() != null) {
                associatedWorkOrderStatusToTheSchedule = workOrderStatusService.getAssociatedWorkOrderStatusToTheSchedule(scheduleMaintenance.getStatusId());
            }
            return ScheduleMaintenanceCreateDTO.map(activity, scheduleMaintenance, associatedAssetToTheSchedule, associatedProjectToTheSchedule, associatedWorkOrderStatusToTheSchedule);
        } else
            return null;
    }

    @Override
    public ScheduleWithTimeAndMetering getScheduleWithTimeAndMetering(String scheduleMaintenanceId) {
        System.out.println(scheduleMaintenanceId);
        ScheduleMaintenance scheduleMaintenance = dao.getScheduleWithTimeAndMetering(scheduleMaintenanceId);
        Print.print("11111111111", scheduleMaintenance);
        if (scheduleMaintenance != null) {
            return ScheduleWithTimeAndMetering.map(scheduleMaintenance);
        } else
            return null;
    }

    @Override
    public ScheduleWithTimeAndMeteringDTO updateScheduleWithTimeAndMeteringByScheduleId(String
                                                                                                scheduleMaintenanceId, ScheduleWithTimeAndMetering scheduleWithTimeAndMetering) {
        System.out.println(scheduleMaintenanceId);
        Print.print("11111", scheduleWithTimeAndMetering);
        ScheduleMaintenance scheduleMaintenance = dao.updateScheduleWithTimeAndMeteringByScheduleId(scheduleMaintenanceId, scheduleWithTimeAndMetering);
        Print.print("222222", scheduleMaintenance);
        if (scheduleWithTimeAndMetering.getMeteringCycle().getEndDistance() != 0) {
            System.out.println("entered");
            String measurementId = scheduleMaintenance.getScheduleWithTimeAndMetering().getMeteringCycle().getUnitOfMeasurementId();
            System.out.println(measurementId);
            UnitOfMeasurement unitOfMeasurement = unitOfMeasurementService.getUnitOfMeasurementByScheduleMaintenanceMeasurementId(measurementId);
            Print.print("unitOfMeasurement", unitOfMeasurement);
//            scheduleMaintenanceBackupService.getScheduleMaintenanceBackUpTimeAndMeteringByScheduleMaintenance(scheduleMaintenanceId, scheduleWithTimeAndMetering);
            Print.print("33333", ScheduleWithTimeAndMeteringDTO.map(scheduleMaintenance, unitOfMeasurement));
            return ScheduleWithTimeAndMeteringDTO.map(scheduleMaintenance, unitOfMeasurement);
        } else {
            System.out.println("entered2");
//            scheduleMaintenanceBackupService.getScheduleMaintenanceBackUpTimeAndMeteringByScheduleMaintenance(scheduleMaintenanceId, scheduleWithTimeAndMetering);
            return ScheduleWithTimeAndMeteringDTO.map(scheduleMaintenance);
        }

    }

    @Override
    public List<TaskGroupDTO> getTaskGroupListByScheduleMaintenanceId(String scheduleMaintenanceId) {
        ScheduleMaintenance scheduleMaintenance = dao.getTaskGroupListByScheduleMaintenanceId(scheduleMaintenanceId);
        if (scheduleMaintenance.getTaskGroupList() != null) {
            return taskGroupService.getAllTaskGroupListByIdList(scheduleMaintenance.getTaskGroupList());
        } else {
            return null;
        }
    }

    @Override
    public boolean updateTaskGroupListByScheduleMaintenanceId(String scheduleMaintenanceId, List<String> taskGroupIdList) {
        scheduleMaintenanceBackupService.updateScheduleMaintenanceBackUpTaskGroupIdList(scheduleMaintenanceId, taskGroupIdList);
        return dao.updateTaskGroupListByScheduleMaintenanceId(scheduleMaintenanceId, taskGroupIdList);
    }

    @Override
    public Page<ScheduleMaintenanceCreateDTOWithObject> getAllByPagination(ScheduleMaintenanceFilterDTO
                                                                                   scheduleMaintenanceFilterDTO,
                                                                           Pageable pageable, Integer totalElement) {

        List<ScheduleMaintenanceCreateDTO> scheduleMaintenanceCreateDTOList = dao.getAllByPagination(scheduleMaintenanceFilterDTO, pageable, totalElement);
        List<String> assetIdList = new ArrayList<>();
        List<String> projectIdList = new ArrayList<>();
        List<String> statusIdList = new ArrayList<>();
        if (scheduleMaintenanceCreateDTOList != null) {
            scheduleMaintenanceCreateDTOList.forEach(scheduleMaintenanceCreateDTO1 -> {
                assetIdList.add(scheduleMaintenanceCreateDTO1.getAssetId());
                projectIdList.add(scheduleMaintenanceCreateDTO1.getProjectId());
                statusIdList.add(scheduleMaintenanceCreateDTO1.getStatusId());
            });
            List<Asset> assetList = assetDao.getAllAssetByAssetIdList(assetIdList);
            List<Project> projectList = projectService.getAllProjectByProjectIdList(projectIdList);
            List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getAllWorkOrderStatusByWorkOrderStatusId(statusIdList);
            return new PageImpl<>(
                    ScheduleMaintenanceCreateDTOWithObject.FilterMap(scheduleMaintenanceCreateDTOList, assetList, projectList, workOrderStatusList),
                    pageable,
                    dao.countAllFilteredScheduleMaintenance(scheduleMaintenanceFilterDTO)
            );
        } else
            return null;
    }

    @Override
    public List<ScheduleMaintenance> getAllScheduleMaintenance() {
        return dao.getAllScheduleMaintenance();
    }

    @Override
    public List<ScheduleMaintenance> getAllScheduleMaintenanceByScheduleMaintenanceBackup
            (List<String> scheduledMaintenanceBackupInventoryIdList) {
        return dao.getAllScheduleMaintenanceByScheduleMaintenanceBackup(scheduledMaintenanceBackupInventoryIdList);
    }

    @Override
    public List<ScheduleMaintenance> getScheduleMaintenanceListById(List<String> scheduleMaintenanceId) {
        return dao.getScheduleMaintenanceListById(scheduleMaintenanceId);
    }

    @Override
    public ScheduleMaintenance getScheduleMaintenanceById(String scheduleMaintenanceId) {
        return dao.getScheduleMaintenanceById(scheduleMaintenanceId);
    }

    @Override
    public ScheduleMaintenanceBackup getScheduleMaintenanceByAssetId(String id) {
        return dao.getScheduleMaintenanceByAssetId(id);
    }

    @Override
    public List<Date> getAllFutureDatesOfScheduleMaintenance(String scheduleMaintenanceId) {
        return scheduleMaintenanceCalenderController.getFutureDatesOfScheduleMaintenance(scheduleMaintenanceId);
    }

    @Override
    public boolean deleteScheduleMaintenanceTimeScheduling(String scheduleMaintenanceId) {
        scheduleMaintenanceBackupService.deleteScheduleMaintenanceBackUpTimeScheduling(scheduleMaintenanceId);
        scheduleMaintenanceCalenderController.deleteScheduleMaintenanceCalendar(scheduleMaintenanceId);
        return super.updateResultStatus(dao.deleteScheduleMaintenanceTimeScheduling(scheduleMaintenanceId));
    }

    @Override
    public boolean deleteScheduleMaintenanceDistanceScheduling(String scheduleMaintenanceId) {
        scheduleMaintenanceBackupService.deleteScheduleMaintenanceBackupDistanceScheduling(scheduleMaintenanceId);
        return super.updateResultStatus(dao.deleteScheduleMaintenanceDistanceScheduling(scheduleMaintenanceId));
    }

    @Override
    public List<Long> getAllFutureDistanceOfScheduleMaintenance(long per, long startDistance, long endDistance) {
        return dao.getAllFutureDistanceOfScheduleMaintenance(per, startDistance, endDistance);
    }

    @Override
    public ScheduleMaintenance getScheduleMaintenanceOfNotify(String referenceId) {
        return dao.getScheduleMaintenanceOfNotify(referenceId);
    }

    @Override
    public boolean checkIfCodeIsUnique(String code) {
        return dao.checkIfCodeIsUnique(code);
    }

    @Override
    public boolean checkIfUserExistsScheduleMaintenance(String userId) {
        return dao.checkIfUserExistsScheduleMaintenance(userId);
    }

    @Override
    public ScheduleMaintenance updateScheduleTime(ScheduledTime scheduledTime, String scheduleMaintenanceId) {
        ScheduleMaintenance scheduleMaintenance = dao.updateScheduleTime(scheduledTime, scheduleMaintenanceId);
        scheduleMaintenanceCalenderController.changeScheduleMaintenanceCalendarDate(scheduledTime, scheduleMaintenanceId);
        if (scheduleMaintenance.isActive())
            scheduleMaintenanceBackupService.updateScheduleTimeOfScheduleMaintenanceBackup(scheduledTime, scheduleMaintenanceId);
        return scheduleMaintenance;
    }

    @Override
    public ScheduleMaintenance updateScheduleDistance(ScheduledMeteringCycle scheduledMeteringCycle, String scheduleMaintenanceId) {
        ScheduleMaintenance scheduleMaintenance = dao.updateScheduleDistance(scheduledMeteringCycle, scheduleMaintenanceId);
        if (scheduleMaintenance.isActive())
            scheduleMaintenanceBackupService.updateScheduleDistance(scheduledMeteringCycle, scheduleMaintenanceId);
        UnitOfMeasurement unitOfMeasurement = unitOfMeasurementService.getUnitOfMeasurementNameById(scheduledMeteringCycle.getUnitOfMeasurementId());
        Print.print("AAA",unitOfMeasurement);
        scheduleMaintenance.getScheduleWithTimeAndMetering().getMeteringCycle().setUnitOfMeasurementName(unitOfMeasurement.getTitle());
        return scheduleMaintenance;
    }

    @Override
    public ScheduledTime getScheduleTime(String scheduleMaintenanceId) {
        ScheduleMaintenance scheduleMaintenance = dao.getScheduleMaintenanceScheduleTime(scheduleMaintenanceId);
        if (scheduleMaintenance == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("زمانبندی نت با id فرستاده شده وجود ندارد ", httpStatus);
        } else {
            if (scheduleMaintenance.getScheduleWithTimeAndMetering() != null && scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduledTime() != null) {
                return scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduledTime();
            } else {
                return new ScheduledTime();
            }
        }
    }

    @Override
    public ScheduledMeteringCycle getScheduleDistance(String scheduleMaintenanceId) {
        ScheduleMaintenance scheduleMaintenance = dao.getScheduleMaintenanceScheduleDistance(scheduleMaintenanceId);
        if (scheduleMaintenance.getScheduleWithTimeAndMetering() != null && scheduleMaintenance.getScheduleWithTimeAndMetering().getMeteringCycle() != null) {
            UnitOfMeasurement unitOfMeasurement = unitOfMeasurementService.getUnitOfMeasurementNameById(scheduleMaintenance.getScheduleWithTimeAndMetering().getMeteringCycle().getUnitOfMeasurementId());
            scheduleMaintenance.getScheduleWithTimeAndMetering().getMeteringCycle().setUnitOfMeasurementName(unitOfMeasurement.getTitle());
            return scheduleMaintenance.getScheduleWithTimeAndMetering().getMeteringCycle();
        } else {
            return new ScheduledMeteringCycle();
        }
    }

    @Override
    public List<Activity> getActivityOfScheduleMaintenanceByRelevantAsset(String assetId) {
        System.out.println(assetId);
        Asset relevantAssetOfTheScheduleMaintenance = dao.getActivityOfScheduleMaintenanceByRelevantAsset(assetId);
        Print.print("111111", relevantAssetOfTheScheduleMaintenance);
        return assetDao.getActivityListOfTheAsset(relevantAssetOfTheScheduleMaintenance.getActivityIdList());
    }

    @Override
    public void inactiveRelevantScheduleMaintenance(List<String> scheduleMaintenanceIdList) {
        dao.inactiveRelevantScheduleMaintenance(scheduleMaintenanceIdList);
    }

    @Override
    public void inActiveExpiredScheduleMaintenance(List<String> expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups) {
        dao.inActiveExpiredScheduleMaintenance(expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups);
    }

    @Override
    public List<UnitOfMeasurement> getAllUnitOfMeasurementOfTheAsset(String assetId) {
        Asset asset = assetDao.getAllUnitOfMeasurementOfTheAsset(assetId);
        List<String> assetUnitIdList = asset.getUnitIdList();
        if (assetUnitIdList != null) {
            return unitOfMeasurementService.getAllUnitOfMeasurementOfTheAsset(assetUnitIdList);
        } else {
            return null;
        }
    }

    @Override
    public boolean ifProjectExistsInScheduleMaintenance(String projectId) {
        return dao.ifProjectExistsInScheduleMaintenance(projectId);
    }

    @Override
    public boolean ifWorkStatusExistsInScheduleMaintenance(String workOrderStatusId) {
        return dao.ifWorkStatusExistsInScheduleMaintenance(workOrderStatusId);
    }

    @Override
    public boolean ifAssetExistsInScheduleMaintenance(String assetId) {
        return dao.ifAssetExistsInScheduleMaintenance(assetId);
    }

    @Override
    public boolean ifTaskGroupExistsInScheduleMaintenance(String taskGroupId) {
        return dao.ifTaskGroupExistsInScheduleMaintenance(taskGroupId);
    }

    @Override
    public boolean deleteAndInActiveScheduleMaintenanceById(String scheduleMaintenanceId) {
        return this.updateResultStatus(dao.deleteAndInActiveScheduleMaintenanceById(scheduleMaintenanceId));
    }

    @Override
    public List<ScheduleMaintenance> getProjectScheduleMaintenance(String projectId) {
        return dao.getProjectScheduleMaintenance(projectId);
    }
}
