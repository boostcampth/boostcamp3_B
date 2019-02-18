package com.swsnack.catchhouse.repository;

import com.swsnack.catchhouse.repository.chatting.ChattingManager;
import com.swsnack.catchhouse.repository.location.LocationDataManager;
import com.swsnack.catchhouse.repository.room.local.FavoriteRoomManager;
import com.swsnack.catchhouse.repository.room.local.RecentRoomManager;
import com.swsnack.catchhouse.repository.room.remote.RoomDataManager;
import com.swsnack.catchhouse.repository.searching.SearchingDataManager;
import com.swsnack.catchhouse.repository.user.UserDataManager;

public interface DataManager extends UserDataManager,
        ChattingManager,
        RoomDataManager,
        FavoriteRoomManager,
        LocationDataManager,
        SearchingDataManager,
        RecentRoomManager {

}
