package org.sayar.net.Model.newModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitOfMeasurement {
    @Id
    private String id;
    protected String unit;
    private String title;
}
