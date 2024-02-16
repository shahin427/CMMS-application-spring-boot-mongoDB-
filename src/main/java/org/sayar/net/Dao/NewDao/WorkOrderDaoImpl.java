package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.sayar.net.Controller.newController.MtbfDTO;
import org.sayar.net.Controller.newController.dto.*;
import org.sayar.net.Enumes.AssetStatus;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.CompletionDetail;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.MtbfReturn;
import org.sayar.net.Model.WorkOrderSchedule;
import org.sayar.net.Model.WorkRequest;
import org.sayar.net.Model.newModel.BasicInformation;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Range;
import org.sayar.net.Model.newModel.Part.Part;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenanceBackup;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Tools.Print;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.sayar.net.Model.newModel.Enum.Priority.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository

public class WorkOrderDaoImpl extends GeneralDaoImpl<WorkOrder> implements WorkOrderDao {
    private final MongoOperations mongoOperations;

    public WorkOrderDaoImpl(MongoDatabaseFactory mongoDbFactory, MongoOperations mongoOperations) {
        super(mongoDbFactory);
        this.mongoOperations = mongoOperations;
    }

    @Override
    public WorkOrder postCreateWorkOrder(WorkOrderCreateDTO workOrderCreateDTO) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setCode(workOrderCreateDTO.getCode());
        workOrder.setTitle(workOrderCreateDTO.getTitle());
        workOrder.setMaintenanceType(workOrderCreateDTO.getMaintenanceType());
        workOrder.setPriority(workOrderCreateDTO.getPriority());
        workOrder.setAssetId(workOrderCreateDTO.getAssetId());
        workOrder.setProjectId(workOrderCreateDTO.getProjectId());
        workOrder.setRequiredCompletionDate(workOrderCreateDTO.getRequiredCompletionDate());
        workOrder.setImage(workOrderCreateDTO.getImage());
        workOrder.setStatusId(workOrderCreateDTO.getStatusId());
        workOrder.setStartDate(workOrderCreateDTO.getStartDate());
        workOrder.setRequiredCompletionDate(workOrderCreateDTO.getRequiredCompletionDate());
        workOrder.setEndDate(workOrderCreateDTO.getEndDate());
        return mongoOperations.save(workOrder);
    }

    @Override
    public UpdateResult updateCreateWorkOrder(WorkOrderCreateDTO workOrderCreateDTO, String workOrderId) {
        Print.print("SSS", workOrderCreateDTO);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));

        Update update = new Update();
        update.set("code", workOrderCreateDTO.getCode());
        update.set("statusId", workOrderCreateDTO.getStatusId());
        update.set("startDate", workOrderCreateDTO.getStartDate());
        update.set("endDate", workOrderCreateDTO.getEndDate());
        update.set("title", workOrderCreateDTO.getTitle());
        update.set("maintenanceType", workOrderCreateDTO.getMaintenanceType());
        update.set("priority", workOrderCreateDTO.getPriority());
        update.set("assetId", workOrderCreateDTO.getAssetId());
        update.set("projectId", workOrderCreateDTO.getProjectId());
        update.set("requiredCompletionDate", workOrderCreateDTO.getRequiredCompletionDate());
        update.set("image", workOrderCreateDTO.getImage());
        return mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public WorkOrder getOneCreateWorkOrder(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.fields()
                .include("code")
                .include("title")
                .include("priority")
                .include("assetId")
                .include("projectId")
                .include("image")
                .include("maintenanceType")
                .include("startDate")
                .include("endDate")
                .include("assetId")
                .include("creationDate")
                .include("statusId")
                .include("requiredCompletionDate")
                .include("image");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public boolean postBasicInformation(WorkOrderDTOBasicInformation workOrderDTOBasicInformation) {

        WorkOrder workOrder = new WorkOrder();
        workOrder.getBasicInformation().setIssueSummary(workOrderDTOBasicInformation.getIssueSummary());
        workOrder.getBasicInformation().setUserAssignedId(workOrderDTOBasicInformation.getUserAssignedId());
        workOrder.getBasicInformation().setCompletedUserId(workOrderDTOBasicInformation.getCompletedUserId());
        workOrder.getBasicInformation().setLaborHour(workOrderDTOBasicInformation.getLaborHour());
        workOrder.getBasicInformation().setActualLaborHour(workOrderDTOBasicInformation.getActualLaborHour());
        workOrder.getBasicInformation().setWorkInstruction(workOrderDTOBasicInformation.getWorkInstruction());
        mongoOperations.save(workOrder);
        return true;
    }

    @Override
    public WorkOrder getOneBasicInformation(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields().include("issueSummary")
                .include("userAssignedId")
                .include("completedUserId")
                .include("laborHour")
                .include("actualLaborHour")
                .include("completionDate")
                .include("workInstruction");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrderDTO> getAllByFilterAndPagination(WorkOrderDTO workOrderDTO, Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        Criteria criteria = new Criteria();
        ProjectionOperation projectionOperation = project()
                .andExpression("id").as("id")
                .andExpression("title").as("title")
                .andExpression("statusId").as("statusId")
                .andExpression("code").as("code")
                .andExpression("priority").as("priority")
                .andExpression("projectId").as("projectId")
                .andExpression("assetId").as("assetId")
                .andExpression("maintenanceType").as("maintenanceType")
                .andExpression("startDate").as("startDate")
                .andExpression("endDate").as("endDate")
                .andExpression("pmSheetCode").as("pmSheetCode")
                .andExpression("fromSchedule").as("fromSchedule");

        List<Criteria> criteriaList = new ArrayList<>();

        criteriaList.add(Criteria.where("fromSchedule").ne(true));
        criteriaList.add(Criteria.where("deleted").ne(true));
        criteriaList.add(Criteria.where("acceptedByManager").is(true));
//        criteriaList.add(Criteria.where("rejectedWorkOrder").ne(true));

        if (workOrderDTO.getFromSchedule() != null)
            criteriaList.add(Criteria.where("fromSchedule").is(workOrderDTO.getFromSchedule()));

        if (workOrderDTO.getCode() != null && !workOrderDTO.getCode().equals(""))
            criteriaList.add(Criteria.where("code").regex(workOrderDTO.getCode()));

        if (workOrderDTO.getTitle() != null && !workOrderDTO.getTitle().equals(""))
            criteriaList.add(Criteria.where("title").regex(workOrderDTO.getTitle()));

        if (workOrderDTO.getStatusId() != null && !workOrderDTO.getStatusId().equals(""))
            criteriaList.add(Criteria.where("statusId").is(workOrderDTO.getStatusId()));

        if (workOrderDTO.getPriority() != null)
            criteriaList.add(Criteria.where("priority").is(workOrderDTO.getPriority()));

        if (workOrderDTO.getProjectId() != null && !workOrderDTO.getProjectId().equals(""))
            criteriaList.add(Criteria.where("projectId").is(workOrderDTO.getProjectId()));

        if (workOrderDTO.getMaintenanceType() != null)
            criteriaList.add(Criteria.where("maintenanceType").is(workOrderDTO.getMaintenanceType()));

        if (workOrderDTO.getAssetId() != null && !workOrderDTO.getAssetId().equals(""))
            criteriaList.add(Criteria.where("assetId").is(workOrderDTO.getAssetId()));

        if (workOrderDTO.getStartDate() != null)
            criteriaList.add(Criteria.where("startDate").gte(workOrderDTO.getStartDate()));

        if (workOrderDTO.getEndDate() != null)
            criteriaList.add(Criteria.where("endDate").lte(workOrderDTO.getEndDate()));

        if (workOrderDTO.getFailureDateFrom() != null && workOrderDTO.getFailureDateUntil() == null) {
            criteriaList.add(Criteria.where("startDate").gte(workOrderDTO.getFailureDateFrom()));
        }
        if (workOrderDTO.getFailureDateUntil() != null && workOrderDTO.getFailureDateFrom() == null) {
            criteriaList.add(Criteria.where("startDate").lte(workOrderDTO.getFailureDateUntil()));
        }

        if (workOrderDTO.getFailureDateUntil() != null && workOrderDTO.getFailureDateFrom() != null) {
            criteriaList.add(criteria.andOperator(
                    Criteria.where("startDate").gte(workOrderDTO.getFailureDateFrom()),
                    Criteria.where("startDate").lte(workOrderDTO.getFailureDateUntil())
            ));
        }

        if (workOrderDTO.getPmSheetCode() != null && workOrderDTO.getPmSheetCode().equals("")) {
            criteriaList.add(Criteria.where("pmSheetCode").regex(workOrderDTO.getPmSheetCode()));
        }

        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                sort(Sort.Direction.DESC, "startDate"),
                projectionOperation,
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, WorkOrderDTO.class).getMappedResults();
    }


    @Override
    public Page<ResWorkOrderPmGetPageDTO> getPageWorkOrderCreatedBySchedule(ReqWorkOrderPmGetPageDTO entity, Pageable pageable, Integer totalElements) {


        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("rejectedInSchedule").is(false);
        criteria.and("fromSchedule").is(true);
        criteria.and("acceptedByManager").is(true);


        if (entity.getAssetId() != null && !entity.getAssetId().equals("")) {
            criteria.and("assetId").is(entity.getAssetId());
        }
        if (entity.getActivityTypeId() != null && !entity.getActivityTypeId().equals("")) {
            criteria.and("activityTypeId").is(entity.getActivityTypeId());
        }
        if (entity.getMinorSubSystem() != null && !entity.getMinorSubSystem().equals("")) {
            criteria.and("minorSubSystem").regex(entity.getMinorSubSystem());
        }
        if (entity.getMainSubSystemId() != null && !entity.getMainSubSystemId().equals("")) {
            criteria.and("mainSubSystemId").is(entity.getMainSubSystemId());
        }
        if (entity.getWorkCategoryId() != null && !entity.getWorkCategoryId().equals("")) {
            criteria.and("workCategoryId").is(entity.getWorkCategoryId());
        }
        if (entity.getImportanceDegreeId() != null && !entity.getImportanceDegreeId().equals("")) {
            criteria.and("importanceDegreeId").is(entity.getImportanceDegreeId());
        }
        if (entity.getAssetStatus() != null && !entity.getAssetStatus().equals("")) {
            criteria.and("assetStatus").is(entity.getAssetStatus());
        }
        if (entity.getStartDate() != null
                && !entity.getStartDate().equals("")
                && entity.getEndDate() != null
                && !entity.getEndDate().equals("")) {
            System.out.println("two");
            Date start = entity.getStartDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.add(Calendar.HOUR, -12);
            start = cal.getTime();

            Date end = entity.getEndDate();
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.add(Calendar.HOUR, 12);
            end = c.getTime();

            criteria.andOperator(Criteria.where("startDate").gte(start)
                    , Criteria.where("startDate").lte(end)
            );
        } else if (entity.getStartDate() != null && !entity.getStartDate().equals("")) {
            System.out.println("start");
            Date start = entity.getStartDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            int daysToDecrement = -12;
            cal.add(Calendar.HOUR, daysToDecrement);
            start = cal.getTime();
            criteria.and("startDate").gte(start);
        } else if (entity.getEndDate() != null && !entity.getEndDate().equals("")) {
            System.out.println("end");
            Date endDte = entity.getEndDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDte);
            int daysToDecrement = -12;
            cal.add(Calendar.HOUR, daysToDecrement);
            endDte = cal.getTime();

            criteria.and("startDate").lte(endDte);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$activityTypeId"))).as("activityTypeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$mainSubSystemId"))).as("mainSubSystemId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$importanceDegreeId"))).as("importanceDegreeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$workCategoryId"))).as("workCategoryId")
                        .and("id").as("id")
                        .and("minorSubSystem").as("minorSubSystem")
                        .and("assetStatus").as("assetStatus")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("solution").as("solution")
                        .and("activityTime").as("activityTime")
                , lookup("asset", "assetId", "_id", "asset")
                , lookup("asset", "mainSubSystemId", "_id", "mainSubSystem")
                , lookup("activityType", "activityTypeId", "_id", "activityType")
                , lookup("workCategory", "workCategoryId", "_id", "workCategory")
                , lookup("importanceDegree", "importanceDegreeId", "_id", "importanceDegree")
                , project()
                        .and("id").as("id")
                        .and("assetStatus").as("assetStatus")//8
                        .and("startDate").as("startDate")//9
                        .and("endDate").as("endDate")//10
                        .and("solution").as("solution")//5
                        .and("minorSubSystem").as("minorSubSystem")//3
                        .and("activityTime").as("activityTime")//11
                        .and("assetId").as("assetId")//1
                        .and("asset.name").as("assetName")//1
                        .and("activityTypeId").as("activityTypeId")//4
                        .and("activityType.name").as("activityTypeName")//4
                        .and("mainSubSystemId").as("mainSubSystemId")//2
                        .and("mainSubSystem.name").as("mainSubSystemName")//2
                        .and("workCategoryId").as("workCategoryId")//6
                        .and("workCategory.name").as("workCategoryName")//6
                        .and("importanceDegreeId").as("importanceDegreeId")//7
                        .and("importanceDegree.name").as("importanceDegreeName"),//7,
                Aggregation.sort(Sort.Direction.DESC, "startDate"),
                skipOperation,
                limitOperation

        );

        AggregationResults<ResWorkOrderPmGetPageDTO> groupResults
                = super.aggregate(aggregation, WorkOrder.class, ResWorkOrderPmGetPageDTO.class);
        List<ResWorkOrderPmGetPageDTO> result = groupResults.getMappedResults();
        if (totalElements <= 0) {
            Aggregation aggCount = Aggregation.newAggregation(
                    Aggregation.match(criteria)
            );
            AggregationResults<ResWorkOrderPmGetPageDTO> groupResultsCount
                    = super.aggregate(aggCount, WorkOrder.class, ResWorkOrderPmGetPageDTO.class);
            List<ResWorkOrderPmGetPageDTO> resultcount = groupResultsCount.getMappedResults();
            totalElements = resultcount.size();
        }
        Page<ResWorkOrderPmGetPageDTO> res = new PageImpl<ResWorkOrderPmGetPageDTO>(result, pageable, totalElements);
        return res;
    }
@Override
    public List<ResWorkOrderForCalendarGetListDTO> getListWorkOrderForCalendar(ReqWorkOrderForCalendarGetListDTO entity) {

        Criteria criteria = new Criteria();
            Date start = entity.getStartDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            int daysToDecrement = -12;
            cal.add(Calendar.HOUR, daysToDecrement);
            start = cal.getTime();

            Date end = entity.getEndDate();
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.add(Calendar.HOUR, 12);
            end = c.getTime();

            criteria.andOperator(Criteria.where("endDate").gte(start)
                    , Criteria.where("endDate").lte(end)
            );

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetId")
                        .and("id").as("id")
                        .and("endDate").as("endDate")
                        .and("userIdList").as("userIdList")
                , lookup("asset", "assetId", "_id", "asset")
                , project()
                        .and("id").as("id")
                        .and("endDate").as("endDate")
                        .and("assetId").as("assetId")
                        .and("asset.name").as("assetName")
                        .and("userIdList").as("userIdList")

        );

        AggregationResults<ResWorkOrderForCalendarGetListDTO> groupResults
                = super.aggregate(aggregation, WorkOrder.class, ResWorkOrderForCalendarGetListDTO.class);
        List<ResWorkOrderForCalendarGetListDTO> result = groupResults.getMappedResults();
        return result;
    }


    @Override
    public List<ResWorkOrderPmGetPageDTO> getAllWorkOrderCreatedBySchedule(ReqWorkOrderPmGetPageDTO entity) {

        Criteria criteria = new Criteria();
        criteria.and("rejectedInSchedule").is(false);
        criteria.and("fromSchedule").is(true);


        if (entity.getAssetId() != null && !entity.getAssetId().equals("")) {
            criteria.and("assetId").is(entity.getAssetId());
        }
        if (entity.getActivityTypeId() != null && !entity.getActivityTypeId().equals("")) {
            criteria.and("activityTypeId").is(entity.getActivityTypeId());
        }
        if (entity.getMinorSubSystem() != null && !entity.getMinorSubSystem().equals("")) {
            criteria.and("minorSubSystem").regex(entity.getMinorSubSystem());
        }
        if (entity.getMainSubSystemId() != null && !entity.getMainSubSystemId().equals("")) {
            criteria.and("mainSubSystemId").is(entity.getMainSubSystemId());
        }
        if (entity.getWorkCategoryId() != null && !entity.getWorkCategoryId().equals("")) {
            criteria.and("workCategoryId").is(entity.getWorkCategoryId());
        }
        if (entity.getImportanceDegreeId() != null && !entity.getImportanceDegreeId().equals("")) {
            criteria.and("importanceDegreeId").is(entity.getImportanceDegreeId());
        }
        if (entity.getAssetStatus() != null && !entity.getAssetStatus().equals("")) {
            criteria.and("assetStatus").is(entity.getAssetStatus());
        }
        if (entity.getStartDate() != null
                && !entity.getStartDate().equals("")
                && entity.getEndDate() != null
                && !entity.getEndDate().equals("")) {
            Date start = entity.getStartDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            int daysToDecrement = -12;
            cal.add(Calendar.HOUR, daysToDecrement);
            start = cal.getTime();

            Date end = entity.getEndDate();
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.add(Calendar.HOUR, 12);
            end = c.getTime();

            criteria.andOperator(Criteria.where("startDate").gte(start)
                    , Criteria.where("startDate").lte(end)
            );
        } else if (entity.getStartDate() != null && !entity.getStartDate().equals("")) {
            Date start = entity.getStartDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            int daysToDecrement = -12;
            cal.add(Calendar.HOUR, daysToDecrement);
            start = cal.getTime();
            criteria.and("startDate").gte(start);
        } else if (entity.getEndDate() != null && !entity.getEndDate().equals("")) {
            Date endDte = entity.getEndDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDte);
            int daysToDecrement = -12;
            cal.add(Calendar.HOUR, daysToDecrement);
            endDte = cal.getTime();

            criteria.and("startDate").lte(endDte);
        }

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$activityTypeId"))).as("activityTypeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$mainSubSystemId"))).as("mainSubSystemId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$importanceDegreeId"))).as("importanceDegreeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$workCategoryId"))).as("workCategoryId")
                        .and("id").as("id")
                        .and("minorSubSystem").as("minorSubSystem")
                        .and("assetStatus").as("assetStatus")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("solution").as("solution")
                        .and("activityTime").as("activityTime")
                , lookup("asset", "assetId", "_id", "asset")
                , lookup("asset", "mainSubSystemId", "_id", "mainSubSystem")
                , lookup("activityType", "activityTypeId", "_id", "activityType")
                , lookup("workCategory", "workCategoryId", "_id", "workCategory")
                , lookup("importanceDegree", "importanceDegreeId", "_id", "importanceDegree")
                , project()
                        .and("id").as("id")
                        .and("assetStatus").as("assetStatus")//8
                        .and("startDate").as("startDate")//9
                        .and("endDate").as("endDate")//10
                        .and("solution").as("solution")//5
                        .and("minorSubSystem").as("minorSubSystem")//3
                        .and("activityTime").as("activityTime")//11
                        .and("assetId").as("assetId")//1
                        .and("asset.name").as("assetName")//1
                        .and("activityTypeId").as("activityTypeId")//4
                        .and("activityType.name").as("activityTypeName")//4
                        .and("mainSubSystemId").as("mainSubSystemId")//2
                        .and("mainSubSystem.name").as("mainSubSystemName")//2
                        .and("workCategoryId").as("workCategoryId")//6
                        .and("workCategory.name").as("workCategoryName")//6
                        .and("importanceDegreeId").as("importanceDegreeId")//7
                        .and("importanceDegree.name").as("importanceDegreeName")//7

        );

        AggregationResults<ResWorkOrderPmGetPageDTO> groupResults
                = super.aggregate(aggregation, WorkOrder.class, ResWorkOrderPmGetPageDTO.class);
        List<ResWorkOrderPmGetPageDTO> result = groupResults.getMappedResults();
        return result;
    }


    @Override
    public Map<String, Object> getOne(String id) {
        Criteria criteria = new Criteria();
        criteria.and("id").is(id);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$activityTypeId"))).as("activityTypeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$mainSubSystemId"))).as("mainSubSystemId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$importanceDegreeId"))).as("importanceDegreeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$workCategoryId"))).as("workCategoryId")
                        .and("id").as("id")
                        .and("requestedDate").as("requestedDate")
                        .and("repairDate").as("repairDate")
                        .and("requestDescription").as("requestDescription")
                        .and("failureReason").as("failureReason")
                        .and("solution").as("solution")
                        .and("userIdList").as("userIdList")
                        .and("usedPartList").as("usedPartList")
                        .and("partSupply").as("partSupply")
                        .and("failureDuration").as("failureDuration")
                        .and("repairDuration").as("repairDuration")
                        .and("acceptedByManager").as("acceptedByManager")
                        .and("workRequestId").as("workRequestId")
                        .and("pmSheetCode").as("pmSheetCode")
                        .and("associatedScheduleMaintenanceId").as("associatedScheduleMaintenanceId")


                        .and("minorSubSystem").as("minorSubSystem")
                        .and("assetStatus").as("assetStatus")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("solution").as("solution")
                        .and("activityTime").as("activityTime")
                , lookup("asset", "assetId", "_id", "asset")
                , lookup("asset", "mainSubSystemId", "_id", "mainSubSystem")
                , lookup("activityType", "activityTypeId", "_id", "activityType")
                , lookup("workCategory", "workCategoryId", "_id", "workCategory")
                , lookup("importanceDegree", "importanceDegreeId", "_id", "importanceDegree")
                , project()
                        .and("id").as("id")
                        .and("requestedDate").as("requestedDate")
                        .and("repairDate").as("repairDate")
                        .and("requestDescription").as("requestDescription")
                        .and("failureReason").as("failureReason")
                        .and("solution").as("solution")
                        .and("userIdList").as("userIdList")
                        .and("usedPartList").as("usedPartList")
                        .and("partSupply").as("partSupply")
                        .and("failureDuration").as("failureDuration")
                        .and("repairDuration").as("repairDuration")
                        .and("acceptedByManager").as("acceptedByManager")
                        .and("workRequestId").as("workRequestId")
                        .and("pmSheetCode").as("pmSheetCode")
                        .and("associatedScheduleMaintenanceId").as("associatedScheduleMaintenanceId")


                        .and("assetStatus").as("assetStatus")//8
                        .and("startDate").as("startDate")//9
                        .and("endDate").as("endDate")//10
                        .and("solution").as("solution")//5
                        .and("minorSubSystem").as("minorSubSystem")//3
                        .and("activityTime").as("activityTime")//11
                        .and("assetId").as("assetId")//1
                        .and("asset.name").as("assetName")//1
                        .and("activityTypeId").as("activityTypeId")//4
                        .and("activityType.name").as("activityTypeName")//4
                        .and("mainSubSystemId").as("mainSubSystemId")//2
                        .and("mainSubSystem.name").as("mainSubSystemName")//2
                        .and("workCategoryId").as("workCategoryId")//6
                        .and("workCategory.name").as("workCategoryName")//6
                        .and("importanceDegreeId").as("importanceDegreeId")//7
                        .and("importanceDegree.name").as("importanceDegreeName")//7

        );
        List<ResWorkOrderPmGetOneDTO> res = mongoOperations.aggregate(aggregation, WorkOrder.class, ResWorkOrderPmGetOneDTO.class).getMappedResults();
        Print.print("res", res);
        List<Part> usePart = new ArrayList<>();
        if (res.get(0) != null && res.size() > 0 && res.get(0).getUsedPartList() != null && res.get(0).getUsedPartList().size() > 0) {
            List<String> partList = new ArrayList<>();

            res.get(0).getUsedPartList().forEach(a -> {
                partList.add(a.getPartId());
            });
            usePart = getUsedPartInWorkOrder(partList);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("res", res);
        result.put("usePart", usePart);
        return result;
    }

    public List<Part> getUsedPartInWorkOrder(List<String> partIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(partIdList));
        query.fields()
                .include("id")
                .include("name")
                .include("partCode");
        return mongoOperations.find(query, Part.class);
    }

    @Override
    public long getAllCount(WorkOrderDTO workOrderDTO) {

        Criteria criteria = new Criteria();
        List<Criteria> criteriaList = new ArrayList<>();

//        criteriaList.add(Criteria.where("assignedToTechnician").is(true));
        criteriaList.add(Criteria.where("deleted").ne(true));
        criteriaList.add(Criteria.where("acceptedByManager").is(true));
        criteriaList.add(Criteria.where("rejectedWorkOrder").ne(true));

        if (workOrderDTO.getFromSchedule() != null)
            criteriaList.add(Criteria.where("fromSchedule").is(workOrderDTO.getFromSchedule()));
        if (workOrderDTO.getCode() != null && !workOrderDTO.getCode().equals(""))
            criteriaList.add(Criteria.where("code").regex(workOrderDTO.getCode()));
        if (workOrderDTO.getTitle() != null && !workOrderDTO.getTitle().equals(""))
            criteriaList.add(Criteria.where("title").regex(workOrderDTO.getTitle()));
        if (workOrderDTO.getStatusId() != null && !workOrderDTO.getStatusId().equals(""))
            criteriaList.add(Criteria.where("statusId").is(workOrderDTO.getStatusId()));
        if (workOrderDTO.getPriority() != null)
            criteriaList.add(Criteria.where("priority").is(workOrderDTO.getPriority()));
        if (workOrderDTO.getProjectId() != null && !workOrderDTO.getProjectId().equals(""))
            criteriaList.add(Criteria.where("projectId").is(workOrderDTO.getProjectId()));
        if (workOrderDTO.getMaintenanceType() != null)
            criteriaList.add(Criteria.where("maintenanceType").is(workOrderDTO.getMaintenanceType()));
        if (workOrderDTO.getAssetId() != null && !workOrderDTO.getAssetId().equals(""))
            criteriaList.add(Criteria.where("assetId").is(workOrderDTO.getAssetId()));
        if (workOrderDTO.getStartDate() != null)
            criteriaList.add(Criteria.where("startDate").gte(workOrderDTO.getStartDate()));
        if (workOrderDTO.getEndDate() != null)
            criteriaList.add(Criteria.where("endDate").lte(workOrderDTO.getEndDate()));
        //-----------------------------------
        if (workOrderDTO.getFailureDateFrom() != null && workOrderDTO.getFailureDateUntil() == null) {
            criteriaList.add(Criteria.where("startDate").gte(workOrderDTO.getFailureDateFrom()));
        }
        if (workOrderDTO.getFailureDateUntil() != null && workOrderDTO.getFailureDateFrom() == null) {
            criteriaList.add(Criteria.where("startDate").lte(workOrderDTO.getFailureDateUntil()));
        }
        if (workOrderDTO.getFailureDateUntil() != null && workOrderDTO.getFailureDateFrom() != null) {
            criteriaList.add(criteria.andOperator(
                    Criteria.where("startDate").gte(workOrderDTO.getFailureDateFrom()),
                    Criteria.where("startDate").lte(workOrderDTO.getFailureDateUntil())
            ));
        }
        if (workOrderDTO.getPmSheetCode() != null && workOrderDTO.getPmSheetCode().equals("")) {
            criteriaList.add(Criteria.where("pmSheetCode").regex(workOrderDTO.getPmSheetCode()));
        }
        //--------------------------------
        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, WorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public WorkOrder findOneWorkOrder(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public boolean checkWorkOrderCode(String workOrderCode) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("code").is(workOrderCode));
        return mongoOperations.exists(query, WorkOrder.class);
    }

    @Override
    public WorkOrder getCompletionDetailByWorkOrderId(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields().include("completionDetail");
        return mongoOperations.findOne(query, WorkOrder.class);

    }

    @Override
    public BasicInformationDTO getBasicInformationByWorkOrderId(String workOrderId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(workOrderId);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("basicInformation.issueSummary").as("basicInformationDTO.issueSummary")
                        .and("basicInformation.laborHour").as("basicInformationDTO.laborHour")
                        .and("basicInformation.actualLaborHour").as("basicInformationDTO.actualLaborHour")
                        .and("basicInformation.completedUserId").as("basicInformationDTO.completedUserId")
                        .and("basicInformation.completedUserUserTypeId").as("basicInformationDTO.completedUserUserTypeId")
//                        .and("basicInformation.completedUserOrgId").as("basicInformationDTO.completedUserOrgId")
                        .and("basicInformation.workInstruction").as("basicInformationDTO.workInstruction")
                        .and(ConvertOperators.ToObjectId.toObjectId("$basicInformation.userAssignedId")).as("basicInformationDTO.userAssignedId")
//                        .and(ConvertOperators.ToObjectId.toObjectId("$basicInformation.userAssignedOrgId")).as("basicInformationDTO.userAssignedOrgId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$basicInformation.userAssignedUserTypeId")).as("basicInformationDTO.userAssignedUserTypeId"),
                lookup("user", "basicInformationDTO.userAssignedId", "_id", "user"),
//                lookup("organization", "basicInformationDTO.userAssignedOrgId", "_id", "organization"),
                lookup("userType", "basicInformationDTO.userAssignedUserTypeId", "_id", "userType"),
                project()
                        .and("basicInformationDTO.issueSummary").as("issueSummary")
                        .and("basicInformationDTO.laborHour").as("laborHour")
                        .and("basicInformationDTO.actualLaborHour").as("actualLaborHour")
                        .and("basicInformationDTO.workInstruction").as("workInstruction")
                        .and("basicInformationDTO.completedUserId").as("completedUserId")
                        .and("basicInformationDTO.completedUserUserTypeId").as("completedUserUserTypeId")
//                        .and("basicInformationDTO.completedUserOrgId").as("completedUserOrgId")
                        .and("basicInformationDTO.userAssignedId").as("userAssignedId")
                        .and("user.name").as("userAssignedName")
                        .and("user.family").as("userAssignedFamily")
//                        .and("basicInformationDTO.userAssignedOrgId").as("userAssignedOrgId")
//                        .and("organization.name").as("userAssignedOrgName")
                        .and("basicInformationDTO.userAssignedUserTypeId").as("userAssignedUserTypeId")
                        .and("userType.name").as("userAssignedUserTypeName")
                        .andExclude("_id")
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, BasicInformationDTO.class).getUniqueMappedResult();
    }

    @Override
    public WorkOrder getOneWorkOrder(WorkOrderMiscCostDTO workOrderMiscCostDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderMiscCostDTO.getWorkOrderId()));
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public UpdateResult updateBasicInformationByWorkOrderId(String workOrderId, BasicInformation basicInformation) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        Update update = new Update();
        update.set("basicInformation", basicInformation);
        return mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public UpdateResult updateCompletionByWorkOrderId(String workOrderId, CompletionDetail completionDetail) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields().include("completionDetail");
        Update update = new Update();
        update.set("completionDetail", completionDetail);
        return mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public WorkOrder getTaskGroupListByWorkOrderId(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields()
                .include("taskGroups");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public boolean getWorkOrderByWorkOrderId(List<String> taskGroup, String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        Update update = new Update();
        update.set("taskGroups", taskGroup);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, WorkOrder.class);
        if (updateResult.getModifiedCount() > 0)
            return true;
        else
            return false;
    }

    @Override
    public List<WorkOrder> getWorkOrderByProjectId(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("projectId").is(projectId));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllWorkOrdersByProjectId(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("projectId").is(projectId));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> generateWorkOrderByArrivedScheduleMaintenance(List<ScheduleMaintenance> scheduleMaintenanceList) {
        List<WorkOrder> workOrderList = new ArrayList<>();
        scheduleMaintenanceList.forEach(scheduleMaintenance -> {
            WorkOrder workOrder = new WorkOrder();
            workOrder.setImage(scheduleMaintenance.getImage());
            workOrder.setAssetId(scheduleMaintenance.getAssetId());
            workOrder.setPriority(scheduleMaintenance.getPriority());
            workOrder.setProjectId(scheduleMaintenance.getProjectId());
            workOrder.setMaintenanceType(scheduleMaintenance.getMaintenanceType());
            workOrder.setTaskGroups(scheduleMaintenance.getTaskGroupList());
            workOrder.setTasks(scheduleMaintenance.getTaskList());
            workOrder.setStartDate(scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduledTime().getStartOn());
            workOrderList.add(workOrder);
        });
        return mongoOperations.save(workOrderList);
    }

    @Override
    public long countAllWorkOrderByUserId(String userAssignedId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countOpenWorkOrderByStatusId(String userAssignedId, List<String> workOrderStatusIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusIdList));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countHighPriorityWorkOrders(String userAssignedId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("priority").is(HIGHEST));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countAllClosedWorkOrdersByUserId(String userAssignedId, List<String> workOrderStatusIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusIdList));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countAllClosedNotCompletedWorkOrdersByUserId(String userAssignedId, List<String> workOrderStatusIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusIdList));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long onTimeCompletedWorkOrders(List<WorkOrder> workOrderListExceptClosedOnes) {

        List<WorkOrder> workOrderList1 = new ArrayList<>();
        workOrderListExceptClosedOnes.forEach(workOrder -> {
            if (workOrder != null
                    && workOrder.getEndDate() != null
                    && workOrder.getRequiredCompletionDate() != null
                    && workOrder.getEndDate().before(workOrder.getRequiredCompletionDate())) {
                workOrderList1.add(workOrder);
            }
        });
        return workOrderList1.size();
    }

    @Override
    public long overDueAndCompletedWorkOrders(List<WorkOrder> closedWorkOrderList) {
        List<WorkOrder> workOrderList1 = new ArrayList<>();
        closedWorkOrderList.forEach(workOrder -> {
            if (!workOrder.isDeleted()
                    && workOrder.getEndDate().after(workOrder.getRequiredCompletionDate())) {
                workOrderList1.add(workOrder);
            }
        });
        return workOrderList1.size();
    }

    @Override
    public long overDueAndNotCompletedWorkOrders(List<String> stringList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("requiredCompletionDate").lt(java.time.LocalDate.now()));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long getAllWorkOrders(List<String> stringList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long onTimeCompletionRate(long onTimeCompletedWorkOrders, long totalWorkOrdersExceptCloseOnes) {
        if (totalWorkOrdersExceptCloseOnes != 0) {
            return onTimeCompletedWorkOrders * 100 / totalWorkOrdersExceptCloseOnes;
        } else return 0;
    }

    @Override
    public long getAllOpenWorkOrdersByUserIdBySpecifiedCurrentMonth
            (int month, String userAssignedId, List<String> workOrderStatusIdList) {
        System.out.println("111111");
        System.out.println(userAssignedId);
        System.out.println(month);
        System.out.println(workOrderStatusIdList);
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        System.out.println(date);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusIdList));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        Print.print("ERERREE", mongoOperations.find(query, WorkOrder.class));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllWorkOrdersList() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long getAllWorkOrdersBySpecifiedMonth(int month, String userAssignedId) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("creationDate").gte(date));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long getHighPriorityWorkOrdersBySpecifiedMonth(long month, String userAssignedId) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("priority").is(HIGHEST));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long getClosedWorkOrdersByUserIdBySpecifiedMonth(int month, String userAssignedId, List<String> workOrderStatusIdList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusIdList));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        Print.print("dfgdgdsge", mongoOperations.count(query, WorkOrder.class));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long getClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth(int month, String userAssignedId, List<String> workOrderStatusIdList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusIdList));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getOnTimeCompletedWorkOrdersBySpecifiedMonth(int month, String userAssignedId, List<WorkOrder> workOrderList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        List<WorkOrder> workOrderList1 = new ArrayList<>();
        workOrderList.forEach(workOrder -> {
            if (!workOrder.isDeleted() && workOrder.getEndDate() != null && workOrder.getRequiredCompletionDate() != null
                    && workOrder.getEndDate().before(workOrder.getRequiredCompletionDate())
                    && workOrder.getStartDate() != null
                    && workOrder.getStartDate().after(date)
                    && workOrder.getBasicInformation() != null
                    && workOrder.getBasicInformation().getUserAssignedId().equals(userAssignedId)) {
                workOrderList1.add(workOrder);
            }
        });
        return workOrderList1;
    }

    @Override
    public List<WorkOrder> getOverDueAndNotCompletedWorkOrdersBySpecifiedTime(int month, String userId, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userId));
        query.addCriteria(Criteria.where("requiredCompletionDate").lt(java.time.LocalDate.now()));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getOverDueAndCompletedWorkOrders(int month, String userId, List<WorkOrder> workOrderListOfUserInSpecifiedMonth) {
        List<WorkOrder> workOrderList = new ArrayList<>();
        workOrderListOfUserInSpecifiedMonth.forEach(workOrder -> {
            if (!workOrder.isDeleted()
                    && workOrder.getEndDate().after(workOrder.getRequiredCompletionDate())) {
                workOrderList.add(workOrder);
            }
        });
        return workOrderList;
    }

    @Override
    public long onTimeCompletedWorkOrdersInSpecifiedTime(int month, List<WorkOrder> workOrderListExceptClosedOnes) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        List<WorkOrder> workOrderList1 = new ArrayList<>();
        workOrderListExceptClosedOnes.forEach(workOrder -> {
            if (workOrder != null
                    && workOrder.getEndDate() != null
                    && workOrder.getRequiredCompletionDate() != null
                    && workOrder.getStartDate().after(date)
                    && (workOrder.getEndDate().before(workOrder.getRequiredCompletionDate())
                    || workOrder.getEndDate().equals(workOrder.getRequiredCompletionDate()))
            ) {
                workOrderList1.add(workOrder);
            }
        });
        return workOrderList1.size();
    }

    @Override
    public long onTimeCompletedWorkOrdersInSpecifiedTimeRate(long totalWorkOrdersInSpecifiedTime, long onTimeCompletedWorkOrders) {
        if (totalWorkOrdersInSpecifiedTime != 0) {
            return onTimeCompletedWorkOrders * 100 / totalWorkOrdersInSpecifiedTime;
        } else
            return 0;
    }

    @Override
    public List<WorkOrder> generateWorkOrdersForTodayByTodayScheduleMaintenanceLists
            (List<ScheduleMaintenanceBackup> todayScheduleMaintenanceBackupsForGeneratingWorkOrder) {

        List<WorkOrder> workOrderList = new ArrayList<>();
        for (ScheduleMaintenanceBackup scheduleMaintenanceBackup : todayScheduleMaintenanceBackupsForGeneratingWorkOrder) {
            WorkOrder workOrder = new WorkOrder(new CompletionDetail(), new BasicInformation());
            Print.print("scheduleRepetition", scheduleMaintenanceBackup);
            //cast long to int
            int numberOfDaysToEndWorkOrder = (int) scheduleMaintenanceBackup.getNumberOfDayForEndingEachScheduleMaintenance();
            Date today = scheduleMaintenanceBackup.getStartDate();
            Calendar calendar = Calendar.getInstance();
            //convert date to calender
            calendar.setTime(today);
            calendar.add(Calendar.DAY_OF_MONTH, numberOfDaysToEndWorkOrder);
            //convert calender to Date
            Date endTimeOfWorkOrder = calendar.getTime();

            // we set scheduleMaintenanceId as the workOrder id for using it in Task,TaskGroup,Notify,.. as referenceId

            // workOrder.setId(scheduleMaintenanceBackup.getScheduleMaintenanceId());
            workOrder.setTitle(scheduleMaintenanceBackup.getTitle());
            workOrder.setCode(scheduleMaintenanceBackup.getCode());
            workOrder.setTasks(scheduleMaintenanceBackup.getTaskList());
            workOrder.setStartDate(scheduleMaintenanceBackup.getStartDate());
            workOrder.setRequiredCompletionDate(endTimeOfWorkOrder);
            workOrder.setTaskGroups(scheduleMaintenanceBackup.getTaskGroupList());
            workOrder.setStatusId(scheduleMaintenanceBackup.getStatusId());
            workOrder.setImage(scheduleMaintenanceBackup.getImage());
            workOrder.setMaintenanceType(scheduleMaintenanceBackup.getMaintenanceType());
            workOrder.setProjectId(scheduleMaintenanceBackup.getProjectId());
            workOrder.setPriority(scheduleMaintenanceBackup.getPriority());
            workOrder.setAssetId(scheduleMaintenanceBackup.getAssetId());
            workOrder.setCode(scheduleMaintenanceBackup.getCode());

            workOrder.getCompletionDetail().setAdminNote(scheduleMaintenanceBackup.getAdminNote());
            workOrder.getCompletionDetail().setNote(scheduleMaintenanceBackup.getNote());
            workOrder.getCompletionDetail().setProblem(scheduleMaintenanceBackup.getProblem());
            workOrder.getCompletionDetail().setRootCause(scheduleMaintenanceBackup.getRootCause());
            workOrder.getCompletionDetail().setSolution(scheduleMaintenanceBackup.getSolution());
            workOrder.getCompletionDetail().setBudgetId(scheduleMaintenanceBackup.getBudgetId());
            workOrder.getCompletionDetail().setChargeDepartmentId(scheduleMaintenanceBackup.getChargeDepartmentId());

            workOrder.getBasicInformation().setLaborHour(scheduleMaintenanceBackup.getLaborHour());
            workOrder.getBasicInformation().setIssueSummary(scheduleMaintenanceBackup.getIssueSummary());
            workOrder.getBasicInformation().setActualLaborHour(scheduleMaintenanceBackup.getActualLaborHour());
            workOrder.getBasicInformation().setWorkInstruction(scheduleMaintenanceBackup.getWorkInstruction());
            workOrder.getBasicInformation().setUserAssignedId(scheduleMaintenanceBackup.getUserAssignedId());
            workOrder.getBasicInformation().setCompletedUserId(scheduleMaintenanceBackup.getCompletedUserId());

            workOrder.setActivityId(scheduleMaintenanceBackup.getActivityId());
            workOrder.setAssociatedScheduleMaintenanceId(scheduleMaintenanceBackup.getScheduleMaintenanceId());
            workOrder.setUsedParts(scheduleMaintenanceBackup.getUsedParts());
            workOrder.setDocumentsList(scheduleMaintenanceBackup.getDocumentsList());
            workOrder.setFromSchedule(true);
            workOrderList.add(workOrder);
        }
        mongoOperations.insertAll(workOrderList);
        return workOrderList;
    }

    @Override
    public long countOnTimeCompletedWorkOrdersBySpecifiedMonth(List<WorkOrder> workOrderListOfUserInSpecificMonth) {

        List<WorkOrder> workOrderList1 = new ArrayList<>();
        workOrderListOfUserInSpecificMonth.forEach(workOrder -> {
            if (workOrder.getEndDate() != null
                    && workOrder.getRequiredCompletionDate() != null
                    && workOrder.getEndDate().before(workOrder.getRequiredCompletionDate())) {
                workOrderList1.add(workOrder);
            }
        });
        return workOrderList1.size();
    }

    @Override
    public long countOverDueAndNotCompletedWorkOrdersBySpecifiedTime(int month, String userId, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userId));
        query.addCriteria(Criteria.where("startDate").gt(date));
        query.addCriteria(Criteria.where("requiredCompletionDate").lt(java.time.LocalDate.now()));
        return mongoOperations.find(query, WorkOrder.class).size();
    }

    @Override
    public long countOverDueAndCompletedWorkOrders(int month, String userAssignedId, List<WorkOrder> workOrderList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        List<WorkOrder> workOrderList1 = new ArrayList<>();
        for (WorkOrder workOrder : workOrderList) {
            if (workOrder.getEndDate() != null
                    && workOrder.getEndDate().after(workOrder.getRequiredCompletionDate())
                    && workOrder.getStartDate().after(date)) {
                workOrderList1.add(workOrder);
            }
        }
        return workOrderList1.size();
    }

    @Override
    public List<AssignedWorkOrderFilterDTO> getAllByFilterAndPaginationByUserId(String userId, WorkOrderDTO workOrderDTO, Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());
        ProjectionOperation projectionOperation = project()
                .and("id").as("id")
                .and("title").as("title")
                .and("code").as("code")
                .and("fromSchedule").as("fromSchedule")
                .and("priority").as("priority")
                .and("maintenanceType").as("maintenanceType")
                .and("startDate").as("startDate")
                .and("endDate").as("endDate")
                .and("creationDate").as("creationDate")
                .and(ConvertOperators.ToObjectId.toObjectId("$projectId")).as("projectId")
                .and(ConvertOperators.ToObjectId.toObjectId("$statusId")).as("statusId")
                .and(ConvertOperators.ToObjectId.toObjectId("$assetId")).as("assetId");

        ProjectionOperation projectionOperationAfterLookup = project()
                .and("id").as("id")
                .and("title").as("title")
                .and("code").as("code")
                .and("fromSchedule").as("fromSchedule")
                .and("priority").as("priority")
                .and("maintenanceType").as("maintenanceType")
                .and("startDate").as("startDate")
                .and("endDate").as("endDate")
                .and("creationDate").as("creationDate")
                .and("project._id").as("projectId")
                .and("project.name").as("projectName")
                .and("asset._id").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatus._id").as("statusId")
                .and("workOrderStatus.name").as("statusName");

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("basicInformation.userAssignedId").is(userId);
        criteria.and("rejectedWorkOrder").ne(true);

        if (workOrderDTO.getCode() != null && !workOrderDTO.getCode().equals(""))
            criteria.and("code").regex(workOrderDTO.getCode());

        if (workOrderDTO.getTitle() != null && !workOrderDTO.getTitle().equals(""))
            criteria.and("title").regex(workOrderDTO.getTitle());

        if (workOrderDTO.getStatusId() != null && !workOrderDTO.getStatusId().equals(""))
            criteria.and("statusId").is(workOrderDTO.getStatusId());

        if (workOrderDTO.getPriority() != null)
            criteria.and("priority").is(workOrderDTO.getPriority());

        if (workOrderDTO.getProjectId() != null && !workOrderDTO.getProjectId().equals(""))
            criteria.and("projectId").is(workOrderDTO.getProjectId());

        if (workOrderDTO.getMaintenanceType() != null)
            criteria.and("maintenanceType").is(workOrderDTO.getMaintenanceType());

        if (workOrderDTO.getAssetId() != null && !workOrderDTO.getAssetId().equals(""))
            criteria.and("assetId").is(workOrderDTO.getAssetId());

        if (workOrderDTO.getStartDate() != null && workOrderDTO.getEndDate() == null)
            criteria.and("startDate").gte(workOrderDTO.getStartDate());

        if (workOrderDTO.getStartDate() == null && workOrderDTO.getEndDate() != null)
            criteria.and("endDate").lte(workOrderDTO.getEndDate());

//        if (workOrderDTO.getStartDate() != null && workOrderDTO.getEndDate() != null)
//            criteria.andOperator(
//                    Criteria.where("startDate").gte(workOrderDTO.getStartDate()),
//                    Criteria.where("endDate").lte(workOrderDTO.getEndDate())
//            );

        if (workOrderDTO.getFromSchedule() != null)
            criteria.and("fromSchedule").is(workOrderDTO.getFromSchedule());

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                projectionOperation,
                lookup("asset", "assetId", "_id", "asset"),
                lookup("project", "projectId", "_id", "project"),
                lookup("workOrderStatus", "statusId", "_id", "workOrderStatus"),
                projectionOperationAfterLookup,
                limitOperation,
                skipOperation
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, AssignedWorkOrderFilterDTO.class).getMappedResults();
    }

    @Override
    public long countAllFilteredWorkOrderByUserId(String userId, WorkOrderDTO workOrderDTO) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("basicInformation.userAssignedId").is(userId);
        criteria.and("rejectedWorkOrder").ne(true);

        if (workOrderDTO.getCode() != null && !workOrderDTO.getCode().equals(""))
            criteria.and("code").regex(workOrderDTO.getCode());

        if (workOrderDTO.getTitle() != null && !workOrderDTO.getTitle().equals(""))
            criteria.and("title").regex(workOrderDTO.getTitle());

        if (workOrderDTO.getStatusId() != null && !workOrderDTO.getStatusId().equals(""))
            criteria.and("statusId").is(workOrderDTO.getStatusId());

        if (workOrderDTO.getPriority() != null)
            criteria.and("priority").is(workOrderDTO.getPriority());

        if (workOrderDTO.getProjectId() != null && !workOrderDTO.getProjectId().equals(""))
            criteria.and("projectId").is(workOrderDTO.getProjectId());

        if (workOrderDTO.getMaintenanceType() != null)
            criteria.and("maintenanceType").is(workOrderDTO.getMaintenanceType());

        if (workOrderDTO.getAssetId() != null && !workOrderDTO.getAssetId().equals(""))
            criteria.and("assetId").is(workOrderDTO.getAssetId());

        if (workOrderDTO.getStartDate() != null && workOrderDTO.getEndDate() == null)
            criteria.and("startDate").gte(workOrderDTO.getStartDate());

        if (workOrderDTO.getStartDate() == null && workOrderDTO.getEndDate() != null)
            criteria.and("endDate").lte(workOrderDTO.getEndDate());

        if (workOrderDTO.getFromSchedule() != null)
            criteria.and("fromSchedule").is(workOrderDTO.getFromSchedule());

//        if (workOrderDTO.getStartDate() != null && workOrderDTO.getEndDate() != null)
//            criteria.andOperator(
//                    Criteria.where("startDate").gte(workOrderDTO.getStartDate()),
//                    Criteria.where("endDate").lte(workOrderDTO.getEndDate())
//            );

//        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria));
        return mongoOperations.aggregate(aggregation, WorkOrder.class, WorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public long getAllWorkOrdersInSpecifiedMonth(int month, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countTotalWorkOrdersAfterSpecifiedMonth(int month, String userId, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("id").is(userId));
        query.addCriteria(Criteria.where("startDate").gte(date));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllWorkOrderListOfUserInSpecifiedMonth(int month, String userId) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userId));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getOnTimeCompletedWorkOrders(List<WorkOrder> workOrderListOfUserInSpecificMonth) {
        List<WorkOrder> workOrderList1 = new ArrayList<>();
        workOrderListOfUserInSpecificMonth.forEach(workOrder -> {
            if (!workOrder.isDeleted()
                    && workOrder.getEndDate().before(workOrder.getRequiredCompletionDate())
                    && workOrder.isAssignedToTechnician()) {
                workOrderList1.add(workOrder);
            }
        });
        return workOrderList1;
    }

    @Override
    public List<WorkOrder> getAllWorkOrderOfUser(String userAssignedId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllWorkOrdersExceptClosedOne(List<String> stringList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllWorkOrderListExceptClosedOnesOfUserInSpecifiedMonth(int month, String userId, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("creationDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userId));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllClosedWorkOrders(List<String> stringList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllClosedWorkOrdersOfUserInSpecificMonth(int month, String userAssignedId, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long getAllClosedWorkOrdersInSpecifiedMonth(int month, List<String> workOrderStatusIdList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusIdList));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        return mongoOperations.find(query, WorkOrder.class).size();
    }

    @Override
    public long getNumberOfOnTimeClosedWorkOrders(List<WorkOrder> closedWorkOrderList) {

        List<WorkOrder> workOrderList = new ArrayList<>();
        closedWorkOrderList.forEach(workOrder -> {
            if (workOrder != null
                    && workOrder.getEndDate() != null
                    && workOrder.getRequiredCompletionDate() != null
                    && (workOrder.getEndDate().before(workOrder.getRequiredCompletionDate())
                    || workOrder.getEndDate().equals(workOrder.getRequiredCompletionDate()))) {
                workOrderList.add(workOrder);
            }
        });
        return workOrderList.size();
    }

    @Override
    public List<WorkOrder> getAllOpenWorkOrderList(List<String> stringList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long numberOfOpenAndOverDueWorkOrderOfUserInSpecifiedTime(int month, String userId, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        System.out.println("123");
        System.out.println(java.time.LocalDate.now());

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userId));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("requiredCompletionDate").lt(java.time.LocalDate.now()));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        Print.print(mongoOperations.count(query, WorkOrder.class));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllOpenWorkOrdersListByUserIdBySpecifiedCurrentMonth(int month, String userId, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userId));
        query.addCriteria(Criteria.where("creationDate").gte(date));
        query.addCriteria(Criteria.where("requiredCompletionDate").lt(java.time.LocalDate.now()));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllClosedWorkOrdersOfUserInSpecifiedMonth(int month, String userAssignedId, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("creationDate").gte(date));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllWorkOrderListExceptDraftForUser(List<String> stringList, String userAssignedId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("priority").is(HIGH));
        Print.print("wwwww", mongoOperations.find(query, WorkOrder.class));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllWorkOrdersExceptDraftOfUser(int month, String userAssignedId, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllHighPriorityWorkOrders(List<String> stringList1) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(stringList1));
        query.addCriteria(Criteria.where("priority").is(HIGH));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllHighPriorityWorkOrderOfUser(String userAssignedId, List<String> stringList1) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("id").in(stringList1));
        query.addCriteria(Criteria.where("priority").is(HIGHEST));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getPendingWorkOrdersOfUserInSpecificMonth(int month, String userAssignedId, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("creationDate").gte(date));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long getAllOpenAndOverDueWorkOrderList(List<String> workOrderStatusIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusIdList));
        query.addCriteria(Criteria.where("requiredCompletionDate").lt(new Date()));
        return mongoOperations.find(query, WorkOrder.class).size();
    }


    @Override
    public WorkOrder getOneWorkOrderForNotification(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public WorkOrder getWorkOrderByNotifyReferenceId(String referenceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(referenceId));
        query.fields()
                .include("assetId")
                .include("name")
                .include("title")
                .include("id")
                .include("statusId");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public void generatingNewWorkOrderByTriggeredScheduleMaintenanceInDistanceRecord(ScheduleMaintenanceBackup newScheduleMaintenanceBackup) {
        System.out.println("entered");
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DAY_OF_MONTH, (int) newScheduleMaintenanceBackup.getNumberOfDayForEndingEachScheduleMaintenance());
        Date currentDatePlusDays = c.getTime();

        WorkOrder workOrder = new WorkOrder(new CompletionDetail(), new BasicInformation());
        if (newScheduleMaintenanceBackup.getCode() != null)
            workOrder.setCode(newScheduleMaintenanceBackup.getCode());
        if (newScheduleMaintenanceBackup.getTitle() != null)
            workOrder.setTitle(newScheduleMaintenanceBackup.getTitle());
        workOrder.setRequiredCompletionDate(currentDatePlusDays);
        if (newScheduleMaintenanceBackup.getImage() != null)
            workOrder.setImage(newScheduleMaintenanceBackup.getImage());
        if (newScheduleMaintenanceBackup.getAssetId() != null)
            workOrder.setAssetId(newScheduleMaintenanceBackup.getAssetId());
        if (newScheduleMaintenanceBackup.getProjectId() != null)
            workOrder.setProjectId(newScheduleMaintenanceBackup.getProjectId());
        if (newScheduleMaintenanceBackup.getTaskGroupList() != null)
            workOrder.setTaskGroups(newScheduleMaintenanceBackup.getTaskGroupList());
        if (newScheduleMaintenanceBackup.getStartDate() != null)
            workOrder.setCreationDate(newScheduleMaintenanceBackup.getStartDate());
        workOrder.setStartDate(new Date());
        workOrder.setEndDate(null);
        if (newScheduleMaintenanceBackup.getPriority() != null)
            workOrder.setPriority(newScheduleMaintenanceBackup.getPriority());
        if (newScheduleMaintenanceBackup.getMaintenanceType() != null)
            workOrder.setMaintenanceType(newScheduleMaintenanceBackup.getMaintenanceType());
        if (newScheduleMaintenanceBackup.getStatusId() != null)
            workOrder.setStatusId(newScheduleMaintenanceBackup.getStatusId());
        if (newScheduleMaintenanceBackup.getChargeDepartmentId() != null)
            workOrder.getCompletionDetail().setChargeDepartmentId(newScheduleMaintenanceBackup.getChargeDepartmentId());
        if (newScheduleMaintenanceBackup.getBudgetId() != null)
            workOrder.getCompletionDetail().setBudgetId(newScheduleMaintenanceBackup.getBudgetId());
        if (newScheduleMaintenanceBackup.getSolution() != null)
            workOrder.getCompletionDetail().setSolution(newScheduleMaintenanceBackup.getSolution());
        if (newScheduleMaintenanceBackup.getRootCause() != null)
            workOrder.getCompletionDetail().setRootCause(newScheduleMaintenanceBackup.getRootCause());
        if (newScheduleMaintenanceBackup.getProblem() != null)
            workOrder.getCompletionDetail().setProblem(newScheduleMaintenanceBackup.getProblem());
        if (newScheduleMaintenanceBackup.getNote() != null)
            workOrder.getCompletionDetail().setNote(newScheduleMaintenanceBackup.getNote());
        if (newScheduleMaintenanceBackup.getAdminNote() != null)
            workOrder.getCompletionDetail().setAdminNote(newScheduleMaintenanceBackup.getAdminNote());
        if (newScheduleMaintenanceBackup.getTaskList() != null)
            workOrder.setTasks(newScheduleMaintenanceBackup.getTaskList());
        if (newScheduleMaintenanceBackup.getWorkInstruction() != null)
            workOrder.getBasicInformation().setWorkInstruction(newScheduleMaintenanceBackup.getWorkInstruction());
        if (newScheduleMaintenanceBackup.getIssueSummary() != null)
            workOrder.getBasicInformation().setIssueSummary(newScheduleMaintenanceBackup.getIssueSummary());
        mongoOperations.save(workOrder);
    }

    @Override
    public WorkOrder getWorkOrderStatusId(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields()
                .include("statusId")
                .include("assetId");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public boolean checkIfUserExistWorkOrder(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("basicInformation.userAssignedId").is(userId),
                Criteria.where("taskGroups").is(userId),
                Criteria.where("tasks").is(userId)
        ));
        return mongoOperations.exists(query, WorkOrder.class);
    }

    @Override
    public void createWorkOrderForWorkRequest(String workRequestId, String assetId) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setAssetId(assetId);
        workOrder.setWorkRequestId(workRequestId);
        workOrder.setNotToBeShown(true);
    }

    @Override
    public WorkOrder createWorkOrderAccordingToAssociatedWorkRequest(WorkRequest workRequest) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkRequestId(workRequest.getId());
        workOrder.setPmSheetCode(workRequest.getPmSheetCode());
        workOrder.setAssetId(workRequest.getAssetId());
        workOrder.setStartDate(workRequest.getFailureDate());
        workOrder.setRequestedDate(workRequest.getRequestDate());
        //----------------------
        //از این به پایین رو فعلا نیاز نیستش
        workOrder.setPriority(workRequest.getPriority());
        workOrder.setMaintenanceType(workRequest.getMaintenanceType());
        workOrder.setAssignedToTechnician(false);
        workOrder.setRequestDescription(workRequest.getDescription());
        return mongoOperations.save(workOrder);
    }

    @Override
    public List<WorkOrder> generateWorkOrdersForDistanceMeasurementForTodayByTodayScheduleMaintenanceLists
            (List<ScheduleMaintenanceBackup> ArrivedTimeScheduleMaintenanceBackupList, long amount) {

        List<WorkOrder> generatedWorkOrderList = new ArrayList<>();
        ArrivedTimeScheduleMaintenanceBackupList.forEach(newScheduleMaintenanceBackup -> {
//            System.out.println("entered");
//            Date currentDate = new Date();
//            Calendar c = Calendar.getInstance();
//            c.setTime(currentDate);
//            c.add(Calendar.DAY_OF_MONTH, (int) newScheduleMaintenanceBackup.getNumberOfDayForEndingEachScheduleMaintenance());
//            Date currentDatePlusDays = c.getTime();

            WorkOrder workOrder = new WorkOrder(new CompletionDetail(), new BasicInformation());
            if (newScheduleMaintenanceBackup.getCode() != null) {
                workOrder.setCode(newScheduleMaintenanceBackup.getCode());
            }
            if (newScheduleMaintenanceBackup.getTitle() != null) {
                workOrder.setTitle(newScheduleMaintenanceBackup.getTitle());
            }
            if (newScheduleMaintenanceBackup.getImage() != null) {
                workOrder.setImage(newScheduleMaintenanceBackup.getImage());
            }
            if (newScheduleMaintenanceBackup.getAssetId() != null) {
                workOrder.setAssetId(newScheduleMaintenanceBackup.getAssetId());
            }
            if (newScheduleMaintenanceBackup.getProjectId() != null) {
                workOrder.setProjectId(newScheduleMaintenanceBackup.getProjectId());
            }
            if (newScheduleMaintenanceBackup.getTaskGroupList() != null) {
                workOrder.setTaskGroups(newScheduleMaintenanceBackup.getTaskGroupList());
            }
            if (newScheduleMaintenanceBackup.getStartDate() != null) {
                workOrder.setCreationDate(newScheduleMaintenanceBackup.getStartDate());
            }
            if (newScheduleMaintenanceBackup.getPriority() != null) {
                workOrder.setPriority(newScheduleMaintenanceBackup.getPriority());
            }
            if (newScheduleMaintenanceBackup.getMaintenanceType() != null) {
                workOrder.setMaintenanceType(newScheduleMaintenanceBackup.getMaintenanceType());
            }
            if (newScheduleMaintenanceBackup.getStatusId() != null) {
                workOrder.setStatusId(newScheduleMaintenanceBackup.getStatusId());
            }
            if (newScheduleMaintenanceBackup.getChargeDepartmentId() != null) {
                workOrder.getCompletionDetail().setChargeDepartmentId(newScheduleMaintenanceBackup.getChargeDepartmentId());
            }
            if (newScheduleMaintenanceBackup.getBudgetId() != null) {
                workOrder.getCompletionDetail().setBudgetId(newScheduleMaintenanceBackup.getBudgetId());
            }
            if (newScheduleMaintenanceBackup.getSolution() != null) {
                workOrder.getCompletionDetail().setSolution(newScheduleMaintenanceBackup.getSolution());
            }
            if (newScheduleMaintenanceBackup.getRootCause() != null) {
                workOrder.getCompletionDetail().setRootCause(newScheduleMaintenanceBackup.getRootCause());
            }
            if (newScheduleMaintenanceBackup.getProblem() != null) {
                workOrder.getCompletionDetail().setProblem(newScheduleMaintenanceBackup.getProblem());
            }
            if (newScheduleMaintenanceBackup.getNote() != null) {
                workOrder.getCompletionDetail().setNote(newScheduleMaintenanceBackup.getNote());
            }
            if (newScheduleMaintenanceBackup.getAdminNote() != null) {
                workOrder.getCompletionDetail().setAdminNote(newScheduleMaintenanceBackup.getAdminNote());
            }
            if (newScheduleMaintenanceBackup.getTaskList() != null) {
                workOrder.setTasks(newScheduleMaintenanceBackup.getTaskList());
            }
            if (newScheduleMaintenanceBackup.getWorkInstruction() != null) {
                workOrder.getBasicInformation().setWorkInstruction(newScheduleMaintenanceBackup.getWorkInstruction());
            }
            if (newScheduleMaintenanceBackup.getIssueSummary() != null) {
                workOrder.getBasicInformation().setIssueSummary(newScheduleMaintenanceBackup.getIssueSummary());
            }
            if (newScheduleMaintenanceBackup.getActivityId() != null) {
                workOrder.setActivityId(newScheduleMaintenanceBackup.getActivityId());
            }
            if (newScheduleMaintenanceBackup.getScheduleMaintenanceId() != null) {
                workOrder.setAssociatedScheduleMaintenanceId(newScheduleMaintenanceBackup.getScheduleMaintenanceId());
            }
            if (newScheduleMaintenanceBackup.getUsedParts() != null) {
                workOrder.setUsedParts(newScheduleMaintenanceBackup.getUsedParts());
            }
            if (newScheduleMaintenanceBackup.getDocumentsList() != null) {
                workOrder.setDocumentsList(newScheduleMaintenanceBackup.getDocumentsList());
            }

            workOrder.getBasicInformation().setLaborHour(newScheduleMaintenanceBackup.getLaborHour());
            workOrder.getBasicInformation().setActualLaborHour(newScheduleMaintenanceBackup.getActualLaborHour());

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date today = new Date();
            Date todayWithZeroTime = null;
            try {
                todayWithZeroTime = formatter.parse(formatter.format(today));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            workOrder.setStartDate(todayWithZeroTime);
            workOrder.setEndDate(null);
            workOrder.setFromSchedule(true);
            workOrder.setRequiredCompletionDate(null);
            generatedWorkOrderList.add(workOrder);
        });
        return (List<WorkOrder>) mongoOperations.insertAll(generatedWorkOrderList);
    }

    @Override
    public WorkOrder getRelevantWorkOrderOfFaultyAsset(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields()
                .include("id")
                .include("code")
                .include("assetId");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public void setTheUserIdAsAssignedUserForWorkOrder(String userId, String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        Update update = new Update();
        update.set("basicInformation.userAssignedId", userId);
        update.set("assignedToTechnician", true);
        mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public boolean ifProjectExistsInWorkOrder(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("projectId").is(projectId));
        return mongoOperations.exists(query, WorkOrder.class);
    }

    @Override
    public boolean ifBudgetExistsInWorkOrder(String budgetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("completionDetail.budgetId").is(budgetId));
        return mongoOperations.exists(query, WorkOrder.class);
    }

    @Override
    public boolean ifWorkStatusExistsInWorkOrder(String workOrderStatusId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").is(workOrderStatusId));
        return mongoOperations.exists(query, WorkOrder.class);
    }

    @Override
    public boolean ifChargeDepartmentExistInWorkOrder(String chargeDepartmentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("completionDetail.chargeDepartmentId").is(chargeDepartmentId));
        return mongoOperations.exists(query, WorkOrder.class);
    }

    @Override
    public boolean ifAssetExistsInWorkOrder(String assetId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetId").is(assetId));
        return mongoOperations.exists(query, WorkOrder.class);
    }

    @Override
    public boolean ifPartExistsInWorkOrder(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assetId").is(partId));
        return mongoOperations.exists(query, WorkOrder.class);
    }

    @Override
    public boolean ifTaskGroupExistsInWorkOrder(String taskGroupId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("taskGroups").is(taskGroupId));
        return mongoOperations.exists(query, WorkOrder.class);
    }

    @Override
    public WorkOrder getRelevantWorkOrder(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields()
                .include("id")
                .include("rejectedWorkOrder");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getWorkOrderListOfScheduledActivitySample(List<String> workOrderIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(workOrderIdList));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getInCompleteWorkOrders(List<String> workOrderStatusIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusIdList));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.fields()
                .include("id");
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealAllWorkOrdersInSpecifiedMonth(int month, String userAssignedId) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAllRealOpenWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<
            String> workOrderStatusListString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusListString));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getHighestPriorityWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<
            String> workOrderStatusListString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(workOrderStatusListString));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getOpenWorkOrderListWithUserAssignedIdAndStatus(String
                                                                                   userAssignedId, List<String> openWorkOrderStatusStringList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("statusId").in(openWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealHighPriorityWorkOrders(String userAssignedId, int month, List<
            String> allWorkOrderStatusExceptClosedString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(allWorkOrderStatusExceptClosedString));
        query.addCriteria(Criteria.where("priority").is(HIGH));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealAveragePriorityWorkOrders(String userAssignedId, int month, List<
            String> allWorkOrderStatusExceptClosedString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(allWorkOrderStatusExceptClosedString));
        query.addCriteria(Criteria.where("priority").is(MEDIUM));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealLowPriorityWorkOrders(String userAssignedId, int month, List<
            String> allWorkOrderStatusExceptClosedString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(allWorkOrderStatusExceptClosedString));
        query.addCriteria(Criteria.where("priority").is(LOW));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealVeryLowPriorityWorkOrders(String userAssignedId, int month, List<
            String> allWorkOrderStatusExceptClosedString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(allWorkOrderStatusExceptClosedString));
        query.addCriteria(Criteria.where("priority").is(LOWEST));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealMaintenanceTypeWorkOrders(String userAssignedId, int month, MaintenanceType
            maintenanceType, List<String> allWorkOrderStatusExceptClosedString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(allWorkOrderStatusExceptClosedString));
        query.addCriteria(Criteria.where("maintenanceType").is(maintenanceType));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long countRealHighestPriorityWorkOrders(String userAssignedId, int month, List<
            String> allWorkOrderStatusExceptClosedString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(allWorkOrderStatusExceptClosedString));
        query.addCriteria(Criteria.where("priority").is(HIGHEST));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countRealHighPriorityWorkOrders(String userAssignedId, int month, List<
            String> allWorkOrderStatusExceptClosedString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(allWorkOrderStatusExceptClosedString));
        query.addCriteria(Criteria.where("priority").is(HIGH));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countRealAveragePriorityWorkOrders(String userAssignedId, int month, List<
            String> allWorkOrderStatusExceptClosedString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(allWorkOrderStatusExceptClosedString));
        query.addCriteria(Criteria.where("priority").is(MEDIUM));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countRealLowPriorityWorkOrders(String userAssignedId, int month, List<
            String> allWorkOrderStatusExceptClosedString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(allWorkOrderStatusExceptClosedString));
        query.addCriteria(Criteria.where("priority").is(LOW));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countRealVeryLowPriorityWorkOrders(String userAssignedId, int month, List<
            String> allWorkOrderStatusExceptClosedString) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(allWorkOrderStatusExceptClosedString));
        query.addCriteria(Criteria.where("priority").is(LOWEST));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealLateAndOpenWorkOrders(String
                                                                userId, List<String> openWorkOrderStatusStringList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("requiredCompletionDate").lt(new Date()));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("statusId").in(openWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.fields()
                .include("id")
                .include("title")
                .include("code")
                .include("requiredCompletionDate");
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealPendingWorkOrder(String
                                                           userAssignedId, List<String> pendingWorkOrderStatusStringList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("statusId").in(pendingWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        query.fields()
                .include("id")
                .include("title")
                .include("code")
                .include("requiredCompletionDate");
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealCurrentWeekOpenWorkOrders(String
                                                                    userAssignedId, List<String> openWorkOrderStatusStringList) {
        LocalDate today = LocalDate.now();   //.now method from LocalDate returns the current date using the system clock and default time-zone.
        LocalDate saturday = today;
        while (saturday.getDayOfWeek() != DayOfWeek.FRIDAY) {
            saturday = saturday.minusDays(1);
        }
        Date currentWeekSaturday = Date.from(saturday.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("requiredCompletionDate").gt(currentWeekSaturday));
        query.addCriteria(Criteria.where("requiredCompletionDate").lte(new Date()));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long countRealOpenWorkOrders(String userAssignedId, int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(openWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealClosedWorkOrders(String userAssignedId, int month, List<
            String> closedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(closedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long countRealClosedWorkOrders(String userAssignedId, int month, List<
            String> closedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(closedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealPendingWorkOrders(String userAssignedId, int month, List<
            String> pendingWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(pendingWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealDraftWorkOrders(String userAssignedId, int month, List<
            String> draftWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(draftWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long countRealDraftWorkOrders(String userAssignedId, int month, List<
            String> draftWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(draftWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getRealOverDueWorkOrders(String userAssignedId, int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(openWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long countRealOverDueWorkOrders(String userAssignedId, int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(openWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.addCriteria(Criteria.where("requiredCompletionDate").lt(new Date()));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long realPlannedMaintenance(int month, List<String> closedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("statusId").in(closedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long realUnplannedWorkOrders(int month, List<String> closedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(closedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countGeneralRealClosedWorkOrders(int month, List<String> closedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(closedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countGeneralRealOnTimeClosedWorkOrders(int month, List<String> closedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(closedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.addCriteria(Criteria.where("requiredCompletionDate").lt(new Date()));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long countRealPendingWorkOrdersOfUserInSpecificMonth(int month, String
            userAssignedId, List<String> stringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(stringList));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> managerDashboardGetOverDueWorkOrders(List<String> openWorkOrderStatusStringList,
                                                                int month) {
        Print.print(month);
        Print.print("openWorkOrderStatusStringList", openWorkOrderStatusStringList);
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Criteria criteria = new Criteria() {
            @Override
            public Document getCriteriaObject() {
                Document obj = new Document();
                obj.put("$where", "this.endDate > this.requiredCompletionDate");
                return obj;
            }
        };
        Query query = new Query();
        query.addCriteria(criteria);
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("statusId").in(openWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getHighestPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId,
                                                                                  int month, List<String> exceptClosedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(exceptClosedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long countHighestPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<
            String> exceptClosedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(exceptClosedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.addCriteria(Criteria.where("priority").is(HIGHEST));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getHighPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId,
                                                                               int month, List<String> exceptClosedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(exceptClosedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.addCriteria(Criteria.where("priority").is(HIGH));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long countHighPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<
            String> exceptClosedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(exceptClosedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.addCriteria(Criteria.where("priority").is(HIGH));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getAveragePriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId,
                                                                                  int month, List<String> exceptClosedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(exceptClosedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.addCriteria(Criteria.where("priority").is(MEDIUM));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long countAveragePriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<
            String> exceptClosedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(exceptClosedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.addCriteria(Criteria.where("priority").is(MEDIUM));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<
            String> exceptClosedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(exceptClosedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.addCriteria(Criteria.where("priority").is(LOW));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long countLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<
            String> exceptClosedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(exceptClosedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.addCriteria(Criteria.where("priority").is(LOW));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrder> getVeryLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId,
                                                                                  int month, List<String> exceptClosedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(exceptClosedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.addCriteria(Criteria.where("priority").is(LOWEST));
        return mongoOperations.find(query, WorkOrder.class);
    }

    @Override
    public long countVeryLowPriorityNotPlannedWorkOrdersBySpecifiedMonth(String userAssignedId, int month, List<
            String> exceptClosedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("assignedToTechnician").is(true));
        query.addCriteria(Criteria.where("startDate").gte(date));
        query.addCriteria(Criteria.where("basicInformation.userAssignedId").is(userAssignedId));
        query.addCriteria(Criteria.where("statusId").in(exceptClosedWorkOrderStatusStringList));
        query.addCriteria(Criteria.where("fromSchedule").ne(true));
        query.addCriteria(Criteria.where("priority").is(LOWEST));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetHighestPriorityWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);
        criteria.and("priority").is(HIGHEST);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountHighestPriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);
        criteria.and("priority").is(HIGHEST);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetHighPriorityWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);
        criteria.and("priority").is(HIGH);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        Print.print("aggResult", aggregation.toString());
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountHighPriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);
        criteria.and("priority").is(HIGH);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetAveragePriorityWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);
        criteria.and("priority").is(MEDIUM);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountAveragePriorityWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);
        criteria.and("priority").is(MEDIUM);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetLowPriorityWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);
        criteria.and("priority").is(LOW);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountLowPriorityWorkOrders(int month, List<String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);
        criteria.and("priority").is(LOW);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetLowestPriorityWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);
        criteria.and("priority").is(LOWEST);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountLowestPriorityWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);
        criteria.and("priority").is(LOWEST);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetOpenWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountOpenWorkOrders(int month, List<String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetClosedWorkOrder(int month, List<
            String> closedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(closedWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountClosedWorkOrder(int month, List<String> closedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(closedWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetPendingWorkOrders(int month, List<
            String> pendingWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(pendingWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountPendingWorkOrders(int month, List<String> pendingWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(pendingWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetDraftWorkOrders(int month, List<
            String> draftWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(draftWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountDraftWorkOrders(int month, List<String> draftWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(draftWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardHighestPriorityPlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);
        criteria.and("priority").is(HIGHEST);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountHighestPriorityPlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);
        criteria.and("priority").is(HIGHEST);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetHighPriorityPlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);
        criteria.and("priority").is(HIGH);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountHighPriorityPlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);
        criteria.and("priority").is(HIGH);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetAveragePriorityPlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);
        criteria.and("priority").is(MEDIUM);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountAveragePriorityPlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);
        criteria.and("priority").is(MEDIUM);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetLowPriorityPlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);
        criteria.and("priority").is(LOW);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountLowPriorityPlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);
        criteria.and("priority").is(LOW);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetLowestPriorityPlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);
        criteria.and("priority").is(LOWEST);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountLowestPriorityPlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);
        criteria.and("priority").is(LOWEST);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetOpenPlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountOpenPlannedWorkOrders(int month, List<String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetClosedPlannedWorkOrders(int month, List<
            String> closedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(closedWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountClosedPlannedWorkOrders(int month, List<
            String> closedWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(closedWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetPendingPlannedWorkOrders(int month, List<
            String> pendingWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(pendingWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountPendingPlannedWorkOrders(int month, List<
            String> pendingWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(pendingWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardWorkOrderDTO> managerDashboardGetDraftPlannedWorkOrders(int month, List<
            String> draftWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(draftWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long managerDashboardCountDraftPlannedWorkOrders(int month, List<
            String> draftWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(draftWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardWorkOrderDTO.class).getMappedResults().size();
    }

    @Override
    public List<DashboardOverDueWorkOrderDTO> managerDashboardCountOverDueWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("requiredCompletionDate").as("workOrderRequiredCompletionDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("workOrderRequiredCompletionDate").as("workOrderRequiredCompletionDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardOverDueWorkOrderDTO.class).getMappedResults();

//        Criteria criteria = new Criteria() {
//            @Override
//            public Document getCriteriaObject() {
//                Document obj = new Document();
//                obj.put("$where", "this.endDate > this.requiredCompletionDate");
//                return obj;
//            }
//        };
    }

    @Override
    public List<DashboardOverDueWorkOrderDTO> managerDashboardGetOverDuePlannedWorkOrders(int month, List<
            String> openWorkOrderStatusStringList) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("startDate").gte(date);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").is(true);

        ProjectionOperation firstProjectionOperation = Aggregation.project()
                .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$projectId"))).as("projectIdObj")
                .and((ConvertOperators.ToObjectId.toObjectId("$statusId"))).as("statusIdObj")
                .and("id").as("workOrderId")
                .and("id").as("workOrderId")
                .and("startDate").as("workOrderStartDate")
                .and("endDate").as("workOrderEndDate")
                .and("requiredCompletionDate").as("workOrderRequiredCompletionDate")
                .and("projectId").as("projectId")
                .and("priority").as("workOrderPriority")
                .and("title").as("workOrderName")
                .and("code").as("workOrderCode")
                .and("assetId").as("assetId")
                .and("statusId").as("workOrderStatusId")
                .and("maintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        ProjectionOperation secondProjectionOperation = Aggregation.project()
                .and("workOrderId").as("workOrderId")
                .and("workOrderName").as("workOrderName")
                .and("workOrderCode").as("workOrderCode")
                .and("workOrderStartDate").as("workOrderStartDate")
                .and("workOrderEndDate").as("workOrderEndDate")
                .and("workOrderRequiredCompletionDate").as("workOrderRequiredCompletionDate")
                .and("projectId").as("projectId")
                .and("project.name").as("projectName")
                .and("workOrderPriority").as("workOrderPriority")
                .and("assetId").as("assetId")
                .and("asset.name").as("assetName")
                .and("workOrderStatusId").as("workOrderStatusId")
                .and("workOrderStatus.status").as("workOrderStatusName")
                .and("workOrderMaintenanceType").as("workOrderMaintenanceType")
                .and("fromSchedule").as("fromSchedule");

        LookupOperation assetLookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetIdObj")
                .foreignField("_id")
                .as("asset");
        LookupOperation projectLookupOperation = LookupOperation.newLookup()
                .from("project")
                .localField("projectIdObj")
                .foreignField("_id")
                .as("project");

        LookupOperation workOrderStatusLookupOperation = LookupOperation.newLookup()
                .from("workOrderStatus")
                .localField("statusIdObj")
                .foreignField("_id")
                .as("workOrderStatus");

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                firstProjectionOperation,
                assetLookupOperation,
                projectLookupOperation,
                workOrderStatusLookupOperation,
                secondProjectionOperation,
                unwind("projectName", true),
                unwind("workOrderStatusName", true),
                unwind("assetName", true)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, DashboardOverDueWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public long numberOfPlannedWorkOrders(int month) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Criteria criteria = new Criteria()
                .andOperator(Criteria.where("startDate").lte(new Date()),
                        Criteria.where("startDate").gte(date)
                );
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(criteria);
        query.addCriteria(Criteria.where("fromSchedule").is(true));
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public long numberOfAllWorkOrders(int month) {
        LocalDate now = LocalDate.now();
        LocalDate earlier = now.minusMonths(month);
        java.util.Date date = java.sql.Date.valueOf(earlier);
        Criteria criteria = new Criteria()
                .andOperator(Criteria.where("startDate").lte(new Date()),
                        Criteria.where("startDate").gte(date)
                );
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(criteria);
        return mongoOperations.count(query, WorkOrder.class);
    }

    @Override
    public List<UnScheduledWorkOrderDTO> managerDashboardUnscheduledWorkOrdersForBar() {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("requiredCompletionDate").is(null);
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("workOrderId")
                        .and("title").as("workOrderTitle")
                        .and("code").as("workOrderCode")
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, UnScheduledWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public List<AggregateResultPendingBarDTO> getPendingWorkOrdersForDashboardBar
            (List<String> pendingWorkOrderStatusStringList) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("statusId").in(pendingWorkOrderStatusStringList);
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("relatedId")
                        .and("title").as("title")
                        .and("code").as("code")
                        .and("requiredCompletionDate").as("requiredCompletionDate")
                        .and("endDate").as("endDate")
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, AggregateResultPendingBarDTO.class).getMappedResults();
    }

    @Override
    public List<LateBarDTO> managerDashboardLateWorkOrdersForBar(List<String> openWorkOrderStatusStringList) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("requiredCompletionDate").lt(new Date());
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("relatedId")
                        .and("title").as("title")
                        .and("code").as("code")
                        .and("requiredCompletionDate").as("requiredCompletionDate"));
        return mongoOperations.aggregate(aggregation, WorkOrder.class, LateBarDTO.class).getMappedResults();
    }

    @Override
    public List<CurrentWeekWorkOrderDTO> managerDashboardCurrentWeekWorkOrdersBar(List<String> openWorkOrderStatusStringList) {
        LocalDate today = LocalDate.now();   //.now method in LocalDate returns the current date using the system clock and default time-zone.
        LocalDate saturday = today;
        while (saturday.getDayOfWeek() != DayOfWeek.FRIDAY) {
            saturday = saturday.minusDays(1);
        }
        Date currentWeekSaturday = Date.from(saturday.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentWeekSaturday);
        cal.add(Calendar.DATE, 7);
        Date currentWeekFriday = cal.getTime();

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("statusId").in(openWorkOrderStatusStringList);
        criteria.and("fromSchedule").ne(true);
        criteria.andOperator(
                Criteria.where("startDate").gt(currentWeekSaturday),
                Criteria.where("startDate").lt(currentWeekFriday)
        );
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("relatedId")
                        .and("code").as("code")
                        .and("title").as("title")
                        .and("requiredCompletionDate").as("requiredCompletionDate")
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, CurrentWeekWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public List<CurrentWeekPlannedWorkOrderDTO> managerDashboardCurrentPlannedWorkOrdersBar() {
        LocalDate today = LocalDate.now();   //.now method from LocalDate returns the current date using the system clock and default time-zone.
        LocalDate saturday = today;
        while (saturday.getDayOfWeek() != DayOfWeek.FRIDAY) {
            saturday = saturday.minusDays(1);
        }
        Date currentWeekSaturday = Date.from(saturday.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentWeekSaturday);
        cal.add(Calendar.DATE, 7);
        Date currentWeekFriday = cal.getTime();

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("fromSchedule").is(true);
        criteria.andOperator(
                Criteria.where("startDate").gt(currentWeekSaturday),
                Criteria.where("startDate").lt(currentWeekFriday)
        );
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("relatedId")
                        .and("code").as("code")
                        .and("title").as("title")
                        .and("requiredCompletionDate").as("requiredCompletionDate")
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, CurrentWeekPlannedWorkOrderDTO.class).getMappedResults();
    }

    @Override
    public List<WorkOrder> getWorkOrderPriorityAndMaintenanceType(List<String> workRequestIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("workRequestId").in(workRequestIdList));
        query.fields()
                .include("id")
                .include("priority")
                .include("maintenanceType");
        return mongoOperations.find(query, WorkOrder.class);

    }

    @Override
    public void workOrderAcceptedByManager(String workOrderId, boolean workOrderAccepted) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        Update update = new Update();
        update.set("acceptedByManager", workOrderAccepted);
        mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public WorkOrder getBasicInformationOfWorkOrder(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields()
                .include("basicInformation");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public WorkOrder getTasksByWorkOrderId(String workOrderId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("id").is(workOrderId);

        Query query = new Query();
        query.addCriteria(criteria);
        query.fields()
                .include("tasks")
                .include("taskGroups");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public void addTaskId(String id, String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        Update update = new Update();
        update.push("tasks", id);
        mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public void deleteTaskFromWorkOrder(String workOrderId, String taskId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        Update update = new Update();
        update.pull("tasks", taskId);
        mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public void addUsedPartToWorkOrder(String workOrderId, String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        Update update = new Update();
        update.push("usedParts", partId);
        mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public void deleteUsedPartFromWorkOrder(String workOrderId, String partWithUsageCountId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        Update update = new Update();
        update.pull("usedParts", partWithUsageCountId);
        mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public void deleteDocumentFromWorkOrder(String documentFileId, String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        Update update = new Update();
        update.pull("documentsList", documentFileId);
        mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public void addDocumentToWorkOrder(String workOrderId, String documentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        Update update = new Update();
        update.push("documentsList", documentId);
        mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public WorkOrder getDocumentIdListOfWorkOrder(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields()
                .include("documentsList");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public WorkOrder getWorkOrderForRepository(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public WorkOrder getPartWithUsageCountOfWorkOrder(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields()
                .include("id")
                .include("usedParts");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public boolean newSaveWorkOrder(NewSaveDTO newSaveDTO) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setAssetId(newSaveDTO.getAssetId());
        workOrder.setTitle(newSaveDTO.getTitle());
        workOrder.setStartDate(newSaveDTO.getStartDate());
        workOrder.setEndDate(newSaveDTO.getEndDate());
        workOrder.setEndDate(newSaveDTO.getEndDate());
        workOrder.setRequestedDate(newSaveDTO.getRequestedDate());
        workOrder.setRequestDescription(newSaveDTO.getRequestDescription());
        workOrder.setFailureReason(newSaveDTO.getFailureReason());
        workOrder.setFailureReason(newSaveDTO.getFailureReason());
        workOrder.setSolution(newSaveDTO.getSolution());
        workOrder.setUserIdList(newSaveDTO.getUserIdList());
        workOrder.setAssetId(newSaveDTO.getAssetId());
        WorkOrder workOrder1 = mongoOperations.save(workOrder);
        if (workOrder1.getId() != null)
            return true;
        else
            return false;

    }

    @Override
    public boolean newUpdate(NewSaveDTO newSaveDTO) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(newSaveDTO.getId()));
        Update update = new Update();
        update.set("title", newSaveDTO.getTitle());
        update.set("startDate", newSaveDTO.getStartDate());
        update.set("endDate", newSaveDTO.getEndDate());
        update.set("repairDate", newSaveDTO.getRepairDate());
        update.set("requestDescription", newSaveDTO.getRequestDescription());
        update.set("failureReason", newSaveDTO.getFailureReason());
        update.set("solution", newSaveDTO.getSolution());
        update.set("userIdList", newSaveDTO.getUserIdList());
        update.set("assetId", newSaveDTO.getAssetId());
        update.set("usedPartList", newSaveDTO.getUsedPartList());
        update.set("partSupply", newSaveDTO.getPartSupply());
        update.set("failureDuration", newSaveDTO.getFailureDuration());
        update.set("repairDuration", newSaveDTO.getRepairDuration());
        update.set("pmSheetCode", newSaveDTO.getPmSheetCode());
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, WorkOrder.class);
//        if (updateResult.getModifiedCount() > 0) {
//            return true;
//        } else {
//            return false;
//        }
        return true;
    }

    @Override
    public boolean scheduleWorkOrderUpdate(ReqWorkOrderScheduleUpdateDTO entity) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(entity.getId()));
        Update update = new Update();
        update.set("estimateCompletionDate", entity.getEstimateCompletionDate());
        update.set("assetStatus", entity.getAssetStatus());
        update.set("importanceDegreeId", entity.getImportanceDegreeId());
        update.set("workCategoryId", entity.getWorkCategoryId());
        update.set("activityTypeId", entity.getActivityTypeId());
        update.set("activityTime", entity.getActivityTime());
        update.set("solution", entity.getSolution());
        update.set("userIdList", entity.getUserIdList());
        update.set("usedPartList", entity.getUsedPartList());
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, WorkOrder.class);
        if (updateResult.getModifiedCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public WorkOrderPrimaryDTO newGetOne(String workOrderId) {

        Criteria criteria = new Criteria();
        criteria.and("id").is(workOrderId);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("requestedDate").as("requestedDate")
                        .and("requestDescription").as("requestDescription")
                        .and("failureReason").as("failureReason")
                        .and("solution").as("solution")
                        .and("userIdList").as("userIdList")
                        .and("repairDate").as("repairDate")
                        .and("usedPartList").as("usedPartList")
                        .and("partSupply").as("partSupply")
                        .and("workRequestId").as("workRequestId")
                        .and("pmSheetCode").as("pmSheetCode")
                        .and(ConvertOperators.ToObjectId.toObjectId("$assetId")).as("assetId"),
                lookup("asset", "assetId", "_id", "asset"),
                project()
                        .and("id").as("id")
                        .and("title").as("title")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("requestedDate").as("requestedDate")
                        .and("requestDescription").as("requestDescription")
                        .and("failureReason").as("failureReason")
                        .and("solution").as("solution")
                        .and("repairDate").as("repairDate")
                        .and("userIdList").as("userIdList")
                        .and("usedPartList").as("usedPartList")
                        .and("partSupply").as("partSupply")
                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")
                        .and("workRequestId").as("workRequestId")
                        .and("pmSheetCode").as("pmSheetCode")
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, WorkOrderPrimaryDTO.class).getUniqueMappedResult();
    }

    @Override
    public List<UsedPartReport> usedPartOfWOrkOrder(UsedPartOfWorkOrder usedPartOfWorkOrder, Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria1 = new Criteria();
        if (usedPartOfWorkOrder.getFrom() != null && usedPartOfWorkOrder.getUntil() == null) {
            criteria1.and("repairDate").gte(usedPartOfWorkOrder.getFrom());
        }

        if (usedPartOfWorkOrder.getFrom() == null && usedPartOfWorkOrder.getUntil() != null) {
            criteria1.is("repairDate").lte(usedPartOfWorkOrder.getUntil());
        }

        if (usedPartOfWorkOrder.getFrom() != null && usedPartOfWorkOrder.getUntil() != null) {
            criteria1.andOperator(
                    Criteria.where("repairDate").gte(usedPartOfWorkOrder.getFrom()),
                    Criteria.where("repairDate").lte(usedPartOfWorkOrder.getUntil())
            );
        }

        Criteria criteria = new Criteria();
        criteria.and("partId").is(usedPartOfWorkOrder.getPartId());

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria1),
                unwind("usedPartList"),
                project()
                        .and(ConvertOperators.ToObjectId.toObjectId("$assetId")).as("assetId")
                        .and("id").as("workOrderId")
                        .and("usedPartList.usedNumber").as("usedNumber")
                        .and("usedPartList.partId").as("partId")
                        .and("repairDate").as("usedTime"),
                lookup("asset", "assetId", "_id", "asset"),
                project()
                        .and("workOrderId").as("workOrderId")
                        .and("usedNumber").as("usedNumber")
                        .and("partId").as("partId")
                        .and("usedTime").as("usedTime")
                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName"),
                match(criteria),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, UsedPartReport.class).getMappedResults();
    }

    @Override
    public long countUsedPart(UsedPartOfWorkOrder usedPartOfWorkOrder) {
        Criteria criteria1 = new Criteria();
        if (usedPartOfWorkOrder.getFrom() != null && usedPartOfWorkOrder.getUntil() == null) {
            criteria1.and("repairDate").gte(usedPartOfWorkOrder.getFrom());
        }

        if (usedPartOfWorkOrder.getFrom() == null && usedPartOfWorkOrder.getUntil() != null) {
            criteria1.is("repairDate").lte(usedPartOfWorkOrder.getUntil());
        }

        if (usedPartOfWorkOrder.getFrom() != null && usedPartOfWorkOrder.getUntil() != null) {
            criteria1.andOperator(
                    Criteria.where("repairDate").gte(usedPartOfWorkOrder.getFrom()),
                    Criteria.where("repairDate").lte(usedPartOfWorkOrder.getUntil())
            );
        }

        Criteria criteria = new Criteria();
        criteria.and("partId").is(usedPartOfWorkOrder.getPartId());

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria1),
                unwind("usedPartList"),
                project()
                        .and("usedPartList.partId").as("partId"),
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, UsedPartReport.class).getMappedResults().size();
    }

    @Override
    public CountUsedPartDTO countUsedPartOfWorkOrder(UsedPartOfWorkOrder usedPartOfWorkOrder) {
        Criteria criteria1 = new Criteria();
        if (usedPartOfWorkOrder.getFrom() != null && usedPartOfWorkOrder.getUntil() == null) {
            criteria1.and("endDate").gte(usedPartOfWorkOrder.getFrom());
        }

        if (usedPartOfWorkOrder.getFrom() == null && usedPartOfWorkOrder.getUntil() != null) {
            criteria1.is("endDate").lte(usedPartOfWorkOrder.getUntil());
        }

        if (usedPartOfWorkOrder.getFrom() != null && usedPartOfWorkOrder.getUntil() != null) {
            criteria1.andOperator(
                    Criteria.where("repairDate").gte(usedPartOfWorkOrder.getFrom()),
                    Criteria.where("repairDate").lte(usedPartOfWorkOrder.getUntil())
            );
        }

        Criteria criteria = new Criteria();
        criteria.and("partId").is(usedPartOfWorkOrder.getPartId());

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria1),
                unwind("usedPartList"),
                project()
                        .and("usedPartList.usedNumber").as("usedNumber")
                        .and("usedPartList.partId").as("partId"),
                match(criteria),
                Aggregation.group("partId")
                        .sum("usedNumber")
                        .as("totalNumber")
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, CountUsedPartDTO.class).getUniqueMappedResult();
    }

    @Override
    public List<PersonnelFunctionGetAllDTO> personnelFunction(PersonnelFunctionDTO personnelFunctionDTO, Pageable pageable, Integer totalElement) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();

        if (personnelFunctionDTO.getFrom() != null && personnelFunctionDTO.getUntil() == null) {
            criteria.and("repairDate").gte(personnelFunctionDTO.getFrom());
        }

        if (personnelFunctionDTO.getFrom() == null && personnelFunctionDTO.getUntil() != null) {
            criteria.and("repairDate").lte(personnelFunctionDTO.getUntil());
        }

        if (personnelFunctionDTO.getFrom() != null && personnelFunctionDTO.getUntil() != null) {
            criteria.andOperator(
                    Criteria.where("repairDate").gte(personnelFunctionDTO.getFrom()),
                    Criteria.where("repairDate").lte(personnelFunctionDTO.getUntil())
            );
        }

        criteria.and("deleted").ne(true);
        criteria.and("userIdList").is(personnelFunctionDTO.getUserId());
        criteria.and("acceptedByManager").is(true);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                skipOperation,
                limitOperation,
                project()
                        .and("repairDate").as("repairDate")
                        .and("failureDuration").as("duration")
                        .and("id").as("workOrderId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$assetId")).as("assetId"),
                lookup("asset", "assetId", "_id", "asset"),
                project()
                        .and("repairDate").as("repairDate")
                        .and("duration").as("duration")
                        .and("workOrderId").as("workOrderId")
                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, PersonnelFunctionGetAllDTO.class).getMappedResults();
    }

    @Override
    public long countPersonnelFunction(PersonnelFunctionDTO personnelFunctionDTO, Pageable pageable, Integer totalElement) {

        Criteria criteria = new Criteria();

        if (personnelFunctionDTO.getFrom() != null && personnelFunctionDTO.getUntil() == null) {
            criteria.and("repairDate").gte(personnelFunctionDTO.getFrom());
        }

        if (personnelFunctionDTO.getFrom() == null && personnelFunctionDTO.getUntil() != null) {
            criteria.and("repairDate").lte(personnelFunctionDTO.getUntil());
        }

        if (personnelFunctionDTO.getFrom() != null && personnelFunctionDTO.getUntil() != null) {
            criteria.andOperator(
                    Criteria.where("repairDate").gte(personnelFunctionDTO.getFrom()),
                    Criteria.where("repairDate").lte(personnelFunctionDTO.getUntil())
            );
        }

        criteria.and("deleted").ne(true);
        criteria.and("userIdList").is(personnelFunctionDTO.getUserId());
        criteria.and("acceptedByManager").is(true);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria)
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, PersonnelFunctionGetAllDTO.class).getMappedResults().size();
    }

    @Override
    public TotalWorkedTimeDTO TotalWorkedTimeOfPersonnel(PersonnelFunctionDTO personnelFunctionDTO) {
        Criteria criteria = new Criteria();

        if (personnelFunctionDTO.getFrom() != null && personnelFunctionDTO.getUntil() == null) {
            criteria.and("repairDate").gte(personnelFunctionDTO.getFrom());
        }

        if (personnelFunctionDTO.getFrom() == null && personnelFunctionDTO.getUntil() != null) {
            criteria.and("repairDate").lte(personnelFunctionDTO.getUntil());
        }

        if (personnelFunctionDTO.getFrom() != null && personnelFunctionDTO.getUntil() != null) {
            criteria.andOperator(
                    Criteria.where("repairDate").gte(personnelFunctionDTO.getFrom()),
                    Criteria.where("repairDate").lte(personnelFunctionDTO.getUntil())
            );
        }

        criteria.and("deleted").ne(true);
        criteria.and("userIdList").is(personnelFunctionDTO.getUserId());
        criteria.and("acceptedByManager").is(true);

        Aggregation aggregation = Aggregation.newAggregation(
                unwind("userIdList", true),
                match(criteria),
                Aggregation.group("userIdList")
                        .sum("failureDuration")
                        .as("totalWorkedTime")
        );

        return mongoOperations.aggregate(aggregation, WorkOrder.class, TotalWorkedTimeDTO.class).getUniqueMappedResult();
    }

    @Override
    public List<MtbfReturn> mtbfCalculation(MtbfDTO mtbfDTO) {
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("startDate").gte(mtbfDTO.getFrom()),
                Criteria.where("startDate").lte(mtbfDTO.getUntil())
        );

        if (mtbfDTO.getAssetId() != null) {
            criteria.and("assetId").is(mtbfDTO.getAssetId());
        }
        criteria.and("acceptedByManager").is(true);
        criteria.and("deleted").ne(true);
        criteria.and("fromSchedule").ne(true);


        List<AggregationOperation> aggList = new ArrayList<>();
        aggList.add(
                Aggregation.match(criteria)
        );


        aggList.add(
                Aggregation.project()
                        .and("startDate").as("startDate")
                        .and("startDate").dateAsFormattedString("%Y-%m-%d").as("yearMonthDay")
                        .and("startDate").dateAsFormattedString("%Y-%m").as("yearMonth")
                        .and("startDate").dateAsFormattedString("%Y").as("year")
                        .and("failureDuration").as("failureDuration")
        );

        if (mtbfDTO.getRange().equals(Range.daily)) {
            aggList.add(
                    Aggregation.group("yearMonthDay")
                            .first("startDate").as("startDate")
                            .first("yearMonthDay").as("date")
                            .sum("failureDuration").as("failureDuration")
                            .count().as("count")
            );

            aggList.add(
                    Aggregation.project()
                            .and("startDate").as("startDate")
                            .and("date").as("date")
                            .and("failureDuration").as("failureDuration")
                            .and(ArithmeticOperators.Subtract.valueOf(24 * 60).subtract("failureDuration")).as("workTimeDuration")
                            .and("count").as("count")
            );
        }

        if (mtbfDTO.getRange().equals(Range.monthly)) {
            aggList.add(
                    Aggregation.group("yearMonth")
                            .first("yearMonthDay").as("date")
                            .sum("failureDuration").as("failureDuration")
                            .count().as("count")
            );

            aggList.add(
                    Aggregation.project()
                            .and("count").as("count")
                            .and("date").as("date")
                            .and(ArithmeticOperators.Subtract.valueOf(30 * 24 * 60).subtract("failureDuration")).as("workTimeDuration")
            );
        }

        if (mtbfDTO.getRange().equals(Range.yearly)) {
            aggList.add(
                    Aggregation.group("year")
                            .first("yearMonthDay").as("date")
                            .sum("failureDuration").as("failureDuration")
                            .count().as("count")
            );

            aggList.add(
                    Aggregation.project()
                            .and("count").as("count")
                            .and("date").as("date")
                            .and(ArithmeticOperators.Subtract.valueOf(365 * 30 * 24 * 60).subtract("failureDuration")).as("workTimeDuration")
            );
        }

        aggList.add(
                Aggregation.project()
                        .and("startDate").as("startDate")
                        .and("date").as("date")
                        .and("failureDuration").as("failureDuration")
                        .and("count").as("count")
                        .and(ArithmeticOperators.Divide.valueOf("workTimeDuration").divideBy("count")).as("workTimeDuration")
        );
        aggList.add(
                Aggregation.sort(Sort.Direction.ASC
                        , "startDate")
        );
        Aggregation aggregation = Aggregation.newAggregation(aggList);
        return mongoOperations.aggregate(aggregation, WorkOrder.class, MtbfReturn.class).getMappedResults();
    }

    @Override
    public List<MttrReturn> mttrCalculation(MttrDTO mttrDTO) {
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("startDate").gte(mttrDTO.getFrom()),
                Criteria.where("startDate").lte(mttrDTO.getUntil())
        );

        if (mttrDTO.getAssetId() != null) {
            criteria.and("assetId").is(mttrDTO.getAssetId());
        }
        criteria.and("acceptedByManager").is(true);
        criteria.and("deleted").ne(true);
        criteria.and("fromSchedule").ne(true);

        List<AggregationOperation> aggList = new ArrayList<>();
        aggList.add(
                Aggregation.match(criteria)
        );

        aggList.add(
                Aggregation.project()
                        .and("repairDuration").as("repairDuration")
                        .and("repairDate").as("repairDate")
                        .and("repairDate").dateAsFormattedString("%Y-%m-%d").as("yearMonthDay")
                        .and("repairDate").dateAsFormattedString("%Y-%m").as("yearMonth")
                        .and("repairDate").dateAsFormattedString("%Y").as("year")
                        .and("repairDuration").as("repairDuration")
        );

        if (mttrDTO.getRange().equals(Range.daily)) {
            aggList.add(
                    Aggregation.group("yearMonthDay")
                            .first("repairDate").as("repairDate")
                            .first("yearMonthDay").as("date")
                            .sum("repairDuration").as("repairDuration")
                            .count().as("count")
            );

            aggList.add(
                    Aggregation.project()
                            .and("repairDate").as("repairDate")
                            .and("date").as("date")
                            .and("count").as("count")
                            .and("repairDuration").as("repairDuration")
                            .and(ArithmeticOperators.Divide.valueOf("repairDuration").divideBy("count")).as("mttr")
            );
        }
        aggList.add(
                Aggregation.sort(Sort.Direction.ASC, "repairDate")
        );
        Aggregation aggregation = Aggregation.newAggregation(aggList);
        return mongoOperations.aggregate(aggregation, WorkOrder.class, MttrReturn.class).getMappedResults();
    }

    @Override
    public List<MtbfTableReturn> mtbfTable(MtbfTableDTO mtbfTableDTO) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("acceptedByManager").is(true);
        criteria.and("fromSchedule").ne(true);
        criteria.andOperator(
                Criteria.where("startDate").gte(mtbfTableDTO.getFrom()),
                Criteria.where("startDate").lte(mtbfTableDTO.getUntil())
        );

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                Aggregation.group("assetId")
                        .sum("failureDuration").as("failureDuration")
                        .sum("repairDuration").as("repairDuration")
                        .first("assetId").as("assetId")
                        .count().as("count"),
                project()
                        .and(ArithmeticOperators.Subtract.valueOf(mtbfTableDTO.getNumber()).subtract("failureDuration")).as("workTime")
                        .and("assetId").as("assetId")
                        .and("failureDuration").as("failureDuration")
                        .and("repairDuration").as("repairDuration")
                        .and("count").as("count"),
                project()
                        .and(ArithmeticOperators.Divide.valueOf("workTime").divideBy("count")).as("mtbf")
                        .and(ConvertOperators.ToObjectId.toObjectId("$assetId")).as("assetId")
                        .and("failureDuration").as("failureDuration")
                        .and("repairDuration").as("repairDuration")
                        .and("count").as("count"),
                lookup("asset", "assetId", "_id", "asset"),
                project()
                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")
                        .and("asset.code").as("assetCode")
                        .and("failureDuration").as("failureDuration")
                        .and("repairDuration").as("repairDuration")
                        .and("count").as("count")
                        .and("mtbf").as("mtbf")
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, MtbfTableReturn.class).getMappedResults();
    }

    @Override
    public List<MttrTableReturn> mttrTable(MttrTableDTO mttrTableDTO) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("acceptedByManager").is(true);
        criteria.and("fromSchedule").ne(true);

        criteria.andOperator(
                Criteria.where("startDate").gte(mttrTableDTO.getFrom()),
                Criteria.where("startDate").lte(mttrTableDTO.getUntil())
        );

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                Aggregation.group("assetId")
                        .first("assetId").as("assetId")
                        .sum("repairDuration").as("repairDuration")
                        .count().as("count"),
                project()
                        .and("assetId").as("assetId")
                        .and(ArithmeticOperators.Divide.valueOf("repairDuration").divideBy("count")).as("mttr")
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, MttrTableReturn.class).getMappedResults();
    }

    @Override
    public WorkOrder isAcceptedByManager(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(workOrderId));
        query.fields()
                .include("id")
                .include("workRequestId")
                .include("acceptedByManager");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public WorkOrder getWorkOrderTechnicians(String workRequestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("workRequestId").is(workRequestId));
        query.fields()
                .include("id")
                .include("userIdList");
        return mongoOperations.findOne(query, WorkOrder.class);
    }

    @Override
    public List<WorkOrderDTO> getAllWorkOrderForExcel(WorkOrderDTO workOrderDTO) {

        Criteria criteria = new Criteria();
        ProjectionOperation projectionOperation = project()
                .and("id").as("id")
                .and(ConvertOperators.ToObjectId.toObjectId("$assetId")).as("assetId")
                .and("startDate").as("startDate")
                .and("endDate").as("endDate")
                .and("fromSchedule").as("fromSchedule");

        List<Criteria> criteriaList = new ArrayList<>();

        criteriaList.add(Criteria.where("deleted").ne(true));
        criteriaList.add(Criteria.where("acceptedByManager").is(true));
        criteriaList.add(Criteria.where("fromSchedule").ne(true));

        if (workOrderDTO.getFromSchedule() != null)
            criteriaList.add(Criteria.where("fromSchedule").is(workOrderDTO.getFromSchedule()));

        if (workOrderDTO.getAssetId() != null && !workOrderDTO.getAssetId().equals(""))
            criteriaList.add(Criteria.where("assetId").is(workOrderDTO.getAssetId()));

        if (workOrderDTO.getFailureDateFrom() != null && workOrderDTO.getFailureDateUntil() == null)
            criteriaList.add(Criteria.where("startDate").gte(workOrderDTO.getFailureDateFrom()));

        if (workOrderDTO.getFailureDateUntil() != null && workOrderDTO.getFailureDateFrom() == null)
            criteriaList.add(Criteria.where("startDate").lte(workOrderDTO.getFailureDateUntil()));

        if (workOrderDTO.getFailureDateUntil() != null && workOrderDTO.getFailureDateFrom() != null) {
            criteriaList.add(criteria.andOperator(
                    Criteria.where("startDate").gte(workOrderDTO.getFailureDateFrom()),
                    Criteria.where("startDate").lte(workOrderDTO.getFailureDateUntil())
            ));
        }
        criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                sort(Sort.Direction.DESC, "startDate"),
                projectionOperation,
                lookup("asset", "assetId", "_id", "asset"),
                project()
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("fromSchedule").as("fromSchedule")
                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")

        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, WorkOrderDTO.class).getMappedResults();
    }

    @Override
    public List<MdtReturn> mdtCalculation(MdtDTO mdtDTO) {
        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("startDate").gte(mdtDTO.getFrom()),
                Criteria.where("startDate").lte(mdtDTO.getUntil())
        );

        if (mdtDTO.getAssetId() != null) {
            criteria.and("assetId").is(mdtDTO.getAssetId());
        }
        criteria.and("acceptedByManager").is(true);
        criteria.and("deleted").ne(true);
        criteria.and("fromSchedule").ne(true);

        List<AggregationOperation> aggList = new ArrayList<>();
        aggList.add(
                Aggregation.match(criteria)
        );

        aggList.add(
                Aggregation.project()
                        .and("failureDuration").as("failureDuration")
                        .and("startDate").as("failureDate")
                        .and("startDate").dateAsFormattedString("%Y-%m-%d").as("yearMonthDay")
                        .and("startDate").dateAsFormattedString("%Y-%m").as("yearMonth")
                        .and("startDate").dateAsFormattedString("%Y").as("year")
                        .and("failureDuration").as("failureDuration")
        );

        if (mdtDTO.getRange().equals(Range.daily)) {
            aggList.add(
                    Aggregation.group("yearMonthDay")
                            .first("failureDate").as("failureDate")
                            .first("yearMonthDay").as("date")
                            .sum("failureDuration").as("failureDuration")
                            .count().as("count")
            );

            aggList.add(
                    Aggregation.project()
                            .and("failureDate").as("failureDate")
                            .and("date").as("date")
                            .and("count").as("count")
                            .and("failureDuration").as("failureDuration")
                            .and(ArithmeticOperators.Divide.valueOf("failureDuration").divideBy("count")).as("mdt")
            );
        }
        aggList.add(
                Aggregation.sort(Sort.Direction.ASC, "failureDate")
        );
        Aggregation aggregation = Aggregation.newAggregation(aggList);
        return mongoOperations.aggregate(aggregation, WorkOrder.class, MdtReturn.class).getMappedResults();
    }

    @Override
    public List<MdtTableReturn> mdtTable(MdtTableDTO mdtTableDTO) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("acceptedByManager").is(true);
        criteria.and("fromSchedule").ne(true);

        criteria.andOperator(
                Criteria.where("startDate").gte(mdtTableDTO.getFrom()),
                Criteria.where("startDate").lte(mdtTableDTO.getUntil())
        );

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                Aggregation.group("assetId")
                        .first("assetId").as("assetId")
                        .sum("failureDuration").as("failureDuration")
                        .count().as("count"),
                project()
                        .and("assetId").as("assetId")
                        .and(ArithmeticOperators.Divide.valueOf("failureDuration").divideBy("count")).as("mdt")
        );
        return mongoOperations.aggregate(aggregation, WorkOrder.class, MdtTableReturn.class).getMappedResults();
    }

    @Override
    public WorkOrder createWorkOrderAccordingToAssociatedWorkRequest(WorkOrderSchedule entity) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, Math.toIntExact(entity.getEstimateCompletionDate()));
        Date date = calendar.getTime();


        WorkOrder workOrder = new WorkOrder();
        workOrder.setAssetId(entity.getAssetId());

        workOrder.setStartDate(new Date());
        workOrder.setRequestedDate(date);

        workOrder.setSolution(entity.getSolution());
        workOrder.setActivityTime(entity.getActivityTime());
        workOrder.setAssetStatus(entity.getAssetStatus());
        workOrder.setActivityId(entity.getActivityId());
        workOrder.setActivityTypeId(entity.getActivityTypeId());
        workOrder.setWorkCategoryId(entity.getWorkCategoryId());
        workOrder.setImportanceDegreeId(entity.getImportanceDegreeId());
        workOrder.setMainSubSystemId(entity.getMainSubSystemId());
        workOrder.setMinorSubSystem(entity.getMinorSubSystem());
        workOrder.setAssociatedScheduleMaintenanceId(entity.getId());
        workOrder.setUserIdList(entity.getUserIdList());
        workOrder.setUsedPartList(entity.getUsedPartList());
        workOrder.setAcceptedByManager(false);
        workOrder.setFromSchedule(true);
        workOrder.setEstimateCompletionDate(entity.getEstimateCompletionDate());

        return mongoOperations.save(workOrder);
    }

    @Override
    public WorkOrderScheduleDTO getOneScheduleWorkOrder(String workOrderId) {

        Criteria criteria = new Criteria();
        criteria.and("id").is(workOrderId);

        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and("id").as("id")
                        .and(ConvertOperators.ToObjectId.toObjectId("$assetId")).as("assetId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$mainSubSystemId")).as("mainSubSystemId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$workCategoryId")).as("workCategoryId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$activityTypeId")).as("activityTypeId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$importanceDegreeId")).as("importanceDegreeId")
                        .and("minorSubSystem").as("minorSubSystem")
                        .and("solution").as("solution")
                        .and("activityTime").as("activityTime")
                        .and("startDate").as("startDate")
                        .and("assetStatus").as("assetStatus")
                        .and("endDate").as("endDate")
                        .and("usedPartList").as("usedPartList")
                        .and("userIdList").as("userIdList")
                        .and("associatedScheduleMaintenanceId").as("associatedScheduleMaintenanceId"),
                lookup("asset", "assetId", "_id", "asset"),
                lookup("asset", "mainSubSystemId", "_id", "mainSubSystem"),
                lookup("workCategory", "workCategoryId", "_id", "workCategory"),
                lookup("activityType", "$activityTypeId", "_id", "activityType"),
                lookup("importanceDegree", "importanceDegreeId", "_id", "importanceDegree"),
                project()
                        .and("id").as("id")
                        .and("minorSubSystem").as("minorSubSystem")
                        .and("solution").as("solution")
                        .and("activityTime").as("activityTime")
                        .and("startDate").as("startDate")
                        .and("assetStatus").as("assetStatus")
                        .and("endDate").as("endDate")
                        .and("usedPartList").as("usedPartList")
                        .and("userIdList").as("userIdList")
                        .and("associatedScheduleMaintenanceId").as("associatedScheduleMaintenanceId")
                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")
                        .and("mainSubSystem._id").as("mainSubSystemId")
                        .and("mainSubSystem.name").as("mainSubSystemName")
                        .and("workCategory._id").as("workCategoryId")
                        .and("workCategory.name").as("workCategoryName")
                        .and("activityType._id").as("activityTypeId")
                        .and("activityType.name").as("activityTypeName")
                        .and("importanceDegree._id").as("importanceDegreeId")
                        .and("importanceDegree.name").as("importanceDegreeName")
        );

        return mongoOperations.aggregate(aggregation, WorkOrder.class, WorkOrderScheduleDTO.class).getUniqueMappedResult();
    }

    @Override
    public boolean updateScheduleWorkOrder(WorkOrderScheduleDTO workOrderScheduleDTO) {

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(workOrderScheduleDTO.getId()));

        Update update = new Update();
        update.set("assetId", workOrderScheduleDTO.getAssetId());
        update.set("mainSubSystemId", workOrderScheduleDTO.getMainSubSystemId());
        update.set("minorSubSystem", workOrderScheduleDTO.getMinorSubSystem());
        update.set("workCategoryId", workOrderScheduleDTO.getWorkCategoryId());
        update.set("activityTypeId", workOrderScheduleDTO.getActivityTypeId());
        update.set("importanceDegreeId", workOrderScheduleDTO.getImportanceDegreeId());
        update.set("solution", workOrderScheduleDTO.getSolution());
        update.set("activityTime", workOrderScheduleDTO.getActivityTime());
        update.set("startDate", workOrderScheduleDTO.getStartDate());
        update.set("endDate", workOrderScheduleDTO.getEndDate());
        update.set("assetStatus", workOrderScheduleDTO.getAssetStatus());
        update.set("usedPartList", workOrderScheduleDTO.getUsedPartList());
        update.set("userIdList", workOrderScheduleDTO.getUserIdList());
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, WorkOrder.class);
        if (updateResult.getModifiedCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deleteWorkOrderSchedule(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("deleted", true);
        update.set("rejectedInSchedule", true);
        mongoOperations.updateFirst(query, update, WorkOrder.class);
    }

    @Override
    public void setEndDateOfWorkOrderSchedule(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(workOrderId));
        Update update = new Update();
        update.set("endDate", new Date());
        mongoOperations.updateFirst(query, update, WorkOrder.class);
    }
}

