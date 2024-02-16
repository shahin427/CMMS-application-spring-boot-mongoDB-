package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.UnitOfMeasurement;

import java.util.Date;

@Data
public class PrimaryAssetMeteringDTO {
    private String meteringId;
    private String assetId;
    private String assetName;
    private UnitOfMeasurement unitOfMeasurement;
    private long amount;
    private String userId;
    private String userName;
    private Date creationDate;
}
