package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Location.Location;

@Data
public class AddressDTO {
    private String id;
    private String description;
    private Location location;
    private String cityId;
    private String cityName;
    private String provinceId;
    private String provinceName;
}
