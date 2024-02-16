package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class BudgetDTO {
    private Long primaryBudgetAmount;
    private Long finalBudgetAmount;
    private String title;
    private String code;
    private String currency;
}
