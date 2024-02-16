package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.ExpirationStatus;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.Date;

@Data
public class ScheduleMaintenanceFilterDTO {
    private String assetId;
    private MaintenanceType maintenanceType;
    private String workOrderStatusId;
    private Priority priority;
    private String projectId;
    private String title;
    private String code;
    private ExpirationStatus expirationStatus;
    private Date dueDate;
}
