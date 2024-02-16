package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.Part.Part;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

@Data
public class StorageAndPartsDTO {
    private Set<UniqueDTO> uniqueStorageList;
    private Page<Part> partList;
    private List<Asset> assetList;

    public static StorageAndPartsDTO map(Set<UniqueDTO> uniqueStorageList, Page<Part> partList, List<Asset> parentAssets) {
        StorageAndPartsDTO storageAndPartsDTO = new StorageAndPartsDTO();
        storageAndPartsDTO.setPartList(partList);
        storageAndPartsDTO.setUniqueStorageList(uniqueStorageList);
        storageAndPartsDTO.setAssetList(parentAssets);
        return storageAndPartsDTO;
    }
}

