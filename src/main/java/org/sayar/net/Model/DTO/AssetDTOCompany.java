package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Company;

import java.util.List;

@Data
public class AssetDTOCompany {
    private List<Company> companyList;//from company collection

    public static AssetDTOCompany map(List<Company> companyList) {
        AssetDTOCompany assetDTOCompany = new AssetDTOCompany();
        assetDTOCompany.setCompanyList(companyList);
        return assetDTOCompany;
    }
}
