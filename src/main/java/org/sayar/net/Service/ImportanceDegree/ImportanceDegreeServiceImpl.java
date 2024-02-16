package org.sayar.net.Service.ImportanceDegree;


import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.ImportanceDegree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ImportanceDegreeServiceImpl")
public class ImportanceDegreeServiceImpl extends GeneralServiceImpl<ImportanceDegree> implements ImportanceDegreeService {
    @Autowired
    private org.sayar.net.Dao.ImportanceDegree.ImportanceDegreeDao entityDao;



    @Override
    public ImportanceDegree createImportanceDegree(ImportanceDegree userType) {
        return entityDao.createImportanceDegree(userType);
    }

    @Override
    public ImportanceDegree getOneImportanceDegree(String userTypeId) {
        return entityDao.getOneImportanceDegree(userTypeId);
    }

    @Override
    public boolean updateImportanceDegree(ImportanceDegree userType) {
        return entityDao.updateImportanceDegree(userType);
    }

    @Override
    public Boolean deleteImportanceDegree(String userTypeId) {
        return entityDao.deleteImportanceDegree(userTypeId);
    }
    @Override
    public Boolean checkedUsed(String id) {
        return entityDao.checkedUsed(id);
    }


    @Override
    public List<ImportanceDegree> getAllType() {
        return entityDao.getAllType();
    }

    @Override
    public Page<ImportanceDegree> getPage(String term, Pageable pageable, Integer total) {
        return entityDao.getPage(term,pageable,total);
    }

}
