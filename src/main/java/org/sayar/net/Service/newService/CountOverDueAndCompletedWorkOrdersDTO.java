package org.sayar.net.Service.newService;

import lombok.Data;
import org.sayar.net.Tools.Print;

@Data
public class CountOverDueAndCompletedWorkOrdersDTO {
    private long countNumberOfOverDueAndCompletedWorkOrdersAfterSpecifiedMonthDTO;
    private long countNumberOfTotalWorkOrdersAfterSpecifiedMonth;

    public static CountOverDueAndCompletedWorkOrdersDTO map(long numberOfTotalClosedWorkOrdersOfUserAfterSpecifiedMonth,
                                                            long numberOfOverDueAndCompletedWorkOrders) {
        CountOverDueAndCompletedWorkOrdersDTO countOverDueAndCompletedWorkOrdersDTO = new CountOverDueAndCompletedWorkOrdersDTO();
        countOverDueAndCompletedWorkOrdersDTO.setCountNumberOfOverDueAndCompletedWorkOrdersAfterSpecifiedMonthDTO(numberOfTotalClosedWorkOrdersOfUserAfterSpecifiedMonth);
        countOverDueAndCompletedWorkOrdersDTO.setCountNumberOfTotalWorkOrdersAfterSpecifiedMonth(numberOfOverDueAndCompletedWorkOrders);
        Print.print("countOverDueAndCompletedWorkOrdersDTO", countOverDueAndCompletedWorkOrdersDTO);
        return countOverDueAndCompletedWorkOrdersDTO;
    }
}
