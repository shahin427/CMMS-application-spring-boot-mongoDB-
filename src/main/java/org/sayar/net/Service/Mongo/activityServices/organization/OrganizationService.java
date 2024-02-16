package org.sayar.net.Service.Mongo.activityServices.organization;//package org.sayar.net.Service.Mongo.activityServices.organization;
//
//
//import org.sayar.net.Dao.Mongo.activityDao.organization.OrganizationDao;
//import org.sayar.net.Model.Mongo.activityModel.Organization;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service("organizationService")
//public class OrganizationService {
//    private final OrganizationDao organizationDao;
//
//    @Autowired
//    public OrganizationService(OrganizationDao organizationDao) {
//        this.organizationDao = organizationDao;
//    }
//
//    public Organization upload(Organization organization) {
//        return organizationDao.upload(organization);
//    }
//
//    public List<Organization> getAll(String companyId) {
//        return organizationDao.getAll(companyId);
//    }
//
//    public Organization getOne(String id) {
//        return organizationDao.getOne(id);
//    }
//
//    public boolean delete(String id) {
//
//        return organizationDao.delete(id);
//    }
//
//    public boolean update(Organization organization) {
//        return organizationDao.update(organization);
//    }
//}
