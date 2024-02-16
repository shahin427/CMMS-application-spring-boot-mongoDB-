package org.sayar.net.Controller.newController;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class BOMPart {
    private String partId;
    private long partQuantity;
}
