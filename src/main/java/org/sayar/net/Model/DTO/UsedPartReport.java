package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UsedPartReport {
    private String workOrderId;
    private String assetId;
    private String assetName;
    private Date usedTime;
    private String usedNumber;
}
