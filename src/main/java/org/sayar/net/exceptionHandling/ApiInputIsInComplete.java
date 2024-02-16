package org.sayar.net.exceptionHandling;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class ApiInputIsInComplete extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;

    public ApiInputIsInComplete(String message, org.springframework.http.HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
