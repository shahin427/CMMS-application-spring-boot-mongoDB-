package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Inventory;

import java.util.List;

@Data
public class InventoryListDTO {
    private List<Inventory> inventoryList;
}
