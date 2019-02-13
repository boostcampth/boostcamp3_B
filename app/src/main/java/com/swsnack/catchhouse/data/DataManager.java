package com.swsnack.catchhouse.data;

import com.swsnack.catchhouse.data.db.chatting.ChattingManager;
import com.swsnack.catchhouse.data.db.location.LocationDataManager;
import com.swsnack.catchhouse.data.db.room.RoomDataManager;
import com.swsnack.catchhouse.data.db.user.UserDataManager;

public interface DataManager extends  UserDataManager, ChattingManager, RoomDataManager, LocationDataManager {

}
