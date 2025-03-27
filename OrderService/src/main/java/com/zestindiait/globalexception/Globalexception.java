package com.zestindiait.globalexception;

import com.zestindiait.customexception.OrderNotFoundException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
public class Globalexception {


    @ExceptionHandler({OrderNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleOrderNotFoundExceptions(RuntimeException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request.getDescription(false));
    }


    @ExceptionHandler({FeignException.NotFound.class})
    public ResponseEntity<Map<String, Object>> handleFeignNotFoundExceptions(FeignException ex, WebRequest request) {
        String errorMessage = ex.getMessage();


        if (errorMessage.contains("User not found")) {
            return buildResponse("User not found for the provided ID", HttpStatus.NOT_FOUND, request.getDescription(false));
        }


        if (errorMessage.contains("Product not found")) {
            return buildResponse("Product not found for the provided ID", HttpStatus.NOT_FOUND, request.getDescription(false));
        }


        return buildResponse(errorMessage, HttpStatus.NOT_FOUND, request.getDescription(false));
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
