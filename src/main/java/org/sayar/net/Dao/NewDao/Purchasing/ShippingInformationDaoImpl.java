package org.sayar.net.Dao.NewDao.Purchasing;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.Purchasing.ShippingInformation;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("shippingInformationDaoImpl")
@Transactional
public class ShippingInformationDaoImpl extends GeneralDaoImpl<ShippingInformation> implements ShippingInformationDao {
    public ShippingInformationDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


//    @Override
//    public ShippingInformation setEmptyFieldToNull(ShippingInformation entity) {
//
//        if(entity.getBillToAddress().getId()==0l){
//            entity.setBillToAddress(null);
//        }
//
//        if(entity.getLocationBillTo().getId()==0l){
//            entity.setLocationBillTo(null);
//        }
//        if(entity.getLocationShipTo().getId()==0l){
//            entity.setLocationShipTo(null);
//        }
//        if(entity.getSupplier().getId()==0l){
//            entity.setSupplier(null);
//        }
//        if(entity.getSupplierAddress().getId()==0l){
//            entity.setSupplierAddress(null);
//        }
//        if(entity.getShipToAddress().getId()==0l){
//            entity.setShipToAddress(null);
//        }
//        return entity;
//
//    }
}
