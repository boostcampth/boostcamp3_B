package com.swsnack.catchhouse.repository.room.local;

import com.swsnack.catchhouse.data.entity.RoomEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setFavoriteRoom(RoomEntity roomEntity);

    @Delete
    void deleteFavoriteRoom(RoomEntity roomEntity);

    @Query("SELECT * FROM my_favorite_room WHERE firebaseUuid = :userUuid")
    List<RoomEntity> loadFavoriteRoom(String userUuid);

    @Query("SELECT * FROM my_favorite_room WHERE room_uid = :key AND firebaseUuid = :userUuid")
    RoomEntity getFavoriteRoom(String key, String userUuid);

    @Query("DELETE FROM my_Favorite_room WHERE firebaseUuid = :userUuid")
    void deleteFavoriteRoom(String userUuid);

    @Update
    void update(RoomEntity roomEntity);
}