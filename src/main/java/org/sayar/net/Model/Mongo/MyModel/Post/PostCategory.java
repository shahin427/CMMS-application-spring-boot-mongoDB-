package org.sayar.net.Model.Mongo.MyModel.Post;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostCategory {
  @Id
  private String id;
  @NotNull
  @Size(min = 2, max = 100)
  private String title;
  private Boolean managerIs;

  public PostCategory() {
    this.managerIs = false;

  }

  public PostCategory(String id, String title, Boolean managerIs) {
    this.id = id;
    this.title = title;
    this.managerIs = managerIs;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Boolean getManagerIs() {
    return managerIs;
  }

  public void setManagerIs(Boolean managerIs) {
    this.managerIs = managerIs;
  }

}
