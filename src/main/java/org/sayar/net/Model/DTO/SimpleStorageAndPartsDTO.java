package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.Part.Part;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

@Data
public class SimpleStorageAndPartsDTO {
    private Set<UniqueDTO> uniqueStorageList;
    private List<Part> partList;
    private List<Asset> assetList;

    public static SimpleStorageAndPartsDTO map(Set<UniqueDTO> uniqueStorageList, List<Part> partList, List<Asset> parentAssets) {

        SimpleStorageAndPartsDTO simpleStorageAndPartsDTO = new SimpleStorageAndPartsDTO();
        simpleStorageAndPartsDTO.setAssetList(parentAssets);
        simpleStorageAndPartsDTO.setPartList(partList);
        simpleStorageAndPartsDTO.setUniqueStorageList(uniqueStorageList);
        return simpleStorageAndPartsDTO;
    }
}
