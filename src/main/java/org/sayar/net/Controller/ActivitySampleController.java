package org.sayar.net.Controller;

import org.sayar.net.Controller.Mongo.activityController.activity.ActivityHistoryDTO;
import org.sayar.net.Controller.newController.NewFormController;
import org.sayar.net.Enumes.RequestStatus;
import org.sayar.net.Enumes.RequestType;
import org.sayar.net.Model.*;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.Mongo.poll.controller.form.FormController;
import org.sayar.net.Model.Mongo.poll.controller.form.FormDataController;
import org.sayar.net.Model.Mongo.poll.model.form.FormData;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Service.Mongo.activityServices.activity.ActivityService;
import org.sayar.net.Service.NotificationService;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.UserTypeService;
import org.sayar.net.Service.WorkOrderSchedule.WorkOrderScheduleService;
import org.sayar.net.Service.WorkRequestService;
import org.sayar.net.Service.newService.AssetService;
import org.sayar.net.Service.newService.OrganService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RestController
@RequestMapping("activity-sample-controller")
public class ActivitySampleController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private UserService userService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private WorkRequestService workRequestService;

    @Autowired
    private OrganService organService;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private FormDataController formDataController;

    @Autowired
    private FormController formController;

    @Autowired
    private NewFormController newFormController;

    @Autowired
    private WorkOrderScheduleService workOrderScheduleService;

    public void map(String activityLevelId, ActivitySample activitySample, String formDataId) {
        ActivityLevelSequenceHistory activityLevelSequenceHistory = new ActivityLevelSequenceHistory();
        activityLevelSequenceHistory.setLevelEndDate(new Date());
        activityLevelSequenceHistory.setLevelId(activityLevelId);
        activityLevelSequenceHistory.setStatus("accepted");
        activityLevelSequenceHistory.setFormData(formDataId);
        activitySample.getActivityLevelSequenceHistory().add(activityLevelSequenceHistory);
        mongoOperations.save(activitySample);
    }

    public ActivitySample createActivitySampleByWorkRequest(String pmSheetCode,
                                                            Date requestedDate,
                                                            String userId,
                                                            String activityId,
                                                            String worRequestTitle,
                                                            String workOrderId,
                                                            String workRequestId,
                                                            String assetId,
                                                            Priority workRequestPriority,
                                                            MaintenanceType workRequestMaintenanceType) {

        UserDetailAndUserTypeDTO userDetailAndUserTypeDTO = userService.getRequesterUser(userId);
        Activity activity = activityService.getActivityByActivityId(activityId);
        ActivitySample activitySample = new ActivitySample();

        activitySample.setWorkRequesterId(userId);
        activitySample.setWorkRequesterName(userDetailAndUserTypeDTO.getName());
        activitySample.setWorkRequesterFamilyName(userDetailAndUserTypeDTO.getFamily());
        activitySample.setWorkRequesterUserTypeName(userDetailAndUserTypeDTO.getUserTypeName());

        if (workRequestPriority != null)
            activitySample.setWorkRequestPriority(workRequestPriority);
        if (workRequestMaintenanceType != null)
            activitySample.setWorkRequestMaintenanceType(workRequestMaintenanceType);
        if (workRequestId != null)
            activitySample.setWorkRequestId(workRequestId);
        if (assetId != null)
            activitySample.setAssetId(assetId);
        if (workOrderId != null)
            activitySample.setWorkOrderId(workOrderId);
        if (activity.getDescription() != null)
            activitySample.setDescription(activity.getDescription());
        if (activity.getTitle() != null)
            activitySample.setTitle(activity.getTitle());
        if (activityId != null)
            activitySample.setRelatedActivityId(activityId);
        if (worRequestTitle != null)
            activitySample.setWorkRequestTitle(worRequestTitle);

        activitySample.setActivityLevelList(activity.getActivityLevelList());
        activitySample.setActivityInstanceId(UUID.randomUUID().toString());
        activitySample.getActivityLevelList().get(1).setRequestStatus(RequestStatus.NEW_REQUEST);
        int lastActivityLevel = activitySample.getActivityLevelList().size() - 1;
        activitySample.getActivityLevelList().get(lastActivityLevel).setActionLevel("finish");
        activitySample.setCreationDateOfWorkRequest(requestedDate);
        activitySample.setPmSheetCode(pmSheetCode);

        return mongoOperations.save(activitySample);
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

    public ActivitySample getPendingAndActiveActivityLevel(String activityInstanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        return mongoOperations.findOne(query, ActivitySample.class);
    }

    public Page<NoticeBoardGetAllDTO> getActivitySamplesHaveConsideredId(String userId, UserPendingActivityDTO userPendingActivityDTO, Pageable pageable) {
        User user = userService.getUserUserTypeIds(userId);
//        List<String> userTypeIdList = new ArrayList<>();
//        user.getOrgAndUserTypeList().forEach(orgAndUserType -> userTypeIdList.addAll(orgAndUserType.getUserTypeList()));
        return new PageImpl<>(
                this.getAllActivitySampleIsTheUserTurn(userId, user.getUserTypeId(), userPendingActivityDTO, pageable),
                pageable,
                this.countAllActivitySamplesAreTheUserTurn(userId, user.getUserTypeId(), userPendingActivityDTO)
        );
    }

    private long countAllActivitySamplesAreTheUserTurn(String userId, String userTypeId, UserPendingActivityDTO userPendingActivityDTO) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("active").is(true);
        criteria.and("fromSchedule").is(false);

        if (userPendingActivityDTO.getAssetId() != null && !userPendingActivityDTO.getAssetId().equals("")) {
            criteria.and("assetId").is(userPendingActivityDTO.getAssetId());
        }
        if (userPendingActivityDTO.getMaintenanceType() != null) {
            criteria.and("workRequestMaintenanceType").is(userPendingActivityDTO.getMaintenanceType());
        }
        if (userPendingActivityDTO.getPriority() != null) {
            criteria.and("workRequestPriority").is(userPendingActivityDTO.getPriority());
        }
        if (userPendingActivityDTO.getRequestTitle() != null && !userPendingActivityDTO.getRequestTitle().equals("")) {
            criteria.and("workRequestTitle").regex(userPendingActivityDTO.getRequestTitle());
        }
        if (userPendingActivityDTO.getFrom() != null && userPendingActivityDTO.getUntil() == null) {
            criteria.and("creationDateOfWorkRequest").gte(userPendingActivityDTO.getFrom());
        }
        if (userPendingActivityDTO.getUntil() != null && userPendingActivityDTO.getFrom() == null) {
            criteria.and("creationDateOfWorkRequest").lte(userPendingActivityDTO.getUntil());
        }
        if (userPendingActivityDTO.getUntil() != null && userPendingActivityDTO.getFrom() != null) {
            criteria.andOperator(Criteria.where("creationDateOfWorkRequest").gte(userPendingActivityDTO.getFrom())
                    , Criteria.where("creationDateOfWorkRequest").lte(userPendingActivityDTO.getUntil())
            );
        }
        if (userPendingActivityDTO.getFromSchedule() != null && userPendingActivityDTO.getFromSchedule().equals(true)) {
            criteria.and("fromSchedule").is(true);
        }

        if (userPendingActivityDTO.getFromSchedule() != null && userPendingActivityDTO.getFromSchedule().equals(false)) {
            criteria.and("fromSchedule").is(false);
        }

        if (userPendingActivityDTO.getRequestType() != null) {
            if (userPendingActivityDTO.getRequestType().equals(RequestType.SENDFORYOU)) {
                criteria.and("activityLevelList.assignedUserId").ne(null);
            }
            if (userPendingActivityDTO.getRequestType().equals(RequestType.SENDFORGROUPE)) {
                criteria.and("activityLevelList.assignedUserId").is(null);
//                    .orOperator(criteria.and("assignedUserId").is(""));
            }
        }

        if (userPendingActivityDTO.getUserId() != null && !userPendingActivityDTO.getUserId().equals("")) {
            criteria.and("workRequesterId").is(userPendingActivityDTO.getUserId());
        }

        if (userPendingActivityDTO.getPmSheetCode() != null && !userPendingActivityDTO.getPmSheetCode().equals("")) {
            criteria.and("pmSheetCode").regex(userPendingActivityDTO.getPmSheetCode());
        }

        criteria.and("activityLevelList.actionLevel").is("pending");
        criteria.orOperator(
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(new ArrayList<>()),
                        Criteria.where("activityLevelList.userTypeId").is(userTypeId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(userId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(userId)
                )
        );

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("activityLevelList"),
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, ActivitySample.class, NoticeBoardGetAllDTO.class).getMappedResults().size();
    }

    private List<NoticeBoardGetAllDTO> getAllActivitySampleIsTheUserTurn(String userId, String userTypeId, UserPendingActivityDTO userPendingActivityDTO, Pageable pageable) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("active").is(true);
        criteria.and("fromSchedule").is(false);

        if (userPendingActivityDTO.getAssetId() != null && !userPendingActivityDTO.getAssetId().equals("")) {
            criteria.and("assetId").is(userPendingActivityDTO.getAssetId());
        }

        if (userPendingActivityDTO.getMaintenanceType() != null) {
            criteria.and("workRequestMaintenanceType").is(userPendingActivityDTO.getMaintenanceType());
        }

        if (userPendingActivityDTO.getPriority() != null) {
            criteria.and("workRequestPriority").is(userPendingActivityDTO.getPriority());
        }

        if (userPendingActivityDTO.getRequestTitle() != null && !userPendingActivityDTO.getRequestTitle().equals("")) {
            criteria.and("workRequestTitle").regex(userPendingActivityDTO.getRequestTitle());
        }

        if (userPendingActivityDTO.getFrom() != null && userPendingActivityDTO.getUntil() == null) {
            criteria.and("creationDateOfWorkRequest").gte(userPendingActivityDTO.getFrom());
        }

        if (userPendingActivityDTO.getUntil() != null && userPendingActivityDTO.getFrom() == null) {
            criteria.and("creationDateOfWorkRequest").lte(userPendingActivityDTO.getUntil());
        }

        if (userPendingActivityDTO.getUntil() != null && userPendingActivityDTO.getFrom() != null) {
            criteria.andOperator(Criteria.where("creationDateOfWorkRequest").gte(userPendingActivityDTO.getFrom())
                    , Criteria.where("creationDateOfWorkRequest").lte(userPendingActivityDTO.getUntil())
            );
        }

        if (userPendingActivityDTO.getFromSchedule() != null && userPendingActivityDTO.getFromSchedule().equals(true)) {
            criteria.and("fromSchedule").is(true);
        }

        if (userPendingActivityDTO.getFromSchedule() != null && userPendingActivityDTO.getFromSchedule().equals(false)) {
            criteria.and("fromSchedule").is(false);
        }

        if (userPendingActivityDTO.getUserId() != null && !userPendingActivityDTO.getUserId().equals("")) {
            criteria.and("workRequesterId").is(userPendingActivityDTO.getUserId());
        }

        if (userPendingActivityDTO.getPmSheetCode() != null && !userPendingActivityDTO.getPmSheetCode().equals("")) {
            criteria.and("pmSheetCode").regex(userPendingActivityDTO.getPmSheetCode());
        }

        if (userPendingActivityDTO.getRequestType() != null) {
            if (userPendingActivityDTO.getRequestType().equals(RequestType.SENDFORYOU)) {
                criteria.and("activityLevelList.assignedUserId").ne(null);
            }
            if (userPendingActivityDTO.getRequestType().equals(RequestType.SENDFORGROUPE)) {
                criteria.and("activityLevelList.assignedUserId").is(null);
//                    .orOperator(criteria.and("assignedUserId").is(""));
            }
        }

        criteria.and("activityLevelList.actionLevel").is("pending");
        criteria.orOperator(
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(new ArrayList<>()),
                        Criteria.where("activityLevelList.userTypeId").is(userTypeId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(userId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(userId)
                )
        );

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetObjId")
                .foreignField("_id")
                .as("asset");

        LookupOperation lookupOperation2 = LookupOperation.newLookup()
                .from("user")
                .localField("workRequesterId")
                .foreignField("_id")
                .as("user");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("activityLevelList"),
                Aggregation.match(criteria),
                Aggregation.sort(Sort.Direction.DESC, "creationDateOfWorkRequest"),
                Aggregation.project()
                        .and("activityInstanceId").as("instanceId")
                        .and("pmSheetCode").as("pmSheetCode")
                        .and("workRequestTitle").as("workRequestTitle")
                        .and("creationDateOfWorkRequest").as("workRequestTime")
                        .and("fromSchedule").as("isSchedule")
                        .and("workRequestPriority").as("priority")
                        .and("workRequestMaintenanceType").as("maintenanceType")
                        .and("activityLevelList.requestStatus").as("requestStatus")
                        .and("activityLevelList.assignedUserId").as("assignedUserId")
                        .and("activityLevelList.id").as("activityLevelId")
                        .and("activityLevelList.workRequestAcceptor").as("workRequestAcceptor")
                        .and("workOrderId").as("workOrderId")
                        .and("assetId").as("assetId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$workRequesterId")).as("workRequesterId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetObjId"),
                lookupOperation,
                lookupOperation2,
                Aggregation.unwind("asset"),
                Aggregation.unwind("user"),
                Aggregation.project()
                        .and("instanceId").as("instanceId")
                        .and("pmSheetCode").as("pmSheetCode")
                        .and("workRequestTitle").as("workRequestTitle")
                        .and("assignedUserId").as("assignedUserId")
                        .and("workRequestTime").as("workRequestTime")
                        .and("isSchedule").as("isSchedule")
                        .and("priority").as("priority")
                        .and("maintenanceType").as("maintenanceType")
                        .and("requestStatus").as("requestStatus")
                        .and("activityLevelId").as("activityLevelId")
                        .and("workOrderId").as("workOrderId")
                        .and("workRequestAcceptor").as("workRequestAcceptor")
                        .and("assetId").as("assetId")
                        .and("asset.name").as("assetName")
                        .and("asset.code").as("assetCode")
                        .and("user._id").as("requesterId")
                        .and("user.name").as("requesterName")
                        .and("user.family").as("requesterFamily"),
                skipOperation,
                limitOperation
        );
        return mongoOperations.aggregate(aggregation, ActivitySample.class, NoticeBoardGetAllDTO.class).getMappedResults();
    }

    public List<ActivitySample> getActivitySampleOfTheRequester(String requesterId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("active").ne(false));
        query.addCriteria(Criteria.where("requesterId").is(requesterId));
        return mongoOperations.find(query, ActivitySample.class);
    }

    public ActivitySample getActivitySampleByInstanceId(String workRequestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("workRequestId").is(workRequestId));
        return mongoOperations.findOne(query, ActivitySample.class);
    }

    public ActivitySample getNeededFormOfActivitySample(String instanceId, String activityLevelId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("active").ne(false));
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("activityInstanceId").is(instanceId),
                Criteria.where("activityLevelList.id").is(activityLevelId)
        ));
        query.fields().include("activityLevelList");
        return mongoOperations.findOne(query, ActivitySample.class);
    }

    public ActivitySample getFormDataOfSampleActivity(String instanceId, String activityLevelId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
//        query.addCriteria(Criteria.where("active").ne(false));
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("activityInstanceId").is(instanceId),
                Criteria.where("activityLevelList.id").is(activityLevelId)
        ));
        query.fields().include("activityLevelList");
        return mongoOperations.findOne(query, ActivitySample.class);
    }

    public void saveFormDataIdInConsideredActivitySample(String instanceId, String activityLevelId, String id) {
        Query query = new Query();
        query.addCriteria(new Criteria().andOperator(
                Criteria.where("deleted").ne(true),
                Criteria.where("active").ne(false),
                Criteria.where("activityInstanceId").is(instanceId),
                Criteria.where("activityLevelList.id").is(activityLevelId)
        ));
        Update update = new Update();
        update.set("formIdData", id);
        mongoOperations.updateFirst(query, update, ActivitySample.class);
    }

    public ActivitySample getTheActivitySample(String instanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("active").ne(false));
        query.addCriteria(Criteria.where("activityInstanceId").is(instanceId));
        return mongoOperations.findOne(query, ActivitySample.class);
    }

    public ActivitySample addFormDataIdToRelatedActivityLevel(String formDataId, ActivitySample activitySample, String activityLevelId) {
        int integerActivityLevelId = Integer.parseInt(activityLevelId);
        activitySample.getActivityLevelList().get(integerActivityLevelId).setFormDataId(formDataId);
        return mongoOperations.save(activitySample);
    }

    public void changeDetailsOfActivitySampleAfterAccepting(ActivitySample activitySample, String activityLevelId) {
        int consideredActivityLevel = Integer.parseInt(activityLevelId);
        activitySample.getActivityLevelList().get(consideredActivityLevel).setRequestStatus(RequestStatus.EMPTY);
        activitySample.getActivityLevelList().get(consideredActivityLevel).setActionLevel("accepted");
        activitySample.getActivityLevelList().get(consideredActivityLevel).setAnswerDate(new Date());

        int nextActivityLevel;
        if (!activitySample.getActivityLevelList().get(consideredActivityLevel).getNextActivityLevel().getId().equals("end")) {
            nextActivityLevel = Integer.parseInt(activitySample.getActivityLevelList().get(consideredActivityLevel).getNextActivityLevel().getId());
            activitySample.getActivityLevelList().get(nextActivityLevel).setRequestStatus(RequestStatus.NEW_REQUEST);
            activitySample.getActivityLevelList().get(nextActivityLevel).setActionLevel("pending");

            //setting pending date of next activityLevel
            activitySample.getActivityLevelList().get(nextActivityLevel).setPendingDate(new Date());

            workRequestService.workRequestStatusIsInProcess(activitySample.getWorkRequestId());
        } else {
            if (activitySample.getScheduleId() != null) {
                workOrderService.setEndDateOfWorkOrderSchedule(activitySample.getWorkOrderId());
                WorkOrderSchedule workOrderSchedule = workOrderScheduleService.getWorkOrderScheduleNextDate(activitySample.getScheduleId());
                if (workOrderSchedule.getMode().equals(WorkOrderSchedule.Mode.FLOAT)) {
                    workOrderScheduleService.updateFloatWorkOrderScheduleNextDate(workOrderSchedule.getPer(), workOrderSchedule.getFrequency(), workOrderSchedule.getId(), workOrderSchedule.getEndDate(), workOrderSchedule.getNexDate());
                }
            }
            workRequestService.changeWorkRequestInProcessStatusToFalse(activitySample.getWorkRequestId());
            activitySample.setActive(false);
        }
        mongoOperations.save(activitySample);
    }

    public Page<ActivitySampleWithAssetNameDTO> getAllAcceptedAndRejectedActivitiesOfUserWithPagination(ActivityHistoryDTO activityHistory, String userId, Pageable pageable, boolean state) {
        return new PageImpl<>(
                this.getAllAcceptedAndRejectedActivitiesOfUser(activityHistory, userId, pageable, state)
                , pageable
                , this.countAllAcceptedAndRejectedActivitySamplesByTheUser(activityHistory, userId, state)
        );
    }

    private long countAllAcceptedAndRejectedActivitySamplesByTheUser(ActivityHistoryDTO activityHistory, String userId, boolean state) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);

        if (state) {
            criteria.and("active").is(true);
        } else {
            criteria.and("active").is(false);
        }

        if (activityHistory.isFromSchedule()) {
            criteria.and("fromSchedule").is(true);
        }

        if (!activityHistory.isFromSchedule()) {
            criteria.and("fromSchedule").is(false);
        }

        if (activityHistory.getAssetId() != null)
            criteria.and("assetId").is(activityHistory.getAssetId());

        if (activityHistory.getMaintenanceType() != null)
            criteria.and("workRequestMaintenanceType").is(activityHistory.getMaintenanceType());

        if (activityHistory.getPriority() != null)
            criteria.and("workRequestPriority").is(activityHistory.getPriority());

        if (activityHistory.getRequestTime() != null)
            criteria.and("creationDateOfWorkRequest").is(activityHistory.getRequestTime());

        if (activityHistory.getRequestTitle() != null)
            criteria.and("workRequestTitle").regex(activityHistory.getRequestTitle());

        criteria.and("activityLevelList.assignedUserId").is(userId);
        criteria.orOperator(
                Criteria.where("activityLevelList.actionLevel").is("accepted"),
                Criteria.where("activityLevelList.actionLevel").is("rejected")
        );

        if (activityHistory.getDeliveryDate() != null)
            criteria.and("activityLevelList.pendingDate").is(activityHistory.getDeliveryDate());

        if (activityHistory.getReplyDate() != null)
            criteria.and("activityLevelList.answerDate").is(activityHistory.getReplyDate());

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("activityLevelList", true),
                Aggregation.match(criteria)
        );
        return mongoOperations.aggregate(aggregation, ActivitySample.class, ActivitySampleWithAssetNameDTO.class).getMappedResults().size();
    }

    private List<ActivitySampleWithAssetNameDTO> getAllAcceptedAndRejectedActivitiesOfUser(ActivityHistoryDTO activityHistory,
                                                                                           String userId,
                                                                                           Pageable pageable,
                                                                                           boolean state) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();

        criteria.and("deleted").ne(true);

        if (state) {
            criteria.and("active").is(true);
        } else {
            criteria.and("active").is(false);
        }

        if (activityHistory.isFromSchedule())
            criteria.and("fromSchedule").is(true);

        if (!activityHistory.isFromSchedule())
            criteria.and("fromSchedule").is(false);

        if (activityHistory.getAssetId() != null)
            criteria.and("assetId").is(activityHistory.getAssetId());

        if (activityHistory.getMaintenanceType() != null)
            criteria.and("workRequestMaintenanceType").is(activityHistory.getMaintenanceType());

        if (activityHistory.getPriority() != null)
            criteria.and("workRequestPriority").is(activityHistory.getPriority());

        if (activityHistory.getRequestTime() != null)
            criteria.and("creationDateOfWorkRequest").is(activityHistory.getRequestTime());

        if (activityHistory.getRequestTitle() != null)
            criteria.and("workRequestTitle").regex(activityHistory.getRequestTitle());

        criteria.and("activityLevelList.assignedUserId").is(userId);
        criteria.orOperator(
                Criteria.where("activityLevelList.actionLevel").is("accepted"),
                Criteria.where("activityLevelList.actionLevel").is("rejected")
        );

        if (activityHistory.getDeliveryDate() != null)
            criteria.and("activityLevelList.pendingDate").is(activityHistory.getDeliveryDate());

        if (activityHistory.getReplyDate() != null)
            criteria.and("activityLevelList.answerDate").is(activityHistory.getReplyDate());

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetObjId")
                .foreignField("_id")
                .as("asset");

        Aggregation aggregation = Aggregation.newAggregation(
                skipOperation,
                limitOperation,
                Aggregation.unwind("activityLevelList", true),
                Aggregation.match(criteria),
                Aggregation.project()
                        .and("activityInstanceId").as("instanceId")
                        .and("activityLevelList.pendingDate").as("deliveryDate")
                        .and("activityLevelList.answerDate").as("replyDate")
                        .and("workRequestTitle").as("workRequestTitle")
                        .and("creationDateOfWorkRequest").as("workRequestTime")
                        .and("fromSchedule").as("isSchedule")
                        .and("workRequestPriority").as("priority")
                        .and("workRequestMaintenanceType").as("maintenanceType")
                        .and("assetId").as("assetId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetObjId"),
                lookupOperation,
                Aggregation.unwind("asset", true),
                Aggregation.project()
                        .and("instanceId").as("instanceId")
                        .and("workRequestTitle").as("workRequestTitle")
                        .and("workRequestTime").as("workRequestTime")
                        .and("isSchedule").as("isSchedule")
                        .and("priority").as("priority")
                        .and("maintenanceType").as("maintenanceType")
                        .and("assetId").as("assetId")
                        .and("asset.name").as("assetName")
                        .and("asset.code").as("assetCode")
                        .and("deliveryDate").as("deliveryDate")
                        .and("replyDate").as("replyDate")
        );
        return mongoOperations.aggregate(aggregation, ActivitySample.class, ActivitySampleWithAssetNameDTO.class).getMappedResults();
    }

    public ActivitySample getTheActivitySampleInRejectTimeAndInHistorySequence(String instanceId, String activityLevelId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("active").ne(false));
        query.addCriteria(Criteria.where("activityInstanceId").is(instanceId));
        return mongoOperations.findOne(query, ActivitySample.class);
    }

    public void changeDetailsOfActivitySampleAfterRejecting(ActivitySample activitySample, String activityLevelId, String formDataId, WorkOrder relevantWorkOrderOfActivitySample) {
        int consideredActivityLevel = Integer.parseInt(activityLevelId);
        activitySample.getActivityLevelList().get(consideredActivityLevel).setRequestStatus(RequestStatus.EMPTY);
        activitySample.getActivityLevelList().get(consideredActivityLevel).setActionLevel("rejected");
        activitySample.getActivityLevelList().get(consideredActivityLevel).setAnswerDate(new Date());

        int previousActivityLevel;

        if (!activitySample.getActivityLevelList().get(consideredActivityLevel).getPrevActivityLevel().getId().equals("start")
                && !activitySample.getActivityLevelList().get(consideredActivityLevel).getPrevActivityLevel().getId().equals("end")) {

            previousActivityLevel = Integer.parseInt(activitySample.getActivityLevelList().get(consideredActivityLevel).getPrevActivityLevel().getId());
            activitySample.getActivityLevelList().get(previousActivityLevel).setRequestStatus(RequestStatus.REJECTED_REQUEST);
            activitySample.getActivityLevelList().get(previousActivityLevel).setActionLevel("pending");
            activitySample.getActivityLevelList().get(previousActivityLevel).setPendingDate(new Date());
            workRequestService.workRequestStatusIsInProcess(activitySample.getWorkRequestId());
        }

        if (activitySample.getActivityLevelList().get(consideredActivityLevel).getPrevActivityLevel().getId().equals("start")) {
            activitySample.getActivityLevelList().get(0).setActionLevel("waiting");
            activitySample.setActive(false);
            workRequestService.changeWorkRequestStatus(activitySample.getWorkRequestId());
        }

        if (activitySample.getActivityLevelList().get(consideredActivityLevel).getPrevActivityLevel().getId().equals("end")) {
            activitySample.setActive(false);

            if (activitySample.getScheduleId() != null) {
                workOrderService.deleteWorkOrderSchedule(relevantWorkOrderOfActivitySample.getId());
                WorkOrderSchedule workOrderSchedule = workOrderScheduleService.getWorkOrderScheduleNextDate(activitySample.getScheduleId());
                if (workOrderSchedule.getMode().equals(WorkOrderSchedule.Mode.FLOAT)) {
                    workOrderScheduleService.updateFloatWorkOrderScheduleNextDate(workOrderSchedule.getPer(), workOrderSchedule.getFrequency(), workOrderSchedule.getId(), workOrderSchedule.getEndDate(), workOrderSchedule.getNexDate());
                }
            } else {
//                activitySample.setActive(false);
                mongoOperations.remove(relevantWorkOrderOfActivitySample);
            }
            workRequestService.changeWorkRequestStatus(activitySample.getWorkRequestId());
        }
        mongoOperations.save(activitySample);
    }

    public void createSequenceOfThisLevel(String formDataId, String activityLevelId, ActivitySample activitySample) {
        ActivityLevelSequenceHistory activityLevelSequenceHistory = new ActivityLevelSequenceHistory();
        activityLevelSequenceHistory.setFormData(formDataId);
        activityLevelSequenceHistory.setLevelEndDate(new Date());
        activityLevelSequenceHistory.setLevelId(activityLevelId);
        activityLevelSequenceHistory.setStatus("rejected");
        activitySample.getActivityLevelSequenceHistory().add(activityLevelSequenceHistory);
        mongoOperations.save(activitySample);
    }

    public boolean saveWorkOrderIdInActivity(String activityInstanceId, String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        Update update = new Update();
        update.set("workOrderId", workOrderId);
        mongoOperations.updateFirst(query, update, ActivitySample.class);
        return true;
    }

    public boolean chooseNextActivityLevelUser(ChooseNextActivityLevelUserClass chooseNextActivityLevelUserClass) {
        Print.print("AAA", chooseNextActivityLevelUserClass);
        if (chooseNextActivityLevelUserClass.getUserIdList() != null && chooseNextActivityLevelUserClass.getUserIdList().size() == 1) {
            String userId = (chooseNextActivityLevelUserClass.getUserIdList().get(0));
//            if (!chooseNextActivityLevelUserClass.isChosenMechanic()) {
            Query query = new Query();
            query.addCriteria(Criteria.where("deleted").ne(true));
            query.addCriteria(Criteria.where("activityInstanceId").is(chooseNextActivityLevelUserClass.getActivityInstanceId()));
            query.addCriteria(Criteria.where("activityLevelList").elemMatch(Criteria.where("_id").is(chooseNextActivityLevelUserClass.getActivityLevelId())));
            Update update = new Update();
            update.set("activityLevelList.$.assignedUserId", userId);
            mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), ActivitySample.class);
            return true;
//            }
//            else {
//                workOrderService.setTheUserIdAsAssignedUserForWorkOrder(userId, chooseNextActivityLevelUserClass.getWorkOrderId());
//                Query query = new Query();
//                query.addCriteria(Criteria.where("deleted").ne(true));
//                query.addCriteria(Criteria.where("activityInstanceId").is(chooseNextActivityLevelUserClass.getActivityInstanceId()));
//                query.addCriteria(Criteria.where("activityLevelList").elemMatch(Criteria.where("_id").is(chooseNextActivityLevelUserClass.getActivityLevelId())));
//                Update update = new Update();
//                update.set("activityLevelList.$.assignedUserId", userId);
//                mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), ActivitySample.class);
//
//                /**
//                 * send notification to assigned user and his parents
//                 */
//                WorkOrder relevantWorkOrderOfFaultyAsset = workOrderService.getRelevantWorkOrderOfFaultyAsset(chooseNextActivityLevelUserClass.getWorkOrderId());
//                Asset faultyAsset = assetService.findFaultyAsset(relevantWorkOrderOfFaultyAsset.getAssetId());
//                User relatedTechnicianOfTheFaultyAsset = userService.findRelatedTechnicianOfTheFaultyAsset(userId);
//                notificationService.sendNotificationToTheTechnician(relevantWorkOrderOfFaultyAsset.getCode(), faultyAsset.getCode(),
//                        faultyAsset.getName(), relatedTechnicianOfTheFaultyAsset.getUsername(), userId);
//                return true;
//            }
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("deleted").ne(true));
            query.addCriteria(Criteria.where("activityInstanceId").is(chooseNextActivityLevelUserClass.getActivityInstanceId()));
            query.addCriteria(Criteria.where("activityLevelList").elemMatch(Criteria.where("_id").is(chooseNextActivityLevelUserClass.getActivityLevelId())));
            Update update = new Update();
            if (chooseNextActivityLevelUserClass.getUserTypeId() != null)
                update.set("activityLevelList.$.userTypeId", chooseNextActivityLevelUserClass.getUserTypeId());
            else
                update.set("activityLevelList.$.chosenCandidateUserIdList", chooseNextActivityLevelUserClass.getUserIdList());
            mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), ActivitySample.class);
            return true;
        }
    }

    public void createActivitySampleForArrivedTimeScheduleMaintenance(List<ScheduleActivityDTO> scheduleActivityDTOList) {
        List<ActivitySample> activitySampleList = new ArrayList<>();
        for (ScheduleActivityDTO scheduleActivityDTO : scheduleActivityDTOList) {
            ActivitySample activitySample = new ActivitySample();
            activitySample.setActive(true);
            activitySample.setActivityInstanceId(UUID.randomUUID().toString());
            activitySample.setFromSchedule(true);
            activitySample.setCreationDateOfWorkRequest(new Date());
            if (scheduleActivityDTO.getActivity() != null) {

                if (scheduleActivityDTO.getAssetId() != null)
                    activitySample.setAssetId(scheduleActivityDTO.getAssetId());
                if (scheduleActivityDTO.getWorkOrderPriority() != null)
                    activitySample.setWorkRequestPriority(scheduleActivityDTO.getWorkOrderPriority());
                if (scheduleActivityDTO.getMaintenanceType() != null)
                    activitySample.setWorkRequestMaintenanceType(scheduleActivityDTO.getMaintenanceType());
                if (scheduleActivityDTO.getActivity().getDescription() != null)
                    activitySample.setDescription(scheduleActivityDTO.getActivity().getDescription());
                if (scheduleActivityDTO.getActivity().getTitle() != null)
                    activitySample.setTitle(scheduleActivityDTO.getActivity().getTitle());
                if (scheduleActivityDTO.getWorkOrderId() != null)
                    activitySample.setWorkOrderId(scheduleActivityDTO.getWorkOrderId());
                if (scheduleActivityDTO.getActivity().getId() != null)
                    activitySample.setRelatedActivityId(scheduleActivityDTO.getActivity().getId());
                if (scheduleActivityDTO.getActivity().getActivityLevelList() != null)
                    activitySample.setActivityLevelList(scheduleActivityDTO.getActivity().getActivityLevelList());
                if (scheduleActivityDTO.getWorkOrderTitle() != null)
                    activitySample.setWorkRequestTitle(scheduleActivityDTO.getWorkOrderTitle());
            }
            /**
             * set action level of activitySample
             */
            int lastActivityLevel = activitySample.getActivityLevelList().size() - 1;
            activitySample.getActivityLevelList().get(lastActivityLevel).setActionLevel("finish");

            activitySample.getActivityLevelList().get(0).setActionLevel("accepted");
            activitySample.getActivityLevelList().get(0).getNextActivityLevel().setActionLevel("pending");
            int nextActivityLevel = Integer.parseInt(activitySample.getActivityLevelList().get(0).getNextActivityLevel().getId());
            activitySample.getActivityLevelList().get(nextActivityLevel).setActionLevel("pending");
            activitySample.getActivityLevelList().get(nextActivityLevel).setRequestStatus(RequestStatus.NEW_REQUEST);

            /**
             * set pending date of next activityLevel
             */
            activitySample.getActivityLevelList().get(nextActivityLevel).setPendingDate(new Date());
            activitySampleList.add(activitySample);
        }
        mongoOperations.insert(activitySampleList, ActivitySample.class);
    }

    public ActivitySample getOldVersionActivitySampleByInstanceId(String activityInstanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        return mongoOperations.findOne(query, ActivitySample.class);
    }

    public boolean ifActivitySampleExist(String workRequestId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("active").ne(false));
        query.addCriteria(Criteria.where("workRequestId").is(workRequestId));
        return mongoOperations.exists(query, ActivitySample.class);
    }

    public long countPendingActivityOfTheUser(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("active").ne(false));
        query.addCriteria(Criteria.where("activityLevelList").elemMatch(
                Criteria.where("assignedUserId").is(userId)
                        .andOperator(Criteria.where("actionLevel").is("pending"))
        ));
        return mongoOperations.count(query, ActivitySample.class);
    }

    public List<ActivitySample> getPendingActivitiesTheUserShouldAnswer(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("active").ne(false));
        query.addCriteria(Criteria.where("activityLevelList").elemMatch(
                Criteria.where("assignedUserId").is(userId)
                        .andOperator(Criteria.where("actionLevel").is("pending"))
        ));
        return mongoOperations.find(query, ActivitySample.class);
    }

    public boolean ifActivityExistInActivitySample(String activityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("Active").ne(false));
        query.addCriteria(Criteria.where("relatedActivityId").is(activityId));
        return mongoOperations.exists(query, ActivitySample.class);
    }

    public List<String> checkWhichActivitiesAreInProcess(List<String> activityIdList) {
        List<ActivitySample> activitySampleList = this.getAllActivitySampleList();
        List<String> activeActivityIdList = new ArrayList<>();
        activitySampleList.forEach(activitySample -> {
            activityIdList.forEach(activityId -> {
                if (activitySample.getRelatedActivityId().equals(activityId)) {
                    activeActivityIdList.add(activityId);
                }
            });
        });
        return activeActivityIdList;
    }

    private List<ActivitySample> getAllActivitySampleList() {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("active").ne(false));
        return mongoOperations.find(query, ActivitySample.class);
    }

    public PendingUserDetailsDTO activityInstanceId(String activityInstanceId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("activityInstanceId").is(activityInstanceId);
        criteria.and("Active").is(true);
        Criteria secondCriteria = new Criteria();
        secondCriteria.and("activityLevelList.actionLevel").is("pending");
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project()
                        .andExpression("activityLevelList").as("activityLevelList")
                        .andExpression("activityLevelSequenceHistory").as("activityLevelSequenceHistory")
                        .andExpression("workOrderId").as("workOrderId")
                        .andExpression("workOrderAccepted").as("workOrderAccepted"),
                Aggregation.unwind("activityLevelList"),
                Aggregation.match(secondCriteria),
                Aggregation.project()
                        .andExpression("activityLevelList.assignedUserId").as("userId")
                        .andExpression("activityLevelList.userTypeId").as("userTypeId")
                        .andExpression("activityLevelList._id").as("activityLevelId")
                        .andExpression("activityLevelList.staticFormsIdList").as("staticFormsIdList")
                        .andExpression("activityLevelList.workOrderAccessId").as("workOrderAccessId")
                        .andExpression("activityLevelList.formDataId").as("formDataId")
                        .andExpression("activityLevelList.formIdCopy.formId").as("formId")
                        .andExpression("activityLevelList.rightToChoose").as("rightToChoose")
                        .andExpression("activityLevelList.organizationId").as("organizationId")
                        .andExpression("activityLevelList.existRecipientOrderUser").as("existRecipientOrderUser")
                        .andExpression("activityLevelList.nextActivityLevel._id").as("nextActivityLevelId")
                        .andExpression("activityLevelList.workRequestAcceptor").as("workRequestAcceptor")
                        .andExpression("workOrderAccepted").as("workOrderAccepted")
                        .andExpression("workOrderId").as("workOrderId")
                        .andExpression("activityLevelSequenceHistory").as("activityLevelSequenceHistory")
        );
        PrimaryPendingUserDetailsDTO primaryPendingUserDetails = mongoOperations.aggregate(aggregation, ActivitySample.class, PrimaryPendingUserDetailsDTO.class).getUniqueMappedResult();
        return PendingUserDetailsDTO.map(primaryPendingUserDetails);
    }

    public boolean checkIfActivityIsInProcess(String activityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("relatedActivityId").is(activityId));
        query.addCriteria(Criteria.where("active").is(true));
        return mongoOperations.exists(query, ActivitySample.class);
    }

    public UserIdAndUserTypeIdDTO getNextLevelUserIdAndUserTypeId(String activityInstanceId, String nextActivityLevelId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        ActivitySample activitySample = mongoOperations.findOne(query, ActivitySample.class);
        UserIdAndUserTypeIdDTO userIdAndUserTypeIdDTO = new UserIdAndUserTypeIdDTO();
        activitySample.getActivityLevelList().forEach(activityLevel -> {
            if (activityLevel.getId().equals(nextActivityLevelId)) {
//                Organization organization = organService.getRelatedOrganByOrganId(activityLevel.getOrganizationId());
                UserType userType = userTypeService.getRelatedUserTypeByUserTypeId(activityLevel.getUserTypeId());
//                userIdAndUserTypeIdDTO.setOrganizationId(activityLevel.getOrganizationId());
//                userIdAndUserTypeIdDTO.setOrganizationName(organization.getName());
                userIdAndUserTypeIdDTO.setUserTypeId(activityLevel.getUserTypeId());
                userIdAndUserTypeIdDTO.setUserTypeName(userType.getName());
                userIdAndUserTypeIdDTO.setCandidateMode(activityLevel.getCandidateMode());
                userIdAndUserTypeIdDTO.setExistRecipientOrderUser(activityLevel.isExistRecipientOrderUser());
                List<UserIdAndUserNameDTO> userIdAndUserNameDTOS = new ArrayList<>();
                if (activityLevel.getAssignedUserId() != null) {
                    User user = userService.getActivityLevelUser(activityLevel.getAssignedUserId());
                    UserIdAndUserNameDTO userIdAndUserNameDTO = new UserIdAndUserNameDTO();
                    userIdAndUserNameDTO.setId(user.getId());
                    userIdAndUserNameDTO.setName(user.getName());
                    userIdAndUserNameDTO.setFamily(user.getFamily());
                    userIdAndUserNameDTOS.add(userIdAndUserNameDTO);
                    userIdAndUserTypeIdDTO.setUserIdAndUserNameDTOList(userIdAndUserNameDTOS);
                }
                if (activityLevel.getAssignedUserId() == null && activityLevel.getCandidateUserIdList().equals(new ArrayList<>())) {
//                    List<User> userList = userService.getUsersOfActivityByUserTypeId(activityLevel.getUserTypeId(), activityLevel.getOrganizationId());
                    List<User> userList = userService.getAllUsersOfActivity(activityLevel.getUserTypeId());
                    userList.forEach(user -> {
                        UserIdAndUserNameDTO userIdAndUserNameDTO = new UserIdAndUserNameDTO();
                        userIdAndUserNameDTO.setId(user.getId());
                        userIdAndUserNameDTO.setName(user.getName());
                        userIdAndUserNameDTO.setFamily(user.getFamily());
                        userIdAndUserNameDTOS.add(userIdAndUserNameDTO);
                        userIdAndUserTypeIdDTO.setUserIdAndUserNameDTOList(userIdAndUserNameDTOS);
                    });
                }
                if (!activityLevel.getCandidateUserIdList().equals(new ArrayList<>()) && activityLevel.getAssignedUserId() == null) {
                    List<User> userList = userService.getCandidateUsersOfActivityByUserIdList(activityLevel.getCandidateUserIdList());
                    userList.forEach(user -> {
                        UserIdAndUserNameDTO userIdAndUserNameDTO = new UserIdAndUserNameDTO();
                        userIdAndUserNameDTO.setId(user.getId());
                        userIdAndUserNameDTO.setName(user.getName());
                        userIdAndUserNameDTO.setFamily(user.getFamily());
                        userIdAndUserNameDTOS.add(userIdAndUserNameDTO);
                        userIdAndUserTypeIdDTO.setUserIdAndUserNameDTOList(userIdAndUserNameDTOS);
                    });
                }
            }
        });
        return userIdAndUserTypeIdDTO;
    }

    @GetMapping("choose-next-activity-level-chosen-candidate-users")
    public ActivitySample chooseNextActivityLevelChosenCandidateUsers(String activityInstanceId, String activityLevelId, List<String> userIdList) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("activityLevelList").elemMatch(Criteria.where("_id").is(activityLevelId)));
        Update update = new Update();
        update.set("activityLevelList.$.chosenCandidateUserIdList", userIdList);
        return mongoOperations.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), ActivitySample.class);
    }

    public boolean checkIfActivitySampleAssigned(String activityInstanceId) {
        System.out.println(activityInstanceId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("activityLevelList").elemMatch(Criteria.where("actionLevel").is("pending")
                .andOperator(Criteria.where("assignedUserId").is(null))));
        return mongoOperations.exists(query, ActivitySample.class);
    }

    public boolean acceptGroupRequest(String userId, String activityInstanceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("activityLevelList").elemMatch(Criteria.where("actionLevel").is("pending")
                .andOperator(Criteria.where("assignedUserId").ne(null))));
        boolean exist = mongoOperations.exists(query, ActivitySample.class);
        if (exist) {
            return false;
        } else {
            Query query2 = new Query();
            query2.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
            query2.addCriteria(Criteria.where("activityLevelList").elemMatch(Criteria.where("actionLevel").is("pending")));
            Update update = new Update();
            update.set("activityLevelList.$.assignedUserId", userId);
            mongoOperations.updateFirst(query2, update, ActivitySample.class);
            return true;
        }
    }

    public void workOrderAcceptedByManager(String activityInstanceId, boolean workOrderAccepted) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        Update update = new Update();
        update.set("workOrderAccepted", workOrderAccepted);
        mongoOperations.updateFirst(query, update, ActivitySample.class);
    }

    public NewForm getFormIdOfTheActivitySample(String activityInstanceId, String activityLevelId) {
        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("activityInstanceId").is(activityInstanceId);
        Criteria criteria2 = new Criteria();
        criteria2.and("activityLevelList._id").is(activityLevelId);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.project("activityLevelList"),
                Aggregation.unwind("activityLevelList"),
                Aggregation.match(criteria2),
                Aggregation.project()
                        .and("activityLevelList.formId").as("formId"),
                Aggregation.lookup("newForm", "formId", "_id", "newForm"),
                Aggregation.unwind("newForm"),
                Aggregation.replaceRoot("newForm")
        );
        return mongoOperations.aggregate(aggregation, ActivitySample.class, NewForm.class).getUniqueMappedResult();
    }

    public List<FormAndFormDataDTO> getActivitySampleFormAndFormData(String activitySampleId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activitySampleId));
        query.fields()
                .include("activityLevelList");
        ActivitySample activitySample = mongoOperations.findOne(query, ActivitySample.class);
        List<String> formIdList = new ArrayList<>();
        List<String> formDataIdList = new ArrayList<>();
        activitySample.getActivityLevelList().forEach(activityLevel -> {
            if (activityLevel.getFormIdCopy() != null)
                formIdList.add(activityLevel.getFormIdCopy().getFormId());

            if (activityLevel.getFormDataId() != null)
                formDataIdList.add(activityLevel.getFormDataId());
        });

        List<FormData> formDataList = formDataController.getFormDataList(formDataIdList);
        List<NewForm> formList = newFormController.getFormList(formIdList);
//        List<Form> formList = formController.getFormList(formIdList);
        return FormAndFormDataDTO.map2(formList, formDataList);
    }

    public boolean checkIfWorkOrderIsInProcess(String workOrderId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("workOrderId").is(workOrderId));
        query.addCriteria(Criteria.where("active").is(true));
        return mongoOperations.exists(query, ActivitySample.class);
    }

    public boolean checkIfActivityLevelIsPending(String activityInstanceId, String activityLevelId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("activityLevelList").elemMatch(Criteria.where("_id").is(activityLevelId)
                .andOperator(Criteria.where("actionLevel").is("pending"))));
        return mongoOperations.exists(query, ActivitySample.class);
    }

    public List<ActivityScheduleGetPageDTO> ActivityScheduleGetPage(String userId, String userTypeId, ActivityScheduleDTO entity, Pageable pageable) {

        SkipOperation skipOperation = new SkipOperation(pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = new LimitOperation(pageable.getPageSize());

        Criteria criteria = new Criteria();
        criteria.and("active").is(true);
        criteria.and("fromSchedule").is(true);


        criteria.and("activityLevelList.actionLevel").is("pending");
        criteria.orOperator(
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(new ArrayList<>()),
                        Criteria.where("activityLevelList.userTypeId").is(userTypeId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(userId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(userId)
                )
        );

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
        if (entity.getStartDate() != null && !entity.getStartDate().equals("")) {

            Date start = entity.getStartDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 1);
            start = cal.getTime();

            Date end = entity.getStartDate();
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.setTime(start);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 59);
            end = c.getTime();

            criteria.andOperator(Criteria.where("creationDateOfWorkRequest").gte(start)
                    , Criteria.where("creationDateOfWorkRequest").lte(end)
            );
        }

        Aggregation aggregation = Aggregation.newAggregation(
                unwind("activityLevelList"),
                match(criteria),
                project()
                        .and(ConvertOperators.ToObjectId.toObjectId("$workOrderId")).as("workOrderId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$activityTypeId"))).as("activityTypeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$mainSubSystemId"))).as("mainSubSystemId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$workCategoryId"))).as("workCategoryId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$importanceDegreeId"))).as("importanceDegreeId")
                        .and("activityInstanceId").as("activityInstanceId")
                        .and("activityLevelList.workRequestAcceptor").as("workRequestAcceptor")
                        .and("activityLevelList.id").as("activityLevelId")
                        .and("active").as("active")
                        .and("assetStatus").as("assetStatus")
                        .and("minorSubSystem").as("minorSubSystem")
                        .and("creationDateOfWorkRequest").as("creationDateOfWorkRequest")
                , lookup("workOrder", "workOrderId", "_id", "workOrder")
                , lookup("asset", "assetId", "_id", "asset")
                , lookup("asset", "mainSubSystemId", "_id", "mainSubSystem")
                , lookup("activityType", "activityTypeId", "_id", "activityType")
                , lookup("workCategory", "workCategoryId", "_id", "workCategory")
                , lookup("importanceDegree", "importanceDegreeId", "_id", "importanceDegree")
                , unwind("workOrder")
                , project()
                        .and("activityInstanceId").as("activityInstanceId")
                        .and("active").as("active")
                        .and("workRequestAcceptor").as("workRequestAcceptor")
                        .and("activityLevelId").as("activityLevelId")
                        .and("assetStatus").as("assetStatus")
                        .and("minorSubSystem").as("minorSubSystem")
                        .and("creationDateOfWorkRequest").as("startDate")
                        .and("workOrderId").as("workOrderId")
                        .and("workOrder.solution").as("solution")
                        .and("workOrder.activityTime").as("activityTime")
                        .and("workOrder.estimateCompletionDate").as("estimateCompletionDate")
                        .and("assetId").as("assetId")
                        .and("asset.name").as("assetName")
                        .and("activityTypeId").as("activityTypeId")
                        .and("activityType.name").as("activityTypeName")
                        .and("mainSubSystemId").as("mainSubSystemId")
                        .and("mainSubSystem.name").as("mainSubSystemName")
                        .and("workCategoryId").as("workCategoryId")
                        .and("workCategory.name").as("workCategoryName")
                        .and("importanceDegreeId").as("importanceDegreeId")
                        .and("importanceDegree.name").as("importanceDegreeName"),
                Aggregation.sort(Sort.Direction.DESC,"startDate"),
                skipOperation,
                limitOperation

        );
        return mongoOperations.aggregate(aggregation, ActivitySample.class, ActivityScheduleGetPageDTO.class).getMappedResults();
    }

    public Page<ActivityScheduleGetPageDTO> getPage(String userId, ActivityScheduleDTO activityScheduleDTO, Pageable pageable) {
        User user = userService.getUserUserTypeIds(userId);
        return new PageImpl<>(
                ActivityScheduleGetPage(userId, user.getUserTypeId(), activityScheduleDTO, pageable),
                pageable,
                ActivityScheduleGetPageSize(userId, user.getUserTypeId(), activityScheduleDTO)
        );
    }

    private long ActivityScheduleGetPageSize(String userId, String userTypeId, ActivityScheduleDTO entity) {

        Criteria criteria = new Criteria();
        criteria.and("active").is(true);
        criteria.and("fromSchedule").is(true);

        criteria.and("activityLevelList.actionLevel").is("pending");
        criteria.orOperator(
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(new ArrayList<>()),
                        Criteria.where("activityLevelList.userTypeId").is(userTypeId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(userId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(userId)
                )
        );

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
        if (entity.getStartDate() != null && !entity.getStartDate().equals("")) {
            Date start = entity.getStartDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 1);
            start = cal.getTime();

            Date end = entity.getStartDate();
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.setTime(start);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 59);
            end = c.getTime();

            criteria.andOperator(Criteria.where("creationDateOfWorkRequest").gte(start)
                    , Criteria.where("creationDateOfWorkRequest").lte(end)
            );
        }

        Aggregation aggregation = Aggregation.newAggregation(
                unwind("activityLevelList"),
                match(criteria),
                project()
                        .and(ConvertOperators.ToObjectId.toObjectId("$workOrderId")).as("workOrderId")
        );
        return mongoOperations.aggregate(aggregation, ActivitySample.class, ActivityScheduleGetPageDTO.class).getMappedResults().size();
    }

    public List<ActivityScheduleGetPageDTO> ActivityGetPageForExcel(String userId, String userTypeId, ActivityScheduleDTO entity) {

        Criteria criteria = new Criteria();
        criteria.and("active").is(true);
        criteria.and("fromSchedule").is(true);

        criteria.and("activityLevelList.actionLevel").is("pending");
        criteria.orOperator(
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(new ArrayList<>()),
                        Criteria.where("activityLevelList.userTypeId").is(userTypeId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(userId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(userId)
                )
        );

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
        if (entity.getStartDate() != null && !entity.getStartDate().equals("")) {
            Date start = entity.getStartDate();
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 1);
            start = cal.getTime();

            Date end = entity.getStartDate();
            Calendar c = Calendar.getInstance();
            c.setTime(end);
            c.setTime(start);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 59);
            end = c.getTime();

            criteria.andOperator(Criteria.where("creationDateOfWorkRequest").gte(start)
                    , Criteria.where("creationDateOfWorkRequest").lte(end)
            );
        }

        Aggregation aggregation = Aggregation.newAggregation(
                unwind("activityLevelList"),
                match(criteria),
                project()
                        .and(ConvertOperators.ToObjectId.toObjectId("$workOrderId")).as("workOrderId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$activityTypeId"))).as("activityTypeId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$mainSubSystemId"))).as("mainSubSystemId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$workCategoryId"))).as("workCategoryId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$importanceDegreeId"))).as("importanceDegreeId")
                        .and("activityInstanceId").as("activityInstanceId")
                        .and("activityLevelList.workRequestAcceptor").as("workRequestAcceptor")
                        .and("activityLevelList.id").as("activityLevelId")
                        .and("active").as("active")
                        .and("assetStatus").as("assetStatus")
                        .and("minorSubSystem").as("minorSubSystem")
                        .and("creationDateOfWorkRequest").as("creationDateOfWorkRequest")
                , lookup("workOrder", "workOrderId", "_id", "workOrder")
                , lookup("asset", "assetId", "_id", "asset")
                , lookup("asset", "mainSubSystemId", "_id", "mainSubSystem")
                , lookup("activityType", "activityTypeId", "_id", "activityType")
                , lookup("workCategory", "workCategoryId", "_id", "workCategory")
                , lookup("importanceDegree", "importanceDegreeId", "_id", "importanceDegree")
                , unwind("workOrder")
                , project()
                        .and("activityInstanceId").as("activityInstanceId")
                        .and("active").as("active")
                        .and("workRequestAcceptor").as("workRequestAcceptor")
                        .and("activityLevelId").as("activityLevelId")
                        .and("assetStatus").as("assetStatus")
                        .and("minorSubSystem").as("minorSubSystem")
                        .and("creationDateOfWorkRequest").as("startDate")
                        .and("workOrderId").as("workOrderId")
                        .and("workOrder.solution").as("solution")
                        .and("workOrder.activityTime").as("activityTime")
                        .and("workOrder.estimateCompletionDate").as("estimateCompletionDate")
                        .and("assetId").as("assetId")
                        .and("asset.name").as("assetName")
                        .and("activityTypeId").as("activityTypeId")
                        .and("activityType.name").as("activityTypeName")
                        .and("mainSubSystemId").as("mainSubSystemId")
                        .and("mainSubSystem.name").as("mainSubSystemName")
                        .and("workCategoryId").as("workCategoryId")
                        .and("workCategory.name").as("workCategoryName")
                        .and("importanceDegreeId").as("importanceDegreeId")
                        .and("importanceDegree.name").as("importanceDegreeName")
        );
        return mongoOperations.aggregate(aggregation, ActivitySample.class, ActivityScheduleGetPageDTO.class).getMappedResults();
    }

    public List<NoticeBoardGetAllDTO> EmActivityGetPageForExcel(String userId, String userTypeId, UserPendingActivityDTO userPendingActivityDTO) {

        Criteria criteria = new Criteria();
        criteria.and("deleted").ne(true);
        criteria.and("active").is(true);
        criteria.and("fromSchedule").is(false);

        if (userPendingActivityDTO.getAssetId() != null && !userPendingActivityDTO.getAssetId().equals("")) {
            criteria.and("assetId").is(userPendingActivityDTO.getAssetId());
        }

        if (userPendingActivityDTO.getMaintenanceType() != null) {
            criteria.and("workRequestMaintenanceType").is(userPendingActivityDTO.getMaintenanceType());
        }

        if (userPendingActivityDTO.getPriority() != null) {
            criteria.and("workRequestPriority").is(userPendingActivityDTO.getPriority());
        }

        if (userPendingActivityDTO.getRequestTitle() != null && !userPendingActivityDTO.getRequestTitle().equals("")) {
            criteria.and("workRequestTitle").regex(userPendingActivityDTO.getRequestTitle());
        }

        if (userPendingActivityDTO.getFrom() != null && userPendingActivityDTO.getUntil() == null) {
            criteria.and("creationDateOfWorkRequest").gte(userPendingActivityDTO.getFrom());
        }

        if (userPendingActivityDTO.getUntil() != null && userPendingActivityDTO.getFrom() == null) {
            criteria.and("creationDateOfWorkRequest").lte(userPendingActivityDTO.getUntil());
        }

        if (userPendingActivityDTO.getUntil() != null && userPendingActivityDTO.getFrom() != null) {
            criteria.andOperator(Criteria.where("creationDateOfWorkRequest").gte(userPendingActivityDTO.getFrom())
                    , Criteria.where("creationDateOfWorkRequest").lte(userPendingActivityDTO.getUntil())
            );
        }

        if (userPendingActivityDTO.getFromSchedule() != null && userPendingActivityDTO.getFromSchedule().equals(true)) {
            criteria.and("fromSchedule").is(true);
        }

        if (userPendingActivityDTO.getFromSchedule() != null && userPendingActivityDTO.getFromSchedule().equals(false)) {
            criteria.and("fromSchedule").is(false);
        }

        if (userPendingActivityDTO.getUserId() != null && !userPendingActivityDTO.getUserId().equals("")) {
            criteria.and("workRequesterId").is(userPendingActivityDTO.getUserId());
        }

        if (userPendingActivityDTO.getPmSheetCode() != null && !userPendingActivityDTO.getPmSheetCode().equals("")) {
            criteria.and("pmSheetCode").regex(userPendingActivityDTO.getPmSheetCode());
        }

        if (userPendingActivityDTO.getRequestType() != null) {
            if (userPendingActivityDTO.getRequestType().equals(RequestType.SENDFORYOU)) {
                criteria.and("activityLevelList.assignedUserId").ne(null);
            }
            if (userPendingActivityDTO.getRequestType().equals(RequestType.SENDFORGROUPE)) {
                criteria.and("activityLevelList.assignedUserId").is(null);
//                    .orOperator(criteria.and("assignedUserId").is(""));
            }
        }

        criteria.and("activityLevelList.actionLevel").is("pending");
        criteria.orOperator(
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(new ArrayList<>()),
                        Criteria.where("activityLevelList.userTypeId").is(userTypeId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(null),
                        Criteria.where("activityLevelList.chosenCandidateUserIdList").is(userId)
                ),
                new Criteria().andOperator(
                        Criteria.where("activityLevelList.assignedUserId").is(userId)
                )
        );

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("asset")
                .localField("assetObjId")
                .foreignField("_id")
                .as("asset");

        LookupOperation lookupOperation2 = LookupOperation.newLookup()
                .from("user")
                .localField("workRequesterId")
                .foreignField("_id")
                .as("user");

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("activityLevelList"),
                Aggregation.match(criteria),
                Aggregation.sort(Sort.Direction.DESC, "creationDateOfWorkRequest"),
                Aggregation.project()
                        .and("activityInstanceId").as("instanceId")
                        .and("pmSheetCode").as("pmSheetCode")
                        .and("workRequestTitle").as("workRequestTitle")
                        .and("creationDateOfWorkRequest").as("workRequestTime")
                        .and("fromSchedule").as("isSchedule")
                        .and("workRequestPriority").as("priority")
                        .and("workRequestMaintenanceType").as("maintenanceType")
                        .and("activityLevelList.requestStatus").as("requestStatus")
                        .and("activityLevelList.assignedUserId").as("assignedUserId")
                        .and("activityLevelList.id").as("activityLevelId")
                        .and("activityLevelList.workRequestAcceptor").as("workRequestAcceptor")
                        .and("workOrderId").as("workOrderId")
                        .and("assetId").as("assetId")
                        .and(ConvertOperators.ToObjectId.toObjectId("$workRequesterId")).as("workRequesterId")
                        .and((ConvertOperators.ToObjectId.toObjectId("$assetId"))).as("assetObjId"),
                lookupOperation,
                lookupOperation2,
                Aggregation.unwind("asset"),
                Aggregation.unwind("user"),
                Aggregation.project()
                        .and("instanceId").as("instanceId")
                        .and("pmSheetCode").as("pmSheetCode")
                        .and("workRequestTitle").as("workRequestTitle")
                        .and("assignedUserId").as("assignedUserId")
                        .and("workRequestTime").as("workRequestTime")
                        .and("isSchedule").as("isSchedule")
                        .and("priority").as("priority")
                        .and("maintenanceType").as("maintenanceType")
                        .and("requestStatus").as("requestStatus")
                        .and("activityLevelId").as("activityLevelId")
                        .and("workOrderId").as("workOrderId")
                        .and("workRequestAcceptor").as("workRequestAcceptor")
                        .and("assetId").as("assetId")
                        .and("asset.name").as("assetName")
                        .and("asset.code").as("assetCode")
                        .and("user._id").as("requesterId")
                        .and("user.name").as("requesterName")
                        .and("user.family").as("requesterFamily")
        );
        return mongoOperations.aggregate(aggregation, ActivitySample.class, NoticeBoardGetAllDTO.class).getMappedResults();
    }

    public boolean checkIfScheduleIsInActivityProcess(String scheduleId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("scheduleId").is(scheduleId));
        return mongoOperations.exists(query, ActivitySample.class);
    }
}
