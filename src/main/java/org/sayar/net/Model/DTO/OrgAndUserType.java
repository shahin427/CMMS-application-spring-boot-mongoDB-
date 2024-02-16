package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.List;

@Data
public class OrgAndUserType {
    private String organizationId;
    private List<String> userTypeList;
}
