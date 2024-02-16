package org.sayar.net.Service.newService;

import lombok.Data;
import org.sayar.net.Model.DTO.OverDueAndNotCompletedWorkOrdersDTO;

@Data
public class OverDueAndCompletedWorkOrdersDTO {

    private long numberOfClosedWorkOrderList;
    private long numberOfOverDueAndCompletedWorkOrders;

    public static OverDueAndCompletedWorkOrdersDTO map(long numberOfClosedWorkOrderList, long numberOfOverDueAndCompletedWorkOrders) {
        OverDueAndNotCompletedWorkOrdersDTO overDueAndNotCompletedWorkOrdersDTO = new OverDueAndNotCompletedWorkOrdersDTO();
        return null;
    }
}
