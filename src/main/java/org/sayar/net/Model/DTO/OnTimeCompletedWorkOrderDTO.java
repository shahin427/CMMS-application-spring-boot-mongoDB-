package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class OnTimeCompletedWorkOrderDTO {
    private long numberOfClosedWorkOrderLis;
    private long numberOfOnTimeCompletedWorkOrders;

    public static OnTimeCompletedWorkOrderDTO map(long numberOfClosedWorkOrderList, long numberOfOnTimeCompletedWorkOrders) {
        OnTimeCompletedWorkOrderDTO onTimeCompletedWorkOrderDTO = new OnTimeCompletedWorkOrderDTO();
        onTimeCompletedWorkOrderDTO.setNumberOfClosedWorkOrderLis(numberOfClosedWorkOrderList);
        onTimeCompletedWorkOrderDTO.setNumberOfOnTimeCompletedWorkOrders(numberOfOnTimeCompletedWorkOrders);
        return onTimeCompletedWorkOrderDTO;
    }
}
