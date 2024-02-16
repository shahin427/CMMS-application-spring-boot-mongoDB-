package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.Image;
import org.sayar.net.Model.newModel.OrganManagment.Organization;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMainInformationDTO {
    private String id;
    private String name;
    private String family;
    private String nationalCode;
    private Image image;
    private String username;
    private String password;
    private String resetPasswordCode;
    private String messageId;
    private List<OrgAndUserTypeName> orgAndUserTypeList;
    private UserType userType;
    private List<Organization> organizationList;

}
