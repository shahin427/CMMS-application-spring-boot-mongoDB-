package org.sayar.net.Service.newService;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Dao.NewDao.PartDao;
import org.sayar.net.Dao.UserDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.*;
import org.sayar.net.Model.Asset.Asset;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Inventory;
import org.sayar.net.Model.newModel.OrganManagment.Organization;
import org.sayar.net.Model.newModel.Part.Part;
import org.sayar.net.Model.newModel.Storage;
import org.sayar.net.Service.UserTypeService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PartServiceImpl extends GeneralServiceImpl<Part> implements PartService {
    @Autowired
    private PartDao partDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AssetService assetService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private OrganService organService;

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public Part postPart(Part part) {
        return partDao.postPart(part);
    }

    @Override
    public Part getPrivateSideOfPart(String partId) {
        return partDao.getPrivateSideOfPart(partId);
    }

    @Override
    public List<Part> getAllPrivateSideOfPart() {
        return partDao.getAllPrivateSideOfPart();
    }

    @Override
    public List<PartGeneralDTO> getAllGeneralSideOfPart() {
        return PartGeneralDTO.map(partDao.getAllGeneralSideOfPart());
    }

    @Override
    public Part getOneGeneralSideOfPart(@PathParam("partId") String partId) {
        return partDao.getOneGeneralSideOfPart(partId);
    }

    @Override
    public boolean checkIfCodeExist(@PathParam("partCode") String partCode) {
        return partDao.checkIfCodeExists(partCode);
    }

    @Override
    public Part postPartInventory(PartDTO partDTO, String PartId) {
        return partDao.postPartInventory(partDTO, PartId);
    }

    @Override
    public boolean checkIfInventoryCodeExists(String inventoryCode) {
        return partDao.checkIfInventoryCodeExists(inventoryCode);
    }

    @Override
    public Page<Part> getAllPartsByFilterAndPagination(String term, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                partDao.getAllPartsByFilterAndPagination(term, pageable, totalElement)
                , pageable
                , partDao.getAllCount(term)
        );
    }

    @GetMapping
    public Part getOnePart(String partId) {
        return partDao.getOnePart(partId);
    }

    @Override
    public UpdateResult updateUpSide(PartPrivateDTO partPrivateDTO, String partId) {
        return partDao.updateUpSide(partPrivateDTO, partId);
    }

    @Override
    public UpdateResult updateDownSide(PartGeneralDTO partGeneralDTO, String partId) {
        return partDao.updateDownSide(partGeneralDTO, partId);
    }

    @Override
    public boolean updateUserListByPartId(List<String> user, String partId) {
        return super.updateResultStatus(partDao.updateUserListByPartId(user, partId));
    }

    @Override
    public List<PartUserDTO> getUsersListByPartId(String partId) {
        Part part = partDao.getUsersListByPartId(partId);
        Print.print("part", part);
        if (part.getUsersIdList() != null) {
            return userDao.getUsersOfPart(part.getUsersIdList());
        } else {
            return null;
        }
    }

    @Override
    public List<Part> getPartsByPartIdList(List<String> bomPartIdList) {
        return partDao.getPartsByPartIdList(bomPartIdList);
    }

    @Override
    public Page<PartGetAllDTO> getAllAssignedUsersOfPartByUserId(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                partDao.getAllAssignedUsersOfPartByUserId(partDtoForSearch, pageable, totalElement),
                pageable,
                partDao.numberOfUserParts(partDtoForSearch)
        );
    }

    @Override
    public List<Part> getAssignedPartListsOfUser(String userId) {
        return partDao.getAssignedPartListsOfUser(userId);
    }

    @Override
    public Page<PartGetAllDTO> getAllPartsWithOutInventoryAndLoadedInventories(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer element) {
        return new PageImpl<>(
                partDao.getAllPartsWithOutInventory(partDtoForSearch, pageable, element),
                pageable,
                partDao.numberOfParts(partDtoForSearch)
        );
    }

    @Override
    public boolean checkIfUserExistsInPart(String userId) {
        return partDao.checkIfUserExistsInPart(userId);
    }

    @Override
    public boolean ifCompanyExistsInPart(String companyId) {
        return partDao.ifCompanyExistsInPart(companyId);
    }

    @Override
    public boolean ifWareHouseExistsInPart(String storageId) {
        return partDao.ifWareHouseExistsInPart(storageId);
    }

    @Override
    public SimpleStorageAndPartsDTO getAllParts() {
        List<Part> partList = partDao.getAllParts();
        List<Inventory> inventoryList = inventoryService.getAllInventoriesWithStorage();
        List<Storage> storageList = new ArrayList<>();
        inventoryList.forEach(inventory -> {
//            if (inventory.getInventoryLocationId() != null) {
//                storageList.add(inventory.getInventoryLocationId());
//            }
        });
        Print.print("storageList", storageList);
        List<String> dependentStorageList = new ArrayList<>();
        storageList.forEach(storage -> {
            if (storage.getAssetId() != null)
                dependentStorageList.add(storage.getAssetId());
        });
        List<Asset> assetsOfStorage = assetService.getAllAssetsOfStorageList(dependentStorageList);

        Print.print("assetOfStorage", assetsOfStorage);
        List<String> parentAssetIdList = new ArrayList<>();
        assetsOfStorage.forEach(asset -> {
            if (asset.getIsPartOfAsset() != null) {
                parentAssetIdList.add(asset.getIsPartOfAsset());
            } else {
                parentAssetIdList.add(asset.getId());
            }
        });
        List<Asset> parentAssets = assetService.getParentAssets(parentAssetIdList);
        List<UniqueDTO> neededStorageField = UniqueDTO.map(storageList);
        Set<UniqueDTO> uniqueStorageList = new HashSet<>();
        uniqueStorageList.addAll(neededStorageField);
        return SimpleStorageAndPartsDTO.map(uniqueStorageList, partList, parentAssets);
    }

    @Override
    public void addOneToNumberOfInventoryOfPart(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partId));
        Update update = new Update();
        update.inc("numberOfInventory", 1);
        mongoOperations.updateFirst(query, update, Part.class);
    }

    @Override
    public void decreaseNumberOfInventoryInPart(String partId) {
        partDao.decreaseNumberOfInventoryInPart(partId);
    }

    @Override
    public Page<Part> getAllBOMPart(Pageable pageable) {
        return new PageImpl<>(
                partDao.getAllBOMPart(pageable),
                pageable,
                partDao.countBOMPart()
        );
    }

    @Override
    public Part getPartCode(String partId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(partId));
        query.fields()
                .include("partCode");
        return mongoOperations.findOne(query, Part.class);
    }

    @Override
    public List<PartPersonnelDTO> getPersonsOfPart(String partId) {
        Part part = partDao.getPersonsOfPart(partId);
        List<String> userIdList = new ArrayList<>();
//        List<String> orgIdList = new ArrayList<>();
        List<String> userTypeIdList = new ArrayList<>();
        if (part.getAssignedToPersonList() != null) {
            part.getAssignedToPersonList().forEach(assignedToPerson -> {
                        userIdList.add(assignedToPerson.getUserId());
//                        orgIdList.add(assignedToPerson.getOrgId());
                        userTypeIdList.add(assignedToPerson.getUserTypeId());
                    }
            );
            List<User> userList = userDao.getNameAndFamilyNameOfUsers(userIdList);
//            List<Organization> organizationList = organService.getOrganizationNameAndId(orgIdList);
            List<UserType> userTypeList = userTypeService.getUserTypeName(userTypeIdList);
            return PartPersonnelDTO.map(userList, userTypeList);
        } else
            return null;
    }

    @Override
    public List<PartGroupPersonnelDTO> getGroupPersonnelOfPart(String partId) {
        Part part = partDao.getGroupPersonnelOfPart(partId);
        List<String> orgIdList = new ArrayList<>();
        List<String> userTypeIdList = new ArrayList<>();
        if (part.getAssignedToGroupList() != null) {
            part.getAssignedToGroupList().forEach(assignedToGroup -> {
                orgIdList.add(assignedToGroup.getOrgId());
                userTypeIdList.add(assignedToGroup.getUserTypeId());
            });
            List<Organization> organizationList = organService.getOrganizationNameAndId(orgIdList);
            List<UserType> userTypeList = userTypeService.getUserTypeName(userTypeIdList);
            return PartGroupPersonnelDTO.map(part, organizationList, userTypeList);
        } else
            return null;
    }

    @Override
    public boolean addPersonTypePersonnel(String partId, List<AssignedToPerson> assignedToPersonList) {
        return partDao.addPersonTypePersonnel(partId, assignedToPersonList);
    }

    @Override
    public boolean addGroupTypePersonnel(String partId, List<AssignedToGroup> assignedToGroupList) {
        return partDao.addGroupTypePersonnel(partId, assignedToGroupList);
    }

    @Override
    public Page<PartGetAllDTO> getAllAssignedPartsOfGroup(PartDtoForSearch partDtoForSearch, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                partDao.getAllAssignedPartsOfGroup(partDtoForSearch, pageable, totalElement),
                pageable,
                partDao.countAllAssignedPartsOfGroup(partDtoForSearch)
        );
    }

    @Override
    public List<Part> getUsedPartInWorkOrder(List<String> partIdList) {
        return partDao.getUsedPartInWorkOrder(partIdList);
    }
    @Override
    public List<Part> getAllByIdList(List<String> partIdList) {
        return partDao.getUsedPartInWorkOrder(partIdList);
    }
}