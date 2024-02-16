package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class PlannedWorkOrdersRatioDTO {
    private long plannedWorkOrders;
    private long allWorkOrders;

    public static PlannedWorkOrdersRatioDTO map(long plannedWorkOrders, long allWorkOrders) {
        PlannedWorkOrdersRatioDTO plannedWorkOrdersRatioDTO = new PlannedWorkOrdersRatioDTO();
        plannedWorkOrdersRatioDTO.setPlannedWorkOrders(plannedWorkOrders);
        plannedWorkOrdersRatioDTO.setAllWorkOrders(allWorkOrders);
        return plannedWorkOrdersRatioDTO;
    }
}
