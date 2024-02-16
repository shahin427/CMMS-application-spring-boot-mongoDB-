package org.sayar.net.Model.newModel.purchase.service;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.newModel.Purchasing.AdditionalCost;
import org.sayar.net.Model.newModel.Purchasing.PurchaseShipping;
import org.sayar.net.Model.newModel.purchase.DTO.PurchaseDTO;
import org.sayar.net.Model.newModel.purchase.model.Purchase;

import java.util.List;


public interface PurchaseService extends GeneralService<Purchase> {

    List<PurchaseDTO> getByIdLIst(List<String> idList);

    Object getOnePurchase(String id);

    boolean postOnePurchase(Purchase purchase);

    boolean postOnePurchaseShipping(String purchaseId, PurchaseShipping purchaseShipping);

    UpdateResult editOnePurchase(String purchaseId, Purchase purchase);

    List<Purchase> getAllPurchasePlanning(Purchase purchase);

    boolean addAdditionalCost(String purchaseId, AdditionalCost additionalCost);

    UpdateResult editAdditionalCost(String purchaseId,String addCostId, AdditionalCost additionalCost);
}
