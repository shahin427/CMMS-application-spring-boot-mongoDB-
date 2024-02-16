package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.Location.Address.Address;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("addressDaoImpl")
@Transactional
public class AddressDaoImpl extends GeneralDaoImpl<Address> implements AddressDao{
    public AddressDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


//    @Override
//    public Address setEmptyFieldToNull(Address entity) {
//
//        if (entity.getLocation().isEmpty()){
//            entity.setLocation(null);
//        }
//
//        if (entity.getCity().isEmpty()){
//            entity.setCity(null);
//        }
//
//        if (entity.getProvince().isEmpty()){
//            entity.setProvince(null);
//        }
//
//        return entity;
//    }

}
