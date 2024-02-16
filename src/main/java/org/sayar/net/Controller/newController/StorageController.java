package org.sayar.net.Controller.newController;

import org.sayar.net.Model.newModel.Storage;
import org.sayar.net.Service.newService.InventoryService;
import org.sayar.net.Service.newService.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private InventoryService inventoryService;


    @PostMapping("save")
    public boolean postStorage(@RequestBody Storage storage) {
        return storageService.postStorage(storage);
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllStorage() {
        return ResponseEntity.ok().body(storageService.getAllStorage());
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneStorage(@PathParam("storageId") String storageId) {
        return ResponseEntity.ok().body(storageService.getOneStorage(storageId));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteStorage(@PathParam("storageId") String storageId) {
        if (inventoryService.ifWarehouseHasPart(storageId)) {
            return ResponseEntity.ok().body("\"برای حذف این انبار ابتدا قطعات موجود در آن را حذف کنید\"");
        } else {
            return ResponseEntity.ok().body(storageService.logicDeleteById(storageId, Storage.class));
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> updateStorage(@RequestBody Storage storage, @PathParam("storageId") String storageId) {
        return ResponseEntity.ok().body(storageService.updateStorage(storage, storageId));
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllByFilterAndPagination(@PathParam("term") String term, @PathParam("code") String code, @PathParam("assetId") String assetId, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(storageService.getAllByFilterAndPagination(term, code, assetId, pageable, totalElement));
    }

    @GetMapping("check-storage-code")
    public ResponseEntity<?> checkIfStorageCodeExists(@PathParam("storageCode") String storageCode) {
        return ResponseEntity.ok().body(storageService.checkIfStorageCodeExists(storageCode));
    }

    @GetMapping("get-all-storage-of-asset-has-parts")
    public ResponseEntity<?> getAllStorageOfAssetHasParts(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(storageService.getAllStorageOfAssetHasParts(assetId));
    }

    @GetMapping("check-if-code-is-unique")
    public ResponseEntity<?> checkIfCodeIsUnique(@PathParam("code") String code) {
        return ResponseEntity.ok().body(storageService.checkIfCodeIsUnique(code));
    }
}
