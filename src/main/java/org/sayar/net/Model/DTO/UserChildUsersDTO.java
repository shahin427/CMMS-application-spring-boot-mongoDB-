package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserChildUsersDTO {
    private String userId;
    private String parentUserId;
}
