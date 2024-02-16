package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class MdtReturn {
    private String date;
    private Date failureDate;
    private long mdt;
    private long count;
    private long failureDuration;
}
