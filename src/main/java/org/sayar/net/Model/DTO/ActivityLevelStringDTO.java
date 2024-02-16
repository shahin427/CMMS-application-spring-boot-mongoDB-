package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class ActivityLevelStringDTO {
    private String pendingUserId;
    private List<String> activeUserIdList;

    public static ActivityLevelStringDTO map(List<String> activeUserIdList, String pendingUserId) {
        ActivityLevelStringDTO activityLevelStringDTO = new ActivityLevelStringDTO();
        activityLevelStringDTO.setPendingUserId(pendingUserId);
        activityLevelStringDTO.setActiveUserIdList(activeUserIdList);
        return activityLevelStringDTO;
    }
}
