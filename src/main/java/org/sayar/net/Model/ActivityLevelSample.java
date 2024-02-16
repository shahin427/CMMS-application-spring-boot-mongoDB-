package org.sayar.net.Model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.sayar.net.Model.Mongo.MyModel.ActivityLevel;

import javax.validation.constraints.NotNull;

@Data
public class ActivityLevelSample {
    @NotNull
    private String id;
    private String title;
    private String recipe;
    private String orgId;
    private ObjectId formId;
    private UserType userType;
    private String form;
    private ActivityLevel nextActivityLevel;
    private ActivityLevel prevActivityLevel;
    private Boolean firstStepIs;
    private Boolean lastStepIs;
    private Boolean showHistory;
    private Boolean canOperate;
    private String organizationName;
    private String formTitle;
    private String userId;
    private String userTypeId;
    private String organizationId;
    private String levelAction = "waiting";
    private boolean cancel;
}
