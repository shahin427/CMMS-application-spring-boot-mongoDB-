package org.sayar.net.Dao;

import lombok.Data;
import org.sayar.net.Controller.newController.BOMPart;

import java.util.List;

@Data
public class BOMPartDTO {
    private String partId;
    private String partName;
    private String partCode;
    private long partQuantity;
}
