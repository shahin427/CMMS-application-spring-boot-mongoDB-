package org.sayar.net.Model.newModel.WorkOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class WorkOrderDataHelper {

    private Date startDate;
    private Date completionDate;

    public WorkOrderDataHelper(Date startDate, Date completionDate) {
        this.startDate = startDate;
        this.completionDate = completionDate;
    }
}
