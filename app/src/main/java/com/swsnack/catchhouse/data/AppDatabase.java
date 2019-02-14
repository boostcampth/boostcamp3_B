package com.swsnack.catchhouse.data;

import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.data.db.room.local.RoomDao;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import static com.swsnack.catchhouse.Constant.DatabaseKey.DATABASE_NAME;

public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance() {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(AppApplication.getAppContext(),
                        AppDatabase.class,
                        DATABASE_NAME).build();
            }
        }
        return INSTANCE;
    }

     public abstract RoomDao getRoomDataAccesser();
}
