package org.sayar.net.Model.Asset;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.newModel.CategoryType;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssetTemplate {

    @Id
    private String id;
    private String name;
    private String description;
    private List<Property> properties;
    private DocumentFile image;
    private String parentCategoryId;
    private String parentCategoryTitle;
    private String subCategoryId;
    private String subCategoryTitle;
    private String note;
    @Indexed(direction = IndexDirection.DESCENDING)
    private Long creationTime;
    private List<AssignedToPerson> assignedToPersonList;
    private List<AssignedToGroup> assignedToGroupList;
    private CategoryType parentCategoryType;
}
