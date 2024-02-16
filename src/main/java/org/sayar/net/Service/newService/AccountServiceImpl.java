package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.Account;
import org.springframework.stereotype.Service;

@Service("accountServiceImpl")
public class AccountServiceImpl extends GeneralServiceImpl<Account> implements AccountService{


}
