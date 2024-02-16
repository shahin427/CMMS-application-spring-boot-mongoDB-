package org.sayar.net.Controller.newController.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.sayar.net.Enumes.AssetStatus;

import java.util.Date;

@Data
@Getter
@Setter
public class ResWorkOrderPmGetPageDTO {
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
}