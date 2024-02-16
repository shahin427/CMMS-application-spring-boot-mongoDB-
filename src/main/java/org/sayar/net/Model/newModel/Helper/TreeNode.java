package org.sayar.net.Model.newModel.Helper;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;

import java.util.ArrayList;
import java.util.List;

@Data
public class TreeNode {

    private String id;
    private List<TreeNode> children;
    private Object data;
    private String title;
    private String code;
    private String chTitle;
    private String accountTitle;

    public TreeNode() {
    }

    public TreeNode(Object input) {

        if (input instanceof Asset){

            Asset asset =(Asset)input;
            TreeNode node2 =convertAssetToNode(asset);
            this.setChildren(node2.getChildren());
//            this.id = asset.getId();
            this.setTitle(node2.getTitle());
            this.setCode(asset.getCode());

//            if (asset.getAccount() != null)
//                this.setAccountTitle(asset.getAccount().getTitle());
//            if (asset.getChargeDepartment() != null)
//                this.setChTitle(asset.getChargeDepartment().getTitle());
            asset.setAssets(null);
            this.setData(asset);

        }
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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

    public String getChTitle() {
        return chTitle;
    }

    public void setChTitle(String chTitle) {
        this.chTitle = chTitle;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    private TreeNode convertAssetToNode(Asset asset){

        TreeNode node2= new TreeNode();
//        node2.setId(asset.getId());
//        node2.setTitle(asset.getTitle());
        node2.setCode(asset.getCode());

//        if (asset.getChargeDepartment() != null)
//            node2.setChTitle(asset.getChargeDepartment().getTitle());
//        if (asset.getAccount() != null)
//            node2.setAccountTitle(asset.getAccount().getTitle());



        if (asset.getAssets().size()!=0){
            TreeNode node;
            List<TreeNode> children = new ArrayList<>();
//            for (Asset a:
//                 asset.getAssets()) {
//                node = convertAssetToNode(a);
//                children.add(node);
//            }

            node2.setChildren(children);
        }

        asset.setAssets(null);
        node2.setData(asset);
        return node2;

    }


}
