package com.swsnack.catchhouse.repository;

import com.swsnack.catchhouse.repository.chatting.ChattingDataSource;
import com.swsnack.catchhouse.repository.favoriteroom.FavoriteRoomRepository;
import com.swsnack.catchhouse.repository.location.LocationDataSource;
import com.swsnack.catchhouse.repository.recentroom.RecentRoomRepository;
import com.swsnack.catchhouse.repository.room.RoomRepository;
import com.swsnack.catchhouse.repository.searching.SearchingDataSource;
import com.swsnack.catchhouse.repository.user.UserDataSource;

public interface DataSource extends UserDataSource,
        ChattingDataSource,
        LocationDataSource,
        SearchingDataSource,
        RoomRepository,
        FavoriteRoomRepository,
        RecentRoomRepository {
}
