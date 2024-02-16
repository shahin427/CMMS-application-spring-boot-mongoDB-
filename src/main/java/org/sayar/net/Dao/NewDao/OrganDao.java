package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.OrganizationSearchDTO;
import org.sayar.net.Model.DTO.UserTypeNameAndIdDTO;
import org.sayar.net.Model.Filter.OrganizationFilter;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrganDao extends GeneralDao<Organization> {


    List<User> getOrganUseres(String id);

    boolean isUniqueOrganCode(String organCode);

    List<Organization> getParentChilderen(String parentOrganId);

    Organization getOrganByUserName(String organId);

    List<Organization> getOrganNameAndId();

    List<Organization> getOrganNameAndIdByCityId(String organId);

    List<Organization> getAllParentOrgan();

    List<OrganizationSearchDTO> getAllByFilterAndPagination(OrganizationFilter organizationFilter, Pageable pageable, Integer totalElement);

    Organization getOneOrganization(String orgId);

    long getAllByFilterCount(OrganizationFilter organizationFilter);

    boolean ifCityExistInOrganization(String cityId);

    Organization getAllUserTypesOfThOrganization(String organizationId);

    UpdateResult updateOrganization(Organization organization);

    List<Organization> getOrganizationOfUser(List<String> organIdList);

    List<Organization> getOrganizationOfUserType(List<String> organIdList);

    List<Organization> getAllOrganizationsByUserTypeId(String userTypeId);

    List<Organization> getOrganizationListOfTheUser(List<String> organizationIdList);

    boolean ifUserTypeExistsInOrgan(String userTypeId);

    boolean ifProvinceExistsInOrganization(String provinceId);

    boolean ifCityExistsInOrganization(String cityId);

    List<Organization> getParentOrganizationList(List<String> parentOrganIdList);

    Organization getTheOrganizationUserType(String organizationId);

    Organization getAllUserTypesByTerm(String term, String organizationId);

    Organization createFirstOrganization(UserType userType);

    boolean checkIfOrganHasChild(String id);

    boolean isOrganCodeUniqueInUpdate(String organCode, String id);

    List<Organization> getOrganizationName();

    List<UserTypeNameAndIdDTO> getUserTypeListByOrganizationId(String orgId);

    List<Organization> getOrganizationNameAndId(List<String> orgIdList);

    Organization getRelatedOrganByOrganId(String organizationId);
}
