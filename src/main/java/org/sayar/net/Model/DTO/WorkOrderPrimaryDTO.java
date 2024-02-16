package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.UsedPart;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkOrderPrimaryDTO {
    private String id;
    private Date startDate;
    private Date requestedDate;
    private Date repairDate;
    private String requestDescription;
    private Date endDate;
    private String failureReason;
    private String solution;
    private String workRequestId;
    private String assetId;
    private String assetName;
    private String pmSheetCode;
    private String title;
    private List<String> userIdList;
    private List<UsedPart> usedPartList;
    private long partSupply;
}
