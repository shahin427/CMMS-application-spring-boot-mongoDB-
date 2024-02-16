package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class PersonnelFunctionDTO {
    private String userId;
    private Date from;
    private Date until;
}
