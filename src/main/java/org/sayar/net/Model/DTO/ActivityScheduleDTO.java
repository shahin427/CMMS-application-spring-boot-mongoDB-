package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Enumes.AssetStatus;

import java.util.Date;

@Data
public class ActivityScheduleDTO {
    private String assetId;
    private String mainSubSystemId;//قطعه ی اصلی
    private String minorSubSystem;//قطعه جزئی
    private String workCategoryId;// رسته کاری
    private String activityTypeId;//نوع فعالیت
    private String importanceDegreeId;//درجه  اهمیت
    private Date startDate;//تاریخ سررسید
    private AssetStatus assetStatus;//وضعیت تجهیز

}
