package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.sayar.net.Model.newModel.Project;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduledMeteringCycle;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;

@Data
public class ScheduleMaintenanceCreateDTO {
    private String id;
    private String code;
    private String title;
    private String assetId;
    private String assetName;
    private DocumentFile image;
    private String projectId;
    private String projectName;
    private Priority priority;
    private MaintenanceType maintenanceType;
    private String statusId;
    private String statusName;
    private boolean active;
    private String activityId;
    private String activityTitle;
    private ScheduledMeteringCycle scheduledMeteringCycle;

    public static ScheduleMaintenanceCreateDTO map(Activity activity, ScheduleMaintenance scheduleMaintenance, Asset associatedAssetToTheSchedule
            , Project associatedProjectToTheSchedule, WorkOrderStatus associatedWorkOrderStatusToTheSchedule) {

        ScheduleMaintenanceCreateDTO scheduleMaintenanceCreateDTO = new ScheduleMaintenanceCreateDTO();
        if (scheduleMaintenance != null) {
            if (scheduleMaintenance.getCode() != null)
                scheduleMaintenanceCreateDTO.setCode(scheduleMaintenance.getCode());
            if (scheduleMaintenance.getTitle() != null)
                scheduleMaintenanceCreateDTO.setTitle(scheduleMaintenance.getTitle());
            if (scheduleMaintenance.getAssetId() != null)
                scheduleMaintenanceCreateDTO.setAssetId(scheduleMaintenance.getAssetId());
            if (scheduleMaintenance.getId() != null)
                scheduleMaintenanceCreateDTO.setId(scheduleMaintenance.getId());
            if (scheduleMaintenance.getImage() != null)
                scheduleMaintenanceCreateDTO.setImage(scheduleMaintenance.getImage());
            if (scheduleMaintenance.getProjectId() != null)
                scheduleMaintenanceCreateDTO.setProjectId(scheduleMaintenance.getProjectId());
            if (scheduleMaintenance.getPriority() != null)
                scheduleMaintenanceCreateDTO.setPriority(scheduleMaintenance.getPriority());
            if (scheduleMaintenance.getMaintenanceType() != null)
                scheduleMaintenanceCreateDTO.setMaintenanceType(scheduleMaintenance.getMaintenanceType());
            if (scheduleMaintenance.getStatusId() != null)
                scheduleMaintenanceCreateDTO.setStatusId(scheduleMaintenance.getStatusId());
            if (scheduleMaintenance.getActivityId() != null)
                scheduleMaintenanceCreateDTO.setActivityId(scheduleMaintenance.getActivityId());


            if (activity != null) {
                if (activity.getTitle() != null && !activity.getTitle().equals(""))
                    scheduleMaintenanceCreateDTO.setActivityTitle(scheduleMaintenance.getTitle());

            }

            scheduleMaintenanceCreateDTO.setActive(scheduleMaintenance.isActive());
        }
        if (associatedProjectToTheSchedule != null && associatedProjectToTheSchedule.getName() != null)
            scheduleMaintenanceCreateDTO.setProjectName(associatedProjectToTheSchedule.getName());
        if (associatedAssetToTheSchedule != null && associatedAssetToTheSchedule.getName() != null)
            scheduleMaintenanceCreateDTO.setAssetName(associatedAssetToTheSchedule.getName());
        if (associatedWorkOrderStatusToTheSchedule != null && associatedWorkOrderStatusToTheSchedule.getName() != null)
            scheduleMaintenanceCreateDTO.setStatusName(associatedWorkOrderStatusToTheSchedule.getName());

        return scheduleMaintenanceCreateDTO;

    }
}
