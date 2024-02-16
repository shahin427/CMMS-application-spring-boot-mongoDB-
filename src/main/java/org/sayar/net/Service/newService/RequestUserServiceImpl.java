package org.sayar.net.Service.newService;//package org.sayar.net.Service.newService;
//
//import org.sayar.net.Dao.NewDao.RequestUserDao;
//
//import org.sayar.net.Security.Model.RequestUser;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//@Service("requestUserServiceImpl")
//public class RequestUserServiceImpl extends GeneralServiceImpl<RequestUser> implements RequestUserService<RequestUser> {
//    private RequestUserDao requestUserDao;
//    public RequestUserServiceImpl( @Qualifier("requestUserDaoImpl") GenericDao<RequestUser> dao) {
//        super(dao);
//
//        requestUserDao = (RequestUserDao)dao;
//
//    }
//
//    @Override
//    public RequestUser add(RequestUser entity, long organId) {
//
//        try {
//
//            requestUserDao.add(entity, organId);
//            return entity;
//        }catch (Exception e){
//            return null;
//        }
//    }
//}
