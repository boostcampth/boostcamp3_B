package com.swsnack.catchhouse.repository.room.local;

import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

public interface RecentRoomDataSource {

    void setRecentRoom(Room room);

    List<Room> getRecentRoom();

    void deleteRecentRoomList();
}
