package com.example.javacv.exception;

public class ConverterException extends RuntimeException{

    private int code=1001;

    private String message;

    public ConverterException(String message) {
        super(message);
        this.message = message;
    }

    public ConverterException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
