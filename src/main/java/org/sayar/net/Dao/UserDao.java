package org.sayar.net.Dao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
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

public interface UserDao extends GeneralDao<User> {


    boolean isUserSignUp(String userName);

    boolean haveUserNationalCode(User user);

    boolean update(User user);

    List<User> filter(UserFilter userFilter);

    User getUserForToken(String username);

    List<UserDTO> getByIdList(List<String> idList);
    List<UserDTO> getByIdList(Set<String> idList);

    List<UserFilterDTO> getAllByFilter(UserFilter userFilter, Pageable pageable, Integer totalElement);

    long getAllByFilterCount(UserFilter userFilter);

    List<User> getAllIdAndTitle();

    User createMainInformationOfUser(UserMainInformationStringDTO userMainInformationStringDTO);

    UpdateResult updateSecondaryInformationOfUser(UserSecondaryInformationDTO userSecondaryInformationDTO);

    UserChildUsersDTO updateChildUsersForReport(UserChildUsersDTO userChildUsersDTO);

    User getAllChildUsersIdOfUserByUserId(String userId);

    List<User> getInformationOfChildUsers(List<String> childUserStringList);

    User getUserChildrenByUserId(String userId);

    UserMainInformationStringDTO getMainInformationOfUser(String userId);

    UpdateResult updateMainInformation(UserMainInformationStringDTO userMainInformationDTO, String userId);

    Page<User> getAllUsersByTermAndPagination(String term, Pageable pageable, Integer totalElement);

    List<User> getChildrenUsersByChildrenIdList(List<String> childrenUserIdList);

    User getSecondaryInformationOfUser(String userId);

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

    List<User> getAllOrganizationOfTheUser(List<String> userIdList);

    User getOrganizationsByAUserId(String userId);

    UserDTOWithUserTypeDTO getParentUserByParentId(String parentUserId);

    User getTheUserNationalCode(String userId);

    boolean checkIfNationalCodeExistForUpdate(String code, String userId);

    User getTheUserUsername(String userId);

    boolean checkIfUsernameExistForUpdate(String username, User userUsername);

    List<User> getChildUser(String userId);

    List<User> getUserOfWorkRequest(List<String> userIdList);

    List<User> getUsersOfActivity(List<String> userIdList);

    User getRequesterDetails(String userId);

    User getParentUserOfConsideredUser(String parentUserId);

    User findRelatedTechnicianOfTheFaultyAsset(String userId);

    boolean ifUserTypeExistsInUser(String userTypeId);

    boolean ifOrganisationExistsInUser(String id);

    User getUser(String userId);

    User getParentOfTheUser(String parentUserId);

    User getAncestorOfTheUser(String parentUserId);

    List<User> getUserListForSendingNotification(List<String> usersListForSendingNotification);

    User getUserParenByParentId(String parentId);

    boolean checkIfUserIsParentOfAnotherUser(String userId);

    List<SubUserDTO> getSubUsers(String userId);

    boolean checkIfUsernameIsUnique(String username);

    List<User> getAllUsersWithRelevantUserType();

    UserDetailAndUserTypeDTO getRequesterUser(String userId);

    boolean checkIfNationalCodeExist(String nationalCode);

    List<User> getChildUsers(String id);

    List<User> getUsersName(List<String> userIdList);

    User getNameAndFamilyNameOfUser(String userId);

    UserType getUserTypeTitle(String userTypeId);

    List<User> getTaskUserList(List<String> userIdList);

    User getWorkRequestUser(String userId);

    User createPrimaryUser(Organization organization, UserType userType);

    boolean checkIfUserTypeOfOrganizationUsedInUser(String organizationId, String userTypeId);

    SubUserDTO getSubUsersInUserGetAll(String userId);

    List<PartUserDTO> getUsersOfPart(List<String> usersIdList);

    List<OrgAndUserType> getOrgAndUserTypeListByUserId(String userId);

    User getUserForResetPassword(String usernameOrEmail);

    void addTokenToResetPassword(User user, String token);

    User getUserMailBeforeChange(String userId);

    User getUserUserTypeIds(String userId);

    User getActivityLevelUser(String assignedUserId);

    List<User> getUsersOfActivityByUserTypeId(String userTypeId, String organizationId);

    List<User> getCandidateUsersOfActivityByUserIdList(List<String> candidateUserIdList);

    List<User> getNameAndFamilyNameOfUsers(List<String> userIdList);

    List<UserWithUserTypeNameDTO> getAllUsersOfAUserType(String userTypeId, Pageable pageable, Integer totalElement);

    long countAllUsersOfAUserType(String userTypeId);

    List<User> getAssetTemplatePersonnel(List<String> userIdList);

    List<User> getAllUsersOfActivity(String userTypeId);

    User getUserReportTo(String userId);

    List<UserWithUserTypeNameDTO> getPersonnelOfProject(List<String> users);

    List<UserWithUserTypeNameDTO> getUserWithUserType();

    List<User> getUserLIstOfNewWorkOrder(List<String> userTypeIdList);

    List<User> getTechnicians(List<String> userIdList);
}
