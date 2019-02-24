package com.swsnack.catchhouse.data.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

public class Chatting implements Serializable {

    private String roomUid;
    private Map<String, Boolean> users;
    @Nullable
    private List<Message> messages;

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

    public String getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(String roomUid) {
        this.roomUid = roomUid;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Chatting)) {
            return false;
        }

        return this.roomUid.equals(((Chatting) obj).getRoomUid());
    }

    @Override
    public int hashCode() {
        return roomUid.hashCode();
    }

    @Nullable
    public List<Message> getMessages() {
        return messages;
    }
}

