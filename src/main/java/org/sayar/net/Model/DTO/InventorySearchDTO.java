package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class InventorySearchDTO {
    private String partName;
    private String corridor;
    private String row;
    private String warehouse;
    private String partCode;
    private String inventoryLocation;
    private String inventoryCode;
}
