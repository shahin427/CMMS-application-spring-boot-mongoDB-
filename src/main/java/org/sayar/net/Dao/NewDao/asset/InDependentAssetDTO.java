package org.sayar.net.Dao.NewDao.asset;

import lombok.Data;
import org.sayar.net.Enumes.AssetPriority;

@Data
public class InDependentAssetDTO {
    private String id;
    private String name;
    private String code;
    private AssetPriority assetPriority;
    private String categoryType;
    private String categoryId;
    private String categoryTitle;

    //------------------
    private boolean status;
    private boolean hasChild;
    private String assetTemplateId;
    private String assetTemplateName;
}
