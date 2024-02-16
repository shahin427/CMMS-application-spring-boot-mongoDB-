package org.sayar.net.Dao.NewDao.asset;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Dao.NewDao.asset.aggregationResult.ParentAndSubActivityId;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.Asset.*;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.newModel.Asset.Logs.AssetConsumablePartLog;
import org.sayar.net.Model.newModel.Asset.Logs.AssetRepairSchedulingLog;
import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.Image;
import org.sayar.net.Model.newModel.Node.Node;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.sayar.net.Model.newModel.metering.model.Metering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.Document;
import java.util.List;

public interface AssetDao extends GeneralDao<Asset> {

    List<AssetDTO> getAllByFilterAndPagination(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement);

    Asset updateAsset(Asset asset);


    //    ________________________________________________
    public List<Asset> findAllInTreeNode();

    public List<Asset> getAssetByCategory(String catId);

    public List<AssetDTO> getCompressAllAsset();

    public List<Node> getTreeNodeBase();

    public List<Node> getTreeNodeLayer(String id);

    public List<Node> getTreeNodeBaseByCategory(String catId);

    public List<Node> getTreeNodeLayerByCategory(String id, String catId);

    public List<Node> getTreeNodeSearch(String searchTerm);

    public List<Node> getTreeNodeSearchByCategory(String searchTerm, String catId);

    public List<Node> getAssetChildrenInTreeNode(String assetId);

    public List<Asset> getAssetForLocationCategory(String organId);

    public long getAssetForLocationCategoryCount(String organId);

    public long getAssetForEquipmentCategoryCount(String organId);

    public long getAssetForToolsCategoryCount(String organId);

    public long getOrganAssetCount(String organId);

    public Image getAssetImage(String assetId);

    public boolean detachImage(String assetId);

    public void addAssetRepairSchedulingLog(String assetId, AssetRepairSchedulingLog log);

    public void addAssetConsumablePartLog(String assetId, String partId, String workOrderId, AssetConsumablePartLog log);

    public void addAssetOnlineOfflineLog(String assetId, boolean online, String userId);

    public void addAssetOpenWorkOrderLog(String assetId, String description);

    public void addAssetWorkOrderDateLog(String assetId, String logDescription);

    public void addAssetMeterReadingLog(String asset);

    public void updateAssetMeterReadingLog(long amount, String meteringId);

    public boolean assetHaveLogForMetering(String meteringId);

    public void addAssetSchedulteMaintananceLog(ScheduleMaintenance scheduleMaintanance, String userId);

    public boolean getAssetStatus(String assetId);


    // formules
    double calculateMTTR(String assetId);

    double calculateMTBF(String assetId);

    List<AssetDTO> getByIdList(List<String> idList);

    ParentAndSubActivityId findOneByIdCategoriesIncluded(String assetId);

    void updateMetering(String assetId, Metering metering);

    void incMeterAmount(Asset asset, int incAmount);

    AssetDaoImpl.GetOneAgg getOne(String id);

    List<purchaseAssetDTO> getAssetDTOForPurchase();

    Asset postAsset(Asset asset);

    long countAllAssets(AssetSearchDTO assetDTO);

    boolean checkIfCodeExists(String assetCode);

    UpdateResult updateAssetBasicInformation(String assetId, AssetBasicInformation assetBasicInformation);

    AssetBasicInformation getOneByAssetId(String assetId);

    boolean deleteCreateAsset(String assetId);

    UpdateResult updateCreateAsset(CreateAsset createAsset);

//    List<Asset> getAllPartsById(String assetId);

    Asset getAllCompanyByAssetId(String assetId);

    Asset getAllUsersByAssetId(String assetId);

    Asset getAssetByAssetId(String assetId);

    List<Asset> getAllWarrantyByAssetId(String assetId);

    Asset getAllMeteringByAssetId(String assetId);

    Asset getAllPartByAssetId(String assetId);

    boolean updatePartListIdByAssetId(String assetId, List<String> partsId);

    boolean updateCompanyListByAssetId(String assetId, List<String> companyId);

    UpdateResult updatePropertyListByAssetId(String assetId, List<Property> propertyList);

    boolean updateUserListByAssetId(String assetId, List<String> usersId);

    UpdateResult updateWarrantyListByAssetId(String assetId, List<String> warrantyId);

    UpdateResult updateMeteringListByAssetId(String assetId, List<String> meteringId);

    Asset getOneAsset(String assetId);

    Asset getOneAssetName(String assetId);

    Asset getUserByAssetIdList(String assetId);

    Asset getPartByAssetIdList(String assetId);

    Asset getPropertyListByAssetId(String assetId);

    Asset getDocumentListByAssetId(String assetId);

    UpdateResult updateDocumentListByAssetId(String assetId, List<Document> documents);

    List<Asset> getAllAssetByCategoryType(CategoryType categoryType);

    List<Asset> getAllNameAndCode();

    List<Asset> getAllAsset();

    List<Asset> getAssetByAssetIdList(List<String> assetIdListForSendingNotification);

    List<Asset> getAllAssetByAssetIdList(List<String> assetIdList);

    long countAllOfflineAssets();

    long countAllOnlineAssets();

    Page<Asset> getAllAssetForBOM(Pageable pageable, Integer totalElement);

    List<AssetDTO> getAllAssetOfAssignedToUserByUserId(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement);

    List<InDependentAssetDTO> getAllIndependentAssets(Pageable pageable, Integer totalElement);

    List<AssetDTOForChildren> getAllChildrenOfAUser(String parentId);

    List<Asset> getAssetsByCategoryType(String categoryType);

    void makeParentHasChildTrue(String isPartOfAsset);

    void getParentAssetAndMakeChildHasFalse(String isPartOfAsset);

    Asset getAssetByWorkOrderAssetId(String assetId);

    Asset getParentAssetByIsPartOfId(String isPartOfAsset);

    void checkIfOldAssetIsParent(Asset parentAssetOfOldAsset);

    void checkIfNewAssetIsParent(Asset newAsset1);

    Asset MakeParentOfNewAssetHasChildTrue(String isPartOfAsset);

    boolean ifDeletedAssetHasChild(String id);

    void ifDeletedAssetParentIsStillParen(Asset deletedAssetParent);

    List<String> getParentsOfAsset(Asset asset);

    List<Asset> getAllAssetsThatHaveStorage(List<String> assetIdList);

    List<Asset> getAllAssetsOfStorageList(List<String> storageListAssetId);

    List<Asset> getAssetsByAssetIdListOfWorkOrders(List<String> workOrdersAssetId);

    Asset getAssetOfScheduleMaintenanceByAssetId(String assetId);

    Asset getAssetOfMetering(String referenceId);

    boolean checkAssetCodeIsUnique(String code);

    Asset getAssetOfScheduleByScheduleAssetId(String assetId);

    boolean checkIfUserExistsAsset(String userId);

    List<Asset> getAssetNameOfWorkRequest(List<String> assetIdList);

    Asset getAssetNameAndCodeOfTheWorkOrder(String workOrderId);

    List<Asset> newGetAllRootFacility();

    List<Asset> getAllRooAsset();

    Asset getParentOfCreatedAsset(String isPartOfAsset);

    Asset findAssetOfWorkRequest(String assetId);

    Asset getActivitiesOfAsset(String assetId);

    Asset getRelevantAssetOfTheSchedule(String assetId);

    List<Activity> getActivityListOfTheAsset(List<String> activityIdList);

    void makeRootIdAsRoot(Asset createdAsset);

    Asset findFaultyAsset(String assetId);

    List<Asset> getAllRelevantAssetOfWorkRequest(List<String> assetIdList);

    Asset getAllUnitOfMeasurementOfTheAsset(String assetId);

    boolean ifProvinceExistsInAsset(String provinceId);

    boolean ifCityExistsInAsset(String cityId);

    boolean ifBudgetExistsInAsset(String budgetId);

    boolean ifUnitExistsInAsset(String unitOfMeasurementId);

    boolean ifPropertyExistsInAsset(String propertyId);

    boolean ifCompanyExistsInAsset(String companyId);

    boolean ifChargeDepartmentExistsInAsset(String chargeDepartmentId);

    boolean ifAssetTemplateExistsInAsset(String assetTemplateId);

    void makeRootId(String id, Asset createdAsset);

    void makeNewVersionAssetAsRoot(Asset newVersionAsset);

    void fillRootIdOfNewVersionAsset(Asset newVersionAsset, String root);

    List<Asset> getFirstLayerChildrenOfNewVersionAsset(String id);

    void getNextLayerOfNewVersionAssetChildren(List<Asset> firstLayerChildrenOfTheNewVersionAsset, List<Asset> childrenOfTheNewVersionAsset);

    void updateTheRootIdsOfChildrenAssetOfNewVersionAsset(List<Asset> childrenOfTheNewVersionAsset, Asset newVersionAsset);

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

    long countAllAssetOfAssignedToUserByUserId(AssetSearchDTO assetDTO);

    Asset getPersonsOfAsset(String assetId);

    Asset getGroupPersonnelOfAsset(String assetId);

    boolean addPersonTypePersonnel(String assetId, List<AssignedToPerson> assignedToPersonList);

    boolean addGroupTypePersonnel(String assetId, List<AssignedToGroup> assignedToGroupList);

    boolean checkIfAssetTemplateUsedInAsset(String assetTemplateId);

    boolean deleteChildAssets(String assetId);

    long countIndependentAssets(Pageable pageable, Integer totalElement);

    List<AssetNameAndId> getAllAssetByTerm(String term, Pageable pageable, Integer totalElement);

    String getParentBuildingAsset(String isPartOfAsset);

    void updateAssetChildren(String id);

    long countAllAssetsByTerm(String term);

    List<AssetDTO> getAllAssignedAssetsOfGroup(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement);

    long countAllAssignedAssetsOfGroup(AssetSearchDTO assetDTO);

    Asset getAssetName(String assetId);

    Asset getParent(String assetId);

    boolean isAssetStillParent(String isPartOfAsset);

    void updateHasChild(String isPartOfAsset);

    boolean checkIfCategoryUsedInAsset(String categoryId);

    List<Category> NewGetAllCategory();

    List<Asset> getAllAssetsName();

    List<Asset> getAllFacility();

    List<Activity> getListActivityListByAssetId(String assetId);
}
