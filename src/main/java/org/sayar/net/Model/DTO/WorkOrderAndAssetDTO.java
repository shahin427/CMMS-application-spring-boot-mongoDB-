package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkOrderAndAssetDTO {
    private String id;
    private String title;
    private String code;
    private Asset asset;
    private Priority priority;
    private MaintenanceType maintenanceType;
    private WorkOrderStatus status;


    public static List<WorkOrderAndAssetDTO> map(List<WorkOrder> workOrder, List<Asset> asset, List<WorkOrderStatus> workOrderStatusList) {
        List<WorkOrderAndAssetDTO> workOrderAndAssetDTOS = new ArrayList<>();
        workOrder.forEach(workOrder1 -> {
            WorkOrderAndAssetDTO workOrderAndAssetDTO = new WorkOrderAndAssetDTO();
            workOrderAndAssetDTO.setId(workOrder1.getId());
            workOrderAndAssetDTO.setTitle(workOrder1.getTitle());
            workOrderAndAssetDTO.setCode(workOrder1.getCode());
            workOrderAndAssetDTO.setPriority(workOrder1.getPriority());
            workOrderAndAssetDTO.setMaintenanceType(workOrder1.getMaintenanceType());
            workOrderAndAssetDTO.setAsset(asset.stream().filter(f -> f.getId().equals(workOrder1.getAssetId())).findFirst().orElse(null));
            workOrderAndAssetDTO.setStatus(workOrderStatusList.stream().filter(workOrderStatus1 ->
                    workOrderStatus1.getId().equals(workOrder1.getStatusId())).findFirst().orElse(null));
            workOrderAndAssetDTOS.add(workOrderAndAssetDTO);
        });
        return workOrderAndAssetDTOS;
    }
}
