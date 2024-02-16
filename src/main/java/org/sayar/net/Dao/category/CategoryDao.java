package org.sayar.net.Dao.category;


import org.sayar.net.Dao.category.aggrigation.CountResults;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.Enum.NewCategory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryDao extends GeneralDao<Category> {
    Category add(Category category);

    Category getParentCategory();

    Category getACategoryParentById(String categoryId);

    List<Category> getChildren(Category category);

    List<Category> getParentChildren(String parentCatId);

    List<CountResults> getCategoryCount(String organId);

    boolean haveCategoryWithTitle(String title, String id);

    boolean detachCategoryAssets(String categoryId);

    void incrementAssetCount(String categoryId, String organId);

    List<Category> getAllSortByAssetCount(String organId);

    List<Category> getAllParents(String organId, boolean isPart);

    boolean existenceByCategory(String categoryId);

    Category updateCategory(Category category);

    Category postCategory(Category category);

    boolean CheckIfCategoryNotDuplicated(Category category);

    boolean ifPropertyExistsInCategory(String id);

    List<Category> getAllListPageable(String term, String parentId, CategoryType categoryType, Pageable pageable, Integer totalElement);

    List<Category> getAllIdAndTitle();

    Category getOneCategory(String categoryId);

    List<Category> getChildrenByParentId(String parentId);

    List<Category> getAllEnumType(CategoryType categoryType);

    Category getCategoryTypeByCategoryId(String parentCategoryId);

    boolean checkIfTitleIsUnique(String title);

    long countAllCategory(String term, String parentId, CategoryType categoryType);

    List<Category> getParentCategories(List<String> parentIdList);

    boolean checkIfCategoryIsParentCategory(String categoryId);

    Category getPropertyOfCategory(String categoryId);

    boolean checkIfCategoryHasSubCategory(String categoryId);

    Category getCategoryType(String parentId);

    boolean newSaveCategory(Category category);

    Category newGetOneCategory(String categoryId);

    List<Category> getAllNewCategoryWithPagination(NewCategory newCategory, Pageable pageable, Integer totalElement);

    long countNewCategory(NewCategory newCategory);

    boolean newUpdateCategory(Category category);

    List<Category> getCategoryListByCategoryIdList(List<String> categoryIdList);
}
