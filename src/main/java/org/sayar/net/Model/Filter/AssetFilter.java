package org.sayar.net.Model.Filter;

import org.sayar.net.Model.Asset.Property;

public class AssetFilter {
    private String title;
    private String code;
    private String parentCategoryId;
    private String subCategoryId;
    private String budgetId;
    private String chargeDepartmentId;
    private Boolean status;
    private Long creationTimeFrom;
    private Long creationTimeTo;
    private Property property;
    private String assetTemplateId;

    public AssetFilter() {
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getAssetTemplateId() {
        return assetTemplateId;
    }

    public void setAssetTemplateId(String assetTemplateId) {
        this.assetTemplateId = assetTemplateId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getChargeDepartmentId() {
        return chargeDepartmentId;
    }

    public void setChargeDepartmentId(String chargeDepartmentId) {
        this.chargeDepartmentId = chargeDepartmentId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getCreationTimeFrom() {
        return creationTimeFrom;
    }

    public void setCreationTimeFrom(Long creationTimeFrom) {
        this.creationTimeFrom = creationTimeFrom;
    }

    public Long getCreationTimeTo() {
        return creationTimeTo;
    }

    public void setCreationTimeTo(Long creationTimeTo) {
        this.creationTimeTo = creationTimeTo;
    }
}
