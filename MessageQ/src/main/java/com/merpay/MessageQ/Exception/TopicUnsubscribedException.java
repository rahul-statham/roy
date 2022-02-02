package com.merpay.MessageQ.Exception;

/**
 * Exception class to handle the scenario of a Subscriber accessing unsubscribed
 * topic
 */
public class TopicUnsubscribedException extends RuntimeException {

    public TopicUnsubscribedException(String message) {
        super(message);
    }

}
