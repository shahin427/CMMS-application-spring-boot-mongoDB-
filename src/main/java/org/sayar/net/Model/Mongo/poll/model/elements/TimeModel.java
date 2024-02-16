package org.sayar.net.Model.Mongo.poll.model.elements;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Meghdad Hajilo
 */

@NoArgsConstructor
@Data
public class TimeModel {
    private Integer hour;
    private Integer minute;
    private Integer second;
}
