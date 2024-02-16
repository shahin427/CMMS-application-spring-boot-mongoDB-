package org.sayar.net.exceptionHandling;

public class ApiParentFoundException extends RuntimeException{
    public ApiParentFoundException() {
        super("ExistInDataBase");
    }
}
