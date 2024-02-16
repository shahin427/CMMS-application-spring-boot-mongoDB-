package org.sayar.net.Model.Mongo.poll.model.form;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author yaqub
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = FormData.ENTITY_NAME)
@Data
@NoArgsConstructor
public class FormData {
    @Transient
    public final static String ENTITY_NAME = "formData";
    @Id
    private String id;
    private String formId;
    private String formTitle;
    private List<QuestionAnswer> answerList;
    private String creatorId;
    private Date systemCreationDate;
}   
