package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class CountOnTimeCompletedWorkOrdersBySpecifiedMonthDTO {
    private long numberOfClosedWorkOrders;
    private long numberOfOnTimeCompletedWorkOrdersBySpecifiedMonth;

    public static CountOnTimeCompletedWorkOrdersBySpecifiedMonthDTO map(long numberOfClosedWorkOrders, long numberOfOnTimeCompletedWorkOrdersBySpecifiedMonth) {
        CountOnTimeCompletedWorkOrdersBySpecifiedMonthDTO countOnTimeCompletedWorkOrdersBySpecifiedMonthDTO = new CountOnTimeCompletedWorkOrdersBySpecifiedMonthDTO();
        countOnTimeCompletedWorkOrdersBySpecifiedMonthDTO.setNumberOfClosedWorkOrders(numberOfClosedWorkOrders);
        countOnTimeCompletedWorkOrdersBySpecifiedMonthDTO.setNumberOfOnTimeCompletedWorkOrdersBySpecifiedMonth(numberOfOnTimeCompletedWorkOrdersBySpecifiedMonth);
        return countOnTimeCompletedWorkOrdersBySpecifiedMonthDTO;
    }
}
