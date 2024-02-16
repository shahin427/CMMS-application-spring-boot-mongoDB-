package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.OrganManagment.Organization;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserWithOrgAndUserTypeNameDTO {
    private String id;
    private String family;
    private String name;
    private List<OrgAndUserTypeName> orgAndUserTypeNameList;

    public static List<UserWithOrgAndUserTypeNameDTO> map(List<User> userList, List<Organization> organizationList, List<UserType> userTypeList) {
        List<UserWithOrgAndUserTypeNameDTO> userWithOrgAndUserTypeNameDTOList = new ArrayList<>();
        userList.forEach(user -> {
            UserWithOrgAndUserTypeNameDTO userWithOrgAndUserTypeNameDTO = new UserWithOrgAndUserTypeNameDTO();
            userWithOrgAndUserTypeNameDTO.setId(user.getId());
            userWithOrgAndUserTypeNameDTO.setFamily(user.getFamily());
            userWithOrgAndUserTypeNameDTO.setName(user.getName());
            List<OrgAndUserTypeName> orgAndUserTypeNameList = new ArrayList<>();
            user.getOrgAndUserTypeList().forEach(orgAndUserType -> {
                OrgAndUserTypeName orgAndUserTypeName = new OrgAndUserTypeName();
                orgAndUserTypeName.setOrganizationId(orgAndUserType.getOrganizationId());
                orgAndUserTypeName.setOrganizationName(organizationList.stream().filter(org -> org.getId().equals(orgAndUserTypeName.getOrganizationId())).findFirst().get().getName());
                List<UserTypeNameAndIdDTO> userTypeNameAndIdDTOS = new ArrayList<>();
                orgAndUserType.getUserTypeList().forEach(userTypeString -> {
                    UserTypeNameAndIdDTO userTypeNameAndIdDTO = new UserTypeNameAndIdDTO();
                    userTypeNameAndIdDTO.setUserTypeId(userTypeString);
                    userTypeNameAndIdDTO.setUserTypeName(userTypeList.stream().filter(userType -> userType.getId().equals(userTypeString)).findFirst().get().getName());
                    userTypeNameAndIdDTOS.add(userTypeNameAndIdDTO);
                });
                orgAndUserTypeName.setUserTypeList(userTypeNameAndIdDTOS);
                orgAndUserTypeNameList.add(orgAndUserTypeName);
            });
            userWithOrgAndUserTypeNameDTO.setOrgAndUserTypeNameList(orgAndUserTypeNameList);
            userWithOrgAndUserTypeNameDTOList.add(userWithOrgAndUserTypeNameDTO);
        });
        return userWithOrgAndUserTypeNameDTOList;
    }
}
