package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ChooseNextActivityLevelUserClass {

    private String activityInstanceId;
    private String activityLevelId;
    private List<String> userIdList;
    private boolean isChosenMechanic;
    private String userTypeId;
    private String workOrderId;
}
