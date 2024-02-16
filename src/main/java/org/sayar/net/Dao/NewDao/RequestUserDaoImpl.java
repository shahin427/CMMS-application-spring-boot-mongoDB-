package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Security.Model.RequestUser;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("requestUserDaoImpl")
@Transactional
public class RequestUserDaoImpl extends GeneralDaoImpl<RequestUser> implements RequestUserDao {
    public RequestUserDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

//    @Override
//    public RequestUser setEmptyFieldToNull(RequestUser entity) {
//        return entity;
//    }
}
