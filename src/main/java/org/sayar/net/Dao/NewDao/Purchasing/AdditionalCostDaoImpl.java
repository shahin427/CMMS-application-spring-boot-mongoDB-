package org.sayar.net.Dao.NewDao.Purchasing;

import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.Purchasing.AdditionalCost;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("additionalCostDaoImpl")
@Transactional
public class AdditionalCostDaoImpl extends GeneralDaoImpl<AdditionalCost> implements AdditionalCostDao {
    public AdditionalCostDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

//    @Override
//    public AdditionalCost setEmptyFieldToNull(AdditionalCost entity) {
//        return entity;
//    }
}
