package com.merpay.MessageQ.Exception;

/**
 * Exception Class to Handle a scenario when a publisher tries to publish
 * message to a topic which he is not registered with
 */
public class UnauthorisedToPublishException extends RuntimeException {

    public UnauthorisedToPublishException(String message) {
        super(message);
    }

}
