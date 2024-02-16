package org.sayar.net.Model.newModel.WorkOrder.Helper;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.Priority;

@Data
public class BackwardWorkOrderPriority {
    private long count;
    private Priority priority;
}
