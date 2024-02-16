package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScheduleMaintenanceAndProjectFilterDTO {

    private String id;
    private String title;
    private String code;
    private Asset asset;
    private MaintenanceType maintenanceType;
    private WorkOrderStatus workOrderStatus;
    private Priority priority;

    public static List<ScheduleMaintenanceAndProjectFilterDTO> map(List<ScheduleMaintenance> scheduleMaintenanceList, List<Asset> assets,
                                                                   List<WorkOrderStatus> workOrderStatusList) {

        List<ScheduleMaintenanceAndProjectFilterDTO> scheduleMaintenanceAndProjectFilterDTOS = new ArrayList<>();
        for (ScheduleMaintenance scheduleMaintenance : scheduleMaintenanceList) {
            ScheduleMaintenanceAndProjectFilterDTO scheduleMaintenanceAndProjectFilterDTO = new ScheduleMaintenanceAndProjectFilterDTO();
            if (scheduleMaintenance.getTitle() != null)
                scheduleMaintenanceAndProjectFilterDTO.setTitle(scheduleMaintenance.getTitle());
            if (scheduleMaintenance.getCode() != null)
                scheduleMaintenanceAndProjectFilterDTO.setCode(scheduleMaintenance.getCode());
            if (scheduleMaintenance.getMaintenanceType() != null)
                scheduleMaintenanceAndProjectFilterDTO.setMaintenanceType(scheduleMaintenance.getMaintenanceType());
            if (scheduleMaintenance.getPriority() != null)
                scheduleMaintenanceAndProjectFilterDTO.setPriority(scheduleMaintenance.getPriority());
            scheduleMaintenanceAndProjectFilterDTO.setId(scheduleMaintenance.getId());
            scheduleMaintenanceAndProjectFilterDTO.setAsset(assets.stream().filter(asset1 ->
                    asset1.getId().equals(scheduleMaintenance.getAssetId()))
                    .findFirst().orElse(null));
            if (workOrderStatusList != null) {
                scheduleMaintenanceAndProjectFilterDTO.setWorkOrderStatus(workOrderStatusList.stream().filter(workOrderStatus1 ->
                        workOrderStatus1.getId().equals(scheduleMaintenance.getStatusId())).findFirst().orElse(null));
            }
            scheduleMaintenanceAndProjectFilterDTOS.add(scheduleMaintenanceAndProjectFilterDTO);
        }
        return scheduleMaintenanceAndProjectFilterDTOS;
    }
}
