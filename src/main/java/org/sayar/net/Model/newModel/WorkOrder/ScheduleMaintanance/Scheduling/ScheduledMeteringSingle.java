package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.Compare;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.springframework.data.annotation.Id;

//import javax.persistence.*;


@Data
public class ScheduledMeteringSingle {
    @Id
    private String id;
    private int per;
    private Compare compare;
    private UnitOfMeasurement unit;

}
