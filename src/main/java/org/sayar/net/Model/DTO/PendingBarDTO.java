package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class PendingBarDTO {
    private String relatedId;
    private String title;
    private String code;
    private Date requiredCompletionDate;
    private boolean late;
}
