package org.sayar.net.Model.Asset;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Model.DTO.AddressDTO;
import org.sayar.net.Model.newModel.Account;
import org.sayar.net.Model.newModel.UnitOfMeasurement;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetBasicInformation {

    private String id;
    private List<String> unitIdList;
    private List<UnitOfMeasurement> unitOfMeasurementName;
    private String note;
    private String budgetId;
    private String budgetName;
    private String chargeDepartmentId;
    private String chargeDepartmentName;
    private String parentId;
    private AddressDTO address;
    private Account account;

//    public static AssetBasicInformation map(Asset asset, Budget budget, ChargeDepartment chargeDepartment, List<UnitOfMeasurement> unitOfMeasurementList) {
//        AssetBasicInformation assetBasicInformation = new AssetBasicInformation();
//        assetBasicInformation.setId(asset.getId());
//        if (asset.getUnitIdList() != null)
//            assetBasicInformation.setUnitIdList(asset.getUnitIdList());
//        if (unitOfMeasurementList != null)
//            assetBasicInformation.setUnitOfMeasurementName(unitOfMeasurementList);
//        if (asset.getNote() != null)
//            assetBasicInformation.setNote(asset.getNote());
//        if (budget != null && budget.getId() != null)
//            assetBasicInformation.setBudgetId(asset.getBudgetId());
//        if (budget != null && budget.getTitle() != null)
//            assetBasicInformation.setBudgetName(budget.getTitle());
//        if (chargeDepartment != null && chargeDepartment.getId() != null)
//            assetBasicInformation.setChargeDepartmentId(asset.getChargeDepartmentId());
//        if (chargeDepartment != null && chargeDepartment.getTitle() != null)
//            assetBasicInformation.setChargeDepartmentName(chargeDepartment.getTitle());
//        if (asset.getParent() != null)
//            assetBasicInformation.setParentId(asset.getParent());
//        if (asset.getAddress() != null)
//            assetBasicInformation.setAddress(asset.getAddress());
//        return assetBasicInformation;
//    }
}
