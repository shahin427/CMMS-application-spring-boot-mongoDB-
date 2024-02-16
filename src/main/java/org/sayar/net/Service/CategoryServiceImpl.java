package org.sayar.net.Service;

import org.sayar.net.Dao.category.CategoryDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.CategoryDTO;
import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.Enum.NewCategory;
import org.sayar.net.Service.newService.AssetService;
import org.sayar.net.Service.newService.AssetTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("categoryServiceImpl")
public class CategoryServiceImpl extends GeneralServiceImpl<Category> implements CategoryService {

    @Autowired
    private CategoryDao dao;

    @Autowired
    private AssetTemplateService assetTemplateService;

    @Autowired
    private AssetService assetService;

    @Override
    public Category add(Category category) {
        return null;
    }

    @Override
    public Category add(Category category, String organId) {
        return null;
    }

    @Override
    public Category updateCategory(Category category) {
        return dao.updateCategory(category);
    }

    @Override
    public Category postCategory(Category category) {
        if (!category.getParentId().equals("ROOT")) {
            Category parentCategory = dao.getCategoryType(category.getParentId());
            category.setCategoryType(parentCategory.getCategoryType());
        }
        return dao.postCategory(category);
    }

    @Override
    public Page<CategoryDTO> getAllListPageable(String term, String parentId, CategoryType categoryType, Pageable pageable, Integer totalElement) {
        List<Category> categoryList = dao.getAllListPageable(term, parentId, categoryType, pageable, totalElement);
        List<String> parentIdList = new ArrayList<>();
        categoryList.forEach(category -> {
            if (!category.getParentId().equals("ROOT"))
                parentIdList.add(category.getParentId());
        });
        List<Category> parentCategoryList = dao.getParentCategories(parentIdList);
        return new PageImpl<>(
                CategoryDTO.map(categoryList, parentCategoryList),
                pageable,
                dao.countAllCategory(term, parentId, categoryType)
        );
    }

    @Override
    public List<Category> getAllIdAndTitle() {
        return dao.getAllIdAndTitle();
    }

    @Override
    public Category getOneCategory(String categoryId) {
        return dao.getOneCategory(categoryId);
    }

    @Override
    public List<Category> getChildrenByParentId(String parentId) {
        return dao.getChildrenByParentId(parentId);
    }

    public List<Category> getAllEnumType(CategoryType categoryType) {
        return dao.getAllEnumType(categoryType);
    }

    @Override
    public boolean checkIfTitleIsUnique(String title) {
        return dao.checkIfTitleIsUnique(title);
    }

    @Override
    public boolean ifPropertyExistsInCategory(String propertyId) {
        return dao.ifPropertyExistsInCategory(propertyId);
    }

    @Override
    public boolean checkIfCategoryIsParentCategory(String categoryId) {
        return dao.checkIfCategoryIsParentCategory(categoryId);
    }

    @Override
    public Category getPropertyOfCategory(String categoryId) {
        return dao.getPropertyOfCategory(categoryId);
    }

    @Override
    public boolean checkIfCategoryHasSubCategory(String categoryId) {
        return dao.checkIfCategoryHasSubCategory(categoryId);
    }

    @Override
    public boolean checkIfCategoryIsAssetTemplateParent(String categoryId) {
        return assetTemplateService.checkIfCategoryIsAssetTemplateParent(categoryId);
    }

    @Override
    public boolean newSaveCategory(Category category) {
        return dao.newSaveCategory(category);
    }

    @Override
    public Category newGetOneCategory(String categoryId) {
        return dao.newGetOneCategory(categoryId);
    }

    @Override
    public Page<Category> getAllNewCategoryWithPagination(NewCategory newCategory, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                dao.getAllNewCategoryWithPagination(newCategory, pageable, totalElement),
                pageable,
                dao.countNewCategory(newCategory)
        );
    }

    @Override
    public boolean newUpdateCategory(Category category) {
        return dao.newUpdateCategory(category);
    }

    @Override
    public boolean checkIfCategoryUsedInAsset(String categoryId) {
        return assetService.checkIfCategoryUsedInAsset(categoryId);
    }

    @Override
    public List<Category> NewGetAllCategory() {
        return assetService.NewGetAllCategory();
    }

    @Override
    public List<Category> getCategoryListByCategoryIdList(List<String> categoryIdList) {
        return dao.getCategoryListByCategoryIdList(categoryIdList);
    }

}
