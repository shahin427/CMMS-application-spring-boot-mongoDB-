package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;

@Data
public class UserAndUserTypDTO {
    private User user;
    private UserType userType;

    public UserAndUserTypDTO(User user, UserType userType) {
        this.user = user;
        this.userType = userType;
    }
}
