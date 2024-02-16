package org.sayar.net.Model;

import lombok.Data;

@Data
public class DashboardAccessAssociatedMonth {
   private long managerDashboardHighestPriorityWorkOrderTime;
   private long managerDashboardHighPriorityWorkOrderTime;
   private long managerDashboardAveragePriorityWorkOrderTime;
   private long managerDashboardLowPriorityWorkOrderTime;
   private long managerDashboardLowestPriorityWorkOrderTime;
   private long managerDashboardOpenWorkOrderTime;
   private long managerDashboardClosedWorkOrderTime;
   private long managerDashboardPendingWorkOrderTime;
   private long managerDashboardDraftWorkOrderTime;
   private long managerDashboardOverDueWorkOrderTime;
   private long managerDashboardHighestPriorityPlannedWorkOrderTime;
   private long managerDashboardHighPriorityPlannedWorkOrderTime;
   private long managerDashboardAveragePriorityPlannedWorkOrderTime;
   private long managerDashboardLowPriorityPlannedWorkOrderTime;
   private long managerDashboardLowestPriorityPlannedWorkOrderTime;
   private long managerDashboardOpenPlannedWorkOrderTime;
   private long managerDashboardClosedPlannedWorkOrderTime;
   private long managerDashboardPendingPlannedWorkOrderTime;
   private long managerDashboardDraftPlannedWorkOrderTime;
   private long managerDashboardOverDuePlannedWorkOrderTime;
   private long managerDashboardPlannedWorkOrderRatioTime;
}
