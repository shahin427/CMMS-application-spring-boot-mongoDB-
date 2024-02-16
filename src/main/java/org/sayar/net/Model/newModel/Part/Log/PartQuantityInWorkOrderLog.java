package org.sayar.net.Model.newModel.Part.Log;

import lombok.Data;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

@Data
public class PartQuantityInWorkOrderLog {

    private String id;
    private WorkOrder workOrder;
    private String date;// work order date
    private String note;
    private long quantity;
}
