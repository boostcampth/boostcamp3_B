package com.swsnack.catchhouse.data.mapper;

import com.google.firebase.database.DataSnapshot;
import com.swsnack.catchhouse.data.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageMapper implements FirebaseMapper<DataSnapshot, Message> {

    @Override
    public List<Message> mapToList(DataSnapshot from) {
        List<Message> messageList = new ArrayList<>();
        for(DataSnapshot snapshot : from.getChildren()) {
            messageList.add(map(snapshot));
        }
        return messageList;
    }

    @Override
    public Message map(DataSnapshot from) {
        return from.getValue(Message.class);
    }
}
