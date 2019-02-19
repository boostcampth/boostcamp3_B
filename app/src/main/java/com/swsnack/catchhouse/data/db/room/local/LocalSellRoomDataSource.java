package com.swsnack.catchhouse.data.db.room.local;

import com.swsnack.catchhouse.data.entity.SellRoomEntity;

import java.util.List;

public interface LocalSellRoomDataSource {

    void setSellRoom(SellRoomEntity sellRoomEntity);

    void deleteSellRoom(SellRoomEntity sellRoomEntity);

    List<SellRoomEntity> getSellRoomList();

    void deleteSellRoom();

    SellRoomEntity getSellRoom(String key);

    void updateRoom(SellRoomEntity sellRoomEntity);
}
