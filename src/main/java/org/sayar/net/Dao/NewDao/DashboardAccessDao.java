package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Model.DTO.DashboardAccessDTO;
import org.sayar.net.Model.DashboardAccess;

import java.util.List;

public interface DashboardAccessDao {

    DashboardAccess createUserDashboardAccess(DashboardAccess dashboardAccess);

    DashboardAccess getUserDashboardAccess(String userId, String creatorId);

}
