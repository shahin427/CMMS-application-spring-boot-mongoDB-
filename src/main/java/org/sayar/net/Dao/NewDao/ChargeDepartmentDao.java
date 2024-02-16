package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.Asset.ChargeDepartment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChargeDepartmentDao extends GeneralDao<ChargeDepartment> {

    ChargeDepartment saveDepartment(ChargeDepartment chargeDepartment);

    UpdateResult updateCharge(ChargeDepartment chargeDepartment);

    List<ChargeDepartment> findAllBudgetByFilterAndPagination(String term, String code, Pageable pageable, Integer totalElement);

    long getAllCount(String term,String code);

    List<ChargeDepartment> findAllBudget(Pageable pageable, Integer totalElement);

    List<ChargeDepartment> getAllChargeDepartment();

    boolean checkIfCodeIsUnique(String code);

    ChargeDepartment getChargeDepartmentTitle(String chargeDepartmentId);
}
