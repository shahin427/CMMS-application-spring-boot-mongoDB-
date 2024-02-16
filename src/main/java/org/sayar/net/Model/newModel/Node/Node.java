package org.sayar.net.Model.newModel.Node;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private Object data;
    private boolean leaf;
    private List<Node> children = new ArrayList<>();

    public Node(Object data,boolean leaf) {
        this.data = data;
        this.leaf = leaf;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public static List<Node> companyConverter(Object input){
        List<Object> list = (List<Object>)input;
        List<Node> result = new ArrayList<>();
        Node temp;
        PartAndAssetNode partAndAssetNode;

        for (Object c:
             list) {
            partAndAssetNode = (PartAndAssetNode)c;
            temp = new Node(
                    c,
                    partAndAssetNode.getChildCount()!=0? true:false
//                    false
            );
            result.add(temp);
        }

        return result;
    }
//    public static List<Node> companyConverterFilter(Object input){
//        List<Object> list = (List<Object>)input;
//        List<Node> result = new ArrayList<>();
//        Node temp;
//        AssetFilterNode assetFilterNode;
//
//        for (Object c:
//             list) {
//            assetFilterNode = (AssetFilterNode) c;
//            temp = new Node(
//                    c,
////                    assetFilterNode.getChildCount()!=0? true:false
//                    false
//            );
//            result.add(temp);
//        }
//
//        return result;
//    }
//

}
