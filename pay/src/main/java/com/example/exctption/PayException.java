package com.example.exctption;

public class PayException extends RuntimeException {

    private String errorMsg;

    public PayException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public PayException(Throwable e) {
        super(e);
        this.errorMsg = e.getMessage();
    }

    public PayException(String errorMsg, Throwable e) {
        super(errorMsg, e);
        this.errorMsg = errorMsg;
    }
}
