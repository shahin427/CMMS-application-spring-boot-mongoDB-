package org.sayar.net.Controller.WorkOrderSchedule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Enumes.AssetStatus;
import org.sayar.net.Model.UsedPart;
import org.sayar.net.Model.WorkOrderSchedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderScheduleCreateDto {
    private Date startDate;
    private Date endDate;
    private List<AssetLight> assetList;
    private String activityTypeId;
    private String workCategoryId;
    private String importanceDegreeId;
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


    public List<WorkOrderSchedule> map() {
        List<WorkOrderSchedule> result = new ArrayList<>();
//        nextDate.setHours(this.startDate.getHours());
//        nextDate.setHours(00);
//        nextDate.setMinutes(00);
//        nextDate.setSeconds(000);
//        Date nextDate = new Date();
        Date nextDate = this.startDate;
        Calendar cal = Calendar.getInstance();
        cal.setTime(nextDate);
        cal.set(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 1);
        cal.set(Calendar.MILLISECOND, 1);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        nextDate = cal.getTime();

        Date finalNextDate = nextDate;
        this.assetList.forEach(a -> {
            result.add(new WorkOrderSchedule(this.startDate, finalNextDate, this.endDate, a.getAssetId(),
                    a.getMainSubSystemId(), a.getMinorSubSystem(), this.activityTypeId, this.workCategoryId,
                    this.importanceDegreeId, this.estimateCompletionDate, this.activityTime, this.solution,
                    this.userIdList, this.usedPartList, this.frequency, this.per, this.mode, this.assetStatus, this.runStatus, a.getActivityId()));
        });
        return result;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetLight {
        private String assetId;//دستگاه
        private String mainSubSystemId;//قطعه ی اصلی
        private String minorSubSystem;//قطعه جزئی
        //to dooooooo
        private String activityId;//قطعه جزئی
    }
}