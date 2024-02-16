package org.sayar.net.Controller.Mongo.activityController.activity;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;

import java.util.Date;

@Data
public class ActivityHistoryDTO {
    private String requestTitle;
    private Date RequestTime;
    private Date deliveryDate;
    private Date replyDate;
    private String assetId;
    private boolean fromSchedule;
    private Priority priority;
    private MaintenanceType maintenanceType;
}
