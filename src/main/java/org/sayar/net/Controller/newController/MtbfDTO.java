package org.sayar.net.Controller.newController;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.Range;

import java.util.Date;

@Data
public class MtbfDTO {
    private String assetId;
    private Date from;
    private Date until;
    private Range range;
}
