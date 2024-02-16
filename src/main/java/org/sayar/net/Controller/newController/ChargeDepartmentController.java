package org.sayar.net.Controller.newController;

import org.sayar.net.Model.Asset.ChargeDepartment;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Security.TokenPrinciple;
import org.sayar.net.Service.newService.AssetService;
import org.sayar.net.Service.newService.ChargeDepartmentService;
import org.sayar.net.Service.newService.InventoryService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("charge-department")
public class ChargeDepartmentController {
    private final ChargeDepartmentService service;

    private final TokenPrinciple tokenPrinciple;

    private final AssetService assetService;

    private final WorkOrderService workOrderService;

    private final InventoryService inventoryService;

    @Autowired
    public ChargeDepartmentController(ChargeDepartmentService service, TokenPrinciple tokenPrinciple, AssetService assetService, WorkOrderService workOrderService, InventoryService inventoryService) {
        this.service = service;
        this.tokenPrinciple = tokenPrinciple;
        this.assetService = assetService;
        this.workOrderService = workOrderService;
        this.inventoryService = inventoryService;
    }

    @GetMapping("get-one")
    public ResponseEntity<?> findOne(@RequestParam("chargeDepartmentId") String chargeDepartmentId) {

        return new ResponseContent().sendOkResponseEntity("", service.findOneById(chargeDepartmentId, ChargeDepartment.class));
    }

    @RequestMapping(method = RequestMethod.GET, value = "get-all-by-pagination")
    public ResponseEntity<?> getAll(@PathParam("term") String term, @PathParam("code") String code, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllByPagination(term, code, pageable, totalElement));
    }

    @RequestMapping(method = RequestMethod.POST, value = "save")
    public ResponseEntity<?> saveDepartment(@RequestBody ChargeDepartment chargeDepartment) {
        return new ResponseContent().sendOkResponseEntity("", service.saveDepartment(chargeDepartment));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "update")
    public ResponseEntity<?> update(@RequestBody ChargeDepartment chargeDepartment) {
        return new ResponseContent().sendOkResponseEntity("", service.updateCharge(chargeDepartment));
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@PathParam("chargeDepartmentId") String chargeDepartmentId) {
        if (assetService.ifChargeDepartmentExistsInAsset(chargeDepartmentId)) {
            return ResponseEntity.ok().body("\"برای حذف این دپارتمان مسئول ابتدا آن را از بخش دارایی ها پاک کنید\"");
        } else if (workOrderService.ifChargeDepartmentExistInWorkOrder(chargeDepartmentId)) {
            return ResponseEntity.ok().body("\"برای حذف این دپارتمان مسئول ابتدا آن را از بخش سفارش کارها پاک کنید\"");
        } else if (inventoryService.ifChargeDepartmentExistsInInventory(chargeDepartmentId)) {
            return ResponseEntity.ok().body("\"برای حذف این دپارتمان مسئول ابتدا آن را از بخش موجودی ها پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(service.logicDeleteById(chargeDepartmentId, ChargeDepartment.class));
        }
    }

    @GetMapping("code-unique-check")
    public ResponseEntity<?> codeUniqueCheck(@RequestParam("code") String code) {
//        return new ResponseContent().sendOkResponseEntity("", service.codeUniqueCheck(code, tokenPrinciple.getOrganCode()));
        return null;
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllChargeDepartment() {
        return ResponseEntity.ok().body(service.getAllChargeDepartment());
    }

    @GetMapping("check-if-code-is-unique")
    public ResponseEntity<?> checkIfCodeIsUnique(@PathParam("code") String code) {
        return ResponseEntity.ok().body(service.checkIfCodeIsUnique(code));
    }
}
