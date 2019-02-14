package com.swsnack.catchhouse.data.db.room.local;

import com.swsnack.catchhouse.data.AppDatabase;
import com.swsnack.catchhouse.data.db.room.RoomDataManager;
import com.swsnack.catchhouse.data.entity.RoomEntity;
import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.pojo.Room;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class AppRoomLocalDataManager implements RoomDataManager {

    private static AppRoomLocalDataManager INSTANCE;
    private RoomDao mRoomDao;

    public static AppRoomLocalDataManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AppRoomLocalDataManager.class) {
                INSTANCE = new AppRoomLocalDataManager();
            }
        }
        return INSTANCE;
    }

    private AppRoomLocalDataManager() {
        mRoomDao = AppDatabase.getInstance().getRoomDataAccesser();
    }

//    @Override
//    public void setFavoriteRoom(RoomEntity roomEntity) {
//        new RoomLocalDataHelper.AsyncSetFavoriteRoom(mRoomDao).execute(roomEntity);
//    }
//
//    @Override
//    public void deleteFavoriteRoom(RoomEntity roomEntity) {
//        new RoomLocalDataHelper.AsyncDeleteFavoriteRoom(mRoomDao).execute(roomEntity);
//
//    }
//
//    @Override
//    public LiveData<List<RoomEntity>> getFavoriteRoom() {
//        try {
//            return new RoomLocalDataHelper.AsyncLoadFavoriteRoom(mRoomDao).execute().get();
//        } catch (ExecutionException | InterruptedException e) {
//            return null;
//        }
//    }

    @Override
    public void createKey(@NonNull OnSuccessListener<String> onSuccessListener, @NonNull OnFailedListener onFailedListener) {

    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<byte[]> imageList, @NonNull OnSuccessListener<List<String>> onSuccessListener, @NonNull OnFailedListener onFailedListener) {

    }

    @Override
    public void setRoom(@NonNull String key, @NonNull Room room, @Nullable OnSuccessListener<Void> onSuccessListener, @Nullable OnFailedListener onFailedListener) {
//        new RoomLocalDataHelper.AsyncSetFavoriteRoom(mRoomDao).execute(roomEntity);
    }

    @Override
    public void getRoom(@NonNull String key, @Nullable OnSuccessListener<Room> onSuccessListener, @Nullable OnFailedListener onFailedListener) {

    }
}
