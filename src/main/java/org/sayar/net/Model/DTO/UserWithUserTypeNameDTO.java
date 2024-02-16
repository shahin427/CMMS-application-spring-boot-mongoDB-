package org.sayar.net.Model.DTO;

import lombok.Data;

@Data
public class UserWithUserTypeNameDTO {
    private String id;
    private String name;
    private String family;
    private String userTypeId;
    private String userTypeName;
}
