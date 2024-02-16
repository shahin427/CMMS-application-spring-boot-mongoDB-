package org.sayar.net.Service;

import org.sayar.net.Dao.UserTypeDTO;
import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.AssetTemplateGroupPersonnelDTO;
import org.sayar.net.Model.DTO.OrganizationTermDTO;
import org.sayar.net.Model.DTO.ProjectGroupPersonnelDTO;
import org.sayar.net.Model.DTO.UserTypeNameAndIdDTO;
import org.sayar.net.Model.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserTypeService extends GeneralService<UserType> {

    Page<UserType> filter(String name, String type, Pageable pageable, Integer totalElement);

    UserType createUserType(UserType userType);

    UserType getOneUserType(String userTypeId);

    boolean updateUserType(UserTypeDTO userType);

    List<UserType> getAllUserTypeByIdList(List<String> userTypeIdList);

    List<UserType> getAllType();

    List<UserType> getAllUserTypesOfThOrganization(String organizationId);

    UserType getUserTypeOfUser(String userTypeId);

    UserType getMainInformationOfUser(String userTypeId);

    UserType getUserTypeByUSerTypeId(String userTypeId);

    List<UserType> getUserTypeListOfUsernames(List<String> userTypeIdList);

    List<UserType> getUserTypeOfActivity(List<String> userTypeIdList);

    UserType getUserTypeOfTheUser(String id);

    UserType getRequesterUserType(String id);

    List<UserType> getAllUserTypesByTerm(String term, String organizationId);

    List<String> getUserTypeOfOrganizationByTerm(OrganizationTermDTO organizationTermDTO, String term);

    List<UserType> getAllUserTypeOfChildUsers(List<String> userTypeIdList);

    List<UserType> getUserTypeList(List<String> userTypeIdList);

    List<UserType> getChildrenUserTypeList(List<String> userTypeIdList);

    List<UserType> getUserTypeNameOfUser(List<String> parentUserTypeList);

    List<UserType> getUserTypeTitleList(List<String> userTypeIdList);

    UserType createFirstUserType();

    List<UserTypeNameAndIdDTO> getUserTypeListByOrganizationId(String orgId);

    UserType getRelatedUserTypeByUserTypeId(String userTypeId);

    List<UserType> getUserTypeName(List<String> userTypeIdList);

    List<UserType> getAssetTemplatePersonnelUserType(List<String> userTypeIdList);

    List<ProjectGroupPersonnelDTO> getProjectPersonnelUserType(List<String> userTypeIdList);

    List<AssetTemplateGroupPersonnelDTO> getAssetTemplateGroupPersonnel(List<String> userTypeIdList);

    List<UserType> getUserTypeListOfNewWorkOrder(List<String> userTypeIdList);
}
