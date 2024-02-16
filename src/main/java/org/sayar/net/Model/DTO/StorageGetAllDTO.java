package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Model.newModel.Location.Address.Address;
import org.springframework.data.annotation.Id;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StorageGetAllDTO {
    @Id
    private String id;
    private String title;
    private String code;
    private Address address;
    private String assetId;
    private String assetName;
    private Boolean hasChild;
    private List<String> inventoryList;
}
