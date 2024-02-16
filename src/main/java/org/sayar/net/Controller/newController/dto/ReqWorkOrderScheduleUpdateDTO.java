package org.sayar.net.Controller.newController.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twilio.rest.serverless.v1.service.Asset;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Enumes.AssetStatus;
import org.sayar.net.Model.UsedPart;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReqWorkOrderScheduleUpdateDTO {
        private String id;
    private String activityTypeId;
    private String workCategoryId;
    private String importanceDegreeId;
    private AssetStatus assetStatus;
    private Long activityTime;
    private Long estimateCompletionDate;
    private String solution;
    private List<String> userIdList;
    private List<UsedPart> usedPartList;

}
