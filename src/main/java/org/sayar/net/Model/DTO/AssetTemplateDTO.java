package org.sayar.net.Model.DTO;

public class AssetTemplateDTO {
    private String id;
    private String title;
    private String parentCategoryTitle;
    private String parentCategoryId;
    private String subCategoryTitle;
    private String subCategoryId;
    private String assetTemplateId;
    private String assetTemplateTitle;
    public AssetTemplateDTO(){}
    public AssetTemplateDTO(String id, String title, String parentCategoryTitle, String parentCategoryId, String subCategoryTitle, String subCategoryId, String assetTemplateId, String assetTemplateTitle) {
        this.id = id;
        this.title = title;

        this.parentCategoryTitle = parentCategoryTitle;
        this.parentCategoryId = parentCategoryId;
        this.subCategoryTitle = subCategoryTitle;
        this.subCategoryId = subCategoryId;
        this.assetTemplateId = assetTemplateId;
        this.assetTemplateTitle = assetTemplateTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentCategoryTitle() {
        return parentCategoryTitle;
    }

    public void setParentCategoryTitle(String parentCategoryTitle) {
        this.parentCategoryTitle = parentCategoryTitle;
    }

    public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getSubCategoryTitle() {
        return subCategoryTitle;
    }

    public void setSubCategoryTitle(String subCategoryTitle) {
        this.subCategoryTitle = subCategoryTitle;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getAssetTemplateId() {
        return assetTemplateId;
    }

    public void setAssetTemplateId(String assetTemplateId) {
        this.assetTemplateId = assetTemplateId;
    }

    public String getAssetTemplateTitle() {
        return assetTemplateTitle;
    }

    public void setAssetTemplateTitle(String assetTemplateTitle) {
        this.assetTemplateTitle = assetTemplateTitle;
    }
}
