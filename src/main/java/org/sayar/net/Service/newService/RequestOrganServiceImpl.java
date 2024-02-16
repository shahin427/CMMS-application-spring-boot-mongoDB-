package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.RequestOrganDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.newModel.OrganManagment.RequestOrgan;
import org.springframework.stereotype.Service;

@Service("requestOrganServiceImpl")
public class RequestOrganServiceImpl extends GeneralServiceImpl<RequestOrgan> implements RequestOrganService {

    private RequestOrganDao requestOrganDao;
}
