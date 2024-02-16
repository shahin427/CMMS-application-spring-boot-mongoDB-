package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.Asset.ChargeDepartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChargeDepartmentService extends GeneralService<ChargeDepartment> {

    boolean updateCharge(ChargeDepartment chargeDepartment);

    boolean codeUniqueCheck(String code, String organCode);

    ChargeDepartment saveDepartment(ChargeDepartment chargeDepartment);

    Page<ChargeDepartment> getAllByPagination(String term, String code, Pageable pageable, Integer totalElement);

    List<ChargeDepartment> getAllChargeDepartment();

    boolean checkIfCodeIsUnique(String code);

    ChargeDepartment getChargeDepartmentTitle(String chargeDepartmentId);
}
