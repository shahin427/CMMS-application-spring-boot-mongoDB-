package org.sayar.net.Service.newService;

import lombok.Data;

@Data
public class ClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonthAndTotalWorkOrderListDTO {
    private long numberOfClosedWorkOrderList;
    private  long NumberOfClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth;

    public static ClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonthAndTotalWorkOrderListDTO
    map(long numberOfClosedWorkOrderList, long numberOfClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth) {

        ClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonthAndTotalWorkOrderListDTO closedNotCompletedWorkOrdersByUserIdBySpecifiedMonthAndTotalWorkOrderListDTO=
                new ClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonthAndTotalWorkOrderListDTO();

        closedNotCompletedWorkOrdersByUserIdBySpecifiedMonthAndTotalWorkOrderListDTO.
                setNumberOfClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth
                        (numberOfClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth);

        closedNotCompletedWorkOrdersByUserIdBySpecifiedMonthAndTotalWorkOrderListDTO.
                setNumberOfClosedWorkOrderList(numberOfClosedWorkOrderList);

        return closedNotCompletedWorkOrdersByUserIdBySpecifiedMonthAndTotalWorkOrderListDTO;
    }
}
