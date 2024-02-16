package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Inventory;
import org.springframework.data.domain.Pageable;

import java.net.UnknownHostException;
import java.util.List;

public interface InventoryDao extends GeneralDao {

    List<InventoryGetAllInPartDTO> getAllByPagination(Pageable pageable, Integer count, String partId);

    long getAllInventoryCount(String partId);

    Inventory postInventory(Inventory inventory);

    boolean checkInventoryCode(String inventoryCode);

    UpdateResult updateInventory(Inventory inventory);

    InventoryGetOneDTO getOneInventory(String inventoryId);

    boolean checkInventoryLocation(PartDTO3 partDTO3);

    List<ChangedInventoryDTO> getAllPartDTO3(inventoryDTO1 inventoryDTO1,Pageable pageable,Integer totalElement);

    List<Inventory> getAllPartDTO4(PartDTO4 partDTO4);

    DeleteResult deleteInventory(inventoryDTO1 inventoryDTO1);

    List<Inventory> getAllInventoryByTermAndPagination(Pageable pageable,String term);

    long getAllInventoryTermCount(String term);

    boolean updateListOfInventory(List<Inventory> inventory) throws UnknownHostException;

    void postListOfInventory(Inventory inventory,InventoryDTOForCurrentQuantity inventoryDTOForCurrentQuantity) ;

    boolean checkIfPartHasInventory(String partId);

    List<InventoryGetAllDTO> getInventoryByPartNameAndInventoryLocationTitle(InventorySearchDTO inventorySearchDTO,Pageable pageable,Integer totalElement);

    long countGetInventoryByPartNameAndInventoryLocationTitle(InventorySearchDTO inventorySearchDTO);

    List<InventoryDTO> getAllInventoryByGroup(Pageable pageable,Integer totalElement);

   long inventoryDaoCountAllInventoryByGroup();

    List<Inventory> getLowStockItems(List<Inventory> inventoryList);

    List<Inventory> getAllInventory();

    long countLowStockItems(List<Inventory> inventoryList);

    List<Inventory> getAllLowStocksItemOfAUser(List<String> partNameList);

    List<Inventory> getAllInventoriesWithStorage();

    List<Inventory> getAllInventoriesOfStorageByStorageId(String storageId);

    boolean ifBudgetExistsInInventory(String budgetId);

    boolean ifChargeDepartmentExistsInInventory(String chargeDepartmentId);

    Inventory getDeletedInventory(String inventoryId);

    void deleteSameDocumentsFromDatabase(Inventory deletedInventory);

    boolean ifWarehouseHasPart(String storageId);

    List<Inventory> InventoryListByPartCode(List<String> partCodeList);

    long countList(inventoryDTO1 inventoryDTO1);

    Inventory getInventoryForInventoryAdjustment(String inventoryId);

    long countInventory(String partId);
}
