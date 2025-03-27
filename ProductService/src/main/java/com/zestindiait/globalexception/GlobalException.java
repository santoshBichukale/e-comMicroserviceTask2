package com.zestindiait.globalexception;

import com.zestindiait.customeexception.ProductNotFoundException;
import com.zestindiait.customeexception.ProductAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleNotFoundExceptions(RuntimeException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request.getDescription(false));
    }

    @ExceptionHandler({ProductAlreadyExistsException.class})
    public ResponseEntity<Map<String, Object>> handleAlreadyExistsExceptions(RuntimeException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request.getDescription(false));
    }


    private ResponseEntity<Map<String, Object>> buildResponse(String message, HttpStatus httpStatus, String description) {
        return new ResponseEntity<>(Map.of(
                "message", message,
                "status", httpStatus.value(),
                "error", httpStatus.getReasonPhrase(),
                "path", description
        ), httpStatus);
    }

}
