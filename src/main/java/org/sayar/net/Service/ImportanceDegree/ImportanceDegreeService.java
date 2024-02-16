package org.sayar.net.Service.ImportanceDegree;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.ImportanceDegree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImportanceDegreeService extends GeneralService<ImportanceDegree> {


    ImportanceDegree createImportanceDegree(ImportanceDegree entity);

    ImportanceDegree getOneImportanceDegree(String id);

    boolean updateImportanceDegree(ImportanceDegree entity);

    Boolean deleteImportanceDegree(String id);
    Boolean checkedUsed(String id);

    List<ImportanceDegree> getAllType();

    Page<ImportanceDegree> getPage(String term, Pageable pageable, Integer total);


}
