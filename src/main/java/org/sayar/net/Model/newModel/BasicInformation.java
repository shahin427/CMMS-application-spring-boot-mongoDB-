package org.sayar.net.Model.newModel;

import lombok.Data;

@Data
public class BasicInformation {
    private String issueSummary;
    private String userAssignedId;
//    private String userAssignedOrgId;
    private String userAssignedUserTypeId;
    private String completedUserId;
    private String completedUserUserTypeId;
//    private String completedUserOrgId;
    private int laborHour;
    private int actualLaborHour;
    private String workInstruction;
}
