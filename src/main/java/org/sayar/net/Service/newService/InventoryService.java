package org.sayar.net.Service.newService;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface InventoryService extends GeneralService<Inventory> {

    Page<InventoryGetAllInPartDTO> getAllByPagination(Pageable pageable, Integer count, String partId);

    InventoryLocationDTO postInventory(Inventory inventory);

    boolean checkInventoryCode(String inventoryCode);

    boolean updateInventory(Inventory inventory);

    InventoryGetOneDTO getOneInventory(String inventoryId);

    boolean checkInventoryLocation(PartDTO3 partDTO3);

    Page<ChangedInventoryDTO> getAllPartDTO3(inventoryDTO1 inventoryDTO1, Pageable pageable, Integer totalElement);

    List<Inventory> getAllPartDTO4(PartDTO4 partDTO4);

    boolean deleteInventory(inventoryDTO1 inventoryDTO1);

    Page<Inventory> getAllInventoryByTermAndPagination(Pageable pageable, String term);

    boolean updateListOfInventory(List<Inventory> inventory) throws UnknownHostException;

    void postListOfInventory(InventoryDTOForCurrentQuantity inventoryDTOForCurrentQuantity);

    boolean checkIfPartHasInventory(String partId);

    Page<InventoryGetAllDTO> getInventoryByPartNameAndInventoryLocationTitle(InventorySearchDTO inventorySearchDTO,Pageable pageable,Integer totalElement);

    Page<InventoryDTO> getAllInventoryByGroup(Pageable pageable,Integer totalElement);

    List<Inventory> getLowStockItems();

    long countLowStockItems();

    List<Inventory> getLOwStockItemsOfUser(String userId);

    List<Inventory> getAllInventoriesWithStorage();

    List<Inventory> getAllInventory();

    List<Inventory> getAllInventoriesOfStorageByStorageId(String storageId);

    boolean ifBudgetExistsInInventory(String budgetId);

    boolean ifChargeDepartmentExistsInInventory(String chargeDepartmentId);

    Inventory getDeletedInventory(String inventoryId);

    void deleteSameDocumentsFromDatabase(Inventory deletedInventory);

    boolean ifWarehouseHasPart(String storageId);

    List<Inventory> InventoryListByPartCode(List<String> partCodeList);
}
