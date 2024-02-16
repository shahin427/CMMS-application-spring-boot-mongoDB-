package org.sayar.net.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Enumes.AssetStatus;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

//@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderSchedule {

    @Id
    private String id;
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;   //بازه شروع  زمانبندی
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date nexDate;//   تاریخ  بعدی فعال شدن زمان بندی
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;//تاریخ  پایان  زمان بندی
    private String assetId;//دستگاه
    private String mainSubSystemId;//قطعه ی اصلی
    private String minorSubSystem;//قطعه جزئی
    private String activityTypeId;//نوع فعالیت
    private String workCategoryId;// رسته کاری
    private String importanceDegreeId;//درجه  اهمیت
    private Long estimateCompletionDate;//تخمین  زمان  تکمیل  پس  از ساخت دستور کار
    private Long activityTime;//مدت زمان فعالیت
    private String solution;                          //شرح فعالیت
    private List<String> userIdList;                  //تعمیرکار
    private List<UsedPart> usedPartList;              //قطعات مصرفی
    private Frequency frequency;
    private Integer per;//هر
    private Mode mode;
    private AssetStatus assetStatus;
    private RunStatus runStatus;
    private String activityId;


    public WorkOrderSchedule(Date startDate, Date nexDate, Date endDate, String assetId,
                             String mainSubSystemId, String minorSubSystem, String activityTypeId, String workCategoryId,
                             String importanceDegreeId, Long estimateCompletionDate, Long activityTime, String solution,
                             List<String> userIdList, List<UsedPart> usedPartList, Frequency frequency, Integer per,
                             Mode mode, AssetStatus assetStatus, RunStatus runStatus, String activityId) {
        super();
        this.startDate = startDate;
        this.nexDate = nexDate;
        this.endDate = endDate;
        this.assetId = assetId;
        this.mainSubSystemId = mainSubSystemId;
        this.minorSubSystem = minorSubSystem;
        this.activityTypeId = activityTypeId;
        this.workCategoryId = workCategoryId;
        this.importanceDegreeId = importanceDegreeId;
        this.estimateCompletionDate = estimateCompletionDate;
        this.activityTime = activityTime;
        this.solution = solution;
        this.userIdList = userIdList;
        this.usedPartList = usedPartList;
        this.frequency = frequency;
        this.per = per;
        this.mode = mode;
        this.assetStatus = assetStatus;
        this.runStatus = runStatus;
        this.activityId = activityId;
    }

    public enum Frequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    public enum Mode {
        FLOAT,//جاری
        FIXED//ثابت
    }


    //    وضعیت اجرا
    public enum RunStatus {
        ACTIVE, // فعال
        DE_ACTIVE, // غیر فعال
    }
}
