package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.sayar.net.Model.newModel.Project;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduledMeteringCycle;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleMaintenanceCreateDTOWithObject {
    private String id;
    private Asset asset;
    private DocumentFile image;
    private Priority priority;
    private Project project;
    private MaintenanceType maintenanceType;
    private WorkOrderStatus workOrderStatus;
    private String code;
    private String title;
    private boolean active;
    private ScheduledMeteringCycle scheduledMeteringCycle;

    public static List<ScheduleMaintenanceCreateDTOWithObject> FilterMap(List<ScheduleMaintenanceCreateDTO> scheduleMaintenanceCreateDTOList, List<Asset> assetList,
                                                                         List<Project> projectList, List<WorkOrderStatus> workOrderStatusList) {
        List<ScheduleMaintenanceCreateDTOWithObject> scheduleMaintenanceCreateDTOListWithObject = new ArrayList<>();
        scheduleMaintenanceCreateDTOList.forEach(scheduleMaintenanceCreateDTO1 -> {

            ScheduleMaintenanceCreateDTOWithObject scheduleMaintenanceCreateDTOWithObject = new ScheduleMaintenanceCreateDTOWithObject();

            scheduleMaintenanceCreateDTOWithObject.setId(scheduleMaintenanceCreateDTO1.getId());
            scheduleMaintenanceCreateDTOWithObject.setTitle(scheduleMaintenanceCreateDTO1.getTitle());
            scheduleMaintenanceCreateDTOWithObject.setCode(scheduleMaintenanceCreateDTO1.getCode());
            scheduleMaintenanceCreateDTOWithObject.setPriority(scheduleMaintenanceCreateDTO1.getPriority());
            scheduleMaintenanceCreateDTOWithObject.setMaintenanceType(scheduleMaintenanceCreateDTO1.getMaintenanceType());
            scheduleMaintenanceCreateDTOWithObject.setScheduledMeteringCycle(scheduleMaintenanceCreateDTO1.getScheduledMeteringCycle());
            scheduleMaintenanceCreateDTOWithObject.setAsset(assetList.stream().filter(asset ->
                    asset.getId().equals(scheduleMaintenanceCreateDTO1.getAssetId())).findFirst().orElse(null));
            scheduleMaintenanceCreateDTOWithObject.setProject(projectList.stream().filter(project1 -> project1.getId()
                    .equals(scheduleMaintenanceCreateDTO1.getProjectId())).findFirst().orElse(null));
            scheduleMaintenanceCreateDTOWithObject.setWorkOrderStatus(workOrderStatusList.stream().filter(workOrderStatus1 -> workOrderStatus1.getId()
                    .equals(scheduleMaintenanceCreateDTO1.getStatusId())).findFirst().orElse(null));
            scheduleMaintenanceCreateDTOListWithObject.add(scheduleMaintenanceCreateDTOWithObject);
        });
        return scheduleMaintenanceCreateDTOListWithObject;
    }
}
