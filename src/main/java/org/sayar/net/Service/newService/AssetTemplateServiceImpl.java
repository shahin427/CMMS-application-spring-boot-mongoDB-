package org.sayar.net.Service.newService;

import org.sayar.net.Dao.Asset.AssetTemplateDao;
import org.sayar.net.Dao.category.CategoryDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.AssetTemplate;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.Asset.Property;
import org.sayar.net.Model.DTO.AssetTemplateGroupPersonnelDTO;
import org.sayar.net.Model.DTO.AssetTemplatePersonnelDTO;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("assetTemplateServiceImpl")
public class AssetTemplateServiceImpl extends GeneralServiceImpl<AssetTemplate> implements AssetTemplateService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AssetTemplateDao assetTemplateDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTypeService userTypeService;

    @Override
    public AssetTemplate getOneAssetTemplate(String assetTemplateId) {
        return assetTemplateDao.getOneAssetTemplate(assetTemplateId);
    }

    @Override
    public String postOneAssetTemplate(AssetTemplate assetTemplate) {
        return assetTemplateDao.postOneAssetTemplate(assetTemplate);
    }

    @Override
    public List<AssetTemplate> getAllAssetTemplate() {
        return assetTemplateDao.getAllAssetTemplate();
    }

    @Override
    public boolean updateAssetTemplate(AssetTemplate assetTemplate) {
        return updateResultStatus(assetTemplateDao.updateAssetTemplate(assetTemplate));
    }

    public Page<AssetTemplate> getAllFilter(String term, String parentCategoryId, String subCategoryId, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                assetTemplateDao.getAllFilter(term, parentCategoryId, subCategoryId, pageable, totalElement),
                pageable,
                assetTemplateDao.countAll(term, parentCategoryId, subCategoryId)
        );
    }

    @Override
    public List<AssetTemplate> getAllAssetTemplateName() {
        return assetTemplateDao.getAllAssetTemplateName();
    }

    public Category getCategoryTypeByAssetId(String assetTemplateId) {
        AssetTemplate assetTemplate = assetTemplateDao.getCategoryTypeByAssetId(assetTemplateId);
        return categoryDao.getCategoryTypeByCategoryId(assetTemplate.getParentCategoryId());
    }

    @Override
    public List<AssetTemplate> getAllAssetTemplateByIdList(List<String> assetTemplateId) {
        return assetTemplateDao.getAllAssetTemplateByIdList(assetTemplateId);
    }

    @Override
    public boolean checkIfAssetTemplateNameIsUnique(String name) {
        return assetTemplateDao.checkIfAssetTemplateNameIsUnique(name);
    }

    @Override
    public boolean checkIfUserExistsAssetTemplate(String userId) {
        return assetTemplateDao.checkIfUserExistsAssetTemplate(userId);
    }

    @Override
    public boolean ifPropertyExistsInAssetTemplate(String propertyId) {
        return assetTemplateDao.ifPropertyExistsInAssetTemplate(propertyId);
    }

    @Override
    public boolean ifCategoryExistsInAssetTemplate(String categoryId) {
        return assetTemplateDao.ifCategoryExistsInAssetTemplate(categoryId);
    }

    @Override
    public Page<AssetTemplate> getAllAssetTemplatesWithPagination(String term, String parentCategory, String subCategory, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                assetTemplateDao.getAllAssetTemplatesWithPagination(term, parentCategory, subCategory, pageable, totalElement)
                , pageable
                , assetTemplateDao.countAllAssetTemplate(term, parentCategory, subCategory)
        );
    }

    @Override
    public boolean addPersonTypePersonnelOfAssetTemplate(String assetTemplateId, List<AssignedToPerson> assignedToPersonList) {
        return assetTemplateDao.addPersonTypePersonnelOfAssetTemplate(assetTemplateId, assignedToPersonList);
    }

    @Override
    public List<AssetTemplatePersonnelDTO> getPersonnelOfAssetTemplate(String assetTemplateId) {
        AssetTemplate assetTemplate = assetTemplateDao.getPersonnelOfAssetTemplate(assetTemplateId);
        List<String> userIdList = new ArrayList<>();
        List<String> userTypeIdList = new ArrayList<>();
        if (assetTemplate.getAssignedToPersonList() != null) {
            assetTemplate.getAssignedToPersonList().forEach(assignedToPerson -> {
                        userIdList.add(assignedToPerson.getUserId());
                        userTypeIdList.add(assignedToPerson.getUserTypeId());
                    }
            );
            List<User> userList = userService.getAssetTemplatePersonnel(userIdList);
            List<UserType> userTypeList = userTypeService.getAssetTemplatePersonnelUserType(userTypeIdList);
            return AssetTemplatePersonnelDTO.map(userList, userTypeList);
        } else
            return null;
    }

    @Override
    public boolean checkIfCategoryIsAssetTemplateParent(String categoryId) {
        return assetTemplateDao.checkIfCategoryIsAssetTemplateParent(categoryId);
    }

    @Override
    public boolean addGroupPersonnelToAssetTemplate(String assetTemplateId, List<AssignedToGroup> assignedToGroupList) {
        return assetTemplateDao.addGroupPersonnelToAssetTemplate(assetTemplateId, assignedToGroupList);
    }

    @Override
    public List<AssetTemplateGroupPersonnelDTO> getGroupPersonnelOfAssetTemplate(String assetTemplateId) {
        AssetTemplate assetTemplate = assetTemplateDao.getGroupPersonnelOfAssetTemplate(assetTemplateId);
        if (assetTemplate.getAssignedToGroupList() != null) {
            List<String> userTypeIdList = new ArrayList<>();
            assetTemplate.getAssignedToGroupList().forEach(assignedToGroup -> {
                userTypeIdList.add(assignedToGroup.getUserTypeId());
            });
            return userTypeService.getAssetTemplateGroupPersonnel(userTypeIdList);
        } else
            return null;
    }

    @Override
    public AssetTemplate getPropertyOfAssetTemplate(String assetTemplateId) {
        return assetTemplateDao.getPropertyOfAssetTemplate(assetTemplateId);
    }

    @Override
    public boolean updateAssetTemplateProperties(String assetTemplateId, List<Property> properties) {
        return assetTemplateDao.updateAssetTemplateProperties(assetTemplateId, properties);
    }

    @Override
    public List<AssetTemplate> getAllAssetTemplateOfAsset(CategoryType categoryType) {
        return assetTemplateDao.getAllAssetTemplateOfAsset(categoryType);
    }
}
