package org.sayar.net.Model.Mongo.MyModel.Post;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

//import netscape.security.Privilege;
//import netscape.security.Privilege;

@Data
@AllArgsConstructor
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Post implements Serializable {

  @Id
  private String id;
  @NotNull
  private long orgId;
  private String title;
  @DBRef
  @NotNull
  private PostCategory postCategory;
//  private List<LightPerson> lightPersonList;
//  @DBRef
//  private List<Privilege> privilegeList;

}
