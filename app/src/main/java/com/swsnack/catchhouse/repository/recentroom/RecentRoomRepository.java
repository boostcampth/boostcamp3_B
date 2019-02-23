package com.swsnack.catchhouse.repository.recentroom;

import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

public interface RecentRoomRepository {

    void setRecentRoom(Room room);

    List<Room> getRecentRoom();

    void deleteRecentRoomList();

    void deleteRoom(Room room);
}
