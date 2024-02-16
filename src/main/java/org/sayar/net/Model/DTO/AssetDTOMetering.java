package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.metering.model.Metering;

import java.util.List;

@Data
public class AssetDTOMetering {
    private String id;
    private List<Metering> meteringList;

    public static AssetDTOMetering map(Asset asset, List<Metering> meteringList) {
        AssetDTOMetering assetDTOMetering=new AssetDTOMetering();
        assetDTOMetering.setId(asset.getId());
        assetDTOMetering.setMeteringList(meteringList);
        return assetDTOMetering;
    }
}
