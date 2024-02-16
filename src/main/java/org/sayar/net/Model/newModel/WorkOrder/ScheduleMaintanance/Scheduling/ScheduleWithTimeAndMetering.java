package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mongodb.lang.Nullable;
import lombok.Data;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.model.ScheduleType;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleWithTimeAndMetering {
    @Nullable
    private ScheduleType scheduleType;
    @Nullable
    private ScheduledTime scheduledTime;
    @Nullable
    private ScheduledMeteringCycle MeteringCycle;

    public ScheduleWithTimeAndMetering() {

    }

    public ScheduleWithTimeAndMetering(ScheduledMeteringCycle meteringCycle) {
        this.MeteringCycle = meteringCycle;
    }

    public static ScheduleWithTimeAndMetering map(ScheduleMaintenance scheduleMaintenance) {
        ScheduleWithTimeAndMetering scheduleWithTimeAndMetering = new ScheduleWithTimeAndMetering();
        if (scheduleMaintenance.getScheduleWithTimeAndMetering() != null) {
            scheduleWithTimeAndMetering.setScheduleType(scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduleType());
            scheduleWithTimeAndMetering.setScheduledTime(scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduledTime());
            scheduleWithTimeAndMetering.setMeteringCycle(scheduleMaintenance.getScheduleWithTimeAndMetering().getMeteringCycle());
            return scheduleWithTimeAndMetering;
        } else
            return null;
    }
}
