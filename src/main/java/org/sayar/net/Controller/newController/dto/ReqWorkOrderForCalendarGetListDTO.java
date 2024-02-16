package org.sayar.net.Controller.newController.dto;

import lombok.Data;
import org.sayar.net.Enumes.AssetStatus;

import java.util.Date;

@Data
public class    ReqWorkOrderForCalendarGetListDTO {

    private Date startDate;
    private Date endDate;

}
