package com.swsnack.catchhouse.data.chattingdata.pojo;

import android.support.annotation.Nullable;

import java.util.Map;

public class Chatting {

    private String roomUid;
    private Map<String, Boolean> users;
    @Nullable
    private Map<String, Message> message;

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
    public Map<String, Message> getMessage() {
        return message;
    }

    public void setMessage(@Nullable Map<String, Message> message) {
        this.message = message;
    }

    public String getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(String roomUid) {
        this.roomUid = roomUid;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Chatting)) {
            return false;
        }
        return this.roomUid.equals(((Chatting) obj).getRoomUid());
    }
}
