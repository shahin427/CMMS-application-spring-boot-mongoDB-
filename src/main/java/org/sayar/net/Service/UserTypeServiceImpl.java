package org.sayar.net.Service;


import org.sayar.net.Dao.UserTypeDTO;
import org.sayar.net.Dao.UserTypeDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.AssetTemplateGroupPersonnelDTO;
import org.sayar.net.Model.DTO.OrganizationTermDTO;
import org.sayar.net.Model.DTO.ProjectGroupPersonnelDTO;
import org.sayar.net.Model.DTO.UserTypeNameAndIdDTO;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.Service.newService.OrganService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("userTypeServiceImpl")
public class UserTypeServiceImpl extends GeneralServiceImpl<UserType> implements UserTypeService {
    @Autowired
    private UserTypeDao userTypeDao;

    @Autowired
    private OrganService organService;

    @Override
    public Page<UserType> filter(String name, String type, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                userTypeDao.filter(name, type, pageable, totalElement),
                pageable,
                userTypeDao.countUserTypes(name, type)
        );
    }

    @Override
    public UserType createUserType(UserType userType) {
        return userTypeDao.createUserType(userType);
    }

    @Override
    public UserType getOneUserType(String userTypeId) {
        return userTypeDao.getOneUserType(userTypeId);
    }

    @Override
    public boolean updateUserType(UserTypeDTO userType) {
        return this.updateResultStatus(userTypeDao.updateUserType(userType));
    }

    @Override
    public List<UserType> getAllUserTypeByIdList(List<String> userTypeIdList) {
        return userTypeDao.getAllUserTypeByIdList(userTypeIdList);

    }

    @Override
    public List<UserType> getAllType() {
        return userTypeDao.getAllType();
    }

    @Override
    public List<UserType> getAllUserTypesOfThOrganization(String organizationId) {
        Print.print(organizationId, "33333");
        Organization organization = organService.getAllUserTypesOfThOrganization(organizationId);
        Print.print("organization", organization);
        if (organization != null && organization.getUserTypeList() != null) {
            List<String> userTypeIdList = new ArrayList<>(organization.getUserTypeList());
            return userTypeDao.getAllUserTypeByOfOrganization(userTypeIdList);
        } else
            return null;
    }

    @Override
    public UserType getUserTypeOfUser(String userTypeId) {
        return userTypeDao.getUserTypeOfUser(userTypeId);
    }

    @Override
    public UserType getMainInformationOfUser(String userTypeId) {
        return userTypeDao.getMainInformationOfUser(userTypeId);
    }

    @Override
    public UserType getUserTypeByUSerTypeId(String userTypeId) {
        return userTypeDao.getUserTypeByUSerTypeId(userTypeId);
    }

    @Override
    public List<UserType> getUserTypeListOfUsernames(List<String> userTypeIdList) {
        return userTypeDao.getUserTypeListOfUsernames(userTypeIdList);
    }

    @Override
    public List<UserType> getUserTypeOfActivity(List<String> userTypeIdList) {
        return userTypeDao.getUserTypeOfActivity(userTypeIdList);
    }

    @Override
    public UserType getUserTypeOfTheUser(String id) {
        return userTypeDao.getUserTypeOfTheUser(id);
    }

    @Override
    public UserType getRequesterUserType(String id) {
        return userTypeDao.getRequesterUserType(id);
    }

    @Override
    public List<UserType> getAllUserTypesByTerm(String term, String organizationId) {
        Organization organization = organService.getTheOrganizationUserType(organizationId);
        List<UserType> userTypeList = userTypeDao.getNeededUserTypeList(organization.getUserTypeList());
        List<UserType> requiredUserTypeList = new ArrayList<>();
        if (term != null && !term.equals("")) {
            Pattern pattern = Pattern.compile(term, Pattern.CASE_INSENSITIVE);
            userTypeList.forEach(userType -> {
                Matcher matcher = pattern.matcher(userType.getName());
                boolean matchFound = matcher.find();
                if (matchFound) {
                    requiredUserTypeList.add(userType);
                }
            });
            return requiredUserTypeList;
        } else {
            return userTypeList;
        }
    }

    @Override
    public List<String> getUserTypeOfOrganizationByTerm(OrganizationTermDTO organizationTermDTO, String term) {
        return userTypeDao.getUserTypeOfOrganizationByTerm(organizationTermDTO, term);
    }

    @Override
    public List<UserType> getAllUserTypeOfChildUsers(List<String> userTypeIdList) {
        return userTypeDao.getAllUserTypeOfChildUsers(userTypeIdList);
    }

    @Override
    public List<UserType> getUserTypeList(List<String> userTypeIdList) {
        return userTypeDao.getUserTypeList(userTypeIdList);
    }

    @Override
    public List<UserType> getChildrenUserTypeList(List<String> userTypeIdList) {
        return userTypeDao.getChildrenUserTypeList(userTypeIdList);
    }

    @Override
    public List<UserType> getUserTypeNameOfUser(List<String> parentUserTypeList) {
        return userTypeDao.getUserTypeNameOfUser(parentUserTypeList);
    }

    @Override
    public List<UserType> getUserTypeTitleList(List<String> userTypeIdList) {
        return userTypeDao.getUserTypeTitleList(userTypeIdList);
    }

    @Override
    public UserType createFirstUserType() {
        return userTypeDao.createFirstUserType();
    }

    @Override
    public List<UserTypeNameAndIdDTO> getUserTypeListByOrganizationId(String orgId) {
        return organService.getUserTypeListByOrganizationId(orgId);
    }

    @Override
    public UserType getRelatedUserTypeByUserTypeId(String userTypeId) {
        return userTypeDao.getRelatedUserTypeByUserTypeId(userTypeId);
    }

    @Override
    public List<UserType> getUserTypeName(List<String> userTypeIdList) {
        return userTypeDao.getUserTypeName(userTypeIdList);
    }

    @Override
    public List<UserType> getAssetTemplatePersonnelUserType(List<String> userTypeIdList) {
        return userTypeDao.getAssetTemplatePersonnelUserType(userTypeIdList);
    }

    @Override
    public List<ProjectGroupPersonnelDTO> getProjectPersonnelUserType(List<String> userTypeIdList) {
        return userTypeDao.getProjectPersonnelUserType(userTypeIdList);
    }

    @Override
    public List<AssetTemplateGroupPersonnelDTO> getAssetTemplateGroupPersonnel(List<String> userTypeIdList) {
        return userTypeDao.getAssetTemplateGroupPersonnel(userTypeIdList);
    }

    @Override
    public List<UserType> getUserTypeListOfNewWorkOrder(List<String> userTypeIdList) {
        return userTypeDao.getUserTypeListOfNewWorkOrder(userTypeIdList);
    }

}
