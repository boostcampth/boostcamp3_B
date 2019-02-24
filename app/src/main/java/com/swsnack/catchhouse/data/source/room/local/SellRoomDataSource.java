package com.swsnack.catchhouse.data.source.room.local;

import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

public interface SellRoomDataSource {

    void setSellRoom(Room room);

    void deleteSellRoom(Room room);

    List<Room> getSellRoomList();

    void deleteSellRoom();

    Room getSellRoom(String key);

    void updateRoom(Room room);
}
