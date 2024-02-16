package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO2 {

    private String id;
    private String name;
    private String username;
    private String family;
    private String userTypeId;
    private Date registerDate;


    public enum FN {
        ID, NAME, USERNAME, FAMILY, USERTYPEID, USERTYPENAME
    }
}
