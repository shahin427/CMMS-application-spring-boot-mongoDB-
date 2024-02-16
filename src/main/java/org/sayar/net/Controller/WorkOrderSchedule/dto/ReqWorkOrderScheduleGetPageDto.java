package org.sayar.net.Controller.WorkOrderSchedule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Enumes.AssetStatus;
import org.sayar.net.Model.WorkOrderSchedule;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReqWorkOrderScheduleGetPageDto {
    private String assetId;//دستگاه
    private String mainSubSystemId;//قطعه ی اصلی
    private String minorSubSystem;//قطعه جزئی
    private String activityTypeId;
    private String workCategoryId;
    private String importanceDegreeId;
    private AssetStatus assetStatus;
    private WorkOrderSchedule.RunStatus runStatus;

}