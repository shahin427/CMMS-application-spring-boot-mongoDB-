package org.sayar.net.Model.newModel.Location.Address;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Location.Location;
import org.springframework.data.annotation.Id;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    @Id
    private String id;
    private String description;
    private Location location;
    private String cityId;
    private String provinceId;
//    private String column;
//    private String row;
//    private String bin;
//    private String postalCode;
//    @DBRef
//    private City city;
//    @DBRef
//    private Province province;
//    private Boolean forDBRef;
}

