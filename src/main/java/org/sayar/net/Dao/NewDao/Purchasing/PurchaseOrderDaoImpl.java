package org.sayar.net.Dao.NewDao.Purchasing;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.Purchasing.PurchaseOrder;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("purchaseOrderDaoImpl")
@Transactional
public class PurchaseOrderDaoImpl extends GeneralDaoImpl<PurchaseOrder> implements PurchaseOrderDao {
    public PurchaseOrderDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


}
