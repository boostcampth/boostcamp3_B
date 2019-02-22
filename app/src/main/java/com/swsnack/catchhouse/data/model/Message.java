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

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Message)) {
            return false;
        }
        Message compareObj = (Message) obj;
        return this.timestamp.equals(((Message) obj).getTimestamp()) &&
                this.sendUuid.equals(compareObj.getSendUuid()) &&
                this.content.equals(compareObj.content);
    }

    @Override
    public int hashCode() {
        return timestamp.hashCode();
    }
}