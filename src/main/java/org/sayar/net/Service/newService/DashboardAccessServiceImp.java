package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.DashboardAccessDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.DashboardAccessDTO;
import org.sayar.net.Model.DashboardAccess;
import org.sayar.net.Service.DashboardAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardAccessServiceImp extends GeneralServiceImpl<DashboardAccess> implements DashboardAccessService {
    @Autowired
    private DashboardAccessDao dashboardAccessDao;

    @Override
    public DashboardAccess createUserDashboardAccess(DashboardAccess dashboardAccess) {
        return dashboardAccessDao.createUserDashboardAccess(dashboardAccess);
    }

    @Override
    public DashboardAccess getUserDashboardAccess(String userId, String creatorId) {
        return dashboardAccessDao.getUserDashboardAccess(userId, creatorId);
    }
}
