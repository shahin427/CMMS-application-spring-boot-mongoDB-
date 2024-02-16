package org.sayar.net.Controller.newController;

import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.newModel.Warranty;
import org.sayar.net.Service.newService.WarrantyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("warranty")
public class WarrantyController {

    private final WarrantyService warrantyService;

    @Autowired
    public WarrantyController(WarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }

    @PostMapping("save")
    public ResponseEntity<?> postOneWarranty(@RequestBody Warranty warranty) {
        return ResponseEntity.ok().body(warrantyService.postOneWarranty(warranty));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneWarranty(@PathParam("warrantyId") String warrantyId) {
        return ResponseEntity.ok().body(warrantyService.getOneWarranty(warrantyId));
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteWarranty(@PathParam("warrantyId") String warrantyId) {
        return ResponseEntity.ok().body(warrantyService.logicDeleteById(warrantyId, Warranty.class));
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllWarrantyByPagination(Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(warrantyService.getAllWarrantyByPagination(pageable, totalElement));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateWarranty(@RequestBody Warranty warranty) {
        return ResponseEntity.ok().body(warrantyService.updateWarranty(warranty));
    }

    @GetMapping("get-all-company")
    public ResponseEntity<?> getAllCompany() {
        return ResponseEntity.ok().body(warrantyService.getAllCompany());
    }

    @GetMapping("get-all-warranty-type")
    public ResponseEntity<?> getAllWarrantyType() {
        return ResponseEntity.ok().body(warrantyService.getAllWarrantyType());
    }

    @GetMapping("get-all-measurement")
    public ResponseEntity<?> getAllWarrantyMeasurement() {
        return ResponseEntity.ok().body(warrantyService.getAllWarrantyMeasurement());
    }

    @GetMapping("get-all-by-asset-id")
    public ResponseEntity<?> getAllPaginationByAssetId(@PathParam("assetId") String assetId, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(warrantyService.getAllPaginationByAssetId(assetId, pageable, totalElement));
    }

    @GetMapping("get-all-by-pagination-part-id")
    public ResponseEntity<?> getAllWarrantyByPartId(@PathParam("partId") String partId, @PathParam("assetId") String assetId, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(warrantyService.getAllWarrantyByPartId(partId, assetId, pageable, totalElement));
    }

    @GetMapping("check-if-code-exists")
    public ResponseEntity<?> checkIfCodeExists(@PathParam("code") String code) {
        return ResponseEntity.ok().body(warrantyService.checkIfCodeExists(code));
    }


    //__________________________________________________
    @GetMapping("by-id-list")
    public ResponseEntity<?> getByIdList(@RequestBody List<String> idList) {
        return new ResponseContent().sendOkResponseEntity("", warrantyService.getByIdList(idList));
    }

    @RequestMapping(method = RequestMethod.GET, value = "get-one1")
    public ResponseEntity<?> findOne(@PathParam("warranty") String warranty) {
        return new ResponseContent().sendOkResponseEntity("", warrantyService.findOneById(warranty, Warranty.class));
    }

    @RequestMapping(method = RequestMethod.GET, value = "get-all1")
    public ResponseEntity<?> getAll() {
        return new ResponseContent().sendOkResponseEntity("", warrantyService.findAllNotLogicDeleted(Warranty.class));
    }

    @RequestMapping(method = RequestMethod.POST, value = "save1")
    public ResponseEntity<?> save(@RequestBody Warranty warranty) {
        return new ResponseContent().sendOkResponseEntity("", warrantyService.save(warranty));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "update1")
    public ResponseEntity<?> update(@RequestBody Warranty warranty) {
        return new ResponseContent().sendOkResponseEntity("", warrantyService.save(warranty));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "delete1")
    public ResponseEntity<?> delete(@PathParam("warranty") String warranty) {
        return new ResponseContent().sendOkResponseEntity("", warrantyService.logicDeleteById(warranty, Warranty.class));
    }


}
