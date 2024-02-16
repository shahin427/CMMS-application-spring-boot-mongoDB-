package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

import java.util.List;

@Data
public class TotalAndOverDueCompletedWorkOrders {
    private List<WorkOrder> totalWorkOrderListOfUserInSpecificMonth ;
    private List<WorkOrder> OverDueAndCompletedWorkOrders;

    public static TotalAndOverDueCompletedWorkOrders map(List<WorkOrder> closedWorkOrderList, List<WorkOrder> overDueAndCompletedWorkOrders) {
           TotalAndOverDueCompletedWorkOrders totalAndOverDueCompletedWorkOrders=new TotalAndOverDueCompletedWorkOrders();
           totalAndOverDueCompletedWorkOrders.setOverDueAndCompletedWorkOrders(overDueAndCompletedWorkOrders);
           totalAndOverDueCompletedWorkOrders.setTotalWorkOrderListOfUserInSpecificMonth(closedWorkOrderList);
           return totalAndOverDueCompletedWorkOrders;
    }
}
