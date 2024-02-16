package org.sayar.net.Model.newModel.Purchasing;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.Company;
import org.sayar.net.Model.newModel.Location.Address.Address;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.JoinColumn;
//import javax.persistence.OneToOne;

//@Entity
@Data
@Document
public class ShippingInformation {
    @Id
    private String id;
    private Company supplier;
    private Asset locationShipTo;
    private Asset locationBillTo;
    private Address supplierAddress;
    private Address shipToAddress;
    private Address billToAddress;


    public ShippingInformation() {
    }

}
