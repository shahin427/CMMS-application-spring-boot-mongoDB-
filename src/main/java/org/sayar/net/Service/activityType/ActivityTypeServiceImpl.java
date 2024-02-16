package org.sayar.net.Service.activityType;


import org.sayar.net.Dao.activityType.ActivityTypeDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.ActivityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ActivityTypeServiceImpl")
public class ActivityTypeServiceImpl extends GeneralServiceImpl<ActivityType> implements ActivityTypeService {
    @Autowired
    private ActivityTypeDao activityTypeDao;



    @Override
    public ActivityType createActivityType(ActivityType entity) {
        return activityTypeDao.createActivityType(entity);
    }

    @Override
    public ActivityType getOneActivityType(String entityId) {
        return activityTypeDao.getOneActivityType(entityId);
    }

    @Override
    public boolean updateActivityType(ActivityType entity) {
        return activityTypeDao.updateActivityType(entity);
    }

    @Override
    public Boolean deleteActivityType(String id) {
        return activityTypeDao.deleteActivityType(id);
    } @Override
    public Boolean checkUsed(String id) {
        return activityTypeDao.checkUsed(id);
    }


    @Override
    public List<ActivityType> getAllType() {
        return activityTypeDao.getAllType();
    }

    @Override
    public  Page<ActivityType> getPage(String term, Pageable pageable, Integer total){
        return activityTypeDao.getPage(term,pageable,total);
    }

}
