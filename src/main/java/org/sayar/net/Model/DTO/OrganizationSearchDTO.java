package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Location.Location;

import java.util.List;

@Data
public class OrganizationSearchDTO {

    private String id;
    private String name;
    private String organCode;
    private Location organLocation;
    private String cityId;
    private String provinceId;
    private List<String> userTypeIdList;
    private String parentOrganId;
}
