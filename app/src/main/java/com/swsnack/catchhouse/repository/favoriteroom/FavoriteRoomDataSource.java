package com.swsnack.catchhouse.repository.favoriteroom;

import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

public interface FavoriteRoomDataSource {

    void setFavoriteRoom(Room room);

    void deleteFavoriteRoom(Room room);

    List<Room> getFavoriteRoomList();

    void deleteFavoriteRoom();

    Room getFavoriteRoom(String key);

    void updateRoom(Room room);
}
