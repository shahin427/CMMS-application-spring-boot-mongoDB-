package org.sayar.net.Scheduler;

import org.sayar.net.Enumes.AssetStatus;
import org.sayar.net.Enumes.RequestStatus;
import org.sayar.net.Model.ActivitySample;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.WorkOrderSchedule;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Service.Mongo.activityServices.activity.ActivityService;
import org.sayar.net.Service.WorkOrderSchedule.WorkOrderScheduleService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.sayar.net.Model.WorkOrderSchedule.Frequency.*;
import static org.sayar.net.Model.WorkOrderSchedule.Mode.FIXED;

/**
 * @author masoud
 */
@Component

public class Schedule {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private WorkOrderScheduleService workOrderScheduleService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private MongoOperations mongoOperations;

    @Scheduled(cron = "0 01 00 * * *")
    private void memoryErasureSchedule() {

        List<WorkOrderSchedule> res = workOrderScheduleService.getAll();
        res.forEach(a -> {
            WorkOrder workOrder = workOrderService.createWorkOrderAccordingToAssociatedWorkRequest(a);
            ActivitySample activitySample = createActivitySampleBySchedule(
                    a.getId(), a.getActivityId(), workOrder.getId(), a.getAssetId(),
                    a.getActivityTypeId(), a.getWorkCategoryId(), a.getImportanceDegreeId(),
                    a.getMainSubSystemId(), a.getAssetStatus(), workOrder.getMinorSubSystem(), workOrder.getStartDate());

            changeMentionedActivityFirsStepStatus(activitySample);
            if (a.getMode().equals(FIXED)) {
                updateNextDate(a);
            } else {
                workOrderScheduleService.makeNextDateNull(a.getId());   
            }
        });
    }


    private void updateNextDate(WorkOrderSchedule entity) {
        Date nextDate = this.setNextDate(entity);

        if (nextDate.before(entity.getEndDate())) {
            workOrderScheduleService.updateNextDate(entity.getId(), nextDate);
        } else {
            workOrderScheduleService.deActiveWorkOrderSchedule(entity.getId());
        }
    }

    private Date setNextDate(WorkOrderSchedule entity) {
        Calendar cal = Calendar.getInstance();
        if (entity.getFrequency().equals(DAILY)) {
            cal.setTime(entity.getNexDate());
            cal.add(Calendar.DAY_OF_MONTH, entity.getPer());
        }

        if (entity.getFrequency().equals(WEEKLY)) {
            cal.setTime(entity.getNexDate());
            cal.add(Calendar.WEEK_OF_MONTH, entity.getPer());
        }

        if (entity.getFrequency().equals(MONTHLY)) {
            cal.setTime(entity.getNexDate());
            cal.add(Calendar.MONTH, entity.getPer());
        }

        if (entity.getFrequency().equals(YEARLY)) {
            cal.setTime(entity.getNexDate());
            cal.add(Calendar.YEAR, entity.getPer());
        }

        return cal.getTime();
    }


    public void changeMentionedActivityFirsStepStatus(ActivitySample activitySample) {
        if (activitySample != null) {
            activitySample.getActivityLevelList().get(0).setActionLevel("accepted");
            activitySample.getActivityLevelList().get(0).getNextActivityLevel().setActionLevel("pending");
            int nextActivityLevel = Integer.parseInt(activitySample.getActivityLevelList().get(0).getNextActivityLevel().getId());
            activitySample.getActivityLevelList().get(nextActivityLevel).setActionLevel("pending");
            // setting pending date of next activityLevel
            activitySample.getActivityLevelList().get(nextActivityLevel).setPendingDate(new Date());
            mongoOperations.save(activitySample);
        }
    }

    public ActivitySample createActivitySampleBySchedule(
            String scheduleId,
            String activityId,
            String workOrderId,
            String assetId,
            String activityTypeId,//نوع فعالیت
            String workCategoryId,// رسته کاری
            String importanceDegreeId,//درجه  اهمیت
            String mainSubSystemId,
            AssetStatus assetStatus,
            String minorSubSystem,
            Date startDate) {

        Activity activity = activityService.getActivityByActivityId(activityId);
        ActivitySample activitySample = new ActivitySample();

        if (assetId != null)
            activitySample.setAssetId(assetId);
        if (workOrderId != null)
            activitySample.setWorkOrderId(workOrderId);
        if (activityId != null)
            activitySample.setRelatedActivityId(activityId);

        activitySample.setActivityTypeId(activityTypeId);
        activitySample.setFromSchedule(true);
        activitySample.setMainSubSystemId(mainSubSystemId);
        activitySample.setMinorSubSystem(minorSubSystem);
        activitySample.setWorkCategoryId(workCategoryId);
        activitySample.setImportanceDegreeId(importanceDegreeId);
        activitySample.setAssetStatus(assetStatus);
        activitySample.setTitle(activity.getTitle());
        activitySample.setScheduleId(scheduleId);
        activitySample.setActivityLevelList(activity.getActivityLevelList());
        activitySample.setActivityInstanceId(UUID.randomUUID().toString());
        activitySample.getActivityLevelList().get(1).setRequestStatus(RequestStatus.NEW_REQUEST);
        int lastActivityLevel = activitySample.getActivityLevelList().size() - 1;
        activitySample.getActivityLevelList().get(lastActivityLevel).setActionLevel("finish");
        activitySample.setCreationDateOfWorkRequest(startDate);

        return mongoOperations.save(activitySample);
    }


}
