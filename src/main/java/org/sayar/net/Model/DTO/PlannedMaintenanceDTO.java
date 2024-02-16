package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class PlannedMaintenanceDTO {
    private long plannedMaintenance;
    private long unplannedWorkOrder;

    public static PlannedMaintenanceDTO map(long plannedMaintenances, long unplannedWorkOrders) {
        PlannedMaintenanceDTO plannedMaintenanceDTO = new PlannedMaintenanceDTO();
        plannedMaintenanceDTO.setPlannedMaintenance(plannedMaintenances);
        plannedMaintenanceDTO.setUnplannedWorkOrder(unplannedWorkOrders);
        return plannedMaintenanceDTO;
    }
}
