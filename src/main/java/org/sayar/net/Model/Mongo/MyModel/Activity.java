package org.sayar.net.Model.Mongo.MyModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class
Activity {
    //مدل فرایند
    @Id
    private String id;
    private String title;       //عنوان فرایند
    private String description;   //توضیحات فرایند
    private List<ActivityLevel> activityLevelList;  //لیست مراحل
}