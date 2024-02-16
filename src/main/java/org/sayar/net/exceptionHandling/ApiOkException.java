package org.sayar.net.exceptionHandling;

public class ApiOkException extends RuntimeException{
    public ApiOkException(String message) {
        super(message);
    }
}
