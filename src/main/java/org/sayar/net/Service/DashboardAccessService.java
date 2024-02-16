package org.sayar.net.Service;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.DashboardAccessDTO;
import org.sayar.net.Model.DashboardAccess;

import java.util.List;


public interface DashboardAccessService extends GeneralService<DashboardAccess> {

    DashboardAccess createUserDashboardAccess(DashboardAccess dashboardAccess);

    DashboardAccess getUserDashboardAccess(String userId, String creatorId);
}
