package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Inventory;
import org.sayar.net.Model.newModel.Storage;

@Data
public class InventoryLocationDTO {
    private String inventoryId;
    private String locationName;

    public static InventoryLocationDTO map(Inventory savedInventory, Storage storage) {
        InventoryLocationDTO inventoryLocationDTO = new InventoryLocationDTO();
        inventoryLocationDTO.setInventoryId(savedInventory.getId());
        inventoryLocationDTO.setLocationName(storage.getTitle());
        return inventoryLocationDTO;
    }
}
