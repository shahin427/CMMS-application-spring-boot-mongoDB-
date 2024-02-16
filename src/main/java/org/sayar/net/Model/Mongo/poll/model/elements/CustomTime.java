package org.sayar.net.Model.Mongo.poll.model.elements;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by sina on 4/8/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class CustomTime {
    private int hour;
    private int minute;

}
