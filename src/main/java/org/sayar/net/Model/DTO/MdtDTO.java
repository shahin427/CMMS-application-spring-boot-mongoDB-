package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.Enum.Range;

import java.util.Date;

@Data
public class MdtDTO {
    private String assetId;
    private Date from;
    private Date until;
    private Range range;
}
