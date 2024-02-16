package org.sayar.net.Service.WorkCategory;


import org.sayar.net.Dao.WorkCategoty.WorkCategoryDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.WorkCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("WorkCategoryServiceImpl")
public class WorkCategoryServiceImpl extends GeneralServiceImpl<WorkCategory> implements WorkCategoryService {
    @Autowired
    private WorkCategoryDao activityTypeDao;



    @Override
    public WorkCategory createWorkCategory(WorkCategory userType) {
        return activityTypeDao.createWorkCategory(userType);
    }

    @Override
    public WorkCategory getOneWorkCategory(String userTypeId) {
        return activityTypeDao.getOneWorkCategory(userTypeId);
    }

    @Override
    public boolean updateWorkCategory(WorkCategory userType) {
        return activityTypeDao.updateWorkCategory(userType);
    }

    @Override
    public Boolean deleteWorkCategory(String userTypeId) {
        return activityTypeDao.deleteWorkCategory(userTypeId);
    }
    @Override
    public Boolean checkUsed(String id) {
        return activityTypeDao.checkUsed(id);
    }


    @Override
    public List<WorkCategory> getAllType() {
        return activityTypeDao.getAllType();
    }

    @Override
    public Page<WorkCategory> getPage(String term, Pageable pageable, Integer total) {
        return activityTypeDao.getPage(term,pageable,total);
    }

}
