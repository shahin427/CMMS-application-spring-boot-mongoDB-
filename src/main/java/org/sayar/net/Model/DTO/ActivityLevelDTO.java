package org.sayar.net.Model.DTO;

import lombok.Data;
import org.bson.types.ObjectId;
import org.sayar.net.Enumes.RequestStatus;
import org.sayar.net.Model.FormId;
import org.sayar.net.Model.Mongo.MyModel.ActivityLevel;
import org.sayar.net.Model.UserIdAndName;
import org.sayar.net.Model.newModel.Enum.CandidateMode;

import java.util.Date;
import java.util.List;

@Data
public class ActivityLevelDTO {
    private String id;
    private String title;
    private String recipe;
    private String orgId;
    private ObjectId formId;
    private Object form;
    private FormId formIdCopy;
    private ActivityLevel nextActivityLevel;
    private ActivityLevel prevActivityLevel;
    private Boolean firstStepIs;
    private Boolean lastStepIs;
    private Boolean showHistory;
    private Boolean canOperate;
    private String organizationName;
    private String formTitle;
    private List<UserIdAndName> userIdList;
    private String assignedUserId;
    private String assignedUserName;
    private String assignedUserFamily;
    private String userTypeId;
    private String userTypeTitle;
    private String organizationId;
    private boolean cancel;
    private String actionLevel;
    private String formIdBeta;
    private String formName;
    private String formDataId;
    private Date pendingDate;
    private Date answerDate;
    private List<String> staticFormsIdList;
    private String startNewActivity;
    //    private boolean rightToChoose;
    private boolean existRecipientOrderUser;    //repairman(technician)
    private String workOrderAccessId;
    private RequestStatus requestStatus;
    private String workOrderAndFormRepositoryId;
    private List<String> candidateUserIdList;
    private List<String> chosenCandidateUserIdList;
    private CandidateMode candidateMode;

    public ActivityLevelDTO(String id, List<String> candidateUserIdList, List<String> chosenCandidateUserIdList, CandidateMode candidateMode, String assignedUserId, String title, String recipe, String orgId, ObjectId formId, Object form, FormId formIdCopy, ActivityLevel nextActivityLevel, ActivityLevel prevActivityLevel, Boolean firstStepIs, Boolean lastStepIs, Boolean showHistory, Boolean canOperate, String organizationName, String formTitle, String userTypeId, String organizationId, boolean cancel, String actionLevel, String formIdBeta, String formName, String formDataId, Date pendingDate, Date answerDate, List<String> staticFormsIdList, String startNewActivity, boolean existRecipientOrderUser, String workOrderAccessId, RequestStatus requestStatus, String workOrderAndFormRepositoryId) {
        this.id = id;
        this.candidateUserIdList = candidateUserIdList;
        this.chosenCandidateUserIdList = chosenCandidateUserIdList;
        this.candidateMode = candidateMode;
        this.assignedUserId = assignedUserId;
        this.title = title;
        this.recipe = recipe;
        this.orgId = orgId;
        this.formId = formId;
        this.form = form;
        this.formIdCopy = formIdCopy;
        this.nextActivityLevel = nextActivityLevel;
        this.prevActivityLevel = prevActivityLevel;
        this.firstStepIs = firstStepIs;
        this.lastStepIs = lastStepIs;
        this.showHistory = showHistory;
        this.canOperate = canOperate;
        this.organizationName = organizationName;
        this.formTitle = formTitle;
        this.userTypeId = userTypeId;
        this.organizationId = organizationId;
        this.cancel = cancel;
        this.actionLevel = actionLevel;
        this.formIdBeta = formIdBeta;
        this.formName = formName;
        this.formDataId = formDataId;
        this.pendingDate = pendingDate;
        this.answerDate = answerDate;
        this.staticFormsIdList = staticFormsIdList;
        this.startNewActivity = startNewActivity;
//        this.rightToChoose = rightToChoose;
        this.existRecipientOrderUser = existRecipientOrderUser;
        this.workOrderAccessId = workOrderAccessId;
        this.requestStatus = requestStatus;
        this.workOrderAndFormRepositoryId = workOrderAndFormRepositoryId;
    }
}
