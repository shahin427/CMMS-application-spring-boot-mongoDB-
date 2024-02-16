package org.sayar.net.Model.Mongo.poll.model.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Created by morteza on 11/16/17.
 */
@Data
@NoArgsConstructor
public class FormCategory implements Serializable {
    @Id
    private String id;
    private String title;
}
