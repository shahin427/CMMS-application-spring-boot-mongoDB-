package org.sayar.net.Service.WorkCategory;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.WorkCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WorkCategoryService extends GeneralService<WorkCategory> {


    WorkCategory createWorkCategory(WorkCategory entity);

    WorkCategory getOneWorkCategory(String id);

    boolean updateWorkCategory(WorkCategory entity);

    Boolean deleteWorkCategory(String id);
    Boolean checkUsed(String id);

    List<WorkCategory> getAllType();

    Page<WorkCategory> getPage(String term, Pageable pageable, Integer total);

}
