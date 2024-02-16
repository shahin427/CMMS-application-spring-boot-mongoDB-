package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.ActivityLevelSequenceHistory;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ActivitySampleDTO {
        @Id
        private String id;

        private String title;
        private String description;
        private List<ActivityLevelDTO> activityLevelDTOList;
        private String organizationId;
        private String requesterId;
        private String requesterName;
        private String requesterFamilyName;
        private String requesterUserTypeName;
        private String relatedActivityId;
        private String activityInstanceId;
        private List<ActivityLevelSequenceHistory> activityLevelSequenceHistory = new ArrayList<>();
        private String workOrderId;
        private boolean active = true;
        private boolean fromSchedule;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private Date creationDateOfWorkRequest;
        private String assetId;
        private String workRequestId;
        private String workRequestTitle;
        private Priority workRequestPriority;
        private MaintenanceType workRequestMaintenanceType;

}
