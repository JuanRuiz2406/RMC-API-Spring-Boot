package com.api.ReportsMyCity.exceptions;

import java.sql.Date;

public class ErrorDetails {

    private Date timestamp;
    private String messageString;
    private String details;


    public ErrorDetails(Date timestamp, String messageString, String details) {
        super();
        this.timestamp = timestamp;
        this.messageString = messageString;
        this.details = details;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public String getMessageString() {
        return messageString;
    }
    public void setMessageString(String messageString) {
        this.messageString = messageString;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
}
