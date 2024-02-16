package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.WarrantyType;
import org.sayar.net.Model.newModel.UnitOfMeasurement;

@Data
public class WarrantyDTO {
    private String id;
    private WarrantyType warrantyType;
    private UnitOfMeasurement unitOfMeasurement;
}
