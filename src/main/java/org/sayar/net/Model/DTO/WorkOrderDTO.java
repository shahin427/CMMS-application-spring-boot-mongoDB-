package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.Date;

@Data
public class WorkOrderDTO {

    private String assetId;
    private String assetName;
    private String pmSheetCode;
    private Boolean fromSchedule;
    private Date failureDateFrom;
    private Date failureDateUntil;

//----------------------
    private Date startDate;
    private String id;
    private String title;
    private String code;
    private Priority priority;
    private String projectId;
    private String statusId;
    private Date endDate;
    private MaintenanceType maintenanceType;
}
