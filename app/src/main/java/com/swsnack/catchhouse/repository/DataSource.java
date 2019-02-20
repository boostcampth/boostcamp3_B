package com.swsnack.catchhouse.repository;

import com.swsnack.catchhouse.repository.chatting.ChattingDataSource;
import com.swsnack.catchhouse.repository.location.LocationDataSource;
import com.swsnack.catchhouse.repository.room.local.FavoriteRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.RecentRoomDataSource;
import com.swsnack.catchhouse.repository.room.remote.RoomDataSource;
import com.swsnack.catchhouse.repository.searching.SearchingDataSource;
import com.swsnack.catchhouse.repository.user.UserDataSource;

public interface DataSource extends UserDataSource,
        ChattingDataSource,
        RoomDataSource,
        FavoriteRoomDataSource,
        LocationDataSource,
        SearchingDataSource,
        RecentRoomDataSource {

}
