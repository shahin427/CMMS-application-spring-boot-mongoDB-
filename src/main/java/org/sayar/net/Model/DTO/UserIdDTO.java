package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class UserIdDTO {
    private String id;

    public static UserIdDTO map(UserChildUsersDTO userChildUsersDTO1) {
        UserIdDTO userIdDTO = new UserIdDTO();
        userIdDTO.setId(userChildUsersDTO1.getUserId());
        return userIdDTO;
    }
}
