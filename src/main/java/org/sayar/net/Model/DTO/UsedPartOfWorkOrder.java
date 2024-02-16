package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UsedPartOfWorkOrder {
    private String partId;
    private Date from;
    private Date until;
}
