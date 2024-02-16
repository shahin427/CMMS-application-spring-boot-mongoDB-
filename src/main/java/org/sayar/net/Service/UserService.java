package org.sayar.net.Service;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Filter.UserFilter;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface UserService extends GeneralService<User> {

    boolean isUserSignUp(String userName);

    boolean haveUserNationalCode(User user);

    User save(User user, String organId);

    User findOne(String id);

    User getUserForToken(String username);

    List<User> filter(UserFilter filter);

    List<UserDTO> getByIdList(List<String> idList);
    List<UserDTO> getByIdList(Set<String> idList);

    Page<UserFilterDTO> getAllByFilter(UserFilter userFilter, org.springframework.data.domain.Pageable pageable, Integer totalElement);

    List<UserWithOrgAndUserTypeNameDTO> getAllIdAndTitle();

    String createMainInformationOfUser(UserMainInformationStringDTO userMainInformationStringDTO);

    boolean updateSecondaryInformationOfUser(UserSecondaryInformationDTO userSecondaryInformationDTO);

    UserIdDTO updateChildUsersForReport(UserChildUsersDTO userChildUsersDTO);

    UserDTOWithUserTypeDTO getAllChildUsersIdOfUserByUserId(String userId);

    List<UserDTOWithUserTypeDTO> getUserChildrenByUserId(String userId);

    UserMainInformationStringDTO getMainInformationOfUser(String userId);

    boolean updateMainInformation(UserMainInformationStringDTO userMainInformationDTO, String userId);

    Page<User> getAllUsersByTermAndPagination(String term, Pageable pageable, Integer totalElement);

    UserSecondaryInformationDTO getSecondaryInformationOfUser(String userId);

    List<User> getAllRecipientUserInformation(List<String> stringList);

    List<Notification> getAllCreationDateOfNotification();

    User getSenderById(String senderUserId);

    List<User> getAllParentUsersByUserIdList(List<String> userIdListForSendingNotification);

    User getOneByUserId(String userId);

    List<User> getAllPersonnelOfAsset(List<String> users);

    List<UserDTOWithUserTypeDTO> getAllUsersExceptOne(String userId);

    List<User> getAllUsersOfMeteringList(List<String> userIdList);

    User getUserOfTheMetering(String meteringId);

    List<User> getAllSenderUsersOfNotifications(List<String> senderUserListId);

    List<User> getAllUsersByUserTypeIdAndOrganId(String userTypeId, String organizationId);

    boolean checkIfUsernameIsRepetitive(String username);

    List<Organization> getAllOrganizationsByUserTypeId(String userTypeId);

    List<Organization> getOrganizationsByAUserId(String userId);

    boolean checkIfNationalCodeExistForUpdate(String code, String userId);

    boolean checkIfUsernameExistForUpdate(String username, String userId);

    List<User> getUserOfWorkRequest(List<String> userIdList);

    List<User> getUsersOfActivity(List<String> userIdList);

    User getRequesterDetails(String userId);

    User findRelatedTechnicianOfTheFaultyAsset(String userId);

    boolean ifUserTypeExistsInUser(String userTypeId);

    boolean ifOrganisationExistsInUser(String id);

    User getAncestorOfTheUser(String parentUserId);

    List<User> getUserListForSendingNotification(List<String> usersListForSendingNotification);

    User getUserParenByParentId(String parentId);

    boolean checkIfUserIsParentOfAnotherUser(String userId);

    List<SubUserDTO> getAllSubUsersOfUserByUserId(String userId);

    boolean checkIfUsernameIsUnique(String username);

    List<User> getAllUsersWithRelevantUserType();

    UserDetailAndUserTypeDTO getRequesterUser(String userId);

    boolean checkIfPasswordAndResetPasswordAreSame(String password, String resetPassword);

    boolean checkIfNationalCodeExist(String nationalCode);

    List<User> getUsersName(List<String> userIdList);

    User getNameAndFamilyNameOfUser(String userId);

    UserType getUserTypeTitle(String userTypeId);

    List<User> getTaskUserList(List<String> userIdList);

    User getWorkRequestUser(String userId);

    User createPrimaryUser(Organization organization, UserType userType);

    boolean checkIfUserTypeOfOrganizationUsedInUser(String organizationId, String userTypeId);

    SubUserDTO getSubUsersInUserGetAll(String userId);

    List<OrgAndUserTypeName> getOrgAndUserTypeListByUserId(String userId);

    User getUserForResetPassword(String usernameOrEmail);

    void addTokenToResetPassword(User user,String token);

    User getUserUserTypeIds(String userId);

    User getActivityLevelUser(String assignedUserId);

    List<User> getUsersOfActivityByUserTypeId(String userTypeId,String organizationId);

    List<User> getCandidateUsersOfActivityByUserIdList(List<String> candidateUserIdList);

    Page<UserWithUserTypeNameDTO> getAllUsersOfAUserType(String userTypeId,Pageable pageable,Integer totalElement);

    List<User> getAssetTemplatePersonnel(List<String> userIdList);

    List<User> getAllUsersOfActivity(String userTypeId);

    String getUserReportTo(String userId);

    List<UserWithUserTypeNameDTO> getPersonnelOfProject(List<String> users);

    List<UserWithUserTypeNameDTO> getUserWithUserType();

    List<User> getUserLIstOfNewWorkOrder(List<String> userTypeIdList);

    List<User> getTechnicians(List<String> userIdList);
}

