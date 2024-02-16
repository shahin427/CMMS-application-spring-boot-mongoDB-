package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.BudgetDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.Budget;
import org.sayar.net.Model.DTO.BudgetDTO;
import org.sayar.net.Model.DTO.BudgetWithCurrencyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl extends GeneralServiceImpl<Budget> implements BudgetService {
    private final BudgetDao dao;

    @Autowired
    public BudgetServiceImpl(BudgetDao budgetDao) {
        this.dao = budgetDao;
    }

    @Override
    public boolean codeUniqueCheck(String code, String organCode) {
        return dao.checkExistenceByOrgId("code", code, "organId", organCode, Budget.class);
    }

    @Override
    public Budget saveBudget(Budget budget) {
        return dao.saeBudget(budget);
    }

    @Override
    public Budget updateBudget(Budget budget) {
        return dao.updateBudget(budget);
    }

    @Override
    public Page<Budget> findAllBudget(Pageable pageable, Integer totalElement) {
        return null;
    }

    @Override
    public Page<BudgetWithCurrencyDTO> getAllByFilterAndPagination(BudgetDTO budgetDTO, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.findAllBudget(budgetDTO, pageable, totalElement)
                , pageable
                , dao.getAllCount(budgetDTO)
        );
    }

    @Override
    public boolean deleteBudget(String budgetId) {
        return dao.deleteBudget(budgetId);
    }

    @Override
    public List<Budget> getAllBudget() {
        return dao.getAllBudget();
    }

    @Override
    public boolean ifCurrencyExistsInBudget(String currencyId) {
        return dao.ifCurrencyExistsInBudget(currencyId);
    }

    @Override
    public boolean checkIfBudgetCodeIsUnique(String code) {
        return dao.checkIfBudgetCodeIsUnique(code);
    }

    @Override
    public Budget getBudgetTitle(String budgetId) {
        return dao.getBudgetTitle(budgetId);
    }

    @Override
    public BudgetWithCurrencyDTO findOneBudget(String id) {
        return dao.findOneBudget(id);
    }
}
