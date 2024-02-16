package org.sayar.net.Model;

import lombok.Data;
import org.sayar.net.General.enums.DashboardAccessEnum;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class DashboardAccess {
    //creator should be got by token
    // userId will be sent by client side
    @Id
    private String id;
    private String creatorId;
    private String userId;
    private List<DashboardAccessEnum> dashboardAccessEnumList;
    private DashboardAccessAssociatedMonth dashboardAccessAssociatedMonth;
}
