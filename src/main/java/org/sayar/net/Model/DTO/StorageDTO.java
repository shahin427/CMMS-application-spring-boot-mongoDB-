package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.Location.Address.Address;
import org.sayar.net.Model.newModel.Storage;
import org.sayar.net.Tools.Print;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class StorageDTO {
    private String id;
    private String title;
    private String code;
    private Address address;
    private Asset asset;
    private Boolean hasChild;

    public static StorageDTO map(Storage storage, Asset asset) {

        StorageDTO storageDTO=new StorageDTO();
        storageDTO.setId(storage.getId());
        storageDTO.setTitle(storage.getTitle());
        storageDTO.setCode(storage.getCode());
        storageDTO.setAddress(storage.getAddress());
        storageDTO.setAsset(asset);
        storageDTO.setHasChild(storage.getHasChild());
        Print.print("storageDTO",storageDTO);
        return storageDTO;
    }
}
