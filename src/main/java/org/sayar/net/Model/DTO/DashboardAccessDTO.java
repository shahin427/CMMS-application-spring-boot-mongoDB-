package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class DashboardAccessDTO {

    private String userId;

    private boolean highestPriority;
    private int highestPriorityNumber;



    private boolean pendingMonth;
    private int pendingMonthNumber;

    private boolean onTimeCompletionRateMonth;
    private int onTimeCompletionRateMonthNumber;

    private boolean onTimeCompletionRate;
    private int onTimeCompletionRateNumber;

    private boolean overDueAndCompletedMonth;
    private int overDueAndCompletedMonthNumber;

    private boolean overDueAndNotCompletedMonth;
    private int overDueAndNotCompletedMonthNumber;

    private boolean overDueNotCompleted;
    private int overDueNotCompletedNumber;

    private boolean onTimeCompletedMonth;
    private int onTimeCompletedMonthNumber;

    private boolean onTimeCompleted;
    private int onTimeCompletedNumber;

    private boolean closeNotCompletedMonth;
    private int closeNotCompletedMonthNumber;

    private boolean closedMonth;
    private int closedMonthNumber;

    private boolean closed;
    private int closedNumber;

    private boolean highPriorityMonth;
    private int highPriorityMonthNumber;

    private boolean highPriority;
    private int highPriorityNumber;

    private boolean open;
    private int openNumber;

    private boolean openMonth;
    private int openMonthNumber;

    private boolean allWorkOrder;
    private int allWorkOrderNumber;

    private boolean allWorkOrderMonth;
    private int allWorkOrderMonthNumber;

    private boolean offlineAssets;
    private int offlineAssetsNumber;

    private boolean onlineAssets;
    private int onlineAssetsNumber;

    private boolean lowStockItems;
    private int lowStockItemsNumber;
}
