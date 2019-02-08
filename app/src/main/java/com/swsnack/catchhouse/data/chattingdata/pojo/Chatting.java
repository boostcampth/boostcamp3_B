package com.swsnack.catchhouse.data.chattingdata.pojo;

import java.util.Map;

public class Chatting {

    private Map<String, Boolean> users;
    private Map<String, Message> message;

    public Chatting(Map<String, Boolean> users) {
        this.users = users;
    }


}
