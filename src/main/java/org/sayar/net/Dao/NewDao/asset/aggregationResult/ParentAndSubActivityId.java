package org.sayar.net.Dao.NewDao.asset.aggregationResult;

public class ParentAndSubActivityId {
    private String id;
    private String parentCategoryId;
    private String subCategoryId;

    public ParentAndSubActivityId() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
