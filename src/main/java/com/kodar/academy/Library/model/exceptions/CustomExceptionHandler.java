package com.kodar.academy.Library.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DeleteActiveBookException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage deleteActiveBookException(DeleteActiveBookException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return message;
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage deleteNonExistingBookException(BookNotFoundException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.NOT_FOUND.value());
        return message;
    }

    @ExceptionHandler(BookNotActiveException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage bookNotActiveException(BookNotActiveException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return message;
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage validationException(BindException ex) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        List<String> msgsList = new ArrayList<>();
        for(FieldError fe : errors) {
            msgsList.add(fe.getDefaultMessage());
        }
        ErrorMessage message = new ErrorMessage(
                LocalDateTime.now(),
                msgsList
        );
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return message;
    }

    @ExceptionHandler(InvalidDeactReasonException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage invalidDeactReasonException(InvalidDeactReasonException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return message;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage userNotFoundException(UserNotFoundException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.NOT_FOUND.value());
        return message;
    }

    @ExceptionHandler(InsufficientBookTotalQuantityException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage insufficientBookTotalQuantityException(InsufficientBookTotalQuantityException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return message;
    }

    @ExceptionHandler(RentNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage rentNotFoundException(RentNotFoundException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.NOT_FOUND.value());
        return message;
    }

    @ExceptionHandler(BookAlreadyReturnedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage bookAlreadyReturnedException(BookAlreadyReturnedException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return message;
    }

    @ExceptionHandler(InsufficientBookAvailableQuantityException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage insufficientBookAvailableQuantityException(InsufficientBookAvailableQuantityException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return message;
    }

    @ExceptionHandler(UserProlongedRentsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage userProlongedRentsException(UserProlongedRentsException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return message;
    }

    @ExceptionHandler(DuplicateRentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage duplicateRentException(DuplicateRentException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return message;
    }

    @ExceptionHandler(RentCapException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage rentCapException(RentCapException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return message;
    }

    @ExceptionHandler(UserNotEligibleToRentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage userNotEligibleToRentException(UserNotEligibleToRentException ex) {
        ErrorMessage message = buildExceptionResponse(ex);
        message.setStatusCode(HttpStatus.BAD_REQUEST.value());
        return message;
    }

    private ErrorMessage buildExceptionResponse(RuntimeException ex) {
        List<String> msgsList = new ArrayList<>();
        msgsList.add(ex.getMessage());

        ErrorMessage message = new ErrorMessage(
                LocalDateTime.now(),
                msgsList
        );

        return message;
    }
}
