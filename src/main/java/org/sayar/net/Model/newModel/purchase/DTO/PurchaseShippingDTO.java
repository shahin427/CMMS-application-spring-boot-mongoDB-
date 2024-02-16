package org.sayar.net.Model.newModel.purchase.DTO;

import org.bson.types.ObjectId;

public class PurchaseShippingDTO {
    private String id;
    private String companyId;
    private String companyName;
    private String companyPhoneNumber;
    private String companyAddress;
    private String assetId;
    private String assetTitle;
    private String assetCode;
    public PurchaseShippingDTO(){}
//    public PurchaseShippingDTO(String id, String companyId,String assetId, String companyName, String companyPhoneNumber, String companyAddress, String assetTitle, String assetCode) {
//        this.id = id;
//        this.companyId = companyId;
//        this.companyName = companyName;
//        this.companyPhoneNumber = companyPhoneNumber;
//        this.companyAddress = companyAddress;
//        this.assetId=assetId;
//        this.assetTitle = assetTitle;
//        this.assetCode = assetCode;
//    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public void setAssetId(ObjectId assetId) {
        this.assetId = assetId.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyPhoneNumber() {
        return companyPhoneNumber;
    }

    public void setCompanyPhoneNumber(String companyPhoneNumber) {
        this.companyPhoneNumber = companyPhoneNumber;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        companyAddress = companyAddress;
    }

    public String getAssetTitle() {
        return assetTitle;
    }

    public void setAssetTitle(String assetTitle) {
        this.assetTitle = assetTitle;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }
}
