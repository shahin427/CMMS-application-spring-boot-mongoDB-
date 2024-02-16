package org.sayar.net.Service.newService;

import lombok.Data;

@Data
public class CountAllClosedWorkOrdersByUserId {
    private long numberOfAllWorkOrderListExceptDraftForUser;
    private long countAllClosedWorkOrdersByUserId;

    public static CountAllClosedWorkOrdersByUserId map(long numberOfAllWorkOrderListExceptDraftForUser, long countAllClosedWorkOrdersByUserId) {
        CountAllClosedWorkOrdersByUserId countAllClosedWorkOrdersByUserId1 = new CountAllClosedWorkOrdersByUserId();
        countAllClosedWorkOrdersByUserId1.setCountAllClosedWorkOrdersByUserId(countAllClosedWorkOrdersByUserId);
        countAllClosedWorkOrdersByUserId1.setNumberOfAllWorkOrderListExceptDraftForUser
                (numberOfAllWorkOrderListExceptDraftForUser);
        return countAllClosedWorkOrdersByUserId1;

    }
}
