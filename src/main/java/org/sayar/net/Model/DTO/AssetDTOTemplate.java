package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.Asset.AssetTemplate;
import org.sayar.net.Model.Asset.Property;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetDTOTemplate {
    private List<Property> propertyList;

    public static AssetDTOTemplate map(AssetTemplate assetTemplate) {
        AssetDTOTemplate assetDTOTemplate = new AssetDTOTemplate();
        if (assetTemplate.getProperties() != (null)) {
            assetDTOTemplate.setPropertyList(assetTemplate.getProperties());
            return assetDTOTemplate;
        } else
            return null;
    }
}
