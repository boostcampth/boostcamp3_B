package com.swsnack.catchhouse.data.mapper;

import com.google.firebase.database.DataSnapshot;
import com.swsnack.catchhouse.data.model.Room;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRoomMapper implements FirebaseMapper<Room> {

    @Override
    public List<Room> mapToList(DataSnapshot from) {
        List<Room> roomList = new ArrayList<>();
        for(DataSnapshot snapshot : from.getChildren()) {
            roomList.add(map(snapshot));
        }
        return roomList;
    }

    @Override
    public String mapFromKey(DataSnapshot from) {
        return from.getKey();
    }

    @Override
    public Room map(DataSnapshot from) {
        return from.getValue(Room.class);
    }
}
