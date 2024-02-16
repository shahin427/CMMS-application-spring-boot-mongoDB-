package org.sayar.net.Model.newModel;


import lombok.Data;
import org.sayar.net.Enumes.MiscCostType;
import org.springframework.data.annotation.Id;

@Data
public class MiscCost {
    @Id
    private String id;
    private String title;
    private Double estimatedQuantity;
    private Double estimatedUnitCost;
    private Double estimatedTotalCost;
    private Integer quantity;
    private Double actualUnitCost;
    private Double actualTotalCost;
    private String description;
    private MiscCostType miscCostType;
    private String referenceId;
}

