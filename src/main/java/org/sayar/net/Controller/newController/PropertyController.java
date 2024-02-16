package org.sayar.net.Controller.newController;


import org.sayar.net.Model.Asset.Property;
import org.sayar.net.Model.ResponseModel.ResponseContent;
import org.sayar.net.Service.CategoryService;
import org.sayar.net.Service.newService.AssetService;
import org.sayar.net.Service.newService.AssetTemplateService;
import org.sayar.net.Service.newService.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("property")
public class PropertyController {

    @Autowired
    private PropertyService service;
    @Autowired
    private AssetTemplateService assetTemplateService;
    @Autowired
    private AssetService assetService;
    @Autowired
    private CategoryService categoryService;


    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody Property property) {
        return new ResponseContent().sendOkResponseEntity("", service.saveProperty(property));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateProperty(@RequestBody Property property) {
        return ResponseEntity.ok().body(service.updateProperty(property));
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllByPagination(String term, String propertyCategoryId, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllByPagination(term, propertyCategoryId, pageable, totalElement));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOne(@PathParam("propertyId") String propertyId) {

        if (categoryService.ifPropertyExistsInCategory(propertyId)) {
            return ResponseEntity.ok().body("\"این مشخصه در قسمت دسته بندی دارایی ها استفاده گردیده و امکان ویرایش آن وجود ندارد برای ویرایش ابتدا آن را از قسمت دسته بندی دارایی ها حذف نمایید\"");
        } else if (assetTemplateService.ifPropertyExistsInAssetTemplate(propertyId)) {
            return ResponseEntity.ok().body("\"این مشخصه در قسمت قالب دارایی ها استفاده گردیده و امکان ویرایش آن وجود ندارد برای ویرایش ابتدا آن را از قسمت قالب دارایی ها حذف نمایید\"");
        } else if (assetService.ifPropertyExistsInAsset(propertyId)) {
            return ResponseEntity.ok().body("\"این مشخصه در قسمت دارایی ها استفاده گردیده و امکان ویرایش آن وجود ندارد برای ویرایش ابتدا آن را از قسمت دارایی ها حذف نمایید\"");
        } else {
            return new ResponseContent().sendOkResponseEntity("", service.findOneById(propertyId, Property.class));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteOne(@PathParam("propertyId") String propertyId) {
        if (assetService.ifPropertyExistsInAsset(propertyId)) {
            return ResponseEntity.ok().body("\"برای حذف این مشخصه ابتدا آن را از قسمت دارایی ها پاک کنید\"");
        } else if (assetTemplateService.ifPropertyExistsInAssetTemplate(propertyId)) {
            return ResponseEntity.ok().body("\"برای حذف این مشخصه ابتدا آن را از قسمت قالب دارایی ها پاک کنید\"");
        } else if (categoryService.ifPropertyExistsInCategory(propertyId)) {
            return ResponseEntity.ok().body("\"برای حذف این مشخصه ابتدا آن را از قسمت دسته بندی دارایی ها پاک کنید\"");
        } else {
            return new ResponseContent().sendOkResponseEntity("", service.logicDeleteById(propertyId, Property.class));
        }
    }

    @GetMapping("property-key-check")
    public ResponseEntity<?> propertyKeyCheck(@PathParam("key") String key) {
        return ResponseEntity.ok().body(service.propertyKeyCheck(key));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllProperty() {
        return ResponseEntity.ok().body(service.getAllProperty());
    }

    @GetMapping("check-property-key")
    public ResponseEntity<?> CheckIfPropertyKeyExist(@PathParam("propertyKey") String propertyKey) {
        return ResponseEntity.ok().body("{\"exist\":" + service.CheckIfPropertyKeyExist(propertyKey) + "}");
    }

    @GetMapping("get-property-by-property-category-id")
    public ResponseEntity<?> getPropertyByPropertyCategoryId(@PathParam("propertyCategoryId") String propertyCategoryId) {
        return ResponseEntity.ok().body(service.getPropertyByPropertyCategoryId(propertyCategoryId));
    }

    @GetMapping("get-one-view")
    public ResponseEntity<?> getOneView(@PathParam("propertyId") String propertyId) {
        return ResponseEntity.ok().body(service.getOneView(propertyId));
    }
}
