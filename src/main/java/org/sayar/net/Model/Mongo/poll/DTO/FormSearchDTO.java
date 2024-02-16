package org.sayar.net.Model.Mongo.poll.DTO;

import lombok.Data;

@Data
public class FormSearchDTO {
    private String id;
    private String name;
    private String description;
    private String formCategoryId;
}
