package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.Location.Address.Address;
import org.springframework.stereotype.Service;

@Service("addressServiceImpl")
public class AddressServiceImpl extends GeneralServiceImpl<Address> implements AddressService {

}
