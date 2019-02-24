package com.swsnack.catchhouse.repository;

import com.swsnack.catchhouse.data.source.chatting.ChattingDataSource;
import com.swsnack.catchhouse.data.source.location.LocationDataSource;
import com.swsnack.catchhouse.data.source.recentroom.RecentRoomRepository;
import com.swsnack.catchhouse.data.source.searching.SearchingDataSource;
import com.swsnack.catchhouse.data.source.user.UserDataSource;

public interface DataSource extends UserDataSource,
        ChattingDataSource,
        LocationDataSource,
        SearchingDataSource,
        RoomRepository,
        FavoriteRoomRepository,
        RecentRoomRepository {
}
