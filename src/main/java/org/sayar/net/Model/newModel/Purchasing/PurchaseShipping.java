package org.sayar.net.Model.newModel.Purchasing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
//@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseShipping {
    @Id
    private String id;
    private ObjectId companyId;
    private ObjectId assetId;
    public enum FN{
        id,
        companyId,
        assetId
    }

    public PurchaseShipping(){}

    public PurchaseShipping(String id, ObjectId companyId, ObjectId assetId) {
        this.id = id;
        this.companyId = companyId;
        this.assetId = assetId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ObjectId getCompanyId() {
        return companyId;
    }

    public String getCompanyIdString() {
        if (companyId!=null)
        return companyId.toString();
        return null;
    }

    public void setCompanyId(ObjectId companyId) {
        this.companyId = companyId;
    }

    public void setCompanyIdString(String companyId) {
        this.companyId = new ObjectId(companyId);
    }

    public ObjectId getAssetId() {
        return assetId;
    }

    public String getAssetIdString() {
        if (assetId!=null)
        return assetId.toString();
        return null;
    }
    public void setAssetId(ObjectId assetId) {
        this.assetId = assetId;
    }

    public void setAssetIdString(String assetId) {
        this.assetId = new ObjectId(assetId);
    }
}


