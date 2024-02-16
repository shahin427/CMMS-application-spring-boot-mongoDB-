package org.sayar.net.Model.Mongo.poll.model.elements;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by morteza on 6/1/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

public class ComboBoxOptionClass {
    @Id
    private String id;
    private String title;
    private List<Object> subQuestionList;
}
