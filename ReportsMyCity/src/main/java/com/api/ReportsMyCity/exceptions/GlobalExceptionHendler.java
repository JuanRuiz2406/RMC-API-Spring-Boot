package com.api.ReportsMyCity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.sql.Date;

public class GlobalExceptionHendler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> hendleResourceNotException(ResourceNotFoundException exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(0), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ApiOkException.class)
    public ResponseEntity<?> hendleApiOktException(ApiOkException exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(0), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> hendleGlobalException(Exception exception, WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(new Date(0), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
