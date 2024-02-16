package org.sayar.net.Controller.newController.dto;

import lombok.Data;
import org.sayar.net.Enumes.AssetStatus;

import java.util.Date;
import java.util.List;

@Data
public class ResWorkOrderForCalendarGetListDTO {

    private String id;

    private String assetId;
    private String assetName;
    private Date endDate;
    private List<String> userIdList;

}
