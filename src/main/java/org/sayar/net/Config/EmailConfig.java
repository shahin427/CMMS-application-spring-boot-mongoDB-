package org.sayar.net.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
@Data
public class EmailConfig {
    @Value("spring.mail.host")
    private String host;
    @Value("spring.mail.port")
    private int port;
    @Value("spring.mail.username")
    private String username;
    @Value("spring.mail.password")
    private String password;
}
