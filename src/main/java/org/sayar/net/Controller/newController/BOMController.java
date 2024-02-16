package org.sayar.net.Controller.newController;

import org.sayar.net.Enumes.ResponseKeyWord;
import org.sayar.net.Model.DTO.AssetBOMDTO;
import org.sayar.net.Model.DTO.BOMDTO;
import org.sayar.net.Model.DTO.BOMNameDTO;
import org.sayar.net.Model.DTO.PartBOMDTO;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Service.newService.BOMService;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/bill-of-materials-group")
public class BOMController {
    @Autowired
    private BOMService bomService;

    @PostMapping("save")
    public ResponseEntity<?> createBOM(@RequestBody BOMNameDTO bomdto) {
        return ResponseEntity.ok().body(bomService.createBOM(bomdto));
    }

    @PutMapping("update-first-page")
    public ResponseEntity<?> updateFirstPage(@RequestBody BOMNameDTO bomNameDTO) {
        return ResponseEntity.ok().body(bomService.updateFirstPage(bomNameDTO));
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllBOMByPagination(@PathParam("term") String name, @PathParam("code") String code, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(bomService.getAllBOMByPagination(name, code, pageable, totalElement));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> removeBOM(@PathParam("BOMId") String BOMId) {
        return ResponseEntity.ok().body(bomService.logicDeleteById(BOMId, BOM.class));
    }

    @PutMapping("update-part-bom")
    public ResponseEntity<?> updatePartBOM(@RequestBody List<BOMPart> partBOMDTOList, @PathParam("id") String id) {
        if (id == null) {
            return new ResponseContent().sendErrorResponseEntity("آی دی از سمت کلاینت فرستاده نمیشود", ResponseKeyWord.INVALID_INPUT, 406);
        } else {
            return ResponseEntity.ok().body(bomService.updateBOM(partBOMDTOList, id));
        }
    }

    @PutMapping("update-asset-bom")
    public ResponseEntity<?> updateAssetBOM(@RequestBody List<AssetBOMDTO> assetBOMDTOList, @PathParam("id") String id) {
        if (id == null) {
            return new ResponseContent().sendErrorResponseEntity("آی دی از سمت کلاینت فرستاده نمیشود", ResponseKeyWord.INVALID_INPUT, 406);
        } else {
            return ResponseEntity.ok().body(bomService.updateAssetBOM(assetBOMDTOList, id));
        }
    }

    @DeleteMapping("delete-part-list")
    public ResponseEntity<?> removeBOMPartList(@RequestParam("BOMId") String BOMId, @RequestParam("BOMPartId") String BOMPartId) {
        return ResponseEntity.ok().body(bomService.removePartOfBOM(BOMId, BOMPartId));
    }

    @DeleteMapping("delete-asset-list")
    public ResponseEntity<?> deleteAssetListOfBom(@PathParam("BOMId") String BOMId, @PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(bomService.removeAssetOfBOM(BOMId, assetId));
    }

    @GetMapping("get-all-bom-part")
    public ResponseEntity<?> getAllBOMPart(Pageable pageable) {
        return ResponseEntity.ok().body(bomService.getAllBOMPart(pageable));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneBOM(@PathParam("BOMId") String BOMId) {
        if (BOMId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("ورودی فرستاده نمیشود", httpStatus);
        } else {
            return ResponseEntity.ok().body(bomService.getOneBOM(BOMId));
        }
    }

    @GetMapping("get-all-part-by-bomId-with-pagination")
    public ResponseEntity<?> getAllPartByPagination(@PathParam("BOMId") String BOMId, Pageable pageable) {
        return ResponseEntity.ok().body(bomService.getAllPartByPagination(BOMId, pageable));
    }

    @GetMapping("get-all-asset-by-bomId-with-pagination")
    public ResponseEntity<?> getAllAssetByPagination(@PathParam("BOMId") String BOMId, Pageable pageable) {
        return ResponseEntity.ok().body(bomService.getAllAssetByPagination(BOMId, pageable));
    }

    @GetMapping("check-bom-code")
    public ResponseEntity<?> checkIfBomExists(@PathParam("BOMCode") String BOMCode) {
        return ResponseEntity.ok().body(bomService.checkIfBomExists(BOMCode));
    }

}
