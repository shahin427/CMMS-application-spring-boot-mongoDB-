package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service;

import org.sayar.net.Controller.ActivitySampleController;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.ScheduleActivityDTO;
import org.sayar.net.Model.DTO.ScheduleMaintenanceCreateDTO;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.*;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.dao.ScheduleMaintenanceBackupDao;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Service.Mongo.activityServices.activity.ActivityService;
import org.sayar.net.Service.NotificationService;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.newService.*;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleMaintenanceBackUpServiceImp extends GeneralServiceImpl<ScheduleMaintenanceBackup> implements ScheduleMaintenanceBackupService {

    @Autowired
    private ScheduleMaintenanceBackupDao scheduleMaintenanceBackupDao;
    @Autowired
    private ScheduleMaintananceService scheduleMaintananceService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private AssetService assetService;
    @Autowired
    private UserService userService;
    @Autowired
    private WorkOrderStatusService workOrderStatusService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivitySampleController activitySampleController;
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public List<ScheduleMaintenanceBackup> getAllScheduleMaintenanceBackup() {
        List<ScheduleMaintenance> scheduleMaintenanceList = scheduleMaintananceService.getAllScheduleMaintenance();
        return scheduleMaintenanceBackupDao.getAllScheduleMaintenanceBackup(scheduleMaintenanceList);
    }

    @Override
    public List<WorkOrder> checkIfScheduleMaintenanceBackupStartTimeArrived() {
        List<ScheduleMaintenanceBackup> scheduleMaintenanceBackup = scheduleMaintenanceBackupDao.checkIfScheduleMaintenanceBackupStartTimeArrived();
        scheduleMaintenanceBackupDao.generateNewScheduleMaintenanceBackup(scheduleMaintenanceBackup);
        List<String> scheduledMaintenanceBackupInventoryIdList = new ArrayList<>();
        scheduleMaintenanceBackup.forEach(scheduleMaintenanceBackup1 -> {
            scheduledMaintenanceBackupInventoryIdList.add(scheduleMaintenanceBackup1.getScheduleMaintenanceId());
        });
        List<ScheduleMaintenance> scheduleMaintenanceList = scheduleMaintananceService.getAllScheduleMaintenanceByScheduleMaintenanceBackup(scheduledMaintenanceBackupInventoryIdList);
        return workOrderService.generateWorkOrderByArrivedScheduleMaintenance(scheduleMaintenanceList);
    }

    @Override
    public ScheduleMaintenanceBackup createScheduleMaintenanceBackUpByScheduleMaintenance(ScheduleMaintenance scheduleMaintenance) {
        return scheduleMaintenanceBackupDao.createScheduleMaintenanceBackUpByScheduleMaintenance(scheduleMaintenance);
    }

    @Override
    public boolean updateScheduleMaintenanceBackUp(String scheduleMaintenanceId, ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO) {
        return super.updateResultStatus(scheduleMaintenanceBackupDao.updateScheduleMaintenanceBackUp(scheduleMaintenanceId, scheduleMaintenanceCreateDTO));
    }

    @Override
    public void getScheduleMaintenanceBackUpTimeAndMeteringByScheduleMaintenance(String scheduleMaintenanceId, ScheduleWithTimeAndMetering scheduleWithTimeAndMetering) {
        ScheduleMaintenance scheduleMaintenance = scheduleMaintananceService.getScheduleMaintenanceById(scheduleMaintenanceId);
        scheduleMaintenanceBackupDao.updateScheduleMaintenanceBackUpTimeAndMeteringByScheduleMaintenance
                (scheduleMaintenanceId, scheduleWithTimeAndMetering, scheduleMaintenance.getNumberOfDayForEndingEachScheduleMaintenance());
    }

    @Override
    public void checkTheScheduleMaintenanceTimeAndProduceNewWorkOrder() {

        /**
         * get all arrived time schedule maintenance backup
         */
        List<ScheduleMaintenanceBackup> ScheduleMaintenanceBackupList = scheduleMaintenanceBackupDao.getAllScheduleMaintenanceBackUp();

        /**
         * generating subsequent schedule maintenance backup
         */
        List<String> deletedScheduleMaintenanceIdList = scheduleMaintenanceBackupDao.checkScheduleMaintenanceBackupForGeneratingNewOne(ScheduleMaintenanceBackupList);

        /**
         * inactive relevant scheduleMaintenance
         */
        scheduleMaintananceService.inactiveRelevantScheduleMaintenance(deletedScheduleMaintenanceIdList);

        /**
         * generating new workOrder by today arrived time schedules
         */
        List<WorkOrder> workOrderList = workOrderService.generateWorkOrdersForTodayByTodayScheduleMaintenanceLists(ScheduleMaintenanceBackupList);

        /**
         *generating activitySample for arrivedTime ScheduleMaintenance for generating request in cardBoard
         */
        List<String> activityId = new ArrayList<>();
        workOrderList.forEach(workOrder -> {
            activityId.add(workOrder.getActivityId());
        });

        List<Activity> activityList = activityService.getListOfActivityForArrivedTimeScheduleMaintenance(activityId);
        List<ScheduleActivityDTO> scheduleActivityDTOList = new ArrayList<>();
        workOrderList.forEach(workOrder -> {
            ScheduleActivityDTO scheduleActivityDTO = new ScheduleActivityDTO();
            scheduleActivityDTO.setAssetId(workOrder.getAssetId());
            scheduleActivityDTO.setWorkOrderId(workOrder.getId());
            scheduleActivityDTO.setMaintenanceType(workOrder.getMaintenanceType());
            scheduleActivityDTO.setWorkOrderPriority(workOrder.getPriority());
            scheduleActivityDTO.setWorkOrderTitle(workOrder.getTitle());
            scheduleActivityDTO.setActivity(activityList.stream().filter(activity ->
                    activity.getId().equals(workOrder.getActivityId())).
                    findFirst().orElse(null));
            scheduleActivityDTOList.add(scheduleActivityDTO);
        });
        activitySampleController.createActivitySampleForArrivedTimeScheduleMaintenance(scheduleActivityDTOList);

//        /**
//         * notification part
//         */
//        for (ScheduleMaintenanceBackup scheduleMaintenanceBackup : ScheduleMaintenanceBackupList) {
//            Asset assetOfThisWorkOrder = assetService.getAssetOfScheduleMaintenanceByAssetId(scheduleMaintenanceBackup.getAssetId());
//            Print.print("assetOfThisWorkOrder", assetOfThisWorkOrder);
//            List<User> personnelOfTheAsset = new ArrayList<>();
//            List<String> ancestorsIdListOfThisWorkOrderUsers = new ArrayList<>();
//            if (assetOfThisWorkOrder.getUsers() != null) {
//                personnelOfTheAsset = userService.getAllPersonnelOfAsset(assetOfThisWorkOrder.getUsers());
//                Print.print("personnelOfTheAsset", personnelOfTheAsset);
//                personnelOfTheAsset.forEach(user -> {
//                    Print.print("userrr", user);
//                    getAncestorOfEachUser(user, ancestorsIdListOfThisWorkOrderUsers);
//                });
//            }
//            Print.print("ancestorsIdListOfThisWorkOrderUsers", ancestorsIdListOfThisWorkOrderUsers);
//            List<String> parenAssetsId = new ArrayList<>();
//            getAllRelatedAssetToSendNotification(assetOfThisWorkOrder, parenAssetsId);
//            Print.print("parenAssetsId", parenAssetsId);
//            List<Asset> parentAssets = assetService.getAssetByAssetIdList(parenAssetsId);
//            Print.print("parentAssets", parentAssets);
//            List<String> parentAssetsPersonnel = new ArrayList<>();
//            parentAssets.forEach(asset -> {
//                parentAssetsPersonnel.addAll(asset.getUsers());
//            });
//            Print.print("parentAssetsPersonnel", parentAssetsPersonnel);
//            List<Notify> notifyListOfWorkOrder = notifyService.getAllNotifyOfTheWorkOrderByNotifyId(scheduleMaintenanceBackup.getScheduleMaintenanceId());
//            Print.print("notifyListOfWorkOrder", notifyListOfWorkOrder);
////            WorkOrderStatus workOrderStatusOfTheNewVersionWorkOrder = workOrderStatusService.getWorkOrderStatusById(scheduleMaintenanceBackup.getStatusId());
//            List<String> usersListForSendingNotification = new ArrayList<>();
//            usersListForSendingNotification.addAll(parentAssetsPersonnel);
//            usersListForSendingNotification.addAll(ancestorsIdListOfThisWorkOrderUsers);
//            Print.print("usersListForSendingNotification", usersListForSendingNotification);
//            List<User> userListForSendingNotification = userService.getUserListForSendingNotification(usersListForSendingNotification);
//            List<Notification> notificationList = new ArrayList<>();
//
//            if (assetOfThisWorkOrder.getUsers() != null) {
//                List<Message> personnelOfAssetMessageList = messageService.getUsersMessageByUserIdList(assetOfThisWorkOrder.getUsers());
//                personnelOfTheAsset.forEach(user -> {
//                    personnelOfAssetMessageList.forEach(message -> {
//                        if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                                && message.isAssetsIAmAssignedTo()) {
//                            //send notification to personnel of the asset of scheduleMaintenance
//                            Notification notification1 = new Notification();
//                            notification1.setRecipientUserId(user.getId());
//                            notification1.setCreationDate(new Date());
//                            notification1.setSubject("سررسید نت برنامه ریزی شده در دارایی تخصیص یافته به شما");
//                            notification1.setMessage(user.getName() + " سلام " + "\n" + " فرا رسیده است و درانتظار تایید و صدور دستور کار توسط مدیر میباشد " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " سررسید نت برنامه ریزی شده برای دارایی ");
//                            notification1.setSenderUserId("حامی");
//                            notificationList.add(notification1);
//                            Print.print("notif1", notification1);
//                        }
//                    });
//                });
//            }
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
//                            notification1.setSubject("سررسید نت برنامه ریزی شده در دارایی تخصیص یافته به زیر مجموعه شما");
//                            notification1.setMessage(user.getName() + " سلام " + "\n" + " فرا رسیده است و درانتظار تایید و صدور دستور کار توسط مدیر میباشد " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " سررسید نت برنامه ریزی شده برای دارایی ");
//                            notification1.setSenderUserId("حامی");
//                            notificationList.add(notification1);
//                            Print.print("notif2", notification1);
//
//                        }
//                    });
//                });
//            }
//
//            if (notifyListOfWorkOrder != null) {
//                notifyListOfWorkOrder.forEach(notify -> {
//                    //send notification to notify list of scheduleMaintenance
//                    if (notify.getEvents().contains(ONSTATUSCHANG)) {
//                        Notification notification = new Notification();
//                        notification.setRecipientUserId(notify.getUser().getId());
//                        notification.setCreationDate(new Date());
//                        notification.setSubject("سررسید نت برنامه ریزی شده در دارایی تخصیص یافته به زیر مجموعه شما");
//                        notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + " فرا رسیده است و درانتظار تایید و صدور دستور کار توسط مدیر میباشد " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " سررسید این نت برای دارایی " + " قرار گرفته اید. " + scheduleMaintenanceBackup.getCode() + "با کد " + scheduleMaintenanceBackup.getTitle() + " شما در آگاه سازی نت برنامه ریزی شده ");
//                        Print.print("notif3", notification);
//                        notificationList.add(notification);
//                    }
//                });
//            }
//            mongoOperations.insertAll(notificationList);
//        }

//            ــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــــ
        /**
         * commented codes are old version of notification
         */

//            Asset asset = assetService.getAssetOfScheduleMaintenanceByAssetId(scheduleMaintenanceBackup.getAssetId());
//            Print.print("ASSETTTT", asset);
//            WorkOrderStatus statusOfTheScheduleMaintenance = workOrderStatusService.getWorkOrderStatusById(scheduleMaintenanceBackup.getStatusId());
//            //for adding parent user of the asset's personnel
//            List<String> notOrderedUserIdListForSendingNotification = new ArrayList<>();
//            //for adding parent building of the asset
//            List<String> assetIdListForSendingNotification = new ArrayList<>();
//
//            /**
//             *finding parent assets
//             */
//        getAllRelatedAssetToSendNotification(asset, assetIdListForSendingNotification);
//            List<Asset> relatedAssetList = assetService.getAssetByAssetIdList(assetIdListForSendingNotification);
//
//            Print.print("88888888", relatedAssetList);
//
//            /**
//             * adding assets' users to userIdListForSendingNotification
//             */
//            if (relatedAssetList != null) {
//                relatedAssetList.forEach(asset1 -> {
//                    if (asset1.getUsers() != null) {
//                        notOrderedUserIdListForSendingNotification.addAll(asset1.getUsers());
//                    }
//                });
//            }
//            Print.print("999999999", notOrderedUserIdListForSendingNotification);
//
//            /**
//             * finding asset's personnel
//             */
//            if (asset.getUsers() != null) {
//                List<User> usersOfAsset = userService.getAllPersonnelOfAsset(asset.getUsers());
//                Print.print("666666", usersOfAsset);
//                asset.getUsers().forEach(userId -> {
//                    User user = userService.getOneByUserId(userId);
//                    getAllRelatedUserToSendNotification(user, notOrderedUserIdListForSendingNotification);
//                });
//                Print.print("7777777", notOrderedUserIdListForSendingNotification);
//                //make userIdList unique
//                List<String> userIdListForSendingNotification = notOrderedUserIdListForSendingNotification.stream().distinct().collect(Collectors.toList());
//                Print.print("343434343422", userIdListForSendingNotification);
//
//                /**
//                 this is for sending notification to the personnel of the asset that has workOrder
//                 */
//                if (statusOfTheScheduleMaintenance != null && statusOfTheScheduleMaintenance.getStatus() != null) {
//                    if (statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                        List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(asset.getUsers());
//                        Print.print(usersMessageList);
//                        usersOfAsset.forEach(user -> {
//                            usersMessageList.forEach(message -> {
//                                if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                                        && message.isAssetsIAmAssignedTo()) {
//                                    Notification notification = new Notification();
//                                    notification.setRecipientUserId(user.getId());
//                                    notification.setCreationDate(new Date());
//                                    notification.setSubject("سفارش کار باز شده برای زمانبندی نت برنامه ریزی شده");
//                                    notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت باز تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                    notification.setSenderUserId("حامی");
//                                    notificationService.save(notification);
//                                    Print.print("dedeedded", notification);
//                                }
//                            });
//                        });
//                    }
//                    if (statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                        List<Message> userMessageList = messageService.getUsersMessageByUserIdList(asset.getUsers());
//                        usersOfAsset.forEach(user -> {
//                            userMessageList.forEach(message -> {
//                                System.out.println("3gggggggggg33");
//                                if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isDraft()
//                                        && message.isAssetsIAmAssignedTo()) {
//                                    System.out.println("4444");
//                                    Notification notification = new Notification();
//                                    notification.setRecipientUserId(user.getId());
//                                    notification.setCreationDate(new Date());
//                                    notification.setSubject("سفارش کار پیشنویس شده برای زمانبندی نت برنامه ریزی شده");
//                                    notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت پیشنویس تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                    notification.setSenderUserId("حامی");
//                                    Print.print("uuuuuuuuuuuu", notification);
//                                    notificationService.save(notification);
//                                }
//                            });
//                        });
//                    }
//                    if (statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDCOMPLETE) ||
//                            statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDINCOMPLETE)) {
//                        System.out.println("EEEEEEEEEEEEEEEEEEENNNNNNNNNNNNNNNTTTTTTTTTTTTEEEEEEEEEEEEERRRRRRRRRREEEEEEEEEEEEEEDFDDDDDDDDDd");
//                        List<Message> userMessageList = messageService.getUsersMessageByUserIdList(asset.getUsers());
//                        usersOfAsset.forEach(user -> {
//                            userMessageList.forEach(message -> {
//                                System.out.println("lolololoolool");
//                                if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isClosed()
//                                        && message.isAssetsIAmAssignedTo()) {
//                                    System.out.println("t666t555656565t");
//                                    Notification notification = new Notification();
//                                    notification.setRecipientUserId(user.getId());
//                                    notification.setCreationDate(new Date());
//                                    notification.setSubject("سفارش کار بسته برای زمانبندی نت برنامه ریزی شده");
//                                    notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت بسته تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                    notification.setSenderUserId("حامی");
//                                    Print.print("KKKKKKKKhhhhhhhhHHHHHHhhhHHHhH", notification);
//                                    notificationService.save(notification);
//                                }
//                            });
//                        });
//                    }
//
//                    /**
//                     this is for sending notification to the report to and personnel of the parent buildings and the users of notify list
//                     */
//                    //users of the notify of the scheduleMaintenanceBackup
//                    List<Notify> notifyListOfTheScheduleMaintenanceBackup = notifyService
//                            .getNotifyListOfTheScheduleMaintenanceBackupById(scheduleMaintenanceBackup.getScheduleMaintenanceId());
//
//                    if (statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//
//                        //sending notification to notify users
//                        notifyListOfTheScheduleMaintenanceBackup.forEach(notify -> {
//                            Notification notification = new Notification();
//                            notification.setRecipientUserId(notify.getUser().getId());
//                            notification.setCreationDate(new Date());
//                            notification.setSubject("سفارش کار باز شده برای زمانبندی نت برنامه ریزی شده");
//                            notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت باز تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                            notification.setSenderUserId("حامی");
//                            notificationService.save(notification);
//                            Print.print("NNNNNNNNBBBBBBBBBb", notification);
//                        });
//
//                        //sending notification to users of parent buildings
//                        System.out.println(userIdListForSendingNotification);
//                        List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(userIdListForSendingNotification);
//                        Print.print("<<<<<<<>>>>>>>>>>>>>>", usersMessageList);
//                        userIdListForSendingNotification.forEach(s -> {
//                            User user = userService.getOneByUserId(s);
//                            usersMessageList.forEach(message -> {
//                                        if (message.getUserId().equals(s) && message.isPushNotificationMessages() && message.isOpen()
//                                                && message.isAssetsInTheFacilitiesThatIManage()) {
//                                            Notification notification = new Notification();
//                                            notification.setRecipientUserId(user.getId());
//                                            notification.setCreationDate(new Date());
//                                            notification.setSubject("سفارش کار باز شده برای زمانبندی نت برنامه ریزی شده");
//                                            notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت باز تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                            notification.setSenderUserId("حامی");
//                                            notificationService.save(notification);
//                                            Print.print("|||||||||||}]]", notification);
//                                        }
//                                    }
//                            );
//                        });
//                    }
//
//                    if (statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//
//                        //sending notification to notify users
//                        notifyListOfTheScheduleMaintenanceBackup.forEach(notify -> {
//                            Notification notification = new Notification();
//                            notification.setRecipientUserId(notify.getUser().getId());
//                            notification.setCreationDate(new Date());
//                            notification.setSubject("سفارش کار پیشنویس شده برای زمانبندی نت برنامه ریزی شده");
//                            notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت پیشنویس تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                            notification.setSenderUserId("حامی");
//                            notificationService.save(notification);
//                            Print.print("}}}}}}{{{{{{{{{{{", notification);
//                        });
//
//                        //sending notification to users of parent buildings
//                        System.out.println(userIdListForSendingNotification);
//                        List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(userIdListForSendingNotification);
//                        Print.print("MMMEEEESSSSAAAAGGGGEEEE", usersMessageList);
//                        userIdListForSendingNotification.forEach(s -> {
//                            User user = userService.getOneByUserId(s);
//                            usersMessageList.forEach(message -> {
//                                        if (message.getUserId().equals(s) && message.isPushNotificationMessages() && message.isDraft()
//                                                && message.isAssetsInTheFacilitiesThatIManage()) {
//                                            System.out.println("3333");
//                                            Notification notification = new Notification();
//                                            notification.setRecipientUserId(user.getId());
//                                            notification.setCreationDate(new Date());
//                                            notification.setSubject("سفارش کار پیشنویس شده برای زمانبندی نت برنامه ریزی شده");
//                                            notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت پیشنویس تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                            notification.setSenderUserId("حامی");
//                                            Print.print("PPPPOOOO", notification);
//                                            notificationService.save(notification);
//                                        }
//                                    }
//                            );
//                        });
//                    }
//
//                    //sending notification to notify users
//                    if (statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDINCOMPLETE)
//                            || statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSEDCOMPLETE)) {
//
//                        notifyListOfTheScheduleMaintenanceBackup.forEach(notify -> {
//                            Notification notification = new Notification();
//                            notification.setRecipientUserId(notify.getUser().getId());
//                            notification.setCreationDate(new Date());
//                            notification.setSubject("سفارش کار بسته برای زمانبندی نت برنامه ریزی شده");
//                            notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت بسته تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                            notification.setSenderUserId("حامی");
//                            notificationService.save(notification);
//                            Print.print("++++_____", notification);
//                        });
//
//                        //sending notification to users of parent buildings
//                        System.out.println(userIdListForSendingNotification);
//                        List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(userIdListForSendingNotification);
//                        Print.print("<<MMMMNNJjk>>", usersMessageList);
//                        userIdListForSendingNotification.forEach(s -> {
//                            User user = userService.getOneByUserId(s);
//                            usersMessageList.forEach(message -> {
//                                        if (message.getUserId().equals(s) && message.isPushNotificationMessages() && message.isClosed()
//                                                && message.isAssetsInTheFacilitiesThatIManage()) {
//                                            Notification notification = new Notification();
//                                            notification.setRecipientUserId(user.getId());
//                                            notification.setCreationDate(new Date());
//                                            notification.setSubject("سفارش کار بسته برای زمانبندی نت برنامه ریزی شده");
//                                            notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت بسته تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                            notification.setSenderUserId("حامی");
//                                            Print.print("[[[[[qqqqqqqqwwwww", notification);
//                                            notificationService.save(notification);
//                                        }
//                                    }
//                            );
//                        });
//                    }
//                }
//            }}
    }

    private void getAllRelatedAssetToSendNotification(Asset asset, List<String> assetIdListForSendingNotification) {
        if (asset != null) {
            String parentAssetList = asset.getIsPartOfAsset();
            if (parentAssetList != null) {
                assetIdListForSendingNotification.add(parentAssetList);
                Asset asset2 = assetService.getOneAsset(parentAssetList);
                getAllRelatedAssetToSendNotification(asset2, assetIdListForSendingNotification);
            }
        }
    }

    private void getAllRelatedUserToSendNotification(User user, List<String> relatedUserId) {
        if (user != null && user.getParentUserId() != null) {
            String parentUserId = user.getParentUserId();
            if (parentUserId != null) {
                relatedUserId.add(parentUserId);
                User parentUser = userService.getOneByUserId(parentUserId);
                getAllRelatedUserToSendNotification(parentUser, relatedUserId);
            }
        }
    }

    @Override
    public void deleteScheduleMaintenanceBackupByScheduleMaintenanceId(String scheduleMaintenanceId) {
        scheduleMaintenanceBackupDao.findAndDeleteScheduleMaintenanceBackup(scheduleMaintenanceId);
    }

    @Override
    public boolean updateScheduleMaintenanceBackUpByBasicInformation(String scheduleMaintenanceId, ScheduleMaintenanceBasicInformation basicInformation) {
        return this.updateResultStatus(scheduleMaintenanceBackupDao.updateScheduleMaintenanceBackUpByBasicInformation(scheduleMaintenanceId, basicInformation));
    }

    @Override
    public boolean updateScheduleMaintenanceBackUpByCompletionDetail(String scheduleMaintenanceId, ScheduleMaintenanceCompletionDetail scheduleMaintenanceCompletionDetail) {
        return this.updateResultStatus(scheduleMaintenanceBackupDao.updateScheduleMaintenanceBackUpByCompletionDetail(scheduleMaintenanceId, scheduleMaintenanceCompletionDetail));
    }

    @Override
    public boolean updateScheduleMaintenanceBackUpTaskGroupIdList(String scheduleMaintenanceId, List<String> taskGroupIdList) {
        return this.updateResultStatus(scheduleMaintenanceBackupDao.updateScheduleMaintenanceBackUpTaskGroupIdList(scheduleMaintenanceId, taskGroupIdList));
    }

    @Override
    public boolean updateScheduleMaintenanceBackUpTaskPartByTask(String taskId, String referenceId) {
        return super.updateResultStatus(scheduleMaintenanceBackupDao.updateScheduleMaintenanceBackUpTaskPartByTask(taskId, referenceId));
    }

    @Override
    public void checkIfAmountArrivedToNextTriggerThresholdOfScheduleMaintenance(String referenceId, long amount, UnitOfMeasurement unitOfMeasurement) {
        scheduleMaintenanceBackupDao.checkIfAmountArrivedToNextTriggerThresholdOfScheduleMaintenance(referenceId, amount, unitOfMeasurement);
    }

    @Override
    public void deleteScheduleMaintenanceBackUpTimeScheduling(String scheduleMaintenanceId) {
        scheduleMaintenanceBackupDao.deleteScheduleMaintenanceBackUpTimeScheduling(scheduleMaintenanceId);
    }

    @Override
    public void deleteScheduleMaintenanceBackupDistanceScheduling(String scheduleMaintenanceId) {
        scheduleMaintenanceBackupDao.deleteScheduleMaintenanceBackupDistanceScheduling(scheduleMaintenanceId);
    }

    @Override
    public void updateScheduleMaintenanceBackupScheduleTime(ScheduleWithTimeAndMetering
                                                                    scheduleWithTimeAndMetering, String scheduleMaintenanceId) {
        scheduleMaintenanceBackupDao.updateScheduleMaintenanceBackupScheduleTime(scheduleWithTimeAndMetering, scheduleMaintenanceId);
    }

    @Override
    public void updateScheduleMaintenanceBackupMetering(ScheduleWithTimeAndMetering scheduleWithTimeAndMetering, String scheduleMaintenanceId) {
        scheduleMaintenanceBackupDao.updateScheduleMaintenanceBackupMetering(scheduleWithTimeAndMetering, scheduleMaintenanceId);
    }

    @Override
    public void updateScheduleTimeOfScheduleMaintenanceBackup(ScheduledTime scheduledTime, String scheduleMaintenanceId) {
        scheduleMaintenanceBackupDao.updateScheduleTimeOfScheduleMaintenanceBackup(scheduledTime, scheduleMaintenanceId);
    }

    @Override
    public void updateScheduleDistance(ScheduledMeteringCycle scheduledMeteringCycle, String scheduleMaintenanceId) {
        scheduleMaintenanceBackupDao.updateScheduleDistance(scheduledMeteringCycle, scheduleMaintenanceId);
    }

    @Override
    public void deleteRelatedTaskOfScheduleBackup(String taskId, String scheduleMaintenanceId) {
        scheduleMaintenanceBackupDao.deleteRelatedTaskOfScheduleBackup(taskId, scheduleMaintenanceId);
    }

    @Override
    public void addUserPartToScheduleBackup(String id, String scheduleMaintenanceId) {
        scheduleMaintenanceBackupDao.addUserPartToScheduleBackup(id, scheduleMaintenanceId);
    }

    @Override
    public void deleteUsedPartOfScheduleBackup(String partWithUsageCountId, String scheduleMaintenanceId) {
        scheduleMaintenanceBackupDao.deleteUsedPartOfScheduleBackup(partWithUsageCountId, scheduleMaintenanceId);
    }

    @Override
    public void addDocumentIdToBackupSchedule(String referenceId, String id) {
        scheduleMaintenanceBackupDao.addDocumentIdToBackupSchedule(referenceId, id);
    }

    @Override
    public void deleteDocumentFromBackupSchedule(String documentId, String scheduleMaintenanceId) {
        scheduleMaintenanceBackupDao.deleteDocumentFromBackupSchedule(documentId, scheduleMaintenanceId);
    }
}
