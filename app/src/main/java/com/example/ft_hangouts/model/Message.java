package com.example.ft_hangouts.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Message implements Serializable {
    private int messageId;
    private String contactNumber;
    private String messageContent;
    private int messageOwner;

    public Message()  {

    }

    public Message(int messageId, String contactNumber, String messageContent, int messageOwner) {
        this.messageId = messageId;
        this.contactNumber = contactNumber;
        this.messageContent = messageContent;
        this.messageOwner = messageOwner;
    }

    public Message(String contactNumber, String messageContent, int messageOwner) {
        this.contactNumber = contactNumber;
        this.messageContent = messageContent;
        this.messageOwner = messageOwner;
    }

    public int getMessageId() { return messageId; }

    public void setMessageId(int messageId) { this.messageId = messageId; }

    public String getContactNumber() { return contactNumber; }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMessageContent() { return messageContent; }

    public void setMessageContent(String messageContent) { this.messageContent = messageContent; }

    public int getMessageOwner() { return messageOwner; }

    public void setMessageOwner(int messageOwner) { this.messageOwner = messageOwner; }

    @NonNull
    @Override
    public String toString()  {
        return this.messageContent;
    }
}
