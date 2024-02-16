package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class BasicInformationDTO {
    private String issueSummary;
    private String userAssignedId;
    private String userAssignedName;
    private String userAssignedFamily;
    private String userAssignedOrgId;
    private String userAssignedOrgName;
    private String userAssignedUserTypeId;
    private String userAssignedUserTypeName;
    private String completedUserId;
    private String completedUserName;
    private String completedUserUserTypeId;
    private String completedUserOrgId;
    private int laborHour;
    private int actualLaborHour;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date CompletionDate;
    private String workInstruction;

    public static BasicInformationDTO map(WorkOrder workOrder, List<User> userNameList) {
        BasicInformationDTO basicInformation = new BasicInformationDTO();
        if (workOrder.getBasicInformation() != null) {
            if (workOrder.getBasicInformation().getIssueSummary() != null)
                basicInformation.setIssueSummary(workOrder.getBasicInformation().getIssueSummary());
            if (workOrder.getBasicInformation().getWorkInstruction() != null)
                basicInformation.setWorkInstruction(workOrder.getBasicInformation().getWorkInstruction());
            basicInformation.setLaborHour(workOrder.getBasicInformation().getLaborHour());
            basicInformation.setActualLaborHour(workOrder.getBasicInformation().getActualLaborHour());
            userNameList.forEach(user -> {
                if (workOrder.getBasicInformation().getUserAssignedId() != null
                        && user.getId().equals(workOrder.getBasicInformation().getUserAssignedId())) {
                    basicInformation.setUserAssignedId(user.getId());
                    basicInformation.setUserAssignedName(user.getName());
                } else {
                    basicInformation.setCompletedUserId(user.getId());
                    basicInformation.setCompletedUserName(user.getName());
                }
            });
            return basicInformation;
        } else {
            return null;
        }
    }
}
