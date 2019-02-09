package com.swsnack.catchhouse.data.chattingdata.pojo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

public class Chatting {

    private Map<String, Boolean> users;

    @Nullable
    private Message message;

    public Chatting() {
    }

    public Chatting(Map<String, Boolean> users) {
        this.users = users;
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }

    public void setUsers(Map<String, Boolean> users) {
        this.users = users;
    }

    @Nullable
    public Message getMessage() {
        return message;
    }

    public void setMessage(@NonNull Message message) {
        this.message = message;
    }
}
