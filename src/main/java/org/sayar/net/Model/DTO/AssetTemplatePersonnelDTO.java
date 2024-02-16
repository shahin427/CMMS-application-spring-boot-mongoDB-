package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssetTemplatePersonnelDTO {
    private String userId;
    private String userName;
    private String userFamily;
    private String userTypeId;
    private String userTypeName;

    public static List<AssetTemplatePersonnelDTO> map(List<User> userList, List<UserType> userTypeList) {
        List<AssetTemplatePersonnelDTO> assetTemplatePersonnelDTOList = new ArrayList<>();
        userList.forEach(user -> {
            AssetTemplatePersonnelDTO assetTemplatePersonnelDTO = new AssetTemplatePersonnelDTO();
            assetTemplatePersonnelDTO.setUserId(user.getId());
            assetTemplatePersonnelDTO.setUserName(user.getName());
            assetTemplatePersonnelDTO.setUserFamily(user.getFamily());
            userTypeList.forEach(userType -> {
                if (userType.getId().equals(user.getUserTypeId())) {
                    assetTemplatePersonnelDTO.setUserTypeId(userType.getId());
                    assetTemplatePersonnelDTO.setUserTypeName(userType.getName());
                }
            });
            assetTemplatePersonnelDTOList.add(assetTemplatePersonnelDTO);
        });
        return assetTemplatePersonnelDTOList;
    }
}
