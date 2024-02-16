package org.sayar.net.Service.newService;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Dao.NewDao.asset.AssetDaoImpl;
import org.sayar.net.Dao.NewDao.asset.InDependentAssetDTO;
import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.Asset.*;
import org.sayar.net.Model.AssetGroupPersonnelDTO;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.Helper.TreeNode;
import org.sayar.net.Model.newModel.Node.Node;
import org.sayar.net.Model.newModel.UnitOfMeasurement;
import org.sayar.net.Model.newModel.metering.model.Metering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.Document;
import java.util.List;

public interface AssetService extends GeneralService<Asset> {

    Page<AssetFilterDTO> getAllByFilterAndPagination(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement);

    Asset postAsset(Asset asset);

    boolean updateAsset(Asset asset);

    boolean checkIfCodeExists(String assetCode);

    boolean updateAssetBasicInformation(String assetId, AssetBasicInformation assetBasicInformation);

    boolean deleteCreateAsset(String assetId);

    boolean updateCreateAsset(CreateAsset createAsset);


    //    _______________________________________
    Asset add(Asset category, String organId);

    List<TreeNode> findAllInTreeNode();

    List<Asset> getAssetByCategory(String catId);

    List<AssetDTO> getCompressAllAsset();

    List<Node> getTreeNodeBase();

    List<Node> getTreeNodeLayer(String id);

    List<Node> getTreeNodeBaseByCategory(String catId);

    List<Node> getTreeNodeLayerByCategory(String id, String catId);

    List<Node> getTreeNodeSearch(String searchTerm);

    List<Node> getTreeNodeSearchByCategory(String searchTerm, String catId);

    List<Node> getAssetChildrenInTreeNode(String assetId);

    List<Asset> getAssetForLocationCategory(String organId);

    long getAssetForLocationCategoryCount(String organId);

    long getAssetForEquipmentCategoryCount(String organId);

    long getAssetForToolsCategoryCount(String organId);

    double calculateMTTR(String assetId);

    double calculateMTBF(String assetId);


    List<AssetDTO> getByIdList(List<String> idList);

    Asset update(Asset asset);

    boolean addMeter(String assetId, Metering metering);

    AssetDaoImpl.GetOneAgg getOne(String id);

    List<purchaseAssetDTO> getAssetDTOForPurchase();


    AssetBasicInformation getOneByAssetId(String assetId);

//    List<AssetDTOPart> getAllPartsById(String assetId);

    AssetDTOCompany getAllCompanyByAssetId(String assetId);

    List<UserDTOWithUserTypeDTO> getAllUsersByAssetId(String assetId);

    AssetDTOTemplate getAllPropertyByAssetId(String assetId);

    List<AssetDTOWarranty> getAllWarrantyByAssetId(String assextId);

    AssetDTOMetering getAllMeteringByAssetId(String assetId);

    Asset getAllPartByAssetId(String assetId);

    boolean updatePartListIdByAssetId(String assetId, List<String> partsId);

    boolean updateCompanyListByAssetId(String assetId, List<String> companyId);

    boolean updatePropertyListByAssetId(String assetId, List<Property> propertyList);

    boolean updateUserListByAssetId(String assetId, List<String> usersId);

    UpdateResult updateWarrantyListByAssetId(String assetId, List<String> warrantyId);

    UpdateResult updateMeteringListByAssetId(String assetId, List<String> meteringId);

    Asset getOneAsset(String assetId);
    List<Activity> getListActivityListByAssetId(String assetId);

    Asset getOneAssetName(String assetId);

    Asset getPropertyListByAssetId(String assetId);

    Asset getDocumentListByAssetId(String assetId);

    boolean updateDocumentListByAssetId(String assetId, List<Document> documents);

    List<Asset> getAllAssetByCategoryType(CategoryType categoryType);

    List<Asset> getAllNameAndCode();

    List<Asset> getAssetByAssetIdList(List<String> assetIdList);

    long countAllOfflineAssets();

    long countAllOnlineAssets();

    Page<Asset> getAllAssetForBOM(Pageable pageable, Integer totalElement);

    Page<AssetDTO> getAllAssetOfAssignedToUserByUserId(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement);

    Page<InDependentAssetDTO> getAllIndependentAssets(Pageable pageable, Integer totalElement);

    List<AssetDTOForChildren> getAllChildrenOfAUser(String parentId);

    List<Asset> getAssetsByCategoryType(String categoryType);


    void getParentAssetAndMakeChildHasFalse(String isPartOfAsset);

    Asset getAssetByWorkOrderAssetId(String assetId);

    AssetDTOWithParentAsset getOneAssetWithParentInfo(String assetId);

    boolean deleteChildAssets(String assetId);

    List<String> getParentsOfAsset(String assetId);

    List<Asset> getAllAssetsThatHaveStorage(List<String> assetIdList);

    List<Asset> getAllAssetsOfStorageList(List<String> storageListAssetId);

    List<Asset> getAssetsByAssetIdListOfWorkOrders(List<String> workOrdersAssetId);

    List<UnitOfMeasurement> getUnitListOfAsset(String assetId);

    Asset getAssetOfScheduleMaintenanceByAssetId(String assetId);

    Asset getAssetOfMetering(String referenceId);

    boolean checkIfUserExistsAsset(String userId);

    List<Asset> getAssetNameOfWorkRequest(List<String> assetIdList);

    Asset getAssetNameAndCodeOfTheWorkOrder(String workOrderId);

    List<Asset> newGetAllRootFacility();
     List<Asset> getAllRooAsset() ;
    Asset findAssetOfWorkRequest(String assetId);

    Asset getActivitiesOfAsset(String assetId);

    Asset findFaultyAsset(String assetId);

    List<Asset> getAllRelevantAssetOfWorkRequest(List<String> assetIdList);

    boolean ifProvinceExistsInAsset(String provinceId);

    boolean ifCityExistsInAsset(String cityId);

    boolean ifBudgetExistsInAsset(String budgetId);

    boolean ifUnitExistsInAsset(String unitOfMeasurementId);

    boolean ifPropertyExistsInAsset(String propertyId);

    boolean ifCompanyExistsInAsset(String companyId);

    boolean ifChargeDepartmentExistsInAsset(String chargeDepartmentId);

    boolean ifAssetTemplateExistsInAsset(String assetTemplateId);

    List<Asset> getAssetOfWorkOrder(List<String> assetIdListOfWorkOrder);

    List<Asset> getAllAssetsOfUser(String userId);

    List<Asset> getParentAssets(List<String> parentAssetIdList);

    List<Asset> getOnlineAssets();

    List<Asset> getOfflineAssets();

    List<Asset> getAssetsName(List<String> assetIdList);

    List<Asset> getUserOnlineAsset(String assignedUserId);

    long countUserOnlineAsset(String userAssignedId);

    List<Asset> getUserOfflineAsset(String userAssignedId);

    long countUserOfflineAsset(String userAssignedId);

    Asset getAssetWorkRequest(String assetId);

    boolean ifActivityExistInAsset(String activityId);

    List<UserWithUserTypeNameDTO> getPersonsOfAsset(String assetId);

    List<AssetGroupPersonnelDTO> getGroupPersonnelOfAsset(String assetId);

    boolean addPersonTypePersonnel(String assetId, List<AssignedToPerson> assignedToPersonList);

    boolean addGroupTypePersonnel(String assetId, List<AssignedToGroup> assignedToGroupList);

    boolean checkIfAssetTemplateUsedInAsset(String assetTemplateId);

    Page<AssetNameAndId> getAllAssetByTerm(String term, Pageable pageable, Integer totalElement);

    Page<AssetDTO> getAllAssignedAssetsOfGroup(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement);

    Asset getAssetName(String assetId);

    Asset getParent(String assetId);

    boolean isAssetStillParent(String isPartOfAsset);

    void updateHasChild(String isPartOfAsset);

    boolean checkIfCategoryUsedInAsset(String categoryId);

    List<Category> NewGetAllCategory();

    List<Asset> getAllAssetsName();

    List<Asset> getAllFacility();
}
