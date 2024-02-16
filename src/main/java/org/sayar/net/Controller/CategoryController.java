package org.sayar.net.Controller;

import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.Enum.NewCategory;
import org.sayar.net.Service.CategoryService;
import org.sayar.net.Service.newService.AssetTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @Autowired
    private AssetTemplateService assetTemplateService;

    @GetMapping("get-one")
    public ResponseEntity<?> findOne(@PathParam("categoryId") String categoryId) {
        return ResponseEntity.ok().body(service.getOneCategory(categoryId));
    }

    @PostMapping("save")
    public ResponseEntity<?> save(@RequestBody Category category) {
        return ResponseEntity.ok().body(service.postCategory(category));
    }

    @PutMapping("update")
    public ResponseEntity<?> update(@RequestBody Category category) {
        return ResponseEntity.ok().body(service.updateCategory(category));
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@PathParam("categoryId") String categoryId) {
        if (assetTemplateService.ifCategoryExistsInAssetTemplate(categoryId)) {
            return ResponseEntity.ok().body("\"این دسته بندی دارایی در قالب دارایی ها استفاده شده است. برای حذف، ابتدا آن را از قالب دارایی ها حذف نمایید\"");
        } else if (service.checkIfCategoryIsParentCategory(categoryId)) {
            return ResponseEntity.ok().body("\"این دسته بندی دارایی والد میباشد برای حذف، ابتدا دسته پایین دستی را پاک کنید\"");
        } else {
            return ResponseEntity.ok().body(service.logicDeleteById(categoryId, Category.class));
        }
    }

    @GetMapping("get-all-by-pagination")
    public ResponseEntity<?> getAllListPageable(@PathParam("term") String term,
                                                @PathParam("parentId") String parentId,
                                                @PathParam("categoryType") CategoryType categoryType,
                                                Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllListPageable(term, parentId, categoryType, pageable, totalElement));
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllIdAndTitle() {
        return ResponseEntity.ok().body(service.getAllIdAndTitle());
    }

    @GetMapping("get-children-by-parent-id")
    public ResponseEntity<?> getChildrenByParentId(@PathParam("parentId") String parentId) {
        return ResponseEntity.ok().body(service.getChildrenByParentId(parentId));
    }

    @GetMapping("get-all-enum")
    public ResponseEntity<?> getAllEnumType(@PathParam("categoryType") CategoryType categoryType) {
        return ResponseEntity.ok().body(service.getAllEnumType(categoryType));
    }

    @GetMapping("check-if-title-is-unique")
    public ResponseEntity<?> checkIfTitleIsUnique(@PathParam("title") String title) {
        return ResponseEntity.ok().body(service.checkIfTitleIsUnique(title));
    }

    @GetMapping("get-property-of-category")
    public ResponseEntity<?> getPropertyOfCategory(@PathParam("categoryId") String categoryId) {
        return ResponseEntity.ok().body(service.getPropertyOfCategory(categoryId));
    }

    @GetMapping("check-if-category-has-sub-category-or-asset-template")
    public ResponseEntity<?> checkIfCategoryHasSubCategoryOrAssetTemplate(@PathParam("categoryId") String categoryId) {
        if (service.checkIfCategoryHasSubCategory(categoryId)) {
            return ResponseEntity.ok().body("\"برای ویرایش دسته بندی مورد نظر باید این دسته، والد دسته دیگری نباشد \"");
        } else if (service.checkIfCategoryIsAssetTemplateParent(categoryId)) {
            return ResponseEntity.ok().body("\"دسته بندی مورد نظر در قالب دارایی استفاده گردیده و امکان ویرایش آن وجود ندارد \"");
        } else {
            return ResponseEntity.ok().body("\"مجاز به ویرایش میباشد \"");
        }
    }

    @PostMapping("new-save")
    public ResponseEntity<?> newSaveCategory(@RequestBody Category category) {
        return ResponseEntity.ok().body(service.newSaveCategory(category));
    }

    @GetMapping("new-get-one")
    public ResponseEntity<?> newGetOneCategory(@PathParam("categoryId") String categoryId) {
        return ResponseEntity.ok().body(service.newGetOneCategory(categoryId));
    }

    @PostMapping("get-all-new-category-with-pagination")
    public ResponseEntity<?> getAllNewCategoryWithPagination(@RequestBody NewCategory newCategory, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(service.getAllNewCategoryWithPagination(newCategory, pageable, totalElement));
    }

    @PutMapping("new-update")
    public ResponseEntity<?> newUpdateCategory(@RequestBody Category category) {
        return ResponseEntity.ok().body(service.newUpdateCategory(category));
    }

    @DeleteMapping("new-delete")
    public ResponseEntity<?> newDeleteCategory(@PathParam("categoryId") String categoryId) {
        return ResponseEntity.ok().body(service.logicDeleteById(categoryId, Category.class));
    }

    @GetMapping("check-if-category-used-in-asset")
    public ResponseEntity<?> checkIfCategoryUsedInAsset(@PathParam("categoryId") String categoryId) {
        return ResponseEntity.ok().body(service.checkIfCategoryUsedInAsset(categoryId));
    }

    @GetMapping("new-category-get-all")
    public ResponseEntity<?> NewGetAllCategory() {
        return ResponseEntity.ok().body(service.NewGetAllCategory());
    }
}
