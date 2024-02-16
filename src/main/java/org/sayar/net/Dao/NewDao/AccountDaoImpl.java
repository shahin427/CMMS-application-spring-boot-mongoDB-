package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.Account;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("accountDaoImpl")
@Transactional
public class AccountDaoImpl extends GeneralDaoImpl<Account> implements AccountDao{
    public AccountDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }



//    @Override
//    public Account setEmptyFieldToNull(Account entity) {
//        return entity;
//    }
}

