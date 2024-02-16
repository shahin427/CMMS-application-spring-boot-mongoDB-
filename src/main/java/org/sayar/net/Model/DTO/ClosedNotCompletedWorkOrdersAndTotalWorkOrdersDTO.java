package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class ClosedNotCompletedWorkOrdersAndTotalWorkOrdersDTO {
    private long numberOfTotalWorkOrderOfUser;
    private long numberOfAllClosedNotCompletedWorkOrdersByUserId;

    public static ClosedNotCompletedWorkOrdersAndTotalWorkOrdersDTO
    map(long numberOfTotalWorkOrderOfUser, long numberOfAllClosedNotCompletedWorkOrdersByUserId) {

        ClosedNotCompletedWorkOrdersAndTotalWorkOrdersDTO closedNotCompletedWorkOrdersAndTotalWorkOrdersDTO =
                new ClosedNotCompletedWorkOrdersAndTotalWorkOrdersDTO();

        closedNotCompletedWorkOrdersAndTotalWorkOrdersDTO.setNumberOfAllClosedNotCompletedWorkOrdersByUserId
                (numberOfAllClosedNotCompletedWorkOrdersByUserId);

        closedNotCompletedWorkOrdersAndTotalWorkOrdersDTO.setNumberOfTotalWorkOrderOfUser(numberOfTotalWorkOrderOfUser);

        return closedNotCompletedWorkOrdersAndTotalWorkOrdersDTO;
    }
}
