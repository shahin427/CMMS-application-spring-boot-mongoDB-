package org.sayar.net.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.DTO.OrgAndUserType;
import org.sayar.net.Model.newModel.Contact;
import org.sayar.net.Model.newModel.Image;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    //مدل کاربران
    @Id
    private String id;
    private String username;
    private String family;
    private String name;
    private String nationalCode;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date birthDay;
    private Contact userContact;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startWork;
    private String fatherName;
    private String password;
    private String resetPasswordCode;
    private Image image;
    private List<String> assets;
    private String parentUserId;
    private List<String> documentIdList;
    private String messageId;
    private Boolean deleted;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date registerDate;
    private ResetPasswordToken resetPasswordToken;
    private String userTypeId;
    private Boolean active;

    private List<String> organizationIdList;
    private List<OrgAndUserType> orgAndUserTypeList;

    public User(String username, String hashPassword) {
        this.username = username;
        this.password = hashPassword;
    }

    public enum FN {
        assets, signerCertPath, timestamp, profile, creationTimestamp, orgId,
        expertise, userType, image, userContact, userTypeId, resetPasswordCode, password, fatherName, startWork,
        lastSignIn, birthDay, nationalCode, name, organId, family, username, id, documentIdList, deleted
    }

    public User() {
    }
}
