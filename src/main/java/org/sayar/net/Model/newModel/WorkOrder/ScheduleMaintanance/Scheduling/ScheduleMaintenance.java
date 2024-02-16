package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Enum.MaintenanceType;
import org.sayar.net.Model.newModel.Enum.Priority;
import org.springframework.data.annotation.Id;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ScheduleMaintenance {
    @Id
    private String id;
    private String code;
    private String title;
    private boolean active = true;
    private String statusId;
    private MaintenanceType maintenanceType;
    private long numberOfDayForEndingEachScheduleMaintenance = 0;
    private DocumentFile image;
    private Priority priority;
    private String assetId;
    private String projectId;
    private List<String> taskGroupList;
    private List<String> taskList;
    private DocumentFile documentFile;
    private ScheduleMaintenanceBasicInformation scheduleMaintenanceBasicInformation;
    private ScheduleMaintenanceCompletionDetail scheduleMaintenanceCompletionDetail;
    private ScheduleWithTimeAndMetering scheduleWithTimeAndMetering;
    private boolean deleted;
    private String activityId;
    private boolean hasTimeSchedule;
    private boolean hasMeteringSchedule;

    public ScheduleMaintenance(ScheduleWithTimeAndMetering scheduleWithTimeAndMetering) {
        this.scheduleWithTimeAndMetering = scheduleWithTimeAndMetering;
    }

    public ScheduleMaintenance() {

    }
}

