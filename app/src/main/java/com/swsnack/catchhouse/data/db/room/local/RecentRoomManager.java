package com.swsnack.catchhouse.data.db.room.local;

import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

public interface RecentRoomManager {

    void setRecentRoom(Room room);

    List<Room> getRecentRoom();

    void deleteRecentRoomList();
}
