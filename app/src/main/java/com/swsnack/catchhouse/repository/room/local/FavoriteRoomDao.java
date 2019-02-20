package com.swsnack.catchhouse.repository.room.local;

import com.swsnack.catchhouse.data.entity.FavoriteRoomEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface FavoriteRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setFavoriteRoom(FavoriteRoomEntity favoriteRoomEntity);

    @Delete
    void deleteFavoriteRoom(FavoriteRoomEntity favoriteRoomEntity);

    @Query("SELECT * FROM my_favorite_room WHERE firebaseUuid = :userUuid")
    List<FavoriteRoomEntity> loadFavoriteRoom(String userUuid);

    @Query("SELECT * FROM my_favorite_room WHERE room_uid = :key AND firebaseUuid = :userUuid")
    FavoriteRoomEntity getFavoriteRoom(String key, String userUuid);

    @Query("DELETE FROM my_favorite_room WHERE firebaseUuid = :userUuid")
    void deleteFavoriteRoom(String userUuid);

    @Update
    void update(FavoriteRoomEntity favoriteRoomEntity);
}