//package org.sayar.net.Model.newModel.Purchasing;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import org.sayar.net.Model.newModel.Account;
//import org.sayar.net.Model.newModel.BaseOne.BaseModel1;
//import org.sayar.net.Model.newModel.ChargeDepartment;
//import org.sayar.net.Model.newModel.Currency;
//import org.sayar.net.Model.newModel.Enum.ConnectionType;
//
////import javax.persistence.*;
//
////@Entity
//public class CostTracking extends BaseModel1 {
//
////    @Column(columnDefinition = "VARCHAR(20)")
//    private String purchaseOrderReferenceNumber;
////    @Enumerated(EnumType.STRING)
//    private ConnectionType connectionType;
//
//    @JsonIgnoreProperties(ignoreUnknown = true)
////    @OneToOne(cascade = CascadeType.MERGE)
////    @JoinColumn(name = "account_id")
//    private Account account;
//
//    @JsonIgnoreProperties(ignoreUnknown = true)
////    @OneToOne(cascade = CascadeType.MERGE)
////    @JoinTable(
////            name = "middle_costtracking_chargedepartment",
////            joinColumns = {@JoinColumn(name = "costtracking_id")},
////            inverseJoinColumns = {@JoinColumn(name = "chargedepartment_id")})
//    private ChargeDepartment chargeDepartment;
//
//    @JsonIgnoreProperties(ignoreUnknown = true)
////    @OneToOne(cascade = CascadeType.MERGE)
////    @JoinTable(
////            name = "middle_costtracking_currency",
////            joinColumns = {@JoinColumn(name = "costtracking_id")},
////            inverseJoinColumns = {@JoinColumn(name = "currency_id")})
//    private Currency purchaseCurrency;
//
//    public CostTracking() {
//    }
//
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getPurchaseOrderReferenceNumber() {
//        return purchaseOrderReferenceNumber;
//    }
//
//    public void setPurchaseOrderReferenceNumber(String purchaseOrderReferenceNumber) {
//        this.purchaseOrderReferenceNumber = purchaseOrderReferenceNumber;
//    }
//
//    public ConnectionType getConnectionType() {
//        return connectionType;
//    }
//
//    public void setConnectionType(ConnectionType connectionType) {
//        this.connectionType = connectionType;
//    }
//
//    public Account getAccount() {
//        return account;
//    }
//
//    public void setAccount(Account account) {
//        this.account = account;
//    }
//
//    public ChargeDepartment getChargeDepartment() {
//        return chargeDepartment;
//    }
//
//    public void setChargeDepartment(ChargeDepartment chargeDepartment) {
//        this.chargeDepartment = (ChargeDepartment) validation(chargeDepartment);
//    }
//
//    public Currency getPurchaseCurrency() {
//        return purchaseCurrency;
//    }
//
//    public void setPurchaseCurrency(Currency purchaseCurrency) {
////        this.purchaseCurrency = (Currency) validation(purchaseCurrency);
//    }
//
//
//}
