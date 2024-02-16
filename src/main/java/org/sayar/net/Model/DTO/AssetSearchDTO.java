package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Enumes.AssetPriority;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.Enum.StatusOfAsset;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetSearchDTO {
    private String id;
    private String name;
    private String code;
    private StatusOfAsset status;
    private CategoryType categoryType;
    private AssetPriority assetPriority;
    private String categoryId;
//--------------------------------
    private String assetTemplateId;
    private String parentLocationId;
    private String userId;
    private String userTypeId;
}
