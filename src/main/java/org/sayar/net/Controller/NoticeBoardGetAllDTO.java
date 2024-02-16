package org.sayar.net.Controller;

import lombok.Data;
import org.sayar.net.Enumes.RequestStatus;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.Date;

@Data
public class NoticeBoardGetAllDTO {
    private String instanceId;
    private String workRequestTitle;
    private Date workRequestTime;
    private String pmSheetCode;
    private String assetId;
    private String assetName;
    private String requesterId;
    private String requesterName;
    private String requesterFamily;
    private boolean isSchedule;
    private Priority priority;
    private MaintenanceType maintenanceType;
    private RequestStatus requestStatus;
    private String assignedUserId;
    private String activityLevelId;
    private String workOrderId;
    private boolean workRequestAcceptor;
}
