package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.CategoryType;

@Data
public class BOMAssetDTO {
    private String assetId;
    private String assetName;
    private String assetCode;
    private String assetQuantity;
}
