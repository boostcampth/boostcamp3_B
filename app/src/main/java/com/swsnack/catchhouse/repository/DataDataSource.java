package com.swsnack.catchhouse.repository;

import com.swsnack.catchhouse.repository.chatting.ChattingDataSource;
import com.swsnack.catchhouse.repository.location.LocationDataManager;
import com.swsnack.catchhouse.repository.room.local.FavoriteRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.RecentRoomDataSource;
import com.swsnack.catchhouse.repository.room.remote.RoomDataManager;
import com.swsnack.catchhouse.repository.searching.SearchingDataManager;
import com.swsnack.catchhouse.repository.user.UserDataManager;

public interface DataDataSource extends UserDataManager,
        ChattingDataSource,
        RoomDataManager,
        FavoriteRoomDataSource,
        LocationDataManager,
        SearchingDataManager,
        RecentRoomDataSource {

}
