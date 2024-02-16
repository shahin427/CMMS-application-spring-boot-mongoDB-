package org.sayar.net.Controller.newController;

import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Inventory;
import org.sayar.net.Service.newService.InventoryService;
import org.sayar.net.Service.newService.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.net.UnknownHostException;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PartService partService;

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllByPagination(Pageable pageable, Integer count, @PathParam("partId") String partId) {
        return ResponseEntity.ok().body(inventoryService.getAllByPagination(pageable, count, partId));
    }

    @PostMapping("save")
    public ResponseEntity<?> postInventory(@RequestBody Inventory inventory) {
        return ResponseEntity.ok().body(inventoryService.postInventory(inventory));
    }

    @GetMapping("check-inventory-code")
    public ResponseEntity<?> checkInventoryCode(@PathParam("inventoryCode") String inventoryCode) {
        if (inventoryCode == null) {
            return ResponseEntity.ok().body("\"کد موجودی فرستاده نشده است\"");
        }
        return ResponseEntity.ok().body(inventoryService.checkInventoryCode(inventoryCode));
    }

    @DeleteMapping("delete-inventory")
    public ResponseEntity<?> deleteInventory(@PathParam("inventoryId") String inventoryId) {
        Inventory deletedInventory = inventoryService.getDeletedInventory(inventoryId);
        partService.decreaseNumberOfInventoryInPart(deletedInventory.getPartId());
        inventoryService.deleteSameDocumentsFromDatabase(deletedInventory);
        return ResponseEntity.ok().body(inventoryService.logicDeleteById(inventoryId, Inventory.class));
    }

    @PutMapping("update-inventory")
    public ResponseEntity<?> updateInventory(@RequestBody Inventory inventory) {
        return ResponseEntity.ok().body(inventoryService.updateInventory(inventory));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneInventory(@PathParam("inventoryId") String inventoryId) {
        return ResponseEntity.ok().body(inventoryService.getOneInventory(inventoryId));
    }

    @PutMapping("check-inventory-location")
    public ResponseEntity<?> checkInventoryLocation(@RequestBody PartDTO3 partDTO3) {
        return ResponseEntity.ok().body(inventoryService.checkInventoryLocation(partDTO3));
    }

    @PostMapping("get-all-users-who-have-changed-inventory")
    public ResponseEntity<?> getAllUsersWhoHaveChangedInventory(@RequestBody inventoryDTO1 inventoryDTO1, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(inventoryService.getAllPartDTO3(inventoryDTO1, pageable, totalElement));
    }

    @GetMapping
    public ResponseEntity<?> getAllPartDTO4(@RequestBody PartDTO4 partDTO4) {
        return ResponseEntity.ok().body(inventoryService.getAllPartDTO4(partDTO4));
    }

//    @PostMapping("delete-inventory")
//    public ResponseEntity<?> deleteAsset(@RequestBody inventoryDTO1 inventoryDTO1) {
//        return ResponseEntity.ok().body(inventoryService.deleteInventory(inventoryDTO1));
//    }

    @GetMapping("get-all-current-inventory-by-pagination")
    public ResponseEntity<?> getAllInventoryByTermAndPagination(Pageable pageable, @PathParam("term") String term) {
        return ResponseEntity.ok().body(inventoryService.getAllInventoryByTermAndPagination(pageable, term));
    }

    @PutMapping("update-list-of-inventory")
    public ResponseEntity<?> updateListOfInventory(@RequestBody List<Inventory> inventory) throws UnknownHostException {
        return ResponseEntity.ok().body(inventoryService.updateListOfInventory(inventory));
    }

    @PostMapping("post-list-of-inventory")
    public void postListOfInventory(@RequestBody InventoryDTOForCurrentQuantity inventoryDTOForCurrentQuantity) {
        inventoryService.postListOfInventory(inventoryDTOForCurrentQuantity);
    }

    @GetMapping("check-if-part-has-inventory")
    public ResponseEntity<?> checkIfPartHasInventory(@PathParam("partId") String partId) {
        return ResponseEntity.ok().body(inventoryService.checkIfPartHasInventory(partId));
    }

    @PostMapping("get-inventory-by-part-name-and-part-code")
    public ResponseEntity<?> getInventoryByPartNameAndInventoryLocationTitle(@RequestBody InventorySearchDTO inventorySearchDTO, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(inventoryService.getInventoryByPartNameAndInventoryLocationTitle(inventorySearchDTO, pageable, totalElement));
    }

    @GetMapping("get-all-inventory-by-group")
    public ResponseEntity<?> getAllInventoryByGroup(Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(inventoryService.getAllInventoryByGroup(pageable, totalElement));
    }

    @GetMapping("get-all-low-stock-items")
    public ResponseEntity<?> getLowStockItems() {
        return ResponseEntity.ok().body(inventoryService.getLowStockItems());
    }

    @GetMapping("count-low-stock-items")
    public ResponseEntity<?> countLowStockItems() {
        return ResponseEntity.ok().body(inventoryService.countLowStockItems());
    }

    @GetMapping("get-low-stuck-items-of-user")
    public ResponseEntity<?> getLOwStuckItems(@PathParam("userId") String userId) {
        return ResponseEntity.ok().body(inventoryService.getLOwStockItemsOfUser(userId));
    }

    @GetMapping("get-all-inventory-of-storage-by-storageId")
    public ResponseEntity<?> getAllInventoriesOfStorageByStorageId(@PathParam("storageId") String storageId) {
        return ResponseEntity.ok().body(inventoryService.getAllInventoriesOfStorageByStorageId(storageId));
    }
}
