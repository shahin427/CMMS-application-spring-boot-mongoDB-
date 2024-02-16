package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Budget;
import org.sayar.net.Model.Asset.ChargeDepartment;
import org.sayar.net.Model.newModel.Storage;

import java.util.Date;

@Data
public class InventoryDTO {
    private String row;
    private String warehouse;
    private String corridor;
    private String partId;
    private String inventoryLocationName;
    private String currentQuantity;
    private String minQuantity;
    private String partName;
    private String previousQuantity;
    private String partCode;
}

