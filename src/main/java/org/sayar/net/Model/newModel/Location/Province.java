package org.sayar.net.Model.newModel.Location;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Province {
    @Id
    private String id;
    private String name;
    private Location location;
}
