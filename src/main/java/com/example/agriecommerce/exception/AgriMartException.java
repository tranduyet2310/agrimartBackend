package com.example.agriecommerce.exception;

import org.springframework.http.HttpStatus;

public class AgriMartException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public AgriMartException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public AgriMartException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
