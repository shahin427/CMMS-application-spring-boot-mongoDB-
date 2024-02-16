package org.sayar.net.Dao.Asset;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.Asset.AssetTemplate;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.Asset.Property;
import org.sayar.net.Model.newModel.CategoryType;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AssetTemplateDao extends GeneralDao<AssetTemplate> {

    AssetTemplate getOneAssetTemplate(String assetTemplateId);

    String postOneAssetTemplate(AssetTemplate assetTemplate);

    List<AssetTemplate> getAllAssetTemplate();

    UpdateResult updateAssetTemplate(AssetTemplate assetTemplate);

    List<AssetTemplate> getAllFilter(String term, String parentCategoryId, String subCategoryId, Pageable pageable, Integer totalElement);

    List<AssetTemplate> getAllAssetTemplateName();

    AssetTemplate getCategoryTypeByAssetId(String assetTemplateId);

    List<AssetTemplate> getAllAssetTemplateByIdList(List<String> assetTemplateId);

    boolean checkIfAssetTemplateNameIsUnique(String name);

    boolean checkIfUserExistsAssetTemplate(String userId);

    boolean ifPropertyExistsInAssetTemplate(String propertyId);

    boolean ifCategoryExistsInAssetTemplate(String categoryId);

    List<AssetTemplate> getAllAssetTemplatesWithPagination(String term, String parentCategory, String subCategory, Pageable pageable, Integer totalElement);

    long countAllAssetTemplate(String term, String parentCategory, String subCategory);

    long countAll(String termString, String parentCategoryId, String subCategoryId);

    boolean addPersonTypePersonnelOfAssetTemplate(String assetTemplateId, List<AssignedToPerson> assignedToPersonList);

    AssetTemplate getPersonnelOfAssetTemplate(String assetTemplateId);

    boolean checkIfCategoryIsAssetTemplateParent(String categoryId);

    boolean addGroupPersonnelToAssetTemplate(String assetTemplateId, List<AssignedToGroup> assignedToGroupList);

    AssetTemplate getGroupPersonnelOfAssetTemplate(String assetTemplateId);

    AssetTemplate getPropertyOfAssetTemplate(String assetTemplateId);

    boolean updateAssetTemplateProperties(String assetTemplateId, List<Property> properties);

    List<AssetTemplate> getAllAssetTemplateOfAsset(CategoryType categoryType);
}
