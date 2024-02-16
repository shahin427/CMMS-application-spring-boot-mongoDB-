package org.sayar.net.Model.Mongo.poll.model.formAnalysis;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sayar.net.Model.Mongo.poll.model.form.Form;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)

@Data
@NoArgsConstructor
public class FormData implements Serializable {
    @Id
    private String id;
    @DBRef
    private Form form;
    private String date;
    private PersonAnswer personAnswer = new PersonAnswer();
    private boolean accepted = true;
    private String publishmentMethod;

}
