package com.swsnack.catchhouse.repository;

import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

public interface FavoriteRoomRepository {

    void setFavoriteRoom(Room room);

    void deleteFavoriteRoom(Room room);

    List<Room> getFavoriteRoomList();

    void deleteFavoriteRoom();

    Room getFavoriteRoom(String key);

    void updateRoom(Room room);
}
