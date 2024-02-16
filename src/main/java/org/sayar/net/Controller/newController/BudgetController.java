package org.sayar.net.Controller.newController;

import org.sayar.net.Model.Asset.Budget;
import org.sayar.net.Model.DTO.BudgetDTO;
import org.sayar.net.Security.TokenPrinciple;
import org.sayar.net.Service.newService.AssetService;
import org.sayar.net.Service.newService.BudgetService;
import org.sayar.net.Service.newService.InventoryService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/budget")
public class BudgetController {
    @Autowired
    private TokenPrinciple tokenPrinciple;
    @Autowired
    private BudgetService service;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private AssetService assetService;
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    public BudgetController(TokenPrinciple tokenPrinciple, BudgetService service) {
        this.tokenPrinciple = tokenPrinciple;
        this.service = service;
    }

    @PostMapping("save")
    public ResponseEntity<?> saveBudget(@RequestBody Budget budget) {
        return ResponseEntity.ok().body(service.saveBudget(budget));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateBudget(@RequestBody Budget budget) {
        return ResponseEntity.ok().body(service.updateBudget(budget));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteOne(@PathParam("budgetId") String budgetId) {
        if (workOrderService.ifBudgetExistsInWorkOrder(budgetId)) {
            return ResponseEntity.ok().body("\"برای حذف این بودجه ابتدا آن را از سفارش کارها پاک کنید\"");
        } else if (assetService.ifBudgetExistsInAsset(budgetId)) {
            return ResponseEntity.ok().body("\"برای حذف این بودجه ابتدا آن را از دارایی ها پاک کنید\"");
        } else if (inventoryService.ifBudgetExistsInInventory(budgetId)) {
            return ResponseEntity.ok().body("\"برای حذف این بودجه ابتدا آن را از موجودی ها پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(service.logicDeleteById(budgetId, Budget.class));
        }
    }

    @GetMapping("get-one")
    public ResponseEntity<?> gteOne(@PathParam("id") String id) {
        return ResponseEntity.ok().body(service.findOneBudget(id));
    }

    @GetMapping()
    public ResponseEntity<?> getAll(Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.findAllBudget(pageable, totalElement));
    }

    @PostMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllByFilterAndPagination(@RequestBody BudgetDTO budgetDTO, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllByFilterAndPagination(budgetDTO, pageable, totalElement));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllBudget() {
        return ResponseEntity.ok().body(service.getAllBudget());
    }

    @GetMapping("check-if-code-is-unique")
    public ResponseEntity<?> checkIfBudgetCodeIsUnique(@PathParam("code") String code) {
        return ResponseEntity.ok().body(service.checkIfBudgetCodeIsUnique(code));
    }
}