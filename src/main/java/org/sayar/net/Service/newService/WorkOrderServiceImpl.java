package org.sayar.net.Service.newService;

import com.mongodb.client.result.UpdateResult;
import org.bouncycastle.asn1.x509.sigi.PersonalData;
import org.sayar.net.Controller.ActivitySampleController;
import org.sayar.net.Controller.Mongo.activityController.activity.ActivityController;
import org.sayar.net.Controller.newController.MtbfDTO;
import org.sayar.net.Controller.newController.dto.*;
import org.sayar.net.Dao.NewDao.WorkOrderDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.Asset.Budget;
import org.sayar.net.Model.Asset.ChargeDepartment;
import org.sayar.net.Model.*;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.BasicInformation;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.MiscCost;
import org.sayar.net.Model.newModel.Notify;
import org.sayar.net.Model.newModel.Part.Part;
import org.sayar.net.Model.newModel.Project;
import org.sayar.net.Model.newModel.Task.model.Task;
import org.sayar.net.Model.newModel.Task.service.TaskService;
import org.sayar.net.Model.newModel.WorkOrder.PartWithUsageCount;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenanceBackup;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;
import org.sayar.net.Service.NotificationService;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.UserTypeService;
import org.sayar.net.Service.WorkRequestService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.websocket.server.PathParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("workOrderServiceImpl")
public class WorkOrderServiceImpl extends GeneralServiceImpl<WorkOrder> implements WorkOrderService {
    @Autowired
    private WorkOrderDao workOrderDao;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private WorkOrderStatusService workOrderStatusService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private ChargeDepartmentService chargeDepartmentService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskGroupService taskGroupService;

    @Autowired
    private ActivitySampleController activitySampleController;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private ActivityController activityController;

    @Autowired
    private PartService partService;

    @Autowired
    private WorkRequestService workRequestService;


    @Override
    public WorkOrder postCreateWorkOrder(WorkOrderCreateDTO workOrderCreateDTO) {
        return workOrderDao.postCreateWorkOrder(workOrderCreateDTO);
//        Asset asset = assetService.getOneAsset(workOrder.getAssetId());
//        if (asset.getUsers() != null) {
//            List<User> usersOfThisWorkOrder = userService.getAllPersonnelOfAsset(asset.getUsers());
//
//            List<String> userIdListOfThisWorkOrder = asset.getUsers();
//            List<String> ancestorsIdListOfThisWorkOrderUsers = new ArrayList<>();
//            workOrderDao.getAncestorsOfThisWorkOrderUsers(usersOfThisWorkOrder, ancestorsIdListOfThisWorkOrderUsers);
//
//            Print.print("ancestorsIdListOfThisWorkOrderUsers", ancestorsIdListOfThisWorkOrderUsers);
//            WorkOrderStatus workOrderStatusOfTheMadeWorkOrder = workOrderStatusService.getWorkOrderStatusById(workOrder.getStatusId());
//            Print.print("workOrderStatus", workOrderStatusOfTheMadeWorkOrder);
//
//            List<String> primaryUserIdListForSendingNotification = new ArrayList<>();
//            List<String> assetIdListForSendingNotification = new ArrayList<>();
//            getAllRelatedAssetToSendNotification(asset, assetIdListForSendingNotification);
//            List<Asset> assetList = assetService.getAssetByAssetIdList(assetIdListForSendingNotification);
//            if (assetList != null) {
//                assetList.forEach(asset1 -> {
//                    if (asset1.getUsers() != null) {
//                        primaryUserIdListForSendingNotification.addAll(asset1.getUsers());
//                    }
//                });
//            }
//            primaryUserIdListForSendingNotification.addAll(ancestorsIdListOfThisWorkOrderUsers);
//            Print.print("primaryUserIdListForSendingNotification", primaryUserIdListForSendingNotification);
//            HashSet<String> uniqueValues = new HashSet<>(primaryUserIdListForSendingNotification);
//            Print.print("uniqueValues", uniqueValues);
//            List<String> userIdListForSendingNotification = new ArrayList<>(uniqueValues);
//            Print.print("userIdListForSendingNotification", userIdListForSendingNotification);
//
//            List<String> unique = new ArrayList<>();
//            if (!userIdListOfThisWorkOrder.containsAll(userIdListForSendingNotification)) {
//                unique.addAll(userIdListForSendingNotification);
//            }
//            Print.print("workOrderUser", userIdListOfThisWorkOrder);
//            Print.print("unique", unique);
//            /**
//             sending notification to personnel of the asset which has workOrder
//             */
//
//            if (workOrderStatusOfTheMadeWorkOrder.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                System.out.println("111111");
//                List<Message> workOrdersUsersMessageList = messageService.getUsersMessageByUserIdList(userIdListOfThisWorkOrder);
//                Print.print("qwqw", workOrdersUsersMessageList);
//                usersOfThisWorkOrder.forEach(user -> {
//                    workOrdersUsersMessageList.forEach(message -> {
//                        if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                                && message.isAssetsIAmAssignedTo()) {
//                            Notification notification = new Notification();
//                            notification.setRecipientUserId(user.getId());
//                            notification.setCreationDate(new Date());
//                            notification.setSubject("سفارش کار باز شد");
//                            notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                            notification.setSenderUserId("حامی");
//                            notificationService.save(notification);
//                            Print.print("notif1", notification);
//                        }
//                    });
//                });
//            }
//
//            if (workOrderStatusOfTheMadeWorkOrder.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                System.out.println("11111");
//                List<Message> workOrdersUsersMessageList = messageService.getUsersMessageByUserIdList(userIdListOfThisWorkOrder);
//                usersOfThisWorkOrder.forEach(user -> {
//                    workOrdersUsersMessageList.forEach(message -> {
//                        System.out.println("2222");
//                        if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isDraft()
//                                && message.isAssetsIAmAssignedTo()) {
//                            System.out.println("3333");
//                            Notification notification = new Notification();
//                            notification.setRecipientUserId(user.getId());
//                            notification.setCreationDate(new Date());
//                            notification.setSubject("پیشنویس سفارش کار");
//                            notification.setMessage(user.getName() + "سلام" + "\n" + "پیشنوسی سفارش کار صادر شد" + asset.getCode() + "با کد" + asset.getName() + "برای دارایی");
//                            notification.setSenderUserId("حامی");
//                            Print.print("notif2", notification);
//                            notificationService.save(notification);
//                        }
//                    });
//                });
//            }
//
//            if (workOrderStatusOfTheMadeWorkOrder.getStatus().equals(WorkOrderStatusEnum.CLOSEDCOMPLETE) ||
//                    workOrderStatusOfTheMadeWorkOrder.getStatus().equals(WorkOrderStatusEnum.CLOSEDINCOMPLETE)) {
//                System.out.println("11111");
//                List<Message> workOrdersUsersMessageList = messageService.getUsersMessageByUserIdList(userIdListOfThisWorkOrder);
//                usersOfThisWorkOrder.forEach(user -> {
//                    workOrdersUsersMessageList.forEach(message -> {
//                        System.out.println("2222");
//                        if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isClosed()
//                                && message.isAssetsIAmAssignedTo()) {
//                            System.out.println("3333");
//                            Notification notification = new Notification();
//                            notification.setRecipientUserId(user.getId());
//                            notification.setCreationDate(new Date());
//                            notification.setSubject(" سفارش کار بسته شد");
//                            notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار بسته شد " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                            notification.setSenderUserId("حامی");
//                            Print.print("notif3", notification);
//                            notificationService.save(notification);
//                        }
//                    });
//                });
//            }
//
//            /**
//             this is for reportTo and parent building personnel of users
//             */
//
//            if (workOrderStatusOfTheMadeWorkOrder.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                System.out.println(userIdListForSendingNotification);
//                List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(userIdListForSendingNotification);
//                Print.print("MESSAGE", usersMessageList);
//                userIdListForSendingNotification.forEach(s -> {
//                    User user = userService.getOneByUserId(s);
//                    usersMessageList.forEach(message -> {
//                                if (message.getUserId().equals(s) && message.isPushNotificationMessages() && message.isOpen()
//                                        && message.isAssetsInTheFacilitiesThatIManage()) {
//                                    System.out.println("2222222GENERAL");
//                                    System.out.println(message);
//                                    Notification notification = new Notification();
//                                    notification.setRecipientUserId(s);
//                                    notification.setCreationDate(new Date());
//                                    notification.setSubject("سفارش کار باز شد");
//                                    notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                    notification.setSenderUserId("حامی");
//                                    Print.print("notif4", notification);
//                                    notificationService.save(notification);
//                                }
//                            }
//                    );
//                });
//            }
//
//            if (workOrderStatusOfTheMadeWorkOrder.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                System.out.println(userIdListForSendingNotification);
//                List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(userIdListForSendingNotification);
//                Print.print("MESSAGE", usersMessageList);
//                userIdListForSendingNotification.forEach(s -> {
//                    User user = userService.getOneByUserId(s);
//                    usersMessageList.forEach(message -> {
//                                if (message.getUserId().equals(s) && message.isPushNotificationMessages() && message.isDraft()
//                                        && message.isAssetsInTheFacilitiesThatIManage()) {
//                                    System.out.println("2222222GENERAL");
//                                    System.out.println(message);
//                                    Notification notification = new Notification();
//                                    notification.setRecipientUserId(s);
//                                    notification.setCreationDate(new Date());
//                                    notification.setSubject("پیشنویس سفارش کار");
//                                    notification.setMessage(user.getName() + "سلام" + "\n" + "پیشنوسی سفارش کار صادر شد" + asset.getCode() + "با کد" + asset.getName() + "برای دارایی");
//                                    notification.setSenderUserId("حامی");
//                                    Print.print("notif5", notification);
//                                    notificationService.save(notification);
//                                }
//                            }
//                    );
//                });
//            }
//
//            if (workOrderStatusOfTheMadeWorkOrder.getStatus().equals(WorkOrderStatusEnum.CLOSEDINCOMPLETE)
//                    || workOrderStatusOfTheMadeWorkOrder.getStatus().equals(WorkOrderStatusEnum.CLOSEDCOMPLETE)) {
//                System.out.println(userIdListForSendingNotification);
//                List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(userIdListForSendingNotification);
//                Print.print("MESSAGE", usersMessageList);
//                userIdListForSendingNotification.forEach(s -> {
//                    User user = userService.getOneByUserId(s);
//                    usersMessageList.forEach(message -> {
//                                if (message.getUserId().equals(s) && message.isPushNotificationMessages() && message.isClosed()
//                                        && message.isAssetsInTheFacilitiesThatIManage()) {
//                                    System.out.println("2222222GENERAL");
//                                    System.out.println(message);
//                                    Notification notification = new Notification();
//                                    notification.setRecipientUserId(s);
//                                    notification.setCreationDate(new Date());
//                                    notification.setSubject(" سفارش کار بسته");
//                                    notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار بسته شد " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                    notification.setSenderUserId("حامی");
//                                    Print.print("CLOSED NOTIFICATION", notification);
//                                    notificationService.save(notification);
//                                }
//                            }
//                    );
//                });
//            }

    }

    public void getAllRelatedAssetToSendNotification(Asset asset, List<String> assetIdListForSendingNotification) {
        if (asset != null) {
            String parentAssetList = asset.getIsPartOfAsset();
            if (parentAssetList != null) {
                assetIdListForSendingNotification.add(parentAssetList);
                Asset asset2 = assetService.getOneAsset(parentAssetList);
                getAllRelatedAssetToSendNotification(asset2, assetIdListForSendingNotification);
            }
        }
    }

    public void getAllRelatedUserToSendNotification(User user, List<String> relatedUserId) {
        if (user != null) {
            String parentUserId = user.getParentUserId();
            if (parentUserId != null) {
                relatedUserId.add(parentUserId);
                User parentUser = userService.getOneByUserId(parentUserId);
                getAllRelatedUserToSendNotification(parentUser, relatedUserId);
            }
        }
    }

    @Override
    public boolean updateCreateWorkOrder(WorkOrderCreateDTO workOrderCreateDTO, String workOrderId) {

//        WorkOrder oldVersionWorkOrder = workOrderDao.getWorkOrderStatusId(workOrderId);
//        Print.print("oldVersion", oldVersionWorkOrder);
        boolean updateResult = this.updateResultStatus(workOrderDao.updateCreateWorkOrder(workOrderCreateDTO, workOrderId));
//        WorkOrder workOrder = workOrderDao.getOneCreateWorkOrder(workOrderId);
//        Print.print("newVersion", workOrder);
//
//        if (workOrder != null && workOrder.getAssetId() != null) {
//            Asset assetOfThisWorkOrder = assetService.getOneAsset(workOrder.getAssetId());
//            boolean assetStatusInBoolean = assetOfThisWorkOrder.getStatus();
//            String assetStatus;
//            if (assetStatusInBoolean) {
//                assetStatus = "فعال";
//            } else {
//                assetStatus = "غیرفعال";
//            }
//
//            Print.print("assetOfThisWorkOrder", assetOfThisWorkOrder);
//            List<User> personnelOfTheAsset = new ArrayList<>();
//            if (assetOfThisWorkOrder.getUsers() != null) {
//                personnelOfTheAsset = userService.getAllPersonnelOfAsset(assetOfThisWorkOrder.getUsers());
//            }
//            Print.print("personnelOfTheAsset", personnelOfTheAsset);
//            List<String> ancestorsIdListOfThisWorkOrderUsers = new ArrayList<>();
//            personnelOfTheAsset.forEach(user -> {
//                getAncestorOfEachUser(user, ancestorsIdListOfThisWorkOrderUsers);
//            });
////            getAncestorsOfThisWorkOrderUsers(personnelOfTheAsset, ancestorsIdListOfThisWorkOrderUsers);    this isn't working better to ask
//            Print.print("ancestorsIdListOfThisWorkOrderUsers", ancestorsIdListOfThisWorkOrderUsers);
//            List<String> parenAssetsId = new ArrayList<>();
//            getAllRelatedAssetToSendNotification(assetOfThisWorkOrder, parenAssetsId);
//            Print.print("parenAssetsId", parenAssetsId);
//            List<Asset> parentAssets = assetService.getAssetByAssetIdList(parenAssetsId);
//            Print.print("parentAssets", parentAssets);
//            List<String> parentAssetsPersonnel = new ArrayList<>();
//            parentAssets.forEach(asset -> {
//                if (asset.getUsers() != null)
//                    parentAssetsPersonnel.addAll(asset.getUsers());
//            });
//            Print.print("parentAssetsPersonnel", parentAssetsPersonnel);
//            String assignedTechnicianForTheWorkOrder = null;
//            if (workOrder.getBasicInformation() != null && workOrder.getBasicInformation().getUserAssignedId() != null) {
//                assignedTechnicianForTheWorkOrder = workOrder.getBasicInformation().getUserAssignedId();
//            }
//            List<Notify> notifyListOfWorkOrder = notifyService.getAllNotifyOfTheWorkOrderByNotifyId(workOrder.getId());
//            Print.print("notifyListOfWorkOrder", notifyListOfWorkOrder);
//            WorkOrderStatus workOrderStatusOfTheNewVersionWorkOrder = workOrderStatusService.getWorkOrderStatusById(workOrder.getStatusId());
//            WorkOrderStatus workOrderStatusOfTheOldVersionWorkOrder = workOrderStatusService.getWorkOrderStatusById(oldVersionWorkOrder.getStatusId());
//            List<String> usersListForSendingNotification = new ArrayList<>();
//            usersListForSendingNotification.addAll(parentAssetsPersonnel);
//            usersListForSendingNotification.addAll(ancestorsIdListOfThisWorkOrderUsers);
//            Print.print("usersListForSendingNotification", usersListForSendingNotification);
//            List<User> userListForSendingNotification = userService.getUserListForSendingNotification(usersListForSendingNotification);

//            /**
//             * this is for when workOrder created for first time as if workOrder saved
//             * sending notification in save time
//             */
//            String statusInPersianAfterUpdate = null;
//            if (workOrderStatusOfTheNewVersionWorkOrder != null) {
//                if (workOrderStatusOfTheNewVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                    statusInPersianAfterUpdate = "باز";
//                }
//                if (workOrderStatusOfTheNewVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.CLOSED)) {
//                    statusInPersianAfterUpdate = "بسته";
//                }
//                if (workOrderStatusOfTheNewVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                    statusInPersianAfterUpdate = "پیشنویس";
//                }
//                if (workOrderStatusOfTheNewVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.PENDING)) {
//                    statusInPersianAfterUpdate = "در انتظار";
//                }
//            } else {
//                statusInPersianAfterUpdate = "تعیین نشده";
//            }
//            String finalStatusInPersianAfterUpdate = statusInPersianAfterUpdate;
//
//            List<Notification> notificationList = new ArrayList<>();
//            if (oldVersionWorkOrder.getStatusId() == null) {
////                List<Message> workOrdersUsersMessageList = messageService.getUsersMessageByUserIdList(assetOfThisWorkOrder.getUsers());
////                personnelOfTheAsset.forEach(user -> {
////                    workOrdersUsersMessageList.forEach(message -> {
////                        if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen() && message.isAssetsIAmAssignedTo()) {
////                            //personnel of the workOrder's asset (notification for workOrderStatus changes)
////                            Notification notification = new Notification();
////                            notification.setRecipientUserId(user.getId());
////                            notification.setCreationDate(new Date());
////                            notification.setSubject("تخصیص دستورکار به دارایی شما");
////                            notification.setMessage(user.getName() + " سلام " + "\n" + " تخصیص یافت " + finalStatusInPersianAfterUpdate + " با وضعیت " + " دستور کار " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " برای دارایی ");
////                            notification.setSenderUserId("حامی");
////                            notificationList.add(notification);
////                            Print.print("notif1A", notification);
////
////                            //personnel of the workOrder's asset (for assetStatus changes)
////                            Notification notificationB = new Notification();
////                            notificationB.setRecipientUserId(user.getId());
////                            notificationB.setCreationDate(new Date());
////                            notificationB.setSubject("وضعیت فعالیت دارایی شما ");
////                            notificationB.setMessage(user.getName() + " سلام " + "\n" + " قرار دارد " + assetStatus + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " دارایی ");
////                            notificationB.setSenderUserId("حامی");
////                            Print.print("notif1B", notificationB);
////                            notificationList.add(notificationB);
////                        }
////                    });
////                });
//
//                List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(usersListForSendingNotification);
//                userListForSendingNotification.forEach(user -> {
//                    usersMessageList.forEach(message -> {
//                                if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen() && message.isAssetsInTheFacilitiesThatIManage()) {
//                                    System.out.println(message);
//                                    // parents of the personnel of the workOrder's asset (for workOrderStatus changes)
//                                    Notification notification = new Notification();
//                                    notification.setRecipientUserId(user.getId());
//                                    notification.setCreationDate(new Date());
//                                    notification.setSubject("تخصیص دستورکار به دارایی تخصیص یافته به پرسنل زیرمجموعه شما ");
//                                    notification.setMessage(user.getName() + " سلام " + "\n" + " تخصیص یافت " + finalStatusInPersianAfterUpdate + " با وضعیت " + " دستور کار " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " برای دارایی ");
//                                    notification.setSenderUserId("حامی");
//                                    Print.print("notif2A", notification);
//                                    notificationList.add(notification);
//                                    // parents of the personnel of the workOrder's asset (for assetStatus changes)
//                                    Notification notificationB = new Notification();
//                                    notificationB.setRecipientUserId(user.getId());
//                                    notificationB.setCreationDate(new Date());
//                                    notificationB.setSubject("وضعیت فعالیت دارایی تخصیص یافته به پرسنل زیرمجوعه شما");
//                                    notificationB.setMessage(user.getName() + " سلام " + "\n" + " قرار دارد " + assetStatus + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " دارایی ");
//                                    notificationB.setSenderUserId("حامی");
//                                    Print.print("notif2B", notificationB);
//                                    notificationList.add(notificationB);
//                                }
//                            }
//                    );
//                });
//            }
////************************************************************************************************************************************************************************************************************
//
//            /**
//             * sending notification in update time when status changes
//             * notification will be sent just if previous workOrder status or previous asset gets updated
//             */
//            // for asset changes ______________________-------------------------________________________
//            if (!oldVersionWorkOrder.getAssetId().equals(workOrder.getAssetId())) {
////                List<Message> personnelOfAssetMessageList = messageService.getUsersMessageByUserIdList(assetOfThisWorkOrder.getUsers());
////                personnelOfTheAsset.forEach(user -> {
////                    personnelOfAssetMessageList.forEach(message -> {
////                        if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
////                                && message.isAssetsIAmAssignedTo()) {
////                            //send notification to personnel of the asset (for workOrder status changes)
////                            Notification notification1 = new Notification();
////                            notification1.setRecipientUserId(user.getId());
////                            notification1.setCreationDate(new Date());
////                            notification1.setSubject("دستور کار برای دارایی تخصیص یافته به شما");
////                            notification1.setMessage(user.getName() + " سلام " + "\n" + " صادر شد " + finalStatusInPersianAfterUpdate + " با وضعیت " + " دستور کار کد " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " برای دارایی ");
////                            notification1.setSenderUserId("حامی");
////                            notificationList.add(notification1);
////                            Print.print("notif3A", notification1);
////
////                            //send notification to personnel of the asset (for asset status changes)
////                            Notification notification2 = new Notification();
////                            notification2.setRecipientUserId(user.getId());
////                            notification2.setCreationDate(new Date());
////                            notification2.setSubject(" وضعیت فعالیت دارایی شما");
////                            notification2.setMessage(user.getName() + " سلام " + "\n" + " قرار دارد " + assetStatus + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " دارایی ");
////                            notification2.setSenderUserId("حامی");
////                            notificationList.add(notification2);
////                            Print.print("notif3B", notification2);
////                        }
////                    });
////                });
//                List<Message> workOrdersUsersMessageList2 = messageService.getUsersMessageByUserIdList(usersListForSendingNotification);
//                if (workOrdersUsersMessageList2 != null) {
//                    userListForSendingNotification.forEach(user -> {
//                        workOrdersUsersMessageList2.forEach(message -> {
//                            if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                                    && message.isAssetsInTheFacilitiesThatIManage()) {
//                                //send notification to assets' personnel's parent &  assets' parents' personnel (for  workOrderStatus changes)
//                                Notification notification1 = new Notification();
//                                notification1.setRecipientUserId(user.getId());
//                                notification1.setCreationDate(new Date());
//                                notification1.setSubject(" دستورکار برای دارایی تخصیص یافته به پرسنل زیرمجموعه شما ");
//                                notification1.setMessage(user.getName() + " سلام " + "\n" + " صادر شد " + finalStatusInPersianAfterUpdate + " با وضعیت " + workOrder.getCode() + " دستور کار کد " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " برای دارایی ");
//                                notification1.setSenderUserId("حامی");
//                                notificationList.add(notification1);
//                                Print.print("notif4A", notification1);
//
//                                //send notification to assets' personnel's parent &  assets' parents' personnel (for asset status changes)
//                                Notification notification2 = new Notification();
//                                notification2.setRecipientUserId(user.getId());
//                                notification2.setCreationDate(new Date());
//                                notification2.setSubject(" وضعیت فعالیت دارایی تخصیص یافته به پرسنل زیرمجوعه شما ");
//                                notification2.setMessage(user.getName() + " سلام " + "\n" + " قرار دارد " + assetStatus + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " دارایی ");
//                                notification2.setSenderUserId("حامی");
//                                notificationList.add(notification2);
//                                Print.print("notif4B", notification2);
//                            }
//                        });
//                    });
//                }
//                if (assignedTechnicianForTheWorkOrder != null && !assignedTechnicianForTheWorkOrder.equals("")) {
//                    User assignedTechnician = userService.getOneByUserId(assignedTechnicianForTheWorkOrder);
//                    //sending notification to assigned technician of the workOrder (for workOrder status)
//                    Notification notification = new Notification();
//                    notification.setRecipientUserId(assignedTechnicianForTheWorkOrder);
//                    notification.setCreationDate(new Date());
//                    notification.setSubject(" دستورکار تخصیص یافته به شما");
//                    notification.setMessage(assignedTechnician.getName() + " سلام " + "\n" + " صادر شد " + finalStatusInPersianAfterUpdate + " با وضعیت " + workOrder.getCode() + " دستور کار کد " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " برای دارایی ");
//                    notification.setSenderUserId("حامی");
//                    notificationList.add(notification);
//                    Print.print("notif5A", notification);
//
//                    //sending notification to assigned technician of the workOrder (for asset status)
//                    Notification notification2 = new Notification();
//                    notification2.setRecipientUserId(assignedTechnicianForTheWorkOrder);
//                    notification2.setCreationDate(new Date());
//                    notification2.setSubject(" وضعیت فعالیت دارایی در دستور کار تخصیص یافته به شما ");
//                    notification2.setMessage(assignedTechnician.getName() + " سلام " + "\n" + " قرار دارد " + assetStatus + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " دارایی ");
//                    notification2.setSenderUserId("حامی");
//                    notificationList.add(notification2);
//                    Print.print("notif5B", notification2);
//                }
//                if (notifyListOfWorkOrder != null) {
//                    notifyListOfWorkOrder.forEach(notify -> {
//                        //send notification to notify list(workOrder status changes)
//                        if (notify.getEvents().contains(ONSTATUSCHANG)) {
//                            Notification notification = new Notification();
//                            notification.setRecipientUserId(notify.getUser().getId());
//                            notification.setCreationDate(new Date());
//                            notification.setSubject("وضعیت دارایی در دستور کار مرتبط به شما");
//                            notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + " قرار دارد " + finalStatusInPersianAfterUpdate + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " دارایی مرتبط به نام " + " قرار گرفته اید. " + workOrder.getCode() + " با کد " + workOrder.getTitle() + " شما در آگاه سازی دستور کار ");
//                            notification.setSenderUserId("حامی");
//                            Print.print("notif6", notification);
//                            notificationList.add(notification);
//                        }
//
//                        //send notification to notify list(asset status changes)
//                        if (notify.getEvents().contains(ONONLINEOFFLINE)) {
//                            Notification notification = new Notification();
//                            notification.setRecipientUserId(notify.getUser().getId());
//                            notification.setCreationDate(new Date());
//                            notification.setSubject("تغییر وضعیت فعالیت دارایی در دستورکار مرتبط به شما");
//                            notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + "قرار دارد." + assetStatus + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " فعالیت دارایی مرتبط به نام " + " قرار گرفته اید. " + workOrder.getCode() + "با کد " + workOrder.getTitle() + " شما در آگاه سازی سفارش کار ");
//                            notification.setSenderUserId("حامی");
//                            Print.print("notif6", notification);
//                            notificationList.add(notification);
//                        }
//                    });
//                }
//            }
//
//            //for status changes__________________-------------------------_________________________
//            if (oldVersionWorkOrder.getStatusId() != null
//                    && workOrder.getStatusId() != null
//                    && !oldVersionWorkOrder.getStatusId().equals(workOrder.getStatusId())) {
//
//                String statusInPersianBeforeUpdates = null;
//                if (workOrderStatusOfTheOldVersionWorkOrder != null) {
//                    if (workOrderStatusOfTheOldVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                        statusInPersianBeforeUpdates = "باز";
//                    }
//                    if (workOrderStatusOfTheOldVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.CLOSED)) {
//                        statusInPersianBeforeUpdates = "بسته";
//                    }
//                    if (workOrderStatusOfTheOldVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                        statusInPersianBeforeUpdates = "پیشنویس";
//                    }
//                    if (workOrderStatusOfTheOldVersionWorkOrder.getStatus().equals(WorkOrderStatusEnum.PENDING)) {
//                        statusInPersianBeforeUpdates = " انتظار";
//                    }
//                }
//                String finalStatusInPersianBeforeUpdates = statusInPersianBeforeUpdates;
//
//                List<Message> personnelOfAssetMessageList = messageService.getUsersMessageByUserIdList(assetOfThisWorkOrder.getUsers());
//                personnelOfTheAsset.forEach(user -> {
//                    personnelOfAssetMessageList.forEach(message -> {
//                        if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                                && message.isAssetsIAmAssignedTo()) {
//                            //send notification to personnel of the asset (for workOrder status changes)
//                            Notification notification1 = new Notification();
//                            notification1.setRecipientUserId(user.getId());
//                            notification1.setCreationDate(new Date());
//                            notification1.setSubject("تغییر وضعیت در دستورکار دارایی تخصیص یافته به شما");
//                            notification1.setMessage(user.getName() + " سلام " + "\n" + " تغییر پیدا کرد " + finalStatusInPersianAfterUpdate + "به وضعیت " + finalStatusInPersianBeforeUpdates + " از وضعیت " + workOrder.getCode() + " دستور کار کد " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " برای دارایی ");
//                            notification1.setSenderUserId("حامی");
//                            notificationList.add(notification1);
//                            Print.print("notif3A", notification1);
//
//                            //send notification to personnel of the asset (for asset status changes)
//                            Notification notification2 = new Notification();
//                            notification2.setRecipientUserId(user.getId());
//                            notification2.setCreationDate(new Date());
//                            notification2.setSubject("تغییر وضعیت فعالیت دارایی شما");
//                            notification2.setMessage(user.getName() + " سلام " + "\n" + " قرار دارد " + assetStatus + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " دارایی ");
//                            notification2.setSenderUserId("حامی");
//                            notificationList.add(notification2);
//                            Print.print("notif3B", notification2);
//                        }
//                    });
//                });
//
//                List<Message> workOrdersUsersMessageList2 = messageService.getUsersMessageByUserIdList(usersListForSendingNotification);
//                if (workOrdersUsersMessageList2 != null) {
//                    userListForSendingNotification.forEach(user -> {
//                        workOrdersUsersMessageList2.forEach(message -> {
//                            if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                                    && message.isAssetsInTheFacilitiesThatIManage()) {
//                                //send notification to assets' personnel's parent &  assets' parents' personnel (for  workOrderStatus changes)
//                                Notification notification1 = new Notification();
//                                notification1.setRecipientUserId(user.getId());
//                                notification1.setCreationDate(new Date());
//                                notification1.setSubject("تغییر وضعیت در دستورکار دارایی تخصیص یافته به پرسنل زیرمجموعه شما ");
//                                notification1.setMessage(user.getName() + " سلام " + "\n" + " تغییر پیدا کرد " + finalStatusInPersianAfterUpdate + "به وضعیت " + finalStatusInPersianBeforeUpdates + " از وضعیت " + workOrder.getCode() + " دستور کار کد " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " برای دارایی ");
//                                notification1.setSenderUserId("حامی");
//                                notificationList.add(notification1);
//                                Print.print("notif4A", notification1);
//
//                                //send notification to assets' personnel's parent &  assets' parents' personnel (for asset status changes)
//                                Notification notification2 = new Notification();
//                                notification2.setRecipientUserId(user.getId());
//                                notification2.setCreationDate(new Date());
//                                notification2.setSubject("تغییر وضعیت در فعالیت دارایی تخصیص یافته به پرسنل زیرمجوعه شما ");
//                                notification2.setMessage(user.getName() + " سلام " + "\n" + " قرار دارد " + assetStatus + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " دارایی ");
//                                notification2.setSenderUserId("حامی");
//                                notificationList.add(notification2);
//                                Print.print("notif4B", notification2);
//                            }
//                        });
//                    });
//                }
//                if (assignedTechnicianForTheWorkOrder != null && !assignedTechnicianForTheWorkOrder.equals("")) {
//                    User assignedTechnician = userService.getOneByUserId(assignedTechnicianForTheWorkOrder);
//                    //sending notification to assigned technician of the workOrder (for workOrder status)
//                    Notification notification = new Notification();
//                    notification.setRecipientUserId(assignedTechnicianForTheWorkOrder);
//                    notification.setCreationDate(new Date());
//                    notification.setSubject("تغییر وضعیت دستورکار تخصیص یافته به شما");
//                    notification.setMessage(assignedTechnician.getName() + " سلام " + "\n" + " تغییر پیدا کرد " + finalStatusInPersianAfterUpdate + " به وضعیت " + finalStatusInPersianBeforeUpdates + " از وضعیت " + workOrder.getCode() + " دستور کار کد " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " برای دارایی ");
//                    notification.setSenderUserId("حامی");
//                    notificationList.add(notification);
//                    Print.print("notif5A", notification);
//
//                    //sending notification to assigned technician of the workOrder (for asset status)
//                    Notification notification2 = new Notification();
//                    notification2.setRecipientUserId(assignedTechnicianForTheWorkOrder);
//                    notification2.setCreationDate(new Date());
//                    notification2.setSubject("تغییر وضعیت فعالیت دارایی در سفارش کار تخصیص یافته به شما ");
//                    notification2.setMessage(assignedTechnician.getName() + " سلام " + "\n" + " قرار دارد " + assetStatus + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " دارایی ");
//                    notification2.setSenderUserId("حامی");
//                    notificationList.add(notification2);
//                    Print.print("notif5B", notification2);
//                }
//                if (notifyListOfWorkOrder != null) {
//                    notifyListOfWorkOrder.forEach(notify -> {
//                        //send notification to notify list(workOrder status changes)
//                        if (notify.getEvents().contains(ONSTATUSCHANG)) {
//                            Notification notification = new Notification();
//                            notification.setRecipientUserId(notify.getUser().getId());
//                            notification.setCreationDate(new Date());
//                            notification.setSubject("وضعیت دارایی در سفارش کار مرتبط به شما");
//                            notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + " تغییر پیدا کرد " + finalStatusInPersianAfterUpdate + " به وضعیت " + finalStatusInPersianBeforeUpdates + " از وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " دارایی مرتبط به نام " + " قرار گرفته اید. " + workOrder.getCode() + "با کد " + workOrder.getTitle() + " شما در آگاه سازی سفارش کار ");
//                            notification.setSenderUserId("حامی");
//                            Print.print("notif6", notification);
//                            notificationList.add(notification);
//                        }
//
//                        //send notification to notify list(asset status changes)
//                        if (notify.getEvents().contains(ONONLINEOFFLINE)) {
//                            Notification notification = new Notification();
//                            notification.setRecipientUserId(notify.getUser().getId());
//                            notification.setCreationDate(new Date());
//                            notification.setSubject("تغییر وضعیت فعالیت دارایی در دستورکار مرتبط به شما");
//                            notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + "قرار دارد." + assetStatus + " در وضعیت " + assetOfThisWorkOrder.getCode() + " با کد " + assetOfThisWorkOrder.getName() + " فعالیت دارایی مرتبط به نام " + " قرار گرفته اید. " + workOrder.getCode() + "با کد " + workOrder.getTitle() + " شما در آگاه سازی سفارش کار ");
//                            notification.setSenderUserId("حامی");
//                            Print.print("notif6", notification);
//                            notificationList.add(notification);
//                        }
//                    });
//                }
//            }
//            mongoOperations.insertAll(notificationList);
//        }
        return updateResult;
    }

    private void getAncestorOfEachUser(User user, List<String> ancestorsIdListOfThisWorkOrderUsers) {
        if (user != null && user.getParentUserId() != null) {
            String parentId = user.getParentUserId();
            ancestorsIdListOfThisWorkOrderUsers.add(parentId);
            User parentUser = userService.getUserParenByParentId(parentId);
            getAncestorOfEachUser(parentUser, ancestorsIdListOfThisWorkOrderUsers);
        }
    }

    private void getAncestorsOfThisWorkOrderUsers(List<User> usersOfThisWorkOrder, List<String> ancestorsIdListOfThisWorkOrderUsers) {

        List<String> thisLevelAncestorIdList = new ArrayList<>();
        usersOfThisWorkOrder.forEach(user -> {
            if (user.getParentUserId() != null)
                thisLevelAncestorIdList.add(user.getParentUserId());
        });
        ancestorsIdListOfThisWorkOrderUsers.addAll(thisLevelAncestorIdList);
        List<User> thisLevelAncestors = this.getThisLevelAncestors(thisLevelAncestorIdList);
        getAncestorsOfThisWorkOrderUsers(thisLevelAncestors, ancestorsIdListOfThisWorkOrderUsers);
    }

    private List<User> getThisLevelAncestors(List<String> thisLevelAncestorIdList) {
        Print.print("AAAAAAAAA", thisLevelAncestorIdList);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(thisLevelAncestorIdList));
        return mongoOperations.find(query, User.class);

    }

    @Override
    public WorkOrderCreateDTO getOneCreateWorkOrder(String workOrderId) {
        WorkOrder workOrder = workOrderDao.getOneCreateWorkOrder(workOrderId);
        Asset workOrderAsset = assetService.getAssetNameAndCodeOfTheWorkOrder(workOrder.getAssetId());
        Print.print("cdcd", workOrder.getStartDate());
        return WorkOrderCreateDTO.map(workOrder, workOrderAsset);
    }

    @Override
    public boolean postBasicInformation(WorkOrderDTOBasicInformation workOrderDTOBasicInformation) {
        return workOrderDao.postBasicInformation(workOrderDTOBasicInformation);
    }

    @Override
    public BasicInformation getOneBasicInformation(String workOrderId) {
        WorkOrder workOrder = workOrderDao.getOneBasicInformation(workOrderId);
        return workOrder.getBasicInformation();
    }

    @Override
    public Page<WorkOrderFilterDTO> getAllByFilterAndPagination(WorkOrderDTO workOrderDTO, Pageable pageable, Integer totalElement) {
        List<WorkOrderDTO> workOrderDTO1 = workOrderDao.getAllByFilterAndPagination(workOrderDTO, pageable, totalElement);
        List<String> ProjectIdList = new ArrayList<>();

        workOrderDTO1.forEach(workOrderDTO2 -> {
            ProjectIdList.add(workOrderDTO2.getProjectId());
        });
        List<Project> projectList = projectService.getAllProjectByProjectIdList(ProjectIdList);

        List<String> assetIdList = new ArrayList<>();
        workOrderDTO1.forEach(workOrderDTO2 -> {
            assetIdList.add(workOrderDTO2.getAssetId());
        });
        List<Asset> assetList = assetService.getAssetByAssetIdList(assetIdList);

        List<String> workOrderStatusIdList = new ArrayList<>();
        workOrderDTO1.forEach(workOrderDTO2 -> {
            workOrderStatusIdList.add(workOrderDTO2.getStatusId());
        });
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getWorkOrderStatusListByWorkOrderStatusId(workOrderStatusIdList);

        return new PageImpl<>(
                WorkOrderFilterDTO.map(workOrderDTO1, projectList, workOrderStatusList, assetList)
                , pageable
                , workOrderDao.getAllCount(workOrderDTO)
        );
    }

    @Override
    public Page<ResWorkOrderPmGetPageDTO> getPageWorkOrderCreatedBySchedule(ReqWorkOrderPmGetPageDTO entity, Pageable pageable, Integer totalElements) {
        return workOrderDao.getPageWorkOrderCreatedBySchedule(entity, pageable, totalElements);
    }

    @Override
    public List<ResWorkOrderPmGetPageDTO> getAllWorkOrderCreatedBySchedule(ReqWorkOrderPmGetPageDTO entity) {
        return workOrderDao.getAllWorkOrderCreatedBySchedule(entity);
    }


    @Override
    public Map<String, Object> getListWorkOrderForCalendar(ReqWorkOrderForCalendarGetListDTO entity) {
        List<ResWorkOrderForCalendarGetListDTO> workOrder = workOrderDao.getListWorkOrderForCalendar(entity);
        Set<String> userIdList = new HashSet<>();
        workOrder.forEach(a -> {
            userIdList.addAll( a.getUserIdList());
        });
        List<UserDTO> userList = new ArrayList<>();
        if (userIdList.size() > 0)
            userList = userService.getByIdList(userIdList);

        Map<String, Object> result = new HashMap<>();
        result.put("workOrder", workOrder);
        result.put("userList", userList);
        return result;

    }

    @Override
    public Map<String, Object> getOne(String id) {
        return workOrderDao.getOne(id);
    }

    @Override
    public List<MiscCost> getMiscCostListByWorkOrderId(@PathParam("workOrderId") String workOrderId) {
        return null;
    }

    @Override
    public List<Notify> getNotifyListByWorkOrderId(String workOrderId) {
        return null;
    }

    @Override
    public List<Task> getTaskListByWorkOrderId(String workOrderId) {
//        WorkOrder workOrder = workOrderDao.findOneWorkOrder(workOrderId);
//        return taskService.getAllTasksByTaskList(workOrder.getTasks());
        return null;
    }

    @Override
    public WorkOrder getTaskGroupListByWorkOrderId(String workOrderId) {
        return workOrderDao.getTaskGroupListByWorkOrderId(workOrderId);
//        TaskGroup taskGroupOfWorkOrder = taskGroupService.getTaskGroupListByWorkOrderId(workOrder.getTaskGroups());
    }

    @Override
    public List<PartWithUsageCount> getPartListByWorkOrderId(String workOrderId) {
//        WorkOrder workOrder = workOrderDao.findOneWorkOrder(workOrderId);
//        return workOrderPartService.getAllWorkOrderPartListByWorkOrderId(workOrder.getWorkOrderParts());
        return null;
    }

    @Override
    public boolean checkWorkOrderCode(String workOrderCode) {
        return workOrderDao.checkWorkOrderCode(workOrderCode);
    }

    @Override
    public CompletionDetailDTO getCompletionDetailByWorkOrderId(String workOrderId) {
        Print.print("232323", workOrderId);
        WorkOrder workOrder = workOrderDao.getCompletionDetailByWorkOrderId(workOrderId);
        Print.print("workOrderrr", workOrder);
        Budget budgetTitle = null;
        ChargeDepartment chargeDepartmentTitle = null;
        if (workOrder.getCompletionDetail() != null) {
            if (workOrder.getCompletionDetail().getBudgetId() != null) {
                budgetTitle = budgetService.getBudgetTitle(workOrder.getCompletionDetail().getBudgetId());
            }
            if (workOrder.getCompletionDetail().getChargeDepartmentId() != null) {
                chargeDepartmentTitle = chargeDepartmentService.getChargeDepartmentTitle
                        (workOrder.getCompletionDetail().getChargeDepartmentId());
            }
        }
        return CompletionDetailDTO.map(workOrder, budgetTitle, chargeDepartmentTitle);
    }

    @Override
    public boolean updateMiscCostListByWorkOrderId(WorkOrderMiscCostDTO workOrderMiscCostDTO) {
        return true;
    }

    @Override
    public BasicInformationDTO getBasicInformationByWorkOrderId(String workOrderId) {
        return workOrderDao.getBasicInformationByWorkOrderId(workOrderId);
//        List<User> userNameList = new ArrayList<>();
//        List<String> userIdList = new ArrayList<>();
//        if (workOrder.getBasicInformation() != null) {
//            userIdList.add(workOrder.getBasicInformation().getUserAssignedId());
//            userIdList.add(workOrder.getBasicInformation().getCompletedUserId());
//            userNameList = userService.getUsersName(userIdList);
//        }
//        return BasicInformationDTO.map(workOrder, userNameList);
    }

    @Override
    public boolean updateBasicInformationByWorkOrderId(String workOrderId, BasicInformation basicInformation) {

//        WorkOrder oldVersionOfBasicInformation = workOrderDao.getBasicInformationOfWorkOrder(workOrderId);
        UpdateResult updateResult = workOrderDao.updateBasicInformationByWorkOrderId(workOrderId, basicInformation);

//        // should send notification when assigned user for the workOrder changes
//        if (oldVersionOfBasicInformation != null
//                && oldVersionOfBasicInformation.getBasicInformation() != null
//                && oldVersionOfBasicInformation.getBasicInformation().getUserAssignedId() != null
//                && basicInformation != null
//                && basicInformation.getUserAssignedId() != null
//                && !oldVersionOfBasicInformation.getBasicInformation().getUserAssignedId().equals(basicInformation.getUserAssignedId())) {
//
//            User user = userService.getOneByUserId(basicInformation.getUserAssignedId());
//            //below service is for putting user in notify list
//            notifyService.createNotifyByUserAssignedIdOfWorkOrder(user, workOrderId);
//            WorkOrder workOrder = workOrderDao.getOneWorkOrderForNotification(workOrderId);
//            Asset asset = assetService.getAssetByWorkOrderAssetId(workOrder.getAssetId());
//
//            //below service is for sending notification to the user about asset status (active or inactive)
//            notificationService.createNotificationForWorkOrderAssignedUser(basicInformation.getUserAssignedId(), asset.getName(), asset.getCode(), user.getName(), asset.getStatus());
//
//            WorkOrderStatus workOrderStatus = workOrderStatusService.getWorkOrderStatusById(workOrder.getStatusId());
//
//            //below service is for sending notification to the user about asset status (open ,close, draft)
//            notificationService.createDraftNotificationForWorkOrderAssignedUser(user, asset.getName(), asset.getCode(), user.getName(), workOrderStatus.getStatus());
//        }
        return super.updateResultStatus(updateResult);
    }

    @Override
    public boolean updateCompletionByWorkOrderId(String workOrderId, CompletionDetail completionDetail) {
        return super.updateResultStatus(workOrderDao.updateCompletionByWorkOrderId(workOrderId, completionDetail));
    }

    @Override
    public boolean updateTaskGroupListByWorkOrderId(List<String> taskGroup, String workOrderId) {
        return workOrderDao.getWorkOrderByWorkOrderId(taskGroup, workOrderId);
    }

    @Override
    public List<WorkOrder> getWorkOrderByProjectId(String projectId) {
        return workOrderDao.getWorkOrderByProjectId(projectId);
    }

    @Override
    public List<WorkOrderAndAssetDTO> getAllWorkOrdersByProjectId(String projectId) {
        List<WorkOrder> workOrder = workOrderDao.getAllWorkOrdersByProjectId(projectId);
        Print.print("workOrder", workOrder);
        List<String> assetIdList = new ArrayList<>();
        List<String> statusIdList = new ArrayList<>();
        workOrder.forEach(workOrder1 -> {
            assetIdList.add(workOrder1.getAssetId());
            statusIdList.add(workOrder1.getStatusId());
        });
        Print.print("assetId", assetIdList);
        Print.print("statusID", statusIdList);
        List<Asset> asset = assetService.getAssetByAssetIdList(assetIdList);
        Print.print("asset", asset);
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getWorkOrderStatusListByWorkOrderStatusId(statusIdList);
        return WorkOrderAndAssetDTO.map(workOrder, asset, workOrderStatusList);
    }

    @Override
    public List<WorkOrder> generateWorkOrderByArrivedScheduleMaintenance
            (List<ScheduleMaintenance> scheduleMaintenanceList) {
        return workOrderDao.generateWorkOrderByArrivedScheduleMaintenance(scheduleMaintenanceList);
    }

    @Override
    public long countAllWorkOrderByUserId(String userAssignedId) {
        System.out.println(userAssignedId);
        List<WorkOrderStatus> workOrderStatusListExceptDraft = workOrderStatusService.getWorkOrderStatusListExceptDraft();
        List<String> stringList = new ArrayList<>();
        workOrderStatusListExceptDraft.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        System.out.println(stringList);
        Print.print(workOrderDao.getAllWorkOrderListExceptDraftForUser(stringList, userAssignedId).size());
        return workOrderDao.getAllWorkOrderListExceptDraftForUser(stringList, userAssignedId).size();
    }

    @Override
    public long getAllOpenWorkOrdersOfUsersByUserId(String userAssignedId) {
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> workOrderStatusIdList = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusIdList.add(workOrderStatus.getId());
        });
        return workOrderDao.countOpenWorkOrderByStatusId(userAssignedId, workOrderStatusIdList);
    }

    @Override
    public long countHighPriorityWorkOrders(String userAssignedId) {
        List<WorkOrderStatus> workOrderStatusListExceptDraft = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> stringList = new ArrayList<>();
        workOrderStatusListExceptDraft.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        System.out.println(stringList);
        List<WorkOrder> getAllWorkOrderExceptDraft = workOrderDao.getAllWorkOrderListExceptDraftForUser(stringList, userAssignedId);
        return getAllWorkOrderExceptDraft.size();
    }

    @Override
    public CountAllClosedWorkOrdersByUserId countAllClosedWorkOrdersByUserId(String userAssignedId) {
        List<WorkOrderStatus> workOrderStatusListExceptDraft = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> stringList = new ArrayList<>();
        workOrderStatusListExceptDraft.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        List<WorkOrder> AllWorkOrderListExceptDraftForUser = workOrderDao.getAllWorkOrderListExceptDraftForUser(stringList, userAssignedId);
        long numberOfAllWorkOrderListExceptDraftForUser = AllWorkOrderListExceptDraftForUser.size();

        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> workOrderStatusIdList = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusIdList.add(workOrderStatus.getId());
        });
        long countAllClosedWorkOrdersByUserId = workOrderDao.countAllClosedWorkOrdersByUserId(userAssignedId, workOrderStatusIdList);

        return CountAllClosedWorkOrdersByUserId.map(numberOfAllWorkOrderListExceptDraftForUser, countAllClosedWorkOrdersByUserId);
    }

    @Override
    public ClosedNotCompletedWorkOrdersAndTotalWorkOrdersDTO countClosedNotCompletedWorkOrdersByUserId(String
                                                                                                               userAssignedId) {
        List<WorkOrder> workOrderListOfUser = workOrderDao.getAllWorkOrderOfUser(userAssignedId);
        long numberOfTotalWorkOrderOfUser = workOrderListOfUser.size();
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getClosedNotCompletedWorkOrdersStatus();
        List<String> workOrderStatusIdList = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusIdList.add(workOrderStatus.getId());
        });
        long numberOfAllClosedNotCompletedWorkOrdersByUserId =
                workOrderDao.countAllClosedNotCompletedWorkOrdersByUserId(userAssignedId, workOrderStatusIdList);

        return ClosedNotCompletedWorkOrdersAndTotalWorkOrdersDTO.
                map(numberOfTotalWorkOrderOfUser, numberOfAllClosedNotCompletedWorkOrdersByUserId);
    }

    @Override
    public OnTimeCompletedWorkOrderDTO onTimeCompletedWorkOrders() {
        List<WorkOrderStatus> closedCompletedWorkOrderStatusList = workOrderStatusService.getAllClosedCompleteAndUnCompleteWorkOrders();
        List<String> stringList = new ArrayList<>();
        closedCompletedWorkOrderStatusList.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        List<WorkOrder> closedWorkOrderList = workOrderDao.getAllClosedWorkOrders(stringList);
        if (closedWorkOrderList != null) {
            long numberOfClosedWorkOrderList = closedWorkOrderList.size();
            long numberOfOnTimeCompletedWorkOrders = workOrderDao.onTimeCompletedWorkOrders(closedWorkOrderList);
            return OnTimeCompletedWorkOrderDTO.map(numberOfClosedWorkOrderList, numberOfOnTimeCompletedWorkOrders);
        } else return null;
    }

    //    @Override
    public OverDueAndCompletedWorkOrdersDTO overDueAndCompletedWorkOrders() {
        List<WorkOrderStatus> closedCompletedWorkOrderStatusList = workOrderStatusService.getAllClosedWorkOrderStatus();
        List<String> stringList = new ArrayList<>();
        closedCompletedWorkOrderStatusList.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        List<WorkOrder> closedWorkOrderList = workOrderDao.getAllClosedWorkOrders(stringList);
        long numberOfClosedWorkOrderList = closedWorkOrderList.size();
        long numberOfOverDueAndCompletedWorkOrders = workOrderDao.overDueAndCompletedWorkOrders(closedWorkOrderList);
        return OverDueAndCompletedWorkOrdersDTO.map(numberOfClosedWorkOrderList, numberOfOverDueAndCompletedWorkOrders);

    }

    @Override
    public OverDueAndNotCompletedWorkOrdersDTO overDueAndNotCompletedWorkOrders() {
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> workOrderStatusIdList = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusIdList.add(workOrderStatus.getId());
        });
        System.out.println(workOrderStatusIdList + "24353345");
        List<WorkOrder> inCompleteWorkOrders = workOrderDao.getInCompleteWorkOrders(workOrderStatusIdList);
        System.out.println(inCompleteWorkOrders + "inCommmmmedme");
        long numberOfAllWorkOrdersExceptClosed = inCompleteWorkOrders.size();
        System.out.println(numberOfAllWorkOrdersExceptClosed);
        long notClosedAndOverDueWorkOrderList = workOrderDao.getAllOpenAndOverDueWorkOrderList(workOrderStatusIdList);
        return OverDueAndNotCompletedWorkOrdersDTO.map(numberOfAllWorkOrdersExceptClosed, notClosedAndOverDueWorkOrderList);
    }

    @Override
    public long onTimeCompletionRate() {
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> workOrderStatusIdList = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusIdList.add(workOrderStatus.getId());
        });
        List<WorkOrder> closedWorkOrderList = workOrderDao.getAllClosedWorkOrders(workOrderStatusIdList);
        long onTimeClosedCompletedWorkOrders = workOrderDao.getNumberOfOnTimeClosedWorkOrders(closedWorkOrderList);
        long totalClosedWorkOrders = closedWorkOrderList.size();
        return workOrderDao.onTimeCompletionRate(onTimeClosedCompletedWorkOrders, totalClosedWorkOrders);
    }

    @Override
    public long getAllOpenWorkOrdersByUserIdBySpecifiedCurrentMonth(int month, String userAssignedId) {

        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> workOrderStatusIdList = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusIdList.add(workOrderStatus.getId());
        });
        return workOrderDao.getAllOpenWorkOrdersByUserIdBySpecifiedCurrentMonth(month, userAssignedId, workOrderStatusIdList);
    }

    @Override
    public long getAllWorkOrdersBySpecifiedMonth(int month, String userAssignedId) {
        List<WorkOrderStatus> workOrderStatusListExceptDraft = workOrderStatusService.getAllWorkOrderStatus();
        List<String> stringList = new ArrayList<>();
        workOrderStatusListExceptDraft.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        Print.print("stringList33", stringList);
        //below service modified and get all workOrderStatus involved draft
        List<WorkOrder> getAllWorkOrderExceptDraft = workOrderDao.getAllWorkOrdersExceptDraftOfUser(month, userAssignedId, stringList);
        Print.print("getAllWorkOrders", getAllWorkOrderExceptDraft);
        return getAllWorkOrderExceptDraft.size();
    }

    @Override
    public long getHighPriorityWorkOrdersBySpecifiedMonth(int month, String userAssignedId) {
        List<WorkOrderStatus> workOrderStatusListExceptDraft = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> stringList = new ArrayList<>();
        workOrderStatusListExceptDraft.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        List<WorkOrder> getAllWorkOrderExceptDraft = workOrderDao.getAllWorkOrdersExceptDraftOfUser(month, userAssignedId, stringList);
        return getAllWorkOrderExceptDraft.size();
    }

    @Override
    public long getClosedWorkOrdersByUserIdBySpecifiedMonth(int month, String userAssignedId) {
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getClosedCompletedWorkOrdersStatus();
        List<String> workOrderStatusIdList = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusIdList.add(workOrderStatus.getId());
        });
        Print.print("sdfrgth", workOrderStatusIdList);
        return workOrderDao.getClosedWorkOrdersByUserIdBySpecifiedMonth(month, userAssignedId, workOrderStatusIdList);
    }

    @Override
    public ClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonthAndTotalWorkOrderListDTO
    getClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth(int month, String userAssignedId) {

        List<WorkOrderStatus> closedWorkOrderStatus = workOrderStatusService.getAllClosedWorkOrderStatus();
        Print.print("closeWorkOrderStasdsds", closedWorkOrderStatus);
        List<String> stringList = new ArrayList<>();
        closedWorkOrderStatus.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        System.out.println(stringList);
        List<WorkOrder> closedWorkOrderList = workOrderDao.getAllClosedWorkOrdersOfUserInSpecifiedMonth(month, userAssignedId, stringList);
        Print.print("llLLL", closedWorkOrderList);
        long numberOfClosedWorkOrderList = closedWorkOrderList.size();
        System.out.println(numberOfClosedWorkOrderList);
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getClosedNotCompletedWorkOrdersStatus();
        List<String> workOrderStatusIdList = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusIdList.add(workOrderStatus.getId());
        });
        long NumberOfClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth =
                workOrderDao.getClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth(month, userAssignedId, workOrderStatusIdList);

        return ClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonthAndTotalWorkOrderListDTO.
                map(numberOfClosedWorkOrderList, NumberOfClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth);
    }

    @Override
    public List<WorkOrder> getOnTimeCompletedWorkOrdersBySpecifiedMonth(int month, String userAssignedId) {
        List<WorkOrderStatus> closedCompletedWorkOrderStatusList = workOrderStatusService.getAllClosedWorkOrderStatus();
        List<String> stringList = new ArrayList<>();
        closedCompletedWorkOrderStatusList.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        List<WorkOrder> closedWorkOrderList = workOrderDao.getAllClosedWorkOrdersOfUserInSpecificMonth(month, userAssignedId, stringList);
        return workOrderDao.getOnTimeCompletedWorkOrders(closedWorkOrderList);
    }

    @Override
    public List<WorkOrder> getOverDueAndNotCompletedWorkOrdersBySpecifiedTime(int month, String userId) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> stringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        return workOrderDao.getAllOpenWorkOrdersListByUserIdBySpecifiedCurrentMonth(month, userId, stringList);

    }

    @Override
    public TotalAndOverDueCompletedWorkOrders getOverDueAndCompletedWorkOrders(int month, String userId) {
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> workOrderStatusIdList = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusIdList.add(workOrderStatus.getId());
        });
        List<WorkOrder> closedWorkOrderListOfUserInSpecifiedMonth = workOrderDao.getAllClosedWorkOrdersOfUserInSpecificMonth(month, userId, workOrderStatusIdList);
        List<WorkOrder> OverDueAndCompletedWorkOrders = workOrderDao.getOverDueAndCompletedWorkOrders(month, userId, closedWorkOrderListOfUserInSpecifiedMonth);
        return TotalAndOverDueCompletedWorkOrders.map(closedWorkOrderListOfUserInSpecifiedMonth, OverDueAndCompletedWorkOrders);
    }

    @Override
    public long getOnTimeCompletionRateBySpecifiedTime(int month) {
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> workOrderStatusIdList = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusIdList.add(workOrderStatus.getId());
        });
        List<WorkOrder> closedWorkOrderList = workOrderDao.getAllClosedWorkOrders(workOrderStatusIdList);
        long totalClosedWorkOrdersInSpecifiedTime = workOrderDao.getAllClosedWorkOrdersInSpecifiedMonth(month, workOrderStatusIdList);
        long onTimeCompletedWorkOrders = workOrderDao.onTimeCompletedWorkOrdersInSpecifiedTime(month, closedWorkOrderList);
        return workOrderDao.onTimeCompletedWorkOrdersInSpecifiedTimeRate(totalClosedWorkOrdersInSpecifiedTime, onTimeCompletedWorkOrders);
    }

    @Override
    public List<WorkOrder> generateWorkOrdersForTodayByTodayScheduleMaintenanceLists(List<ScheduleMaintenanceBackup> todayScheduleMaintenanceBackupsForGeneratingWorkOrder) {
        return workOrderDao.generateWorkOrdersForTodayByTodayScheduleMaintenanceLists(todayScheduleMaintenanceBackupsForGeneratingWorkOrder);
    }


    @Override
    public CountOnTimeCompletedWorkOrdersBySpecifiedMonthDTO countOnTimeCompletedWorkOrdersBySpecifiedMonth(
            int month, String userAssignedId) {
        List<WorkOrderStatus> closedCompletedWorkOrderStatusList = workOrderStatusService.getAllClosedWorkOrderStatus();
        List<String> stringList = new ArrayList<>();
        closedCompletedWorkOrderStatusList.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        Print.print("statusList", closedCompletedWorkOrderStatusList);
        List<WorkOrder> closedWorkOrderList = workOrderDao.getAllClosedWorkOrdersOfUserInSpecificMonth(month, userAssignedId, stringList);
        Print.print("closedWorkOrderList", closedWorkOrderList);
        long numberOfClosedWorkOrders = closedWorkOrderList.size();
        long numberOfOnTimeCompletedWorkOrdersBySpecifiedMonth = workOrderDao.countOnTimeCompletedWorkOrdersBySpecifiedMonth(closedWorkOrderList);
        System.out.println(numberOfOnTimeCompletedWorkOrdersBySpecifiedMonth + "dcfdcf");
        return CountOnTimeCompletedWorkOrdersBySpecifiedMonthDTO.map(numberOfClosedWorkOrders, numberOfOnTimeCompletedWorkOrdersBySpecifiedMonth);
    }

    @Override
    public CountOverDueAndNotCompletedWorkOrdersBySpecifiedTimeDTO countOverDueAndNotCompletedWorkOrdersBySpecifiedTime(int month, String userId) {
        System.out.println(userId);
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenedDraftAndPendingWorOrderStatus();
        List<String> stringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });

        long numberOfOpenWorkOrderOfUserInSpecifiedMonth = workOrderDao.getAllOpenWorkOrdersByUserIdBySpecifiedCurrentMonth(month, userId, stringList);
        long numberOfOpenAndOverDueWorkOrderOfUserInSpecifiedTime = workOrderDao.numberOfOpenAndOverDueWorkOrderOfUserInSpecifiedTime(month, userId, stringList);

        return CountOverDueAndNotCompletedWorkOrdersBySpecifiedTimeDTO.map(numberOfOpenWorkOrderOfUserInSpecifiedMonth,
                numberOfOpenAndOverDueWorkOrderOfUserInSpecifiedTime);
    }

    @Override
    public CountOverDueAndCompletedWorkOrdersDTO countOverDueAndCompletedWorkOrders(int month, String userAssignedId) {
        List<WorkOrderStatus> completedWorkOrderStatus = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> stringList = new ArrayList<>();
        completedWorkOrderStatus.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        List<WorkOrder> totalClosedWorkOrdersAfterSpecifiedMonth = workOrderDao.getAllClosedWorkOrdersOfUserInSpecificMonth(month, userAssignedId, stringList);
        Print.print(totalClosedWorkOrdersAfterSpecifiedMonth);
        long numberOfTotalClosedWorkOrdersOfUserAfterSpecifiedMonth = totalClosedWorkOrdersAfterSpecifiedMonth.size();
        long numberOfOverDueAndCompletedWorkOrders = workOrderDao.countOverDueAndCompletedWorkOrders(month, userAssignedId, totalClosedWorkOrdersAfterSpecifiedMonth);
        return CountOverDueAndCompletedWorkOrdersDTO.map(numberOfTotalClosedWorkOrdersOfUserAfterSpecifiedMonth, numberOfOverDueAndCompletedWorkOrders);
    }

    @Override
    public Page<AssignedWorkOrderFilterDTO> getAllByFilterAndPaginationByUserId(String userId, WorkOrderDTO workOrderDTO, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                workOrderDao.getAllByFilterAndPaginationByUserId(userId, workOrderDTO, pageable, totalElement),
                pageable,
                workOrderDao.countAllFilteredWorkOrderByUserId(userId, workOrderDTO)
        );
    }


    @Override
    public long numberOfPendingWorkOrderOfUserInSpecificTime(int month, String userAssignedId) {
        List<WorkOrderStatus> pendingWorkOrderStatus = workOrderStatusService.getPendingWorkOrderStatus();
        List<String> stringList = new ArrayList<>();
        pendingWorkOrderStatus.forEach(workOrderStatus -> {
            stringList.add(workOrderStatus.getId());
        });
        return workOrderDao.countRealPendingWorkOrdersOfUserInSpecificMonth(month, userAssignedId, stringList);

    }

    @Override
    public WorkOrder getWorkOrderByNotifyReferenceId(String referenceId) {
        return workOrderDao.getWorkOrderByNotifyReferenceId(referenceId);
    }

    @Override
    public void generatingNewWorkOrderByTriggeredScheduleMaintenanceInDistanceRecord(ScheduleMaintenanceBackup
                                                                                             newScheduleMaintenanceBackup) {
        workOrderDao.generatingNewWorkOrderByTriggeredScheduleMaintenanceInDistanceRecord(newScheduleMaintenanceBackup);
    }

    @Override
    public boolean checkIfUserExistWorkOrder(String userId) {
        return workOrderDao.checkIfUserExistWorkOrder(userId);
    }

    @Override
    public void createWorkOrderForWorkRequest(String workRequestId, String assetId) {
        workOrderDao.createWorkOrderForWorkRequest(workRequestId, assetId);
    }

    @Override
    public WorkOrder createWorkOrderAccordingToAssociatedWorkRequest(WorkRequest workRequest) {
        return workOrderDao.createWorkOrderAccordingToAssociatedWorkRequest(workRequest);
    }

    @Override
    public List<WorkOrder> generateWorkOrdersForDistanceMeasurementForTodayByTodayScheduleMaintenanceLists
            (List<ScheduleMaintenanceBackup> ArrivedTimeScheduleMaintenanceBackupList, long amount) {
        return workOrderDao.generateWorkOrdersForDistanceMeasurementForTodayByTodayScheduleMaintenanceLists(ArrivedTimeScheduleMaintenanceBackupList, amount);
    }

    @Override
    public WorkOrder getRelevantWorkOrderOfFaultyAsset(String workOrderId) {
        return workOrderDao.getRelevantWorkOrderOfFaultyAsset(workOrderId);
    }

    @Override
    public void setTheUserIdAsAssignedUserForWorkOrder(String userId, String workOrderId) {
        workOrderDao.setTheUserIdAsAssignedUserForWorkOrder(userId, workOrderId);
    }

    @Override
    public boolean ifProjectExistsInWorkOrder(String projectId) {
        return workOrderDao.ifProjectExistsInWorkOrder(projectId);
    }

    @Override
    public boolean ifBudgetExistsInWorkOrder(String budgetId) {
        return workOrderDao.ifBudgetExistsInWorkOrder(budgetId);
    }

    @Override
    public boolean ifWorkStatusExistsInWorkOrder(String workOrderStatusId) {
        return workOrderDao.ifWorkStatusExistsInWorkOrder(workOrderStatusId);
    }

    @Override
    public boolean ifChargeDepartmentExistInWorkOrder(String chargeDepartmentId) {
        return workOrderDao.ifChargeDepartmentExistInWorkOrder(chargeDepartmentId);
    }

    @Override
    public boolean ifAssetExistsInWorkOrder(String assetId) {
        return workOrderDao.ifAssetExistsInWorkOrder(assetId);
    }

    @Override
    public boolean ifPartExistsInWorkOrder(String partId) {
        return workOrderDao.ifPartExistsInWorkOrder(partId);
    }

    @Override
    public boolean ifTaskGroupExistsInWorkOrder(String taskGroupId) {
        return workOrderDao.ifTaskGroupExistsInWorkOrder(taskGroupId);
    }

    @Override
    public WorkOrder getRelevantWorkOrder(String workOrderId) {
        return workOrderDao.getRelevantWorkOrder(workOrderId);
    }

    @Override
    public List<WorkOrder> getWorkOrderListOfScheduledActivitySample(List<String> workOrderIdList) {
        return workOrderDao.getWorkOrderListOfScheduledActivitySample(workOrderIdList);
    }

    @Override
    public List<WorkOrder> getAllWorkOrdersInSpecifiedMonth(int month, String userAssignedId) {
        return workOrderDao.getRealAllWorkOrdersInSpecifiedMonth(month, userAssignedId);
    }

    @Override
    public List<WorkOrder> getAllRealOpenWorkOrdersBySpecifiedMonth(String userAssignedId, int month) {
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> workOrderStatusListString = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusListString.add(workOrderStatus.getId());
        });
        return workOrderDao.getAllRealOpenWorkOrdersBySpecifiedMonth(userAssignedId, month, workOrderStatusListString);
    }

    @Override
    public List<WorkOrder> getHighestPriorityWorkOrdersBySpecifiedMonth(String userAssignedId, int month) {
        List<WorkOrderStatus> workOrderStatusList = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> workOrderStatusListString = new ArrayList<>();
        workOrderStatusList.forEach(workOrderStatus -> {
            workOrderStatusListString.add(workOrderStatus.getId());
        });
        return workOrderDao.getHighestPriorityWorkOrdersBySpecifiedMonth(userAssignedId, month, workOrderStatusListString);
    }

    @Override
    public OnTimeCompletedWorkOrdersAgainstAllCompletedWorkOrdersDTO getOnTimeCompletedWorkOrdersAgainstAllCompletedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> closedWorkOrderStatusList = workOrderStatusService.getAllClosedWorkOrderStatus();
        List<String> closedWorkOrderStatusStringList = new ArrayList<>();
        closedWorkOrderStatusList.forEach(workOrderStatus -> {
            closedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        List<WorkOrder> completedWorkOrders = workOrderDao.getAllClosedWorkOrdersOfUserInSpecificMonth(month, userAssignedId, closedWorkOrderStatusStringList);
        List<WorkOrder> onTimeCompletedWorkOrders = workOrderDao.getOnTimeCompletedWorkOrders(completedWorkOrders);
        return OnTimeCompletedWorkOrdersAgainstAllCompletedWorkOrdersDTO.map(completedWorkOrders, onTimeCompletedWorkOrders);
    }

    @Override
    public List<WorkOrder> getAllRealWorkOrdersByUserAssignedId(String userAssignedId) {
        List<WorkOrderStatus> openWorkOrderStatusList = workOrderStatusService.getAllOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatusList.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.getOpenWorkOrderListWithUserAssignedIdAndStatus(userAssignedId, openWorkOrderStatusStringList);
    }

    @Override
    public List<WorkOrder> getRealHighPriorityWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allWorkOrderStatusExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> allWorkOrderStatusExceptClosedString = new ArrayList<>();
        allWorkOrderStatusExceptClosed.forEach(workOrderStatus -> {
            allWorkOrderStatusExceptClosedString.add(workOrderStatus.getId());
        });
        return workOrderDao.getRealHighPriorityWorkOrders(userAssignedId, month, allWorkOrderStatusExceptClosedString);
    }

    @Override
    public List<WorkOrder> getRealAverageWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allWorkOrderStatusExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> allWorkOrderStatusExceptClosedString = new ArrayList<>();
        allWorkOrderStatusExceptClosed.forEach(workOrderStatus -> {
            allWorkOrderStatusExceptClosedString.add(workOrderStatus.getId());
        });
        return workOrderDao.getRealAveragePriorityWorkOrders(userAssignedId, month, allWorkOrderStatusExceptClosedString);
    }

    @Override
    public List<WorkOrder> getRealLowPriorityWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allWorkOrderStatusExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> allWorkOrderStatusExceptClosedString = new ArrayList<>();
        allWorkOrderStatusExceptClosed.forEach(workOrderStatus -> {
            allWorkOrderStatusExceptClosedString.add(workOrderStatus.getId());
        });
        return workOrderDao.getRealLowPriorityWorkOrders(userAssignedId, month, allWorkOrderStatusExceptClosedString);
    }

    @Override
    public List<WorkOrder> getRealVeryLowPriorityWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allWorkOrderStatusExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> allWorkOrderStatusExceptClosedString = new ArrayList<>();
        allWorkOrderStatusExceptClosed.forEach(workOrderStatus -> {
            allWorkOrderStatusExceptClosedString.add(workOrderStatus.getId());
        });
        return workOrderDao.getRealVeryLowPriorityWorkOrders(userAssignedId, month, allWorkOrderStatusExceptClosedString);
    }

    @Override
    public List<WorkOrder> getRealMaintenanceTypeWorkOrders(String userAssignedId, int month, MaintenanceType maintenanceType) {
        List<WorkOrderStatus> allWorkOrderStatusExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> allWorkOrderStatusExceptClosedString = new ArrayList<>();
        allWorkOrderStatusExceptClosed.forEach(workOrderStatus -> {
            allWorkOrderStatusExceptClosedString.add(workOrderStatus.getId());
        });
        return workOrderDao.getRealMaintenanceTypeWorkOrders(userAssignedId, month, maintenanceType, allWorkOrderStatusExceptClosedString);
    }

    @Override
    public long countRealHighestPriorityWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allWorkOrderStatusExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> allWorkOrderStatusExceptClosedString = new ArrayList<>();
        allWorkOrderStatusExceptClosed.forEach(workOrderStatus -> {
            allWorkOrderStatusExceptClosedString.add(workOrderStatus.getId());
        });
        return workOrderDao.countRealHighestPriorityWorkOrders(userAssignedId, month, allWorkOrderStatusExceptClosedString);
    }

    @Override
    public long countRealHighPriorityWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allWorkOrderStatusExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> allWorkOrderStatusExceptClosedString = new ArrayList<>();
        allWorkOrderStatusExceptClosed.forEach(workOrderStatus -> {
            allWorkOrderStatusExceptClosedString.add(workOrderStatus.getId());
        });
        return workOrderDao.countRealHighPriorityWorkOrders(userAssignedId, month, allWorkOrderStatusExceptClosedString);
    }

    @Override
    public long countRealAveragePriorityWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allWorkOrderStatusExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> allWorkOrderStatusExceptClosedString = new ArrayList<>();
        allWorkOrderStatusExceptClosed.forEach(workOrderStatus -> {
            allWorkOrderStatusExceptClosedString.add(workOrderStatus.getId());
        });
        return workOrderDao.countRealAveragePriorityWorkOrders(userAssignedId, month, allWorkOrderStatusExceptClosedString);
    }

    @Override
    public long countRealLowPriorityWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allWorkOrderStatusExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> allWorkOrderStatusExceptClosedString = new ArrayList<>();
        allWorkOrderStatusExceptClosed.forEach(workOrderStatus -> {
            allWorkOrderStatusExceptClosedString.add(workOrderStatus.getId());
        });
        return workOrderDao.countRealLowPriorityWorkOrders(userAssignedId, month, allWorkOrderStatusExceptClosedString);
    }

    @Override
    public long countRealVeryLowPriorityWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allWorkOrderStatusExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> allWorkOrderStatusExceptClosedString = new ArrayList<>();
        allWorkOrderStatusExceptClosed.forEach(workOrderStatus -> {
            allWorkOrderStatusExceptClosedString.add(workOrderStatus.getId());
        });
        return workOrderDao.countRealVeryLowPriorityWorkOrders(userAssignedId, month, allWorkOrderStatusExceptClosedString);
    }

    @Override
    public List<WorkOrder> getRealLateAndOpenWorkOrders(String userId) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.getRealLateAndOpenWorkOrders(userId, openWorkOrderStatusStringList);
    }

    @Override
    public List<WorkOrderWithColorDTO> getRealPendingWorkOrdersWithColorStatus(String userAssignedId) {
        List<WorkOrderStatus> pendingWorkOrderStatus = workOrderStatusService.getPendingWorkOrderStatus();
        List<String> pendingWorkOrderStatusStringList = new ArrayList<>();
        pendingWorkOrderStatus.forEach(workOrderStatus -> {
            pendingWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        List<WorkOrder> pendingWorkOrders = workOrderDao.getRealPendingWorkOrder(userAssignedId, pendingWorkOrderStatusStringList);
        return WorkOrderWithColorDTO.map(pendingWorkOrders);
    }

    @Override
    public List<WorkOrderWithColorDTO> getRealCurrentWeekOpenWorkOrdersWithColorStatus(String userAssignedId) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        List<WorkOrder> currentWeekOpenWorkOrders = workOrderDao.getRealCurrentWeekOpenWorkOrders(userAssignedId, openWorkOrderStatusStringList);
        return WorkOrderWithColorDTO.map(currentWeekOpenWorkOrders);
    }

    @Override
    public long countRealOpenWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.countRealOpenWorkOrders(userAssignedId, month, openWorkOrderStatusStringList);
    }

    @Override
    public List<WorkOrder> getRealClosedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> closedWorkOrderStatus = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> closedWorkOrderStatusStringList = new ArrayList<>();
        closedWorkOrderStatus.forEach(workOrderStatus -> {
            closedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.getRealClosedWorkOrders(userAssignedId, month, closedWorkOrderStatusStringList);
    }

    @Override
    public long countRealClosedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> closedWorkOrderStatus = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> closedWorkOrderStatusStringList = new ArrayList<>();
        closedWorkOrderStatus.forEach(workOrderStatus -> {
            closedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.countRealClosedWorkOrders(userAssignedId, month, closedWorkOrderStatusStringList);
    }

    @Override
    public List<WorkOrder> getRealPendingWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> pendingWorkOrderStatus = workOrderStatusService.getPendingWorkOrderStatus();
        List<String> pendingWorkOrderStatusStringList = new ArrayList<>();
        pendingWorkOrderStatus.forEach(workOrderStatus -> {
            pendingWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.getRealPendingWorkOrders(userAssignedId, month, pendingWorkOrderStatusStringList);
    }

    @Override
    public List<WorkOrder> getRealDraftWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> draftWorkOrderStatus = workOrderStatusService.getDraftWorkOrderStatus();
        List<String> draftWorkOrderStatusStringList = new ArrayList<>();
        draftWorkOrderStatus.forEach(workOrderStatus -> {
            draftWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.getRealDraftWorkOrders(userAssignedId, month, draftWorkOrderStatusStringList);
    }

    @Override
    public long countRealDraftWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> draftWorkOrderStatus = workOrderStatusService.getDraftWorkOrderStatus();
        List<String> draftWorkOrderStatusStringList = new ArrayList<>();
        draftWorkOrderStatus.forEach(workOrderStatus -> {
            draftWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.countRealDraftWorkOrders(userAssignedId, month, draftWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetOverDueWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        List<DashboardOverDueWorkOrderDTO> openWorkOrders = workOrderDao.managerDashboardCountOverDueWorkOrders(month, openWorkOrderStatusStringList);
        List<DashboardWorkOrderDTO> dashboardWorkOrderDTOList = new ArrayList<>();

        openWorkOrders.forEach(workOrder -> {
            if (workOrder.getWorkOrderEndDate() != null
                    && workOrder.getWorkOrderRequiredCompletionDate() != null
                    && workOrder.getWorkOrderEndDate().after(workOrder.getWorkOrderRequiredCompletionDate())) {

                DashboardWorkOrderDTO dashboardWorkOrderDTO = new DashboardWorkOrderDTO();
                dashboardWorkOrderDTO.setAssetId(workOrder.getAssetId());
                dashboardWorkOrderDTO.setAssetName(workOrder.getAssetName());
                dashboardWorkOrderDTO.setFromSchedule(workOrder.isFromSchedule());
                dashboardWorkOrderDTO.setProjectId(workOrder.getProjectId());
                dashboardWorkOrderDTO.setProjectName(workOrder.getProjectName());
                dashboardWorkOrderDTO.setWorkOrderId(workOrder.getWorkOrderId());
                dashboardWorkOrderDTO.setWorkOrderCode(workOrder.getWorkOrderCode());
                dashboardWorkOrderDTO.setWorkOrderEndDate(workOrder.getWorkOrderEndDate());
                dashboardWorkOrderDTO.setWorkOrderStartDate(workOrder.getWorkOrderStartDate());
                dashboardWorkOrderDTO.setWorkOrderMaintenanceType(workOrder.getWorkOrderMaintenanceType());
                dashboardWorkOrderDTO.setWorkOrderName(workOrder.getWorkOrderName());
                dashboardWorkOrderDTO.setWorkOrderPriority(workOrder.getWorkOrderPriority());
                dashboardWorkOrderDTO.setWorkOrderStatusId(workOrder.getWorkOrderStatusId());
                dashboardWorkOrderDTO.setWorkOrderStatusName(workOrder.getWorkOrderStatusName());
                dashboardWorkOrderDTOList.add(dashboardWorkOrderDTO);
            }
        });
        return dashboardWorkOrderDTOList;
    }

    @Override
    public long countRealOverDueWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        List<WorkOrder> openWorkOrdersOfUser = workOrderDao.getOpenWorkOrderListWithUserAssignedIdAndStatus(userAssignedId, openWorkOrderStatusStringList);
        List<WorkOrder> openAndOverDueWorkOrders = new ArrayList<>();
        openWorkOrdersOfUser.forEach(workOrder -> {
            if (workOrder.getEndDate().after(workOrder.getRequiredCompletionDate())) {
                openAndOverDueWorkOrders.add(workOrder);
            }
        });
        return openAndOverDueWorkOrders.size();
    }

    @Override
    public PlannedMaintenanceDTO realPlannedMaintenanceRatio(int month) {
        List<WorkOrderStatus> closedWorkOrderStatus = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> closedWorkOrderStatusStringList = new ArrayList<>();
        closedWorkOrderStatus.forEach(workOrderStatus -> {
            closedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        long plannedMaintenances = workOrderDao.realPlannedMaintenance(month, closedWorkOrderStatusStringList);
        long unplannedWorkOrders = workOrderDao.realUnplannedWorkOrders(month, closedWorkOrderStatusStringList);
        return PlannedMaintenanceDTO.map(plannedMaintenances, unplannedWorkOrders);
    }

    @Override
    public long realOnTimeCompletionRate(int month) {
        List<WorkOrderStatus> closedWorkOrderStatus = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> closedWorkOrderStatusStringList = new ArrayList<>();
        closedWorkOrderStatus.forEach(workOrderStatus -> {
            closedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        long realClosedWorkOrders = workOrderDao.countGeneralRealClosedWorkOrders(month, closedWorkOrderStatusStringList);
        long realOnTimeCloseWorkOrders = workOrderDao.countGeneralRealOnTimeClosedWorkOrders(month, closedWorkOrderStatusStringList);
        return 0;
    }

    @Override
    public List<WorkOrder> getHighestPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month) {
        List<WorkOrderStatus> allExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> exceptClosedWorkOrderStatusStringList = new ArrayList<>();
        allExceptClosed.forEach(workOrderStatus -> {
            exceptClosedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.getHighestPriorityNotPlannedWorkOrdersBySpecifiedMonth(userAssignedId, month, exceptClosedWorkOrderStatusStringList);
    }

    @Override
    public long countRealHighestPriorityNotPlannedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> exceptClosedWorkOrderStatusStringList = new ArrayList<>();
        allExceptClosed.forEach(workOrderStatus -> {
            exceptClosedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.countHighestPriorityNotPlannedWorkOrdersBySpecifiedMonth(userAssignedId, month, exceptClosedWorkOrderStatusStringList);
    }

    @Override
    public List<WorkOrder> getRealHighPriorityNotPlannedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> exceptClosedWorkOrderStatusStringList = new ArrayList<>();
        allExceptClosed.forEach(workOrderStatus -> {
            exceptClosedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.getHighPriorityNotPlannedWorkOrdersBySpecifiedMonth(userAssignedId, month, exceptClosedWorkOrderStatusStringList);
    }

    @Override
    public long countRealHighPriorityNotPlannedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> exceptClosedWorkOrderStatusStringList = new ArrayList<>();
        allExceptClosed.forEach(workOrderStatus -> {
            exceptClosedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.countHighPriorityNotPlannedWorkOrdersBySpecifiedMonth(userAssignedId, month, exceptClosedWorkOrderStatusStringList);
    }

    @Override
    public List<WorkOrder> getRealAveragePriorityNotPlannedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> exceptClosedWorkOrderStatusStringList = new ArrayList<>();
        allExceptClosed.forEach(workOrderStatus -> {
            exceptClosedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.getAveragePriorityNotPlannedWorkOrdersBySpecifiedMonth(userAssignedId, month, exceptClosedWorkOrderStatusStringList);
    }

    @Override
    public long countRealAveragePriorityNotPlannedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> exceptClosedWorkOrderStatusStringList = new ArrayList<>();
        allExceptClosed.forEach(workOrderStatus -> {
            exceptClosedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.countAveragePriorityNotPlannedWorkOrdersBySpecifiedMonth(userAssignedId, month, exceptClosedWorkOrderStatusStringList);
    }

    @Override
    public List<WorkOrder> getRealLowPriorityNotPlannedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> exceptClosedWorkOrderStatusStringList = new ArrayList<>();
        allExceptClosed.forEach(workOrderStatus -> {
            exceptClosedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.getLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(userAssignedId, month, exceptClosedWorkOrderStatusStringList);
    }

    @Override
    public long countRealLowPriorityNotPlannedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> exceptClosedWorkOrderStatusStringList = new ArrayList<>();
        allExceptClosed.forEach(workOrderStatus -> {
            exceptClosedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.countLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(userAssignedId, month, exceptClosedWorkOrderStatusStringList);
    }

    @Override
    public List<WorkOrder> getRealVeryLowPriorityNotPlannedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> exceptClosedWorkOrderStatusStringList = new ArrayList<>();
        allExceptClosed.forEach(workOrderStatus -> {
            exceptClosedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.getVeryLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(userAssignedId, month, exceptClosedWorkOrderStatusStringList);
    }

    @Override
    public long countRealVeryLowPriorityNotPlannedWorkOrders(String userAssignedId, int month) {
        List<WorkOrderStatus> allExceptClosed = workOrderStatusService.getAllWorkOrderStatusExceptClose();
        List<String> exceptClosedWorkOrderStatusStringList = new ArrayList<>();
        allExceptClosed.forEach(workOrderStatus -> {
            exceptClosedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.countVeryLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(userAssignedId, month, exceptClosedWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetHighestPriorityWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetHighestPriorityWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountHighestPriorityWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountHighestPriorityWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetHighPriorityWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetHighPriorityWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountHighPriorityWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountHighPriorityWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetAveragePriorityWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetAveragePriorityWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountAveragePriorityWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountAveragePriorityWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetLowPriorityWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetLowPriorityWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountLowPriorityWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountLowPriorityWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetLowestPriorityWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetLowestPriorityWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountLowestPriorityWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountLowestPriorityWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetOpenWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetOpenWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountOpenWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountOpenWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetClosedWorkOrder(int month) {
        List<WorkOrderStatus> closedWorkOrderStatus = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> closedWorkOrderStatusStringList = new ArrayList<>();
        closedWorkOrderStatus.forEach(workOrderStatus -> {
            closedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetClosedWorkOrder(month, closedWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountClosedWorkOrder(int month) {
        List<WorkOrderStatus> closedWorkOrderStatus = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> closedWorkOrderStatusStringList = new ArrayList<>();
        closedWorkOrderStatus.forEach(workOrderStatus -> {
            closedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountClosedWorkOrder(month, closedWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetPendingWorkOrders(int month) {
        List<WorkOrderStatus> pendingWorkOrderStatus = workOrderStatusService.getPendingWorkOrderStatus();
        List<String> pendingWorkOrderStatusStringList = new ArrayList<>();
        pendingWorkOrderStatus.forEach(workOrderStatus -> {
            pendingWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetPendingWorkOrders(month, pendingWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountPendingWorkOrders(int month) {
        List<WorkOrderStatus> pendingWorkOrderStatus = workOrderStatusService.getPendingWorkOrderStatus();
        List<String> pendingWorkOrderStatusStringList = new ArrayList<>();
        pendingWorkOrderStatus.forEach(workOrderStatus -> {
            pendingWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountPendingWorkOrders(month, pendingWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetDraftWorkOrders(int month) {
        List<WorkOrderStatus> draftWorkOrderStatus = workOrderStatusService.getDraftWorkOrderStatus();
        List<String> draftWorkOrderStatusStringList = new ArrayList<>();
        draftWorkOrderStatus.forEach(workOrderStatus -> {
            draftWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetDraftWorkOrders(month, draftWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountDraftWorkOrders(int month) {
        List<WorkOrderStatus> draftWorkOrderStatus = workOrderStatusService.getDraftWorkOrderStatus();
        List<String> draftWorkOrderStatusStringList = new ArrayList<>();
        draftWorkOrderStatus.forEach(workOrderStatus -> {
            draftWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountDraftWorkOrders(month, draftWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardHighestPriorityPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardHighestPriorityPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountHighestPriorityPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountHighestPriorityPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetHighPriorityPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetHighPriorityPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountHighPriorityPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountHighPriorityPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetAveragePriorityPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetAveragePriorityPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountAveragePriorityPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountAveragePriorityPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetLowPriorityPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetLowPriorityPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountLowPriorityPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountLowPriorityPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetLowestPriorityPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetLowestPriorityPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountLowestPriorityPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountLowestPriorityPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetOpenPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetOpenPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountOpenPlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountOpenPlannedWorkOrders(month, openWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetClosedPlannedWorkOrders(int month) {
        List<WorkOrderStatus> closedWorkOrderStatus = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> closedWorkOrderStatusStringList = new ArrayList<>();
        closedWorkOrderStatus.forEach(workOrderStatus -> {
            closedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetClosedPlannedWorkOrders(month, closedWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountClosedPlannedWorkOrders(int month) {
        List<WorkOrderStatus> closedWorkOrderStatus = workOrderStatusService.getClosedWorkOrdersStatus();
        List<String> closedWorkOrderStatusStringList = new ArrayList<>();
        closedWorkOrderStatus.forEach(workOrderStatus -> {
            closedWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountClosedPlannedWorkOrders(month, closedWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetPendingPlannedWorkOrders(int month) {
        List<WorkOrderStatus> pendingWorkOrderStatus = workOrderStatusService.getPendingWorkOrderStatus();
        List<String> pendingWorkOrderStatusStringList = new ArrayList<>();
        pendingWorkOrderStatus.forEach(workOrderStatus -> {
            pendingWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetPendingPlannedWorkOrders(month, pendingWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountPendingPlannedWorkOrders(int month) {
        List<WorkOrderStatus> pendingWorkOrderStatus = workOrderStatusService.getPendingWorkOrderStatus();
        List<String> pendingWorkOrderStatusStringList = new ArrayList<>();
        pendingWorkOrderStatus.forEach(workOrderStatus -> {
            pendingWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountPendingPlannedWorkOrders(month, pendingWorkOrderStatusStringList);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetDraftPlannedWorkOrders(int month) {
        List<WorkOrderStatus> draftWorkOrderStatus = workOrderStatusService.getDraftWorkOrderStatus();
        List<String> draftWorkOrderStatusStringList = new ArrayList<>();
        draftWorkOrderStatus.forEach(workOrderStatus -> {
            draftWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardGetDraftPlannedWorkOrders(month, draftWorkOrderStatusStringList);
    }

    @Override
    public long managerDashboardCountDraftPlannedWorkOrders(int month) {
        List<WorkOrderStatus> draftWorkOrderStatus = workOrderStatusService.getDraftWorkOrderStatus();
        List<String> draftWorkOrderStatusStringList = new ArrayList<>();
        draftWorkOrderStatus.forEach(workOrderStatus -> {
            draftWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCountDraftPlannedWorkOrders(month, draftWorkOrderStatusStringList);
    }

    private int numberOfOverDueWorkOrders = 0;

    @Override
    public long managerDashboardCountOverDueWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        List<DashboardOverDueWorkOrderDTO> openWorkOrders = workOrderDao.managerDashboardCountOverDueWorkOrders(month, openWorkOrderStatusStringList);

        openWorkOrders.forEach(workOrder -> {
            if (workOrder.getWorkOrderEndDate() != null
                    && workOrder.getWorkOrderRequiredCompletionDate() != null
                    && workOrder.getWorkOrderEndDate().after(workOrder.getWorkOrderRequiredCompletionDate())) {
                ++numberOfOverDueWorkOrders;
            }
        });
        return numberOfOverDueWorkOrders;
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetOverDuePlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        List<DashboardOverDueWorkOrderDTO> openWorkOrderList = workOrderDao.managerDashboardGetOverDuePlannedWorkOrders(month, openWorkOrderStatusStringList);
        List<DashboardWorkOrderDTO> overDueWorkOrderList = new ArrayList<>();

        openWorkOrderList.forEach(workOrder -> {
            if (workOrder.getWorkOrderEndDate() != null
                    && workOrder.getWorkOrderRequiredCompletionDate() != null
                    && workOrder.getWorkOrderEndDate().after(workOrder.getWorkOrderRequiredCompletionDate())) {
                DashboardWorkOrderDTO dashboardWorkOrderDTO = new DashboardWorkOrderDTO();
                dashboardWorkOrderDTO.setAssetId(workOrder.getAssetId());
                dashboardWorkOrderDTO.setAssetName(workOrder.getAssetName());
                dashboardWorkOrderDTO.setFromSchedule(workOrder.isFromSchedule());
                dashboardWorkOrderDTO.setProjectId(workOrder.getProjectId());
                dashboardWorkOrderDTO.setProjectName(workOrder.getProjectName());
                dashboardWorkOrderDTO.setWorkOrderId(workOrder.getWorkOrderId());
                dashboardWorkOrderDTO.setWorkOrderCode(workOrder.getWorkOrderCode());
                dashboardWorkOrderDTO.setWorkOrderEndDate(workOrder.getWorkOrderEndDate());
                dashboardWorkOrderDTO.setWorkOrderStartDate(workOrder.getWorkOrderStartDate());
                dashboardWorkOrderDTO.setWorkOrderMaintenanceType(workOrder.getWorkOrderMaintenanceType());
                dashboardWorkOrderDTO.setWorkOrderName(workOrder.getWorkOrderName());
                dashboardWorkOrderDTO.setWorkOrderPriority(workOrder.getWorkOrderPriority());
                dashboardWorkOrderDTO.setWorkOrderStatusId(workOrder.getWorkOrderStatusId());
                dashboardWorkOrderDTO.setWorkOrderStatusName(workOrder.getWorkOrderStatusName());
                overDueWorkOrderList.add(dashboardWorkOrderDTO);
            }
        });
        return overDueWorkOrderList;
    }

    int numberOfOverDuePlannedWorkOrders = 0;

    @Override
    public long managerDashboardCountOverDuePlannedWorkOrders(int month) {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });

        List<DashboardOverDueWorkOrderDTO> openWorkOrderList = workOrderDao
                .managerDashboardGetOverDuePlannedWorkOrders(month, openWorkOrderStatusStringList);
        openWorkOrderList.forEach(workOrder -> {
            openWorkOrderList.forEach(workOrderDTO -> {
                if (workOrder.getWorkOrderEndDate() != null
                        && workOrder.getWorkOrderRequiredCompletionDate() != null
                        && workOrder.getWorkOrderEndDate().after(workOrder.getWorkOrderRequiredCompletionDate())) {
                    ++numberOfOverDuePlannedWorkOrders;
                }
            });
        });
        return numberOfOverDuePlannedWorkOrders;
    }

    @Override
    public PlannedWorkOrdersRatioDTO managerDashboardPlannedWorkOrdersRatio(int month) {
        long plannedWorkOrders = workOrderDao.numberOfPlannedWorkOrders(month);
        long allWorkOrders = workOrderDao.numberOfAllWorkOrders(month);
        return PlannedWorkOrdersRatioDTO.map(plannedWorkOrders, allWorkOrders);
    }

    @Override
    public List<UnScheduledWorkOrderDTO> managerDashboardUnscheduledWorkOrdersForBar() {
        return workOrderDao.managerDashboardUnscheduledWorkOrdersForBar();
    }

    @Override
    public List<PendingBarDTO> managerDashboardPendingWorkOrdersForBar() {
        List<WorkOrderStatus> pendingWorkOrderStatusList = workOrderStatusService.getPendingWorkOrderStatus();
        List<String> pendingWorkOrderStatusStringList = new ArrayList<>();
        if (pendingWorkOrderStatusList != null) {
            pendingWorkOrderStatusList.forEach(workOrderStatus -> pendingWorkOrderStatusStringList.add(workOrderStatus.getId()));
            List<AggregateResultPendingBarDTO> aggregateResultPendingBarDTOS = workOrderDao.getPendingWorkOrdersForDashboardBar(pendingWorkOrderStatusStringList);
            List<PendingBarDTO> pendingBarDTOS = new ArrayList<>();
            aggregateResultPendingBarDTOS.forEach(aggregateResultPendingBarDTO -> {
                PendingBarDTO pendingBarDTO = new PendingBarDTO();
                pendingBarDTO.setRelatedId(aggregateResultPendingBarDTO.getRelatedId());
                pendingBarDTO.setCode(aggregateResultPendingBarDTO.getCode());
                pendingBarDTO.setTitle(aggregateResultPendingBarDTO.getTitle());
                pendingBarDTO.setRequiredCompletionDate(aggregateResultPendingBarDTO.getRequiredCompletionDate());
                if (aggregateResultPendingBarDTO.getRequiredCompletionDate().after(new Date())) {
                    pendingBarDTO.setLate(true);
                } else {
                    pendingBarDTO.setLate(false);
                }
                pendingBarDTOS.add(pendingBarDTO);
            });
            return pendingBarDTOS;
        } else {
            return null;
        }
    }

    @Override
    public List<LateBarDTO> managerDashboardLateWorkOrdersForBar() {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardLateWorkOrdersForBar(openWorkOrderStatusStringList);
    }

    @Override
    public List<CurrentWeekWorkOrderDTO> managerDashboardCurrentWeekWorkOrdersBar() {
        List<WorkOrderStatus> openWorkOrderStatus = workOrderStatusService.getOpenWorkOrderStatus();
        List<String> openWorkOrderStatusStringList = new ArrayList<>();
        openWorkOrderStatus.forEach(workOrderStatus -> {
            openWorkOrderStatusStringList.add(workOrderStatus.getId());
        });
        return workOrderDao.managerDashboardCurrentWeekWorkOrdersBar(openWorkOrderStatusStringList);
    }

    @Override
    public List<CurrentWeekPlannedWorkOrderDTO> managerDashboardCurrentPlannedWorkOrdersBar() {
        return workOrderDao.managerDashboardCurrentPlannedWorkOrdersBar();
    }

    @Override
    public List<WorkOrder> getWorkOrderPriorityAndMaintenanceType(List<String> workRequestIdList) {
        return workOrderDao.getWorkOrderPriorityAndMaintenanceType(workRequestIdList);
    }

    @Override
    public void workOrderAcceptedByManager(String workOrderId, boolean workOrderAccepted) {
        workOrderDao.workOrderAcceptedByManager(workOrderId, workOrderAccepted);
    }

    @Override
    public List<Task> getTasksByWorkOrderId(String workOrderId) {
        WorkOrder workOrder = workOrderDao.getTasksByWorkOrderId(workOrderId);
        if (workOrder.getTasks() != null) {
            return taskService.getTasksOfWorkOrder(workOrder.getTasks());
        } else {
            return null;
        }
    }

    @Override
    public List<TaskGroupDTO> getTaskGroupOfWorkOrder(String workOrderId) {
        WorkOrder workOrder = workOrderDao.getTasksByWorkOrderId(workOrderId);
        if (workOrder.getTaskGroups() != null) {
            return taskGroupService.getAllTaskGroupOfWorkOrder(workOrder.getTaskGroups());
        } else {
            return null;
        }
    }

    @Override
    public Task saveTaskOfWorkOrder(Task task) {
        Task savedTask = taskService.saveTaskOfWorkOrder(task);
        workOrderDao.addTaskId(task.getId(), task.getReferenceId());
        return savedTask;
    }

    @Override
    public void deleteTaskFromWorkOrder(String workOrderId, String taskId) {
        workOrderDao.deleteTaskFromWorkOrder(workOrderId, taskId);
    }

    @Override
    public void addUsedPartToWorkOrder(String workOrderId, String partId) {
        workOrderDao.addUsedPartToWorkOrder(workOrderId, partId);
    }

    @Override
    public void deleteUsedPartFromWorkOrder(String workOrderId, String partWithUsageCountId) {
        workOrderDao.deleteUsedPartFromWorkOrder(workOrderId, partWithUsageCountId);
    }

    @Override
    public void deleteDocumentFromWorkOrder(String documentFileId, String workOrderId) {
        workOrderDao.deleteDocumentFromWorkOrder(documentFileId, workOrderId);
    }

    @Override
    public void addDocumentToWorkOrder(String workOrderId, String documentId) {
        workOrderDao.addDocumentToWorkOrder(workOrderId, documentId);
    }

    @Override
    public WorkOrder getDocumentIdListOfWorkOrder(String workOrderId) {
        return workOrderDao.getDocumentIdListOfWorkOrder(workOrderId);
    }

    @Override
    public WorkOrder getWorkOrderForRepository(String workOrderId) {
        return workOrderDao.getWorkOrderForRepository(workOrderId);
    }

    @Override
    public WorkOrder getPartWithUsageCountOfWorkOrder(String workOrderId) {
        return workOrderDao.getPartWithUsageCountOfWorkOrder(workOrderId);
    }

    @Override
    public boolean checkIfWorkOrderIsInProcess(String workOrderId) {
        return activitySampleController.checkIfWorkOrderIsInProcess(workOrderId);
    }

    @Override
    public boolean newSaveWorkOrder(NewSaveDTO newSaveDTO) {
        return workOrderDao.newSaveWorkOrder(newSaveDTO);
    }

    @Override
    public boolean newUpdate(NewSaveDTO newSaveDTO) {
        if (newSaveDTO.getEndDate() != null && newSaveDTO.getStartDate() != null) {
            long diff = newSaveDTO.getEndDate().getTime() - newSaveDTO.getStartDate().getTime();
            newSaveDTO.setFailureDuration(diff / 60000);
        }

        if (newSaveDTO.getEndDate() != null && newSaveDTO.getRepairDate() != null) {
            long diff = newSaveDTO.getEndDate().getTime() - newSaveDTO.getRepairDate().getTime();
            newSaveDTO.setRepairDuration(diff / 60000);
        }
        return workOrderDao.newUpdate(newSaveDTO);
    }

    @Override
    public boolean scheduleWorkOrderUpdate(ReqWorkOrderScheduleUpdateDTO entity) {

        return workOrderDao.scheduleWorkOrderUpdate(entity);
    }

    @Override
    public NewGetOneٔWorkOrderDTO newGetOne(String workOrderId) {
        WorkOrderPrimaryDTO workOrderPrimaryDTO = workOrderDao.newGetOne(workOrderId);
        List<GetOneUsedPart> getOneUsedPartList = new ArrayList<>();
        if (workOrderPrimaryDTO.getUsedPartList() != null) {
            List<String> partIdList = new ArrayList<>();
            workOrderPrimaryDTO.getUsedPartList().forEach(
                    usedPart -> {
                        partIdList.add(usedPart.getPartId());
                    }
            );
            List<Part> partList = partService.getUsedPartInWorkOrder(partIdList);
            partList.forEach(part -> {
                GetOneUsedPart getOneUsedPart = new GetOneUsedPart();
                getOneUsedPart.setPartId(part.getId());
                getOneUsedPart.setPartName(part.getName());
                getOneUsedPart.setPartCode(part.getPartCode());
                workOrderPrimaryDTO.getUsedPartList().forEach(usedPart -> {
                    if (usedPart.getPartId().equals(part.getId())) {
                        getOneUsedPart.setUsedNumber(usedPart.getUsedNumber());
                    }
                });
                getOneUsedPartList.add(getOneUsedPart);
            });
        }
        return NewGetOneٔWorkOrderDTO.map(workOrderPrimaryDTO, getOneUsedPartList);
    }

    @Override
    public Page<UsedPartReport> usedPartOfWOrkOrder(UsedPartOfWorkOrder usedPartOfWorkOrder, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                workOrderDao.usedPartOfWOrkOrder(usedPartOfWorkOrder, pageable, totalElement),
                pageable,
                workOrderDao.countUsedPart(usedPartOfWorkOrder)
        );
    }

    @Override
    public CountUsedPartDTO countUsedPartOfWorkOrder(UsedPartOfWorkOrder usedPartOfWorkOrder) {
        CountUsedPartDTO countUsedPartDTO = workOrderDao.countUsedPartOfWorkOrder(usedPartOfWorkOrder);
        if (countUsedPartDTO == null) {
            CountUsedPartDTO countUsedPartDTO1 = new CountUsedPartDTO();
            countUsedPartDTO1.setTotalNumber(0);
            return countUsedPartDTO1;
        } else {
            return countUsedPartDTO;
        }
    }

    @Override
    public Page<PersonnelFunctionGetAllDTO> personnelFunction(PersonnelFunctionDTO personnelFunctionDTO, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                workOrderDao.personnelFunction(personnelFunctionDTO, pageable, totalElement),
                pageable,
                workOrderDao.countPersonnelFunction(personnelFunctionDTO, pageable, totalElement)
        );
    }

    @Override
    public TotalWorkedTimeDTO TotalWorkedTimeOfPersonnel(PersonnelFunctionDTO personnelFunctionDTO) {
        TotalWorkedTimeDTO totalWorkedTimeDTO = workOrderDao.TotalWorkedTimeOfPersonnel(personnelFunctionDTO);
        if (totalWorkedTimeDTO == null) {
            TotalWorkedTimeDTO totalWorkedTimeDTO1 = new TotalWorkedTimeDTO();
            totalWorkedTimeDTO1.setTotalWorkedTime(0);
            return totalWorkedTimeDTO1;
        } else {
            return totalWorkedTimeDTO;
        }
    }

    @Override
    public List<MtbfReturn> mtbfCalculation(MtbfDTO mtbfDTO) {
//        return workOrderDao.mtbfCalculation(mtbfDTO);
        List<MtbfReturn> list = workOrderDao.mtbfCalculation(mtbfDTO);
        List<MtbfReturn> mtbfReturnList = new ArrayList<>(list);
        checkMtbfList(mtbfReturnList);
        Set<String> m = new HashSet<>();
        List<MtbfReturn> mtbfReturns = new ArrayList<>();
        mtbfReturnList.forEach(mtbfReturn -> {
            if (!m.contains(mtbfReturn.getDate())) {
                m.add(mtbfReturn.getDate());
                mtbfReturn.setWorkTimeDuration((1440 - mtbfReturn.getFailureDuration()) / mtbfReturn.getCount());
                mtbfReturns.add(mtbfReturn);
            } else {
                mtbfReturns.forEach(mtbfReturn1 -> {
                    if (mtbfReturn1.getDate().equals(mtbfReturn.getDate())) {
                        mtbfReturn1.setFailureDuration(mtbfReturn.getFailureDuration() + mtbfReturn1.getFailureDuration());
                        mtbfReturn1.setCount(mtbfReturn.getCount() + mtbfReturn1.getCount());
                        mtbfReturn1.setWorkTimeDuration((1440 - mtbfReturn1.getFailureDuration()) / mtbfReturn1.getCount());
                    }
                });
            }
        });
        Print.print("mtbfReturns", mtbfReturns);
        return mtbfReturns;
    }


    private void checkMtbfList(List<MtbfReturn> mtbfReturnList) {
        List<MtbfReturn> mtbfReturns = new ArrayList<>();
        Print.print("1111", mtbfReturnList);
        mtbfReturnList.forEach(mtbfReturn -> {
            if (mtbfReturn.getFailureDuration() > 1440) {

                long primaryFailureDuration = mtbfReturn.getFailureDuration();

                LocalDateTime now = LocalDateTime.of(mtbfReturn.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalTime.MAX);
                ZoneId defaultZoneId = ZoneId.systemDefault();
                Date endOfDay = Date.from(now.atZone(defaultZoneId).toInstant());

                long thisDayDuration = endOfDay.getTime() - mtbfReturn.getStartDate().getTime();
                mtbfReturn.setFailureDuration(thisDayDuration / 60000);
                long nextDayDuration = primaryFailureDuration - mtbfReturn.getFailureDuration();

                MtbfReturn mtbfReturn1 = new MtbfReturn();
                mtbfReturn1.setFailureDuration(nextDayDuration);
                mtbfReturn1.setCount(1);
                Date newStartDate = setNewStartDate(mtbfReturn.getStartDate());
                mtbfReturn1.setStartDate(newStartDate);
                try {
                    String date = incrementStringDate(mtbfReturn.getDate());
                    mtbfReturn1.setDate(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mtbfReturns.add(mtbfReturn1);
                Print.print("mtbfReturnList", mtbfReturnList);
            }
        });
        Print.print("22222", mtbfReturns);
        mtbfReturnList.addAll(mtbfReturns);
        Print.print("mtbfReturnList", mtbfReturnList);
        int i = (int) mtbfReturnList.stream().filter(mtbfReturn -> mtbfReturn.getFailureDuration() > 1440).count();
        Print.print("iiii", i);
        if (i > 0) {
            checkMtbfList(mtbfReturnList);
        }
    }

    private String incrementStringDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(date));
        c.add(Calendar.DATE, 1);
        return sdf.format(c.getTime());
    }

    private Date setNewStartDate(Date startDate) {
        LocalDateTime start = LocalDateTime.of(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalTime.MIN);
        LocalDateTime localStartDate = start.plusDays(1);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(localStartDate.atZone(defaultZoneId).toInstant());
    }

    @Override
    public List<MttrReturn> mttrCalculation(MttrDTO mttrDTO) {
        return workOrderDao.mttrCalculation(mttrDTO);
    }

    @Override
    public List<MtbfTableReturn> mtbfTable(MtbfTableDTO mtbfTableDTO) {
        long number = (this.daysBetweenTwoDates(mtbfTableDTO) * 24 * 60) + 60 * 24;
        mtbfTableDTO.setNumber(number);
        List<MtbfTableReturn> mtbfTableReturns = workOrderDao.mtbfTable(mtbfTableDTO);
        List<String> assetIdList = new ArrayList<>();
        List<MtbfTableReturn> mtbfTableReturnList = new ArrayList<>();
        mtbfTableReturnList.addAll(mtbfTableReturns);
        mtbfTableReturns.forEach(mtbfTableReturn -> {
            assetIdList.add(mtbfTableReturn.getAssetId());
        });
        List<Asset> assetList = assetService.getAllAssetsName();
        assetList.forEach(asset -> {
                    if (!assetIdList.contains(asset.getId())) {
                        MtbfTableReturn mtbfTableReturn = new MtbfTableReturn();
                        mtbfTableReturn.setAssetId(asset.getId());
                        mtbfTableReturn.setAssetName(asset.getName());
                        mtbfTableReturn.setAssetCode(asset.getCode());
                        mtbfTableReturn.setFailureDuration(0);
                        mtbfTableReturn.setRepairDuration(0);
                        mtbfTableReturn.setCount(0);
                        mtbfTableReturn.setMtbf(null);
                        mtbfTableReturnList.add(mtbfTableReturn);
                    }
                }
        );
        mtbfTableReturnList.forEach(mtbfTableReturn -> {
            if (mtbfTableReturn.getMtbf() != null && mtbfTableReturn.getMtbf() < 0) {
                mtbfTableReturn.setMtbf((long) 0);
            }
            if (mtbfTableReturn.getFailureDuration() > number) {
                mtbfTableReturn.setFailureDuration(number);
            }
            if (mtbfTableReturn.getRepairDuration() > number) {
                mtbfTableReturn.setRepairDuration(number);
            }
        });
        return mtbfTableReturnList;
    }

    private long daysBetweenTwoDates(MtbfTableDTO mtbfTableDTO) {
        long diff = mtbfTableDTO.getUntil().getTime() - mtbfTableDTO.getFrom().getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }


    @Override
    public List<MttrTableReturn> mttrTable(MttrTableDTO mttrTableDTO) {
        long number = (this.daysBetweenTwoDatesForMttr(mttrTableDTO) * 24 * 60) + 60 * 24;
        List<MttrTableReturn> mttrTableReturnList = workOrderDao.mttrTable(mttrTableDTO);
        mttrTableReturnList.forEach(mttrTableReturn -> {
            if (mttrTableReturn.getMttr() != null && mttrTableReturn.getMttr() > number) {
                mttrTableReturn.setMttr(number);
            }
        });
        return mttrTableReturnList;
    }

    private long daysBetweenTwoDatesForMttr(MttrTableDTO mttrTableDTO) {
        long diff = mttrTableDTO.getUntil().getTime() - mttrTableDTO.getFrom().getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isAcceptedByManager(String workOrderId) {
        WorkOrder workOrder = workOrderDao.isAcceptedByManager(workOrderId);
        boolean bool;
        if (workOrder.isAcceptedByManager()) {
            bool = false;
        } else {
            bool = true;
            workRequestService.setAssessmentTrue(workOrder.getWorkRequestId());
        }
        return bool;
    }

    @Override
    public WorkOrder getWorkOrderTechnicians(String workRequestId) {
        return workOrderDao.getWorkOrderTechnicians(workRequestId);
    }

    @Override
    public List<WorkOrderDTO> getAllWorkOrderForExcel(WorkOrderDTO workOrderDTO) {
        return workOrderDao.getAllWorkOrderForExcel(workOrderDTO);
    }

    @Override
    public List<MdtReturn> mdtCalculation(MdtDTO mdtDTO) {
        return workOrderDao.mdtCalculation(mdtDTO);
    }

    @Override
    public List<MdtTableReturn> mdtTable(MdtTableDTO mdtTableDTO) {
        long number = (this.daysBetweenTwoDatesForMdt(mdtTableDTO) * 24 * 60) + 60 * 24;
        List<MdtTableReturn> mdtTableReturns = workOrderDao.mdtTable(mdtTableDTO);
        mdtTableReturns.forEach(mdtTableReturn -> {
            if (mdtTableReturn.getMdt() > number) {
                mdtTableReturn.setMdt(number);
            }
        });
        return mdtTableReturns;
    }

    @Override
    public WorkOrder createWorkOrderAccordingToAssociatedWorkRequest(WorkOrderSchedule entity) {
        return workOrderDao.createWorkOrderAccordingToAssociatedWorkRequest(entity);

    }

    @Override
    public Map<String, Object> getOneScheduleWorkOrder(String workOrderId) {
        WorkOrderScheduleDTO res = workOrderDao.getOneScheduleWorkOrder(workOrderId);

        List<String> partList = new ArrayList<>();
        res.getUsedPartList().forEach(a -> {
            partList.add(a.getPartId());
        });
        List<Part> usePart = partService.getAllByIdList(partList);
        Map<String, Object> result = new HashMap<>();
        result.put("res", res);
        result.put("usePart", usePart);
        return result;
    }

    @Override
    public boolean updateScheduleWorkOrder(WorkOrderScheduleDTO workOrderScheduleDTO) {
        if (workOrderScheduleDTO.getMainSubSystemId().equals("")) {
            workOrderScheduleDTO.setMainSubSystemId(null);
        }
        return workOrderDao.updateScheduleWorkOrder(workOrderScheduleDTO);
    }

    @Override
    public void deleteWorkOrderSchedule(String id) {
        workOrderDao.deleteWorkOrderSchedule(id);
    }

    @Override
    public void setEndDateOfWorkOrderSchedule(String workOrderId) {
        workOrderDao.setEndDateOfWorkOrderSchedule(workOrderId);
    }

    private long daysBetweenTwoDatesForMdt(MdtTableDTO mdtTableDTO) {
        long diff = mdtTableDTO.getUntil().getTime() - mdtTableDTO.getFrom().getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

}