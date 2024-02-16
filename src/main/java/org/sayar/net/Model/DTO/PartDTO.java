package org.sayar.net.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.Asset.Budget;
import org.sayar.net.Model.Asset.ChargeDepartment;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartDTO {
 private String minQuantity;
 private String quantity;
 private String corridor;
 private String inventoryCode;
 private String warehouse;
 private String price;
 private String row;
 private Budget Budget;
 private ChargeDepartment chargeDepartment;
}
