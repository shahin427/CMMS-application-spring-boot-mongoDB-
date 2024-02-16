package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Enumes.AssetPriority;
import org.sayar.net.Model.Asset.AssetTemplate;
import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssetFilterDTO {
    private String id;
    private String name;
    private String code;
    private Boolean status;
    private CategoryType categoryType;
    private AssetPriority assetPriority;
    private String categoryId;
    private String categoryTitle;
    //------------------------------
    private AssetTemplate assetTemplate;

    public static List<AssetFilterDTO> map(List<Category> categoryList, List<AssetTemplate> assetTemplateList, List<AssetDTO> assetDTOS) {
        List<AssetFilterDTO> assetFilterDTOS = new ArrayList<>();
        assetDTOS.forEach(assetDTO -> {
            AssetFilterDTO assetFilterDTO = new AssetFilterDTO();
            assetFilterDTO.setId(assetDTO.getId());
            assetFilterDTO.setName(assetDTO.getName());
            assetFilterDTO.setCategoryType(assetDTO.getCategoryType());
            assetFilterDTO.setCode(assetDTO.getCode());
            assetFilterDTO.setStatus(assetDTO.getStatus());
            assetFilterDTO.setAssetPriority(assetDTO.getAssetPriority());
            assetFilterDTO.setAssetTemplate(assetTemplateList.stream().filter(assetTemplate1 ->
                    assetTemplate1.getId().equals(assetDTO.getAssetTemplateId())).findFirst().orElse(null));
            categoryList.forEach(category -> {
                if (category.getId().equals(assetDTO.getCategoryId())) {
                    assetFilterDTO.setCategoryId(category.getId());
                    assetFilterDTO.setCategoryTitle(category.getTitle());
                }
            });
            assetFilterDTOS.add(assetFilterDTO);
        });
        return assetFilterDTOS;
    }
}
