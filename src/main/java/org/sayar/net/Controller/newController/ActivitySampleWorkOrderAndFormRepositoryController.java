package org.sayar.net.Controller.newController;

import com.mongodb.BasicDBObject;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Controller.ActivitySampleController;
import org.sayar.net.Controller.UploadAndDownloadFile;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.CompletionDetail;
import org.sayar.net.Model.DTO.FormAndFormDataDTO;
import org.sayar.net.Model.DTO.WorkOrderCreateDTO;
import org.sayar.net.Model.Mongo.poll.controller.form.FormController;
import org.sayar.net.Model.Mongo.poll.controller.form.FormDataController;
import org.sayar.net.Model.Mongo.poll.model.form.FormData;
import org.sayar.net.Model.NewForm;
import org.sayar.net.Model.newModel.ActivitySampleWorkOrderAndFormRepository;
import org.sayar.net.Model.newModel.BasicInformation;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.MiscCost;
import org.sayar.net.Model.newModel.Notify;
import org.sayar.net.Model.newModel.Task.controller.TaskController;
import org.sayar.net.Model.newModel.Task.model.Task;
import org.sayar.net.Model.newModel.Task.service.TaskService;
import org.sayar.net.Model.newModel.WorkOrder.PartWithUsageCount;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Service.newService.*;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("work-order-repository")
public class ActivitySampleWorkOrderAndFormRepositoryController {
    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private WorkOrderPartService workOrderPartService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private MiscCostService miscCostService;

    @Autowired
    private UploadAndDownloadFile uploadAndDownloadFile;

    @Autowired
    private TaskController taskController;

    @Autowired
    private WorkOrderPartController workOrderPartController;

    @Autowired
    private MiscCostController miscCostController;

    @Autowired
    private NotifyController notifyController;

    @Autowired
    private DocumentController documentController;

    @Autowired
    private ActivitySampleController activitySampleController;

    @Autowired
    private FormController formController;

    @Autowired
    private TaskGroupService taskGroupService;

    @Autowired
    private FormDataController formDataController;

    @Autowired
    private NewFormController newFormController;

    @Autowired
    private AssetService assetService;

    @PostMapping("save")
    public ResponseEntity<?> saveActivitySampleWorkOrderAndFormRepository(@RequestBody ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository) {
        if (activitySampleWorkOrderAndFormRepository.getActivityInstanceId() == null || activitySampleWorkOrderAndFormRepository.getActivityLevelId() == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("ورودی ارسال شده از سمت کلاینت ناقص است", httpStatus);
        } else {
            ActivitySampleWorkOrderAndFormRepository savedActivitySampleWorkOrderAndFormRepository = mongoOperations.save(activitySampleWorkOrderAndFormRepository);
            String ActivitySampleWorkOrderAndFormRepository = savedActivitySampleWorkOrderAndFormRepository.getId();
            return ResponseEntity.ok().body(ActivitySampleWorkOrderAndFormRepository);
        }
    }

    @GetMapping("get-work-order-and-form")
    public ResponseEntity<?> getActivitySampleWorkOrderAndFormRepositoryOnConsideredActivityLeve(@PathParam("activityInstanceId") String activityInstanceId,
                                                                                                 @PathParam("activityLevelId") String activityLevelId,
                                                                                                 @PathParam("numberOfParticipation") int numberOfParticipation) {

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = mongoOperations.findOne(query, ActivitySampleWorkOrderAndFormRepository.class);

        if (activitySampleWorkOrderAndFormRepository != null) {
            if (activitySampleWorkOrderAndFormRepository.getWorkOrderCreateDTO().getAssetId() != null) {
                Asset asset = assetService.getAssetName(activitySampleWorkOrderAndFormRepository.getWorkOrderCreateDTO().getAssetId());
                activitySampleWorkOrderAndFormRepository.getWorkOrderCreateDTO().setAssetName(asset.getName());
            }

            if (activitySampleWorkOrderAndFormRepository.getForm() == null) {
                NewForm form = activitySampleController.getFormIdOfTheActivitySample(activityInstanceId, activityLevelId);
                activitySampleWorkOrderAndFormRepository.setForm(form);
                return ResponseEntity.ok().body(activitySampleWorkOrderAndFormRepository);
            } else {
                return ResponseEntity.ok().body(activitySampleWorkOrderAndFormRepository);
            }
        } else {
            return ResponseEntity.ok().body(new ActivitySampleWorkOrderAndFormRepository());
        }

    }

    @PutMapping("update")
    public ResponseEntity<?> updateActivitySampleWorkOrderAndFormRepositoryController(@RequestBody ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(activitySampleWorkOrderAndFormRepository.getId()));
        Update update = new Update();
        update.set("activityLevelId", activitySampleWorkOrderAndFormRepository.getActivityLevelId());
        update.set("activityInstanceId", activitySampleWorkOrderAndFormRepository.getActivityInstanceId());
        update.set("workOrderCreateDTO", activitySampleWorkOrderAndFormRepository.getWorkOrderCreateDTO());
        update.set("form", activitySampleWorkOrderAndFormRepository.getForm());
        update.set("numberOfParticipation", activitySampleWorkOrderAndFormRepository.getNumberOfParticipation());
        update.set("formatData", activitySampleWorkOrderAndFormRepository.getFormData());
        update.set("notifyList", activitySampleWorkOrderAndFormRepository.getNotifyList());
        update.set("taskList", activitySampleWorkOrderAndFormRepository.getTaskList());
        update.set("taskGroupList", activitySampleWorkOrderAndFormRepository.getTaskGroupList());
        update.set("partWithUsageCountList", activitySampleWorkOrderAndFormRepository.getPartWithUsageCountList());
        update.set("miscCostList", activitySampleWorkOrderAndFormRepository.getMiscCostList());
        update.set("completionDetail", activitySampleWorkOrderAndFormRepository.getCompletionDetail());
        update.set("workOrderBasicInformation", activitySampleWorkOrderAndFormRepository.getWorkOrderBasicInformation());
        if (activitySampleWorkOrderAndFormRepository.getDocumentList() != null)
            update.set("documentList", activitySampleWorkOrderAndFormRepository.getDocumentList());
        mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("is-there-work-order-and-form")
    public ResponseEntity<?> isThereWorkOrderAndForm(@PathParam("activityInstanceId") String activityInstanceId,
                                                     @PathParam("activityLevelId") String activityLevelId,
                                                     @PathParam("numberOfParticipation") int numberOfParticipation) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        return ResponseEntity.ok().body(mongoOperations.exists(query, ActivitySampleWorkOrderAndFormRepository.class));
    }

    @PostMapping("save-createDto")
    public ResponseEntity<?> saveCreateDto(@PathParam("activityLeveId") String activityLevelId,
                                           @PathParam("activityInstanceId") String activityInstanceId,
                                           @PathParam("numberOfParticipation") int numberOfParticipation,
                                           @PathParam("workOrderID") String workOrderId,
                                           @RequestBody WorkOrderCreateDTO workOrderCreateDTO) {
        workOrderService.updateCreateWorkOrder(workOrderCreateDTO, workOrderId);
        ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = new ActivitySampleWorkOrderAndFormRepository();
        activitySampleWorkOrderAndFormRepository.setActivityLevelId(activityLevelId);
        activitySampleWorkOrderAndFormRepository.setActivityInstanceId(activityInstanceId);
        activitySampleWorkOrderAndFormRepository.setNumberOfParticipation(numberOfParticipation);
        activitySampleWorkOrderAndFormRepository.setWorkOrderCreateDTO(workOrderCreateDTO);
        return ResponseEntity.ok().body(mongoOperations.save(activitySampleWorkOrderAndFormRepository));
    }

    @PutMapping("update-createDTO")
    public ResponseEntity<?> updateCreateDTO(@PathParam("activityLevelId") String activityLevelId,
                                             @PathParam("activityInstanceId") String activityInstanceId,
                                             @PathParam("numberOfParticipation") int numberOfParticipation,
                                             @PathParam("workOrderId") String workOrderId,
                                             @RequestBody WorkOrderCreateDTO workOrderCreateDTO) {
        workOrderService.updateCreateWorkOrder(workOrderCreateDTO, workOrderId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        Update update = new Update();
        update.set("workOrderCreateDTO", workOrderCreateDTO);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("save-basic-information")
    public ResponseEntity<?> saveBasicInformation(@PathParam("activityLevelId") String activityLevelId,
                                                  @PathParam("activityInstanceId") String activityInstanceId,
                                                  @PathParam("numberOfParticipation") int numberOfParticipation,
                                                  @PathParam("workOrderId") String workOrderId,
                                                  @RequestBody BasicInformation basicInformation) {
        workOrderService.updateBasicInformationByWorkOrderId(workOrderId, basicInformation);
        ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = new ActivitySampleWorkOrderAndFormRepository();
        activitySampleWorkOrderAndFormRepository.setActivityLevelId(activityLevelId);
        activitySampleWorkOrderAndFormRepository.setActivityInstanceId(activityInstanceId);
        activitySampleWorkOrderAndFormRepository.setNumberOfParticipation(numberOfParticipation);
        activitySampleWorkOrderAndFormRepository.setWorkOrderBasicInformation(basicInformation);
        return ResponseEntity.ok().body(mongoOperations.save(activitySampleWorkOrderAndFormRepository));
    }

    @PutMapping("update-basic-information")
    public ResponseEntity<?> updateBasicInformation(@PathParam("activityLevelId") String activityLevelId,
                                                    @PathParam("activityInstanceId") String activityInstanceId,
                                                    @PathParam("numberOfParticipation") int numberOfParticipation,
                                                    @PathParam("workOrderId") String workOrderId,
                                                    @RequestBody BasicInformation basicInformation) {
        workOrderService.updateBasicInformationByWorkOrderId(workOrderId, basicInformation);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        Update update = new Update();
        update.set("workOrderBasicInformation", basicInformation);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("save-completion-detail")
    public ResponseEntity<?> saveCompletionDetail(@PathParam("activityLevelId") String activityLevelId,
                                                  @PathParam("activityInstanceId") String activityInstanceId,
                                                  @PathParam("numberOfParticipation") int numberOfParticipation,
                                                  @PathParam("workOrderId") String workOrderId,
                                                  @RequestBody CompletionDetail completionDetail) {
        workOrderService.updateCompletionByWorkOrderId(workOrderId, completionDetail);
        ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = new ActivitySampleWorkOrderAndFormRepository();
        activitySampleWorkOrderAndFormRepository.setActivityLevelId(activityLevelId);
        activitySampleWorkOrderAndFormRepository.setActivityInstanceId(activityInstanceId);
        activitySampleWorkOrderAndFormRepository.setNumberOfParticipation(numberOfParticipation);
        activitySampleWorkOrderAndFormRepository.setCompletionDetail(completionDetail);
        return ResponseEntity.ok().body(mongoOperations.save(activitySampleWorkOrderAndFormRepository));
    }

    @PutMapping("update-completion-detail")
    public ResponseEntity<?> updateCompletionDetail(@PathParam("activityLevelId") String activityLevelId,
                                                    @PathParam("activityInstanceId") String activityInstanceId,
                                                    @PathParam("numberOfParticipation") int numberOfParticipation,
                                                    @PathParam("workOrderId") String workOrderId,
                                                    @RequestBody CompletionDetail completionDetail) {
        workOrderService.updateCompletionByWorkOrderId(workOrderId, completionDetail);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        Update update = new Update();
        update.set("completionDetail", completionDetail);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("save-form-and-form-data")
    public ResponseEntity<?> saveFormAndFormData(@PathParam("activityLevelId") String activityLevelId,
                                                 @PathParam("activityInstanceId") String activityInstanceId,
                                                 @PathParam("numberOfParticipation") int numberOfParticipation,
                                                 @RequestBody FormAndFormDataDTO formAndFormDataDTO) {
        ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = new ActivitySampleWorkOrderAndFormRepository();
        activitySampleWorkOrderAndFormRepository.setActivityLevelId(activityLevelId);
        activitySampleWorkOrderAndFormRepository.setActivityInstanceId(activityInstanceId);
        activitySampleWorkOrderAndFormRepository.setNumberOfParticipation(numberOfParticipation);
        activitySampleWorkOrderAndFormRepository.setForm(formAndFormDataDTO.getForm());
        activitySampleWorkOrderAndFormRepository.setFormData(formAndFormDataDTO.getFormData());
        return ResponseEntity.ok().body(mongoOperations.save(activitySampleWorkOrderAndFormRepository));
    }

    @PutMapping("update-form-and-form-data")
    public ResponseEntity<?> updateFormAndFormData(@PathParam("activityLevelId") String activityLevelId,
                                                   @PathParam("activityInstanceId") String activityInstanceId,
                                                   @PathParam("numberOfParticipation") int numberOfParticipation,
                                                   @RequestBody FormAndFormDataDTO formAndFormDataDTO) {

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        Update update = new Update();
        update.set("form", formAndFormDataDTO.getForm());
        update.set("formData", formAndFormDataDTO.getFormData());
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("save-task-group-list")
    public ResponseEntity<?> saveTaskGroupList(@PathParam("activityLevelId") String activityLevelId,
                                               @PathParam("activityInstanceId") String activityInstanceId,
                                               @PathParam("numberOfParticipation") int numberOfParticipation,
                                               @PathParam("workOrderId") String workOrderId,
                                               @RequestBody List<String> taskGroupList) {
        workOrderService.updateTaskGroupListByWorkOrderId(taskGroupList, workOrderId);
        ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = new ActivitySampleWorkOrderAndFormRepository();
        activitySampleWorkOrderAndFormRepository.setActivityLevelId(activityLevelId);
        activitySampleWorkOrderAndFormRepository.setActivityInstanceId(activityInstanceId);
        activitySampleWorkOrderAndFormRepository.setNumberOfParticipation(numberOfParticipation);
        activitySampleWorkOrderAndFormRepository.setTaskGroupList(taskGroupList);
        return ResponseEntity.ok().body(mongoOperations.save(activitySampleWorkOrderAndFormRepository));
    }

    @PutMapping("update-task-group-list")
    public ResponseEntity<?> updateTaskGroupList(@PathParam("activityLevelId") String activityLevelId,
                                                 @PathParam("activityInstanceId") String activityInstanceId,
                                                 @PathParam("numberOfParticipation") int numberOfParticipation,
                                                 @PathParam("workOrderId") String workOrderId,
                                                 @RequestBody List<String> taskGroupList) {
        workOrderService.updateTaskGroupListByWorkOrderId(taskGroupList, workOrderId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        Update update = new Update();
        update.set("taskGroupList", taskGroupList);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("save-part-with-usage-count-in-first-time")
    public ResponseEntity<?> savePartWithUsageCountInFirstTime(@PathParam("activityLevelId") String activityLevelId,
                                                               @PathParam("activityInstanceId") String activityInstanceId,
                                                               @PathParam("numberOfParticipation") int numberOfParticipation,
                                                               @PathParam("workOrderId") String workOrderId,
                                                               @RequestBody PartWithUsageCount partWithUsageCount) {
        partWithUsageCount.setReferenceId(workOrderId);
        workOrderService.addUsedPartToWorkOrder(workOrderId, partWithUsageCount.getPartId());
        PartWithUsageCount savedPartWithUsageCount = workOrderPartService.postWorkOrderPart(partWithUsageCount);
        List<PartWithUsageCount> partWithUsageCounts = new ArrayList<>();
        partWithUsageCount.setId(savedPartWithUsageCount.getId());
        partWithUsageCounts.add(partWithUsageCount);
        ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = new ActivitySampleWorkOrderAndFormRepository();
        activitySampleWorkOrderAndFormRepository.setActivityLevelId(activityLevelId);
        activitySampleWorkOrderAndFormRepository.setActivityInstanceId(activityInstanceId);
        activitySampleWorkOrderAndFormRepository.setNumberOfParticipation(numberOfParticipation);
        activitySampleWorkOrderAndFormRepository.setPartWithUsageCountList(partWithUsageCounts);
        return ResponseEntity.ok().body(mongoOperations.save(activitySampleWorkOrderAndFormRepository).getId());
    }

    @PutMapping("save-part-with-usage-count-after-first-time")
    public ResponseEntity<?> savePartWithUsageCountAfterFirstTime(@PathParam("activityLevelId") String activityLevelId,
                                                                  @PathParam("activityInstanceId") String activityInstanceId,
                                                                  @PathParam("numberOfParticipation") int numberOfParticipation,
                                                                  @PathParam("workOrderId") String workOrderId,
                                                                  @RequestBody PartWithUsageCount partWithUsageCount) {

        partWithUsageCount.setReferenceId(workOrderId);
        PartWithUsageCount savedPartWithUsageCount = workOrderPartService.postWorkOrderPartOfNoticeBoard(partWithUsageCount);
        workOrderService.addUsedPartToWorkOrder(workOrderId, savedPartWithUsageCount.getId());
        partWithUsageCount.setId(savedPartWithUsageCount.getId());
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        Update update = new Update();
        update.push("partWithUsageCountList", partWithUsageCount);
        mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
//        if (updateResult.getModifiedCount() > 0) {
//            return ResponseEntity.ok().body(true);
//        } else {
//            return ResponseEntity.ok().body(false);
//        }
        return ResponseEntity.ok().body(savedPartWithUsageCount.getId());
    }

    @PutMapping("update-part-with-usage-count")
    public ResponseEntity<?> updatePartWithUsageCount(@PathParam("activityLevelId") String activityLevelId,
                                                      @PathParam("activityInstanceId") String activityInstanceId,
                                                      @PathParam("numberOfParticipation") int numberOfParticipation,
                                                      @PathParam("workOrderId") String workOrderId,
                                                      @RequestBody PartWithUsageCount partWithUsageCount) {
        partWithUsageCount.setReferenceId(workOrderId);
        workOrderPartService.updatePArtWithUsageCount(partWithUsageCount.getId(), partWithUsageCount);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        query.addCriteria(Criteria.where("partWithUsageCountList").elemMatch(Criteria.where("id").is(partWithUsageCount.getId())));
        Update update = new Update();
        update.set("partWithUsageCountList.$.planedQuantity", partWithUsageCount.getPlanedQuantity());
        update.set("partWithUsageCountList.$.actualQuantity", partWithUsageCount.getActualQuantity());
        update.set("partWithUsageCountList.$.partId", partWithUsageCount.getPartId());
        update.set("partWithUsageCountList.$.partName", partWithUsageCount.getPartName());
        update.set("partWithUsageCountList.$.referenceId", partWithUsageCount.getReferenceId());
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }


    @PostMapping("save-task-list-in-first-time")
    public ResponseEntity<?> saveTaskListInFirstTime(@PathParam("activityLevelId") String activityLevelId,
                                                     @PathParam("activityInstanceId") String activityInstanceId,
                                                     @PathParam("numberOfParticipation") int numberOfParticipation,
                                                     @PathParam("workOrderId") String workOrderId,
                                                     @RequestBody Task task) {
        task.setReferenceId(workOrderId);
//        Task savedTask = taskService.taskService(task);
        Task savedTask = workOrderService.saveTaskOfWorkOrder(task);
        task.setId(savedTask.getId());
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = new ActivitySampleWorkOrderAndFormRepository();
        activitySampleWorkOrderAndFormRepository.setActivityLevelId(activityLevelId);
        activitySampleWorkOrderAndFormRepository.setActivityInstanceId(activityInstanceId);
        activitySampleWorkOrderAndFormRepository.setNumberOfParticipation(numberOfParticipation);
        activitySampleWorkOrderAndFormRepository.setTaskList(taskList);
        mongoOperations.save(activitySampleWorkOrderAndFormRepository);
        return ResponseEntity.ok().body(savedTask.getId());
    }

    @PutMapping("save-task-after-first-time")
    public ResponseEntity<?> saveTaskAfterFirstTime(@PathParam("activityLevelId") String activityLevelId,
                                                    @PathParam("activityInstanceId") String activityInstanceId,
                                                    @PathParam("numberOfParticipation") int numberOfParticipation,
                                                    @PathParam("workOrderId") String workOrderId,
                                                    @RequestBody Task task) {
        task.setReferenceId(workOrderId);
//        Task savedTask = taskService.taskService(task);
        Task savedTask = workOrderService.saveTaskOfWorkOrder(task);
        task.setId(savedTask.getId());
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        Update update = new Update();
        update.push("taskList", task);
//        FindAndModifyOptions options = FindAndModifyOptions.options();
//        options.returnNew(true);
        mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        return ResponseEntity.ok().body(savedTask.getId());
    }

    @PutMapping("update-task")
    public ResponseEntity<?> updateTask(@PathParam("activityLevelId") String activityLevelId,
                                        @PathParam("activityInstanceId") String activityInstanceId,
                                        @PathParam("numberOfParticipation") int numberOfParticipation,
                                        @PathParam("workOrderId") String workOrderId,
                                        @RequestBody Task task) {
        task.setReferenceId(workOrderId);
        taskService.updateTask(task);
        Query query = new Query();
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        query.addCriteria(Criteria.where("taskList").elemMatch(Criteria.where("_id").is(task.getId())));
        Update update = new Update();
        update.set("taskList.$.code", task.getCode());
        update.set("taskList.$.title", task.getTitle());
        update.set("taskList.$.price", task.getPrice());
        update.set("taskList.$.description", task.getDescription());
        update.set("taskList.$.taskType", task.getTaskType());
        update.set("taskList.$.users", task.getUsers());
        update.set("taskList.$.referenceId", task.getReferenceId());
        update.set("taskList.$.taskGroupId", task.getTaskGroupId());
        update.set("taskList.$.timeEstimate", task.getTimeEstimate());
        update.set("taskList.$.status", task.getStatus());
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("save-misc-cost-in-first-time")
    public ResponseEntity<?> saveTaskListInFirstTime(@PathParam("activityLevelId") String activityLevelId,
                                                     @PathParam("activityInstanceId") String activityInstanceId,
                                                     @PathParam("numberOfParticipation") int numberOfParticipation,
                                                     @PathParam("workOrderId") String workOrderId,
                                                     @RequestBody MiscCost miscCost) {
        miscCost.setReferenceId(workOrderId);
        MiscCost savedMiscCost = miscCostService.postMiscCostController(miscCost);
        List<MiscCost> miscCostList = new ArrayList<>();
        miscCost.setId(savedMiscCost.getId());
        miscCostList.add(miscCost);

        ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = new ActivitySampleWorkOrderAndFormRepository();
        activitySampleWorkOrderAndFormRepository.setActivityLevelId(activityLevelId);
        activitySampleWorkOrderAndFormRepository.setActivityInstanceId(activityInstanceId);
        activitySampleWorkOrderAndFormRepository.setNumberOfParticipation(numberOfParticipation);
        activitySampleWorkOrderAndFormRepository.setMiscCostList(miscCostList);
        return ResponseEntity.ok().body(mongoOperations.save(activitySampleWorkOrderAndFormRepository));
    }

    @PutMapping("save-misc-cost-after-first-time")
    public ResponseEntity<?> saveTaskAfterFirstTime(@PathParam("activityLevelId") String activityLevelId,
                                                    @PathParam("activityInstanceId") String activityInstanceId,
                                                    @PathParam("numberOfParticipation") int numberOfParticipation,
                                                    @PathParam("workOrderId") String workOrderId,
                                                    @RequestBody MiscCost miscCost) {
        miscCost.setReferenceId(workOrderId);
        MiscCost savedMiscCost = miscCostService.postMiscCostController(miscCost);
        miscCost.setId(savedMiscCost.getId());
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        Update update = new Update();
        update.push("miscCostList", miscCost);
        mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        return ResponseEntity.ok().body(miscCost.getId());
//        if (updateResult.getModifiedCount() > 0) {
//            return ResponseEntity.ok().body(true);
//        } else {
//            return ResponseEntity.ok().body(false);
//        }
    }

    @PutMapping("update-misc-cost")
    public ResponseEntity<?> updateTask(@PathParam("activityLevelId") String activityLevelId,
                                        @PathParam("activityInstanceId") String activityInstanceId,
                                        @PathParam("numberOfParticipation") int numberOfParticipation,
                                        @PathParam("workOrderId") String workOrderId,
                                        @RequestBody MiscCost miscCost) {
        miscCost.setReferenceId(workOrderId);
        miscCostService.updateMiscCost(miscCost);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        query.addCriteria(Criteria.where("miscCostList").elemMatch(Criteria.where("id").is(miscCost.getId())));
        Update update = new Update();
        update.set("miscCostList.$.title", miscCost.getTitle());
        update.set("miscCostList.$.estimatedQuantity", miscCost.getEstimatedQuantity());
        update.set("miscCostList.$.estimatedUnitCost", miscCost.getEstimatedUnitCost());
        update.set("miscCostList.$.estimatedTotalCost", miscCost.getEstimatedTotalCost());
        update.set("miscCostList.$.miscCostType", miscCost.getMiscCostType());
        update.set("miscCostList.$.quantity", miscCost.getQuantity());
        update.set("miscCostList.$.actualUnitCost", miscCost.getActualUnitCost());
        update.set("miscCostList.$.actualTotalCost", miscCost.getActualTotalCost());
        update.set("miscCostList.$.description", miscCost.getDescription());
        update.set("miscCostList.$.referenceId", miscCost.getReferenceId());
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("save-notify-in-first-time")
    public ResponseEntity<?> saveNotifyInFirstTime(@PathParam("activityLevelId") String activityLevelId,
                                                   @PathParam("activityInstanceId") String activityInstanceId,
                                                   @PathParam("numberOfParticipation") int numberOfParticipation,
                                                   @PathParam("workOrderID") String workOrderId,
                                                   @RequestBody Notify notify) {
        notify.setReferenceId(workOrderId);
        Notify savedNotify = notifyService.postNotify(notify);
        List<Notify> notifyList = new ArrayList<>();
        notify.setId(savedNotify.getId());
        notifyList.add(notify);
        ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = new ActivitySampleWorkOrderAndFormRepository();
        activitySampleWorkOrderAndFormRepository.setActivityLevelId(activityLevelId);
        activitySampleWorkOrderAndFormRepository.setActivityInstanceId(activityInstanceId);
        activitySampleWorkOrderAndFormRepository.setNumberOfParticipation(numberOfParticipation);
        activitySampleWorkOrderAndFormRepository.setNotifyList(notifyList);
        return ResponseEntity.ok().body(mongoOperations.save(activitySampleWorkOrderAndFormRepository));
    }

    @PutMapping("save-notify-after-first-time")
    public ResponseEntity<?> saveNotifyAfterFirstTime(@PathParam("activityLevelId") String activityLevelId,
                                                      @PathParam("activityInstanceId") String activityInstanceId,
                                                      @PathParam("numberOfParticipation") int numberOfParticipation,
                                                      @PathParam("workOrderId") String workOrderId,
                                                      @RequestBody Notify notify) {
        notify.setReferenceId(workOrderId);
        Notify savedNotify = notifyService.postNotify(notify);
        notify.setId(savedNotify.getId());
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        Update update = new Update();
        update.push("notifyList", notify);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @PutMapping("update-notify")
    public ResponseEntity<?> updateNotify(@PathParam("activityLevelId") String activityLevelId,
                                          @PathParam("activityInstanceId") String activityInstanceId,
                                          @PathParam("numberOfParticipation") int numberOfParticipation,
                                          @PathParam("workOrderId") String workOrderId,
                                          @RequestBody Notify notify) {
        notify.setReferenceId(workOrderId);
        notifyService.updateNotify(notify, notify.getId());
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        query.addCriteria(Criteria.where("notifyList").elemMatch(Criteria.where("id").is(notify.getId())));
        Update update = new Update();
        update.set("notifyList.$.title", notify.getUser());
        update.set("notifyList.$.userId", notify.getUserId());
        update.set("notifyList.$.events", notify.getEvents());
        update.set("notifyList.$.referenceId", notify.getReferenceId());
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @PostMapping("upload-document-file-in-first-time")
    public ResponseEntity<?> uploadDocumentFileInFirstTime(@PathParam("activityLevelId") String activityLevelId,
                                                           @PathParam("activityInstanceId") String activityInstanceId,
                                                           @PathParam("numberOfParticipation") int numberOfParticipation,
                                                           @PathParam("showName") String showName,
                                                           @PathParam("workOrderId") String workOrderId,
                                                           @RequestParam("file") MultipartFile multipartFile) throws IOException {
        DocumentFile documentFile = uploadAndDownloadFile.upload(multipartFile, workOrderId, showName);
        workOrderService.addDocumentToWorkOrder(workOrderId, documentFile.getId());
        List<DocumentFile> documentFileList = new ArrayList<>();
        DocumentFile document = new DocumentFile();
//        document.setId(documentFile.getId());
//        document.setFileByte(multipartFile.getBytes());
//        document.setFileName(multipartFile.getOriginalFilename());
//        document.setFileContentType(multipartFile.getContentType());
//        document.setShowName(showName);
        documentFileList.add(document);

        ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = new ActivitySampleWorkOrderAndFormRepository();
        activitySampleWorkOrderAndFormRepository.setActivityLevelId(activityLevelId);
        activitySampleWorkOrderAndFormRepository.setActivityInstanceId(activityInstanceId);
        activitySampleWorkOrderAndFormRepository.setNumberOfParticipation(numberOfParticipation);
        activitySampleWorkOrderAndFormRepository.setDocumentList(documentFileList);
        mongoOperations.save(activitySampleWorkOrderAndFormRepository);
        return ResponseEntity.ok().body(documentFile);
    }

    @PostMapping("upload-document-file-after-first-time")
    public ResponseEntity<?> uploadDocumentFileAfterFirstTime(@PathParam("activityLevelId") String activityLevelId,
                                                              @PathParam("activityInstanceId") String activityInstanceId,
                                                              @PathParam("numberOfParticipation") int numberOfParticipation,
                                                              @PathParam("showName") String showName,
                                                              @PathParam("workOrderId") String workOrderId,
                                                              @RequestParam("file") MultipartFile multipartFile) throws IOException {
        DocumentFile documentFile = uploadAndDownloadFile.upload(multipartFile, workOrderId, showName);
        workOrderService.addDocumentToWorkOrder(workOrderId, documentFile.getId());
//        DocumentFile document = new DocumentFile();
//        document.setId(documentFile.getId());
//        document.setFileByte(multipartFile.getBytes());
//        document.setFileName(multipartFile.getOriginalFilename());
//        document.setFileContentType(multipartFile.getContentType());
//        document.setShowName(showName);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        Update update = new Update();
        update.push("documentList", documentFile);
        mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
//        if (updateResult.getModifiedCount() > 0) {
//            return ResponseEntity.ok().body(true);
//        } else {
//            return ResponseEntity.ok().body(false);
//        }
        return ResponseEntity.ok().body(documentFile);
    }

    @PutMapping("delete-task-group-list")
    public ResponseEntity<?> deleteTaskGroupList(@PathParam("activityLevelId") String activityLevelId,
                                                 @PathParam("activityInstanceId") String activityInstanceId,
                                                 @PathParam("numberOfParticipation") int numberOfParticipation,
                                                 @PathParam("taskGroupId") String taskGroupId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        query.addCriteria(Criteria.where("taskGroupList").is(taskGroupId));
        Update update = new Update();
        update.pull("taskGroupList", taskGroupId);
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @DeleteMapping("delete-task")
    public ResponseEntity<?> deleteTask(@PathParam("activityLevelId") String activityLevelId,
                                        @PathParam("activityInstanceId") String activityInstanceId,
                                        @PathParam("numberOfParticipation") int numberOfParticipation,
                                        @PathParam("taskId") String taskId,
                                        @PathParam("workOrderId") String workOrderId,
                                        @PathParam("forSchedule") boolean forSchedule) {
        if (!forSchedule)
            taskController.deleteTask(taskId);
        workOrderService.deleteTaskFromWorkOrder(workOrderId, taskId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        query.addCriteria(Criteria.where("taskList").elemMatch(Criteria.where("id").is(taskId)));
        Update update = new Update();
        update.pull("taskList", new BasicDBObject("_id", taskId));
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @DeleteMapping("delete-part-with-usage-count")
    public ResponseEntity<?> deletePartWithUsageCount(@PathParam("activityLevelId") String activityLevelId,
                                                      @PathParam("activityInstanceId") String activityInstanceId,
                                                      @PathParam("numberOfParticipation") int numberOfParticipation,
                                                      @PathParam("partWithUsageCountId") String partWithUsageCountId,
                                                      @PathParam("forSchedule") boolean forSchedule,
                                                      @PathParam("workOrderId") String workOrderId) {
        workOrderService.deleteUsedPartFromWorkOrder(workOrderId, partWithUsageCountId);
        if (!forSchedule)
            workOrderPartController.deletePartWithUsageCountById(partWithUsageCountId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        query.addCriteria(Criteria.where("partWithUsageCountList").elemMatch(Criteria.where("id").is(partWithUsageCountId)));
        Update update = new Update();
        update.pull("partWithUsageCountList", new BasicDBObject("_id", partWithUsageCountId));
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @DeleteMapping("delete-misc-cost")
    public ResponseEntity<?> deleteMiscCost(@PathParam("activityLevelId") String activityLevelId,
                                            @PathParam("activityInstanceId") String activityInstanceId,
                                            @PathParam("numberOfParticipation") int numberOfParticipation,
                                            @PathParam("miscCostId") String miscCostId) {
        miscCostController.delete(miscCostId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        query.addCriteria(Criteria.where("miscCostList").elemMatch(Criteria.where("id").is(miscCostId)));
        Update update = new Update();
        update.pull("miscCostList", new BasicDBObject("_id", miscCostId));
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @DeleteMapping("delete-notify")
    public ResponseEntity<?> deleteNotify(@PathParam("activityLevelId") String activityLevelId,
                                          @PathParam("activityInstanceId") String activityInstanceId,
                                          @PathParam("numberOfParticipation") int numberOfParticipation,
                                          @PathParam("notifyId") String notifyId) {
        notifyController.delete(notifyId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        query.addCriteria(Criteria.where("notifyList").elemMatch(Criteria.where("id").is(notifyId)));
        Update update = new Update();
        update.pull("notifyList", new BasicDBObject("_id", notifyId));
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @DeleteMapping("delete-document-file")
    public ResponseEntity<?> deleteDocumentFileId(@PathParam("activityLevelId") String activityLevelId,
                                                  @PathParam("activityInstanceId") String activityInstanceId,
                                                  @PathParam("numberOfParticipation") int numberOfParticipation,
                                                  @PathParam("documentFileId") String documentFileId,
                                                  @PathParam("workOrderId") String workOrderId,
                                                  @PathParam("forSchedule") boolean forSchedule) {
        if (!forSchedule)
            documentController.deleteDocument(documentFileId);
        workOrderService.deleteDocumentFromWorkOrder(documentFileId, workOrderId);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        query.addCriteria(Criteria.where("documentList").elemMatch(Criteria.where("id").is(documentFileId)));
        Update update = new Update();
        update.pull("documentList", new BasicDBObject("_id", documentFileId));
        UpdateResult updateResult = mongoOperations.updateFirst(query, update, ActivitySampleWorkOrderAndFormRepository.class);
//        return ResponseEntity.ok().body(updateResult.getModifiedCount() > 0);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok().body(true);
        } else {
            return ResponseEntity.ok().body(false);
        }
    }

    @GetMapping("is-there-repository")
    public ResponseEntity<?> isThereRepository(@PathParam("activityLevelId") String activityLevelId,
                                               @PathParam("activityInstanceId") String activityInstanceId,
                                               @PathParam("numberOfParticipation") int numberOfParticipation,
                                               @PathParam("workOrderId") String workOrderId,
                                               @PathParam("formId") String formId,
                                               @PathParam("formDataId") String formDataId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("activityInstanceId").is(activityInstanceId));
        query.addCriteria(Criteria.where("numberOfParticipation").is(numberOfParticipation));
        query.addCriteria(Criteria.where("activityLevelId").is(activityLevelId));
        boolean exist = mongoOperations.exists(query, ActivitySampleWorkOrderAndFormRepository.class);
        if (!exist) {

            ActivitySampleWorkOrderAndFormRepository activitySampleWorkOrderAndFormRepository = new ActivitySampleWorkOrderAndFormRepository();
            WorkOrder workOrder = workOrderService.getWorkOrderForRepository(workOrderId);
            WorkOrderCreateDTO workOrderCreateDTO = new WorkOrderCreateDTO();
            workOrderCreateDTO.setAssetId(workOrder.getAssetId());

            if (workOrder.getCode() != null) {
                workOrderCreateDTO.setCode(workOrder.getCode());
            }

            if (workOrder.getCreationDate() != null)
                workOrderCreateDTO.setCreationDate(workOrder.getCreationDate());

            if (workOrder.getStartDate() != null)
                workOrderCreateDTO.setStartDate(workOrder.getStartDate());

            if (workOrder.getEndDate() != null)
                workOrderCreateDTO.setEndDate(workOrder.getEndDate());

            if (workOrder.getImage() != null)
                workOrderCreateDTO.setImage(workOrder.getImage());

            if (workOrder.getStatusId() != null)
                workOrderCreateDTO.setStatusId(workOrder.getStatusId());

            if (workOrder.getProjectId() != null)
                workOrderCreateDTO.setProjectId(workOrder.getProjectId());

            if (workOrder.getRequiredCompletionDate() != null)
                workOrderCreateDTO.setRequiredCompletionDate(workOrder.getRequiredCompletionDate());

            workOrderCreateDTO.setTitle(workOrder.getTitle());
            workOrderCreateDTO.setPriority(workOrder.getPriority());
            workOrderCreateDTO.setMaintenanceType(workOrder.getMaintenanceType());

            if (workOrder.getCompletionDetail() != null)
                activitySampleWorkOrderAndFormRepository.setCompletionDetail(workOrder.getCompletionDetail());

            if (workOrder.getBasicInformation() != null)
                activitySampleWorkOrderAndFormRepository.setWorkOrderBasicInformation(workOrder.getBasicInformation());

            activitySampleWorkOrderAndFormRepository.setWorkOrderCreateDTO(workOrderCreateDTO);
            activitySampleWorkOrderAndFormRepository.setActivityLevelId(activityLevelId);
            activitySampleWorkOrderAndFormRepository.setActivityInstanceId(activityInstanceId);
            activitySampleWorkOrderAndFormRepository.setNumberOfParticipation(numberOfParticipation);

            if (formId != null) {
                NewForm form = newFormController.getFormOfActivityByFormId(formId);
                activitySampleWorkOrderAndFormRepository.setForm(form);
            }

            if (formDataId != null) {
                FormData formData = formDataController.getFormDataOfRepository(formDataId);
                activitySampleWorkOrderAndFormRepository.setFormData(formData);
            }

            if (workOrder.getTasks() != null) {
                List<Task> taskList = taskService.getAllTasksByTaskList(workOrder.getTasks());
                activitySampleWorkOrderAndFormRepository.setTaskList(taskList);
            }

            if (workOrder.getTaskGroups() != null) {
//                List<TaskGroup> taskGroupList = taskGroupService.getAllTaskGroupByTaskGroupList(workOrder.getTaskGroups());
                activitySampleWorkOrderAndFormRepository.setTaskGroupList(workOrder.getTaskGroups());
            }

            if (workOrder.getUsedParts() != null) {
                List<PartWithUsageCount> partWithUsageCountList = workOrderPartService.getPartOfRepository(workOrder.getUsedParts());
                activitySampleWorkOrderAndFormRepository.setPartWithUsageCountList(partWithUsageCountList);
            }

            if (workOrder.getDocumentsList() != null) {
                List<DocumentFile> documentList = documentController.getDocumentsOfRepository(workOrder.getDocumentsList());
                activitySampleWorkOrderAndFormRepository.setDocumentList(documentList);
            }

            List<MiscCost> miscCostList = miscCostService.getMiscCostListByReferenceId(workOrderId);
            if (miscCostList != null) {
                activitySampleWorkOrderAndFormRepository.setMiscCostList(miscCostList);
            }

            mongoOperations.save(activitySampleWorkOrderAndFormRepository);
        }
        return ResponseEntity.ok().body(true);
    }
}
