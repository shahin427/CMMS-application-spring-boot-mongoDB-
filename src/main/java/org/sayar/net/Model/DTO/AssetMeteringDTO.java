package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class AssetMeteringDTO {
    private String meteringId;
    private String assetId;
    private String assetName;
    private String unitOfMeasurementName;
    private String unitOfMeasurementId;
    private long amount;
    private String userId;
    private String userName;
    private String userFamily;
    private Date creationDate;
}
