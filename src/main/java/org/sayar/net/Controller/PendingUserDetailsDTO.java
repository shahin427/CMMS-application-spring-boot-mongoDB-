package org.sayar.net.Controller;

import lombok.Data;
import org.sayar.net.Model.DTO.PrimaryPendingUserDetailsDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class PendingUserDetailsDTO {
    private String formId;
    private String formDataId;
    private List<String> staticFormsIdList = new ArrayList<>();
    private String workOrderId;
    private String workOrderAccessId;
    private String userTypeId;
    private String userId;
    private int numberOfParticipation;
    private boolean rightToChoose;
    private boolean existRecipientOrderUser;
    private String organizationId;
    private String nextActivityLevelId;
    private String activityLevelId;
    private boolean workRequestAcceptor;
    private boolean workOrderAccepted;

    public static PendingUserDetailsDTO map(PrimaryPendingUserDetailsDTO primaryPendingUserDetails) {
        PendingUserDetailsDTO pendingUserDetailsDTO = new PendingUserDetailsDTO();
        pendingUserDetailsDTO.setExistRecipientOrderUser(primaryPendingUserDetails.isExistRecipientOrderUser());

        if (primaryPendingUserDetails.getFormDataId() != null)
            pendingUserDetailsDTO.setFormDataId(primaryPendingUserDetails.getFormDataId());
        if (primaryPendingUserDetails.getFormId() != null)
            pendingUserDetailsDTO.setFormId(primaryPendingUserDetails.getFormId());
        if (primaryPendingUserDetails.getNextActivityLevelId() != null)
            pendingUserDetailsDTO.setNextActivityLevelId(primaryPendingUserDetails.getNextActivityLevelId());
        if (primaryPendingUserDetails.getOrganizationId() != null)
            pendingUserDetailsDTO.setOrganizationId(primaryPendingUserDetails.getOrganizationId());
        if (primaryPendingUserDetails.getStaticFormsIdList() != null)
            pendingUserDetailsDTO.setStaticFormsIdList(primaryPendingUserDetails.getStaticFormsIdList());
        if (primaryPendingUserDetails.getUserId() != null)
            pendingUserDetailsDTO.setUserId(primaryPendingUserDetails.getUserId());
        if (primaryPendingUserDetails.getUserTypeId() != null)
            pendingUserDetailsDTO.setUserTypeId(primaryPendingUserDetails.getUserTypeId());
        if (primaryPendingUserDetails.getWorkOrderAccessId() != null)
            pendingUserDetailsDTO.setWorkOrderAccessId(primaryPendingUserDetails.getWorkOrderAccessId());
        if (primaryPendingUserDetails.getWorkOrderId() != null)
            pendingUserDetailsDTO.setWorkOrderId(primaryPendingUserDetails.getWorkOrderId());
        if (primaryPendingUserDetails.getActivityLevelId() != null)
            pendingUserDetailsDTO.setActivityLevelId(primaryPendingUserDetails.getActivityLevelId());
        pendingUserDetailsDTO.setWorkRequestAcceptor(primaryPendingUserDetails.isWorkRequestAcceptor());
        pendingUserDetailsDTO.setWorkOrderAccepted(primaryPendingUserDetails.isWorkOrderAccepted());

        pendingUserDetailsDTO.setNumberOfParticipation(primaryPendingUserDetails.getActivityLevelSequenceHistory().size() + 1);
        pendingUserDetailsDTO.setRightToChoose(primaryPendingUserDetails.isRightToChoose());
        return pendingUserDetailsDTO;
    }
}
