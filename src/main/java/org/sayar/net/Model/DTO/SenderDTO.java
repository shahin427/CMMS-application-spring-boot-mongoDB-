package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class SenderDTO {
    private String receiverUserId;
    private Date from;
    private Date to;
}
