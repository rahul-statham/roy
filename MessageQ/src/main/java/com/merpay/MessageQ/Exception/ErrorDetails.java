package com.merpay.MessageQ.Exception;

/**
 * Class to store and propagate exception related details
 */
public class ErrorDetails {

    private String errorMessage;

    public ErrorDetails(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
