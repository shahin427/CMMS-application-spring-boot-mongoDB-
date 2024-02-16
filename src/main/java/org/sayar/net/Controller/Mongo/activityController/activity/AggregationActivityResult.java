package org.sayar.net.Controller.Mongo.activityController.activity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.Mongo.MyModel.ActivityLevel;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class AggregationActivityResult {



        private String id;
        private String title;
        private String description;
        private ActivityLevel activityLevelList
                ;
        private Object activityLevelListform;
        private long orgId;
        private String companyName;


}
