package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Enumes.AssetStatus;
import org.sayar.net.Model.UsedPart;

import java.util.Date;
import java.util.List;

@Data
public class WorkOrderScheduleDTO {
    private String id;
    private String assetId;//نام دستگاه
    private String assetName;//نام دستگاه
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
    private Long activityTime;//مدت زمان فعالیت
    private Date startDate;//تاریخ  سررسید
    private Date endDate;//تاریخ اقدام
    private AssetStatus assetStatus;//وضعیت تجهیز
    private List<UsedPart> usedPartList;//قطعات مصرفی
    private List<String> userIdList;//تعمیرکار
    private String associatedScheduleMaintenanceId;
}
