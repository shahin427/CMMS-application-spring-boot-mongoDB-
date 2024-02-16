package org.sayar.net.Service.newService;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.Asset.Budget;
import org.sayar.net.Model.DTO.BudgetDTO;
import org.sayar.net.Model.DTO.BudgetWithCurrencyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BudgetService extends GeneralService<Budget> {

    boolean codeUniqueCheck(String code, String organCode);

    Budget saveBudget(Budget budget);

    Budget updateBudget(Budget budget);

    Page<Budget> findAllBudget(Pageable pageable, Integer totalElement);

    Page<BudgetWithCurrencyDTO> getAllByFilterAndPagination(BudgetDTO budgetDTO, Pageable pageable, Integer totalElement);

    boolean deleteBudget(String budgetId);

    List<Budget> getAllBudget();

    boolean ifCurrencyExistsInBudget(String currencyId);

    boolean checkIfBudgetCodeIsUnique(String code);

    Budget getBudgetTitle(String budgetId);

    BudgetWithCurrencyDTO findOneBudget(String id);
}
