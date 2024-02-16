package org.sayar.net.Model.newModel.purchase.controller;

import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.newModel.Purchasing.AdditionalCost;
import org.sayar.net.Model.newModel.Purchasing.PurchaseShipping;
import org.sayar.net.Model.newModel.purchase.model.Purchase;
import org.sayar.net.Model.newModel.purchase.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("purchase")
public class PurchaseController {

    private PurchaseService service;

    public PurchaseController(PurchaseService service) {
        this.service = service;
    }

    @GetMapping("by-id-list")
    public ResponseEntity<?> getByIdLIst(@RequestBody List<String> idList) {
        return new ResponseContent().sendOkResponseEntity("", service.getByIdLIst(idList));
    }

    @GetMapping("shipping/{id}")
    public ResponseEntity<?> getOnePurchase(@PathVariable String id) {
          return new ResponseContent().sendOkResponseEntity("",service.getOnePurchase(id));
    }
    @PostMapping()
    public ResponseEntity<?> postOnePurchasePlanning(@RequestBody Purchase purchase){
        return new ResponseContent().sendOkResponseEntity("",service.postOnePurchase(purchase));
    }
    @PostMapping ("{purchaseId}")
    public ResponseEntity<?> postOnePurchaseShipping(@PathVariable String purchaseId, @RequestBody PurchaseShipping purchaseShipping){
        return new ResponseContent().sendOkResponseEntity("",service.postOnePurchaseShipping(purchaseId,purchaseShipping));
    }
    @PutMapping("{purchaseId}")
    public ResponseEntity<?> editOnePurchasePlanning(@PathVariable String purchaseId,@RequestBody Purchase purchase){

            return new ResponseContent().sendOkResponseEntity("", service.editOnePurchase(purchaseId,purchase));
    }
    @GetMapping("purchasePlanning")
    public ResponseEntity<?> getAllPurchasePlanning(Purchase purchase){
        return new ResponseContent().sendOkResponseEntity("",service.getAllPurchasePlanning(purchase));
    }
    @DeleteMapping("{purchaseId}")
    public ResponseEntity<?> deletePurchasePlanning(@PathVariable String purchaseId){
        return new ResponseContent().sendOkResponseEntity("",service.logicDeleteById(purchaseId,Purchase.class));
    }
    @GetMapping("{costId}")
    public ResponseEntity<?> getAdditionalCost(@PathVariable String costId){
        return new ResponseContent().sendOkResponseEntity("",service.findOneById(costId,Purchase.class));
    }
    @PostMapping("additionalCost/{purchaseId}")
    public ResponseEntity<?> addAdditionalCost(@PathVariable String purchaseId,@RequestBody AdditionalCost additionalCost){
        return new ResponseContent().sendOkResponseEntity("",service.addAdditionalCost(purchaseId,additionalCost));
    }
    @PutMapping("{purchaseId}/{addCostId}")
    public ResponseEntity<?> editAdditionalCost(@PathVariable String purchaseId,@PathVariable String addCostId,@RequestBody AdditionalCost additionalCost){
        return new ResponseContent().sendOkResponseEntity("",service.editAdditionalCost(purchaseId,addCostId,additionalCost));
    }
}

