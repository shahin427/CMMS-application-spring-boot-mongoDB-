package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.OrganDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.OrganizationDTO;
import org.sayar.net.Model.DTO.OrganizationSearchDTO;
import org.sayar.net.Model.DTO.UserTypeNameAndIdDTO;
import org.sayar.net.Model.Filter.OrganizationFilter;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.Location.City;
import org.sayar.net.Model.newModel.Location.Province;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.Service.ProvinceService;
import org.sayar.net.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("OrganServiceImpl")
public class OrganServiceImpl extends GeneralServiceImpl<Organization> implements OrganService {

    @Autowired
    private OrganDao dao;

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CityService cityService;

    @Autowired
    private UserService userService;

    @Override
    public List<User> getOrganUseres(String id) {
        return dao.getOrganUseres(id);
    }

    @Override
    public boolean isUniqueOrganCode(String organCode) {
        return dao.isUniqueOrganCode(organCode);
    }

    @Override
    public List<Organization> getParentChilderen(String parentOrganId) {
        return dao.getParentChilderen(parentOrganId);
    }

    @Override
    public Organization getOrganByUserName(String userId) {
        return dao.getOrganByUserName(userId);
    }

    @Override
    public List<Organization> getOrganNameAndId() {
        return dao.getOrganNameAndId();
    }

    @Override
    public List<Organization> getOrganNameAndIdByCityId(String cityId) {
        return dao.getOrganNameAndIdByCityId(cityId);
    }

    @Override
    public List<Organization> getAllParentOrgan() {
        return dao.getAllParentOrgan();
    }

    @Override
    public Page<OrganizationDTO> getAllByFilterAndPagination(OrganizationFilter organizationFilter, Pageable pageable, Integer totalElement) {
        List<OrganizationSearchDTO> organizationSearchDTOS = dao.getAllByFilterAndPagination(organizationFilter, pageable, totalElement);

        List<String> provinceIdList = new ArrayList<>();
        List<String> cityIdList = new ArrayList<>();
        List<String> parentOrganIdList = new ArrayList<>();

        organizationSearchDTOS.forEach(organizationSearchDTO -> {
            provinceIdList.add(organizationSearchDTO.getProvinceId());
            cityIdList.add(organizationSearchDTO.getCityId());
            if (organizationSearchDTO.getParentOrganId() != null && !organizationSearchDTO.getParentOrganId().equals("PARENT"))
                parentOrganIdList.add(organizationSearchDTO.getParentOrganId());
        });

        List<String> uniqueParentIdList = parentOrganIdList.stream().distinct().collect(Collectors.toList());
        List<City> cityList = cityService.getCityList(cityIdList);
        List<Province> provinces = provinceService.getProvinceList(provinceIdList);
        List<Organization> parentOrganizationList = dao.getParentOrganizationList(uniqueParentIdList);

        long count = dao.getAllByFilterCount(organizationFilter);

        return new PageImpl<>(
                OrganizationDTO.map(organizationSearchDTOS, cityList, provinces, parentOrganizationList),
                pageable,
                count
        );
    }

    @Override
    public Organization getOneOrganisation(String orgId) {
        return dao.getOneOrganization(orgId);
    }

    @Override
    public Organization getAllUserTypesOfThOrganization(String organizationId) {
        System.out.println("555555555");
        return dao.getAllUserTypesOfThOrganization(organizationId);
    }

    @Override
    public boolean updateOrganization(Organization organization) {
        return super.updateResultStatus(dao.updateOrganization(organization));
    }

    @Override
    public List<Organization> getOrganizationOfUser(List<String> organIdList) {
        return dao.getOrganizationOfUser(organIdList);
    }

    @Override
    public List<Organization> getOrganizationOfUserType(List<String> organIdList) {
        return dao.getOrganizationOfUserType(organIdList);
    }

    @Override
    public List<Organization> getAllOrganizationsByUserTypeId(String userTypeId) {
        return dao.getAllOrganizationsByUserTypeId(userTypeId);
    }

    @Override
    public List<Organization> getOrganizationListOfTheUser(List<String> organizationIdList) {
        return dao.getOrganizationListOfTheUser(organizationIdList);
    }

    @Override
    public boolean ifUserTypeExistsInOrgan(String userTypeId) {
        return dao.ifUserTypeExistsInOrgan(userTypeId);
    }

    @Override
    public boolean ifProvinceExistsInOrganization(String provinceId) {
        return dao.ifProvinceExistsInOrganization(provinceId);
    }

    @Override
    public boolean ifCityExistsInOrganization(String cityId) {
        return dao.ifCityExistsInOrganization(cityId);
    }

    @Override
    public Organization getTheOrganizationUserType(String organizationId) {
        return dao.getTheOrganizationUserType(organizationId);
    }

    @Override
    public Organization getAllUserTypesByTerm(String term, String organizationId) {
        return dao.getAllUserTypesByTerm(term, organizationId);
    }

    @Override
    public Organization createFirstOrganization(UserType userType) {
        return dao.createFirstOrganization(userType);
    }

    @Override
    public boolean checkIfOrganHasChild(String id) {
        return dao.checkIfOrganHasChild(id);
    }

    @Override
    public boolean checkIfUserTypeOfOrganizationUsedInUser(String organizationId, String userTypeId) {
        return userService.checkIfUserTypeOfOrganizationUsedInUser(organizationId, userTypeId);
    }

    @Override
    public boolean isOrganCodeUniqueInUpdate(String organCode, String id) {
        return dao.isOrganCodeUniqueInUpdate(organCode, id);
    }

    @Override
    public List<Organization> getOrganizationName() {
        return dao.getOrganizationName();
    }

    @Override
    public List<UserTypeNameAndIdDTO> getUserTypeListByOrganizationId(String orgId) {
        return dao.getUserTypeListByOrganizationId(orgId);
    }

    @Override
    public List<Organization> getOrganizationNameAndId(List<String> orgIdList) {
        return dao.getOrganizationNameAndId(orgIdList);
    }

    @Override
    public Organization getRelatedOrganByOrganId(String organizationId) {
        return dao.getRelatedOrganByOrganId(organizationId);
    }

}
