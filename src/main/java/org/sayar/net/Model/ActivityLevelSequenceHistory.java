package org.sayar.net.Model;

import lombok.Data;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Date;

@Data
public class ActivityLevelSequenceHistory {
    private String levelId;
    private String status;
    private String formData;
    private Date levelEndDate;
    private String workOrderAndFormRepositoryId;
}
