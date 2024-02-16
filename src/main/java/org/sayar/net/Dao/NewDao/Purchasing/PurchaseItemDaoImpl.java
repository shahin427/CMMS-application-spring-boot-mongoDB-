package org.sayar.net.Dao.NewDao.Purchasing;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.Purchasing.PurchaseItem;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("purchaseItemDaoImpl")
@Transactional
public class PurchaseItemDaoImpl extends GeneralDaoImpl<PurchaseItem> implements PurchaseItemDao {
    public PurchaseItemDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

//    @Override
//    public PurchaseItem setEmptyFieldToNull(PurchaseItem entity) {
//        if(entity.getPart().getId()==0l){
//            entity.setPart(null);
//        }
//        if(entity.getSupplier().getId()==0l){
//            entity.setSupplier(null);
//        }
//        if(entity.getAsset().getId()==0l){
//            entity.setAsset(null);
//        }
//        return entity;
//    }
}
