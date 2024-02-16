package org.sayar.net.Model.newModel.purchase.service;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.Purchasing.AdditionalCost;
import org.sayar.net.Model.newModel.Purchasing.PurchaseShipping;
import org.sayar.net.Model.newModel.purchase.DTO.PurchaseDTO;
import org.sayar.net.Model.newModel.purchase.dao.PurchaseDao;
import org.sayar.net.Model.newModel.purchase.model.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("purchaseServiceImpl")
public class PurchaseServiceImpl extends GeneralServiceImpl<Purchase> implements PurchaseService {

    private final PurchaseDao purchaseDao;

    @Autowired
    public PurchaseServiceImpl(PurchaseDao purchaseDao) {
        this.purchaseDao = purchaseDao;
    }

    @Override
    public List<PurchaseDTO> getByIdLIst(List<String> idList) {

        return purchaseDao.getByIdLIst(idList);
    }
    @Override
    public Object getOnePurchase(String id){
        return purchaseDao.getOnePurchase(id);
    }
    @Override
    public boolean postOnePurchase(Purchase purchase){
        return purchaseDao.postOnePurchase(purchase);
    }
    @Override
    public boolean postOnePurchaseShipping(String purchaseId, PurchaseShipping purchaseShipping){
         return purchaseDao.postOnePurchaseShipping(purchaseId,purchaseShipping);
    }
    @Override
    public UpdateResult editOnePurchase(String purchaseId, Purchase purchase){
        return purchaseDao.editOnePurchase(purchaseId,purchase);
    }
    @Override
    public List<Purchase> getAllPurchasePlanning(Purchase purchase){
        return purchaseDao.getAllPurchasePlanning(purchase);
    }
    @Override
    public boolean addAdditionalCost(String purchaseId, AdditionalCost additionalCost){
        return purchaseDao.addAdditionalCost(purchaseId,additionalCost);
    }
    @Override
    public UpdateResult editAdditionalCost(String purchaseId,String addCostId, AdditionalCost additionalCost){
        return purchaseDao.editAdditionalCost(purchaseId,addCostId,additionalCost);
    }


}
