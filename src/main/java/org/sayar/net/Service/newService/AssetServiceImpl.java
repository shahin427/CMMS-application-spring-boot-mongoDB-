package org.sayar.net.Service.newService;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Dao.Asset.AssetTemplateDao;
import org.sayar.net.Dao.ImageDao;
import org.sayar.net.Dao.NewDao.CompanyDao;
import org.sayar.net.Dao.NewDao.PartDao;
import org.sayar.net.Dao.NewDao.WarrantyDao;
import org.sayar.net.Dao.NewDao.WorkOrderDao;
import org.sayar.net.Dao.NewDao.asset.AssetDao;
import org.sayar.net.Dao.NewDao.asset.AssetDaoImpl;
import org.sayar.net.Dao.NewDao.asset.InDependentAssetDTO;
import org.sayar.net.Dao.UserDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.*;
import org.sayar.net.Model.AssetGroupPersonnelDTO;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.Mongo.MyModel.Activity;
import org.sayar.net.Model.User;
import org.sayar.net.Model.UserType;
import org.sayar.net.Model.newModel.*;
import org.sayar.net.Model.newModel.Helper.TreeNode;
import org.sayar.net.Model.newModel.Node.Node;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.Model.newModel.metering.dao.MeteringDao;
import org.sayar.net.Model.newModel.metering.model.Metering;
import org.sayar.net.Security.TokenPrinciple;
import org.sayar.net.Service.CategoryService;
import org.sayar.net.Service.UnitOfMeasurementService;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.util.ArrayList;
import java.util.List;

@Service("assetServiceImpl")
public class AssetServiceImpl extends GeneralServiceImpl<Asset> implements AssetService {
    private final AssetDao dao;
    private final UserService userService;
    private final CompanyDao companyDao;
    private final UserDao userDao;
    private final WarrantyDao warrantyDao;
    private final MeteringDao meteringDao;
    private final AssetTemplateDao assetTemplateDao;
    private final AssetTemplateService assetTemplateService;
    private final UnitOfMeasurementService unitOfMeasurementService;
    private final UserTypeService userTypeService;
    private final OrganService organService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    public AssetServiceImpl(AssetDao dao, ImageDao imageDao, MessageService messageService, TokenPrinciple tokenPrinciple, WorkOrderDao workOrderDao, UserService userService, CompanyDao companyDao, UserDao userDao, WarrantyDao warrantyDao, MeteringDao meteringDao, PartDao partDao, AssetTemplateDao assetTemplateDao, AssetTemplateService assetTemplateService, UnitOfMeasurementService unitOfMeasurementService, BudgetService budgetService, ChargeDepartmentService chargeDepartmentService, UserTypeService userTypeService, OrganService organService) {
        this.dao = dao;
        this.userService = userService;
        this.companyDao = companyDao;
        this.userDao = userDao;
        this.warrantyDao = warrantyDao;
        this.meteringDao = meteringDao;
        this.assetTemplateDao = assetTemplateDao;
        this.assetTemplateService = assetTemplateService;
        this.unitOfMeasurementService = unitOfMeasurementService;
        this.userTypeService = userTypeService;
        this.organService = organService;
    }

    @Override
    public Page<AssetFilterDTO> getAllByFilterAndPagination(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement) {
        List<AssetDTO> assetDTOS = dao.getAllByFilterAndPagination(assetDTO, pageable, totalElement);
        List<String> assetTemplateId = new ArrayList<>();
        List<String> categoryIdList = new ArrayList<>();
        if (assetDTOS != null) {
            assetDTOS.forEach(assetDTO1 -> {
                if (assetDTO1.getAssetTemplateId() != null) {
                    assetTemplateId.add(assetDTO1.getAssetTemplateId());
                }

                if (assetDTO1.getCategoryId() != null) {
                    categoryIdList.add(assetDTO1.getCategoryId());
                }
            });
            List<AssetTemplate> assetTemplateList = assetTemplateService.getAllAssetTemplateByIdList(assetTemplateId);
            List<Category> categoryList = categoryService.getCategoryListByCategoryIdList(categoryIdList);
            return new PageImpl<>(
                    AssetFilterDTO.map(categoryList, assetTemplateList, assetDTOS)
                    , pageable
                    , dao.countAllAssets(assetDTO));
        } else
            return null;
    }

    @Override
    public AssetBasicInformation getOneByAssetId(String assetId) {
        AssetBasicInformation assetBasicInformation = dao.getOneByAssetId(assetId);

        if (assetBasicInformation.getBudgetId().equals("")) {
            assetBasicInformation.setBudgetId(null);
        }
        if (assetBasicInformation.getChargeDepartmentId().equals("")) {
            assetBasicInformation.setChargeDepartmentId(null);
        }
        if (assetBasicInformation.getAddress() != null && assetBasicInformation.getAddress().getCityId().equals("")) {
            assetBasicInformation.getAddress().setCityId(null);
        }
        if (assetBasicInformation.getAddress() != null && assetBasicInformation.getAddress().getProvinceId().equals("")) {
            assetBasicInformation.getAddress().setProvinceId(null);
        }
        return assetBasicInformation;
//        Budget budgetTitle = null;
//        if (asset.getBudgetId() != null) {
//            budgetTitle = budgetService.getBudgetTitle(asset.getBudgetId());
//        }
//        ChargeDepartment chargeDepartmentTitle = null;
//        if (asset.getChargeDepartmentId() != null) {
//            chargeDepartmentTitle = chargeDepartmentService.getChargeDepartmentTitle(asset.getChargeDepartmentId());
//        }
//        List<UnitOfMeasurement> unitOfMeasurementTitleList = null;
//        if (asset.getUnitIdList() != null) {
//            unitOfMeasurementTitleList = unitOfMeasurementService.getUnitOfMeasurementTitle(asset.getUnitIdList());
//        }
//        return AssetBasicInformation.map(asset, budgetTitle, chargeDepartmentTitle, unitOfMeasurementTitleList);
    }

    @Override
    public boolean deleteCreateAsset(String assetId) {
        return dao.deleteCreateAsset(assetId);
    }

    @Override
    public boolean updateCreateAsset(CreateAsset createAsset) {
        return super.updateResultStatus(dao.updateCreateAsset(createAsset));
    }

//    @Override
//    public List<AssetDTOPart> getAllPartsById(String assetId) {
//        List<String> partListId = new ArrayList<>();
//        List<Asset> assetList = dao.getAllPartsById(assetId);
//        assetList.forEach(asset -> {
//            partListId.addAll(asset.getParts());
//        });
//             List<Part> partList = PartDao.getPartsByAssetIdList(partListId);
//        return null;
//    }

    @Override
    public AssetDTOCompany getAllCompanyByAssetId(String assetId) {
        Asset asset = dao.getAllCompanyByAssetId(assetId);
        if (asset.getCompanyList() != null) {
            List<Company> companyList = companyDao.getCompanyByIdList(asset.getCompanyList());
            return AssetDTOCompany.map(companyList);
        } else
            return null;
    }

    @Override
    public List<UserDTOWithUserTypeDTO> getAllUsersByAssetId(String assetId) {
        Asset asset = dao.getUserByAssetIdList(assetId);
        if (asset.getUsers() != null) {
            List<User> userList = userDao.getByIdList(asset.getUsers(), User.class);
            List<String> userTypeIdList = new ArrayList<>();
//            userList.forEach(user -> userTypeIdList.add(user.getUserTypeId()));
            //TODO commented part was red
            List<UserType> userTypeTitle = userTypeService.getUserTypeTitleList(userTypeIdList);
            return UserDTOWithUserTypeDTO.map2(userList, userTypeTitle);
        } else
            return null;
    }

    @Override
    public List<AssetDTOWarranty> getAllWarrantyByAssetId(String assetId) {
        List<String> warrantyIdList = new ArrayList<>();
        List<Asset> assetList = dao.getAllWarrantyByAssetId(assetId);
        assetList.forEach(asset ->
                warrantyIdList.addAll(asset.getWarranties()));
        List<Warranty> warrantyList = warrantyDao.getWarrantyByIdList(warrantyIdList);
        return AssetDTOWarranty.map(assetList, warrantyList);
    }

    @Override
    public AssetDTOTemplate getAllPropertyByAssetId(String assetId) {
        Asset asset = dao.getAssetByAssetId(assetId);
        AssetTemplate assetTemplate = assetTemplateDao.getOneAssetTemplate(asset.getAssetTemplateId());
        if (assetTemplate != null) {
            return AssetDTOTemplate.map(assetTemplate);
        } else return null;
    }

    @Override
    public AssetDTOMetering getAllMeteringByAssetId(String assetId) {
//        List<String> meteringIdList = new ArrayList<>();
        Asset asset = dao.getAllMeteringByAssetId(assetId);
//        assetList.forEach(asset -> {
//            meteringIdList.addAll(asset.getMeterings());
//        });
        List<Metering> meteringList = meteringDao.getMeteringByAssetId(asset.getMeterings());
        return AssetDTOMetering.map(asset, meteringList);
    }

    @Override
    public Asset getAllPartByAssetId(String assetId) {
        return dao.getAllPartByAssetId(assetId);
    }

    @Override
    public boolean updatePartListIdByAssetId(String assetId, List<String> partsId) {
        return dao.updatePartListIdByAssetId(assetId, partsId);
    }

    @Override
    public boolean updateCompanyListByAssetId(String assetId, List<String> companyId) {
        return dao.updateCompanyListByAssetId(assetId, companyId);
    }

    @Override
    public boolean updatePropertyListByAssetId(String assetId, List<Property> propertyList) {
        return this.updateResultStatus(dao.updatePropertyListByAssetId(assetId, propertyList));
    }

    @Override
    public boolean updateUserListByAssetId(String assetId, List<String> usersId) {
        return dao.updateUserListByAssetId(assetId, usersId);
    }

    @Override
    public UpdateResult updateWarrantyListByAssetId(String assetId, List<String> warrantyId) {
        return dao.updateWarrantyListByAssetId(assetId, warrantyId);
    }

    @Override
    public UpdateResult updateMeteringListByAssetId(String assetId, List<String> meteringId) {
        return dao.updateMeteringListByAssetId(assetId, meteringId);
    }

    @Override
    public Asset getOneAsset(String assetId) {
        return dao.getOneAsset(assetId);
    }

    @Override
    public List<Activity> getListActivityListByAssetId(String assetId) {
        return dao.getListActivityListByAssetId(assetId);
    }

    @Override
    public Asset getOneAssetName(String assetId) {
        return dao.getOneAssetName(assetId);
    }

    @Override
    public Asset getPropertyListByAssetId(String assetId) {
        return dao.getPropertyListByAssetId(assetId);
    }

    @Override
    public Asset getDocumentListByAssetId(String assetId) {
        return dao.getDocumentListByAssetId(assetId);
    }

    @Override
    public boolean updateDocumentListByAssetId(String assetId, List<Document> documents) {
        return super.updateResultStatus(dao.updateDocumentListByAssetId(assetId, documents));
    }

    @Override
    public List<Asset> getAllAssetByCategoryType(CategoryType categoryType) {
        return dao.getAllAssetByCategoryType(categoryType);
    }

    @Override
    public List<Asset> getAllNameAndCode() {
        return dao.getAllNameAndCode();
    }

    @Override
    public List<Asset> getAssetByAssetIdList(List<String> assetIdList) {
        return dao.getAssetByAssetIdList(assetIdList);
    }

    @Override
    public long countAllOfflineAssets() {
        return dao.countAllOfflineAssets();
    }

    @Override
    public long countAllOnlineAssets() {
        return dao.countAllOnlineAssets();
    }

    @Override
    public Page<Asset> getAllAssetForBOM(Pageable pageable, Integer totalElement) {
        return dao.getAllAssetForBOM(pageable, totalElement);
    }

    @Override
    public Page<AssetDTO> getAllAssetOfAssignedToUserByUserId(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllAssetOfAssignedToUserByUserId(assetDTO, pageable, totalElement),
                pageable,
                dao.countAllAssetOfAssignedToUserByUserId(assetDTO)
        );
    }

    @Override
    public Page<InDependentAssetDTO> getAllIndependentAssets(Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllIndependentAssets(pageable, totalElement),
                pageable,
                dao.countIndependentAssets(pageable, totalElement)
        );
    }

    @Override
    public List<AssetDTOForChildren> getAllChildrenOfAUser(String parentId) {
        return dao.getAllChildrenOfAUser(parentId);
    }

    @Override
    public List<Asset> getAssetsByCategoryType(String categoryType) {
        return dao.getAssetsByCategoryType(categoryType);
    }

    @Override
    public void getParentAssetAndMakeChildHasFalse(String isPartOfAsset) {
        dao.getParentAssetAndMakeChildHasFalse(isPartOfAsset);
    }

    @Override
    public Asset getAssetByWorkOrderAssetId(String assetId) {
        return dao.getAssetByWorkOrderAssetId(assetId);
    }

    @Override
    public AssetDTOWithParentAsset getOneAssetWithParentInfo(String assetId) {
        Asset asset = dao.getOneAsset(assetId);
        Asset parentAsset = dao.getParentAssetByIsPartOfId(asset.getIsPartOfAsset());
        return AssetDTOWithParentAsset.map(asset, parentAsset);
    }

    @Override
    public boolean deleteChildAssets(String assetId) {
        return dao.deleteChildAssets(assetId);
//        boolean deleted;
//        Asset deletedAsset = dao.getOneAsset(assetId);
//        boolean childExist = dao.ifDeletedAssetHasChild(deletedAsset.getId());
//        if (deletedAsset.getHasChild()) {
//            System.out.println("{\"this asset has child\"}");
//            return false;
//        } else {
//            deleted = super.updateResultStatus(dao.logicDeleteById(assetId, Asset.class));
//            if (deletedAsset.getIsPartOfAsset() != null) {
//                Asset deletedAssetParent = dao.getOneAsset(deletedAsset.getIsPartOfAsset());
//                dao.ifDeletedAssetParentIsStillParen(deletedAssetParent);
//            }
//            return deleted;
//        }
    }

    @Override
    public List<String> getParentsOfAsset(String assetId) {
        Asset asset = dao.getOneAsset(assetId);
        return dao.getParentsOfAsset(asset);
    }

    @Override
    public List<Asset> getAllAssetsThatHaveStorage(List<String> assetIdList) {
        return dao.getAllAssetsThatHaveStorage(assetIdList);
    }

    @Override
    public List<Asset> getAllAssetsOfStorageList(List<String> storageListAssetId) {
        return dao.getAllAssetsOfStorageList(storageListAssetId);
    }

    @Override
    public List<Asset> getAssetsByAssetIdListOfWorkOrders(List<String> workOrdersAssetId) {
        return dao.getAssetsByAssetIdListOfWorkOrders(workOrdersAssetId);
    }

    @Override
    public List<UnitOfMeasurement> getUnitListOfAsset(String assetId) {
        Asset asset = dao.getOneAsset(assetId);
        if (asset != null && asset.getUnitIdList() != null) {
            return unitOfMeasurementService.getAllUnitOfMeasurementById(asset.getUnitIdList());
        } else
            return null;
    }

    @Override
    public Asset getAssetOfScheduleMaintenanceByAssetId(String assetId) {
        return dao.getAssetOfScheduleMaintenanceByAssetId(assetId);
    }

    @Override
    public Asset getAssetOfMetering(String referenceId) {
        return dao.getAssetOfMetering(referenceId);
    }

    @Override
    public boolean checkIfUserExistsAsset(String userId) {
        return dao.checkIfUserExistsAsset(userId);
    }

    @Override
    public List<Asset> getAssetNameOfWorkRequest(List<String> assetIdList) {
        return dao.getAssetNameOfWorkRequest(assetIdList);
    }

    @Override
    public Asset getAssetNameAndCodeOfTheWorkOrder(String workOrderId) {
        return dao.getAssetNameAndCodeOfTheWorkOrder(workOrderId);
    }

    @Override
    public List<Asset> newGetAllRootFacility() {
        return dao.newGetAllRootFacility();
    }

    @Override
    public List<Asset> getAllRooAsset() {
        return dao.getAllRooAsset();
    }

    @Override
    public Asset findAssetOfWorkRequest(String assetId) {
        return dao.findAssetOfWorkRequest(assetId);
    }

    @Override
    public Asset getActivitiesOfAsset(String assetId) {
        return dao.getActivitiesOfAsset(assetId);
    }

    @Override
    public Asset findFaultyAsset(String assetId) {
        return dao.findFaultyAsset(assetId);
    }

    @Override
    public List<Asset> getAllRelevantAssetOfWorkRequest(List<String> assetIdList) {
        return dao.getAllRelevantAssetOfWorkRequest(assetIdList);
    }

    @Override
    public boolean ifProvinceExistsInAsset(String provinceId) {
        return dao.ifProvinceExistsInAsset(provinceId);
    }

    @Override
    public boolean ifCityExistsInAsset(String cityId) {
        return dao.ifCityExistsInAsset(cityId);
    }

    @Override
    public boolean ifBudgetExistsInAsset(String budgetId) {
        return dao.ifBudgetExistsInAsset(budgetId);
    }

    @Override
    public boolean ifUnitExistsInAsset(String unitOfMeasurementId) {
        return dao.ifUnitExistsInAsset(unitOfMeasurementId);
    }

    @Override
    public boolean ifPropertyExistsInAsset(String propertyId) {
        return dao.ifPropertyExistsInAsset(propertyId);
    }

    @Override
    public boolean ifCompanyExistsInAsset(String companyId) {
        return dao.ifCompanyExistsInAsset(companyId);
    }

    @Override
    public boolean ifChargeDepartmentExistsInAsset(String chargeDepartmentId) {
        return dao.ifChargeDepartmentExistsInAsset(chargeDepartmentId);
    }

    @Override
    public boolean ifAssetTemplateExistsInAsset(String assetTemplateId) {
        return dao.ifAssetTemplateExistsInAsset(assetTemplateId);
    }

    @Override
    public List<Asset> getAssetOfWorkOrder(List<String> assetIdListOfWorkOrder) {
        return dao.getAssetOfWorkOrder(assetIdListOfWorkOrder);
    }

    @Override
    public List<Asset> getAllAssetsOfUser(String userId) {
        return dao.getAllAssetsOfUser(userId);
    }

    @Override
    public List<Asset> getParentAssets(List<String> parentAssetIdList) {
        return dao.getParentAssets(parentAssetIdList);
    }

    @Override
    public List<Asset> getOnlineAssets() {
        return dao.getOnlineAssets();
    }

    @Override
    public List<Asset> getOfflineAssets() {
        return dao.getOfflineAssets();
    }

    @Override
    public List<Asset> getAssetsName(List<String> assetIdList) {
        return dao.getAssetsName(assetIdList);
    }

    @Override
    public List<Asset> getUserOnlineAsset(String assignedUserId) {
        return dao.getUserOnlineAsset(assignedUserId);
    }

    @Override
    public long countUserOnlineAsset(String userAssignedId) {
        return dao.countUserOnlineAsset(userAssignedId);
    }

    @Override
    public List<Asset> getUserOfflineAsset(String userAssignedId) {
        return dao.getUserOfflineAsset(userAssignedId);
    }

    @Override
    public long countUserOfflineAsset(String userAssignedId) {
        return dao.countUserOfflineAsset(userAssignedId);
    }

    @Override
    public Asset getAssetWorkRequest(String assetId) {
        return dao.getAssetWorkRequest(assetId);
    }

    @Override
    public boolean ifActivityExistInAsset(String activityId) {
        return dao.ifActivityExistInAsset(activityId);
    }

    @Override
    public List<UserWithUserTypeNameDTO> getPersonsOfAsset(String assetId) {

        Asset asset = dao.getPersonsOfAsset(assetId);

        if (asset.getAssignedToPersonList() != null) {
            List<String> users = new ArrayList<>();
            asset.getAssignedToPersonList().forEach(assignedToPerson -> {
                        users.add(assignedToPerson.getUserId());
                    }
            );
            return userService.getPersonnelOfProject(users);
        } else
            return null;
//        List<String> userIdList = new ArrayList<>();
////        List<String> orgIdList = new ArrayList<>();
//        List<String> userTypeIdList = new ArrayList<>();
//        if (asset.getAssignedToPersonList() != null) {
//            asset.getAssignedToPersonList().forEach(assignedToPerson -> {
//                        userIdList.add(assignedToPerson.getUserId());
////                        orgIdList.add(assignedToPerson.getOrgId());
//                        userTypeIdList.add(assignedToPerson.getUserTypeId());
//                    }
//            );
//            Print.print("userIdList", userIdList);
//            Print.print("userTypeIdList", userTypeIdList);
//            List<User> userList = userDao.getNameAndFamilyNameOfUsers(userIdList);
//            Print.print("userList", userList);
////            List<Organization> organizationList = organService.getOrganizationNameAndId(orgIdList);
//            List<UserType> userTypeList = userTypeService.getUserTypeName(userTypeIdList);
//            Print.print("userTypeList", userTypeList);
//            return AssetPersonnelDTO.map(userList, userTypeList);
//        } else
//            return null;

    }

    @Override
    public List<AssetGroupPersonnelDTO> getGroupPersonnelOfAsset(String assetId) {
        Asset asset = dao.getGroupPersonnelOfAsset(assetId);
        List<String> orgIdList = new ArrayList<>();
        List<String> userTypeIdList = new ArrayList<>();
        if (asset.getAssignedToGroupList() != null) {
            asset.getAssignedToGroupList().forEach(assignedToGroup -> {
                orgIdList.add(assignedToGroup.getOrgId());
                userTypeIdList.add(assignedToGroup.getUserTypeId());
            });
            List<Organization> organizationList = organService.getOrganizationNameAndId(orgIdList);
            List<UserType> userTypeList = userTypeService.getUserTypeName(userTypeIdList);
            return AssetGroupPersonnelDTO.map(asset, organizationList, userTypeList);
        } else
            return null;
    }

    @Override
    public boolean addPersonTypePersonnel(String assetId, List<AssignedToPerson> assignedToPersonList) {
        return dao.addPersonTypePersonnel(assetId, assignedToPersonList);
    }

    @Override
    public boolean addGroupTypePersonnel(String assetId, List<AssignedToGroup> assignedToGroupList) {
        return dao.addGroupTypePersonnel(assetId, assignedToGroupList);
    }

    @Override
    public boolean checkIfAssetTemplateUsedInAsset(String assetTemplateId) {
        return dao.checkIfAssetTemplateUsedInAsset(assetTemplateId);
    }

    @Override
    public Page<AssetNameAndId> getAllAssetByTerm(String term, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllAssetByTerm(term, pageable, totalElement),
                pageable,
                dao.countAllAssetsByTerm(term)
        );
    }

    @Override
    public Page<AssetDTO> getAllAssignedAssetsOfGroup(AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllAssignedAssetsOfGroup(assetDTO, pageable, totalElement),
                pageable,
                dao.countAllAssignedAssetsOfGroup(assetDTO)
        );
    }

    @Override
    public Asset getAssetName(String assetId) {
        return dao.getAssetName(assetId);
    }

    @Override
    public Asset getParent(String assetId) {
        return dao.getParent(assetId);
    }

    @Override
    public boolean isAssetStillParent(String isPartOfAsset) {
        return dao.isAssetStillParent(isPartOfAsset);
    }

    @Override
    public void updateHasChild(String isPartOfAsset) {
        dao.updateHasChild(isPartOfAsset);
    }

    @Override
    public boolean checkIfCategoryUsedInAsset(String categoryId) {
        return dao.checkIfCategoryUsedInAsset(categoryId);
    }

    @Override
    public List<Category> NewGetAllCategory() {
        return dao.NewGetAllCategory();
    }

    @Override
    public List<Asset> getAllAssetsName() {
        return dao.getAllAssetsName();
    }

    @Override
    public List<Asset> getAllFacility() {

        List<Asset> res= dao.getAllFacility();
        System.out.println(res.size());
        System.out.println(res.size());
        System.out.println(res.size());
        System.out.println(res.size());
        return dao.getAllFacility();
    }


    //    ___________________________________________
    @Override
    public Asset add(Asset entity, String organId) {

//        System.out.println("do asset service");
//
//        String date = PersianCalendar.getPersianDate(new Date());
//        messageService.createAutoMessage(
//                "Create Asset id date : " + date,
//                null,
//                date
//        );

//        Image file = entity.getImage();
//
//        if (file!=null && file.getImageStatus() == ImageStatus.NEW_IMAGE) {
//            if (file.imageData.equals("")) {
//                entity.setImage(null);
//            }
//
//            if (Base64Converter.base64Validation(file.imageData)) {
//                file.createImageBlob();
//            }
//        }else {
//            entity.setImage(null);
//        }

//        long id = dao.add(entity, organId);
        String id = "";


        entity.setId(id);

        Asset res = new Asset();
        if (res != null) {

        }

        return res;

    }

//    @Override
//    public boolean update(Asset entity, long organId) {
//
////        Print.print(entity);
//
//        Image file = entity.getImage();
//
//
//        if (file.getImageStatus() == ImageStatus.NEW_IMAGE){
//
//
//            if (Base64Converter.base64Validation(file.imageData)) {
//                file.createImageBlob();
//            }else {
//                entity.setImage(imageDao.findOne(file.getId()));
//            }
//
//        }else {
//            entity.setImage(null);
//        }
//
//
////        if (entity.getImage()!=null) {
////            if (file.imageData == null && file.imageData.equals("")) {
////                entity.setImage(null);
////            }else {
////                if (Base64Converter.base64Validation(file.imageData)) {
////                    file.createImageBlob();
////                }
////            }
////
////        }
//
//        boolean assetLastStatus =dao.getAssetStatus(entity.getId());
//        boolean res = dao.update(entity, organId);
//        if (assetLastStatus != entity.isStatus()){
//            manageLogs(entity);
//        }
//
//        return res;
//    }

    @Override
    public List<TreeNode> findAllInTreeNode() {
        List<Asset> assets = dao.findAllInTreeNode();
        List<TreeNode> result = new ArrayList<>();

        for (Asset a :
                assets) {
            result.add(
                    new TreeNode(a)
            );
        }

        return result;
    }


    @Override
    public List<Asset> getAssetByCategory(String catId) {
        return dao.getAssetByCategory(catId);
    }

    @Override
    public List<AssetDTO> getCompressAllAsset() {
        return dao.getCompressAllAsset();
    }

    @Override
    public List<Node> getTreeNodeBase() {
        return dao.getTreeNodeBase();
    }

    @Override
    public List<Node> getTreeNodeLayer(String id) {
        return dao.getTreeNodeLayer(id);
    }


    @Override
    public List<Node> getTreeNodeBaseByCategory(String catId) {
        return dao.getTreeNodeBaseByCategory(catId);
    }

    @Override
    public List<Node> getTreeNodeLayerByCategory(String id, String catId) {
        return dao.getTreeNodeLayerByCategory(id, catId);
    }

    @Override
    public List<Node> getTreeNodeSearch(String searchTerm) {
        return dao.getTreeNodeSearch(searchTerm);
    }

    @Override
    public List<Node> getTreeNodeSearchByCategory(String searchTerm, String catId) {
        return dao.getTreeNodeSearchByCategory(searchTerm, catId);
    }

    @Override
    public List<Node> getAssetChildrenInTreeNode(String assetId) {
        return dao.getAssetChildrenInTreeNode(assetId);
    }

    @Override
    public List<Asset> getAssetForLocationCategory(String organId) {
        return dao.getAssetForLocationCategory(organId);
    }

    @Override
    public long getAssetForLocationCategoryCount(String organId) {
        return dao.getAssetForLocationCategoryCount(organId);
    }

    @Override
    public long getAssetForEquipmentCategoryCount(String organId) {
        return dao.getAssetForEquipmentCategoryCount(organId);
    }

    @Override
    public long getAssetForToolsCategoryCount(String organId) {
        return dao.getAssetForToolsCategoryCount(organId);
    }


    @Override
    public double calculateMTTR(String assetId) {
        return dao.calculateMTTR(assetId);
    }

    @Override
    public double calculateMTBF(String assetId) {
        return dao.calculateMTBF(assetId);
    }

    @Override
    public List<AssetDTO> getByIdList(List<String> idList) {

        return dao.getByIdList(idList);
    }

    @Override
    public Asset update(Asset asset) {
//        ScheduleMaintenance scheduleMaintanance = scheduleDao.findOne("assetId", asset.getId(), ScheduleMaintenance.class);
//        boolean isRightTime = new Date().getTime() > scheduleMaintanance.getMeteringCycle().getStartOn() && new Date().getTime() < scheduleMaintanance.getMeteringCycle().getEndOn();

//        if (scheduleMaintanance.getMeteringCycle() != null && isRightTime && asset.getMeterAmount() >= scheduleMaintanance.getMeteringCycle().getPer()) {
//            WorkOrder workOrder = scheduleMaintanance.createWorkOrder();
//            workOrderDao.save(workOrder);
//            asset.setMeterAmount(asset.getMeterAmount() - scheduleMaintanance.getMeteringCycle().getPer());
//        }
//        this.save(asset);
        return null;
    }

    @Override
    public boolean addMeter(String assetId, Metering metering) {
//        try {
//            dao.updateMetering(assetId, metering);
//            Asset asset = dao.findOne("id", assetId, Asset.class, "meterAmount");
//            ScheduleMaintenance scheduleMaintanance = scheduleDao.findOneByAssetId(assetId, ScheduleType.METER);
//
//            boolean isRightTime;
//
//            if (scheduleMaintanance != null) {
//                isRightTime = new Date().getTime() > scheduleMaintanance.getMeteringCycle().getStartOn() && new Date().getTime() < scheduleMaintanance.getMeteringCycle().getEndOn();
//                if (scheduleMaintanance.getMeteringCycle() != null && isRightTime && asset.getMeterAmount() >= scheduleMaintanance.getMeteringCycle().getPer()) {
////                    WorkOrder workOrder = scheduleMaintanance.createWorkOrder();
//
////                    workOrderDao.save(workOrder);
//                    dao.incMeterAmount(asset, -scheduleMaintanance.getMeteringCycle().getPer());
//                }
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
        return true;

    }

    @Override
    public AssetDaoImpl.GetOneAgg getOne(String id) {
        return dao.getOne(id);
    }

    @Override
    public List<purchaseAssetDTO> getAssetDTOForPurchase() {
        return dao.getAssetDTOForPurchase();
    }

    @Override
    public Asset postAsset(Asset asset) {
        if (asset.getIsPartOfAsset() != null && !asset.getIsPartOfAsset().equals("")) {
            String parentBuilding = dao.getParentBuildingAsset(asset.getIsPartOfAsset());
            asset.setFirstParentBuilding(parentBuilding);
        } else {
            asset.setFirstParentBuilding(null);
        }
        Asset createdAsset = dao.postAsset(asset);
        if (createdAsset.getIsPartOfAsset() != null && !createdAsset.getIsPartOfAsset().equals("")) {
            dao.makeParentHasChildTrue(createdAsset.getIsPartOfAsset());

            /**
             * determining the rootId
             */
            Asset parentAsset = dao.getParentOfCreatedAsset(createdAsset.getIsPartOfAsset());

            if (parentAsset.getRootId().equals("ROOT")) {
                dao.makeRootId(parentAsset.getId(), createdAsset);
            } else {
                dao.makeRootId(parentAsset.getRootId(), createdAsset);
            }
        } else {
            dao.makeRootIdAsRoot(createdAsset);
        }
        return createdAsset;
    }

    @Override
    public boolean updateAsset(Asset asset) {

        Asset oldVersionAsset = dao.getOneAsset(asset.getId());

        if (asset.getIsPartOfAsset() != null && !asset.getIsPartOfAsset().equals("")) {
            String parentBuilding = dao.getParentBuildingAsset(asset.getIsPartOfAsset());
            asset.setFirstParentBuilding(parentBuilding);
        } else {
            asset.setFirstParentBuilding(null);
        }

        Asset newVersionAsset = dao.updateAsset(asset);
//        dao.updateAssetChildren(asset.getId());


        if (oldVersionAsset.getIsPartOfAsset() != null && !oldVersionAsset.getIsPartOfAsset().equals("")) {
            //finding parent of old version asset for checking has still child or not (checking will be occurring next service)
            Asset parentOfOldVersionAsset = dao.getOneAsset(oldVersionAsset.getIsPartOfAsset());
            //checking parent of old version asset has still child or not
            dao.checkIfOldAssetIsParent(parentOfOldVersionAsset);
        }

        if (newVersionAsset.getIsPartOfAsset() != null && !newVersionAsset.getIsPartOfAsset().equals("")) {
            // after updating the asset, if new version becomes child of an asset this asset's hasChild's field should be changed to true
            Asset parentAssetOfNewVersionAsset = dao.MakeParentOfNewAssetHasChildTrue(newVersionAsset.getIsPartOfAsset());
            //set rootId of new version asset
            dao.fillRootIdOfNewVersionAsset(newVersionAsset, parentAssetOfNewVersionAsset.getRootId());
        } else {
            // set rootId of new version asset
            dao.makeNewVersionAssetAsRoot(newVersionAsset);
        }

//        /**
//         * set rootId of new version asset's children
//         */
//        List<Asset> childrenOfTheNewVersionAsset = new ArrayList<>();
//        List<Asset> firstLayerChildrenOfTheNewVersionAsset = dao.getFirstLayerChildrenOfNewVersionAsset(newVersionAsset.getId());
//        Print.print("firstLayerChildrenOfTheNewVersionAsset", firstLayerChildrenOfTheNewVersionAsset);
//        childrenOfTheNewVersionAsset.addAll(firstLayerChildrenOfTheNewVersionAsset);
//        Print.print("childrenOfTheNewVersionAssetChildrenOfTheNewVersionAsset", childrenOfTheNewVersionAsset);
//        dao.getNextLayerOfNewVersionAssetChildren(firstLayerChildrenOfTheNewVersionAsset, childrenOfTheNewVersionAsset);
//
//        Print.print("nextLayerChildrenOfTheNewVersionAsset", firstLayerChildrenOfTheNewVersionAsset);
////        update the rootIds of children asset
//        dao.updateTheRootIdsOfChildrenAssetOfNewVersionAsset(childrenOfTheNewVersionAsset, newVersionAsset);
        return true;
    }

    @Override
    public boolean checkIfCodeExists(String assetCode) {
        return dao.checkIfCodeExists(assetCode);
    }

    @Override
    public boolean updateAssetBasicInformation(String assetId, AssetBasicInformation assetBasicInformation) {
        return super.updateResultStatus(dao.updateAssetBasicInformation(assetId, assetBasicInformation));
    }
}
