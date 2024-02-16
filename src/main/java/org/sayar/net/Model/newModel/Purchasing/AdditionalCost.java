package org.sayar.net.Model.newModel.Purchasing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;

//import javax.persistence.*;

//@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionalCost{//extends baseModel1
    @Id
    String id;
//
//    private float taxPercentage; /// most be store ??
//    private double taxAmount;
//    private  double shippingCost;
////    @Enumerated(EnumType.STRING)
//    private ShippingType shippingType;
//
//    public AdditionalCost() {
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public float getTaxPercentage() {
//        return taxPercentage;
//    }
//
//    public void setTaxPercentage(float taxPercentage) {
//        this.taxPercentage = taxPercentage;
//    }
//
//    public double getTaxAmount() {
//        return taxAmount;
//    }
//
//    public void setTaxAmount(double taxAmount) {
//        this.taxAmount = taxAmount;
//    }
//
//    public ShippingType getShippingType() {
//        return shippingType;
//    }
//
//    public void setShippingType(String n) {
//        this.shippingType = ShippingType.create(Integer.valueOf(n));
//    }
//
//    public double getShippingCost() {
//        return shippingCost;
//    }
//
//    public void setShippingCost(double shippingCost) {
//        this.shippingCost = shippingCost;
//    }
//    شاهین
   String title;
   String note;
   String cost;

   public enum FN{
       ID,TITLE,NOTE,COST
    }
}
