package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling;

import lombok.Data;
import java.util.Date;

@Data
public class ScheduledMeteringCycle {
    private long per;
    private boolean createWo;
    private long endDistance;
    private long startDistance = 0;
    private FixType fixType;
    private String unitOfMeasurementId;
    private String unitOfMeasurementName;
    private Date registrationDate;
}
