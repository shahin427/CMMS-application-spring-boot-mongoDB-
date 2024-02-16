package org.sayar.net.Dao.ImportanceDegree;


import org.sayar.net.Model.ImportanceDegree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImportanceDegreeDao {


    ImportanceDegree createImportanceDegree(ImportanceDegree entity);

    ImportanceDegree getOneImportanceDegree(String entityId);

    Boolean updateImportanceDegree(ImportanceDegree entity);

    Boolean deleteImportanceDegree(String entity);
    Boolean checkedUsed(String entity);

    List<ImportanceDegree> getAllType();

    Page<ImportanceDegree> getPage(String term, Pageable pageable, Integer total);
}
