package org.sayar.net.Model.ResponseModel;

import org.sayar.net.Model.newModel.Image;

public class CategoryResponse {

    private Long id;
    private Long parentId;
    private String name;
    private String description;
    private String persianName;
    private Image image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        if (parentId==null){
            this.parentId = 0l;
        }else {
            this.parentId = parentId;
        }
    }


//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

//    public long getParentId() {
//        return parentId;
//    }
//
//    public void setParentId(Object parentId) {
//        if (parentId!=null){
//            this.parentId = (long)parentId;
//        }else {
//            this.parentId = 0;
//        }
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPersianName() {
        return persianName;
    }

    public void setPersianName(String persianName) {
        this.persianName = persianName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
