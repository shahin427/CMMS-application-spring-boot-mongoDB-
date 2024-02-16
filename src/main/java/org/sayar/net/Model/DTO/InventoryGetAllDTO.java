package org.sayar.net.Model.DTO;

 import lombok.Data;
import org.sayar.net.Model.newModel.Storage;

@Data
public class InventoryGetAllDTO {
    private String inventoryId;
    private String row;
    private String corridor;
    private String warehouse;
    private Storage inventoryLocation;
    private String currentQuantity;
    private String minQuantity;
    private String partId;
    private String partName;
    private String partCode;
    private String inventoryCode;
    private String previousQuantity;
}
