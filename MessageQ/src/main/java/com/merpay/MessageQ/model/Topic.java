package com.merpay.MessageQ.model;

/**
 * Topic POJO Class
 */
public class Topic {

    private String topicId;
    private String publisherId;

    public Topic(String topicId, String publisherId) {
        this.topicId = topicId;
        this.publisherId = publisherId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

}
