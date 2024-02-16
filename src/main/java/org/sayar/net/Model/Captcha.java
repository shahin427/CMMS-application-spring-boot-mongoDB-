package org.sayar.net.Model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Captcha {
    @Id
    private String id;
    private byte[] image;
    private String code;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date expireDate;
}
