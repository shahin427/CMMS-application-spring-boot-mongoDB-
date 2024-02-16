package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class CountOverDueAndNotCompletedWorkOrdersBySpecifiedTimeDTO {
    private long countNumberOfTotalOpenWorkOrderInSpecifiedTime;
    private long countOverDueAndOpenWorkOrdersBySpecifiedTime;

    public static CountOverDueAndNotCompletedWorkOrdersBySpecifiedTimeDTO map(long numberOfOpenWorkOrderOfUserInSpecifiedMonth,
                                                                              long numberOfOpenAndOverDueWorkOrderOfUserInSpecifiedTime) {

        CountOverDueAndNotCompletedWorkOrdersBySpecifiedTimeDTO countOverDueAndNotCompletedWorkOrdersBySpecifiedTimeDTO = new CountOverDueAndNotCompletedWorkOrdersBySpecifiedTimeDTO();

        countOverDueAndNotCompletedWorkOrdersBySpecifiedTimeDTO.
                setCountNumberOfTotalOpenWorkOrderInSpecifiedTime(numberOfOpenWorkOrderOfUserInSpecifiedMonth);

        countOverDueAndNotCompletedWorkOrdersBySpecifiedTimeDTO.
                setCountOverDueAndOpenWorkOrdersBySpecifiedTime(numberOfOpenAndOverDueWorkOrderOfUserInSpecifiedTime);

        return countOverDueAndNotCompletedWorkOrdersBySpecifiedTimeDTO;
    }
}
