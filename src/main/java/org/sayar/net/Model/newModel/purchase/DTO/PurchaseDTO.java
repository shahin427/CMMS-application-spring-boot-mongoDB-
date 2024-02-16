package org.sayar.net.Model.newModel.purchase.DTO;

import org.sayar.net.Model.newModel.Currency;

public class PurchaseDTO {
    private String id;
    private long price;
    private String deliverDate;
    private String purchaseDate;
    private Currency currency;
    public long organId;

    public PurchaseDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public long getOrganId() {
        return organId;
    }

    public void setOrganId(long organId) {
        this.organId = organId;
    }
}
