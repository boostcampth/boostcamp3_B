package com.swsnack.catchhouse.data.chattingdata.model;

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

}