package com.api.ReportsMyCity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class ApiUnproccessableEntityException extends Exception{

    public ApiUnproccessableEntityException(String message){
        super(message);
    }
}
