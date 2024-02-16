package org.sayar.net.Model.Mongo.poll.model.formAnalysis;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by morteza on 7/3/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class PersonAnswer implements Serializable{

    private long personId;
    private List<Map<String,Object>> answers =new ArrayList<>();

}
