package org.sayar.net.Model.newModel.file.dto;

import java.util.ArrayList;
import java.util.List;

public class IdSetterDTO {
   public enum FN{
      imageIdList,
      categoryId
   }
   private List<String> imageIdList= new ArrayList<>();
   private String categoryId;

   public IdSetterDTO() {
   }

   public List<String> getImageIdList() {
      return imageIdList;
   }

   public void setImageIdList(List<String> imageIdList) {
      this.imageIdList = imageIdList;
   }

   public String getCategoryId() {
      return categoryId;
   }

   public void setCategoryId(String categoryId) {
      this.categoryId = categoryId;
   }
}
