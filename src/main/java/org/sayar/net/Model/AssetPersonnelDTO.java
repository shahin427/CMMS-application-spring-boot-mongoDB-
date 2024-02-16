package org.sayar.net.Model;

import lombok.Data;
import org.sayar.net.Tools.Print;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssetPersonnelDTO {
    private String userId;
    private String userName;
    private String userFamily;
    private String userTypeId;
    private String userTypeName;
//    private String orgId;
//    private String orgName;

    public static List<AssetPersonnelDTO> map(List<User> userList, List<UserType> userTypeList) {
        List<AssetPersonnelDTO> assetPersonnelDTOList = new ArrayList<>();
        userList.forEach(user -> {
            AssetPersonnelDTO assetPersonnelDTO = new AssetPersonnelDTO();
            assetPersonnelDTO.setUserId(user.getId());
            assetPersonnelDTO.setUserName(user.getName());
            assetPersonnelDTO.setUserFamily(user.getFamily());
            userTypeList.forEach(userType -> {
                if (userType.getId().equals(user.getUserTypeId())) {
                    assetPersonnelDTO.setUserTypeId(userType.getId());
                    assetPersonnelDTO.setUserTypeName(userType.getName());
                }
            });
            Print.print("assetPersonnelDTO",assetPersonnelDTO);
//            user.getOrgAndUserTypeList().forEach(orgAndUserType -> {
//                organizationList.forEach(organization -> {
//                    if (organization.getId().equals(orgAndUserType.getOrganizationId())) {
//                        assetPersonnelDTO.setOrgId(organization.getId());
//                        assetPersonnelDTO.setOrgName(organization.getName());
//                    }
//                });
//                orgAndUserType.getUserTypeList().forEach(uTyId -> {
//                    userTypeList.forEach(userType -> {
//                        if (uTyId.equals(userType.getId())) {
//                            assetPersonnelDTO.setUserTypeId(userType.getId());
//                            assetPersonnelDTO.setUserTypeName(userType.getName());
//                        }
//                    });
//                });
//            });
            assetPersonnelDTOList.add(assetPersonnelDTO);
        });
        return assetPersonnelDTOList;
    }
}
