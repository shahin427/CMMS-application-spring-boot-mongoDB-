package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class PersonnelFunctionGetAllDTO {
    private Date repairDate;
    private Long duration;
    private String assetId;
    private String assetName;
    private String workOrderId;
}
