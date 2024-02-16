package org.sayar.net.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ApiRequestException.class)
    public ResponseEntity<ApiException> handleApiRequestException(ApiRequestException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Asia/Tehran"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = ApiNotFoundException.class)
    public ResponseEntity<ApiException> handleApiNotFoundException(ApiNotFoundException e) {

        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ApiException exception = new ApiException(e.getMessage(), notFound, ZonedDateTime.now(ZoneId.of("Asia/Tehran")));
        return new ResponseEntity<>(exception, notFound);
    }

    @ExceptionHandler(value = ApiOrganIdException.class)
    public ResponseEntity<ApiException> handleOrganIdException(ApiOrganIdException e) {
        HttpStatus notFound = HttpStatus.OK;
        ApiException exception = new ApiException(e.getMessage(), notFound, ZonedDateTime.now(ZoneId.of("Asia/Tehran")));
        return new ResponseEntity<>(exception, notFound);
    }

    @ExceptionHandler(value = ApiRepetitiveException.class)
    public ResponseEntity<ApiException> handleRepetitiveException(ApiRepetitiveException e) {
        HttpStatus notFound = HttpStatus.OK;
        ApiException exception = new ApiException(e.getMessage(), notFound, ZonedDateTime.now(ZoneId.of("Asia/Tehran")));
        return new ResponseEntity<>(exception, notFound);
    }

    @ExceptionHandler(value = ApiParentFoundException.class)
    public ResponseEntity<ApiException> handleExistenceInDataBaseException(ApiParentFoundException e) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ApiException apiException = new ApiException(e.getMessage(), httpStatus, ZonedDateTime.now(ZoneId.of("Asia/Tehran")));
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = ApiFoundException.class)
    public ResponseEntity<ApiException> handleExistenceInDataBaseException(ApiFoundException e) {
        HttpStatus httpStatus = HttpStatus.FOUND;
        ApiException apiException = new ApiException((e.getMessage()), httpStatus, ZonedDateTime.now(ZoneId.of("Asia/Tehran")));
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = ApiExistsException.class)
    public ResponseEntity<ApiException> handleExistenceInDataBaseException(ApiExistsException e) {
        HttpStatus httpStatus = HttpStatus.FOUND;
        ApiException apiException = new ApiException(e.getMessage(), httpStatus, ZonedDateTime.now(ZoneId.of("Asia/Tehran")));
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = ApiOkException.class)
    public ResponseEntity<ApiOkException> handleExistenceInDataBaseExceptionWithOkStatus(ApiOkException e) {
        HttpStatus httpStatus = HttpStatus.OK;
        ApiOkException apiException = new ApiOkException(e.getMessage());
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = ApiInputIsInComplete.class)
    public ResponseEntity<ApiInputIsInComplete> handleExistenceInDataBaseExceptionWithOkStatus(ApiInputIsInComplete e) {
        ApiInputIsInComplete apiInputIsInComplete = new ApiInputIsInComplete(e.getMessage(), e.getHttpStatus());
        return new ResponseEntity<>(apiInputIsInComplete, e.getHttpStatus());
    }

    @ExceptionHandler(value = ApiWrongUserNamePassword.class)
    public ResponseEntity<ApiWrongUserNamePassword> handleWrongUserNamePassword(ApiWrongUserNamePassword e) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ApiWrongUserNamePassword apiWrongUserNamePassword = new ApiWrongUserNamePassword(e.getMessage());
        return new ResponseEntity<>(apiWrongUserNamePassword, httpStatus);
    }
}