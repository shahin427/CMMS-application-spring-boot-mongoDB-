package org.sayar.net.Model.newModel;

import lombok.Data;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.ArrayList;
import java.util.List;

@Data
public class ActivityHistory {

    @Autowired
    private static MongoOperations mongoOperations;
    @Id
    private String id;
    private List<String> activityLevelUserIdList;
    private String pendingUserId;
    private String activityId;
    private boolean active;
}
