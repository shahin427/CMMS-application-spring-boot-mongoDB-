package org.sayar.net.Model.newModel.Purchasing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.BaseOne.BaseModel1;
import org.sayar.net.Model.newModel.Company;
import org.springframework.data.annotation.Id;

//import javax.persistence.*;

//@Entity
@Data
public class PurchaseItem {
    @Id
    private long id;
    private int quantity;
    private long unitPrice;
    private long totalPrice;
    private long taxRate;

    @JsonIgnoreProperties(ignoreUnknown = true)

    private Company supplier;
    @JsonIgnoreProperties(ignoreUnknown = true)

    private Asset asset;

    public PurchaseItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Asset getAsset() {
        return asset;
    }



    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public Company getSupplier() {
        return supplier;
    }

    public void setSupplier(Company supplier) {
        this.supplier = supplier;
    }


    public int getQuantity() {
        return quantity;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(long taxRate) {
        this.taxRate = taxRate;
    }
}
