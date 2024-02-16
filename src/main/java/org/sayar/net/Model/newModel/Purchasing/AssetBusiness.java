package org.sayar.net.Model.newModel.Purchasing;

import lombok.Data;
import org.sayar.net.Model.newModel.Company;
import org.sayar.net.Model.newModel.Enum.BusinessType;
import org.springframework.data.annotation.Id;

//import javax.persistence.*;

//@Entity
@Data
public class AssetBusiness {

    @Id
    private String id;
    private boolean displayAsPrimary;
    private boolean myPreferVendor;
    private boolean addStockLocation;
    private boolean sendRFQ;
    private int renderQuantity;
    private int numberDayDeliver;
    private String supplierPhoneNumber;
    private BusinessType businessType;
    private Company business;

    public AssetBusiness() {
    }

}
