package org.sayar.net.Model.newModel.WorkOrder;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Enumes.AssetStatus;
import org.sayar.net.Model.CompletionDetail;
import org.sayar.net.Model.UsedPart;
import org.sayar.net.Model.newModel.BasicInformation;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkOrder {

    //مدل آرشیو دستورکار
    @Id
    private String id;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;                           //تاریخ  سررسید -تاریخ وقوع خرابی
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;                             //تاریخ راه اندازی-تارخ اقدام
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date requestedDate;                       //تاریخ تحویل به تعمیرات-تاریخی که باید زمانبندی انجام شود
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date repairDate;                          //تاریخ شروع تعمیر
    private String requestDescription;                //شرح درخواست
    private String failureReason;                     //علت خرابی
    private String solution;                          //شرح اقدامات انجام شده-شرح فعالیت
    private List<String> userIdList;                  //تعمیرکار
    private String assetId;                           //آیدی دستگاه
    private List<UsedPart> usedPartList;              //قطعات مصرفی
    private long partSupply;                          //تامین قطعه
    private Long failureDuration;                     //مدت زمان خرابی
    private Long repairDuration;                      //مدت زمان تعمیر
    private boolean acceptedByManager;                //دستورکار تایید شده
    private String workRequestId;                     //آیدی درخواست کار
    private boolean fromSchedule;                     //نوع درخواست
    private String pmSheetCode;                       //شماره برگه pm
    private String associatedScheduleMaintenanceId;
    private Long activityTime;//مدت زمان فعالیت
    private AssetStatus assetStatus;//وضعیت تجهیز
    private String activityTypeId;//نوع فعالیت
    private String workCategoryId;// رسته کاری
    private String importanceDegreeId;//درجه  اهمیت
    private String mainSubSystemId;//قطعه ی اصلی
    private String minorSubSystem;//قطعه جزئی
    private Long estimateCompletionDate;//تخمین  زمان  تکمیل  پس  از ساخت دستور کار
    private boolean rejectedInSchedule;//زمانبندی های رد شده
    //-------------------------------------------------------------
    //از این به پایین فعلا نیازی نسیت
    private String title;
    private String code;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date requiredCompletionDate;
    private DocumentFile image;
    private String projectId;
    private List<String> laborTasks;
    private List<String> taskGroups;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date creationDate;
    private Priority priority;
    private MaintenanceType maintenanceType;
    private String statusId;
    private CompletionDetail completionDetail;
    private List<String> tasks;
    private BasicInformation basicInformation;
    private boolean deleted;
    private boolean notToBeShown;
    private String activityId;  //this is real activityId
    private boolean rejectedWorkOrder;
    private boolean assignedToTechnician; // when it's true, means the workOrder accepted by maintenance manager and assigned to a technician and will be used in workOrder archive and manager's dashboard
    private List<String> usedParts;
    private List<String> documentsList;

    public WorkOrder(CompletionDetail completionDetail, BasicInformation basicInformation) {
        this.completionDetail = completionDetail;
        this.basicInformation = basicInformation;
    }

    public WorkOrder() {
    }
}
