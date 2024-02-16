package org.sayar.net.Model.newModel;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class Currency {
    @Id
    private String id;
    private String title;
    private String isoCode;
}
