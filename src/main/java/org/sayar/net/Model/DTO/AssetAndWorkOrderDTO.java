package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

@Data
public class AssetAndWorkOrderDTO {
    private WorkOrder workOrder;
    private Asset asset;
}
