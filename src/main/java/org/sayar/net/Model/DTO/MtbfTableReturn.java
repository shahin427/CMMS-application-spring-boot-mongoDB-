package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class MtbfTableReturn {
    private String assetId;
    private String assetName;
    private String assetCode;
    private long failureDuration;
    private long repairDuration;
    private long count;
    private Long mtbf;
}
