package org.sayar.net.exceptionHandling;

public class ApiOrganIdException extends RuntimeException {
    public ApiOrganIdException() {
        super("این دارای زیر مجموعه میباشد و قابل حذف نمی باشد");
    }
}
