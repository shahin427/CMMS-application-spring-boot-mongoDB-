package org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Enum.ScheduledFixType;
import org.sayar.net.Model.newModel.Enum.TimeCycle;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduledTime {
    @Id
    private String id;
    private Integer per;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endOn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startOn;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date lastModify;
    private TimeCycle cycle;
    private ScheduledFixType fixType;
}
