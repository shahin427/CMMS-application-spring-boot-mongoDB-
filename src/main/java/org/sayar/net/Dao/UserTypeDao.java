package org.sayar.net.Dao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Model.DTO.AssetTemplateGroupPersonnelDTO;
import org.sayar.net.Model.DTO.OrganizationTermDTO;
import org.sayar.net.Model.DTO.ProjectGroupPersonnelDTO;
import org.sayar.net.Model.UserType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserTypeDao {

    List<UserType> filter(String name, String role, Pageable pageable, Integer totalElement);

    UserType createUserType(UserType userType);

    UserType getOneUserType(String userTypeId);

    UpdateResult updateUserType(UserTypeDTO userType);

    List<UserType> getAllUserTypeByIdList(List<String> userTypeIdList);

    List<UserType> getAllType();

    UserType getOneUserTypeByAssetId(String id);

    List<UserType> getAllUserTypeByOfOrganization(List<String> userTypeIdList);

    UserType getUserTypeOfUser(String userTypeId);

    UserType getMainInformationOfUser(String userTypeId);

    UserType getUserTypeByUSerTypeId(String userTypeId);

    long countUserTypes(String name, String type);

    List<UserType> getUserTypeListOfUsernames(List<String> userTypeIdList);

    List<UserType> getUserTypeOfActivity(List<String> userTypeIdList);

    UserType getUserTypeOfTheUser(String id);

    UserType getRequesterUserType(String id);

    List<UserType> getAllUserTypesByTerm(String term, String organizationId);

    List<String> getUserTypeOfOrganizationByTerm(OrganizationTermDTO organizationTermDTO, String term);

    List<UserType> getAllUserTypeOfChildUsers(List<String> userTypeIdList);

    List<UserType> getUserTypeList(List<String> userTypeIdList);

    List<UserType> getRelevantUserType(List<String> userTypeIdList);

    List<UserType> getNeededUserTypeList(List<String> userTypeList);

    List<UserType> getChildrenUserTypeList(List<String> userTypeIdList);

    List<UserType> getUserTypeNameOfUser(List<String> parentUserTypeList);

    List<UserType> getUserTypeTitleList(List<String> userTypeIdList);

    UserType createFirstUserType();

    UserType getRelatedUserTypeByUserTypeId(String userTypeId);

    List<UserType> getUserTypeName(List<String> userTypeIdList);

    List<UserType> getAssetTemplatePersonnelUserType(List<String> userTypeIdList);

    List<ProjectGroupPersonnelDTO> getProjectPersonnelUserType(List<String> userTypeIdList);

    List<AssetTemplateGroupPersonnelDTO> getAssetTemplateGroupPersonnel(List<String> userTypeIdList);

    List<UserType> getUserTypeListOfNewWorkOrder(List<String> userTypeIdList);
}
