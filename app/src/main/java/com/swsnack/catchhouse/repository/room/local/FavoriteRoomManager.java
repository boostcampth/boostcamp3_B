package com.swsnack.catchhouse.repository.room.local;

import com.swsnack.catchhouse.data.entity.RoomEntity;

import java.util.List;

public interface FavoriteRoomManager {

    void setFavoriteRoom(RoomEntity roomEntity);

    void deleteFavoriteRoom(RoomEntity roomEntity);

    List<RoomEntity> getFavoriteRoomList();

    void deleteFavoriteRoom();

    RoomEntity getFavoriteRoom(String key);

    void updateRoom(RoomEntity roomEntity);
}
