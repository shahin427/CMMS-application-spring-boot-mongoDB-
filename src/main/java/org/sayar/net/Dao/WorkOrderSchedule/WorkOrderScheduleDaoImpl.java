package org.sayar.net.Dao.WorkOrderSchedule;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Controller.WorkOrderSchedule.dto.*;
import org.sayar.net.Controller.newController.dto.ReqWorkOrderForCalendarGetListDTO;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.WorkOrderSchedule;
import org.sayar.net.Model.newModel.Part.Part;
import org.sayar.net.Scheduler.Schedule;
import org.sayar.net.Service.WorkOrderSchedule.WorkOrderScheduleService;
import org.sayar.net.Service.newService.PartService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.*;

import static org.sayar.net.Model.WorkOrderSchedule.Frequency.*;
import static org.sayar.net.Model.WorkOrderSchedule.RunStatus.DE_ACTIVE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@Repository("WorkOrderScheduleDaoImpl")
public class WorkOrderScheduleDaoImpl extends GeneralDaoImpl<WorkOrderSchedule> implements WorkOrderScheduleDao {

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private PartService partService;

    @Autowired
    private Schedule schedule;

    @Autowired
    private WorkOrderScheduleService workOrderScheduleService;


    public WorkOrderScheduleDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


    @Override
    public Boolean createWorkOrderSchedule(WorkOrderScheduleCreateDto entity) {
        entity.map().forEach(A -> {
            mongoOperations.save(A);
        });
        return true;
    }

    @Override
    public Map<String, Object> getOneWorkOrderSchedule(String id) {


        Aggregation aggregation = Aggregation.newAggregation(
                match(Criteria.where("id").is(id)),
                project()
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$activityTypeId"))).as("activityTypeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$mainSubSystemId"))).as("mainSubSystemId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$workCategoryId"))).as("workCategoryId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$importanceDegreeId"))).as("importanceDegreeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$activityId"))).as("activityId")
                        .and("id").as("id")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("nexDate").as("nexDate")
                        .and("activityTime").as("activityTime")
                        .and("solution").as("solution")
                        .and("userIdList").as("userIdList")
                        .and("usedPartList").as("usedPartList")
                        .and("frequency").as("frequency")
                        .and("per").as("per")
                        .and("mode").as("mode")
                        .and("assetStatus").as("assetStatus")
                        .and("runStatus").as("runStatus")
                        .and("estimateCompletionDate").as("estimateCompletionDate")
                        .and("userIdList").as("userIdList")
                        .and("usedPartList").as("usedPartList")
                        .and("minorSubSystem").as("minorSubSystem")
                , lookup("asset", "assetId", "_id", "asset")
                , lookup("asset", "mainSubSystemId", "_id", "mainSubSystem")
                , lookup("activityType", "activityTypeId", "_id", "activityType")
                , lookup("workCategory", "workCategoryId", "_id", "workCategory")
                , lookup("importanceDegree", "importanceDegreeId", "_id", "importanceDegree")
                , lookup("activity", "activityId", "_id", "activity")
                , project()
                        .and("id").as("id")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("nexDate").as("nexDate")
                        .and("activityTime").as("activityTime")
                        .and("solution").as("solution")
                        .and("userIdList").as("userIdList")
                        .and("usedPartList").as("usedPartList")
                        .and("frequency").as("frequency")
                        .and("per").as("per")
                        .and("mode").as("mode")
                        .and("assetStatus").as("assetStatus")
                        .and("runStatus").as("runStatus")
                        .and("minorSubSystem").as("minorSubSystem")
                        .and("estimateCompletionDate").as("estimateCompletionDate")
                        .and("userIdList").as("userIdList")
                        .and("usedPartList").as("usedPartList")

                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")

                        .and("activityType._id").as("activityTypeId")
                        .and("activityType.name").as("activityTypeName")

                        .and("mainSubSystem._id").as("mainSubSystemId")
                        .and("mainSubSystem.name").as("mainSubSystemName")

                        .and("workCategory._id").as("workCategoryId")
                        .and("workCategory.name").as("workCategoryName")

                        .and("importanceDegree._id").as("importanceDegreeId")
                        .and("importanceDegree.name").as("importanceDegreeName")
                        .and("activity._id").as("activityId")
                        .and("activity.title").as("activityTitle")
        );
        List<WorkOrderScheduleGetOneDto> res = mongoOperations.aggregate(aggregation, WorkOrderSchedule.class, WorkOrderScheduleGetOneDto.class).getMappedResults();
        Print.print("res",res);
        List<String> partList = new ArrayList<>();
        res.get(0).getUsedPartList().forEach(a -> {
            partList.add(a.getPartId());
        });
        List<Part> usePart = partService.getAllByIdList(partList);
        Map<String, Object> result = new HashMap<>();
        result.put("res", res);
        result.put("usePart", usePart);
        return result;
    }

    @Override
    public Boolean updateWorkOrderSchedule(WorkOrderSchedule entity) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(entity.getStartDate());
        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 1);
        cal.set(Calendar.MILLISECOND, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        Date nextDate = cal.getTime();
        entity.setNexDate(nextDate);

        WorkOrderSchedule res = mongoOperations.save(entity);
        if (res != null) {
            return true;
        } else {
            return false;
        }
    }

    private Date setNextDate(WorkOrderSchedule entity) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 1);
        cal.set(Calendar.MILLISECOND, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        return cal.getTime();
    }

    @Override
    public Boolean deleteWorkOrderSchedule(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        DeleteResult res = mongoOperations.remove(query, WorkOrderSchedule.class);
        if (res != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<WorkOrderSchedule> getAllType() {
        Query query = new Query();
        return mongoOperations.find(query, WorkOrderSchedule.class);
    }

    @Override
    public List<WorkOrderSchedule> getAll() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 1);
        cal.set(Calendar.MILLISECOND, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        date = cal.getTime();

        Query query = new Query();
        query.addCriteria(Criteria.where("nexDate").in(date));
        return mongoOperations.find(query, WorkOrderSchedule.class);
    }

    @Override
    public boolean updateNextDate(String id, Date nextDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("nexDate", nextDate);
        UpdateResult res = mongoOperations.updateFirst(query, update, WorkOrderSchedule.class);
        if (res.getModifiedCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void deActiveWorkOrderSchedule(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("runStatus", DE_ACTIVE);
        mongoOperations.updateFirst(query, update, WorkOrderSchedule.class);
    }

    @Override
    public void makeNextDateNull(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("nexDate", null);
        mongoOperations.updateFirst(query, update, WorkOrderSchedule.class);
    }

    @Override
    public WorkOrderSchedule getWorkOrderScheduleNextDate(String scheduleId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(scheduleId));
        query.fields()
                .include("id")
                .include("mode")
                .include("per")
                .include("endDate")
                .include("frequency");
        return mongoOperations.findOne(query, WorkOrderSchedule.class);
    }

    @Override
    public void updateFloatWorkOrderScheduleNextDate(Integer per, WorkOrderSchedule.Frequency frequency, String id, Date endDate, Date nextDate) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 1);
        cal.set(Calendar.MILLISECOND, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        nextDate = cal.getTime();

        if (frequency.equals(DAILY)) {
            cal.setTime(nextDate);
            cal.add(Calendar.DAY_OF_MONTH, per);
        }

        if (frequency.equals(WEEKLY)) {
            cal.setTime(nextDate);
            cal.add(Calendar.WEEK_OF_MONTH, per);
        }

        if (frequency.equals(MONTHLY)) {
            cal.setTime(nextDate);
            cal.add(Calendar.MONTH, per);
        }

        if (frequency.equals(YEARLY)) {
            cal.setTime(nextDate);
            cal.add(Calendar.YEAR, per);
        }
        Date finalNextDate = cal.getTime();

        if (finalNextDate.before(endDate)) {
            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(id));
            Update update = new Update();
            update.set("nexDate", finalNextDate);
            mongoOperations.updateFirst(query, update, WorkOrderSchedule.class);
        } else {
            workOrderScheduleService.deActiveWorkOrderSchedule(id);
        }


    }

    @Override
    public List<ReqWorkOrderScheduleForCalenderGetListDto> getListWorkOrderScheduleForCalendar(ReqWorkOrderForCalendarGetListDTO entity) {

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

        criteria.andOperator(Criteria.where("nexDate").gte(start)
                , Criteria.where("nexDate").lte(end)
        );
        Aggregation aggregation = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetId")
                        .and("id").as("id")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("nexDate").as("nexDate")
                        .and("frequency").as("frequency")
                        .and("per").as("per")
                        .and("userIdList").as("userIdList")
                , lookup("asset", "assetId", "_id", "asset")
                , project()
                        .and("id").as("id")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("nexDate").as("nexDate")
                        .and("frequency").as("frequency")
                        .and("per").as("per")
                        .and("userIdList").as("userIdList")
                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")
        );
        return mongoOperations.aggregate(aggregation, WorkOrderSchedule.class, ReqWorkOrderScheduleForCalenderGetListDto.class).getMappedResults();


    }

    @Override
    public Page<WorkOrderScheduleGetPageDto> getPage(ReqWorkOrderScheduleGetPageDto entity, Pageable pageable, Integer total) {

        Criteria criteria = new Criteria();
        if (entity.getAssetId() != null && entity.getAssetId() != "") {
            criteria.and("assetId").is(entity.getAssetId());
        }
        if (entity.getActivityTypeId() != null && entity.getActivityTypeId() != "") {
            criteria.and("activityTypeId").is(entity.getActivityTypeId());
        }
        if (entity.getMinorSubSystem() != null && entity.getMinorSubSystem() != "") {
            criteria.and("minorSubSystem").regex(entity.getMinorSubSystem());
        }
        if (entity.getMainSubSystemId() != null && entity.getMainSubSystemId() != "") {
            criteria.and("mainSubSystemId").is(entity.getMainSubSystemId());
        }
        if (entity.getWorkCategoryId() != null && entity.getWorkCategoryId() != "") {
            criteria.and("workCategoryId").is(entity.getWorkCategoryId());
        }
        if (entity.getImportanceDegreeId() != null && entity.getImportanceDegreeId() != "") {
            criteria.and("importanceDegreeId").is(entity.getImportanceDegreeId());
        }
        if (entity.getAssetStatus() != null && !entity.getAssetStatus().equals("")) {
            criteria.and("assetStatus").is(entity.getAssetStatus());
        }
        if (entity.getRunStatus() != null && !entity.getRunStatus().equals("")) {
            criteria.and("runStatus").is(entity.getRunStatus());
        }
        Aggregation agg = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$activityTypeId"))).as("activityTypeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$mainSubSystemId"))).as("mainSubSystemId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$workCategoryId"))).as("workCategoryId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$importanceDegreeId"))).as("importanceDegreeId")
                        .and("id").as("id")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("activityTime").as("activityTime")
                        .and("solution").as("solution")
                        .and("userIdList").as("userIdList")
                        .and("usedPartList").as("usedPartList")
                        .and("frequency").as("frequency")
                        .and("per").as("per")
                        .and("mode").as("mode")
                        .and("assetStatus").as("assetStatus")
                        .and("runStatus").as("runStatus")
                        .and("estimateCompletionDate").as("estimateCompletionDate")
                        .and("minorSubSystem").as("minorSubSystem")
                , lookup("asset", "assetId", "_id", "asset")
                , lookup("asset", "mainSubSystemId", "_id", "mainSubSystem")
                , lookup("activityType", "activityTypeId", "_id", "activityType")
                , lookup("workCategory", "workCategoryId", "_id", "workCategory")
                , lookup("workCategory", "workCategoryId", "_id", "workCategory")
                , lookup("importanceDegree", "importanceDegreeId", "_id", "importanceDegree")
                , project().and("id").as("id")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("activityTime").as("activityTime")
                        .and("solution").as("solution")
                        .and("userIdList").as("userIdList")
                        .and("usedPartList").as("usedPartList")
                        .and("frequency").as("frequency")
                        .and("per").as("per")
                        .and("mode").as("mode")
                        .and("assetStatus").as("assetStatus")
                        .and("runStatus").as("runStatus")
                        .and("estimateCompletionDate").as("estimateCompletionDate")
                        .and("minorSubSystem").as("minorSubSystem")

                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")

                        .and("activityType._id").as("activityTypeId")
                        .and("activityType.name").as("activityTypeName")

                        .and("mainSubSystem._id").as("mainSubSystemId")
                        .and("mainSubSystem.name").as("mainSubSystemName")

                        .and("workCategory._id").as("workCategoryId")
                        .and("workCategory.name").as("workCategoryName")

                        .and("importanceDegree._id").as("importanceDegreeId")
                        .and("importanceDegree.name").as("importanceDegreeName")
                , skip(pageable.getPageNumber() * pageable.getPageSize())
                , limit(pageable.getPageSize())
        );
        AggregationResults<WorkOrderScheduleGetPageDto> groupResults
                = super.aggregate(agg, WorkOrderSchedule.class, WorkOrderScheduleGetPageDto.class);
        List<WorkOrderScheduleGetPageDto> result = groupResults.getMappedResults();
        if (total == -1) {
            Aggregation aggCount = Aggregation.newAggregation(
                    Aggregation.match(criteria)
            );
            AggregationResults<WorkOrderSchedule> groupResultsCount
                    = super.aggregate(aggCount, WorkOrderSchedule.class, WorkOrderSchedule.class);
            List<WorkOrderSchedule> resultcount = groupResultsCount.getMappedResults();
            total = resultcount.size();
        }
        return new PageImpl<WorkOrderScheduleGetPageDto>(result, pageable, total);
    }

    @Override
    public List<WorkOrderScheduleGetPageDto> getAll(ReqWorkOrderScheduleGetPageDto entity) {

        Criteria criteria = new Criteria();
        if (entity.getAssetId() != null && entity.getAssetId() != "") {
            criteria.and("assetId").is(entity.getAssetId());
        }
        if (entity.getActivityTypeId() != null && entity.getActivityTypeId() != "") {
            criteria.and("activityTypeId").is(entity.getActivityTypeId());
        }
        if (entity.getMinorSubSystem() != null && entity.getMinorSubSystem() != "") {
            criteria.and("minorSubSystem").regex(entity.getMinorSubSystem());
        }
        if (entity.getMainSubSystemId() != null && entity.getMainSubSystemId() != "") {
            criteria.and("mainSubSystemId").is(entity.getMainSubSystemId());
        }
        if (entity.getWorkCategoryId() != null && entity.getWorkCategoryId() != "") {
            criteria.and("workCategoryId").is(entity.getWorkCategoryId());
        }
        if (entity.getImportanceDegreeId() != null && entity.getImportanceDegreeId() != "") {
            criteria.and("importanceDegreeId").is(entity.getImportanceDegreeId());
        }
        if (entity.getAssetStatus() != null && !entity.getAssetStatus().equals("")) {
            criteria.and("assetStatus").is(entity.getAssetStatus());
        }
        if (entity.getRunStatus() != null && !entity.getRunStatus().equals("")) {
            criteria.and("runStatus").is(entity.getRunStatus());
        }
        Aggregation agg = Aggregation.newAggregation(
                match(criteria),
                project()
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$activityTypeId"))).as("activityTypeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$mainSubSystemId"))).as("mainSubSystemId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$workCategoryId"))).as("workCategoryId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$importanceDegreeId"))).as("importanceDegreeId")
                        .and("id").as("id")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("activityTime").as("activityTime")
                        .and("solution").as("solution")
                        .and("userIdList").as("userIdList")
                        .and("usedPartList").as("usedPartList")
                        .and("frequency").as("frequency")
                        .and("per").as("per")
                        .and("mode").as("mode")
                        .and("assetStatus").as("assetStatus")
                        .and("runStatus").as("runStatus")
                        .and("estimateCompletionDate").as("estimateCompletionDate")
                        .and("minorSubSystem").as("minorSubSystem")
                , lookup("asset", "assetId", "_id", "asset")
                , lookup("asset", "mainSubSystemId", "_id", "mainSubSystem")
                , lookup("activityType", "activityTypeId", "_id", "activityType")
                , lookup("workCategory", "workCategoryId", "_id", "workCategory")
                , lookup("workCategory", "workCategoryId", "_id", "workCategory")
                , lookup("importanceDegree", "importanceDegreeId", "_id", "importanceDegree")
                , project().and("id").as("id")
                        .and("startDate").as("startDate")
                        .and("endDate").as("endDate")
                        .and("activityTime").as("activityTime")
                        .and("solution").as("solution")
                        .and("userIdList").as("userIdList")
                        .and("usedPartList").as("usedPartList")
                        .and("frequency").as("frequency")
                        .and("per").as("per")
                        .and("mode").as("mode")
                        .and("assetStatus").as("assetStatus")
                        .and("runStatus").as("runStatus")
                        .and("estimateCompletionDate").as("estimateCompletionDate")
                        .and("minorSubSystem").as("minorSubSystem")

                        .and("asset._id").as("assetId")
                        .and("asset.name").as("assetName")

                        .and("activityType._id").as("activityTypeId")
                        .and("activityType.name").as("activityTypeName")

                        .and("mainSubSystem._id").as("mainSubSystemId")
                        .and("mainSubSystem.name").as("mainSubSystemName")

                        .and("workCategory._id").as("workCategoryId")
                        .and("workCategory.name").as("workCategoryName")

                        .and("importanceDegree._id").as("importanceDegreeId")
                        .and("importanceDegree.name").as("importanceDegreeName")

        );
        AggregationResults<WorkOrderScheduleGetPageDto> groupResults
                = super.aggregate(agg, WorkOrderSchedule.class, WorkOrderScheduleGetPageDto.class);
        List<WorkOrderScheduleGetPageDto> result = groupResults.getMappedResults();
        return result;
    }


}
