package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class WorkOrderDTOBasicInformation {
    private String issueSummary;
    private String userAssignedId;
    private String completedUserId;
    private int laborHour;
    private int actualLaborHour;
    private Date completionDate;
    private String workInstruction;
}
