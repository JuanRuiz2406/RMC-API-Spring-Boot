package com.api.ReportsMyCity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.OK)
public class ApiOkException extends RuntimeException{

    public static final long serialVersionUID = 1L;

    public ApiOkException(String message){
        super(message);
    }
}
