package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.InventoryDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Inventory;
import org.sayar.net.Model.newModel.Part.Part;
import org.sayar.net.Model.newModel.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryServiceImpl extends GeneralServiceImpl<Inventory> implements InventoryService {

    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private PartService partService;

    @Autowired
    private StorageService storageService;

    @Override
    public Page<InventoryGetAllInPartDTO> getAllByPagination(Pageable pageable, Integer count, String partId) {
        return new PageImpl<>(
                inventoryDao.getAllByPagination(pageable, count, partId)
                , pageable
                , inventoryDao.countInventory(partId)
        );
    }

    @Override
    public InventoryLocationDTO postInventory(Inventory inventory) {
        Inventory savedInventory = inventoryDao.postInventory(inventory);
        partService.addOneToNumberOfInventoryOfPart(inventory.getPartId());
        Storage storage = storageService.getStorageNameById(savedInventory.getInventoryLocationId());
        storageService.addInventoryIdToStorage(savedInventory.getId(), savedInventory.getInventoryLocationId());
        return InventoryLocationDTO.map(savedInventory, storage);
    }

    @Override
    public boolean checkInventoryCode(String inventoryCode) {
        return inventoryDao.checkInventoryCode(inventoryCode);
    }

    @Override
    public boolean updateInventory(Inventory inventory) {
        return super.updateResultStatus(inventoryDao.updateInventory(inventory));
    }

    @Override
    public InventoryGetOneDTO getOneInventory(String inventoryId) {
        InventoryGetOneDTO inventoryGetOneDTO = inventoryDao.getOneInventory(inventoryId);

        if (inventoryGetOneDTO.getChargeDepartmentId().equals("")) {
            inventoryGetOneDTO.setChargeDepartmentId(null);
        }
        if (inventoryGetOneDTO.getBudgetId().equals("")) {
            inventoryGetOneDTO.setBudgetId(null);
        }
        return inventoryGetOneDTO;
    }

    @Override
    public boolean checkInventoryLocation(PartDTO3 partDTO3) {
        return inventoryDao.checkInventoryLocation(partDTO3);
    }

    @Override
    public Page<ChangedInventoryDTO> getAllPartDTO3(inventoryDTO1 inventoryDTO1, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                inventoryDao.getAllPartDTO3(inventoryDTO1, pageable, totalElement),
                pageable,
                inventoryDao.countList(inventoryDTO1)
        );
    }

    @Override
    public List<Inventory> getAllPartDTO4(PartDTO4 partDTO4) {
        return inventoryDao.getAllPartDTO4(partDTO4);
    }

    @Override
    public boolean deleteInventory(inventoryDTO1 inventoryDTO1) {
        return super.deleteResultStatus(inventoryDao.deleteInventory(inventoryDTO1));
    }

    @Override
    public Page<Inventory> getAllInventoryByTermAndPagination(Pageable pageable, String term) {
        return new PageImpl<>(
                inventoryDao.getAllInventoryByTermAndPagination(pageable, term)
                , pageable
                , inventoryDao.getAllInventoryTermCount(term)
        );
    }

    @Override
    public boolean updateListOfInventory(List<Inventory> inventory) throws UnknownHostException {
        return inventoryDao.updateListOfInventory(inventory);
    }

    @Override
    public void postListOfInventory(InventoryDTOForCurrentQuantity inventoryDTOForCurrentQuantity) {
        Inventory inventory = inventoryDao.getInventoryForInventoryAdjustment(inventoryDTOForCurrentQuantity.getInventoryId());
        inventoryDao.postListOfInventory(inventory, inventoryDTOForCurrentQuantity);
    }

    @Override
    public boolean checkIfPartHasInventory(String partId) {
        return inventoryDao.checkIfPartHasInventory(partId);
    }

    @Override
    public Page<InventoryGetAllDTO> getInventoryByPartNameAndInventoryLocationTitle(InventorySearchDTO inventorySearchDTO, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                inventoryDao.getInventoryByPartNameAndInventoryLocationTitle(inventorySearchDTO, pageable, totalElement),
                pageable,
                inventoryDao.countGetInventoryByPartNameAndInventoryLocationTitle(inventorySearchDTO)
        );
    }

    @Override
    public Page<InventoryDTO> getAllInventoryByGroup(Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                inventoryDao.getAllInventoryByGroup(pageable, totalElement)
                , pageable
                , inventoryDao.inventoryDaoCountAllInventoryByGroup()
        );
    }

    @Override
    public List<Inventory> getLowStockItems() {
        List<Inventory> inventoryList = inventoryDao.getAllInventory();
        return inventoryDao.getLowStockItems(inventoryList);
    }

    @Override
    public long countLowStockItems() {
        List<Inventory> inventoryList = inventoryDao.getAllInventory();
        return inventoryDao.countLowStockItems(inventoryList);
    }

    @Override
    public List<Inventory> getLOwStockItemsOfUser(String userId) {
        List<Part> assignedPartListsOfAUser = partService.getAssignedPartListsOfUser(userId);
        List<String> partNameList = new ArrayList<>();
        assignedPartListsOfAUser.forEach(part -> {
            partNameList.add(part.getName());
        });
        List<Inventory> inventoryListOfAUser = inventoryDao.getAllLowStocksItemOfAUser(partNameList);
        return inventoryDao.getLowStockItems(inventoryListOfAUser);
    }

    @Override
    public List<Inventory> getAllInventoriesWithStorage() {
        return inventoryDao.getAllInventoriesWithStorage();
    }

    @Override
    public List<Inventory> getAllInventory() {
        return inventoryDao.getAllInventory();
    }

    @Override
    public List<Inventory> getAllInventoriesOfStorageByStorageId(String storageId) {
        return inventoryDao.getAllInventoriesOfStorageByStorageId(storageId);
    }

    @Override
    public boolean ifBudgetExistsInInventory(String budgetId) {
        return inventoryDao.ifBudgetExistsInInventory(budgetId);
    }

    @Override
    public boolean ifChargeDepartmentExistsInInventory(String chargeDepartmentId) {
        return inventoryDao.ifChargeDepartmentExistsInInventory(chargeDepartmentId);
    }

    @Override
    public Inventory getDeletedInventory(String inventoryId) {
        return inventoryDao.getDeletedInventory(inventoryId);
    }

    @Override
    public void deleteSameDocumentsFromDatabase(Inventory deletedInventory) {
        inventoryDao.deleteSameDocumentsFromDatabase(deletedInventory);
    }

    @Override
    public boolean ifWarehouseHasPart(String storageId) {
        return inventoryDao.ifWarehouseHasPart(storageId);
    }

    @Override
    public List<Inventory> InventoryListByPartCode(List<String> partCodeList) {
        return inventoryDao.InventoryListByPartCode(partCodeList);
    }

}
