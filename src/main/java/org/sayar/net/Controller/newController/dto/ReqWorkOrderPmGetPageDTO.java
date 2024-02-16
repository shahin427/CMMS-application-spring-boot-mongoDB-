package org.sayar.net.Controller.newController.dto;

import lombok.Data;
import org.sayar.net.Enumes.AssetStatus;

import java.util.Date;

@Data
public class ReqWorkOrderPmGetPageDTO {

    private String assetId;
    private String mainSubSystemId;//قطعه ی اصلی
    private String minorSubSystem;//قطعه جزئی
    private String workCategoryId;// رسته کاری
    private String activityTypeId;//نوع فعالیت
    private String importanceDegreeId;//درجه  اهمیت
    private Date startDate;//تاریخ سررسید
    private Date endDate;//تاریخ سررسید
    private AssetStatus assetStatus;//وضعیت تجهیز

}
