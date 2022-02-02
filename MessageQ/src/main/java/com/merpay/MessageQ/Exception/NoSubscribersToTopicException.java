package com.merpay.MessageQ.Exception;

/**
 * Exception class to handle the scenario where there are no subscribers to a
 * topic and a subscriber trying to get messages
 */
public class NoSubscribersToTopicException extends RuntimeException {

    public NoSubscribersToTopicException(String message) {
        super(message);
    }

}
