package org.sayar.net.Model.Mongo.poll.model.elements;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by sina on 4/29/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class MatrixQuestion implements Serializable{
    private String title;
    private boolean multipleSelect;
}
