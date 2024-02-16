package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.sayar.net.Model.newModel.Project;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class WorkOrderFilterDTO {
    private String id;
    private String title;
    private String code;
    private String pmSheetCode;
    private Priority priority;
    private Project project;
    private Asset asset;
    private WorkOrderStatus status;
    private Date startDate;
    private Date endDate;
    private MaintenanceType maintenanceType;
    private boolean fromSchedule;

    public static List<WorkOrderFilterDTO> map(List<WorkOrderDTO> workOrderDTO1, List<Project> projectList
            , List<WorkOrderStatus> workOrderStatusList, List<Asset> assetList) {

        List<WorkOrderFilterDTO> workOrderDTOList = new ArrayList<>();
        workOrderDTO1.forEach(workOrderDTO -> {
            WorkOrderFilterDTO workOrderFilterDTO = new WorkOrderFilterDTO();
            workOrderFilterDTO.setId(workOrderDTO.getId());
            workOrderFilterDTO.setTitle(workOrderDTO.getTitle());
            workOrderFilterDTO.setCode(workOrderDTO.getCode());
            workOrderFilterDTO.setPriority(workOrderDTO.getPriority());
            workOrderFilterDTO.setStartDate(workOrderDTO.getStartDate());
            workOrderFilterDTO.setEndDate(workOrderDTO.getEndDate());
            workOrderFilterDTO.setPmSheetCode(workOrderDTO.getPmSheetCode());
            workOrderFilterDTO.setMaintenanceType(workOrderDTO.getMaintenanceType());
            workOrderFilterDTO.setFromSchedule(workOrderDTO.getFromSchedule());
            workOrderFilterDTO.setAsset(assetList.stream().filter(asset1 -> asset1.getId().equals(workOrderDTO.getAssetId())).findFirst().orElse(null));
            workOrderFilterDTO.setProject(projectList.stream().filter(project1 -> project1.getId().equals(workOrderDTO.getProjectId())).findFirst().orElse(null));
            workOrderFilterDTO.setStatus(workOrderStatusList.stream().filter(workOrderStatus -> workOrderStatus.getId().equals(workOrderDTO.getStatusId())).findFirst().orElse(null));
            workOrderDTOList.add(workOrderFilterDTO);
        });
        return workOrderDTOList;
    }
}
