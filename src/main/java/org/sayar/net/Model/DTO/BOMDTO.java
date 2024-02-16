package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Controller.newController.BOMAsset;
import org.sayar.net.Controller.newController.BOMPart;

import java.util.List;

@Data
public class BOMDTO {
    private String bomGroupName;
    private List<BOMPart> bomPartList;
    private List<BOMAsset> bomAssetList;
    private long partNumber;
}
