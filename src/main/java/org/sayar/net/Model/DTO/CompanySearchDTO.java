package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class CompanySearchDTO {
    private String companyName;
    private String companyCode;
    private String provinceId;
    private String cityId;
}
