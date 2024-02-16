package org.sayar.net.Model;

import lombok.Data;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.newModel.OrganManagment.Organization;

import java.util.ArrayList;
import java.util.List;

@Data
public class AssetGroupPersonnelDTO {
    private String userTypeId;
    private String userTypeName;
    private String orgId;
    private String orgName;

    public static List<AssetGroupPersonnelDTO> map(Asset asset, List<Organization> organizationList, List<UserType> userTypeList) {
        List<AssetGroupPersonnelDTO> assetGroupPersonnelDTOList = new ArrayList<>();
        asset.getAssignedToGroupList().forEach(assignedToGroup -> {
            AssetGroupPersonnelDTO assetGroupPersonnelDTO = new AssetGroupPersonnelDTO();
            userTypeList.forEach(userType -> {
                if (assignedToGroup.getUserTypeId().equals(userType.getId())) {
                    assetGroupPersonnelDTO.setUserTypeId(userType.getId());
                    assetGroupPersonnelDTO.setUserTypeName(userType.getName());
                }
            });

            organizationList.forEach(organization -> {
                if (assignedToGroup.getOrgId().equals(organization.getId())) {
                    assetGroupPersonnelDTO.setOrgId(organization.getId());
                    assetGroupPersonnelDTO.setOrgName(organization.getName());
                }
            });
            assetGroupPersonnelDTOList.add(assetGroupPersonnelDTO);
        });
        return assetGroupPersonnelDTOList;
    }
}
