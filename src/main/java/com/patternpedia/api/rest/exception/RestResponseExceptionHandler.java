package com.patternpedia.api.rest.exception;

import com.patternpedia.api.exception.NullPatternSchemaException;
import com.patternpedia.api.rest.model.ErrorMessageDTO;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestResponseExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            ResourceNotFoundException.class
    })
    protected ResponseEntity<Object> handleEntityNotFoundExceptions(RuntimeException ex, WebRequest request) {
        ErrorMessageDTO errorMessage = new ErrorMessageDTO(ex.getMessage(), HttpStatus.NOT_FOUND);
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {
            NullPatternSchemaException.class
    })
    protected ResponseEntity<Object> handleNullPatternSchemaException(RuntimeException ex, WebRequest request) {
        ErrorMessageDTO errorMessage = new ErrorMessageDTO(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {
            Exception.class
    })
    protected ResponseEntity<Object> handleStorageExceptions(RuntimeException ex, WebRequest request) {
        ErrorMessageDTO errorMessage = new ErrorMessageDTO(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return handleExceptionInternal(ex, errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
