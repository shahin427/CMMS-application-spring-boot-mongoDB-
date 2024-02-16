package org.sayar.net.Service.newService;


import org.sayar.net.Dao.NewDao.ChargeDepartmentDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.ChargeDepartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("chargeDepartmentServiceImpl")
public class ChargeDepartmentServiceImpl extends GeneralServiceImpl<ChargeDepartment> implements ChargeDepartmentService {
    @Autowired
    private MongoOperations mongoOperations;
    private final ChargeDepartmentDao dao;

    @Autowired
    public ChargeDepartmentServiceImpl(ChargeDepartmentDao dao) {
        this.dao = dao;
    }

    @Override
    public boolean updateCharge(ChargeDepartment chargeDepartment) {
        return this.updateResultStatus(dao.updateCharge(chargeDepartment));
    }

    @Override
    public boolean codeUniqueCheck(String code, String organCode) {

        return dao.checkExistenceByOrgId("code", code, "orgId", organCode, ChargeDepartment.class);
    }

    @Override
    public ChargeDepartment saveDepartment(ChargeDepartment chargeDepartment) {
        return dao.saveDepartment(chargeDepartment);
    }

    @Override
    public Page<ChargeDepartment> getAllByPagination(String term, String code, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.findAllBudgetByFilterAndPagination(term, code, pageable, totalElement)
                , pageable
                , dao.getAllCount(term, code)
        );
    }

    @Override
    public List<ChargeDepartment> getAllChargeDepartment() {
        return dao.getAllChargeDepartment();
    }

    @Override
    public boolean checkIfCodeIsUnique(String code) {
        return dao.checkIfCodeIsUnique(code);
    }

    @Override
    public ChargeDepartment getChargeDepartmentTitle(String chargeDepartmentId) {
        return dao.getChargeDepartmentTitle(chargeDepartmentId);
    }
}
