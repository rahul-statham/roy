package com.merpay.MessageQ.model;

/**
 * Subscriber POJO Class
 */
public class Subscriber {

    private String subscriberId;

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public Subscriber(String subscriberId) {
        this.subscriberId = subscriberId;
    }

}
