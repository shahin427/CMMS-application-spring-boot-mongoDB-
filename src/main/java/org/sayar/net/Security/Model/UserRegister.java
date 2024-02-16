package org.sayar.net.Security.Model;

import lombok.Data;

@Data
public class UserRegister {
    private String username;
    private String password;
    private String organId;
    private String userTypeId;

}
