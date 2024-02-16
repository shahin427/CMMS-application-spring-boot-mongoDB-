package org.sayar.net.exceptionHandling;

public class ApiRepetitiveException extends RuntimeException {
        public ApiRepetitiveException() {
            super("ورودی تکراری");
        }
    }
