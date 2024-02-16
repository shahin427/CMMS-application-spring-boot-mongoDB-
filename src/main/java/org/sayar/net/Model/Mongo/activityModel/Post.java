package org.sayar.net.Model.Mongo.activityModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    private String id;
//    @Pattern(regexp = PatternList.FA_TEXT)
    private String title;
//    @Pattern(regexp = PatternList.objectId)
//    private String organizationId;
    private long organizationId;
    private boolean accountingAccess;
    private boolean statisticsAccess;
    private boolean tripsAccess;
    private boolean requestAccess;


    private boolean hallAccess=false;
    private boolean createRequestAccess=false;
    private boolean manageEmployeeAccess=false;
    private boolean activityAccess=false;
    private boolean formBuilderAccess=false;

}
