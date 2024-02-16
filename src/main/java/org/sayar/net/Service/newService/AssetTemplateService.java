package org.sayar.net.Service.newService;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.Asset.AssetTemplate;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.Asset.Property;
import org.sayar.net.Model.DTO.AssetTemplateGroupPersonnelDTO;
import org.sayar.net.Model.DTO.AssetTemplatePersonnelDTO;
import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetTemplateService extends GeneralService<AssetTemplate> {

    AssetTemplate getOneAssetTemplate(String assetTemplateId);

    String postOneAssetTemplate(AssetTemplate assetTemplate);

    List<AssetTemplate> getAllAssetTemplate();

    boolean updateAssetTemplate(AssetTemplate assetTemplate);

    Page<AssetTemplate> getAllFilter(String term, String parentCategoryId, String subCategoryId, Pageable pageable, Integer totalElement);

    List<AssetTemplate> getAllAssetTemplateName();

    Category getCategoryTypeByAssetId(String assetTemplateId);

    List<AssetTemplate> getAllAssetTemplateByIdList(List<String> assetTemplateId);

    boolean checkIfAssetTemplateNameIsUnique(String name);

    boolean checkIfUserExistsAssetTemplate(String userId);

    boolean ifPropertyExistsInAssetTemplate(String propertyId);

    boolean ifCategoryExistsInAssetTemplate(String categoryId);

    Page<AssetTemplate> getAllAssetTemplatesWithPagination(String term, String parentCategory, String subCategory, Pageable pageable, Integer totalElement);

    boolean addPersonTypePersonnelOfAssetTemplate(String assetTemplateId, List<AssignedToPerson> assignedToPersonList);

    List<AssetTemplatePersonnelDTO> getPersonnelOfAssetTemplate(String assetTemplateId);

    boolean checkIfCategoryIsAssetTemplateParent(String categoryId);

    boolean addGroupPersonnelToAssetTemplate(String assetTemplateId, List<AssignedToGroup> assignedToGroupList);

    List<AssetTemplateGroupPersonnelDTO> getGroupPersonnelOfAssetTemplate(String assetTemplateId);

    AssetTemplate getPropertyOfAssetTemplate(String assetTemplateId);

    boolean updateAssetTemplateProperties(String assetTemplateId, List<Property> properties);

    List<AssetTemplate> getAllAssetTemplateOfAsset(CategoryType categoryType);
}
