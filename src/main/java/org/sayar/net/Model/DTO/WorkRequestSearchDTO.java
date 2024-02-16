package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.WorkRequestStatus;

@Data
public class WorkRequestSearchDTO {
    private String name;
    private String assetId;
    private WorkRequestStatus workRequestStatus;
}
