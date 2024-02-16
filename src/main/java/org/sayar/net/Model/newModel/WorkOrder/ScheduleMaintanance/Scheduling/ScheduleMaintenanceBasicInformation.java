package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Data
public class ScheduleMaintenanceBasicInformation {

    private String issueSummary;
    private int laborHour;
    private int actualLaborHour;
    private String workInstruction;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date completionDate;
}

