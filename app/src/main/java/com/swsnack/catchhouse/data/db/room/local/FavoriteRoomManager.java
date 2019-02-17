package com.swsnack.catchhouse.data.db.room.local;

import com.swsnack.catchhouse.data.entity.RoomEntity;

import java.util.List;

import androidx.lifecycle.LiveData;

public interface FavoriteRoomManager {

    void setFavoriteRoom(RoomEntity roomEntity);

    void deleteFavoriteRoom(RoomEntity roomEntity);

    List<RoomEntity> getFavoriteRoomList();

    void deleteFavoriteRoom();

    RoomEntity getFavoriteRoom(String key);
}
