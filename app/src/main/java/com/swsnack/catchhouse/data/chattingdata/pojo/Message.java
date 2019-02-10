package com.swsnack.catchhouse.data.chattingdata.pojo;

import java.util.Date;

public class Message {

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

//    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof Message)) {
//            return false;
//        }
//
//        Message compareObj = (Message) obj;
//        return this.timestamp.equals(compareObj.getTimestamp()) && this.content.equals(compareObj.getContent());
//    }
}