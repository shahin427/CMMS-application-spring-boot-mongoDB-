package org.sayar.net.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class TechnicianAssessment {
    @Id
    private String id;
    private String commenter;
    private String userId;
    private double point;
    private String workRequestId;
}
