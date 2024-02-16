package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class BudgetWithCurrencyDTO {
    private String id;
    private String title;
    private String description;
    private String code;
    private Long budgetAmount;
    private String currencyId;
    private String currencyName;
}
