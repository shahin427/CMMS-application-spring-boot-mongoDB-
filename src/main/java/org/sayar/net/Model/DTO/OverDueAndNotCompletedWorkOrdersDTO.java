package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class OverDueAndNotCompletedWorkOrdersDTO {
    private long numberOfAllWorkOrdersExceptClosed;
    private long notClosedAndOverDueWorkOrderList;

    public static OverDueAndNotCompletedWorkOrdersDTO map(long numberOfAllWorkOrdersExceptClosed, long notClosedAndOverDueWorkOrderList) {
        OverDueAndNotCompletedWorkOrdersDTO overDueAndNotCompletedWorkOrdersDTO = new OverDueAndNotCompletedWorkOrdersDTO();
        overDueAndNotCompletedWorkOrdersDTO.setNumberOfAllWorkOrdersExceptClosed(numberOfAllWorkOrdersExceptClosed);
        overDueAndNotCompletedWorkOrdersDTO.setNotClosedAndOverDueWorkOrderList(notClosedAndOverDueWorkOrderList);
        return overDueAndNotCompletedWorkOrdersDTO;
    }
}
