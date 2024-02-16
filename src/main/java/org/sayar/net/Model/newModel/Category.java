package org.sayar.net.Model.newModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.Asset.Property;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {

    //مدل دسته بندی دارایی
    @Id
    private String id;
    private String title;   //نام دسته بندی
    private String description;   //توضیحات

    //_______________________________________

    private List<Property> properties;
    private DocumentFile image;
    private List<String> subCategoriesId;
    private String parentId;
    private CategoryType categoryType;
    private Long assetCount = 0L;
    private String parentCategoryId;
    private String subCategoryId;

    public Category() {
    }

    public enum MN {
        id,
        title,
        organId,
        description,
        properties,
        image,
        subCategoriesId,
        parentId,
        assetCount,
        isPart
    }
}