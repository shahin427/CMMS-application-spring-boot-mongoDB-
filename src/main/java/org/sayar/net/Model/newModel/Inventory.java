package org.sayar.net.Model.newModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    @Id
    private String id;
    private String inventoryId;
    private Long currentQuantity;
    private Long previousQuantity;
    private Long minQuantity;
    private String partId;
    private String partName;
    private String partCode;
    private String chargeDepartmentId;
    private String budgetId;
    private String corridor;
    private String row;
    private String warehouse;
    private String price;
    private String inventoryCode;
    private String userId;
    private Date creationDate;
    private String inventoryLocationId;
    private boolean sameDocumentsDeleted;

    public enum FN {
        id, currentQuantity, previousQuantity, minQuantity, partName, partCode, chargeDepartment, budget, corridor, row, partId, warehouse, price,
        inventoryCode, user, creationDate, inventoryLocation
    }

//    public Inventory() {
//        this.creationDate = new Date();
//    }
}
