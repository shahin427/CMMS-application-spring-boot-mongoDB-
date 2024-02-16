package org.sayar.net.Model.Mongo.poll.model.formAnalysis;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by morteza on 7/23/17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
public class AggregateCount {
    private String type;
    private long total;
}
