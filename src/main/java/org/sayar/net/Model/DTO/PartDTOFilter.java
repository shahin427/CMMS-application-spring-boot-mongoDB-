package org.sayar.net.Model.DTO;

import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.Asset.ChargeDepartment;
import org.sayar.net.Model.newModel.Account;
import org.sayar.net.Model.newModel.Location.Address.Address;
//import org.hibernate.criterion.Projections;

public class PartDTOFilter {
     private long id;
     private String name;
     private String code;
     private String brand;
     private Address address;
     private ChargeDepartment chargeDepartment;
     private Account account;
     private long quantity;
     private long minQuantity;
     private Asset assetLocation;

    public Asset getAssetLocation() {
        return assetLocation;
    }

    public void setAssetLocation(Asset assetLocation) {
        this.assetLocation = assetLocation;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public PartDTOFilter() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ChargeDepartment getChargeDepartment() {
        return chargeDepartment;
    }

    public void setChargeDepartment(ChargeDepartment chargeDepartment) {
        this.chargeDepartment = chargeDepartment;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(long minQuantity) {
        this.minQuantity = minQuantity;
    }
}
