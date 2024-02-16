package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Inventory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ChangedInventoryDTO {
    private String userName;
    private String userFamily;
    private String userTypeName;
    private Long previousQuantity;
    private Long currentQuantity;
    private Date creationDate;

    public static List<ChangedInventoryDTO> map(List<Inventory> inventoryList) {
        List<ChangedInventoryDTO> changedInventoryDTOList = new ArrayList<>();
        inventoryList.forEach(inventory -> {
            ChangedInventoryDTO changedInventoryDTO = new ChangedInventoryDTO();
            changedInventoryDTO.setCreationDate(inventory.getCreationDate());
            changedInventoryDTO.setCurrentQuantity(inventory.getCurrentQuantity());
            changedInventoryDTO.setPreviousQuantity(inventory.getPreviousQuantity());
            changedInventoryDTOList.add(changedInventoryDTO);
        });
            return changedInventoryDTOList;
    }
}
