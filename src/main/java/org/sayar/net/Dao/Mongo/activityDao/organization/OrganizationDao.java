package org.sayar.net.Dao.Mongo.activityDao.organization;//package org.sayar.net.Dao.Mongo.activityDao.organization;
//
//
//import org.sayar.net.Model.Mongo.activityModel.Organization;
//import org.sayar.net.Model.Mongo.activityModel.Post;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoOperations;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository("organizationDao")
//public class OrganizationDao {
//    private final MongoOperations mongoOperations;
//
//    @Autowired
//    public OrganizationDao(MongoOperations mongoOperations) {
//        this.mongoOperations = mongoOperations;
//    }
//
//    public Organization upload(Organization organization) {
//        mongoOperations.save(organization);
//        return organization;
//    }
//
//    public List<Organization> getAll(String companyId) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("companyId").is(companyId));
//        System.out.println("companyId: " + companyId);
//
//        return mongoOperations.find(query, Organization.class);
//    }
//
//    public Organization getOne(String id) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("id").is(id));
//        return mongoOperations.findOne(query, Organization.class);
//    }
//
//    public boolean delete(String id) {
//        try {
//            Query query = new Query();
//            query.addCriteria(Criteria.where("organizationId").is(id));
//            if (mongoOperations.exists(query, Post.class))
//                return false;
//            else
//                mongoOperations.remove(new Query().addCriteria(Criteria.where("id").is(id)), Organization.class);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    public boolean update(Organization organization) {
//        Organization organizationFoRollBack = mongoOperations.findOne(
//                new Query().addCriteria(Criteria.where("id").is(organization.getId())), Organization.class);
//        try {
//            mongoOperations.save(organization);
//            return true;
//        } catch (Exception e) {
//            mongoOperations.save(organizationFoRollBack);
//            return false;
//        }
//    }
//}
