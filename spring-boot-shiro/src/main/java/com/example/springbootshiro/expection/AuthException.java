package com.example.springbootshiro.expection;

public class AuthException extends RuntimeException {

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthException() {
    }

    public AuthException(String message, Throwable throwable) {
        super(message, throwable);
        this.message = message;
    }
}
