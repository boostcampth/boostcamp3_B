package com.swsnack.catchhouse.data.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private String timestamp;
    private String sendUuid;
    private String content;

    public Message() {
    }

    public Message(String sendUuid, String content) {
        this.sendUuid = sendUuid;
        this.content = content;
        this.timestamp = String.valueOf(new Date().getTime());
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSendUuid() {
        return sendUuid;
    }

    public String getContent() {
        return content;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setSendUuid(String sendUuid) {
        this.sendUuid = sendUuid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Message)) {
           return false;
        }
        return this.timestamp.equals(((Message) obj).getTimestamp());
    }
}