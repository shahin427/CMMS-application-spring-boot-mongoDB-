package org.sayar.net.Model.DTO;


import lombok.Data;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.Location.Location;
import org.sayar.net.Model.newModel.Location.Province;
import org.sayar.net.Model.newModel.OrganManagment.Organization;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrganizationDTO {
    private String id;
    private String name;
    private String organCode;
    private Location organLocation;
    private String cityName;
    private String provinceName;
    private List<String> userTypeIdList;
    private String parentOrganName;

    public static List<OrganizationDTO> map(List<OrganizationSearchDTO> organizationSearchDTOS, List<City> cityList, List<Province> provinces, List<Organization> parentOrganizationList) {
        List<OrganizationDTO> organizationDTOList = new ArrayList<>();
        organizationSearchDTOS.forEach(organizationSearchDTO -> {
            OrganizationDTO organizationDTO = new OrganizationDTO();
            organizationDTO.setId(organizationSearchDTO.getId());
            organizationDTO.setName(organizationSearchDTO.getName());
            organizationDTO.setOrganCode(organizationSearchDTO.getOrganCode());
            if (organizationSearchDTO.getOrganLocation() != null)
                organizationDTO.setOrganLocation(organizationSearchDTO.getOrganLocation());
            if (organizationSearchDTO.getUserTypeIdList() != null)
                organizationDTO.setUserTypeIdList(organizationSearchDTO.getUserTypeIdList());
            cityList.forEach(city -> {
                if (city.getId().equals(organizationSearchDTO.getCityId())) {
                    organizationDTO.setCityName(city.getName());
                }
            });
            provinces.forEach(province -> {
                if (province.getId().equals(organizationSearchDTO.getProvinceId())) {
                    organizationDTO.setProvinceName(province.getName());
                }
            });
            parentOrganizationList.forEach(parentOrganization -> {
                if (parentOrganization.getId().equals(organizationSearchDTO.getParentOrganId())) {
                    System.out.println("innnnnn");
                    organizationDTO.setParentOrganName(parentOrganization.getName());
                }
            });
            organizationDTOList.add(organizationDTO);
        });
        return organizationDTOList;
    }
}
