package org.sayar.net.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class SearchBoxSelected {
    @Id
    private String id;
    private boolean title;
    private boolean code;
    private boolean statusId;
    private boolean assetId;
    private boolean projectId;
    private boolean priority;
    private boolean maintenanceType;
    private boolean fromSchedule;
    private boolean startDate;
    private boolean endDate;
}
