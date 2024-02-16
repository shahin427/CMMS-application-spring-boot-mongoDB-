package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.Model.newModel.Part.Part;

import java.util.ArrayList;
import java.util.List;

@Data
public class PartGroupPersonnelDTO {
    private String userTypeId;
    private String userTypeName;
    private String orgId;
    private String orgName;

    public static List<PartGroupPersonnelDTO> map(Part part, List<Organization> organizationList, List<UserType> userTypeList) {
        List<PartGroupPersonnelDTO> partGroupPersonnelDTOList = new ArrayList<>();
        part.getAssignedToGroupList().forEach(assignedToGroup -> {
            PartGroupPersonnelDTO partGroupPersonnelDTO = new PartGroupPersonnelDTO();
            userTypeList.forEach(userType -> {
                if (assignedToGroup.getUserTypeId().equals(userType.getId())) {
                    partGroupPersonnelDTO.setUserTypeId(userType.getId());
                    partGroupPersonnelDTO.setUserTypeName(userType.getName());
                }
            });

            organizationList.forEach(organization -> {
                if (assignedToGroup.getOrgId().equals(organization.getId())) {
                    partGroupPersonnelDTO.setOrgId(organization.getId());
                    partGroupPersonnelDTO.setOrgName(organization.getName());
                }
            });
            partGroupPersonnelDTOList.add(partGroupPersonnelDTO);
        });
        return partGroupPersonnelDTOList;
    }
}
