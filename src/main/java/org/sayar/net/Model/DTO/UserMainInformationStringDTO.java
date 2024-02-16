package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.Image;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserMainInformationStringDTO {
    private String id;
    private String name;
    private String family;
    private String nationalCode;
    private Image image;
    private String username;
    private String password;
    private String resetPasswordCode;
    private String messageId;
    private String userTypeId;
    private String userTypeName;
    private Boolean active;


    private List<OrgAndUserType> orgAndUserTypeList;
    private List<String> organizationIdList;
}
