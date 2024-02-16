package org.sayar.net.Model.newModel.WorkOrder;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class PartWithUsageCount {
    @Id
    private String id;
    private long planedQuantity;
    private long actualQuantity;
    private String partId;
    private String partName;
    private String partCode;
    private String referenceId;
    private boolean forSchedule;
}
