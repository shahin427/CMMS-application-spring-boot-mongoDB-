package org.sayar.net.Service.Mongo.activityServices.activity;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.OrganManagment.Organization;

import java.util.List;

@Data
public class ConvertIdToObject {
    private String userId;
    private String username;
    private String userTypeId;
    private String userTypeName;
    private String organizationId;
    private String organizationName;

    public static ConvertIdToObject map(User user, UserType userType, Organization organization) {
        ConvertIdToObject convertIdToObject = new ConvertIdToObject();
        if (organization != null && organization.getName() != null) {
            convertIdToObject.setOrganizationId(organization.getId());
            convertIdToObject.setOrganizationName(organization.getName());
        }
        if (userType != null && userType.getName() != null) {
            convertIdToObject.setUserTypeId(userType.getId());
            convertIdToObject.setUserTypeName(userType.getName());
        }
        if (user != null && user.getName() != null) {
            convertIdToObject.setUserId(user.getId());
            convertIdToObject.setUsername(user.getName());
        }
        return convertIdToObject;
    }
}
