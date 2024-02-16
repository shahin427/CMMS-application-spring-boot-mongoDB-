package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;

@Data
public class ScheduleMaintenanceCalender {
    @Id
    private String id;
    private String scheduleMaintenanceId;
    private List<Date> scheduleMaintenanceTriggerDateList;
}
