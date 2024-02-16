package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.dao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Controller.ActivitySampleController;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.ScheduleActivityDTO;
import org.sayar.net.Model.DTO.ScheduleMaintenanceCreateDTO;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.*;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintananceService;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Service.Mongo.activityServices.activity.ActivityService;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.newService.AssetService;
import org.sayar.net.Service.newService.MessageService;
import org.sayar.net.Service.newService.NotifyService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.TimeType.*;

@Repository

public class ScheduleMaintenanceBackupDaoImp extends GeneralDaoImpl<ScheduleMaintenanceBackup> implements ScheduleMaintenanceBackupDao {
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private AssetService assetService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivitySampleController activitySampleController;
    @Autowired
    private ScheduleMaintananceService scheduleMaintananceService;


    public ScheduleMaintenanceBackupDaoImp(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public List<ScheduleMaintenanceBackup> getAllScheduleMaintenanceBackup(List<ScheduleMaintenance> scheduleMaintenanceList) {

        List<ScheduleMaintenanceBackup> scheduleMaintenanceBackupList = new ArrayList<>();
        scheduleMaintenanceList.forEach(scheduleMaintenance -> {
            ScheduleMaintenanceBackup scheduleMaintenanceBackup = new ScheduleMaintenanceBackup();

            if (scheduleMaintenance.getId() != null)
                scheduleMaintenanceBackup.setScheduleMaintenanceId(scheduleMaintenance.getId());

            if (scheduleMaintenance.getAssetId() != null)
                scheduleMaintenanceBackup.setAssetId(scheduleMaintenance.getAssetId());

            if (scheduleMaintenance.getScheduleWithTimeAndMetering() != null)
                scheduleMaintenanceBackup.setPer(scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduledTime().getPer());

            if (scheduleMaintenance.getScheduleWithTimeAndMetering() != null)
                scheduleMaintenanceBackup.setStartDate(scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduledTime().getStartOn());

            if (scheduleMaintenance.getScheduleWithTimeAndMetering() != null)
                scheduleMaintenanceBackup.setEndDate(scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduledTime().getEndOn());

            scheduleMaintenanceBackupList.add(scheduleMaintenanceBackup);
        });
        return (List<ScheduleMaintenanceBackup>) mongoOperations.insertAll(scheduleMaintenanceBackupList);
    }

    @Override
    public List<ScheduleMaintenanceBackup> checkIfScheduleMaintenanceBackupStartTimeArrived() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("startDate").in(nowAsISO));
        return mongoOperations.find(query, ScheduleMaintenanceBackup.class);
    }

    @Override
    public List<ScheduleMaintenanceBackup> generateNewScheduleMaintenanceBackup(List<ScheduleMaintenanceBackup> scheduleMaintenanceBackup) {
        List<ScheduleMaintenanceBackup> scheduleMaintenanceBackup1 = new ArrayList<>();
        scheduleMaintenanceBackup.forEach(scheduleMaintenanceBackup2 -> {
            ScheduleMaintenanceBackup scheduleMaintenanceBackup3 = new ScheduleMaintenanceBackup();
            scheduleMaintenanceBackup3.setAssetId(scheduleMaintenanceBackup2.getAssetId());
            scheduleMaintenanceBackup3.setPer(scheduleMaintenanceBackup2.getPer());
            scheduleMaintenanceBackup3.setScheduleMaintenanceId(scheduleMaintenanceBackup2.getScheduleMaintenanceId());
            scheduleMaintenanceBackup3.setScheduledFixType(scheduleMaintenanceBackup2.getScheduledFixType());
            scheduleMaintenanceBackup3.setCycle(scheduleMaintenanceBackup2.getCycle());

            if (scheduleMaintenanceBackup2.getCycle().equals(MONTHLY)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(scheduleMaintenanceBackup2.getStartDate());
                cal.add(Calendar.MONTH, scheduleMaintenanceBackup2.getPer());
                Date datePlusMonth = cal.getTime();
                scheduleMaintenanceBackup3.setStartDate(datePlusMonth);
                scheduleMaintenanceBackup1.add(scheduleMaintenanceBackup3);
            }
            if (scheduleMaintenanceBackup2.getCycle().equals(DAILY)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(scheduleMaintenanceBackup2.getStartDate());
                cal.add(Calendar.DAY_OF_MONTH, scheduleMaintenanceBackup2.getPer());
                Date datePlusMonth = cal.getTime();
                scheduleMaintenanceBackup3.setStartDate(datePlusMonth);
                scheduleMaintenanceBackup1.add(scheduleMaintenanceBackup3);
            }
            if (scheduleMaintenanceBackup2.getCycle().equals(YEARLY)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(scheduleMaintenanceBackup2.getStartDate());
                cal.add(Calendar.YEAR, scheduleMaintenanceBackup2.getPer());
                Date datePlusMonth = cal.getTime();
                scheduleMaintenanceBackup3.setStartDate(datePlusMonth);
                scheduleMaintenanceBackup1.add(scheduleMaintenanceBackup3);
            }
            if (scheduleMaintenanceBackup2.getCycle().equals(WEEKLY)) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(scheduleMaintenanceBackup2.getStartDate());
                cal.add(Calendar.WEEK_OF_MONTH, scheduleMaintenanceBackup2.getPer());
                Date datePlusMonth = cal.getTime();
                scheduleMaintenanceBackup3.setStartDate(datePlusMonth);
                scheduleMaintenanceBackup1.add(scheduleMaintenanceBackup3);
            }
            mongoOperations.save(scheduleMaintenanceBackup1);
        });
        return scheduleMaintenanceBackup1;
    }

    @Override
    public ScheduleMaintenanceBackup createScheduleMaintenanceBackUpByScheduleMaintenance(ScheduleMaintenance scheduleMaintenance) {

        ScheduleMaintenanceBackup scheduleMaintenanceBackup = new ScheduleMaintenanceBackup();

        if (scheduleMaintenance.getPriority() != null)
            scheduleMaintenanceBackup.setPriority(scheduleMaintenance.getPriority());

        if (scheduleMaintenance.getCode() != null)
            scheduleMaintenanceBackup.setCode(scheduleMaintenance.getCode());

        if (scheduleMaintenance.getTitle() != null)
            scheduleMaintenanceBackup.setTitle(scheduleMaintenance.getTitle());

        if (scheduleMaintenance.getId() != null)
            scheduleMaintenanceBackup.setScheduleMaintenanceId(scheduleMaintenance.getId());

        if (scheduleMaintenance.getAssetId() != null)
            scheduleMaintenanceBackup.setAssetId(scheduleMaintenance.getAssetId());

        if (scheduleMaintenance.getProjectId() != null)
            scheduleMaintenanceBackup.setProjectId(scheduleMaintenance.getProjectId());

        if (scheduleMaintenance.getMaintenanceType() != null)
            scheduleMaintenanceBackup.setMaintenanceType(scheduleMaintenance.getMaintenanceType());

        if (scheduleMaintenance.getImage() != null)
            scheduleMaintenanceBackup.setImage(scheduleMaintenance.getImage());

        if (scheduleMaintenance.getActivityId() != null)
            scheduleMaintenanceBackup.setActivityId(scheduleMaintenance.getActivityId());

        if (scheduleMaintenance.getStatusId() != null)
            scheduleMaintenanceBackup.setStatusId(scheduleMaintenance.getStatusId());

        scheduleMaintenanceBackup.setActive(true);

        return mongoOperations.save(scheduleMaintenanceBackup);

    }

    @Override
    public UpdateResult updateScheduleMaintenanceBackUp(String scheduleMaintenanceId, ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO) {

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("assetId", scheduleMaintenanceCreateDTO.getAssetId());
        update.set("code", scheduleMaintenanceCreateDTO.getCode());
        update.set("title", scheduleMaintenanceCreateDTO.getTitle());
        update.set("image", scheduleMaintenanceCreateDTO.getImage());
        update.set("projectId", scheduleMaintenanceCreateDTO.getProjectId());
        update.set("priority", scheduleMaintenanceCreateDTO.getPriority());
        update.set("maintenanceType", scheduleMaintenanceCreateDTO.getMaintenanceType());
        update.set("statusId", scheduleMaintenanceCreateDTO.getStatusId());
        update.set("active", scheduleMaintenanceCreateDTO.isActive());
        update.set("activityId", scheduleMaintenanceCreateDTO.getActivityId());
        return mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void updateScheduleMaintenanceBackUpTimeAndMeteringByScheduleMaintenance
            (String scheduleMaintenanceId, ScheduleWithTimeAndMetering scheduleWithTimeAndMetering,
             long numberOfDayForEndingEachScheduleMaintenance) {

        if (scheduleWithTimeAndMetering.getMeteringCycle().getStartDistance() +
                scheduleWithTimeAndMetering.getMeteringCycle().getPer() < scheduleWithTimeAndMetering.getMeteringCycle().getEndDistance()) {
            Query query = new Query();
            query.addCriteria(Criteria.where("deleted").ne(true));
            query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));

            Update update = new Update();
            update.set("cycle", scheduleWithTimeAndMetering.getScheduledTime().getCycle());
            update.set("perTime", scheduleWithTimeAndMetering.getScheduledTime().getPer());
            update.set("startDate", scheduleWithTimeAndMetering.getScheduledTime().getStartOn());
            update.set("endDate", scheduleWithTimeAndMetering.getScheduledTime().getEndOn());
            update.set("scheduledFixType", scheduleWithTimeAndMetering.getScheduledTime().getFixType());
            update.set("per", scheduleWithTimeAndMetering.getMeteringCycle().getPer());
            update.set("endDistance", scheduleWithTimeAndMetering.getMeteringCycle().getEndDistance());
            update.set("startDistance", scheduleWithTimeAndMetering.getMeteringCycle().getStartDistance());
            update.set("fixType", scheduleWithTimeAndMetering.getMeteringCycle().getFixType());
            update.set("unitOfMeasurementId", scheduleWithTimeAndMetering.getMeteringCycle().getUnitOfMeasurementId());
            update.set("scheduleType", scheduleWithTimeAndMetering.getScheduleType());
            update.set("nextTriggerThreshold", scheduleWithTimeAndMetering.getMeteringCycle().getStartDistance() +
                    scheduleWithTimeAndMetering.getMeteringCycle().getPer());
            update.set("numberOfDayForEndingEachScheduleMaintenance", numberOfDayForEndingEachScheduleMaintenance);
            update.set("startDateOfMainScheduleMaintenanceProject", new Date());
            update.set("unitOfMeasurementId", scheduleWithTimeAndMetering.getMeteringCycle().getUnitOfMeasurementId());
            mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
        }
    }

    @Override
    public List<String> checkScheduleMaintenanceBackupForGeneratingNewOne(List<ScheduleMaintenanceBackup> scheduleMaintenanceBackupList) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        List<ScheduleMaintenanceBackup> subsequentScheduleMaintenanceBackups = new ArrayList<>();
        List<String> scheduleMaintenanceIdListForDelete = new ArrayList<>();
        List<ScheduleMaintenanceBackup> meteringScheduleMaintenanceBackupList = new ArrayList<>();

        for (ScheduleMaintenanceBackup scheduleMaintenanceBackup : scheduleMaintenanceBackupList) {

            ScheduleMaintenanceBackup scheduleMaintenanceBackup1 = new ScheduleMaintenanceBackup();

            /**
             * below codes are for setting startTime for subsequent triggers
             */
            Date nextTrigger = null;
            if (scheduleMaintenanceBackup.getCycle().equals(DAILY)) {
                LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                localDateTime = localDateTime.plusDays(scheduleMaintenanceBackup.getPerTime());
                Date currentDatePlusDays = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                try {
                    nextTrigger = simpleDateFormat.parse(simpleDateFormat.format(currentDatePlusDays));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                scheduleMaintenanceBackup1.setStartDate(nextTrigger);
            }

            if (scheduleMaintenanceBackup.getCycle().equals(WEEKLY)) {
                LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                localDateTime = localDateTime.plusWeeks(scheduleMaintenanceBackup.getPerTime());
                Date currentDatePlusWeeks = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                try {
                    nextTrigger = simpleDateFormat.parse(simpleDateFormat.format(currentDatePlusWeeks));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                scheduleMaintenanceBackup1.setStartDate(nextTrigger);
            }

            if (scheduleMaintenanceBackup.getCycle().equals(MONTHLY)) {
                LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                localDateTime = localDateTime.plusMonths(scheduleMaintenanceBackup.getPerTime());
                Date currentDatePlusMonths = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

                try {
                    nextTrigger = simpleDateFormat.parse(simpleDateFormat.format(currentDatePlusMonths));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                scheduleMaintenanceBackup1.setStartDate(nextTrigger);
            }

            if (scheduleMaintenanceBackup.getCycle().equals(YEARLY)) {
                LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                localDateTime = localDateTime.plusYears(scheduleMaintenanceBackup.getPerTime());
                Date currentDatePlusYears = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                try {
                    nextTrigger = simpleDateFormat.parse(simpleDateFormat.format(currentDatePlusYears));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                scheduleMaintenanceBackup1.setStartDate(nextTrigger);
            }

            /**
             *  next scheduleMaintenanceBackup startTime shouldn't be after the main endDate of scheduleMaintenanceBackup (endDate of scheduleMaintenance)
             */
            if (scheduleMaintenanceBackup1.getStartDate().before(scheduleMaintenanceBackup.getEndDate())
                    || scheduleMaintenanceBackup1.getStartDate().equals(scheduleMaintenanceBackup.getEndDate())) {

                scheduleMaintenanceBackup1.setPer(scheduleMaintenanceBackup.getPer());
                scheduleMaintenanceBackup1.setUnitOfMeasurementId(scheduleMaintenanceBackup.getUnitOfMeasurementId());
                scheduleMaintenanceBackup1.setStartDistance(scheduleMaintenanceBackup.getStartDistance());
                scheduleMaintenanceBackup1.setEndDistance(scheduleMaintenanceBackup.getEndDistance());
                scheduleMaintenanceBackup1.setNextTriggerThreshold(scheduleMaintenanceBackup.getNextTriggerThreshold());

                scheduleMaintenanceBackup1.setPerTime(scheduleMaintenanceBackup.getPerTime());
                scheduleMaintenanceBackup1.setEndDate(scheduleMaintenanceBackup.getEndDate());
                scheduleMaintenanceBackup1.setScheduleMaintenanceId(scheduleMaintenanceBackup.getScheduleMaintenanceId());
                scheduleMaintenanceBackup1.setAssetId(scheduleMaintenanceBackup.getAssetId());
                scheduleMaintenanceBackup1.setIssueSummary(scheduleMaintenanceBackup.getIssueSummary());
                scheduleMaintenanceBackup1.setCycle(scheduleMaintenanceBackup.getCycle());
                scheduleMaintenanceBackup1.setScheduledFixType(scheduleMaintenanceBackup.getScheduledFixType());
                scheduleMaintenanceBackup1.setTaskIdList(scheduleMaintenanceBackup.getTaskIdList());
                scheduleMaintenanceBackup1.setProjectId(scheduleMaintenanceBackup.getProjectId());
                scheduleMaintenanceBackup1.setMaintenanceType(scheduleMaintenanceBackup.getMaintenanceType());
                scheduleMaintenanceBackup1.setPriority(scheduleMaintenanceBackup.getPriority());
                scheduleMaintenanceBackup1.setStatusId(scheduleMaintenanceBackup.getStatusId());
                scheduleMaintenanceBackup1.setActualLaborHour(scheduleMaintenanceBackup.getLaborHour());
                scheduleMaintenanceBackup1.setAdminNote(scheduleMaintenanceBackup.getAdminNote());
                scheduleMaintenanceBackup1.setDocumentFile(scheduleMaintenanceBackup.getDocumentFile());
                scheduleMaintenanceBackup1.setFixType(scheduleMaintenanceBackup.getFixType());
                scheduleMaintenanceBackup1.setLaborHour(scheduleMaintenanceBackup.getLaborHour());
                scheduleMaintenanceBackup1.setProblem(scheduleMaintenanceBackup.getProblem());
                scheduleMaintenanceBackup1.setRootCause(scheduleMaintenanceBackup.getRootCause());
                scheduleMaintenanceBackup1.setWorkInstruction(scheduleMaintenanceBackup.getWorkInstruction());
                scheduleMaintenanceBackup1.setTaskList(scheduleMaintenanceBackup.getTaskList());
                scheduleMaintenanceBackup1.setSolution(scheduleMaintenanceBackup.getSolution());
                scheduleMaintenanceBackup1.setNote(scheduleMaintenanceBackup.getNote());
                scheduleMaintenanceBackup1.setExpired(scheduleMaintenanceBackup.isExpired());
                scheduleMaintenanceBackup1.setUserAssignedId(scheduleMaintenanceBackup.getUserAssignedId());
                scheduleMaintenanceBackup1.setCompletedUserId(scheduleMaintenanceBackup.getCompletedUserId());
                scheduleMaintenanceBackup1.setStatusId(scheduleMaintenanceBackup.getStatusId());
                scheduleMaintenanceBackup1.setBudgetId(scheduleMaintenanceBackup.getBudgetId());
                scheduleMaintenanceBackup1.setChargeDepartmentId(scheduleMaintenanceBackup.getChargeDepartmentId());
                scheduleMaintenanceBackup1.setCode(scheduleMaintenanceBackup.getCode());
                scheduleMaintenanceBackup1.setTitle(scheduleMaintenanceBackup.getTitle());
                scheduleMaintenanceBackup1.setStartDateOfMainScheduleMaintenanceProject(scheduleMaintenanceBackup.getStartDateOfMainScheduleMaintenanceProject());
                scheduleMaintenanceBackup1.setScheduleMaintenanceId(scheduleMaintenanceBackup.getScheduleMaintenanceId());
                scheduleMaintenanceBackup1.setNumberOfDayForEndingEachScheduleMaintenance(scheduleMaintenanceBackup.getNumberOfDayForEndingEachScheduleMaintenance());
                scheduleMaintenanceBackup1.setActivityId(scheduleMaintenanceBackup.getActivityId());
                scheduleMaintenanceBackup1.setActive(true);
                scheduleMaintenanceBackup1.setUsedParts(scheduleMaintenanceBackup.getUsedParts());
                scheduleMaintenanceBackup1.setTaskGroupList(scheduleMaintenanceBackup.getTaskGroupList());
                scheduleMaintenanceBackup1.setDocumentsList(scheduleMaintenanceBackup.getDocumentsList());
                subsequentScheduleMaintenanceBackups.add(scheduleMaintenanceBackup1);
            } else {

                /**
                 * check if this scheduleBackup has metering part
                 */
                meteringScheduleMaintenanceBackupList = this.checkIfItHasMeteringScheduleMaintenance(scheduleMaintenanceBackup);
                scheduleMaintenanceIdListForDelete.add(scheduleMaintenanceBackup.getScheduleMaintenanceId());
            }
        }
        Print.print("subsequentScheduleMaintenanceBackups2222", subsequentScheduleMaintenanceBackups);
        mongoOperations.insertAll(meteringScheduleMaintenanceBackupList);
        mongoOperations.insertAll(subsequentScheduleMaintenanceBackups);
        return scheduleMaintenanceIdListForDelete;
    }

    private List<ScheduleMaintenanceBackup> checkIfItHasMeteringScheduleMaintenance(ScheduleMaintenanceBackup scheduleMaintenanceBackup) {
        List<ScheduleMaintenanceBackup> meteringScheduleMaintenanceBackupList = new ArrayList<>();
        if (scheduleMaintenanceBackup.getPer() != 0) {
            ScheduleMaintenanceBackup scheduleMaintenanceBackup1 = new ScheduleMaintenanceBackup();

            scheduleMaintenanceBackup1.setPer(scheduleMaintenanceBackup.getPer());
            scheduleMaintenanceBackup1.setUnitOfMeasurementId(scheduleMaintenanceBackup.getUnitOfMeasurementId());
            scheduleMaintenanceBackup1.setStartDistance(scheduleMaintenanceBackup.getStartDistance());
            scheduleMaintenanceBackup1.setEndDistance(scheduleMaintenanceBackup.getEndDistance());
            scheduleMaintenanceBackup1.setNextTriggerThreshold(scheduleMaintenanceBackup.getNextTriggerThreshold());

            scheduleMaintenanceBackup1.setScheduleMaintenanceId(scheduleMaintenanceBackup.getScheduleMaintenanceId());
            scheduleMaintenanceBackup1.setAssetId(scheduleMaintenanceBackup.getAssetId());
            scheduleMaintenanceBackup1.setIssueSummary(scheduleMaintenanceBackup.getIssueSummary());
            scheduleMaintenanceBackup1.setCycle(scheduleMaintenanceBackup.getCycle());
            scheduleMaintenanceBackup1.setTaskIdList(scheduleMaintenanceBackup.getTaskIdList());
            scheduleMaintenanceBackup1.setProjectId(scheduleMaintenanceBackup.getProjectId());
            scheduleMaintenanceBackup1.setMaintenanceType(scheduleMaintenanceBackup.getMaintenanceType());
            scheduleMaintenanceBackup1.setPriority(scheduleMaintenanceBackup.getPriority());
            scheduleMaintenanceBackup1.setStatusId(scheduleMaintenanceBackup.getStatusId());
            scheduleMaintenanceBackup1.setActualLaborHour(scheduleMaintenanceBackup.getLaborHour());
            scheduleMaintenanceBackup1.setAdminNote(scheduleMaintenanceBackup.getAdminNote());
            scheduleMaintenanceBackup1.setDocumentFile(scheduleMaintenanceBackup.getDocumentFile());
            scheduleMaintenanceBackup1.setLaborHour(scheduleMaintenanceBackup.getLaborHour());
            scheduleMaintenanceBackup1.setProblem(scheduleMaintenanceBackup.getProblem());
            scheduleMaintenanceBackup1.setRootCause(scheduleMaintenanceBackup.getRootCause());
            scheduleMaintenanceBackup1.setWorkInstruction(scheduleMaintenanceBackup.getWorkInstruction());
            scheduleMaintenanceBackup1.setTaskList(scheduleMaintenanceBackup.getTaskList());
            scheduleMaintenanceBackup1.setSolution(scheduleMaintenanceBackup.getSolution());
            scheduleMaintenanceBackup1.setNote(scheduleMaintenanceBackup.getNote());
            scheduleMaintenanceBackup1.setUserAssignedId(scheduleMaintenanceBackup.getUserAssignedId());
            scheduleMaintenanceBackup1.setCompletedUserId(scheduleMaintenanceBackup.getCompletedUserId());
            scheduleMaintenanceBackup1.setBudgetId(scheduleMaintenanceBackup.getBudgetId());
            scheduleMaintenanceBackup1.setChargeDepartmentId(scheduleMaintenanceBackup.getChargeDepartmentId());
            scheduleMaintenanceBackup1.setCode(scheduleMaintenanceBackup.getCode());
            scheduleMaintenanceBackup1.setTitle(scheduleMaintenanceBackup.getTitle());
            scheduleMaintenanceBackup1.setStartDateOfMainScheduleMaintenanceProject(scheduleMaintenanceBackup.getStartDateOfMainScheduleMaintenanceProject());
            scheduleMaintenanceBackup1.setNumberOfDayForEndingEachScheduleMaintenance(scheduleMaintenanceBackup.getNumberOfDayForEndingEachScheduleMaintenance());
            scheduleMaintenanceBackup1.setActivityId(scheduleMaintenanceBackup.getActivityId());
            scheduleMaintenanceBackup1.setActive(true);
            scheduleMaintenanceBackup1.setUsedParts(scheduleMaintenanceBackup.getUsedParts());
            scheduleMaintenanceBackup1.setTaskGroupList(scheduleMaintenanceBackup.getTaskGroupList());
            scheduleMaintenanceBackup1.setDocumentsList(scheduleMaintenanceBackup.getDocumentsList());
            meteringScheduleMaintenanceBackupList.add(scheduleMaintenanceBackup1);
        }
        return meteringScheduleMaintenanceBackupList;
    }

    @Override
    public List<ScheduleMaintenanceBackup> getAllScheduleMaintenanceBackUp() {

//        this for adding day to present day for test
//        Date date = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        calendar.add(Calendar.DATE, 2);
//        Date finalPresentDate = calendar.getTime();

        /**
         * making present date for comparison
         */
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Date presentDate = null;
        try {
            presentDate = simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date finalPresentDate = presentDate;
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("startDate").is(finalPresentDate));
        List<ScheduleMaintenanceBackup> arrivedTimeScheduleMaintenanceBackup = mongoOperations.find(query, ScheduleMaintenanceBackup.class);
        Update update = new Update();
        update.set("deleted", true);
        update.set("active", false);
        mongoOperations.updateMulti(query, update, ScheduleMaintenanceBackup.class);
        return arrivedTimeScheduleMaintenanceBackup;
    }

    @Override
    public UpdateResult updateScheduleMaintenanceBackUpByBasicInformation(String scheduleMaintenanceId, ScheduleMaintenanceBasicInformation basicInformation) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("laborHour", basicInformation.getLaborHour());
        update.set("issueSummary", basicInformation.getIssueSummary());
        update.set("actualLaborHour", basicInformation.getActualLaborHour());
        update.set("workInstruction", basicInformation.getWorkInstruction());
        return mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public UpdateResult updateScheduleMaintenanceBackUpByCompletionDetail(String scheduleMaintenanceId, ScheduleMaintenanceCompletionDetail scheduleMaintenanceCompletionDetail) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("note", scheduleMaintenanceCompletionDetail.getNote());
        update.set("adminNote", scheduleMaintenanceCompletionDetail.getAdminNote());
        update.set("rootCause", scheduleMaintenanceCompletionDetail.getRootCause());
        update.set("solution", scheduleMaintenanceCompletionDetail.getSolution());
        update.set("problem", scheduleMaintenanceCompletionDetail.getProblem());
        return mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public UpdateResult updateScheduleMaintenanceBackUpTaskGroupIdList(String scheduleMaintenanceId, List<String> taskGroupIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("taskGroupList", taskGroupIdList);
        return mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public UpdateResult updateScheduleMaintenanceBackUpTaskPartByTask(String taskId, String referenceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(referenceId));
        Update update = new Update();
        update.push("taskList", taskId);
        return mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void checkIfAmountArrivedToNextTriggerThresholdOfScheduleMaintenance(String referenceId, long amount, UnitOfMeasurement unitOfMeasurement) {
        List<ScheduleMaintenanceBackup> arrivedTimeScheduleMaintenanceBackupList = this.getArrivedScheduleMaintenanceBackup(referenceId, unitOfMeasurement.getId(), amount);
        Print.print("arrivedTimeScheduleMaintenanceBackupList", arrivedTimeScheduleMaintenanceBackupList);
        this.deleteArrivedScheduleMaintenanceBackupList(referenceId, unitOfMeasurement.getId(), amount);

        /**
         * creating new workOrder according to arrivedTimeScheduleMaintenanceBackup with few changes
         */
        List<WorkOrder> workOrderList = workOrderService.
                generateWorkOrdersForDistanceMeasurementForTodayByTodayScheduleMaintenanceLists(arrivedTimeScheduleMaintenanceBackupList, amount);
        Print.print("workOrderList11", workOrderList);

        /**
         * creating new ActivitySample in cardBoard
         */
        List<String> activityIdList = new ArrayList<>();
        arrivedTimeScheduleMaintenanceBackupList.forEach(scheduleMaintenanceBackup -> {
            activityIdList.add(scheduleMaintenanceBackup.getActivityId());
        });

        List<Activity> activityList = activityService.getListOfActivityForArrivedTimeScheduleMaintenance(activityIdList);
        List<ScheduleActivityDTO> scheduleActivityDTOList = new ArrayList<>();

        workOrderList.forEach(workOrder -> {
            ScheduleActivityDTO scheduleActivityDTO = new ScheduleActivityDTO();
            scheduleActivityDTO.setWorkOrderId(workOrder.getId());
            scheduleActivityDTO.setMaintenanceType(workOrder.getMaintenanceType());
            scheduleActivityDTO.setWorkOrderPriority(workOrder.getPriority());
            scheduleActivityDTO.setAssetId(workOrder.getAssetId());
            scheduleActivityDTO.setWorkOrderTitle(workOrder.getTitle());
            scheduleActivityDTO.setActivity(activityList.stream().filter(activity ->
                    activity.getId().equals(workOrder.getActivityId())).
                    findFirst().orElse(null));
            scheduleActivityDTOList.add(scheduleActivityDTO);
        });

        activitySampleController.createActivitySampleForArrivedTimeScheduleMaintenance(scheduleActivityDTOList);

        List<String> expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups = new ArrayList<>();

        for (ScheduleMaintenanceBackup scheduleMaintenanceBackup : arrivedTimeScheduleMaintenanceBackupList) {

            /**
             * following code is for making deadLine foreach schedule maintenance
             */
            Calendar c = Calendar.getInstance();
            if (scheduleMaintenanceBackup.getStartDate() != null) {
                c.setTime(scheduleMaintenanceBackup.getStartDate());
            }
            c.add(Calendar.DAY_OF_MONTH, (int) scheduleMaintenanceBackup.getNumberOfDayForEndingEachScheduleMaintenance());
            Date deadLineOfEachScheduleMaintenance = c.getTime();
            Print.print("deadLineOfEachScheduleMaintenance", deadLineOfEachScheduleMaintenance);

            /**
             * following service is for making subsequent trigger threshold
             */
            int newNextTriggerTime = this.makingNextTriggerThreShold(scheduleMaintenanceBackup, amount);
            Print.print("nextTrigger", newNextTriggerTime);

            if (newNextTriggerTime <= scheduleMaintenanceBackup.getEndDistance()) {
                Print.print("getPer", scheduleMaintenanceBackup.getPer());

                /**
                 * setting new schedule maintenance backup according to previous one with few changes
                 */
                this.settingNewScheduleMaintenanceAccordingToPreviousOneWithFewChanges
                        (newNextTriggerTime, unitOfMeasurement.getId(), scheduleMaintenanceBackup, deadLineOfEachScheduleMaintenance, amount);
            } else {
                /**
                 * generating new scheduleMaintenanceBackup if it has timing
                 */
                this.checkIfExpiredScheduleBackupHasTiming(scheduleMaintenanceBackup, deadLineOfEachScheduleMaintenance);

                /**
                 * gathering expired scheduleMaintenance to delete
                 */
                expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups.add(scheduleMaintenanceBackup.getScheduleMaintenanceId());
                Print.print("expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups", expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups);

            }

//            /**
//             * sending notification
//             */
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
//
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
        }

        /**
         * inactive associated scheduleMaintenance
         */
        if (expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups.size() > 0) {
            System.out.println("sddsdsdsdsds");
            scheduleMaintananceService.inActiveExpiredScheduleMaintenance(expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups);
        }
    }

    private void checkIfExpiredScheduleBackupHasTiming(ScheduleMaintenanceBackup scheduleMaintenanceBackup, Date deadLineOfEachScheduleMaintenance) {
        if (scheduleMaintenanceBackup.getPerTime() != 0) {
            ScheduleMaintenanceBackup newScheduleMaintenanceBackup = new ScheduleMaintenanceBackup();
            newScheduleMaintenanceBackup.setTitle(scheduleMaintenanceBackup.getTitle());
            newScheduleMaintenanceBackup.setEndDate(scheduleMaintenanceBackup.getEndDate());
            newScheduleMaintenanceBackup.setStartDate(scheduleMaintenanceBackup.getStartDate());
            newScheduleMaintenanceBackup.setScheduleMaintenanceId(scheduleMaintenanceBackup.getScheduleMaintenanceId());
            newScheduleMaintenanceBackup.setAssetId(scheduleMaintenanceBackup.getAssetId());
            newScheduleMaintenanceBackup.setIssueSummary(scheduleMaintenanceBackup.getIssueSummary());
            newScheduleMaintenanceBackup.setCycle(scheduleMaintenanceBackup.getCycle());
            newScheduleMaintenanceBackup.setTaskIdList(scheduleMaintenanceBackup.getTaskIdList());
            newScheduleMaintenanceBackup.setProjectId(scheduleMaintenanceBackup.getProjectId());
            newScheduleMaintenanceBackup.setMaintenanceType(scheduleMaintenanceBackup.getMaintenanceType());
            newScheduleMaintenanceBackup.setPriority(scheduleMaintenanceBackup.getPriority());
            newScheduleMaintenanceBackup.setPerTime(scheduleMaintenanceBackup.getPerTime());
            newScheduleMaintenanceBackup.setStatusId(scheduleMaintenanceBackup.getStatusId());
            newScheduleMaintenanceBackup.setActualLaborHour(scheduleMaintenanceBackup.getLaborHour());
            newScheduleMaintenanceBackup.setAdminNote(scheduleMaintenanceBackup.getAdminNote());
            newScheduleMaintenanceBackup.setDocumentFile(scheduleMaintenanceBackup.getDocumentFile());
            newScheduleMaintenanceBackup.setFixType(scheduleMaintenanceBackup.getFixType());
            newScheduleMaintenanceBackup.setLaborHour(scheduleMaintenanceBackup.getLaborHour());
            newScheduleMaintenanceBackup.setProblem(scheduleMaintenanceBackup.getProblem());
            newScheduleMaintenanceBackup.setRootCause(scheduleMaintenanceBackup.getRootCause());
            newScheduleMaintenanceBackup.setWorkInstruction(scheduleMaintenanceBackup.getWorkInstruction());
            newScheduleMaintenanceBackup.setTaskList(scheduleMaintenanceBackup.getTaskList());
            newScheduleMaintenanceBackup.setSolution(scheduleMaintenanceBackup.getSolution());
            newScheduleMaintenanceBackup.setNote(scheduleMaintenanceBackup.getNote());
            newScheduleMaintenanceBackup.setUserAssignedId(scheduleMaintenanceBackup.getUserAssignedId());
            newScheduleMaintenanceBackup.setCompletedUserId(scheduleMaintenanceBackup.getCompletedUserId());
            newScheduleMaintenanceBackup.setStatusId(scheduleMaintenanceBackup.getStatusId());
            newScheduleMaintenanceBackup.setBudgetId(scheduleMaintenanceBackup.getBudgetId());
            newScheduleMaintenanceBackup.setChargeDepartmentId(scheduleMaintenanceBackup.getChargeDepartmentId());
            newScheduleMaintenanceBackup.setCode(scheduleMaintenanceBackup.getCode());
            newScheduleMaintenanceBackup.setTitle(scheduleMaintenanceBackup.getTitle());
            newScheduleMaintenanceBackup.setScheduleMaintenanceId(scheduleMaintenanceBackup.getScheduleMaintenanceId());
            newScheduleMaintenanceBackup.setEndDateOfEachScheduleMaintenance(deadLineOfEachScheduleMaintenance);
            newScheduleMaintenanceBackup.setStartDateOfMainScheduleMaintenanceProject(scheduleMaintenanceBackup.getStartDateOfMainScheduleMaintenanceProject());
            newScheduleMaintenanceBackup.setStartDistance(scheduleMaintenanceBackup.getStartDistance());
            newScheduleMaintenanceBackup.setActivityId(scheduleMaintenanceBackup.getActivityId());
            newScheduleMaintenanceBackup.setActive(true);
            newScheduleMaintenanceBackup.setTaskList(scheduleMaintenanceBackup.getTaskList());
            newScheduleMaintenanceBackup.setTaskGroupList(scheduleMaintenanceBackup.getTaskGroupList());
            newScheduleMaintenanceBackup.setUsedParts(scheduleMaintenanceBackup.getUsedParts());
            newScheduleMaintenanceBackup.setDocumentsList(scheduleMaintenanceBackup.getDocumentsList());
            mongoOperations.save(newScheduleMaintenanceBackup);
        }
    }

    /**
     * old version notification
     */
//            //for adding parent user of the asset's personnel
//            List<String> userIdListForSendingNotification = new ArrayList<>();
//            //for adding parent building of the asset
//            List<String> assetIdListForSendingNotification = new ArrayList<>();
//
//            Asset asset = assetService.getAssetOfScheduleMaintenanceByAssetId(scheduleMaintenanceBackup.getAssetId());
//            Print.print("mainAsset", asset);
//
//            /**
//             *finding parent assets
//             */
//            getAllRelatedAssetToSendNotification(asset, assetIdListForSendingNotification);
//            List<Asset> relatedAssetList = assetService.getAssetByAssetIdList(assetIdListForSendingNotification);
//
//            Print.print("88888888", relatedAssetList);
//
//            /**
//             * adding parent's asset's users to userIdListForSendingNotification
//             */
//            if (relatedAssetList != null) {
//                relatedAssetList.forEach(asset1 -> {
//                    if (asset1.getUsers() != null) {
//                        userIdListForSendingNotification.addAll(asset1.getUsers());
//                    }
//                });
//            }
//            WorkOrderStatus statusOfTheScheduleMaintenance = workOrderStatusService.getWorkOrderStatusById(scheduleMaintenanceBackup.getStatusId());
//
//            if (asset.getUsers() != null) {
//                List<User> usersOfAsset = userService.getAllPersonnelOfAsset(asset.getUsers());
//                Print.print("666666", usersOfAsset);
//
//                /**
//                 * finding related users
//                 */
//                asset.getUsers().forEach(userId -> {
//                    User user = userService.getOneByUserId(userId);
//                    getAllRelatedUserToSendNotification(user, userIdListForSendingNotification);
//                });
//                Print.print("999999999", userIdListForSendingNotification);
//
//                /**
//                 this is for sending notification to the personnel of the asset that has workOrder
//                 */
//                if (statusOfTheScheduleMaintenance != null && statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//                    List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(asset.getUsers());
//                    Print.print("RRRRRR", usersMessageList);
//                    usersOfAsset.forEach(user -> {
//                        usersMessageList.forEach(message -> {
//                            if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isOpen()
//                                    && message.isAssetsIAmAssignedTo()) {
//                                Notification notification = new Notification();
//                                notification.setRecipientUserId(user.getId());
//                                notification.setCreationDate(new Date());
//                                notification.setSubject("سفارش کار برای زمانبندی نت برنامه ریزی شده");
//                                notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت باز تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                notification.setSenderUserId("حامی");
//                                notificationService.save(notification);
//                                Print.print("dcdcddfffggw21", notification);
//                            }
//                        });
//                    });
//                }
//                if (statusOfTheScheduleMaintenance != null && statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//                    List<Message> userMessageList = messageService.getUsersMessageByUserIdList(asset.getUsers());
//                    usersOfAsset.forEach(user -> {
//                        userMessageList.forEach(message -> {
//                            System.out.println("ee1e12e");
//                            if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isDraft()
//                                    && message.isAssetsIAmAssignedTo()) {
//                                System.out.println("4erfer444");
//                                Notification notification = new Notification();
//                                notification.setRecipientUserId(user.getId());
//                                notification.setCreationDate(new Date());
//                                notification.setSubject("سفارش کار برای زمانبندی نت برنامه ریزی شده");
//                                notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت پیشنویس تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                notification.setSenderUserId("حامی");
//                                Print.print(notification);
//                                notificationService.save(notification);
//                            }
//                        });
//                    });
//                }
//                if (statusOfTheScheduleMaintenance != null && (statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSED))) {
//                    System.out.println("EEEEEEEEEEEEEEEEEEENNNNNNNNNNNNNNNTTTTTTTTTTTTEEEEEEEEEEEEERRRRRRRRRREEEEEEEEEEEEEEDFDDDDDDDDDd");
//                    System.out.println("rqrf   fq");
//                    List<Message> userMessageList = messageService.getUsersMessageByUserIdList(asset.getUsers());
//                    usersOfAsset.forEach(user -> {
//                        userMessageList.forEach(message -> {
//                            System.out.println("rhdgdr");
//                            if (message.getUserId().equals(user.getId()) && message.isPushNotificationMessages() && message.isClosed()
//                                    && message.isAssetsIAmAssignedTo()) {
//                                System.out.println("rgwswsarg");
//                                Notification notification = new Notification();
//                                notification.setRecipientUserId(user.getId());
//                                notification.setCreationDate(new Date());
//                                notification.setSubject("سفارش کار برای زمانبندی نت برنامه ریزی شده");
//                                notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت بسته تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                notification.setSenderUserId("حامی");
//                                Print.print(notification);
//                                notificationService.save(notification);
//                            }
//                        });
//                    });
//                }
//            }
//
//
//            /**
//             this is for sending notification to the report to and personnel of the parent buildings and the users of notify list
//             */
//            //users of the notify of the scheduleMaintenanceBackup
//            List<Notify> notifyListOfTheScheduleMaintenanceBackup = notifyService.getNotifyListOfTheScheduleMaintenanceBackupById(scheduleMaintenanceBackup.getScheduleMaintenanceId());
//
//            if (statusOfTheScheduleMaintenance != null && statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.OPENED)) {
//
//                //sending notification to notify users
//                notifyListOfTheScheduleMaintenanceBackup.forEach(notify -> {
//                    Notification notification = new Notification();
//                    notification.setRecipientUserId(notify.getUser().getId());
//                    notification.setCreationDate(new Date());
//                    notification.setSubject("سفارش کار برای زمانبندی نت برنامه ریزی شده");
//                    notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت باز تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                    notification.setSenderUserId("حامی");
//                    notificationService.save(notification);
//                    Print.print("hjjkld45", notification);
//                });
//
//                // sending notification to users of parent buildings
//                System.out.println("wwwwwwwww" + userIdListForSendingNotification);
//                List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(userIdListForSendingNotification);
//                Print.print("yyyyyyyyytytyt", usersMessageList);
//                userIdListForSendingNotification.forEach(s -> {
//                    User user = userService.getOneByUserId(s);
//                    usersMessageList.forEach(message -> {
//                                if (message.getUserId().equals(s) && message.isPushNotificationMessages() && message.isOpen()
//                                        && message.isAssetsInTheFacilitiesThatIManage()) {
//                                    Notification notification = new Notification();
//                                    notification.setRecipientUserId(user.getId());
//                                    notification.setCreationDate(new Date());
//                                    notification.setSubject("سفارش کار برای زمانبندی نت برنامه ریزی شده");
//                                    notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت باز تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                    notification.setSenderUserId("حامی");
//                                    notificationService.save(notification);
//                                    Print.print("xcvn bbjh", notification);
//                                }
//                            }
//                    );
//                });
//            }
//
//            if (statusOfTheScheduleMaintenance != null && statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.DRAFT)) {
//
//                //sending notification to notify users
//                notifyListOfTheScheduleMaintenanceBackup.forEach(notify -> {
//                    Notification notification = new Notification();
//                    notification.setRecipientUserId(notify.getUser().getId());
//                    notification.setCreationDate(new Date());
//                    notification.setSubject("سفارش کار برای زمانبندی نت برنامه ریزی شده");
//                    notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت پیشنویس تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                    notification.setSenderUserId("حامی");
//                    notificationService.save(notification);
//                    Print.print("fdfgjlpo0", notification);
//                });
//
//                //sending notification to users of parent buildings
//                System.out.println(userIdListForSendingNotification);
//                List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(userIdListForSendingNotification);
//                Print.print("MESSAGE", usersMessageList);
//                userIdListForSendingNotification.forEach(s -> {
//                    User user = userService.getOneByUserId(s);
//                    usersMessageList.forEach(message -> {
//                                if (message.getUserId().equals(s) && message.isPushNotificationMessages() && message.isDraft()
//                                        && message.isAssetsInTheFacilitiesThatIManage()) {
//                                    System.out.println("33fhbdg33");
//                                    Notification notification = new Notification();
//                                    notification.setRecipientUserId(user.getId());
//                                    notification.setCreationDate(new Date());
//                                    notification.setSubject("سفارش کار پیشنویس شده برای زمانبندی نت برنامه ریزی شده");
//                                    notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت پیشنویس تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                    notification.setSenderUserId("حامی");
//                                    Print.print(notification);
//                                    notificationService.save(notification);
//                                }
//                            }
//                    );
//                });
//            }
//
//            //sending notification to notify users
//            if (statusOfTheScheduleMaintenance != null && (statusOfTheScheduleMaintenance.getStatus().equals(WorkOrderStatusEnum.CLOSED))) {
//                notifyListOfTheScheduleMaintenanceBackup.forEach(notify -> {
//                    Notification notification = new Notification();
//                    notification.setRecipientUserId(notify.getUser().getId());
//                    notification.setCreationDate(new Date());
//                    notification.setSubject("سفارش کار بسته برای زمانبندی نت برنامه ریزی شده");
//                    notification.setMessage(notify.getUser().getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت بسته تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                    notification.setSenderUserId("حامی");
//                    notificationService.save(notification);
//                    Print.print("geqgrht6ejh6y", notification);
//                });
//
//                //sending notification to users of parent buildings
//                System.out.println(userIdListForSendingNotification);
//                List<Message> usersMessageList = messageService.getUsersMessageByUserIdList(userIdListForSendingNotification);
//                Print.print("MESSAGE", usersMessageList);
//                userIdListForSendingNotification.forEach(s -> {
//                    User user = userService.getOneByUserId(s);
//                    usersMessageList.forEach(message -> {
//                                if (message.getUserId().equals(s) && message.isPushNotificationMessages() && message.isClosed()
//                                        && message.isAssetsInTheFacilitiesThatIManage()) {
//                                    Notification notification = new Notification();
//                                    notification.setRecipientUserId(user.getId());
//                                    notification.setCreationDate(new Date());
//                                    notification.setSubject("سفارش کار بسته برای زمانبندی نت برنامه ریزی شده");
//                                    notification.setMessage(user.getName() + " سلام " + "\n" + " سفارش کار برنامه ریزی شده با حالت بسته تخصیص یافت " + asset.getCode() + " با کد " + asset.getName() + " برای دارایی ");
//                                    notification.setSenderUserId("حامی");
//                                    Print.print(notification);
//                                    notificationService.save(notification);
//                                }
//                            }
//                    );
//                });
//            }
    private void deleteArrivedScheduleMaintenanceBackupList(String referenceId, String id, long amount) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetId").is(referenceId));
        query.addCriteria(Criteria.where("unitOfMeasurementId").is(id));
        query.addCriteria(Criteria.where("nextTriggerThreshold").lte(amount));
        Update update = new Update();
        update.set("deleted", true);
        update.set("active", false);
        mongoOperations.updateMulti(query, update, ScheduleMaintenanceBackup.class);
    }


    private List<ScheduleMaintenanceBackup> getArrivedScheduleMaintenanceBackup(String referenceId, String id, long amount) {
        Print.print("referenceId", referenceId);
        Print.print("id", id);
        Print.print("amount", amount);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("active").is(true));
        query.addCriteria(Criteria.where("assetId").is(referenceId));
        query.addCriteria(Criteria.where("unitOfMeasurementId").is(id));
        query.addCriteria(Criteria.where("nextTriggerThreshold").lte(amount));
        return mongoOperations.find(query, ScheduleMaintenanceBackup.class);
    }

    private void getAncestorOfEachUser(User user, List<String> ancestorsIdListOfThisWorkOrderUsers) {
        if (user.getParentUserId() != null) {
            String parentId = user.getParentUserId();
            ancestorsIdListOfThisWorkOrderUsers.add(parentId);
            User parentUser = userService.getUserParenByParentId(parentId);
            getAncestorOfEachUser(parentUser, ancestorsIdListOfThisWorkOrderUsers);
        }
    }


    private void settingNewScheduleMaintenanceAccordingToPreviousOneWithFewChanges
            (int newNextTriggerTime, String unitOfMeasurementId, ScheduleMaintenanceBackup scheduleMaintenanceBackup,
             Date deadLineOfEachScheduleMaintenance, long amount) {

        ScheduleMaintenanceBackup newScheduleMaintenanceBackup = new ScheduleMaintenanceBackup();
        newScheduleMaintenanceBackup.setNextTriggerThreshold(newNextTriggerTime);
        newScheduleMaintenanceBackup.setUnitOfMeasurementId(unitOfMeasurementId);
        newScheduleMaintenanceBackup.setTitle(scheduleMaintenanceBackup.getTitle());
        newScheduleMaintenanceBackup.setAssetId(scheduleMaintenanceBackup.getAssetId());
        newScheduleMaintenanceBackup.setPer(scheduleMaintenanceBackup.getPer());
        newScheduleMaintenanceBackup.setEndDate(scheduleMaintenanceBackup.getEndDate());
        newScheduleMaintenanceBackup.setStartDate(scheduleMaintenanceBackup.getStartDate());
        newScheduleMaintenanceBackup.setScheduleMaintenanceId(scheduleMaintenanceBackup.getScheduleMaintenanceId());
        newScheduleMaintenanceBackup.setIssueSummary(scheduleMaintenanceBackup.getIssueSummary());
        newScheduleMaintenanceBackup.setCycle(scheduleMaintenanceBackup.getCycle());
        newScheduleMaintenanceBackup.setPerTime(scheduleMaintenanceBackup.getPerTime());
        newScheduleMaintenanceBackup.setScheduledFixType(scheduleMaintenanceBackup.getScheduledFixType());
        newScheduleMaintenanceBackup.setTaskIdList(scheduleMaintenanceBackup.getTaskIdList());
        newScheduleMaintenanceBackup.setProjectId(scheduleMaintenanceBackup.getProjectId());
        newScheduleMaintenanceBackup.setMaintenanceType(scheduleMaintenanceBackup.getMaintenanceType());
        newScheduleMaintenanceBackup.setPriority(scheduleMaintenanceBackup.getPriority());
        newScheduleMaintenanceBackup.setStatusId(scheduleMaintenanceBackup.getStatusId());
        newScheduleMaintenanceBackup.setActualLaborHour(scheduleMaintenanceBackup.getLaborHour());
        newScheduleMaintenanceBackup.setAdminNote(scheduleMaintenanceBackup.getAdminNote());
        newScheduleMaintenanceBackup.setDocumentFile(scheduleMaintenanceBackup.getDocumentFile());
        newScheduleMaintenanceBackup.setFixType(scheduleMaintenanceBackup.getFixType());
        newScheduleMaintenanceBackup.setLaborHour(scheduleMaintenanceBackup.getLaborHour());
        newScheduleMaintenanceBackup.setProblem(scheduleMaintenanceBackup.getProblem());
        newScheduleMaintenanceBackup.setRootCause(scheduleMaintenanceBackup.getRootCause());
        newScheduleMaintenanceBackup.setWorkInstruction(scheduleMaintenanceBackup.getWorkInstruction());
        newScheduleMaintenanceBackup.setTaskList(scheduleMaintenanceBackup.getTaskList());
        newScheduleMaintenanceBackup.setSolution(scheduleMaintenanceBackup.getSolution());
        newScheduleMaintenanceBackup.setNote(scheduleMaintenanceBackup.getNote());
        newScheduleMaintenanceBackup.setExpired(scheduleMaintenanceBackup.isExpired());
        newScheduleMaintenanceBackup.setUserAssignedId(scheduleMaintenanceBackup.getUserAssignedId());
        newScheduleMaintenanceBackup.setCompletedUserId(scheduleMaintenanceBackup.getCompletedUserId());
        newScheduleMaintenanceBackup.setStatusId(scheduleMaintenanceBackup.getStatusId());
        newScheduleMaintenanceBackup.setBudgetId(scheduleMaintenanceBackup.getBudgetId());
        newScheduleMaintenanceBackup.setChargeDepartmentId(scheduleMaintenanceBackup.getChargeDepartmentId());
        newScheduleMaintenanceBackup.setCode(scheduleMaintenanceBackup.getCode());
        newScheduleMaintenanceBackup.setTitle(scheduleMaintenanceBackup.getTitle());
        newScheduleMaintenanceBackup.setScheduleMaintenanceId(scheduleMaintenanceBackup.getScheduleMaintenanceId());
        newScheduleMaintenanceBackup.setEndDateOfEachScheduleMaintenance(deadLineOfEachScheduleMaintenance);
        newScheduleMaintenanceBackup.setStartDateOfMainScheduleMaintenanceProject(scheduleMaintenanceBackup.getStartDateOfMainScheduleMaintenanceProject());
        newScheduleMaintenanceBackup.setStartDistance(scheduleMaintenanceBackup.getStartDistance());
        newScheduleMaintenanceBackup.setEndDistance(scheduleMaintenanceBackup.getEndDistance());
        newScheduleMaintenanceBackup.setActivityId(scheduleMaintenanceBackup.getActivityId());
        newScheduleMaintenanceBackup.setSpentDistance(amount);
        newScheduleMaintenanceBackup.setActive(true);
        newScheduleMaintenanceBackup.setTaskList(scheduleMaintenanceBackup.getTaskList());
        newScheduleMaintenanceBackup.setTaskGroupList(scheduleMaintenanceBackup.getTaskGroupList());
        newScheduleMaintenanceBackup.setUsedParts(scheduleMaintenanceBackup.getUsedParts());
        newScheduleMaintenanceBackup.setDocumentsList(scheduleMaintenanceBackup.getDocumentsList());
        mongoOperations.save(newScheduleMaintenanceBackup);
    }


    private int makingNextTriggerThreShold(ScheduleMaintenanceBackup scheduleMaintenanceBackup, long amount) {
        int result = (int) (amount - scheduleMaintenanceBackup.getStartDistance());
        int divideResult = result / scheduleMaintenanceBackup.getPer();
        return (int) (scheduleMaintenanceBackup.getStartDistance() + divideResult * scheduleMaintenanceBackup.getPer() + scheduleMaintenanceBackup.getPer());

//        Print.print("AMount", amount);
//        int divideResult = (int) amount / scheduleMaintenanceBackup.getPer();
//        Print.print("divide", divideResult);
//        Print.print("resultt", divideResult * scheduleMaintenanceBackup.getPer() + scheduleMaintenanceBackup.getStartDistance());
//        return divideResult * scheduleMaintenanceBackup.getPer() + scheduleMaintenanceBackup.getPer();
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
    public void findAndDeleteScheduleMaintenanceBackup(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("deleted", true);
        update.set("active", false);
        mongoOperations.updateMulti(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void deleteScheduleMaintenanceBackUpTimeScheduling(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("perTime", null);
        update.set("endDate", null);
        update.set("startDate", null);
        update.set("cycle", null);
        update.set("fixType", null);
        update.set("startDateOfMainScheduleMaintenanceProject", null);
        mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void deleteScheduleMaintenanceBackupDistanceScheduling(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("per", null);
        update.set("endDistance", 0);
        update.set("startDistance", 0);
        update.set("unitOfMeasurementId", null);
        update.set("spentDistance", 0);
        update.set("nextTriggerThreshold", 0);
        mongoOperations.updateMulti(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void updateScheduleMaintenanceBackupScheduleTime(ScheduleWithTimeAndMetering scheduleWithTimeAndMetering, String scheduleMaintenanceId) {
        Print.print("fdgsghds", scheduleWithTimeAndMetering);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        ScheduleMaintenanceBackup previousFormOfScheduleMaintenanceBackup = mongoOperations.findOne(query, ScheduleMaintenanceBackup.class);

        Update update = new Update();
        update.set("perTime", scheduleWithTimeAndMetering.getScheduledTime().getPer());
        update.set("startDate", scheduleWithTimeAndMetering.getScheduledTime().getStartOn());
        update.set("endDate", scheduleWithTimeAndMetering.getScheduledTime().getEndOn());
        update.set("cycle", scheduleWithTimeAndMetering.getScheduledTime().getCycle());
        if (previousFormOfScheduleMaintenanceBackup != null) {
            update.set("per", previousFormOfScheduleMaintenanceBackup.getPer());
        }
        if (previousFormOfScheduleMaintenanceBackup != null) {
            update.set("endDistance", previousFormOfScheduleMaintenanceBackup.getEndDistance());
        }
        if (previousFormOfScheduleMaintenanceBackup != null) {
            update.set("startDistance", previousFormOfScheduleMaintenanceBackup.getEndDistance());
        }
        if (previousFormOfScheduleMaintenanceBackup != null) {
            update.set("unitOfMeasurementId", previousFormOfScheduleMaintenanceBackup.getUnitOfMeasurementId());
        }

        mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void updateScheduleMaintenanceBackupMetering(ScheduleWithTimeAndMetering scheduleWithTimeAndMetering, String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        ScheduleMaintenanceBackup previousFormOfScheduleMaintenanceBackup = mongoOperations.findOne(query, ScheduleMaintenanceBackup.class);
        Print.print("123123123", previousFormOfScheduleMaintenanceBackup);
        Update update = new Update();
        update.set("per", scheduleWithTimeAndMetering.getMeteringCycle().getPer());
        update.set("endDistance", scheduleWithTimeAndMetering.getMeteringCycle().getEndDistance());
        update.set("startDistance", scheduleWithTimeAndMetering.getMeteringCycle().getStartDistance());
        update.set("unitOfMeasurementId", scheduleWithTimeAndMetering.getMeteringCycle().getUnitOfMeasurementId());
        if (previousFormOfScheduleMaintenanceBackup != null) {
            update.set("perTime", previousFormOfScheduleMaintenanceBackup.getPerTime());
        }
        if (previousFormOfScheduleMaintenanceBackup != null) {
            update.set("startDate", previousFormOfScheduleMaintenanceBackup.getStartDate());
        }
        if (previousFormOfScheduleMaintenanceBackup != null) {
            update.set("endDate", previousFormOfScheduleMaintenanceBackup.getEndDate());
        }
        if (previousFormOfScheduleMaintenanceBackup != null) {
            update.set("cycle", previousFormOfScheduleMaintenanceBackup.getCycle());
        }
        mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void updateScheduleTimeOfScheduleMaintenanceBackup(ScheduledTime scheduledTime, String scheduleMaintenanceId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("perTime", scheduledTime.getPer());
        update.set("startDate", scheduledTime.getStartOn());
        update.set("endDate", scheduledTime.getEndOn());
        update.set("cycle", scheduledTime.getCycle());
        update.set("startDateOfMainScheduleMaintenanceProject", new Date());
        mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void updateScheduleDistance(ScheduledMeteringCycle scheduledMeteringCycle, String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("per", scheduledMeteringCycle.getPer());
        update.set("endDistance", scheduledMeteringCycle.getEndDistance());
        update.set("startDistance", scheduledMeteringCycle.getStartDistance());
        update.set("unitOfMeasurementId", scheduledMeteringCycle.getUnitOfMeasurementId());
        update.set("nextTriggerThreshold", scheduledMeteringCycle.getStartDistance() + scheduledMeteringCycle.getPer());
        mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public List<ScheduleMaintenanceBackup> deleteExpiredScheduleMaintenance() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        Date presentDate = null;
        try {
            presentDate = simpleDateFormat.parse(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Print.print("presentDate", presentDate);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("endDate").lt(presentDate));
        Update update = new Update();
        update.set("deleted", true);
        update.set("active", false);
        mongoOperations.updateMulti(query, update, ScheduleMaintenanceBackup.class);
        return mongoOperations.find(query, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void deleteRelatedTaskOfScheduleBackup(String taskId, String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.pull("taskList", taskId);
        mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void addUserPartToScheduleBackup(String id, String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.push("usedParts", id);
        mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void deleteUsedPartOfScheduleBackup(String partWithUsageCountId, String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.pull("usedParts", partWithUsageCountId);
        mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void addDocumentIdToBackupSchedule(String referenceId, String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(referenceId));
        Update update = new Update();
        update.push("documentsList", id);
        mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }

    @Override
    public void deleteDocumentFromBackupSchedule(String documentId, String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("scheduleMaintenanceId").is(scheduleMaintenanceId));
        Update update = new Update();
        update.pull("documentsList", documentId);
        mongoOperations.updateFirst(query, update, ScheduleMaintenanceBackup.class);
    }
}
