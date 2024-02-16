//package org.sayar.net.Dao.NewDao.Purchasing;
//
//
//import org.sayar.net.General.dao.GeneralDaoImpl;
//import org.sayar.net.Model.newModel.Purchasing.CostTracking;
//import org.springframework.data.mongodb.MongoDataBaseFactory;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//@Repository("costTrackingDaoImpl")
//@Transactional
//public class CostTrackingDaoImpl extends GeneralDaoImpl<CostTracking> implements CostTrackingDao {
//    public CostTrackingDaoImpl(MongoDataBaseFactory mongoDbFactory) {
//        super(mongoDbFactory);
//    }
//
////    @Override
////    public CostTracking setEmptyFieldToNull(CostTracking entity) {
////        if(entity.getAccount().getId()==0l){
////            entity.setAccount(null);
////        }
////
////        if(entity.getChargeDepartment().getId()==0l){
////            entity.setChargeDepartment(null);
////        }
////
////        if(entity.getPurchaseCurrency().getId()==0l){
////            entity.setPurchaseCurrency(null);
////        }
////        return entity;
////    }
//}
