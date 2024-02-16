package org.sayar.net.Dao.NewDao.Purchasing;

import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.Purchasing.AssetBusiness;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("assetBusinessDaoImpl")
@Transactional
public class AssetBusinessDaoImpl extends GeneralDaoImpl<AssetBusiness> implements AssetBusinessDao {
    public AssetBusinessDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


//    @Override
//    public AssetBusiness setEmptyFieldToNull(AssetBusiness entity) {
//        if(entity.getBusiness().getId()==0l){
//            entity.setBusiness(null);
//        }
//
//        return entity;
//    }
}
