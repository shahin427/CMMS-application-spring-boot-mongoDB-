package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;

@Data
public class AssetDTOWithParentAsset {
    private Asset mainAsset;
    private Asset parentAsset;

    public static AssetDTOWithParentAsset map(Asset asset, Asset parentAsset) {
        AssetDTOWithParentAsset assetDTOWithParentAsset=new AssetDTOWithParentAsset();
        assetDTOWithParentAsset.setMainAsset(asset);
        assetDTOWithParentAsset.setParentAsset(parentAsset);
        return assetDTOWithParentAsset;
    }
}
