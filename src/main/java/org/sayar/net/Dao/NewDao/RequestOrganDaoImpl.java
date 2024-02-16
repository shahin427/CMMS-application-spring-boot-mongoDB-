package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.OrganManagment.RequestOrgan;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Repository;

@Repository("requestOrganDaoImpl")
public class RequestOrganDaoImpl extends GeneralDaoImpl<RequestOrgan> implements RequestOrganDao {
    public RequestOrganDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }





//    @Override
//    public RequestOrgan setEmptyFieldToNull(RequestOrgan entity) {
//        return entity;
//    }
}
