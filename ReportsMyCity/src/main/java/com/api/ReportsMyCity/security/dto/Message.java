package com.api.ReportsMyCity.security.dto;

import org.springframework.http.HttpStatus;

public class Message {
    private String message;

    private HttpStatus code;

    public Message(String message, HttpStatus code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }
}
