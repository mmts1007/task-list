package com.example.handler;

import com.example.api.exception.NotFoundException;
import com.example.entity.ErrorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ErrorEntity errorEntity = createErrorEntity(e, status);
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorEntity.addDetail(fieldError.getField() + " " + getMessage(fieldError, request));
        }

        return super.handleExceptionInternal(e, errorEntity, headers, status, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorEntity errorEntity = createErrorEntity(e, status, "Specified resource is not found");
        return handleExceptionInternal(e, errorEntity, null, status, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleSystemException(Exception e, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorEntity errorEntity = createErrorEntity(e, status, "System error is occurred");
        return handleExceptionInternal(e, errorEntity, null, status, request);
    }

    private ErrorEntity createErrorEntity(Exception e, HttpStatus status, String defaultMessage) {
        ErrorEntity errorEntity = createErrorEntity(e, status);
        errorEntity.addDetail(defaultMessage);
        return errorEntity;
    }

    private ErrorEntity createErrorEntity(Exception e, HttpStatus status) {
        ErrorEntity errorEntity = new ErrorEntity();
        errorEntity.setStatus(status);
        return errorEntity;
    }

    private String getMessage(MessageSourceResolvable resolvable, WebRequest request) {
        return messageSource.getMessage(resolvable, request.getLocale());
    }
}
