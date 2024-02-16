package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Contact;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSecondaryInformationDTO {
    private String userId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date birthDay;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startWork;
    private String fatherName;
    private Contact userContact;

    public static UserSecondaryInformationDTO map(User user) {
        UserSecondaryInformationDTO userSecondaryInformationDTO = new UserSecondaryInformationDTO();
        userSecondaryInformationDTO.setUserId(user.getId());
        userSecondaryInformationDTO.setBirthDay(user.getBirthDay());
        userSecondaryInformationDTO.setFatherName(user.getFatherName());
        userSecondaryInformationDTO.setStartWork(user.getStartWork());
        userSecondaryInformationDTO.setUserContact(user.getUserContact());
        return userSecondaryInformationDTO;
    }
}
