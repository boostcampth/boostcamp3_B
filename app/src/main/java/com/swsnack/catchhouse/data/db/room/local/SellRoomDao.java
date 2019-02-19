package com.swsnack.catchhouse.data.db.room.local;

import com.swsnack.catchhouse.data.entity.SellRoomEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SellRoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void setFavoriteRoom(SellRoomEntity sellRoomEntity);

    @Delete
    void deleteFavoriteRoom(SellRoomEntity sellRoomEntity);

    @Query("SELECT * FROM my_sell_room WHERE firebaseUuid = :userUuid")
    List<SellRoomEntity> loadFavoriteRoom(String userUuid);

    @Query("SELECT * FROM my_sell_room WHERE room_uid = :key AND firebaseUuid = :userUuid")
    SellRoomEntity getFavoriteRoom(String key, String userUuid);

    @Query("DELETE FROM my_sell_room WHERE firebaseUuid = :userUuid")
    void deleteFavoriteRoom(String userUuid);

    @Update
    void update(SellRoomEntity sellRoomEntity);
}
