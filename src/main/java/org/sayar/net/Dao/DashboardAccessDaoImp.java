package org.sayar.net.Dao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Dao.NewDao.DashboardAccessDao;
import org.sayar.net.Model.DTO.DashboardAccessDTO;
import org.sayar.net.Model.DashboardAccess;
import org.sayar.net.Security.TokenPrinciple;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DashboardAccessDaoImp implements DashboardAccessDao {
    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public DashboardAccess createUserDashboardAccess(DashboardAccess dashboardAccess) {
        return mongoOperations.save(dashboardAccess);
    }

    @Override
    public DashboardAccess getUserDashboardAccess(String userId, String creatorId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userId").is(userId));
        query.addCriteria(Criteria.where("creatorId").is(creatorId));
        Print.print("resulttt",mongoOperations.findOne(query, DashboardAccess.class));
        return mongoOperations.findOne(query, DashboardAccess.class);
    }

}