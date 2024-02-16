package org.sayar.net.Dao.NewDao;

import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.Asset.Budget;
import org.sayar.net.Model.DTO.BudgetDTO;
import org.sayar.net.Model.DTO.BudgetWithCurrencyDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BudgetDao extends GeneralDao<Budget> {

    Budget saeBudget(Budget budget);

    Budget updateBudget(Budget budget);

    boolean checkIfCodeExist(Budget budget);

    List<BudgetWithCurrencyDTO> findAllBudget(BudgetDTO budgetDTO, Pageable pageable, Integer totalElement);

    long getAllCount(BudgetDTO budgetDTO);

    List<Budget> findAllBudgetByFilterAndPagination(String name, Pageable pageable);

    boolean deleteBudget(String id);

    List<Budget> getAllBudget();

    boolean ifCurrencyExistsInBudget(String currencyId);

    boolean checkIfBudgetCodeIsUnique(String code);

    Budget getBudgetTitle(String budgetId);

    BudgetWithCurrencyDTO findOneBudget(String id);
}
