package org.sayar.net.Model.Filter;

public class WorkOrderFilter {
    private String priority;
    private String statusId;
    private String maintenanceType;
    private String projectId;
    private String assetId;
    private Long creationDateFrom;
    private Long creationDateTo;



    public WorkOrderFilter() {
    }

    public Long getCreationDateFrom() {
        return creationDateFrom;
    }

    public void setCreationDateFrom(Long creationDateFrom) {
        this.creationDateFrom = creationDateFrom;
    }

    public Long getCreationDateTo() {
        return creationDateTo;
    }

    public void setCreationDateTo(Long creationDateTo) {
        this.creationDateTo = creationDateTo;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
}
