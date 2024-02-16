package org.sayar.net.Model;

import lombok.Data;

import java.util.Date;

@Data
public class MtbfReturn {
    long workTimeDuration;
    private String date;
    private long failureDuration;
    private long count;
    private Date startDate;
}
