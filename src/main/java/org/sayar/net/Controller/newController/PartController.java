package org.sayar.net.Controller.newController;


import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Part.Part;
import org.sayar.net.Service.newService.BOMService;
import org.sayar.net.Service.newService.PartService;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Controller
@RequestMapping("/part")
public class PartController {

    @Autowired
    private PartService partService;

    @Autowired
    private PartWithUsageCountController partWithUsageCountController;

    @Autowired
    private BOMService bomService;

    @PostMapping("save")
    public ResponseEntity<?> postPart(@RequestBody Part part) {
        return ResponseEntity.ok().body(partService.postPart(part));
    }

    @PostMapping("save-inventory")
    public ResponseEntity<?> postPartInventory(@RequestBody PartDTO partDTO, @RequestParam("partId") String PartId) {
        return ResponseEntity.ok().body(partService.postPartInventory(partDTO, PartId));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOnePArt(@PathParam("partId") String partId) {
        return ResponseEntity.ok().body(partService.getOnePart(partId));
    }

    @GetMapping("get-one-private")
    public ResponseEntity<?> getPrivateSideOfPart(@PathParam("partCode") String partId) {
        return ResponseEntity.ok().body(partService.getPrivateSideOfPart(partId));
    }

    @GetMapping("get-all-private")
    public ResponseEntity<?> getAllPrivateSideOfPart() {
        return ResponseEntity.ok().body(partService.getAllPrivateSideOfPart());
    }

    @GetMapping("get-all-general")
    public ResponseEntity<?> getAllGeneralSideOfPart() {
        return ResponseEntity.ok().body(partService.getAllGeneralSideOfPart());
    }

    @GetMapping("get-one-general")
    public ResponseEntity<?> getOneGeneralSideOfPart(@PathParam("partId") String partId) {
        return ResponseEntity.ok().body(partService.getOneGeneralSideOfPart(partId));
    }
    @GetMapping("get-list-by-idList")
    public ResponseEntity<?> getAllByIdList(@PathParam("idList") List<String> idList) {
        return ResponseEntity.ok().body(partService.getAllByIdList(idList));
    }

    @GetMapping("check-part-code")
    public ResponseEntity<?> checkIfCodeExists(@PathParam("partCode") String partCode) {
        return ResponseEntity.ok().body("{\"exist\":" + partService.checkIfCodeExist(partCode) + "}");
    }

    @GetMapping("check-inventory-code")
    public ResponseEntity<?> checkIfInventoryCodeExists(@PathParam("inventoryCode") String inventoryCode) {
        return ResponseEntity.ok().body("{\"exist\":" + partService.checkIfInventoryCodeExists(inventoryCode) + "}");
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllPartsByFilterAndPagination(@PathParam("term") String term, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(partService.getAllPartsByFilterAndPagination(term, pageable, totalElement));
    }

    @DeleteMapping("delete-part")
    public ResponseEntity<?> deleteAllPart(@PathParam("partId") String partId) {
        if (partId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آیدی فرستاده نمیشود", httpStatus);
        } else {
            if (partWithUsageCountController.getUsedPartsInWorkOrders(partId)) {
                return ResponseEntity.ok().body("{\"برای خذف این قطعه ابتدا آن را از قسمت سفارش کار و زمانبندی برنامه ریزی شده پاک کنید\"");
            } else if (bomService.ifPartExistsInBOM(partId)) {
                return ResponseEntity.ok().body("{\"برای خذف این قطعه ابتدا آن را از گروه بندی ساز پاک کنید\"");
            } else {
                return ResponseEntity.ok().body(partService.logicDeleteById(partId, Part.class));
            }
        }
    }

    @PutMapping("update-up-side")
    public ResponseEntity<?> updateUpSide(@RequestBody PartPrivateDTO partPrivateDTO, @PathParam("partId") String partId) {
        return ResponseEntity.ok().body(partService.updateUpSide(partPrivateDTO, partId));
    }

    @PutMapping("update-down-side")
    public ResponseEntity<?> updateDownSide(@RequestBody PartGeneralDTO partGeneralDTO, @PathParam("partId") String partId) {
        return ResponseEntity.ok().body(partService.updateDownSide(partGeneralDTO, partId));
    }

    @PutMapping("update-user-list-by-part-id")
    public ResponseEntity<?> updateUserListByPartId(@RequestBody List<String> user, @RequestParam("partId") String partId) {

        return ResponseEntity.ok().body(partService.updateUserListByPartId(user, partId));
    }

    @GetMapping("get-user-list-by-part-id")
    public ResponseEntity<?> getUsersListByPartId(@PathParam("partId") String partId) {
        if (partId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آیدی فرستاده نشده است", httpStatus);
        } else {
            return ResponseEntity.ok().body(partService.getUsersListByPartId(partId));
        }
    }

    @PostMapping("get-all-assigned-parts-of-user-by-user-id")
    public ResponseEntity<?> getAllAssignedUsersOfPartByUserId(@RequestBody PartDtoForSearch partDtoForSearch, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(partService.getAllAssignedUsersOfPartByUserId(partDtoForSearch, pageable, totalElement));
    }

    @PostMapping("get-all-parts-with-out-inventory-and-loaded-storage")
    public ResponseEntity<?> getAllPartsWithOutInventoryAndLoadedInventories(@RequestBody PartDtoForSearch partDtoForSearch, Pageable pageable, Integer element) {
        return ResponseEntity.ok().body(partService.getAllPartsWithOutInventoryAndLoadedInventories(partDtoForSearch, pageable, element));
    }

    @GetMapping("get-all-parts")
    public ResponseEntity<?> getAllParts() {
        return ResponseEntity.ok().body(partService.getAllParts());
    }

    @GetMapping("get-person-personnel-of-part")
    public ResponseEntity<?> getPersonsOfPart(@PathParam("partId") String partId) {
        return ResponseEntity.ok().body(partService.getPersonsOfPart(partId));
    }

    @GetMapping("get-group-personnel-of-part")
    public ResponseEntity<?> getGroupPersonnelOfPart(@PathParam("partId") String partId) {
        return ResponseEntity.ok().body(partService.getGroupPersonnelOfPart(partId));
    }


    @PutMapping("add-person-type-personnel")
    public ResponseEntity<?> addPersonTypePersonnel(@PathParam("partId") String partId, @RequestBody List<AssignedToPerson> assignedToPersonList) {
        return ResponseEntity.ok().body(partService.addPersonTypePersonnel(partId, assignedToPersonList));
    }

    @PutMapping("add-group-type-personnel")
    public ResponseEntity<?> addGroupTypePersonnel(@PathParam("partId") String partId, @RequestBody List<AssignedToGroup> assignedToGroupList) {
        return ResponseEntity.ok().body(partService.addGroupTypePersonnel(partId, assignedToGroupList));
    }

    @PostMapping("get-all-assigned-parts-of-group")
    public ResponseEntity<?> getAllAssignedPartsOfGroup(@RequestBody PartDtoForSearch partDtoForSearch, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(partService.getAllAssignedPartsOfGroup(partDtoForSearch, pageable, totalElement));
    }
}