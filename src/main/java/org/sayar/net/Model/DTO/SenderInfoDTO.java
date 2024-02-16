package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class SenderInfoDTO {
    private String senderUserId;
    private Date from;
    private Date to;
}
