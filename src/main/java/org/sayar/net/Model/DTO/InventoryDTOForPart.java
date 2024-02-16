package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Storage;

@Data
public class InventoryDTOForPart {
    private String id;
    private String code;
    private Storage inventoryLocation;
    private String row;
    private String corridor;
    private String warehouse;
}
