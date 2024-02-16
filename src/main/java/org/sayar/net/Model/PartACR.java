package org.sayar.net.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class PartACR {
    @Id
    private String id;
    private String assetName;
    private String assetCode;
    private String quantity;
    private String partId;
}
