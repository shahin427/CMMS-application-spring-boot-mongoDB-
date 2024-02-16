package org.sayar.net.Model.DTO;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class WorkOrderAccess {
    @Id
    private String id;
    private boolean code;
    private boolean title;
    private boolean requiredCompletionDate;
    private boolean image;
    private boolean assetId;
    private boolean projectId;
    private boolean startDate;
    private boolean endDate;
    private boolean statusId;
    private boolean priority;
    private boolean maintenanceType;
}