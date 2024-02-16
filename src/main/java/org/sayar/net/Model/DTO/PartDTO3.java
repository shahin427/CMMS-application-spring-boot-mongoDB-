package org.sayar.net.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sayar.net.Model.newModel.Storage;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PartDTO3 {
    private String partId;
    private String inventoryLocationId;
    private String row;
    private String warehouse;
    private String corridor;
}
