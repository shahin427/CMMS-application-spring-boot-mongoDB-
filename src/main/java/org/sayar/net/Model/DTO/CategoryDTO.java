package org.sayar.net.Model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.Asset.Property;
import org.sayar.net.Model.newModel.Category;
import org.sayar.net.Model.newModel.CategoryType;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    private String id;
    private String title;
    private String parentCategoryId;
    private String parentCategoryTitle;
    private CategoryType categoryType;
    private List<Property> properties;

    public static List<CategoryDTO> map(List<Category> categoryList, List<Category> parentCategoryList) {
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        categoryList.forEach(childCategory -> {
            if (childCategory.getParentId().equals("ROOT")) {
                CategoryDTO categoryDTO = new CategoryDTO();
                if (childCategory.getId() != null)
                    categoryDTO.setId(childCategory.getId());
                if (childCategory.getTitle() != null)
                    categoryDTO.setTitle(childCategory.getTitle());
                if (childCategory.getProperties() != null)
                    categoryDTO.setProperties(childCategory.getProperties());
                categoryDTO.setCategoryType(childCategory.getCategoryType());
                categoryDTO.setParentCategoryId("ROOT");
                categoryDTOS.add(categoryDTO);
            } else {
                parentCategoryList.forEach(parentCategory -> {
                    if (childCategory.getParentId().equals(parentCategory.getId())) {
                        CategoryDTO categoryDTO = new CategoryDTO();

                        if (childCategory.getId() != null)
                            categoryDTO.setId(childCategory.getId());
                        if (childCategory.getTitle() != null)
                            categoryDTO.setTitle(childCategory.getTitle());
                        if (childCategory.getProperties() != null) {
                            categoryDTO.setProperties(childCategory.getProperties());
                        }
                        if (parentCategory.getId() != null) {
                            categoryDTO.setParentCategoryId(parentCategory.getId());
                        }
                        if (parentCategory.getTitle() != null) {
                            categoryDTO.setParentCategoryTitle(parentCategory.getTitle());
                        }
                        categoryDTO.setCategoryType(parentCategory.getCategoryType());
                        categoryDTOS.add(categoryDTO);
                    }
                });
            }
        });
        return categoryDTOS;
    }
}
