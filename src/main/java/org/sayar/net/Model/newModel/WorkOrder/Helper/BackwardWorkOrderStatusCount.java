package org.sayar.net.Model.newModel.WorkOrder.Helper;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;

@Data
public class BackwardWorkOrderStatusCount {
    private long count;
    private MaintenanceType status;
}
