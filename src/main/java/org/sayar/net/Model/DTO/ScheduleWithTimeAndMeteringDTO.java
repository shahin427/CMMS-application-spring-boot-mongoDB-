package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduledMeteringCycle;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduledTime;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.model.ScheduleType;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Date;

@Data
public class ScheduleWithTimeAndMeteringDTO {

    private ScheduleType scheduleType;
    private ScheduledTime scheduledTime;
    private ScheduledMeteringCycle MeteringCycle;     //unitOfMeasurementName added


    public static ScheduleWithTimeAndMeteringDTO map(ScheduleMaintenance scheduleMaintenance, UnitOfMeasurement unitOfMeasurement) {

        ScheduleWithTimeAndMeteringDTO scheduleWithTimeAndMeteringDTO = new ScheduleWithTimeAndMeteringDTO();

//        if (scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduledTime().getCycle() != null) {
//            System.out.println("nullnull");
//            scheduleWithTimeAndMeteringDTO.setScheduledTime(scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduledTime());
//        }
//        scheduleWithTimeAndMeteringDTO.setScheduleType(scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduleType());
        scheduleWithTimeAndMeteringDTO.setMeteringCycle(scheduleMaintenance.getScheduleWithTimeAndMetering().getMeteringCycle());
        scheduleWithTimeAndMeteringDTO.getMeteringCycle().setUnitOfMeasurementId(unitOfMeasurement.getId());
        scheduleWithTimeAndMeteringDTO.getMeteringCycle().setRegistrationDate(new Date());
        Print.print("final", scheduleWithTimeAndMeteringDTO);

        return scheduleWithTimeAndMeteringDTO;

    }

    public static ScheduleWithTimeAndMeteringDTO map(ScheduleMaintenance scheduleMaintenance) {
        ScheduleWithTimeAndMeteringDTO scheduleWithTimeAndMeteringDTO = new ScheduleWithTimeAndMeteringDTO();
//        scheduleWithTimeAndMeteringDTO.setScheduleType(scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduleType());
        scheduleWithTimeAndMeteringDTO.setScheduledTime(scheduleMaintenance.getScheduleWithTimeAndMetering().getScheduledTime());
//        scheduleWithTimeAndMeteringDTO.setMeteringCycle(null);
        return scheduleWithTimeAndMeteringDTO;
    }
}
