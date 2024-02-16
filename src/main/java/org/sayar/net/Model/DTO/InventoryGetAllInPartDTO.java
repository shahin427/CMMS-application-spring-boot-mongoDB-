package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class InventoryGetAllInPartDTO {
    private String inventoryId;
    private String row;
    private String corridor;
    private String warehouse;
    private String inventoryLocationName;
    private String currentQuantity;
    private String minQuantity;
    private String inventoryCode;
    private String previousQuantity;
}
