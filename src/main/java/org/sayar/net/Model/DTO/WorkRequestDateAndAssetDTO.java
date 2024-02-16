
package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.Date;

@Data
public class WorkRequestDateAndAssetDTO {
    private String assetId;
    private String assetCode;
    private String assetNAme;
    private String activityInstanceId;
    private String workRequestId;
    private Date workRequestTime;
    private MaintenanceType maintenanceType;
    private Priority priority;
}