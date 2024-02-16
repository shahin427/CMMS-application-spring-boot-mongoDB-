package org.sayar.net.Controller.newController;

import org.sayar.net.Model.Asset.AssetTemplate;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.Asset.Property;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Service.newService.AssetService;
import org.sayar.net.Service.newService.AssetTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/asset-template")
public class AssetTemplateController {

    @Autowired
    private AssetTemplateService assetTemplateService;

    @Autowired
    private AssetService assetService;

    @GetMapping("get-one")
    public ResponseEntity<?> getOneAssetTemplate(@PathParam("assetTemplateId") String assetTemplateId) {
        return ResponseEntity.ok().body(assetTemplateService.getOneAssetTemplate(assetTemplateId));
    }

    @PostMapping("save")
    public ResponseEntity<?> postOneAssetTemplate(@RequestBody AssetTemplate assetTemplate) {
        return ResponseEntity.ok().body(assetTemplateService.postOneAssetTemplate(assetTemplate));
    }

    @GetMapping("get-all-for-asset")
    public ResponseEntity<?> getAllAssetTemplate(@PathParam("categoryType") CategoryType categoryTypeValue) {
        return ResponseEntity.ok().body(assetTemplateService.getAllAssetTemplateOfAsset(categoryTypeValue));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAssetTemplate(@PathParam("assetTemplateId") String assetTemplateId) {
        if (assetService.ifAssetTemplateExistsInAsset(assetTemplateId)) {
            return ResponseEntity.ok().body("\"برای حذف این قالب دارایی ابتدا آن را از قسمت درایی ها حذف نمایید\"");
        } else {
            return ResponseEntity.ok().body(assetTemplateService.logicDeleteById(assetTemplateId, AssetTemplate.class));
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> updateAssetTemplate(@RequestBody AssetTemplate assetTemplate) {
        return ResponseEntity.ok().body(assetTemplateService.updateAssetTemplate(assetTemplate));
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllFilter(@PathParam("term") String term,
                                          @PathParam("parentCategory") String parentCategoryId,
                                          @PathParam("subCategory") String subCategoryId,
                                          Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(assetTemplateService.getAllFilter(term, parentCategoryId, subCategoryId, pageable, totalElement));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllAssetTemplatesName() {
        return ResponseEntity.ok().body(assetTemplateService.getAllAssetTemplateName());
    }

    @GetMapping("get-category-type-by-asset-template-id")
    public ResponseEntity<?> getCategoryTypeByAssetId(@PathParam("assetTemplateId") String assetTemplateId) {
        return ResponseEntity.ok().body(assetTemplateService.getCategoryTypeByAssetId(assetTemplateId));
    }

    @GetMapping("check-if-asset-template-name-is-unique")
    public ResponseEntity<?> checkIfAssetTemplateNameIsUnique(@PathParam("name") String name) {
        return ResponseEntity.ok().body(assetTemplateService.checkIfAssetTemplateNameIsUnique(name));
    }

    @GetMapping("get-all-asset-templates-with-pagination")
    public ResponseEntity<?> getAllAssetTemplatesWithPagination(@PathParam("term") String term, @PathParam("parentCategory") String parentCategory,
                                                                @PathParam("subCategory") String subCategory, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(assetTemplateService.getAllAssetTemplatesWithPagination(term, parentCategory, subCategory, pageable, totalElement));
    }

    @PutMapping("add-person-type-personnel-of-asset-template")
    public ResponseEntity<?> addPersonTypePersonnel(@PathParam("assetTemplateId") String assetTemplateId, @RequestBody List<AssignedToPerson> assignedToPersonList) {
        return ResponseEntity.ok().body(assetTemplateService.addPersonTypePersonnelOfAssetTemplate(assetTemplateId, assignedToPersonList));
    }

    @GetMapping("get-personnel-of-asset-template")
    public ResponseEntity<?> getPersonnelOfAssetTemplate(@PathParam("assetTemplateId") String assetTemplateId) {
        return ResponseEntity.ok().body(assetTemplateService.getPersonnelOfAssetTemplate(assetTemplateId));
    }

    @PutMapping("add-group-personnel-to-asset-template")
    public ResponseEntity<?> addGroupPersonnelToAssetTemplate(@PathParam("assetTemplateId") String assetTemplateId, @RequestBody List<AssignedToGroup> assignedToGroupList) {
        return ResponseEntity.ok().body(assetTemplateService.addGroupPersonnelToAssetTemplate(assetTemplateId, assignedToGroupList));
    }

    @GetMapping("get-group-personnel-of-project")
    public ResponseEntity<?> getGroupPersonnelOfAssetTemplate(@PathParam("assetTemplateId") String assetTemplateId) {
        return ResponseEntity.ok().body(assetTemplateService.getGroupPersonnelOfAssetTemplate(assetTemplateId));
    }

    @GetMapping("get-property-of-asset-template")
    public ResponseEntity<?> getPropertyOfAssetTemplate(@PathParam("assetTemplateId") String assetTemplateId) {
        return ResponseEntity.ok().body(assetTemplateService.getPropertyOfAssetTemplate(assetTemplateId));
    }


    @PutMapping("update-asset-template-properties")
    public ResponseEntity<?> updateAssetTemplateProperties(@PathParam("assetTemplateId") String assetTemplateId, @RequestBody List<Property> properties) {
        return ResponseEntity.ok().body(assetTemplateService.updateAssetTemplateProperties(assetTemplateId, properties));
    }

    @GetMapping("check-if-asset-template-used-in-asset")
    public ResponseEntity<?> checkIfAssetTemplateUsedInAsset(@PathParam("assetTemplateId") String assetTemplateId) {
        if (assetService.checkIfAssetTemplateUsedInAsset(assetTemplateId)) {
            return ResponseEntity.ok().body("\"برای ویرایش این قالب دارایی ابتدا آن را از قسمت درایی ها حذف نمایید\"");
        } else {
            return ResponseEntity.ok().body("\"مجاز به ویرایش میباشد \"");
        }
    }
}

