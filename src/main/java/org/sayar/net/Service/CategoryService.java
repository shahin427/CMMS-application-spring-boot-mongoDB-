package org.sayar.net.Service;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.CategoryDTO;
import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.Enum.NewCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService extends GeneralService<Category> {

    Category add(Category category);

    Category add(Category category, String organId);

    Category updateCategory(Category category);

    Category postCategory(Category category);

    Page<CategoryDTO> getAllListPageable(String term, String parentId, CategoryType categoryType, Pageable pageable, Integer totalElement);

    List<Category> getAllIdAndTitle();

    Category getOneCategory(String categoryId);

    List<Category> getChildrenByParentId(String parentId);

    List<Category> getAllEnumType(CategoryType categoryType);

    boolean checkIfTitleIsUnique(String title);

    boolean ifPropertyExistsInCategory(String propertyId);

    boolean checkIfCategoryIsParentCategory(String categoryId);

    Category getPropertyOfCategory(String categoryId);

    boolean checkIfCategoryHasSubCategory(String categoryId);

    boolean checkIfCategoryIsAssetTemplateParent(String categoryId);

    boolean newSaveCategory(Category category);

    Category newGetOneCategory(String categoryId);

    Page<Category> getAllNewCategoryWithPagination(NewCategory newCategory, Pageable pageable, Integer totalElement);

    boolean newUpdateCategory(Category category);

    boolean checkIfCategoryUsedInAsset(String categoryId);

    List<Category> NewGetAllCategory();

    List<Category> getCategoryListByCategoryIdList(List<String> categoryIdList);
}
