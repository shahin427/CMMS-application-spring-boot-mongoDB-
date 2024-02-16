package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling;

import lombok.Data;

@Data
public class ScheduleMaintenanceCompletionDetail {

    private String note;
    private String problem;
    private String rootCause;
    private String solution;
    private String adminNote;
}
