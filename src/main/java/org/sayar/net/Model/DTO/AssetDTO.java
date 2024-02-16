package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Enumes.AssetPriority;
import org.sayar.net.Model.newModel.CategoryType;

@Data
public class AssetDTO {
    private String id;
    private String name;
    private String code;
    private Boolean status;
    private AssetPriority assetPriority;
    private CategoryType categoryType;
    private String categoryId;

    //__________________
    private String assetTemplateId;
    private String assetTemplateName;
    private String parentLocationId;
    private String parentLocationName;

    public AssetDTO() {
    }
}
