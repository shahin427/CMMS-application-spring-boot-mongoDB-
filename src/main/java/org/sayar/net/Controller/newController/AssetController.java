package org.sayar.net.Controller.newController;

import org.sayar.net.Model.Asset.*;
import org.sayar.net.Model.DTO.AssetSearchDTO;
import org.sayar.net.Model.DTO.CreateAsset;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintananceService;
import org.sayar.net.Security.TokenPrinciple;
import org.sayar.net.Service.WorkRequestService;
import org.sayar.net.Service.newService.AssetService;
import org.sayar.net.Service.newService.BOMService;
import org.sayar.net.Service.newService.StorageService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.Document;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/asset")
public class AssetController {

    @Autowired
    private TokenPrinciple tokenPrinciple;

    @Autowired
    private AssetService assetService;

    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private ScheduleMaintananceService scheduleMaintananceService;

    @Autowired
    private WorkRequestService workRequestService;

    @Autowired
    private BOMService bomService;

    @Autowired
    private StorageService storageService;


    @PostMapping("get-all-by-filter-and-pagination")
    public ResponseEntity<?> getAllByFilterAndPagination(@RequestBody AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(assetService.getAllByFilterAndPagination(assetDTO, pageable, totalElement));
    }

    @PostMapping("save")
    public ResponseEntity<?> postAsset(@RequestBody Asset asset) {
        return ResponseEntity.ok().body(assetService.postAsset(asset));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOneAsset(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getOneAsset(assetId));
    }
    @GetMapping("get-list-activity-by-assetId")
    public ResponseEntity<?> getListActivityListByAssetId(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getListActivityListByAssetId(assetId));
    }

    @GetMapping("check-asset-code")
    public ResponseEntity<?> checkIfCodeExists(@PathParam("assetCode") String assetCode) {
        return ResponseEntity.ok().body("{\"exist\":" + assetService.checkIfCodeExists(assetCode) + "}");
    }

    @PutMapping("update")
    public ResponseEntity<?> updateAsset(@RequestBody Asset asset) {
        return ResponseEntity.ok().body(assetService.updateAsset(asset));
    }

    @PutMapping("update/basic-information")
    public ResponseEntity<?> updateAssetBasicInformation(@RequestParam("assetId") String assetId, @RequestBody AssetBasicInformation assetBasicInformation) {
        return ResponseEntity.ok().body(assetService.updateAssetBasicInformation(assetId, assetBasicInformation));
    }

    @GetMapping("get-basic-information-by-asset-id")
    public ResponseEntity<?> getOneByAssetId(@RequestParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getOneByAssetId(assetId));
    }

    @DeleteMapping("delete-create-asset")
    public ResponseEntity<?> deleteCreateAsset(@PathParam("assetId") String assetId) {
        if (workOrderService.ifAssetExistsInWorkOrder(assetId)) {
            return ResponseEntity.ok().body("\"برای خذف این دارایی ابتدا آن را از قسمت دستور کارها حذف نمایید\"");
        } else if (scheduleMaintananceService.ifAssetExistsInScheduleMaintenance(assetId)) {
            return ResponseEntity.ok().body("\"برای خذف این دارایی ابتدا آن را از قسمت زمانبندی نت حذف نمایید\"");
        } else if (workRequestService.ifAssetExistsInWorkRequest(assetId)) {
            return ResponseEntity.ok().body("\"برای خذف این دارایی ابتدا آن را از قسمت درخواست کار حذف نمایید\"");
        } else if (bomService.ifAssetExistsInBOM(assetId)) {
            return ResponseEntity.ok().body("\"برای خذف این دارایی ابتدا آن را از قسمت ساختار فنی ماشین آلات و تجهیزات حذف نمایید\"");
        } else if (storageService.ifAssetExistsInStorage(assetId)) {
            return ResponseEntity.ok().body("\"برای خذف این دارایی ابتدا آن را از قسمت انبار حذف نمایید\"");
        } else {
            return ResponseEntity.ok().body(assetService.logicDeleteById(assetId, Asset.class));
        }
    }

    @PutMapping("asset-update-createAsset")
    public ResponseEntity<?> updateCreateAsset(@RequestBody CreateAsset createAsset) {
        return ResponseEntity.ok().body(assetService.updateCreateAsset(createAsset));
    }

    @GetMapping("get-company-list-by-asset-id")
    public ResponseEntity<?> getAllCompanyByAssetId(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getAllCompanyByAssetId(assetId));
    }

    @GetMapping("get-user-list-by-asset-id")
    public ResponseEntity<?> getAllUsersByAssetId(@PathParam("assetId") String assetId) {
        System.out.println(assetId);
        return ResponseEntity.ok().body(assetService.getAllUsersByAssetId(assetId));
    }

    @GetMapping("get-asset-template-property-list-by-asset-id")
    public ResponseEntity<?> getAllPropertyByAssetId(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getAllPropertyByAssetId(assetId));
    }

    @GetMapping("get-warranty-by-asset-id")
    public ResponseEntity<?> getAllWarrantyByAssetId(String assetId) {
        return ResponseEntity.ok().body(assetService.getAllWarrantyByAssetId(assetId));
    }

    @GetMapping("get-all-metering-by-asset-id")
    public ResponseEntity<?> getAllMeteringByAssetId(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getAllMeteringByAssetId(assetId));
    }

    @GetMapping("get-part-list-by-asset-id")
    public ResponseEntity<?> getAllPartByAssetId(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getAllPartByAssetId(assetId));
    }

    @PutMapping("update-part-list-by-asset-id")
    public ResponseEntity<?> updatePartListIdByAssetId(@PathParam("assetId") String assetId, @RequestBody List<String> partsId) {
        return ResponseEntity.ok().body(assetService.updatePartListIdByAssetId(assetId, partsId));
    }

    @GetMapping("get-property-list-by-asset-id")
    public ResponseEntity<?> getPropertyListByAssetId(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getPropertyListByAssetId(assetId));
    }

    @GetMapping("get-document-list-by-asset-id")
    public ResponseEntity<?> getDocumentListByAssetId(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getDocumentListByAssetId(assetId));
    }

    @PutMapping("update-company-list-by-asset-id")
    public ResponseEntity<?> updateCompanyListByAssetId(@PathParam("assetId") String assetId, @RequestBody List<String> companyId) {
        return ResponseEntity.ok().body(assetService.updateCompanyListByAssetId(assetId, companyId));
    }

    @PutMapping("update-property-list-by-asset-id")
    public ResponseEntity<?> updatePropertyListByAssetId(@PathParam("assetId") String assetId, @RequestBody List<Property> propertyList) {
        return ResponseEntity.ok().body(assetService.updatePropertyListByAssetId(assetId, propertyList));
    }

    @PutMapping("update-user-list-by-asset-id")
    public ResponseEntity<?> updateUserListByAssetId(@PathParam("assetId") String assetId, @RequestBody List<String> usersId) {
        return ResponseEntity.ok().body(assetService.updateUserListByAssetId(assetId, usersId));
    }

    @PutMapping("update-warranty-list-by-asset-id")
    public ResponseEntity<?> updateWarrantyListByAssetId(@PathParam("assetId") String assetId, @RequestBody List<String> warrantyId) {
        return ResponseEntity.ok().body(assetService.updateWarrantyListByAssetId(assetId, warrantyId));
    }

    @PutMapping("update-metering-list-by-asset-id")
    public ResponseEntity<?> updateMeteringListByAssetId(@PathParam("assetId") String assetId, @RequestBody List<String> meteringId) {
        return ResponseEntity.ok().body(assetService.updateMeteringListByAssetId(assetId, meteringId));
    }

    @PutMapping("update-document-list-by-asset-id")
    public ResponseEntity<?> updateDocumentListByAssetId(@PathParam("assetId") String assetId, @RequestBody List<Document> documents) {
        return ResponseEntity.ok().body(assetService.updateDocumentListByAssetId(assetId, documents));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllAsset() {
        return ResponseEntity.ok().body(assetService.getAllNameAndCode());
    }

    @GetMapping("get-asset-list-by-category-type")
    public ResponseEntity<?> getAllAssetByCategoryType(@RequestParam CategoryType categoryType) {
        return ResponseEntity.ok().body(assetService.getAllAssetByCategoryType(categoryType));
    }

    @PostMapping("get-all-assigned-assets-of-user-by-user-id")
    public ResponseEntity<?> getAllAssetOfAssignedToUserByUserId(@RequestBody AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(assetService.getAllAssetOfAssignedToUserByUserId(assetDTO, pageable, totalElement));
    }

    @GetMapping("get-all-independent-assets")
    public ResponseEntity<?> getAllIndependentAssets(Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(assetService.getAllIndependentAssets(pageable, totalElement));
    }

    @GetMapping("get-all-children-of-asset")
    public ResponseEntity<?> getAllChildrenOfAUser(@PathParam("parentId") String parentId) {
        return ResponseEntity.ok().body(assetService.getAllChildrenOfAUser(parentId));
    }

    @GetMapping("get-assets-by-category-type")
    public ResponseEntity<?> getAssetsByCategoryType(@RequestParam("categoryType") String categoryType) {
        return ResponseEntity.ok().body(assetService.getAssetsByCategoryType(categoryType));
    }

    @GetMapping("get-buildings-and-its-children")
    public ResponseEntity<?> getBuildingsAndItsChildren() {
        return null;
    }

    @GetMapping("get-one-asset-with-parent-info")
    public ResponseEntity<?> getOneAssetWithParentInfo(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getOneAssetWithParentInfo(assetId));
    }

    @DeleteMapping("delete-child-and-parent-assets")
    public ResponseEntity<?> deleteChildAssets(@PathParam("assetId") String assetId) {
        if (assetService.deleteChildAssets(assetId)) {
            return ResponseEntity.ok().body("\"برای حذف این دارایی ابتدا باید دارایی های زیرمجموعه آن را حذف کنید\"");
        } else {
            Boolean logicDelete = assetService.logicDeleteById(assetId, Asset.class);
            Asset asset = assetService.getParent(assetId);
            boolean isAssetStillParent = assetService.isAssetStillParent(asset.getIsPartOfAsset());
            if (!isAssetStillParent)
                assetService.updateHasChild(asset.getIsPartOfAsset());
            return ResponseEntity.ok().body(logicDelete);
        }
    }

    @GetMapping("get-parents-of-asset")
    public ResponseEntity<?> getParentsOfAsset(@RequestParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getParentsOfAsset(assetId));
    }

    @GetMapping("get-unit-list-of-asset")
    public ResponseEntity<?> getUnitListOfAsset(@RequestParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getUnitListOfAsset(assetId));
    }

    @GetMapping("get-all-roots-of-building-and-facility")
    public ResponseEntity<?> newGetAllRootFacility() {
        return ResponseEntity.ok().body(assetService.newGetAllRootFacility());
    }
    @GetMapping("get-all-roots")
    public ResponseEntity<?> getAllRootAsset() {
        return ResponseEntity.ok().body(assetService.getAllRooAsset());
    }

    @GetMapping("get-real-user-online-asset")
    public ResponseEntity<?> getUserOnlineAsset(@PathParam("assignedUserId") String userAssignedId) {
        return ResponseEntity.ok().body(assetService.getUserOnlineAsset(userAssignedId));
    }

    @GetMapping("count-real-user-online-asset")
    public ResponseEntity<?> countUserOnlineAsset(@PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(assetService.countUserOnlineAsset(userAssignedId));
    }

    @GetMapping("get-real-offline-asset")
    public ResponseEntity<?> getUserOfflineAsset(@PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(assetService.getUserOfflineAsset(userAssignedId));
    }

    @GetMapping("count-real-offline-asset")
    public ResponseEntity<?> countUserOfflineAsset(@PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(assetService.countUserOfflineAsset(userAssignedId));
    }

    @GetMapping("get-all-asset-by-term")
    public ResponseEntity<?> getAllAssetByTerm(@PathParam("term") String term, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(assetService.getAllAssetByTerm(term, pageable, totalElement));
    }

    @PostMapping("get-all-assigned-assets-of-group")
    public ResponseEntity<?> getAllAssignedAssetsOfGroup(@RequestBody AssetSearchDTO assetDTO, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(assetService.getAllAssignedAssetsOfGroup(assetDTO, pageable, totalElement));
    }

    //    @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
    //    @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
    //    @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2

    @RequestMapping(method = RequestMethod.GET, value = "all/treenode/tree")
    public ResponseEntity<?> findAllInTreeNode() {
        return new ResponseContent().sendOkResponseEntity("", assetService.findAllInTreeNode());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all/compress")
    public ResponseEntity<?> getCompressAllAsset() {
        return new ResponseContent().sendOkResponseEntity("", assetService.getCompressAllAsset());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/purchaseAssetDTO")
    public ResponseEntity<?> getAssetDTOForPurchase() {
        return new ResponseContent().sendOkResponseEntity("", assetService.getAssetDTOForPurchase());
    }
    @RequestMapping(method = RequestMethod.GET, value = "/getAll-facility")
    public ResponseEntity<?> getAllFacility() {
        return new ResponseContent().sendOkResponseEntity("", assetService.getAllFacility());
    }

    @RequestMapping(method = RequestMethod.GET, value = "category/{catId}")
    public ResponseEntity<?> getAssetByCategory(@PathVariable String catId) {
        return new ResponseContent().sendOkResponseEntity("", assetService.getAssetByCategory(catId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "all/treenode")
    public ResponseEntity<?> getTreeNodeBase(@RequestParam(required = false, defaultValue = "") String searchTerm) {
        if (searchTerm.isEmpty())
            return new ResponseContent().sendOkResponseEntity("", assetService.getTreeNodeBase());
        else
            return new ResponseContent().sendOkResponseEntity("", assetService.getTreeNodeSearch(searchTerm));
    }

    @RequestMapping(method = RequestMethod.GET, value = "all/treenode/search/{searchTerm}")
    public ResponseEntity<?> getTreeNodeBaseSearch(@PathVariable String searchTerm) {
        return new ResponseContent().sendOkResponseEntity("", assetService.getTreeNodeSearch(searchTerm));
    }


    @RequestMapping(method = RequestMethod.GET, value = "{assetId}/treenode")
    public ResponseEntity<?> getTreeNodeLayer(@PathVariable String assetId) {
        return new ResponseContent().sendOkResponseEntity("", assetService.getTreeNodeLayer(assetId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "all/treenode/{catId}")
    public ResponseEntity<?> getTreeNodeBaseByCategory(@PathVariable String catId,
                                                       @RequestParam(required = false, defaultValue = "") String searchTerm) {
        if (searchTerm.isEmpty()) {
            return new ResponseContent().sendOkResponseEntity("", assetService.getTreeNodeBaseByCategory(catId));
        } else {
            return new ResponseContent().sendOkResponseEntity("", assetService.getTreeNodeSearchByCategory(searchTerm, catId));
        }
    }


    @RequestMapping(method = RequestMethod.GET, value = "all/treenode/{catId}/search/{searchTerm}")
    public ResponseEntity<?> getTreeNodeBaseByCategorySearch(@PathVariable String searchTerm, @PathVariable String catId) {
        return new ResponseContent().sendOkResponseEntity("", assetService.getTreeNodeSearchByCategory(searchTerm, catId));
    }


    @RequestMapping(method = RequestMethod.GET, value = "{assetId}/treenode/{catId}")
    public ResponseEntity<?> getTreeNodeLayerByCategory(@PathVariable String catId, @PathVariable String assetId) {
        return new ResponseContent().sendOkResponseEntity("", assetService.getTreeNodeLayerByCategory(assetId, catId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "treenode/children/{assetId}")
    public ResponseEntity<?> getAAssetsChildren(@PathVariable String assetId) {
        return new ResponseContent().sendOkResponseEntity("", assetService.getAssetChildrenInTreeNode(assetId));
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/location")
//    public ResponseEntity<?> getAssetLocation() {
//        return new ResponseContent().sendOkResponseEntity(
//                "",
//                assetService.getAssetForLocationCategory(
//                        tokenPrinciple.getOrganCode()
//                )
//        );
//    }

//    @RequestMapping(method = RequestMethod.GET, value = "/tools/count")
//    public ResponseEntity<?> getAssetToolsCount() {
//        return new ResponseContent().sendOkResponseEntity(
//                "",
//                assetService.getAssetForToolsCategoryCount(
//                        tokenPrinciple.getOrganCode()
//                )
//        );
//    }

//    @RequestMapping(method = RequestMethod.GET, value = "/equipment/count")
//    public ResponseEntity<?> getEquipmentCount() {
//        return new ResponseContent().sendOkResponseEntity(
//                "",
//                assetService.getAssetForEquipmentCategoryCount(
//                        tokenPrinciple.getOrganCode()
//                )
//        );
//    }

//    @RequestMapping(method = RequestMethod.GET, value = "/location/count")
//    public ResponseEntity<?> getLocationCount() {
//        return new ResponseContent().sendOkResponseEntity(
//                "",
//                assetService.getAssetForLocationCategoryCount(
//                        tokenPrinciple.getOrganCode()
//                )
//        );
//    }

//
//    @RequestMapping(method = RequestMethod.GET, value = "/mtbf/{assetId:" + PatternList.digitNumber + "}")
//    public ResponseEntity<?> calculateMTBF(@PathVariable String assetId) {
//        return new ResponseContent().sendOkResponseEntity(
//                "",
//                assetService.calculateMTBF(assetId)
//        );
//    }
//
//    @RequestMapping(method = RequestMethod.GET, value = "/mttr/{assetId:" + PatternList.digitNumber + "}")
//    public ResponseEntity<?> calculateMTTR(@PathVariable String assetId) {
//        return new ResponseContent().sendOkResponseEntity(
//                "",
//                assetService.calculateMTTR(assetId)
//        );
//    }

//    @RequestMapping(method = RequestMethod.POST, value = "save1")
//    public ResponseEntity<?> save(@RequestBody Asset asset) {
//
//        try {
//            String organId = tokenPrinciple.getOrganCode();
//            System.out.println(organId);
//            assetService.save(asset);
//            return new ResponseContent().sendOkResponseEntity("", asset);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseContent().sendErrorResponseEntity("error", ResponseKeyWord.FAILURE, 500);
//        }
//    }
//
//    @RequestMapping(method = RequestMethod.POST, value = "update")
//    public ResponseEntity<?> update(@RequestBody Asset asset) {
//        return new ResponseContent().sendOkResponseEntity("", assetService.update(asset));
//    }
//
//    @RequestMapping(method = RequestMethod.GET, value = "{assetId}")
//    public ResponseEntity<?> findOne(@PathVariable String assetId) {
//        return new ResponseContent().sendOkResponseEntity("", assetService.findOneById(assetId, Asset.class));
//    }
//
//    @RequestMapping(method = RequestMethod.DELETE, value = "{assetId}")
//    public ResponseEntity<?> delete(@PathVariable String assetId) {
//        return new ResponseContent().sendOkResponseEntity("", assetService.logicDeleteById(assetId, Asset.class));
//    }
//
//
//    @PostMapping("by-id-list")
//    public ResponseEntity<?> getByIdList(@RequestBody List<String> idList) {
//        return new ResponseContent().sendOkResponseEntity("", assetService.getByIdList(idList));
//    }
//
//    @PostMapping("add-meter")
//    public ResponseEntity<?> addMeter(@RequestParam("assetId") String assetId, @RequestBody Metering metering) {
//        return new ResponseContent().sendOkResponseEntity("", assetService.addMeter(assetId, metering));
//    }
//
//    @GetMapping("get-one1")
//    public ResponseEntity<?> getOne(@RequestParam("id") String id) {
//        return new ResponseContent().sendOkResponseEntity("", assetService.getOne(id));
//    }

    @GetMapping("get-all-assets-of-user")
    public ResponseEntity<?> getAllAssetsOfUser(@PathParam("userId") String userId) {
        if (userId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی فرستاده نمیشود", httpStatus);
        } else {
            return ResponseEntity.ok().body(assetService.getAllAssetsOfUser(userId));
        }
    }

    @GetMapping("get-general-online-assets")
    public ResponseEntity<?> getGeneralOnlineAssets() {
        return ResponseEntity.ok().body(assetService.getOnlineAssets());
    }

    @GetMapping("count-general-online-assets")
    public ResponseEntity<?> countGeneralOnlineAssets() {
        return ResponseEntity.ok().body(assetService.countAllOnlineAssets());
    }

    @GetMapping("get-general-offline-assets")
    public ResponseEntity<?> getGeneralOfflineAssets() {
        return ResponseEntity.ok().body(assetService.getOfflineAssets());
    }

    @GetMapping("count-general-offline-assets")
    public ResponseEntity<?> countGeneralOfflineAssets() {
        return ResponseEntity.ok().body(assetService.countAllOfflineAssets());
    }

    @GetMapping("get-person-personnel-of-asset")
    public ResponseEntity<?> getPersonsOfAsset(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getPersonsOfAsset(assetId));
    }

    @GetMapping("get-group-personnel-of-asset")
    public ResponseEntity<?> getGroupPersonnelOfAsset(@PathParam("assetId") String assetId) {
        return ResponseEntity.ok().body(assetService.getGroupPersonnelOfAsset(assetId));
    }

    @PutMapping("add-person-type-personnel")
    public ResponseEntity<?> addPersonTypePersonnel(@PathParam("assetId") String assetId, @RequestBody List<AssignedToPerson> assignedToPersonList) {
        return ResponseEntity.ok().body(assetService.addPersonTypePersonnel(assetId, assignedToPersonList));
    }

    @PutMapping("add-group-type-personnel")
    public ResponseEntity<?> addGroupTypePersonnel(@PathParam("assetId") String assetId, @RequestBody List<AssignedToGroup> assignedToGroupList) {
        return ResponseEntity.ok().body(assetService.addGroupTypePersonnel(assetId, assignedToGroupList));
    }


}

