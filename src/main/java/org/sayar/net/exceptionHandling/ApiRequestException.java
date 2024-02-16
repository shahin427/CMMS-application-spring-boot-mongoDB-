package org.sayar.net.exceptionHandling;

public class ApiRequestException extends RuntimeException {

    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
