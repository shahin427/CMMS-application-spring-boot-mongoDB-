package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

import java.util.List;

@Data
public class OnTimeCompletedWorkOrdersAgainstAllCompletedWorkOrdersDTO {
    private List<WorkOrder> onTimeCompletedWorkOrders;
    private List<WorkOrder> allCompletedWorkOrders;

    public static OnTimeCompletedWorkOrdersAgainstAllCompletedWorkOrdersDTO map(List<WorkOrder> completedWorkOrders, List<WorkOrder> onTimeCompletedWorkOrders) {
        OnTimeCompletedWorkOrdersAgainstAllCompletedWorkOrdersDTO onTimeCompletedWorkOrdersAgainstAllCompletedWorkOrdersDTO = new OnTimeCompletedWorkOrdersAgainstAllCompletedWorkOrdersDTO();
        onTimeCompletedWorkOrdersAgainstAllCompletedWorkOrdersDTO.setAllCompletedWorkOrders(completedWorkOrders);
        onTimeCompletedWorkOrdersAgainstAllCompletedWorkOrdersDTO.setOnTimeCompletedWorkOrders(onTimeCompletedWorkOrders);
        return onTimeCompletedWorkOrdersAgainstAllCompletedWorkOrdersDTO;
    }
}
