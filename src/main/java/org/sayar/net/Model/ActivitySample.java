package org.sayar.net.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Enumes.AssetStatus;
import org.sayar.net.Model.Mongo.MyModel.ActivityLevel;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ActivitySample {
    //مدل فرایندهای در انتظار تایید

    @Id
    private String id;
    private String assetId;          //آیدی دستگاه
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date creationDateOfWorkRequest;          //تاریخ درخواست تعمیر
    private List<ActivityLevel> activityLevelList;   //لیست مراحل
    private String requesterId;     //آیدی درخواست کار
    private String relatedActivityId;    //آیدی فرآیند
    private String activityInstanceId;     //آیدی فرایند در کارتابل
    private List<ActivityLevelSequenceHistory> activityLevelSequenceHistory = new ArrayList<>();    //تاریخچه مراحل طی شده
    private String workOrderId;         //آیدی دستورکار
    private boolean active = true;
    private boolean fromSchedule;    //نوع درخواست
    private String workRequestId;      //ایدی درخواست
    private String workRequesterId;            //(درخواست کننده)آیدی درخواست کننده
    private String workRequesterName;      //(درخواست کننده) نام درخواست کننده
    private String workRequesterFamilyName;    // (درخواست کننده) فامیل درخواست کننده
    private String workRequesterUserTypeId;        //آیدی پست درخواست کنده
    private String workRequesterUserTypeName;     //نام پست درخواست کننده
    private String workRequestTitle;            //نام درخواست
    private boolean workOrderAccepted;         //دستور کار تایید شد یا نه
    private String organizationId;
    private Priority workRequestPriority;
    private MaintenanceType workRequestMaintenanceType;
    private String title;
    private String description;
    private String pmSheetCode;
    private String activityTypeId;//نوع فعالیت
    private String workCategoryId;// رسته کاری
    private String importanceDegreeId;//درجه  اهمیت
    private String mainSubSystemId;//قطعه ی اصلی
    private String minorSubSystem;//قطعه جزئی
    private String scheduleId;//آیدی زمانبندی مرتبط
    private AssetStatus assetStatus; //وضعیت تجهیز

    public List<ActivityLevelSequenceHistory> getActivityLevelSequenceHistory() {
        if (activityLevelSequenceHistory == null) {
            return new ArrayList<>();
        } else {
            return activityLevelSequenceHistory;
        }
    }
}
