package org.sayar.net.Model.Asset;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Budget {
    @Id
    private String id;
    private String title;
    private String description;
    private String code;
    private Long budgetAmount;
    private String currencyId;
    private String organId;
    private Boolean deleted;

    public Budget() {
    }

    public enum FN {
        ID, TITLE, DESCRIPTION, CODE, BUDGETAMOUNT, CURRENCY, DELETED
    }
}
