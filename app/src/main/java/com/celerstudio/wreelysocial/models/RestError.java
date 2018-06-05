package com.celerstudio.wreelysocial.models;

public class RestError {

    private boolean error;
    private String message;

    public RestError() {
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
