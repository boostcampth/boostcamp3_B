package com.swsnack.catchhouse.data;

import com.swsnack.catchhouse.data.db.chatting.ChattingManager;
import com.swsnack.catchhouse.data.db.location.LocationDataManager;
import com.swsnack.catchhouse.data.db.room.remote.RoomDataManager;
import com.swsnack.catchhouse.data.db.searching.SearchingDataManager;
import com.swsnack.catchhouse.data.db.user.UserDataManager;

public interface DataManager extends  UserDataManager, ChattingManager, RoomDataManager, LocationDataManager, SearchingDataManager {

}
