package com.swsnack.catchhouse.data.db;

import com.swsnack.catchhouse.data.model.Room;

import java.util.HashMap;

public class AppDataCache {

    private static AppDataCache INSTANCE;
    private HashMap<Room, Long> mRecentRoomCache;

    public static AppDataCache getInstance() {
        if(INSTANCE == null) {
            synchronized (AppDataCache.class) {
                INSTANCE = new AppDataCache();
            }
        }
        return INSTANCE;
    }

    private AppDataCache() {
        mRecentRoomCache = new HashMap<>();
    }

    public HashMap<Room, Long> getRecentRoomCache() {
        return mRecentRoomCache;
    }
}
