package com.swsnack.catchhouse.data.source.room.local;

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
    void setSellRoom(SellRoomEntity sellRoomEntity);

    @Delete
    void deleteSellRoom(SellRoomEntity sellRoomEntity);

    @Query("SELECT * FROM my_sell_room WHERE firebaseUuid = :userUuid")
    List<SellRoomEntity> getSellRoom(String userUuid);

    @Query("SELECT * FROM my_sell_room WHERE room_uid = :key AND firebaseUuid = :userUuid")
    SellRoomEntity getSellRoom(String key, String userUuid);

    @Query("DELETE FROM my_sell_room WHERE firebaseUuid = :userUuid")
    void deleteSellRoom(String userUuid);

    @Update
    void updateSellRoom(SellRoomEntity sellRoomEntity);
}
