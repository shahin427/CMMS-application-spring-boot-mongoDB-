package org.sayar.net.exceptionHandling;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiWrongUserNamePassword extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public ApiWrongUserNamePassword(String message) {
        this.message = message;
    }
}
