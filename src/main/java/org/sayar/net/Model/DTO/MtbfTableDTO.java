package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class MtbfTableDTO {
    private Date from;
    private Date until;
    private long number;
}
