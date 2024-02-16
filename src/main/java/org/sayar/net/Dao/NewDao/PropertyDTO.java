package org.sayar.net.Dao.NewDao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.PropertyCategory;
import org.sayar.net.Model.newModel.Enum.PropertyType;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDTO {
    private String id;
    private String key;
    private List<String> repository;
    private PropertyType type;
    private List<String> data;
    private PropertyCategory propertyCategory;
}
