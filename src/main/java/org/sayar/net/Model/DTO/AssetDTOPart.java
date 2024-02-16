package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Part.Part;

import java.util.List;

@Data
public class AssetDTOPart {
    private List<Part> partList;

    public static AssetDTOPart map(List<Part> partList) {
        AssetDTOPart assetDTOPart=new AssetDTOPart();
        assetDTOPart.setPartList(partList);
        return assetDTOPart;
    }
}
