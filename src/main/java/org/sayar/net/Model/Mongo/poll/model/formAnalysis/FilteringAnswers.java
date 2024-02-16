package org.sayar.net.Model.Mongo.poll.model.formAnalysis;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by morteza on 7/27/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class FilteringAnswers {
    private String filteringValue;
    private List<Object> filteringAnswers = new ArrayList<>();

}
