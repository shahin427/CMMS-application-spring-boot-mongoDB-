package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class TechnicianAssessmentDTO {
    private String commenter;
    private String userId;
    private long point;
    private String workRequestId;
}
