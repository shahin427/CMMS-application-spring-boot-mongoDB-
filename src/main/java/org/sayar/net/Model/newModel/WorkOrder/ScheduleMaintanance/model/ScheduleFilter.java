package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.model;

import org.bson.types.ObjectId;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderStatus;

public class ScheduleFilter {
    private Priority priority;
    private MaintenanceType maintenanceType;
    private WorkOrderStatus status;
    private String assetId;
    private ScheduleType type;
    private boolean active;
    private String projectId;

    public ScheduleFilter() {
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public MaintenanceType getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(MaintenanceType maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public WorkOrderStatus getStatus() {
        return status;
    }

    public void setStatus(WorkOrderStatus status) {
        this.status = status;
    }

    public ObjectId getAssetId() {
        return new ObjectId(assetId);
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public ScheduleType getType() {
        return type;
    }

    public void setType(ScheduleType type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
