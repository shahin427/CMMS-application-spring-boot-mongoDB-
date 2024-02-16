package org.sayar.net.Model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PartPersonnelDTO {
    private String userId;
    private String userName;
    private String userFamily;
    private String userTypeId;
    private String userTypeName;
//    private String orgId;
//    private String orgName;

    public static List<PartPersonnelDTO> map(List<User> userList, List<UserType> userTypeList) {
        List<PartPersonnelDTO> personnelDTOList = new ArrayList<>();
        userList.forEach(user -> {
            PartPersonnelDTO partPersonnelDTO = new PartPersonnelDTO();
            partPersonnelDTO.setUserId(user.getId());
            partPersonnelDTO.setUserName(user.getName());
            partPersonnelDTO.setUserFamily(user.getFamily());
            userTypeList.forEach(userType -> {
                if (userType.getId().equals(user.getUserTypeId())) {
                    partPersonnelDTO.setUserTypeId(userType.getId());
                    partPersonnelDTO.setUserTypeName(userType.getName());
                }
            });
//            user.getOrgAndUserTypeList().forEach(orgAndUserType -> {
//                organizationList.forEach(organization -> {
//                    if (organization.getId().equals(orgAndUserType.getOrganizationId())) {
//                        partPersonnelDTO.setOrgId(organization.getId());
//                        partPersonnelDTO.setOrgName(organization.getName());
//                    }
//                });
//                orgAndUserType.getUserTypeList().forEach(uTyId -> {
//                    userTypeList.forEach(userType -> {
//                        if (uTyId.equals(userType.getId())) {
//                            partPersonnelDTO.setUserTypeId(userType.getId());
//                            partPersonnelDTO.setUserTypeName(userType.getName());
//                        }
//                    });
//                });
//            });
            personnelDTOList.add(partPersonnelDTO);
        });
        return personnelDTOList;
    }
}
