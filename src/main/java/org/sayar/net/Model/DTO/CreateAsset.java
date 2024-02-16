package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.AssetTemplate;
import org.sayar.net.Model.newModel.Document.DocumentFile;
@Data
public class CreateAsset {
    private String id;
    private String name;
//    private String assetTemplateName;
    private AssetTemplate assetTemplate;
    private boolean status;
    private String code;
    private DocumentFile image;

}
