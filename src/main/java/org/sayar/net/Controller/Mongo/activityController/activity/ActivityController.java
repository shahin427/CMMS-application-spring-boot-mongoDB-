package org.sayar.net.Controller.Mongo.activityController.activity;

import org.bson.types.ObjectId;
import org.sayar.net.Controller.ActivitySampleController;
import org.sayar.net.Controller.newController.NewFormController;
import org.sayar.net.Model.*;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.Mongo.MyModel.ActivityLevel;
import org.sayar.net.Model.Mongo.poll.controller.form.FormController;
import org.sayar.net.Model.Mongo.poll.controller.form.FormDataController;
import org.sayar.net.Model.Mongo.poll.model.form.Form;
import org.sayar.net.Model.Mongo.poll.model.form.FormData;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Service.Mongo.activityServices.activity.ActivityService;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.UserTypeService;
import org.sayar.net.Service.WorkRequestService;
import org.sayar.net.Service.newService.AssetService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@RestController
@RequestMapping("activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    MongoOperations mongoOperations;

    private ResponseContent content;

    @Autowired
    private ActivitySampleController activitySampleController;

    @Autowired
    private FormController formController;

    @Autowired
    private FormDataController formDataController;

    @Autowired
    private UserService userService;

    @Autowired
    private WorkRequestService workRequestService;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private NewFormController newFormController;


    @GetMapping("getActivityListByOrgId")
    public ResponseEntity<?> getActivityListByCompanyId() {
        try {
            content = new ResponseContent();
            Query query = new Query();
            List<Activity> activityList = mongoOperations.find(query, Activity.class);
            content.setData(activityList);
            content.setFlag(true);
            return ResponseEntity.ok().body(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "light")
    public ResponseEntity<?> getAllLight() {
        try {
            content = new ResponseContent();
            content.setFlag(true);
            return ResponseEntity.ok().body(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e);
        }
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOne(@PathParam("activityId") String activityId) {
        try {
            content = new ResponseContent();
            Query query = new Query();
            Criteria criteria = new Criteria();
            criteria.andOperator(
                    Criteria.where("id").is(activityId)
            );
            Aggregation aggregation = newAggregation(
                    match(criteria),
                    project()
                            .andExpression("id").as("_id")
                            .andExpression("title").as("title")
                            .andExpression("activityLevelList").as("activityLevelList")
                            .andExpression("description").as("description"),

                    unwind("activityLevelList"),
                    lookup("form", "activityLevelList.formId", "id", "activityLevelList.form"),
                    group("_id", "title", "description")
                            .push("activityLevelList").as("activityLevelList"),
//                            .and("_id._id").as("id")LevelList"),
                    Aggregation.project()
                            .and("_id.title").as("title")
                            .and("_id.description").as("description")
                            .and("activityLevelList").as("activityLevelList")
                            .andExclude("_id")
            );
            AggregationResults<Activity> groupResults = mongoOperations.aggregate(aggregation, Activity.class, Activity.class);
            List<Activity> result = groupResults.getMappedResults();

            result.get(0).setId(activityId);
            content.setData(result.get(0));
            query.addCriteria(criteria);
            content.setFlag(true);
            return ResponseEntity.ok().body(content);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(content);
        }
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody @Valid Activity activity) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < activity.getActivityLevelList().size(); i++) {
            if (activity.getActivityLevelList().get(i).getId() == null ||
                    activity.getActivityLevelList().get(i).getId().equals("")
                    || activity.getActivityLevelList().get(i).getId().length() <= 10
            ) {
                map.put(activity.getActivityLevelList().get(i).getId(), new ObjectId().toString());
            }
        }

        activity.setId(new ObjectId().toString());
        activity.getActivityLevelList().forEach(activityLevel1 -> {
            if (activityLevel1.getFormIdCopy() != null && activityLevel1.getFormIdCopy().getFormId() != null)
                activityLevel1.setFormId(new ObjectId(activityLevel1.getFormIdCopy().getFormId()));
        });
        return ResponseEntity.ok().body(activityService.save(activity));
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Activity activity) {
        Map<String, String> map = new HashMap<>();
        String tempId;
        ActivityLevel activityLevel = new ActivityLevel(new ActivityLevel(), new ActivityLevel());

        tempId = activityLevel.getId();
        activityLevel.setId(map.get(tempId));
        tempId = activityLevel.getNextActivityLevel().getId();
        activityLevel.getNextActivityLevel().setId(map.get(tempId));
        tempId = activityLevel.getPrevActivityLevel().getId();
        activityLevel.getPrevActivityLevel().setId(map.get(tempId));
        activityService.save(activity);
        return ResponseEntity.ok().body(activity);
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllActivityList(@PathParam("term") String term, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(activityService.findAllActivity(term, pageable, totalElement));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteOneActivity(@PathParam("activityId") String activityId) {
//        if (assetService.ifActivityExistInAsset(activityId)) {
//            return ResponseEntity.ok().body("\"برای حذف این فرایند ابتدا آن را از قسمت دارایی ها پاک کنید\"");
//        } else if (activitySampleController.ifActivityExistInActivitySample(activityId)) {
//            return ResponseEntity.ok().body("\"برای حذف این فرایند ابتدا آن را از قسمت فرایندهای فعال پاک کنید\"");
//        } else {
        return ResponseEntity.ok().body(activityService.logicDeleteById(activityId, Activity.class));
//        }
    }

    @GetMapping("get-all-limit")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(activityService.getAllLimit());
    }

    @GetMapping("get-user-by-user-type-id-and-organization-id")
    public ResponseEntity<?> getAllUsersByUserTypeIdAndOrganId(@PathParam("userTypeId") String userTypeId, @PathParam("organizationId") String organizationId) {
        return ResponseEntity.ok().body(activityService.getAllUsersByUserTypeIdAndOrganId(userTypeId, organizationId));
    }

    @GetMapping("get-user-name-organisation-name-and-user-type-name")
    public ResponseEntity<?> getUserNameOrganisationNameAndUserTypeName(@PathParam("userId") String userId,
                                                                        @PathParam("userTypeId") String userTypeId,
                                                                        @PathParam("organisationId") String organisationId) {
        return ResponseEntity.ok().body(activityService.getUserNameOrganisationNameAndUserTypeName(userId, userTypeId, organisationId));
    }

    @GetMapping("get-all-activity")
    public ResponseEntity<?> getAllActivity() {
        return ResponseEntity.ok().body(activityService.getAllActivity());
    }

    @GetMapping("get-form-by-activity-id")
    public ResponseEntity<?> getFormByActivityId(@PathParam("activityId") String activityId) {
        return ResponseEntity.ok().body(activityService.getFormByActivityId(activityId));
    }

    @PostMapping("get-activity-of-pending-user-with-pagination")
    public ResponseEntity<?> getActivityOfPendingUserWithPagination(@PathParam("userId") String userId,
                                                                    @RequestBody UserPendingActivityDTO userPendingActivityDTO,
                                                                    Pageable pageable) {
        return ResponseEntity.ok().body(activitySampleController.getActivitySamplesHaveConsideredId(userId, userPendingActivityDTO, pageable));
    }

    @GetMapping("get-pending-accepted-and-rejected-levels")
    public ResponseEntity<?> getPendingActiveAndRejectedActivityLevel(@PathParam("activityInstanceId") String activityInstanceId) {
        ActivitySample activitySample = activitySampleController.getPendingAndActiveActivityLevel(activityInstanceId);
        ActivitySampleDTO activitySampleDTO = addToActivitySampleDTO(activitySample);
        List<String> userIdList = new ArrayList<>();
        List<String> userTypeIdList = new ArrayList<>();
        List<String> formIdList = new ArrayList<>();

        for (ActivityLevelDTO activityLevel : activitySampleDTO.getActivityLevelDTOList()) {
            userIdList.add(activityLevel.getAssignedUserId());
            userTypeIdList.add(activityLevel.getUserTypeId());
            formIdList.add(activityLevel.getFormIdBeta());
        }

        List<User> userList = userService.getUsersOfActivity(userIdList);
        List<UserType> userTypeList = userTypeService.getUserTypeOfActivity(userTypeIdList);
        List<Form> formList = formController.getFormsOfActivitySamples(formIdList);

        activitySampleDTO.getActivityLevelDTOList().forEach(activityLevel -> {
            for (Form form : formList) {
                if (activityLevel.getFormIdBeta() != null && activityLevel.getFormIdBeta().equals(form.getId())) {
                    activityLevel.setFormName(form.getTitle());
                }
            }
        });

        activitySampleDTO.getActivityLevelDTOList().forEach(activityLevel -> {
            for (User user : userList) {
                if (activityLevel.getAssignedUserId() != null && activityLevel.getAssignedUserId().equals(user.getId())) {
                    activityLevel.setAssignedUserName(user.getName());
                    activityLevel.setAssignedUserFamily(user.getFamily());
                }
            }
        });

        activitySampleDTO.getActivityLevelDTOList().forEach(activityLevel -> {
            for (UserType userType : userTypeList) {
                if (activityLevel.getUserTypeId() != null && activityLevel.getUserTypeId().equals(userType.getId())) {
                    activityLevel.setUserTypeTitle(userType.getName());
                }
            }
        });
        return ResponseEntity.ok().body(activitySampleDTO);
    }

    private ActivitySampleDTO addToActivitySampleDTO(ActivitySample activitySample) {
        List<ActivityLevelDTO> activityLevelDTOList = new ArrayList<>();
        activitySample.getActivityLevelList().forEach(acl -> {
            ActivityLevelDTO activityLevelDTO = new ActivityLevelDTO(
                    acl.getId(),
                    acl.getCandidateUserIdList(),
                    acl.getChosenCandidateUserIdList(),
                    acl.getCandidateMode(),
                    acl.getAssignedUserId(),
                    acl.getTitle(),
                    acl.getRecipe(),
                    acl.getOrgId(),
                    acl.getFormId(),
                    acl.getForm(),
                    acl.getFormIdCopy(),
                    acl.getNextActivityLevel(),
                    acl.getPrevActivityLevel(),
                    acl.getFirstStepIs(),
                    acl.getLastStepIs(),
                    acl.getShowHistory(),
                    acl.getCanOperate(),
                    acl.getOrganizationName(),
                    acl.getFormTitle(),
                    acl.getUserTypeId(),
                    acl.getOrganizationId(),
                    acl.isCancel(),
                    acl.getActionLevel(),
                    acl.getFormIdBeta(),
                    acl.getFormName(),
                    acl.getFormDataId(),
                    acl.getPendingDate(),
                    acl.getAnswerDate(),
                    acl.getStaticFormsIdList(),
                    acl.getStartNewActivity(),
//                    acl.isRightToChoose(),
                    acl.isExistRecipientOrderUser(),
                    acl.getWorkOrderAccessId(),
                    acl.getRequestStatus(),
                    acl.getWorkOrderAndFormRepositoryId()
            );
            if (acl.getCandidateUserIdList() != null) {
                for (String candidate : acl.getCandidateUserIdList()) {
                    UserIdAndName userIdAndName = new UserIdAndName();
                    userIdAndName.setId(candidate);
                }
            }
            activityLevelDTOList.add(activityLevelDTO);
        });

        ActivitySampleDTO activitySampleDTO = new ActivitySampleDTO();
        activitySampleDTO.setId(activitySample.getId());
        activitySampleDTO.setTitle(activitySample.getTitle());
        activitySampleDTO.setDescription(activitySample.getDescription());
        activitySampleDTO.setOrganizationId(activitySample.getOrganizationId());
        activitySampleDTO.setRequesterId(activitySample.getRequesterId());
        activitySampleDTO.setRelatedActivityId(activitySample.getRelatedActivityId());
        activitySampleDTO.setActivityInstanceId(activitySample.getActivityInstanceId());
        activitySampleDTO.setActivityLevelSequenceHistory(activitySample.getActivityLevelSequenceHistory());
        activitySampleDTO.setWorkOrderId(activitySample.getWorkOrderId());
        activitySampleDTO.setActive(activitySample.isActive());
        activitySampleDTO.setFromSchedule(activitySample.isFromSchedule());
        activitySampleDTO.setAssetId(activitySample.getAssetId());
        activitySampleDTO.setWorkRequestId(activitySample.getWorkRequestId());
        activitySampleDTO.setWorkRequestTitle(activitySample.getWorkRequestTitle());
        activitySampleDTO.setRequesterId(activitySample.getWorkRequesterId());
        activitySampleDTO.setRequesterName(activitySample.getWorkRequesterName());
        activitySampleDTO.setRequesterFamilyName(activitySample.getWorkRequesterFamilyName());
        activitySampleDTO.setRequesterUserTypeName(activitySample.getWorkRequesterUserTypeName());
        activitySampleDTO.setWorkRequestPriority(activitySample.getWorkRequestPriority());
        activitySampleDTO.setWorkRequestMaintenanceType(activitySample.getWorkRequestMaintenanceType());
        activitySampleDTO.setCreationDateOfWorkRequest(activitySample.getCreationDateOfWorkRequest());
        activitySampleDTO.setActivityLevelDTOList(activityLevelDTOList);
        return activitySampleDTO;
    }

    @GetMapping("get-each-activity-level-form")
    public ResponseEntity<?> getEachActivityLevelForm(@PathParam("activityId") String activityId, @PathParam("activityLevelId") String activityLevelId) {
        return ResponseEntity.ok().body(activityService.getEachActivityLevelForm(activityId, activityLevelId));
    }

    @GetMapping("get-activity-sample-of-the-requester")
    public ResponseEntity<?> getActivitySamplesOfTheRequester(@PathParam("requesterId") String requesterId) {
        return ResponseEntity.ok().body(activitySampleController.getActivitySampleOfTheRequester(requesterId));
    }

    @GetMapping("get-activity-sample-by-instance-id")
    public ResponseEntity<?> getActivitySampleByInstanceId(@PathParam("workRequestId") String workRequestId) {
        return ResponseEntity.ok().body(activitySampleController.getActivitySampleByInstanceId(workRequestId));
    }

    @GetMapping("get-form-by-activity-level-id-and-instance-id")
    public ResponseEntity<?> getFormByActivityLevelIdAndInstanceId(@PathParam("instanceId") String instanceId,
                                                                   @PathParam("activityLevelId") String activityLevelId) {

        ActivitySample activitySample = activitySampleController.getFormDataOfSampleActivity(instanceId, activityLevelId);
        String formId = null;
        if (activitySample.getActivityLevelList().get(Integer.parseInt(activityLevelId)).getFormIdCopy() != null) {
            formId = activitySample.getActivityLevelList().get(Integer.parseInt(activityLevelId)).getFormIdCopy().getFormId();
        }
        String formDataId = null;
        if (activitySample.getActivityLevelList().get(Integer.parseInt(activityLevelId)).getFormDataId() != null) {
            formDataId = activitySample.getActivityLevelList().get(Integer.parseInt(activityLevelId)).getFormDataId();
        }
        FormData formData = null;
        if (formDataId != null) {
            formData = formDataController.getFormDataOfSampleActivity(formDataId);
        }
        NewForm form = newFormController.getFormByActivitySampleFormId(formId);
        return ResponseEntity.ok().body(FormAndFormDataDTO.map(form, formData));
    }

    @PostMapping("create-form-data-by-form")
    public void createFormDataByForm(@RequestBody FormData formData,
                                     @PathParam("instanceId") String instanceId,
                                     @PathParam("activityLevelId") String activityLevelId) {
        formData.setSystemCreationDate(new Date());
        FormData createdFormData = formDataController.createFormData(formData);
        activitySampleController.saveFormDataIdInConsideredActivitySample(instanceId, activityLevelId, createdFormData.getId());
    }

    @PostMapping("when-user-pushes-the-accept-button")
    public ResponseEntity<?> whenUserPushesAcceptButton(@RequestBody FormData formData,
                                                        @PathParam("instanceId") String instanceId,
                                                        @PathParam("activityLevelId") String activityLevelId) {
        ActivitySample activitySample = activitySampleController.getTheActivitySample(instanceId);
        if (activitySample != null) {
            // adding sequence of activityLevels
            FormData savedFormData = formDataController.createFormDataOfActivitySample(formData);
            activitySampleController.map(activityLevelId, activitySample, savedFormData.getId());
            ActivitySample modifiedActivitySample = activitySampleController.addFormDataIdToRelatedActivityLevel(savedFormData.getId(), activitySample, activityLevelId);
            activitySampleController.changeDetailsOfActivitySampleAfterAccepting(modifiedActivitySample, activityLevelId);
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(null);
        }
    }

    @PostMapping("when-user-pushes-the-accept-button-in-constant-form")
    public ResponseEntity<?> whenUserPushesTheAcceptButtonInConstantForm(@PathParam("instanceId") String instanceId,
                                                                         @PathParam("activityLevelId") String activityLevelId) {
        ActivitySample activitySample = activitySampleController.getTheActivitySample(instanceId);
        if (activitySample != null) {
            activitySampleController.map(activityLevelId, activitySample, null);
            activitySampleController.changeDetailsOfActivitySampleAfterAccepting(activitySample, activityLevelId);
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(null);
        }
    }

    @PostMapping("when-user-pushes-the-reject-button")
    public ResponseEntity<?> whenUserPushesRejectButton(@RequestBody FormData formData,
                                                        @PathParam("instanceId") String instanceId,
                                                        @PathParam("activityLevelId") String activityLevelId) {

        ActivitySample activitySample = activitySampleController.getTheActivitySampleInRejectTimeAndInHistorySequence(instanceId, activityLevelId);
        WorkOrder relevantWorkOrderOfActivitySample = workOrderService.getRelevantWorkOrder(activitySample.getWorkOrderId());
        FormData createFormData = null;
        if (formData != null) {
            createFormData = formDataController.createFormDataOfActivitySample(formData);
        }
        //adding sequence of activityLevels
        activitySampleController.createSequenceOfThisLevel(createFormData.getId(), activityLevelId, activitySample);
        ActivitySample modifiedActivitySample = activitySampleController.addFormDataIdToRelatedActivityLevel(createFormData.getId(), activitySample, activityLevelId);
        activitySampleController.changeDetailsOfActivitySampleAfterRejecting(modifiedActivitySample, activityLevelId, formData.getId(), relevantWorkOrderOfActivitySample);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("save-form-data-and-put-in-associated-activity-level")
    public ResponseEntity<?> saveFormDataAndPutInAssociatedActivityLevel(@RequestBody FormData formData,
                                                                         @PathParam("activityInstanceId") String activityInstanceId,
                                                                         @PathParam("activityLevelId") String activityLevelId) {
        FormData savedFormData = formDataController.createFormDataOfActivitySample(formData);
        ActivitySample oldVersionActivityLevel = activitySampleController.getOldVersionActivitySampleByInstanceId(activityInstanceId);
        ActivitySample modifiedActivitySample = activitySampleController.addFormDataIdToRelatedActivityLevel(savedFormData.getId(), oldVersionActivityLevel, activityLevelId);
        return ResponseEntity.ok().body(modifiedActivitySample);
    }

    @PostMapping("when-user-pushes-the-reject-button-in-constant-form")
    public ResponseEntity<?> whenUserPushesTheRejectButtonInConstantForm(@PathParam("instanceId") String instanceId,
                                                                         @PathParam("activityLevelId") String activityLevelId) {
        ActivitySample activitySample = activitySampleController.getTheActivitySampleInRejectTimeAndInHistorySequence(instanceId, activityLevelId);
        WorkOrder relevantWorkOrderOfActivitySample = workOrderService.getRelevantWorkOrder(activitySample.getWorkOrderId());
        //adding sequence of activityLevels
        activitySampleController.createSequenceOfThisLevel("", activityLevelId, activitySample);
        activitySampleController.changeDetailsOfActivitySampleAfterRejecting(activitySample, activityLevelId, "", relevantWorkOrderOfActivitySample);
        return ResponseEntity.ok().body(true);
    }

    @PostMapping("get-all-accepted-and-rejected-activities-of-user-with-pagination")
    public ResponseEntity<?> getAllAcceptedAndRejectedActivitiesOfUserWithPagination(@RequestBody ActivityHistoryDTO activityHistory, @PathParam("userId") String userId, Pageable pageable, @PathParam("state") boolean state) {
        return ResponseEntity.ok().body(activitySampleController.getAllAcceptedAndRejectedActivitiesOfUserWithPagination(activityHistory, userId, pageable, state));
    }

    @GetMapping("get-form-and-form-data-of-the-activity-level")
    public ResponseEntity<?> getFormAndFormDataOfTheActivityLevel(@PathParam("formId") String formId, @PathParam("formDataId") String formDataId) {
        return ResponseEntity.ok().body(activityService.getFormAndFormDataOfTheActivityLevel(formId, formDataId));
    }

    @PostMapping("save-work-order-id-in-activity")
    public ResponseEntity<?> saveWorkOrderIdInActivity(@PathParam("activityInstanceId") String activityInstanceId,
                                                       @PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(activitySampleController.saveWorkOrderIdInActivity(activityInstanceId, workOrderId));
    }

    @PostMapping("choose-next-activity-level-user")
    public ResponseEntity<?> chooseNextActivityLevelUser(@RequestBody ChooseNextActivityLevelUserClass chooseNextActivityLevelUserClass) {
        return ResponseEntity.ok().body(activitySampleController.chooseNextActivityLevelUser(chooseNextActivityLevelUserClass));
    }

    public boolean ifFormUsedInActivity(String formId) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("deleted").ne(true),
                        Criteria.where("activityLevelList.formIdCopy.formId").is(formId)
                ));
        return mongoOperations.exists(query, Activity.class);
    }

    public Activity getActivityForWorkOrderAccess(String activityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(activityId));
        return mongoOperations.findOne(query, Activity.class);
    }

    @GetMapping("count-pending-activity-of-the-user")
    public ResponseEntity<?> countPendingActivityOfTheUser(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(activitySampleController.countPendingActivityOfTheUser(userId));
    }

    @GetMapping("get-activity-of-pending-user")
    public ResponseEntity<?> getActivityOfPendingUser(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(activitySampleController.getPendingActivitiesTheUserShouldAnswer(userId));
    }

    public boolean ifFormExistsInActivity(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityLevelList").elemMatch(Criteria.where("formIdCopy.formId").is(id)));
        return mongoOperations.exists(query, Activity.class);
    }

    @GetMapping("check-which-activities-are-in-process")
    public ResponseEntity<?> checkWhichActivitiesAreInProcess(List<String> activityIdList) {
        return ResponseEntity.ok().body(activitySampleController.checkWhichActivitiesAreInProcess(activityIdList));
    }

    @GetMapping("get-pending-user-activity-details")
    public ResponseEntity<?> getPendingUserActivityDetails(@PathParam("activityInstanceId") String activityInstanceId) {
        if (activityInstanceId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("ورودی فرستاده نشده است", httpStatus);
        } else {
            return ResponseEntity.ok().body(activitySampleController.activityInstanceId(activityInstanceId));
        }
    }

    @GetMapping("check-if-form-used-in-activity")
    public ResponseEntity<?> checkIfFormUsedInActivity(@PathParam("formId") String formId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("activityLevelList").elemMatch(
                Criteria.where("formIdCopy.formId").is(formId)
        ));
        return ResponseEntity.ok().body(mongoOperations.exists(query, Activity.class));
    }

    @GetMapping("check-if-activity-is-in-process")
    public ResponseEntity<?> checkIfActivityIsInProcess(@PathParam("activityId") String activityId) {
        return ResponseEntity.ok().body(activitySampleController.checkIfActivityIsInProcess(activityId));
    }

    @GetMapping("get-next-level-user-and-userTypeId")
    public ResponseEntity<?> getNextLevelUserIdAndUserTypeId(@PathParam("activityInstanceId") String activityInstanceId, @PathParam("nextActivityLevelId") String nextActivityLevelId) {
        return ResponseEntity.ok().body(activitySampleController.getNextLevelUserIdAndUserTypeId(activityInstanceId, nextActivityLevelId));
    }

    @GetMapping("check-if-activity-sample-assigned")
    public ResponseEntity<?> checkIfActivitySampleAssigned(@PathParam("activityInstanceId") String activityInstanceId) {
        return ResponseEntity.ok().body(activitySampleController.checkIfActivitySampleAssigned(activityInstanceId));
    }

    @GetMapping("accept-group-request")
    public ResponseEntity<?> acceptGroupRequest(@PathParam("userId") String userId, @PathParam("activityInstanceId") String activityInstanceId) {
        return ResponseEntity.ok().body(activitySampleController.acceptGroupRequest(userId, activityInstanceId));
    }

    @GetMapping("work-order-accepted-by-manager")
    public ResponseEntity<?> workOrderAcceptedByManager(@PathParam("workOrderId") String workOrderId,
                                                        @PathParam("activityInstanceId") String activityInstanceId,
                                                        @PathParam("workRequestId") String workRequestId,
                                                        @PathParam("workOrderAccepted") boolean workOrderAccepted) {
        activitySampleController.workOrderAcceptedByManager(activityInstanceId, workOrderAccepted);
        workOrderService.workOrderAcceptedByManager(workOrderId, workOrderAccepted);
//        workRequestService.setAssessmentTrue(workRequestId);
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("get-activity-sample-form-and-form-data")
    public ResponseEntity<?> getActivitySampleFormAndFormData(@PathParam("activitySampleId") String activitySampleId) {
        return ResponseEntity.ok().body(activitySampleController.getActivitySampleFormAndFormData(activitySampleId));
    }

    @GetMapping("check-if-activity-exists-in-asset-or-activity")
    public ResponseEntity<?> checkIfActivityExistsInAsset(@PathParam("activityId") String activityId) {
        if (assetService.ifActivityExistInAsset(activityId)) {
            ActivityCheckDTO activityCheckDTO = new ActivityCheckDTO();
            activityCheckDTO.setExist(true);
            activityCheckDTO.setDescription("برای حذف این فرایند ابتدا آن را از قسمت دارایی ها پاک کنید");
            return ResponseEntity.ok().body(activityCheckDTO);
        } else if (activitySampleController.checkIfActivityIsInProcess(activityId)) {
            ActivityCheckDTO activityCheckDTO = new ActivityCheckDTO();
            activityCheckDTO.setExist(true);
            activityCheckDTO.setDescription("از این فرآیند در حال حاضر استفاده میگردد");
            return ResponseEntity.ok().body(activityCheckDTO);
        } else {
            ActivityCheckDTO activityCheckDTO = new ActivityCheckDTO();
            activityCheckDTO.setExist(false);
            activityCheckDTO.setDescription(null);
            return ResponseEntity.ok().body(activityCheckDTO);
        }
    }

    @GetMapping("check-if-activity-level-is-pending")
    public ResponseEntity<?> checkIfActivityLevelIsPending(@PathParam("activityInstanceId") String activityInstanceId, @PathParam("activityLevelId") String activityLevelId) {
        return ResponseEntity.ok().body(activitySampleController.checkIfActivityLevelIsPending(activityInstanceId, activityLevelId));
    }

    @PostMapping("activity-schedule-get-page")
    public ResponseEntity<?> ActivityScheduleGetPage(@PathParam("userId") String userId,
                                                     @RequestBody ActivityScheduleDTO activityScheduleDTO,
                                                     Pageable pageable) {
        return ResponseEntity.ok().body(activitySampleController.getPage(userId, activityScheduleDTO, pageable));
    }

    @PostMapping("activity-get-page-for-excel")
    public ResponseEntity<?> PmActivityGetPageForExcel(@PathParam("userId") String userId,
                                                       @RequestBody ActivityScheduleDTO activityScheduleDTO) {
        User user = userService.getUserUserTypeIds(userId);
        return ResponseEntity.ok().body(activitySampleController.ActivityGetPageForExcel(userId, user.getUserTypeId(), activityScheduleDTO));
    }

    @PostMapping("em-activity-get-page-for-excel")
    public ResponseEntity<?> EmActivityGetPageForExcel(@PathParam("userId") String userId, @RequestBody UserPendingActivityDTO userPendingActivityDTO) {
        User user = userService.getUserUserTypeIds(userId);
        return ResponseEntity.ok().body(activitySampleController.EmActivityGetPageForExcel(userId, user.getUserTypeId(), userPendingActivityDTO));
    }

    @PostMapping("check-if-schedule-is-in-activity-process")
    public ResponseEntity<?> checkIfScheduleIsInActivityProcess(@PathParam("scheduleId") String scheduleId) {
        return ResponseEntity.ok().body(activitySampleController.checkIfScheduleIsInActivityProcess(scheduleId));
    }
}