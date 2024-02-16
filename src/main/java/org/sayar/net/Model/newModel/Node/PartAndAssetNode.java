package org.sayar.net.Model.newModel.Node;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PartAndAssetNode {

    private long id;
    private String name;
    private String code;
    private String accountTitle;
    private String chargeDepartmentTitle;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @JsonIgnore
    private long childCount;
//    private List<BaseModel> childCount;

//    public boolean getChildCount() {
//        return childCount;
//    }
//
//    public void setChildCount(Object childCount) {
//        if (childCount!=null)
//            this.childCount = true;
//        else {
//            this.childCount = false;
//        }
//    }


//    public List<Long> getChild() {
//        return child;
//    }
//
//    public void setChild(List<Long> child) {
//        this.child = child;
//    }


    public long getChildCount() {
        return childCount;
    }

    public void setChildCount(Object childCount) {
        if (childCount!=null)
            this.childCount = (long)childCount;
        else {
            this.childCount = 0;
        }
    }

    public PartAndAssetNode() {
    }

    public long getId() {
        return id;
    }

    public void setId(Object id) {
        if (id!=null){

            this.id = (Long) id;
        }
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

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getChargeDepartmentTitle() {
        return chargeDepartmentTitle;
    }

    public void setChargeDepartmentTitle(String chargeDepartmentTitle) {
        this.chargeDepartmentTitle = chargeDepartmentTitle;
    }

    @Override
    public boolean equals(Object obj) {
        PartAndAssetNode temp = (PartAndAssetNode)obj;
        PartAndAssetNode temp2 = (PartAndAssetNode) this;
        return temp.getId() == temp2.getId();
    }

}
