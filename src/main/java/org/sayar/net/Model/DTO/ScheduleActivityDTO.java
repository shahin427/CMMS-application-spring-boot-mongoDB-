package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

@Data
public class ScheduleActivityDTO {
    private String assetId;
    private String assetName;
    private String workOrderId;
    private String workOrderTitle;
    private Priority workOrderPriority;
    private MaintenanceType maintenanceType;
    private Activity activity;
}
