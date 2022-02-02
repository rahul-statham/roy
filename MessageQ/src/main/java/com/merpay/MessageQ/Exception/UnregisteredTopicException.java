package com.merpay.MessageQ.Exception;

/**
 * Custom Exception class to handle the exception for unregistered topic
 */
public class UnregisteredTopicException extends RuntimeException {

    public UnregisteredTopicException(String message) {
        super(message);
    }

}
