package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Image;
import org.sayar.net.Model.newModel.OrganManagment.Organization;

import java.util.List;
@Data
public class UserMainInformationUpdateDTO {
    private String name;
    private String family;
    private String nationalCode;
    private DocumentFile image;
    private String username;
    private String password;
    private String resetPasswordCode;
    private String messageId;
    private String userTypeId;
    private List<String> organizationIdList;
}
