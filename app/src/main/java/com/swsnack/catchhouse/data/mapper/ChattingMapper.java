package com.swsnack.catchhouse.data.mapper;

import com.google.firebase.database.DataSnapshot;
import com.swsnack.catchhouse.data.model.Chatting;

import java.util.ArrayList;
import java.util.List;

public class ChattingMapper implements FirebaseMapper<DataSnapshot, Chatting> {

    @Override
    public Chatting map(DataSnapshot from) {
        Chatting chatting = from.getValue(Chatting.class);
        chatting.setRoomUid(from.getKey());
        return chatting;
    }

    @Override
    public List<Chatting> mapToList(DataSnapshot from) {
        List<Chatting> chattingList = new ArrayList<>();
        for(DataSnapshot snapshot : from.getChildren()) {
            chattingList.add(map(snapshot));
        }
        return chattingList;
    }
}
