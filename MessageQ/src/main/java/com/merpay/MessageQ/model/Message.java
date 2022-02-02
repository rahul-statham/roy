package com.merpay.MessageQ.model;

/**
 * Message POJO Class
 */
public class Message {

    private String messageId;
    private String message;

    public Message(String messageId, String message) {
        this.messageId = messageId;
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
