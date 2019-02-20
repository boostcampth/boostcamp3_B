package com.swsnack.catchhouse.data.db;

import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.repository.room.local.RoomDao;
import com.swsnack.catchhouse.repository.room.local.TypeConverter;
import com.swsnack.catchhouse.data.entity.FavoriteRoomEntity;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import static com.swsnack.catchhouse.Constant.DatabaseKey.DATABASE_NAME;

@Database(entities = {FavoriteRoomEntity.class}, version =  1, exportSchema = false)
@TypeConverters({TypeConverter.class})
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

     public abstract RoomDao getRoomDataAccessor();
}
