package com.swsnack.catchhouse.data;

import com.swsnack.catchhouse.data.db.chatting.ChattingManager;
import com.swsnack.catchhouse.data.db.location.LocationDataManager;
import com.swsnack.catchhouse.data.db.room.local.LocalRoomDataSource;
import com.swsnack.catchhouse.data.db.room.local.LocalSellRoomDataSource;
import com.swsnack.catchhouse.data.db.room.local.RecentRoomManager;
import com.swsnack.catchhouse.data.db.room.remote.RoomDataManager;
import com.swsnack.catchhouse.data.db.searching.SearchingDataManager;
import com.swsnack.catchhouse.data.db.user.UserDataManager;

public interface DataManager extends UserDataManager,
        ChattingManager,
        RoomDataManager,
        LocalRoomDataSource,
        LocationDataManager,
        SearchingDataManager,
        RecentRoomManager,
        LocalSellRoomDataSource {

}
