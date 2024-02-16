package org.sayar.net.Model.newModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Location.Address.Address;
import org.springframework.data.annotation.Id;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Storage {
    @Id
   private String id;
   private String title;
   private String code;
   private Address address;
   private String assetId;
   private Boolean hasChild;
   private List<String> inventoryList;
}
