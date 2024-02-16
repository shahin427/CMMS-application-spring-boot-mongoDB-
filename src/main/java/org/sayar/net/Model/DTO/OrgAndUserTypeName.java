package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrgAndUserTypeName {
    private String organizationId;
    private String organizationName;
    private List<UserTypeNameAndIdDTO> userTypeList;
}
