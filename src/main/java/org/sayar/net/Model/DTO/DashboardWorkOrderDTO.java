package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.Date;

@Data
public class DashboardWorkOrderDTO {
    private String workOrderId;
    private String workOrderName;
    private String workOrderCode;
    private String assetId;
    private String assetName;
    private Priority workOrderPriority;
    private MaintenanceType workOrderMaintenanceType;
    private String projectId;
    private String projectName;
    private Date workOrderStartDate;
    private Date workOrderEndDate;
    private boolean fromSchedule;
    private String workOrderStatusId;
    private String workOrderStatusName;
}
