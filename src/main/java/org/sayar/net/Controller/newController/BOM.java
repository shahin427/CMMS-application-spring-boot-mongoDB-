package org.sayar.net.Controller.newController;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BOM {
    @Id
    private String id;
    private String code;
    private String bomGroupName;
    private List<BOMPart> bomPartList;
    private List<BOMAsset> bomAssetList;
}
