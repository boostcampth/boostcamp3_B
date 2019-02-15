package com.swsnack.catchhouse.data.db.room.local;

import com.swsnack.catchhouse.data.entity.RoomEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import static com.swsnack.catchhouse.Constant.DatabaseKey.ROOM_TABLE;

@Dao
public interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setFavoriteRoom(RoomEntity roomEntity);

    @Delete
    void deleteFavoriteRoom(RoomEntity roomEntity);

    @Query("SELECT * FROM " + ROOM_TABLE)
    List<RoomEntity> loadFavoriteRoom();

    @Query("SELECT * FROM my_favorite_room WHERE room_uid = :key")
    RoomEntity getFavoriteRoom(String key);
}