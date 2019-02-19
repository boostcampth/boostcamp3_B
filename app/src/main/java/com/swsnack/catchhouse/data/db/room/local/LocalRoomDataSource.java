package com.swsnack.catchhouse.data.db.room.local;

import com.swsnack.catchhouse.data.entity.RoomEntity;

import java.util.List;

import androidx.lifecycle.LiveData;

//FIXME : Entity MarkingInterface 사용해서 묶기, Dao인터페이스 추상화해보기, Helper클래스 추상화 해보기
public interface LocalRoomDataSource {

    void setFavoriteRoom(RoomEntity roomEntity);

    void deleteFavoriteRoom(RoomEntity roomEntity);

    List<RoomEntity> getFavoriteRoomList();

    void deleteFavoriteRoom();

    RoomEntity getFavoriteRoom(String key);

    void updateRoom(RoomEntity roomEntity);
}
