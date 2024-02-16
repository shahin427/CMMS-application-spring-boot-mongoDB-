package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class InventoryDTOForCurrentQuantity {
    private long currentQuantity;
    private String inventoryId;
    private String userId;
}
