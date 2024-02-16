package org.sayar.net.Dao.WorkCategoty;


import org.sayar.net.Model.WorkCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkCategoryDao {


    WorkCategory createWorkCategory(WorkCategory userType);

    WorkCategory getOneWorkCategory(String userTypeId);

    Boolean updateWorkCategory(WorkCategory userType);

    Boolean deleteWorkCategory(String userTypeId);
    Boolean checkUsed(String userTypeId);

    List<WorkCategory> getAllType();

    Page<WorkCategory> getPage(String term, Pageable pageable, Integer total);
}
