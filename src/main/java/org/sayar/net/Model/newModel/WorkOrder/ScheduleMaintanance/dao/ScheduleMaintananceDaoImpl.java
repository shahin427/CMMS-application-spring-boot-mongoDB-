package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.dao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.DTO.ScheduleMaintenanceCreateDTO;
import org.sayar.net.Model.DTO.ScheduleMaintenanceFilterDTO;
import org.sayar.net.Model.newModel.Enum.ExpirationStatus;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.*;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintenanceBackupService;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintenanceCalenderController;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@Repository()
public class ScheduleMaintananceDaoImpl extends GeneralDaoImpl<ScheduleMaintenance> implements ScheduleMaintananceDao {
    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ScheduleMaintenanceBackupService scheduleMaintenanceBackupService;

    @Autowired
    private ScheduleMaintenanceCalenderController scheduleMaintenanceCalenderController;

    public ScheduleMaintananceDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Override
    public ScheduleMaintenance postScheduleMaintenance(ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO) {
        ScheduleMaintenance scheduleMaintenance = new ScheduleMaintenance();


        if (scheduleMaintenanceCreateDTO.getCode() != null) {
            scheduleMaintenance.setCode(scheduleMaintenanceCreateDTO.getCode());
        }
        if (scheduleMaintenanceCreateDTO.getTitle() != null) {
            scheduleMaintenance.setTitle(scheduleMaintenanceCreateDTO.getTitle());
        }
        if (scheduleMaintenanceCreateDTO.getAssetId() != null) {
            scheduleMaintenance.setAssetId(scheduleMaintenanceCreateDTO.getAssetId());
        }
        if (scheduleMaintenanceCreateDTO.getImage() != null) {
            scheduleMaintenance.setImage(scheduleMaintenanceCreateDTO.getImage());
        }
        if (scheduleMaintenanceCreateDTO.getProjectId() != null) {
            scheduleMaintenance.setProjectId(scheduleMaintenanceCreateDTO.getProjectId());
        }
        if (scheduleMaintenanceCreateDTO.getPriority() != null) {
            scheduleMaintenance.setPriority(scheduleMaintenanceCreateDTO.getPriority());
        }
        if (scheduleMaintenanceCreateDTO.getMaintenanceType() != null) {
            scheduleMaintenance.setMaintenanceType(scheduleMaintenanceCreateDTO.getMaintenanceType());
        }
        if (scheduleMaintenanceCreateDTO.getStatusId() != null) {
            scheduleMaintenance.setStatusId(scheduleMaintenanceCreateDTO.getStatusId());
        }
        if (scheduleMaintenanceCreateDTO.getActivityId() != null) {
            scheduleMaintenance.setActivityId(scheduleMaintenanceCreateDTO.getActivityId());
        }
        scheduleMaintenance.setActive(scheduleMaintenanceCreateDTO.isActive());
        return mongoOperations.save(scheduleMaintenance);
    }

    @Override
    public ScheduleMaintenance getAllBasicInformation(String scheduleMaintenanceId) {
        System.out.println(scheduleMaintenanceId);
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().include("scheduleMaintenanceBasicInformation");
        return mongoOperations.findOne(query, ScheduleMaintenance.class);
    }

    @Override
    public List<ScheduleMaintenance> getAllScheduleMaintenanceByProjectId(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("projectId").is(projectId));
        query.fields()
                .include("title")
                .include("code")
                .include("assetId")
                .include("priority")
                .include("statusId")
                .include("maintenanceType")
                .include("id");
        return mongoOperations.find(query, ScheduleMaintenance.class);
    }

    @Override
    public ScheduleMaintenance ScheduleMaintenanceCompletionDetail(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        return mongoOperations.findOne(query, ScheduleMaintenance.class);
    }

    @Override
    public boolean updateBasicInformationByScheduleMaintenanceId(String scheduleMaintenanceId, ScheduleMaintenanceBasicInformation basicInformation) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("scheduleMaintenanceBasicInformation.issueSummary", basicInformation.getIssueSummary());
        update.set("scheduleMaintenanceBasicInformation.laborHour", basicInformation.getLaborHour());
        update.set("scheduleMaintenanceBasicInformation.actualLaborHour", basicInformation.getActualLaborHour());
        update.set("scheduleMaintenanceBasicInformation.workInstruction", basicInformation.getWorkInstruction());
        update.set("scheduleMaintenanceBasicInformation.completionDate", basicInformation.getCompletionDate());
        mongoOperations.updateFirst(query, update, ScheduleMaintenance.class);
        return true;
    }

    @Override
    public UpdateResult updateScheduleMaintenanceCreateByScheduleId(String scheduleMaintenanceId, ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("id", scheduleMaintenanceCreateDTO.getId());
        update.set("code", scheduleMaintenanceCreateDTO.getCode());
        update.set("title", scheduleMaintenanceCreateDTO.getTitle());
        update.set("assetId", scheduleMaintenanceCreateDTO.getAssetId());
        update.set("image", scheduleMaintenanceCreateDTO.getImage());
        update.set("projectId", scheduleMaintenanceCreateDTO.getProjectId());
        update.set("maintenanceType", scheduleMaintenanceCreateDTO.getMaintenanceType());
        update.set("statusId", scheduleMaintenanceCreateDTO.getStatusId());
        update.set("priority", scheduleMaintenanceCreateDTO.getPriority());
        update.set("active", scheduleMaintenanceCreateDTO.isActive());
        update.set("activityId", scheduleMaintenanceCreateDTO.getActivityId());
        return mongoOperations.updateFirst(query, update, ScheduleMaintenance.class);
    }

    @Override
    public UpdateResult updateCompletionDetailByAssetId(String scheduleMaintenanceId, ScheduleMaintenanceCompletionDetail scheduleMaintenanceCompletionDetail) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true)//                , new BasicAggregationOperation(exprQuery)
        );
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("scheduleMaintenanceCompletionDetail.note", scheduleMaintenanceCompletionDetail.getNote());
        update.set("scheduleMaintenanceCompletionDetail.problem", scheduleMaintenanceCompletionDetail.getProblem());
        update.set("scheduleMaintenanceCompletionDetail.rootCause", scheduleMaintenanceCompletionDetail.getRootCause());
        update.set("scheduleMaintenanceCompletionDetail.solution", scheduleMaintenanceCompletionDetail.getSolution());
        update.set("scheduleMaintenanceCompletionDetail.adminNote", scheduleMaintenanceCompletionDetail.getAdminNote());
        return mongoOperations.updateFirst(query, update, ScheduleMaintenance.class);
    }

    @Override
    public List<ScheduleMaintenanceCreateDTO> getAllByFilterAndPagination(ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO,
                                                                          Pageable pageable, Integer totalElement) {
        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectId")
                .foreignField("_id")
                .as("project");

        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("id")
                .andExpression("assetId").as("assetId")
                .andExpression("image").as("image")
                .andExpression("projectId").as("projectId")
                .andExpression("project.name").as("projectName")
                .andExpression("priority").as("priority")
                .andExpression("maintenanceType").as("maintenanceType")
                .andExpression("statusId").as("statusId");

        Criteria criteria;
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("deleted").ne(true));

        if (scheduleMaintenanceFilterDTO.getAssetId() != null && !scheduleMaintenanceFilterDTO.getAssetId().equals(""))
            criteriaList.add(Criteria.where("assetId").regex(scheduleMaintenanceFilterDTO.getAssetId()));

        if (scheduleMaintenanceFilterDTO.getMaintenanceType() != null)
            criteriaList.add(Criteria.where("maintenanceType").is(scheduleMaintenanceFilterDTO.getMaintenanceType()));

        if (scheduleMaintenanceFilterDTO.getPriority() != null)
            criteriaList.add(Criteria.where("priority").is(scheduleMaintenanceFilterDTO.getPriority()));

        if (scheduleMaintenanceFilterDTO.getProjectId() != null && !scheduleMaintenanceFilterDTO.getProjectId().equals(""))
            criteriaList.add(Criteria.where("projectId").regex(scheduleMaintenanceFilterDTO.getProjectId()));

        if (scheduleMaintenanceFilterDTO.getWorkOrderStatusId() != null && !scheduleMaintenanceFilterDTO.getAssetId().equals(""))
            criteriaList.add(Criteria.where("statusId").is(scheduleMaintenanceFilterDTO.getWorkOrderStatusId()));

        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));


        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
                , skipOperation
                , limitOperation
                , lookupOperation
                , projectionOperation
        );
        return mongoOperations.aggregate(aggregation, ScheduleMaintenance.class, ScheduleMaintenanceCreateDTO.class).getMappedResults();
    }

    @Override
    public long countAllByFilterAndPagination(ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO) {

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));

        if (scheduleMaintenanceFilterDTO.getAssetId() != null && !scheduleMaintenanceFilterDTO.getAssetId().equals(""))
            query.addCriteria(Criteria.where("assetId").is(scheduleMaintenanceFilterDTO.getAssetId()));

        if (scheduleMaintenanceFilterDTO.getMaintenanceType() != null)
            query.addCriteria(Criteria.where("maintenanceType").is(scheduleMaintenanceFilterDTO.getMaintenanceType()));

        if (scheduleMaintenanceFilterDTO.getPriority() != null)
            query.addCriteria(Criteria.where("priority").is(scheduleMaintenanceFilterDTO.getPriority()));

        if (scheduleMaintenanceFilterDTO.getProjectId() != null && !scheduleMaintenanceFilterDTO.getProjectId().equals(""))
            query.addCriteria(Criteria.where("projectId").regex(scheduleMaintenanceFilterDTO.getProjectId()));

        if (scheduleMaintenanceFilterDTO.getWorkOrderStatusId() != null && !scheduleMaintenanceFilterDTO.getAssetId().equals(""))
            query.addCriteria(Criteria.where("statusId").is(scheduleMaintenanceFilterDTO.getWorkOrderStatusId()));

        return mongoOperations.count(query, ScheduleMaintenance.class);
    }

    @Override
    public ScheduleMaintenance getOneScheduleMaintenance(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        return mongoOperations.findOne(query, ScheduleMaintenance.class);
    }

    @Override
    public ScheduleMaintenance getScheduleWithTimeAndMetering(String scheduleMaintenanceId) {
        System.out.println(scheduleMaintenanceId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        Print.print("find method", mongoOperations.findOne(query, ScheduleMaintenance.class));
        return mongoOperations.findOne(query, ScheduleMaintenance.class);
    }

    @Override
    public ScheduleMaintenance updateScheduleWithTimeAndMeteringByScheduleId(String scheduleMaintenanceId, ScheduleWithTimeAndMetering scheduleWithTimeAndMetering) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        ScheduleMaintenance scheduleMaintenance = mongoOperations.findOne(query, ScheduleMaintenance.class);
        if (scheduleMaintenance != null && scheduleMaintenance.getScheduleWithTimeAndMetering() == null) {
            //if the object is null below line avoids nullPointerException
            scheduleMaintenance.setScheduleWithTimeAndMetering(new ScheduleWithTimeAndMetering());
        }
        if (scheduleWithTimeAndMetering.getScheduledTime().getEndOn() != null) {
            scheduleMaintenance.getScheduleWithTimeAndMetering().setScheduledTime(scheduleWithTimeAndMetering.getScheduledTime());
            scheduleMaintenance.getScheduleWithTimeAndMetering().setScheduleType(scheduleWithTimeAndMetering.getScheduleType());
            scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduledTime().setLastModify(new Date());
            scheduleMaintenanceBackupService.updateScheduleMaintenanceBackupScheduleTime(scheduleWithTimeAndMetering, scheduleMaintenanceId);
        }
        if (scheduleWithTimeAndMetering.getMeteringCycle().getEndDistance() != 0) {
            scheduleMaintenance.getScheduleWithTimeAndMetering().setMeteringCycle(scheduleWithTimeAndMetering.getMeteringCycle());
            scheduleMaintenance.getScheduleWithTimeAndMetering().setScheduleType(scheduleWithTimeAndMetering.getScheduleType());
            scheduleMaintenance.getScheduleWithTimeAndMetering().getMeteringCycle().setRegistrationDate(new Date());
            scheduleMaintenanceBackupService.updateScheduleMaintenanceBackupMetering(scheduleWithTimeAndMetering, scheduleMaintenanceId);
        }
        return mongoOperations.save(scheduleMaintenance);
    }

    @Override
    public ScheduleMaintenance getTaskGroupListByScheduleMaintenanceId(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        query.fields().include("taskGroupList");
        return mongoOperations.findOne(query, ScheduleMaintenance.class);
    }

    @Override
    public boolean updateTaskGroupListByScheduleMaintenanceId(String scheduleMaintenanceId, List<String> taskGroupIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("taskGroupList", taskGroupIdList);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ScheduleMaintenance.class);
        if (updateResult.getModifiedCount() > 0) {
            return true;
        } else
            return false;
    }

    @Override
    public List<ScheduleMaintenanceCreateDTO> getAllByPagination(ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO,
                                                                 Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("id")
                .andExpression("assetId").as("assetId")
                .andExpression("image").as("image")
                .andExpression("projectId").as("projectId")
                .andExpression("priority").as("priority")
                .andExpression("maintenanceType").as("maintenanceType")
                .andExpression("statusId").as("statusId")
                .andExpression("active").as("active")
                .andExpression("code").as("code")
                .andExpression("title").as("title")
                .andExpression("scheduleWithTimeAndMetering.MeteringCycle").as("scheduledMeteringCycle");

        Criteria criteria;
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("deleted").ne(true));

        if (scheduleMaintenanceFilterDTO.getAssetId() != null && !scheduleMaintenanceFilterDTO.getAssetId().equals(""))
            criteriaList.add(Criteria.where("assetId").regex(scheduleMaintenanceFilterDTO.getAssetId()));

        if (scheduleMaintenanceFilterDTO.getDueDate() != null) {
            List<ScheduleMaintenanceCalender> scheduleMaintenanceCalenderList = scheduleMaintenanceCalenderController.getTriggeredScheduleMaintenanceInSpecifiedTime(scheduleMaintenanceFilterDTO.getDueDate());
            List<String> scheduleMaintenanceIdList = new ArrayList<>();
            scheduleMaintenanceCalenderList.forEach(scheduleMaintenanceCalender -> scheduleMaintenanceIdList.add(scheduleMaintenanceCalender.getScheduleMaintenanceId()));
            criteriaList.add(Criteria.where("id").in(scheduleMaintenanceIdList));
        }

        if (scheduleMaintenanceFilterDTO.getExpirationStatus() != null) {
            boolean active = false;
            if (scheduleMaintenanceFilterDTO.getExpirationStatus().equals(ExpirationStatus.INPROCESS)) {
                active = true;
            }
            criteriaList.add(Criteria.where("active").is(active));
        }

        if (scheduleMaintenanceFilterDTO.getTitle() != null && !scheduleMaintenanceFilterDTO.getTitle().equals(""))
            criteriaList.add(Criteria.where("title").regex(scheduleMaintenanceFilterDTO.getTitle()));

        if (scheduleMaintenanceFilterDTO.getCode() != null && !scheduleMaintenanceFilterDTO.getCode().equals(""))
            criteriaList.add(Criteria.where("code").regex(scheduleMaintenanceFilterDTO.getCode()));

        if (scheduleMaintenanceFilterDTO.getMaintenanceType() != null)
            criteriaList.add(Criteria.where("maintenanceType").is(scheduleMaintenanceFilterDTO.getMaintenanceType()));

        if (scheduleMaintenanceFilterDTO.getPriority() != null)
            criteriaList.add(Criteria.where("priority").is(scheduleMaintenanceFilterDTO.getPriority()));

        if (scheduleMaintenanceFilterDTO.getProjectId() != null && !scheduleMaintenanceFilterDTO.getProjectId().equals(""))
            criteriaList.add(Criteria.where("projectId").regex(scheduleMaintenanceFilterDTO.getProjectId()));

        if (scheduleMaintenanceFilterDTO.getWorkOrderStatusId() != null && !scheduleMaintenanceFilterDTO.getWorkOrderStatusId().equals(""))
            criteriaList.add(Criteria.where("statusId").is(scheduleMaintenanceFilterDTO.getWorkOrderStatusId()));

        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
                , projectionOperation
                , skipOperation
                , limitOperation
        );
        return mongoOperations.aggregate(aggregation, ScheduleMaintenance.class, ScheduleMaintenanceCreateDTO.class).getMappedResults();
    }

    @Override
    public long countAllFilteredScheduleMaintenance(ScheduleMaintenanceFilterDTO scheduleMaintenanceFilterDTO) {
        Criteria criteria;
        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("deleted").ne(true));

        if (scheduleMaintenanceFilterDTO.getAssetId() != null && !scheduleMaintenanceFilterDTO.getAssetId().equals(""))
            criteriaList.add(Criteria.where("assetId").regex(scheduleMaintenanceFilterDTO.getAssetId()));

        if (scheduleMaintenanceFilterDTO.getDueDate() != null) {
            List<ScheduleMaintenanceCalender> scheduleMaintenanceCalenderList = scheduleMaintenanceCalenderController.getTriggeredScheduleMaintenanceInSpecifiedTime(scheduleMaintenanceFilterDTO.getDueDate());
            List<String> scheduleMaintenanceIdList = new ArrayList<>();
            scheduleMaintenanceCalenderList.forEach(scheduleMaintenanceCalender -> scheduleMaintenanceIdList.add(scheduleMaintenanceCalender.getScheduleMaintenanceId()));
            criteriaList.add(Criteria.where("id").in(scheduleMaintenanceIdList));
        }

        if (scheduleMaintenanceFilterDTO.getExpirationStatus() != null) {
            boolean active = false;
            if (scheduleMaintenanceFilterDTO.getExpirationStatus().equals(ExpirationStatus.INPROCESS)) {
                active = true;
            }
            criteriaList.add(Criteria.where("active").is(active));
        }

        if (scheduleMaintenanceFilterDTO.getTitle() != null && !scheduleMaintenanceFilterDTO.getTitle().equals(""))
            criteriaList.add(Criteria.where("title").regex(scheduleMaintenanceFilterDTO.getTitle()));

        if (scheduleMaintenanceFilterDTO.getCode() != null && !scheduleMaintenanceFilterDTO.getCode().equals(""))
            criteriaList.add(Criteria.where("code").regex(scheduleMaintenanceFilterDTO.getCode()));

        if (scheduleMaintenanceFilterDTO.getMaintenanceType() != null)
            criteriaList.add(Criteria.where("maintenanceType").is(scheduleMaintenanceFilterDTO.getMaintenanceType()));

        if (scheduleMaintenanceFilterDTO.getPriority() != null)
            criteriaList.add(Criteria.where("priority").is(scheduleMaintenanceFilterDTO.getPriority()));

        if (scheduleMaintenanceFilterDTO.getProjectId() != null && !scheduleMaintenanceFilterDTO.getProjectId().equals(""))
            criteriaList.add(Criteria.where("projectId").regex(scheduleMaintenanceFilterDTO.getProjectId()));

        if (scheduleMaintenanceFilterDTO.getWorkOrderStatusId() != null && !scheduleMaintenanceFilterDTO.getWorkOrderStatusId().equals(""))
            criteriaList.add(Criteria.where("statusId").is(scheduleMaintenanceFilterDTO.getWorkOrderStatusId()));


        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, ScheduleMaintenance.class, ScheduleMaintenanceCreateDTO.class).getMappedResults().size();
    }

    @Override
    public List<ScheduleMaintenance> getAllScheduleMaintenance() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields().exclude("documentFile").exclude("image");
        return mongoOperations.find(query, ScheduleMaintenance.class);
    }

    @Override
    public List<ScheduleMaintenance> getAllScheduleMaintenanceByScheduleMaintenanceBackup(List<String> scheduledMaintenanceBackupInventoryIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(scheduledMaintenanceBackupInventoryIdList));
        return mongoOperations.find(query, ScheduleMaintenance.class);
    }

    @Override
    public ScheduleMaintenance getScheduleMaintenanceById(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        return mongoOperations.findOne(query, ScheduleMaintenance.class);
    }

    @Override
    public ScheduleMaintenanceBackup getScheduleMaintenanceByAssetId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetId").is(id));
        return mongoOperations.findOne(query, ScheduleMaintenanceBackup.class);
    }

    @Override
    public UpdateResult deleteScheduleMaintenanceTimeScheduling(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        Update update = new Update();
        update.unset("scheduleWithTimeAndMetering.scheduledTime");
        update.set("hasTimeSchedule", false);
        return mongoOperations.updateFirst(query, update, ScheduleMaintenance.class);
    }

    @Override
    public UpdateResult deleteScheduleMaintenanceDistanceScheduling(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        Update update = new Update();
        update.unset("scheduleWithTimeAndMetering.MeteringCycle");
        update.set("hasMeteringSchedule", false);
        return mongoOperations.updateFirst(query, update, ScheduleMaintenance.class);
    }

    @Override
    public List<Long> getAllFutureDistanceOfScheduleMaintenance(long per, long startDistance, long endDistance) {
        List<Long> distanceCollection = new ArrayList<>();
        startDistance = startDistance + per;
        while (startDistance <= endDistance) {
            distanceCollection.add(startDistance);
            startDistance = startDistance + per;
        }
        return distanceCollection;
    }

    @Override
    public ScheduleMaintenance getOldVersionOfScheduleMaintenance(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        return mongoOperations.findOne(query, ScheduleMaintenance.class);
    }

    @Override
    public ScheduleMaintenance getScheduleMaintenanceOfNotify(String referenceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(referenceId));
        return mongoOperations.findOne(query, ScheduleMaintenance.class);
    }

    @Override
    public boolean checkIfCodeIsUnique(String code) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(code));
        return mongoOperations.exists(query, ScheduleMaintenance.class);
    }

    @Override
    public boolean checkIfUserExistsScheduleMaintenance(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("taskList").is(userId),
                Criteria.where("taskGroupList").is(userId)
        ));
        return mongoOperations.exists(query, ScheduleMaintenance.class);
    }

    @Override
    public ScheduleMaintenance updateScheduleTime(ScheduledTime scheduledTime, String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
//        query.fields()
//                .include("scheduleWithTimeAndMetering.scheduledTime");
        Update update = new Update();
        update.set("scheduleWithTimeAndMetering.scheduledTime.per", scheduledTime.getPer());
        update.set("scheduleWithTimeAndMetering.scheduledTime.endOn", scheduledTime.getEndOn());
        update.set("scheduleWithTimeAndMetering.scheduledTime.startOn", scheduledTime.getStartOn());
        update.set("scheduleWithTimeAndMetering.scheduledTime.cycle", scheduledTime.getCycle());
        update.set("scheduleWithTimeAndMetering.scheduledTime.lastModify", new Date());
        update.set("hasTimeSchedule", true);
        return mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), ScheduleMaintenance.class);
    }

    @Override
    public ScheduleMaintenance updateScheduleDistance(ScheduledMeteringCycle scheduledMeteringCycle, String
            scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        Update update = new Update();
        update.set("scheduleWithTimeAndMetering.MeteringCycle.per", scheduledMeteringCycle.getPer());
        update.set("scheduleWithTimeAndMetering.MeteringCycle.endDistance", scheduledMeteringCycle.getEndDistance());
        update.set("scheduleWithTimeAndMetering.MeteringCycle.startDistance", scheduledMeteringCycle.getStartDistance());
        update.set("scheduleWithTimeAndMetering.MeteringCycle.unitOfMeasurementId", scheduledMeteringCycle.getUnitOfMeasurementId());
        update.set("scheduleWithTimeAndMetering.MeteringCycle.registrationDate", new Date());
        update.set("hasMeteringSchedule", true);
        return mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), ScheduleMaintenance.class);
    }

    @Override
    public ScheduleMaintenance getScheduleMaintenanceScheduleTime(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        query.fields()
                .include("scheduleWithTimeAndMetering.scheduledTime");
        return mongoOperations.findOne(query, ScheduleMaintenance.class);
    }

    @Override
    public ScheduleMaintenance getScheduleMaintenanceScheduleDistance(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        query.fields()
                .include("scheduleWithTimeAndMetering.MeteringCycle");
        return mongoOperations.findOne(query, ScheduleMaintenance.class);
    }

    @Override
    public Asset getActivityOfScheduleMaintenanceByRelevantAsset(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(assetId));
        query.fields().include("activityIdList");
        return mongoOperations.findOne(query, Asset.class);
    }

    @Override
    public void inactiveRelevantScheduleMaintenance(List<String> scheduleMaintenanceIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(scheduleMaintenanceIdList));
        Update update = new Update();
        update.set("hasTimeSchedule", false);
        mongoOperations.updateMulti(query, update, ScheduleMaintenance.class);
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("deleted").ne(true));
        query1.addCriteria(Criteria.where("id").in(scheduleMaintenanceIdList));
        query1.addCriteria(Criteria.where("hasMeteringSchedule").in(false));
        update.set("deleted", true);
        update.set("active", false);
        mongoOperations.updateMulti(query1, update, ScheduleMaintenance.class);
    }

    @Override
    public void inActiveExpiredScheduleMaintenance(List<String> expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups) {

        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups));
        query.addCriteria(Criteria.where("deleted").ne(true));
        Update update = new Update();
        update.set("hasMeteringSchedule", false);
        mongoOperations.updateMulti(query, update, ScheduleMaintenance.class);
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("id").in(expiredInMeteringRelevantScheduleMaintenancesIdOfScheduleMaintenanceBackups));
        query1.addCriteria(Criteria.where("deleted").ne(true));
        query1.addCriteria(Criteria.where("hasTimeSchedule").is(false));
        Update update1 = new Update();
        update1.set("active", false);
        update1.set("deleted", true);
        mongoOperations.updateMulti(query1, update1, ScheduleMaintenance.class);
    }

    @Override
    public boolean ifProjectExistsInScheduleMaintenance(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("projectId").in(projectId));
        return mongoOperations.exists(query, ScheduleMaintenance.class);
    }

    @Override
    public boolean ifWorkStatusExistsInScheduleMaintenance(String workOrderStatusId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusId));
        return mongoOperations.exists(query, ScheduleMaintenance.class);
    }

    @Override
    public boolean ifAssetExistsInScheduleMaintenance(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetId").in(assetId));
        return mongoOperations.exists(query, ScheduleMaintenance.class);
    }

    @Override
    public boolean ifTaskGroupExistsInScheduleMaintenance(String taskGroupId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("taskGroupList").in(taskGroupId));
        return mongoOperations.exists(query, ScheduleMaintenance.class);
    }

    @Override
    public UpdateResult deleteAndInActiveScheduleMaintenanceById(String scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(scheduleMaintenanceId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        Update update = new Update();
        update.set("deleted", true);
        update.set("active", false);
        return mongoOperations.updateMulti(query, update, ScheduleMaintenance.class);
    }

    @Override
    public List<ScheduleMaintenance> getProjectScheduleMaintenance(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(projectId));
        query.fields()
                .include("id")
                .include("statusId")
                .include("assetId")
                .include("priority");
        return mongoOperations.find(query, ScheduleMaintenance.class);
    }

    @Override
    public List<ScheduleMaintenance> getScheduleMaintenanceListById(List<String> scheduleMaintenanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(scheduleMaintenanceId));
        return mongoOperations.find(query, ScheduleMaintenance.class);
    }
}
