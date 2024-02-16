package org.sayar.net.Model.DTO;


import lombok.Data;

import java.util.Date;

@Data
public class MttrReturn {
    private String date;
    private Date repairDate;
    private long mttr;
    private long count;
    private long repairDuration;
}
