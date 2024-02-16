package org.sayar.net.Model.newModel;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Enum.WarrantyType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;



@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Warranty {
    @Id
    private String id;
    private String name;
    private String warrantyCode;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date time;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date expiry;
    private WarrantyType type;
    private String kilometerWarranty;
    private String description;
    private String companyId;
    private String unitOfMeasurementId;
    private String partId;
    private String assetId;
}