package org.sayar.net.Model.newModel.WorkOrder;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.WorkOrderStatusEnum;
import org.springframework.data.annotation.Id;

@Data
public class WorkOrderStatus {
    @Id
    private String id;
    private String name;
    private WorkOrderStatusEnum status;
}
