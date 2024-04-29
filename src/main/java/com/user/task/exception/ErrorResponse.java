package com.user.task.exception;

public class ErrorResponse {

    private String errorMessage;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return errorMessage;
    }

    public void setMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}