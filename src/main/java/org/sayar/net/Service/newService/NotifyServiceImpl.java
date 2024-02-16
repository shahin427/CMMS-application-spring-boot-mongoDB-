package org.sayar.net.Service.newService;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Dao.NewDao.NotifyDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.NotifyDTO;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Notify;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintananceService;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;
import org.sayar.net.Service.NotificationService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.sayar.net.Model.newModel.Enum.NotifyEvent.ONONLINEOFFLINE;
import static org.sayar.net.Model.newModel.Enum.NotifyEvent.ONSTATUSCHANG;


@Service
public class NotifyServiceImpl extends GeneralServiceImpl<Notify> implements NotifyService {

    @Autowired
    private NotifyDao notifyDao;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WorkOrderStatusService workOrderStatusService;

    @Autowired
    private ScheduleMaintananceService scheduleMaintananceService;

    @Autowired
    private MongoOperations mongoOperations;

//    @Autowired
//    public NotifyServiceImpl(@Lazy AssetService assetService) {
//        this.assetService = assetService;
//    }

    @Override
    public Notify getOneById(String notifyId) {
        return notifyDao.getOneById(notifyId);
    }

    @Override
    public Notify postNotify(Notify notify) {
        Notify savedNotify = notifyDao.postNotify(notify);
        WorkOrder workOrder = workOrderService.getWorkOrderByNotifyReferenceId(savedNotify.getReferenceId());
        Asset asset = assetService.getOneAsset(workOrder.getAssetId());
        savedNotify.getEvents().forEach(notifyEvent -> {
            if (notifyEvent.equals(ONONLINEOFFLINE)) {
                notificationService.createNotificationForSpecifiedUsersOfWorkOrder(notify.getUser(), asset.getStatus(), asset.getName(), asset.getCode(), workOrder);
            }
            if (notifyEvent.equals(ONSTATUSCHANG)) {
                WorkOrderStatus workOrderStatus = workOrderStatusService.getWorkOrderStatusById(workOrder.getStatusId());
                if (workOrderStatus != null)
                    notificationService.createNotificationForSpecifiedUsersOfWorkOrder(notify.getUser(), asset.getName(), workOrderStatus.getStatus(), asset.getCode(), workOrder);
            }
        });
        return savedNotify;
    }

    @Override
    public boolean updateNotify(Notify notify, String notifyId) {
        Notify oldVersionNotify = notifyDao.getOldVersionNotify(notifyId);
        Print.print("oldVersionNotify", oldVersionNotify);
        Notify newVersionNotify = notifyDao.newVersionNotify(notify, notifyId);
        Print.print("newVersionNotify", newVersionNotify);
        Print.print("notify", notify);
        if (!newVersionNotify.getUser().getId().equals(oldVersionNotify.getUser().getId())) {
            WorkOrder workOrder = workOrderService.getWorkOrderByNotifyReferenceId(newVersionNotify.getReferenceId());
            Print.print("workOrder", workOrder);
            Asset asset = assetService.getOneAsset(workOrder.getAssetId());
            Print.print("assett", asset);
            newVersionNotify.getEvents().forEach(notifyEvent -> {
                if (notifyEvent.equals(ONONLINEOFFLINE)) {
                    notificationService.createNotificationForSpecifiedUsersOfWorkOrder(notify.getUser(), asset.getStatus(), asset.getName(), asset.getCode(), workOrder);
                }
                if (notifyEvent.equals(ONSTATUSCHANG)) {
                    WorkOrderStatus workOrderStatus = workOrderStatusService.getWorkOrderStatusById(workOrder.getStatusId());
                    notificationService.createNotificationForSpecifiedUsersOfWorkOrder(notify.getUser(), asset.getName(), workOrderStatus.getStatus(), asset.getCode(), workOrder);
                }
            });
        }
        return newVersionNotify.equals(notify);
    }


    public List<Notify> getAllNotifyByNotifyIdList(List<String> notifications) {
        return notifyDao.getAllNotifyByNotifyIdList(notifications);
    }

    @Override
    public List<NotifyDTO> getNotifyListByReferenceId(String referenceId) {
        List<Notify> notifyList = notifyDao.getNotifyListByReferenceId(referenceId);
        return NotifyDTO.map(notifyList);
    }

    @Override
    public void createNotifyByUserAssignedIdOfWorkOrder(User user, String workOrderId) {
        notifyDao.createNotifyByUserAssignedIdOfWorkOrder(user, workOrderId);
    }

    @Override
    public boolean updateNotifyOfScheduleMaintenance(Notify notify) {
//        Notify oldVersionNotify = notifyDao.getOldVersionNotify(notify.getId());
        return this.updateResultStatus(notifyDao.updateNotify(notify, notify.getId()));

//        if (!newVersionNotify.getUser().getId().equals(oldVersionNotify.getUser().getId())) {
//            ScheduleMaintenance scheduleMaintenance = scheduleMaintananceService.getScheduleMaintenanceOfNotify(notify.getReferenceId());
//            Asset asset = assetService.getOneAsset(scheduleMaintenance.getAssetId());
//            newVersionNotify.getEvents().forEach(notifyEvent -> {
//                if (notifyEvent.equals(ONONLINEOFFLINE)) {
//                    notificationService.createNotificationForSpecifiedUsersOfScheduleMaintenance(notify.getUser(), asset.isStatus(), asset.getName(), asset.getCode());
//                }
//                if (notifyEvent.equals(ONSTATUSCHANG)) {
//                    WorkOrderStatus workOrderStatus = workOrderStatusService.getWorkOrderStatusById(scheduleMaintenance.getStatusId());
//                    notificationService.createNotificationForSpecifiedUsersOfScheduleMaintenanceInUpdate
//                            (notify.getUser(), asset.getName(), workOrderStatus.getStatus(), asset.getCode());
//                }
//            });
//        }
//        return true;
    }

    @Override
    public Notify saveScheduleMaintenanceNotify(Notify notify) {
        return notifyDao.saveScheduleMaintenanceNotify(notify);
//        if (notify.getReferenceId() != null) {
//            ScheduleMaintenance scheduleMaintenance = scheduleMaintananceService.getScheduleMaintenanceOfNotify(notify.getReferenceId());
//            if (scheduleMaintenance.getAssetId() != null) {
//                Asset scheduleMaintenanceAsset = assetService.getOneAsset(scheduleMaintenance.getAssetId());
//                notify.getEvents().forEach(notifyEvent -> {
//                    if (notifyEvent.equals(ONONLINEOFFLINE)) {
//                        notificationService.createNotificationForSpecifiedUsersOfScheduleMaintenanceInPost(notify.getUser(), scheduleMaintenanceAsset.isStatus(), scheduleMaintenanceAsset.getName(), scheduleMaintenanceAsset.getCode());
//                    }
//
//                    if (notifyEvent.equals(ONSTATUSCHANG)) {
//                        WorkOrderStatus workOrderStatus = workOrderStatusService.getWorkOrderStatusById(scheduleMaintenance.getStatusId());
//                        notificationService.createNotificationForSpecifiedUsersOfScheduleMaintenanceInPostForWorkOrderStatusChange
//                                (notify.getUser(), scheduleMaintenanceAsset.getName(), workOrderStatus.getStatus(), scheduleMaintenanceAsset.getCode());
//                    }
//                });
//            }
//        }
//        return notify1;
    }

    @Override
    public List<Notify> getAllNotifyOfTheWorkOrderByNotifyId(String id) {
        return notifyDao.getAllNotifyOfTheWorkOrderByNotifyId(id);
    }

    @Override
    public List<Notify> getNotifyListOfTheScheduleMaintenanceBackupById(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("referenceId").is(scheduleMaintenanceId));
        query.fields()
                .include("user")
                .include("id");
        return mongoOperations.find(query, Notify.class);
    }

}
