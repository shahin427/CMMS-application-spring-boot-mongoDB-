package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Enumes.AssetPriority;
import org.sayar.net.Model.newModel.CategoryType;

@Data
public class AssetDTOForChildren {
    private String id;
    private String name;
    private String code;
    private String categoryId;
    private String categoryTitle;
    private String assetTemplateId;
    private String assetTemplateName;
    public CategoryType categoryType;
    private String isPartOfAsset;
    private boolean hasChild;
    private boolean status;
    private AssetPriority assetPriority;

//    public static List<AssetDTOForChildren> map(List<Asset> assetList) {
//        List<AssetDTOForChildren> assetDTOForChildrenList = new ArrayList<>();
//        assetList.forEach(asset -> {
//            AssetDTOForChildren assetDTOForChildren = new AssetDTOForChildren();
//            assetDTOForChildren.setId(asset.getId());
//            assetDTOForChildren.setName(asset.getName());
//            assetDTOForChildren.setCategoryType(asset.getCategoryType());
//            assetDTOForChildren.setCode(asset.getCode());
//            assetDTOForChildren.setHasChild(asset.getHasChild());
//            assetDTOForChildren.setIsPartOfAsset(asset.getIsPartOfAsset());
//            assetDTOForChildren.setStatus(asset.getStatus());
//            assetDTOForChildrenList.add(assetDTOForChildren);
//        });
//        return assetDTOForChildrenList;
//    }
}
