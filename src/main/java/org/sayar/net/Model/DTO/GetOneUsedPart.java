package org.sayar.net.Model.DTO;

import lombok.Data;

@Data

public class GetOneUsedPart {
    private String partId;
    private String partName;
    private String partCode;
    private Long usedNumber;
}
