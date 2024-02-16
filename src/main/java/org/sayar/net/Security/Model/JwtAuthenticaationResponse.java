package org.sayar.net.Security.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.sayar.net.Model.DTO.UserAndUserTypDTO;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtAuthenticaationResponse  {
    private String token;
    private UserAndUserTypDTO userAndUserTypDTO;
    private boolean firstLogin;

    public JwtAuthenticaationResponse(String token,UserAndUserTypDTO userAndUserTypDTO ,boolean firstLogin) {
        this.token = token;
        this.userAndUserTypDTO = userAndUserTypDTO;
        this.firstLogin = firstLogin;
    }
}
