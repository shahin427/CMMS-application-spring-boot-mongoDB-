package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.ActivityLevelSequenceHistory;

import java.util.List;

@Data
public class PrimaryPendingUserDetailsDTO {
    private String formId;
    private String formDataId;
    private List<String> staticFormsIdList;
    private String workOrderId;
    private String workOrderAccessId;
    private String userTypeId;
    private String userId;
    private String userName;
    private String userFamily;
    private String userTypeTitle;
    private List<ActivityLevelSequenceHistory> activityLevelSequenceHistory;
    private boolean rightToChoose;
    private boolean existRecipientOrderUser;
    private boolean workRequestAcceptor;
    private boolean workOrderAccepted;
    private String organizationId;
    private String nextActivityLevelId;
    private String activityLevelId;
}
