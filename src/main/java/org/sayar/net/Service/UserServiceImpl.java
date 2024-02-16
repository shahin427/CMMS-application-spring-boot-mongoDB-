package org.sayar.net.Service;

import org.sayar.net.Dao.UserDao;
import org.sayar.net.Email.EmailConfiguration;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Filter.UserFilter;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.Notification;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.Service.newService.MessageService;
import org.sayar.net.Service.newService.OrganService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl extends GeneralServiceImpl<User> implements UserService {

    @Autowired
    private UserDao dao;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private OrganService organService;

    @Autowired
    private EmailConfiguration emailConfiguration;

    @Override
    public boolean isUserSignUp(String userName) {
        return dao.isUserSignUp(userName);
    }

    @Override
    public boolean haveUserNationalCode(User user) {
        return dao.haveUserNationalCode(user);
    }

    @Override
    public User save(User user, String organId) {
        return null;
    }

    @Override
    public User findOne(String id) {
        return null;
    }


    @Override
    public User getUserForToken(String username) {
        return dao.getUserForToken(username);
    }

    @Override
    public List<User> filter(UserFilter filter) {
        return dao.filter(filter);
    }

    @Override
    public List<UserDTO> getByIdList(List<String> idList) {
        return dao.getByIdList(idList);
    }
    @Override
    public List<UserDTO> getByIdList(Set<String> idList) {
        return dao.getByIdList(idList);
    }

    @Override
    public Page<UserFilterDTO> getAllByFilter(UserFilter userFilter, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllByFilter(userFilter, pageable, totalElement)
                , pageable
                , dao.getAllByFilterCount(userFilter)
        );
    }

    @Override
    public List<UserWithOrgAndUserTypeNameDTO> getAllIdAndTitle() {
        List<User> userList = dao.getAllIdAndTitle();
        List<String> uTIdList = new ArrayList<>();
        List<String> orgIdList = new ArrayList<>();
        userList.forEach(user -> {
            user.getOrgAndUserTypeList().forEach(orgAndUserType -> {
                orgIdList.add(orgAndUserType.getOrganizationId());
                uTIdList.addAll(orgAndUserType.getUserTypeList());
            });
        });
        List<Organization> organizationList = organService.getOrganizationNameAndId(orgIdList);
        List<UserType> userTypeList = userTypeService.getUserTypeTitleList(uTIdList);
        return UserWithOrgAndUserTypeNameDTO.map(userList, organizationList, userTypeList);
    }

    @Override
    public String createMainInformationOfUser(UserMainInformationStringDTO userMainInformationStringDTO) {
        User user = dao.createMainInformationOfUser(userMainInformationStringDTO);
        messageService.createNewMessageForAUser(user.getId());
        return user.getId();
    }

    @Override
    public boolean updateSecondaryInformationOfUser(UserSecondaryInformationDTO userSecondaryInformationDTO) {
        User user = dao.getUserMailBeforeChange(userSecondaryInformationDTO.getUserId());
        boolean updateResult = super.updateResultStatus(dao.updateSecondaryInformationOfUser(userSecondaryInformationDTO));
        //send welcome email to registered user
        if (updateResult
                && user.getUserContact() != null
                && user.getUserContact().getEmail() != null
                && userSecondaryInformationDTO.getUserContact() != null
                && userSecondaryInformationDTO.getUserContact().getEmail() != null
                && !userSecondaryInformationDTO.getUserContact().getEmail().equals("")
                && !user.getUserContact().getEmail().equals(userSecondaryInformationDTO.getUserContact().getEmail())) {
            emailConfiguration.sendSimpleEmail(userSecondaryInformationDTO.getUserContact().getEmail());
        }
        return updateResult;
    }

    @Override
    public UserIdDTO updateChildUsersForReport(UserChildUsersDTO userChildUsersDTO) {
        UserChildUsersDTO userChildUsersDTO1 = dao.updateChildUsersForReport(userChildUsersDTO);
        return UserIdDTO.map(userChildUsersDTO1);

    }

    @Override
    public UserDTOWithUserTypeDTO getAllChildUsersIdOfUserByUserId(String userId) {
        User user = dao.getAllChildUsersIdOfUserByUserId(userId);
        if (user != null && user.getParentUserId() != null) {
            return dao.getParentUserByParentId(user.getParentUserId());
//            if (parentUser != null) {
//                List<String> parentUserTypeList = new ArrayList<>();
//                parentUser.getOrgAndUserTypeList().stream().filter(orgAndUserType -> parentUserTypeList.addAll(orgAndUserType.getUserTypeList())).findFirst().orElse(null);
//
//                List<UserType> userTypesOfParentUser = userTypeService.getUserTypeNameOfUser(parentUserTypeList);
//                return UserDTOWithUserTypeDTO.map(parentUser, userTypesOfParentUser);
//            } else {
//                return null;
//            }
        } else {
            return null;
        }
    }

    @Override
    public List<UserDTOWithUserTypeDTO> getUserChildrenByUserId(String userId) {
        User user = dao.getUser(userId);
        List<User> childUsers = dao.getChildUsers(user.getId());
        List<String> userTypeIdList = new ArrayList<>();
        childUsers.forEach(childUser -> {
//            userTypeIdList.add(childUser.getUserTypeId());
            //TODO red part
        });
        List<UserType> childrenUserTypeList = userTypeService.getChildrenUserTypeList(userTypeIdList);
        return null;
    }

    @Override
    public UserMainInformationStringDTO getMainInformationOfUser(String userId) {
        return dao.getMainInformationOfUser(userId);
//        List<String> orgIdList = new ArrayList<>();
//        List<String> userTypeIdList = new ArrayList<>();
//
//        userMainInformationStringDTO.getOrgAndUserTypeList().forEach(orgAndUserTypeDTO -> {
//            orgIdList.add(orgAndUserTypeDTO.getOrganizationId());
//            userTypeIdList.addAll(orgAndUserTypeDTO.getUserTypeList());
//        });
//        List<Organization> organizationList = organService.getOrganizationNameAndId(orgIdList);
//        List<UserType> userTypeList = userTypeService.getUserTypeTitleList(userTypeIdList);
//        return createUserMainInformationDTO(userMainInformationStringDTO, organizationList, userTypeList);
    }

    private UserMainInformationDTO createUserMainInformationDTO(UserMainInformationStringDTO userMainInformationStringDTO,
                                                                List<Organization> organizationList, List<UserType> userTypeList) {

        UserMainInformationDTO userMainInformationDTO = new UserMainInformationDTO();
        userMainInformationDTO.setId(userMainInformationStringDTO.getId());
        userMainInformationDTO.setFamily(userMainInformationStringDTO.getFamily());
        userMainInformationDTO.setImage(userMainInformationStringDTO.getImage());
        userMainInformationDTO.setName(userMainInformationStringDTO.getName());
        userMainInformationDTO.setUsername(userMainInformationStringDTO.getUsername());
        userMainInformationDTO.setPassword(userMainInformationStringDTO.getPassword());
        userMainInformationDTO.setNationalCode(userMainInformationStringDTO.getNationalCode());

        List<OrgAndUserTypeName> orgAndUserTypeNameList = new ArrayList<>();
        userMainInformationStringDTO.getOrgAndUserTypeList().forEach(orgAndUserType -> {
            OrgAndUserTypeName orgAndUserTypeName = new OrgAndUserTypeName();
            orgAndUserTypeName.setOrganizationId(orgAndUserType.getOrganizationId());
            orgAndUserTypeName.setOrganizationName(organizationList.stream().filter(f -> f.getId().equals(orgAndUserType.getOrganizationId())).findFirst().get().getName());
            List<UserTypeNameAndIdDTO> userTypeNameAndIdDTOS = new ArrayList<>();
            orgAndUserType.getUserTypeList().forEach(userTypeString -> {
                UserTypeNameAndIdDTO userTypeNameAndIdDTO = new UserTypeNameAndIdDTO();
                userTypeNameAndIdDTO.setUserTypeId(userTypeString);
                userTypeNameAndIdDTO.setUserTypeName(userTypeList.stream().filter(f -> f.getId().equals(userTypeString)).findFirst().get().getName());
                userTypeNameAndIdDTOS.add(userTypeNameAndIdDTO);
                orgAndUserTypeName.setUserTypeList(userTypeNameAndIdDTOS);
            });
            orgAndUserTypeNameList.add(orgAndUserTypeName);
        });
        userMainInformationDTO.setOrgAndUserTypeList(orgAndUserTypeNameList);
        return userMainInformationDTO;
    }

    @Override
    public boolean updateMainInformation(UserMainInformationStringDTO userMainInformationDTO, String userId) {
        return super.updateResultStatus(dao.updateMainInformation(userMainInformationDTO, userId));
    }

    @Override
    public Page<User> getAllUsersByTermAndPagination(String term, Pageable pageable, Integer totalElement) {
        return dao.getAllUsersByTermAndPagination(term, pageable, totalElement);
    }

    @Override
    public UserSecondaryInformationDTO getSecondaryInformationOfUser(String userId) {
        User user = dao.getSecondaryInformationOfUser(userId);
        if (user != null) {
            return UserSecondaryInformationDTO.map(user);
        } else {
            return null;
        }
    }

    @Override
    public List<User> getAllRecipientUserInformation(List<String> stringList) {
        return dao.getAllRecipientUserInformation(stringList);
    }

    @Override
    public List<Notification> getAllCreationDateOfNotification() {
        return dao.getAllCreationDateOfNotification();
    }

    @Override
    public User getSenderById(String senderUserId) {
        return dao.getSenderById(senderUserId);
    }

    @Override
    public List<User> getAllParentUsersByUserIdList(List<String> userIdListForSendingNotification) {
        return dao.getAllParentUsersByUserIdList(userIdListForSendingNotification);
    }

    @Override
    public User getOneByUserId(String userId) {
        return dao.getOneByUserId(userId
        );
    }

    @Override
    public List<User> getAllPersonnelOfAsset(List<String> users) {
        return dao.getAllPersonnelOfAsset(users);
    }

    @Override
    public List<UserDTOWithUserTypeDTO> getAllUsersExceptOne(String userId) {
        return dao.getAllUsersExceptOne(userId);
    }

    @Override
    public List<User> getAllUsersOfMeteringList(List<String> userIdList) {
        return dao.getAllUsersOfMeteringList(userIdList);
    }

    @Override
    public User getUserOfTheMetering(String meteringId) {
        return dao.getUserOfTheMetering(meteringId);
    }

    @Override
    public List<User> getAllSenderUsersOfNotifications(List<String> senderUserListId) {
        return dao.getAllSenderUsersOfNotifications(senderUserListId);
    }

    @Override
    public List<User> getAllUsersByUserTypeIdAndOrganId(String userTypeId, String organizationId) {
        return dao.getAllUsersByUserTypeIdAndOrganId(userTypeId, organizationId);
    }

    @Override
    public boolean checkIfUsernameIsRepetitive(String username) {
        return dao.checkIfUsernameIsRepetitive(username);
    }

    @Override
    public List<Organization> getAllOrganizationsByUserTypeId(String userTypeId) {
        return organService.getAllOrganizationsByUserTypeId(userTypeId);
    }

    @Override
    public List<Organization> getOrganizationsByAUserId(String userId) {
        User user = dao.getOrganizationsByAUserId(userId);
//        List<String> organizationIdList = user.getOrganizationIdList();
//        if (organizationIdList != null) {
//            return organService.getOrganizationListOfTheUser(organizationIdList);
//        } else
        //TODO red part
        return null;
    }

    @Override
    public boolean checkIfNationalCodeExistForUpdate(String code, String userId) {
        return dao.checkIfNationalCodeExistForUpdate(code, userId);
    }

    @Override
    public boolean checkIfUsernameExistForUpdate(String username, String userId) {
        User userUsername = dao.getTheUserUsername(userId);
        return dao.checkIfUsernameExistForUpdate(username, userUsername);
    }

    @Override
    public List<User> getUserOfWorkRequest(List<String> userIdList) {
        return dao.getUserOfWorkRequest(userIdList);
    }

    @Override
    public List<User> getUsersOfActivity(List<String> userIdList) {
        return dao.getUsersOfActivity(userIdList);
    }

    @Override
    public User getRequesterDetails(String userId) {
        return dao.getRequesterDetails(userId);
    }

    @Override
    public User findRelatedTechnicianOfTheFaultyAsset(String userId) {
        return dao.findRelatedTechnicianOfTheFaultyAsset(userId);
    }

    @Override
    public boolean ifUserTypeExistsInUser(String userTypeId) {
        return dao.ifUserTypeExistsInUser(userTypeId);
    }

    @Override
    public boolean ifOrganisationExistsInUser(String id) {
        return dao.ifOrganisationExistsInUser(id);
    }

    @Override
    public User getAncestorOfTheUser(String parentUserId) {
        return dao.getAncestorOfTheUser(parentUserId);
    }

    @Override
    public List<User> getUserListForSendingNotification(List<String> usersListForSendingNotification) {
        return dao.getUserListForSendingNotification(usersListForSendingNotification);
    }

    @Override
    public User getUserParenByParentId(String parentId) {
        return dao.getUserParenByParentId(parentId);
    }

    @Override
    public boolean checkIfUserIsParentOfAnotherUser(String userId) {
        return dao.checkIfUserIsParentOfAnotherUser(userId);
    }

    @Override
    public List<SubUserDTO> getAllSubUsersOfUserByUserId(String userId) {
        return dao.getSubUsers(userId);
//        List<String> uTIdList = new ArrayList<>();
//        List<String> orgIdList = new ArrayList<>();
//        for (User user : userList) {
//            user.getOrgAndUserTypeList().forEach(orgAndUserType -> {
//                orgIdList.add(orgAndUserType.getOrganizationId());
//                uTIdList.addAll(orgAndUserType.getUserTypeList());
//            });
//        }
//        List<Organization> organizationList = organService.getOrganizationNameAndId(orgIdList);
//        List<UserType> userTypeList = userTypeService.getUserTypeTitleList(uTIdList);
//        return UserWithOrgAndUserTypeNameDTO.map(userList, organizationList, userTypeList);
    }

    @Override
    public boolean checkIfUsernameIsUnique(String username) {
        return dao.checkIfUsernameIsUnique(username);
    }

    @Override
    public List<User> getAllUsersWithRelevantUserType() {
        return dao.getAllUsersWithRelevantUserType();
    }

    @Override
    public UserDetailAndUserTypeDTO getRequesterUser(String userId) {
        return dao.getRequesterUser(userId);
    }

    @Override
    public boolean checkIfPasswordAndResetPasswordAreSame(String password, String resetPassword) {
        if (resetPassword.equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkIfNationalCodeExist(String nationalCode) {
        return dao.checkIfNationalCodeExist(nationalCode);
    }

    @Override
    public List<User> getUsersName(List<String> userIdList) {
        return dao.getUsersName(userIdList);
    }

    @Override
    public User getNameAndFamilyNameOfUser(String userId) {
        return dao.getNameAndFamilyNameOfUser(userId);
    }

    @Override
    public UserType getUserTypeTitle(String userTypeId) {
        return dao.getUserTypeTitle(userTypeId);
    }

    @Override
    public List<User> getTaskUserList(List<String> userIdList) {
        return dao.getTaskUserList(userIdList);
    }

    @Override
    public User getWorkRequestUser(String userId) {
        return dao.getWorkRequestUser(userId);
    }

    @Override
    public User createPrimaryUser(Organization organization, UserType userType) {
        return dao.createPrimaryUser(organization, userType);
    }

    @Override
    public boolean checkIfUserTypeOfOrganizationUsedInUser(String organizationId, String userTypeId) {
        return dao.checkIfUserTypeOfOrganizationUsedInUser(organizationId, userTypeId);
    }

    @Override
    public SubUserDTO getSubUsersInUserGetAll(String userId) {
        return dao.getSubUsersInUserGetAll(userId);
    }

    @Override
    public List<OrgAndUserTypeName> getOrgAndUserTypeListByUserId(String userId) {
        List<OrgAndUserType> orgAndUserTypeList = dao.getOrgAndUserTypeListByUserId(userId);
        Print.print(orgAndUserTypeList);
        List<String> orgIdList = new ArrayList<>();
        List<String> userTypeIdList = new ArrayList<>();

        orgAndUserTypeList.forEach(orgAndUserType -> {
            orgIdList.add(orgAndUserType.getOrganizationId());
            userTypeIdList.addAll(orgAndUserType.getUserTypeList());
        });
        Print.print("orgIdList", orgIdList);
        Print.print("userTypeIdList", userTypeIdList);
        List<Organization> organizationList = organService.getOrganizationNameAndId(orgIdList);
        List<UserType> userTypeList = userTypeService.getUserTypeTitleList(userTypeIdList);
        Print.print("organizationList", organizationList);
        Print.print("userTypeList", userTypeList);
        List<OrgAndUserTypeName> orgAndUserTypeNameList = new ArrayList<>();
        orgAndUserTypeList.forEach(orgAndUserType -> {
            OrgAndUserTypeName orgAndUserTypeName = new OrgAndUserTypeName();
            orgAndUserTypeName.setOrganizationId(orgAndUserType.getOrganizationId());
            orgAndUserTypeName.setOrganizationName(organizationList.stream().filter(org -> org.getId().equals(orgAndUserType.getOrganizationId())).findFirst().get().getName());
            List<UserTypeNameAndIdDTO> userTypeNameAndIdDTOList = new ArrayList<>();
            orgAndUserType.getUserTypeList().forEach(uType -> {
                UserTypeNameAndIdDTO userTypeNameAndIdDTO = new UserTypeNameAndIdDTO();
                userTypeNameAndIdDTO.setUserTypeId(uType);
                userTypeNameAndIdDTO.setUserTypeName(userTypeList.stream().filter(uT -> uT.getId().equals(uType)).findFirst().get().getName());
                userTypeNameAndIdDTOList.add(userTypeNameAndIdDTO);
            });
            orgAndUserTypeName.setUserTypeList(userTypeNameAndIdDTOList);
            orgAndUserTypeNameList.add(orgAndUserTypeName);
        });
        return orgAndUserTypeNameList;
    }

    @Override
    public User getUserForResetPassword(String usernameOrEmail) {
        return dao.getUserForResetPassword(usernameOrEmail);
    }

    @Override
    public void addTokenToResetPassword(User user, String token) {
        dao.addTokenToResetPassword(user, token);
    }

    @Override
    public User getUserUserTypeIds(String userId) {
        return dao.getUserUserTypeIds(userId);
    }

    @Override
    public User getActivityLevelUser(String assignedUserId) {
        return dao.getActivityLevelUser(assignedUserId);
    }

    @Override
    public List<User> getUsersOfActivityByUserTypeId(String userTypeId, String organizationId) {
        return dao.getUsersOfActivityByUserTypeId(userTypeId, organizationId);
    }

    @Override
    public List<User> getCandidateUsersOfActivityByUserIdList(List<String> candidateUserIdList) {
        return dao.getCandidateUsersOfActivityByUserIdList(candidateUserIdList);
    }

    @Override
    public Page<UserWithUserTypeNameDTO> getAllUsersOfAUserType(String userTypeId, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllUsersOfAUserType(userTypeId, pageable, totalElement),
                pageable,
                dao.countAllUsersOfAUserType(userTypeId)
        );
    }

    @Override
    public List<User> getAssetTemplatePersonnel(List<String> userIdList) {
        return dao.getAssetTemplatePersonnel(userIdList);
    }

    @Override
    public List<User> getAllUsersOfActivity(String userTypeId) {
        return dao.getAllUsersOfActivity(userTypeId);
    }

    @Override
    public String getUserReportTo(String userId) {
        User user = dao.getUserReportTo(userId);
        if (user.getParentUserId() != null && !user.getParentUserId().equals("")) {
            return user.getParentUserId();
        } else {
            return null;
        }
    }

    @Override
    public List<UserWithUserTypeNameDTO> getPersonnelOfProject(List<String> users) {
        return dao.getPersonnelOfProject(users);
    }

    @Override
    public List<UserWithUserTypeNameDTO> getUserWithUserType() {
        return dao.getUserWithUserType();
    }

    @Override
    public List<User> getUserLIstOfNewWorkOrder(List<String> userTypeIdList) {
        return dao.getUserLIstOfNewWorkOrder(userTypeIdList);
    }

    @Override
    public List<User> getTechnicians(List<String> userIdList) {
        return dao.getTechnicians(userIdList);
    }
}

