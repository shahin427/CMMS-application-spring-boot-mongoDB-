package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Company;
import org.sayar.net.Model.newModel.Enum.WarrantyType;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.Warranty;

import java.util.Date;

@Data
public class WarrantyGetOneDTO {
    private String id;
    private String name;
    private String warrantyCode;
    private Date time;
    private Date expiry;
    private WarrantyType type;
    private String kilometerWarranty;
    private String description;
    private String companyId;
    private String companyName;
    private String unitOfMeasurementId;
    private String unitOfMeasurementName;

    public static WarrantyGetOneDTO map(Warranty savedWarranty, Company company, UnitOfMeasurement unitOfMeasurement) {
        WarrantyGetOneDTO warrantyGetOneDTO = new WarrantyGetOneDTO();
        warrantyGetOneDTO.setId(savedWarranty.getId());
        warrantyGetOneDTO.setName(savedWarranty.getName());
        warrantyGetOneDTO.setWarrantyCode(savedWarranty.getWarrantyCode());

        if (savedWarranty.getTime() != null)
            warrantyGetOneDTO.setTime(savedWarranty.getTime());

        if (savedWarranty.getExpiry() != null)
            warrantyGetOneDTO.setExpiry(savedWarranty.getExpiry());

        if (savedWarranty.getKilometerWarranty() != null)
            warrantyGetOneDTO.setKilometerWarranty(savedWarranty.getKilometerWarranty());

        if (savedWarranty.getDescription() != null)
            warrantyGetOneDTO.setDescription(savedWarranty.getDescription());

        if (savedWarranty.getCompanyId() != null)
            warrantyGetOneDTO.setCompanyId(savedWarranty.getCompanyId());

        if (savedWarranty.getType() != null)
            warrantyGetOneDTO.setType(savedWarranty.getType());

        if (savedWarranty.getUnitOfMeasurementId() != null)
            warrantyGetOneDTO.setUnitOfMeasurementId(savedWarranty.getUnitOfMeasurementId());

        if (company != null && company.getName() != null)
            warrantyGetOneDTO.setCompanyName(company.getName());

        if (unitOfMeasurement != null && unitOfMeasurement.getTitle() != null)
            warrantyGetOneDTO.setUnitOfMeasurementName(unitOfMeasurement.getTitle());

        return warrantyGetOneDTO;
    }
}
