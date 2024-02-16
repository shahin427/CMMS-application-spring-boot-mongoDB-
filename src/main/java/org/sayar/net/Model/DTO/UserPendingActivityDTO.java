package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Enumes.RequestType;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.Date;

@Data
public class UserPendingActivityDTO {
    private String requestTitle;
    private Priority priority;
    private String assetId;
    private String userId;
    private String pmSheetCode;
    private Boolean fromSchedule;
    private MaintenanceType maintenanceType;
    private Date from;
    private Date until;
    private RequestType requestType;
}
