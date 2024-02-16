package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.WarrantyType;

import java.util.Date;

@Data
public class WarrantyGetAllDTO {
    private String id;
    private String name;
    private String warrantyCode;
    private Date time;
    private Date expiry;
    private String kilometerWarranty;
    private WarrantyType type;
    private String companyName;
    private String unitOfMeasurementName;
}
