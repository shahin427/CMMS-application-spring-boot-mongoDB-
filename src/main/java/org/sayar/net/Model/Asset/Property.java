package org.sayar.net.Model.Asset;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.DTO.ValueType;
import org.sayar.net.Model.newModel.Enum.PropertyType;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property {
    @Id
    private String id;
    private String key;
    private List<String> repository;
    private PropertyType type;
    private List<String> data;
    private String propertyCategoryId;
    private String parentCategoryId;
    private ValueType valueType;

    public enum ME {
        id,
        key,
        repository,
        type,
        data
    }
}
