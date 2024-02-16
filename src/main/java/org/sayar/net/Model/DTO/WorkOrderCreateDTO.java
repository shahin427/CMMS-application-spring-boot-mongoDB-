package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;

import java.util.Date;

@Data
public class WorkOrderCreateDTO {
    private String id;
    private String title;
    private String code;
    private MaintenanceType maintenanceType;
    private Date creationDate;
    private String statusId;
    private Priority priority;
    private String assetId;
    private String assetName;
    private String assetCode;
    private Date startDate;
    private WorkOrderStatus status;
    private String projectId;
    private Date endDate;
    private Date requiredCompletionDate;
    private DocumentFile image;
    private boolean fromSchedule;

    public static WorkOrderCreateDTO map(WorkOrder workOrder, Asset workOrderAsset) {
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date todayWithZeroTime = null;
//        try {
//            todayWithZeroTime = formatter.parse(formatter.format(workOrder.getStartDate()));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        WorkOrderCreateDTO workOrderCreateDTO = new WorkOrderCreateDTO();
        workOrderCreateDTO.setCode(workOrder.getCode());
        workOrderCreateDTO.setTitle(workOrder.getTitle());
        workOrderCreateDTO.setAssetId(workOrder.getAssetId());
        if (workOrderAsset != null && workOrderAsset.getName() != null && workOrderAsset.getCode() != null) {
            workOrderCreateDTO.setAssetName(workOrderAsset.getName());
            workOrderCreateDTO.setAssetCode(workOrderAsset.getCode());
        }
        workOrderCreateDTO.setMaintenanceType(workOrder.getMaintenanceType());
        workOrderCreateDTO.setCreationDate(workOrder.getCreationDate());
        workOrderCreateDTO.setEndDate(workOrder.getEndDate());
        workOrderCreateDTO.setRequiredCompletionDate(workOrder.getRequiredCompletionDate());
        workOrderCreateDTO.setProjectId(workOrder.getProjectId());
        workOrderCreateDTO.setStartDate(new Date());
        workOrderCreateDTO.setStatusId(workOrder.getStatusId());
        workOrderCreateDTO.setPriority(workOrder.getPriority());
        workOrderCreateDTO.setImage(workOrder.getImage());
        return workOrderCreateDTO;
    }
}
