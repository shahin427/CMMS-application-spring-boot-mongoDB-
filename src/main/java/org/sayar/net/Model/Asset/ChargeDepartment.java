package org.sayar.net.Model.Asset;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargeDepartment {
    @Id
    private String id;
    private String title;
    private String code;
    private String description;
    private String orgId;
    private List<String> categories;//lookup
    private List<String> abilities;
    private String assetLocationId;//lookup
}
