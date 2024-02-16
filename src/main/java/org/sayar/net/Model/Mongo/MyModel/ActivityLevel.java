package org.sayar.net.Model.Mongo.MyModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.sayar.net.Enumes.RequestStatus;
import org.sayar.net.Model.FormId;
import org.sayar.net.Model.newModel.Enum.CandidateMode;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by meghdad on 6/17/17.
 */

/**
 */

@Data
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ActivityLevel {

    @NotNull
    private String id;
    @NotNull
    @Size(min = 3, max = 100)
    private String title;     //عنوان مرحله
    private String recipe;    //دستورالعمل
    private List<String> staticFormsIdList;     //سایر فرم های دستورکار
    private ActivityLevel nextActivityLevel;    //مشخصات مرحله بعد این مرحله
    private ActivityLevel prevActivityLevel;    //مشخصات مرحله قبل این مرحله
    private CandidateMode candidateMode;        //مخاطب
    private String userTypeId;                  //مخاطب---->پست
    private List<String> candidateUserIdList = new ArrayList<>();       //مخاطب----> به برخی کاربران این پست ارسال شود (ایدی کاربران)
    private boolean workRequestAcceptor;        //کاربر تایید کننده درخواست

    //فیلدهای پایین در کارتابل مورد استفاده قرار میگیرد
    private String actionLevel = "waiting";    //آیا نوبت به این مرحله رسیده یا نه وقتی waiting هست یعنی نرسیده وقتی pending هست یعنی رسیده
    private Date pendingDate;           //زمان pending شدن
    private RequestStatus requestStatus;    //نوع درخواست
    private Date answerDate;      //تاریخ پاسخ
    private List<String> chosenCandidateUserIdList = new ArrayList<>();   //افراد انتخاب شده برای مرحله بعد

    //از این به پایین فعلا نیازی نیست
    private String orgId;
    private ObjectId formId;
    private Object form;
    private FormId formIdCopy;
    private Boolean firstStepIs;
    private Boolean lastStepIs;
    private Boolean showHistory;
    private Boolean canOperate;
    private String organizationName;
    private String formTitle;
    private String assignedUserId;
    private String organizationId;
    private boolean cancel;
    private String formIdBeta;
    private String formName;
    private String formDataId;
    private String startNewActivity;
    private boolean rightToChoose;
    private boolean existRecipientOrderUser;    //repairman(technician)
    private String workOrderAccessId;
    private String workOrderAndFormRepositoryId;

    public ActivityLevel(ActivityLevel nextActivityLevel, ActivityLevel prevActivityLevel) {
        this.nextActivityLevel = nextActivityLevel;
        this.prevActivityLevel = prevActivityLevel;
    }
}
