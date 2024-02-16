package org.sayar.net.Controller.newController.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.sayar.net.Enumes.AssetStatus;
import org.sayar.net.Model.UsedPart;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
public class ResWorkOrderPmGetOneDTO {
    private String id;

    private String assetId;
    private String assetName;

    private String mainSubSystemId;//قطعه ی اصلی
    private String mainSubSystemName;//قطعه ی اصلی

    private String minorSubSystem;//قطعه جزئی

    private String workCategoryId;// رسته کاری
    private String workCategoryName;// رسته کاری

    private String activityTypeId;//نوع فعالیت
    private String activityTypeName;//نوع فعالیت

    private String importanceDegreeId;//درجه  اهمیت
    private String importanceDegreeName;//درجه  اهمیت

    private String solution;//شرح فعالیت

    private AssetStatus assetStatus;//وضعیت تجهیز

    private Long activityTime;//مدت زمان فعالیت
    private Date startDate;//تاریخ  سررسید
    private Date endDate;//تاریخ  اقدام

    private Date requestedDate;
    private Date repairDate;
    private String requestDescription;
    private String failureReason;
    private Long failureDuration;
    private List<String> userIdList;
    private List<UsedPart> usedPartList;              //قطعات مصرفی
    private long partSupply;
    private long repairDuration;
    private boolean acceptedByManager;                //دستورکار تایید شده
    private String pmSheetCode;
    private String associatedScheduleMaintenanceId;



 }