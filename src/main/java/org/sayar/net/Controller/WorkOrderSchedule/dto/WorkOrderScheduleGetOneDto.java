package org.sayar.net.Controller.WorkOrderSchedule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Enumes.AssetStatus;
import org.sayar.net.Model.UsedPart;
import org.sayar.net.Model.WorkOrderSchedule;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderScheduleGetOneDto {
    private Date startDate;
    private Date endDate;
    private Date nexDate;
    private String assetId;
    private String assetName;
    private String mainSubSystemId;
    private String mainSubSystemName;
    private String activityTypeId;
    private String activityTypeName;
    private String workCategoryId;
    private String minorSubSystem;
    private String workCategoryName;
    private String importanceDegreeId;
    private String importanceDegreeName;
    private Long estimateCompletionDate;//تخمین  زمان  تکمیل  پس  از ساخت دستور کار
    private Long activityTime;//مدت زمان فعالیت
    private String solution;                          //شرح فعالیت
    private List<String> userIdList;                  //تعمیرکار
    private List<UsedPart> usedPartList;              //قطعات مصرفی
    private WorkOrderSchedule.Frequency frequency;//
    private Integer per;//هر
    private WorkOrderSchedule.Mode mode;
    private AssetStatus assetStatus;
    private WorkOrderSchedule.RunStatus runStatus;
    private String activityId;
    private String activityTitle;

}