//package org.sayar.net.Model.Category;
//
//import org.sayar.net.Model.Asset.Property;
//import org.sayar.net.Model.newModel.Image;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.index.IndexDirection;
//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.util.Date;
//import java.util.List;
//
////import javax.persistence.Id;
//
//@Document
//public class Category {
//    @Id
//    private String id;
//    private String title;
//    private String description;
//    private List<Property> properties;
//    private Image image;
//    private List<String> subCategoriesId;
//    private String parentId;
//    @Indexed(direction = IndexDirection.DESCENDING)
//    private Long creationTime;
//
//    public Category() {
//        this.creationTime=new Date().getTime();
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public List<String> getSubCategoriesId() {
//        return subCategoriesId;
//    }
//
//    public void setSubCategoriesId(List<String> subCategoriesId) {
//        this.subCategoriesId = subCategoriesId;
//    }
//
//    public String getParentId() {
//        return parentId;
//    }
//
//    public void setParentId(String parentId) {
//        this.parentId = parentId;
//    }
//
//    public List<Property> getProperties() {
//        return properties;
//    }
//
//    public void setProperties(List<Property> properties) {
//        this.properties = properties;
//    }
//
//    public Image getImage() {
//        return image;
//    }
//
//    public void setImage(Image image) {
//        this.image = image;
//    }
//}
