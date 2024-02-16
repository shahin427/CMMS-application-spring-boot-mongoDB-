package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.OrganizationDTO;
import org.sayar.net.Model.DTO.UserTypeNameAndIdDTO;
import org.sayar.net.Model.Filter.OrganizationFilter;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrganService extends GeneralService<Organization> {

    List<User> getOrganUseres(String id);

    boolean isUniqueOrganCode(String organCode);

    List<Organization> getParentChilderen(String parentOrganId);

    Organization getOrganByUserName(String userId);

    List<Organization> getOrganNameAndId();

    List<Organization> getOrganNameAndIdByCityId(String organId);

    List<Organization> getAllParentOrgan();

    Page<OrganizationDTO> getAllByFilterAndPagination(OrganizationFilter organizationFilter, Pageable pageable,Integer totalElement);

    Organization getOneOrganisation(String orgId);

    Organization getAllUserTypesOfThOrganization(String organizationId);

    boolean updateOrganization(Organization organization);

    List<Organization> getOrganizationOfUser(List<String> organIdList);

    List<Organization> getOrganizationOfUserType(List<String> organIdList);

    List<Organization> getAllOrganizationsByUserTypeId(String userTypeId);

    List<Organization> getOrganizationListOfTheUser(List<String> organizationIdList);

    boolean ifUserTypeExistsInOrgan(String userTypeId);

    boolean ifProvinceExistsInOrganization(String provinceId);

    boolean ifCityExistsInOrganization(String cityId);

    Organization getTheOrganizationUserType(String organizationId);

    Organization getAllUserTypesByTerm(String term, String organizationId);

    Organization createFirstOrganization(UserType userType);

    boolean checkIfOrganHasChild(String id);

    boolean checkIfUserTypeOfOrganizationUsedInUser(String organizationId, String userTypeId);

    boolean isOrganCodeUniqueInUpdate(String organCode, String id);

    List<Organization> getOrganizationName();

    List<UserTypeNameAndIdDTO> getUserTypeListByOrganizationId(String orgId);

    List<Organization> getOrganizationNameAndId(List<String> orgIdList);

    Organization getRelatedOrganByOrganId(String organizationId);
}
