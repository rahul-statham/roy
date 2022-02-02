package com.merpay.MessageQ.model;

/**
 * Publisher POJO Class
 */
public class Publisher {

    private String publisherId;

    public Publisher(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

}
