package org.sayar.net.Model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ResetPasswordToken {
    private String token;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date tokenExpireDate;
}
