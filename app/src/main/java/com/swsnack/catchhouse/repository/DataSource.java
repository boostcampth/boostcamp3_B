package com.swsnack.catchhouse.repository;

import com.swsnack.catchhouse.repository.chatting.ChattingDataSource;
import com.swsnack.catchhouse.repository.location.LocationDataSource;
import com.swsnack.catchhouse.repository.room.local.FavoriteRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.SellRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.RecentRoomDataSource;
import com.swsnack.catchhouse.repository.room.remote.RemoteRoomDataSource;
import com.swsnack.catchhouse.repository.searching.SearchingDataSource;
import com.swsnack.catchhouse.repository.user.UserDataSource;

public interface DataSource extends UserDataSource,
        ChattingDataSource,
        RemoteRoomDataSource,
        FavoriteRoomDataSource,
        LocationDataSource,
        SearchingDataSource,
        RecentRoomDataSource,
        SellRoomDataSource {

}
