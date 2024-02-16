package org.sayar.net.Model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class DeletedNotificationDTO {
    private Date from;
    private Date to;
    private String subject;
    private String senderUserId;
    private String recipientUserId;
}
