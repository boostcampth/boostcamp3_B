package com.swsnack.catchhouse.repository.room;

import android.net.Uri;
import android.os.Build;

import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;
import com.swsnack.catchhouse.repository.room.local.FavoriteRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.FavoriteRoomImpl;
import com.swsnack.catchhouse.repository.room.local.LocalRecentRoomImpl;
import com.swsnack.catchhouse.repository.room.local.RecentRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.SellRoomDataSource;
import com.swsnack.catchhouse.repository.room.local.SellRoomImpl;
import com.swsnack.catchhouse.repository.room.remote.RemoteRoomDataSource;
import com.swsnack.catchhouse.repository.room.remote.RoomDataImpl;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class RoomRepository implements RemoteRoomDataSource, FavoriteRoomDataSource, RecentRoomDataSource, SellRoomDataSource {

    private static RoomRepository INSTANCE;
    private FavoriteRoomDataSource mLocalRoomDataManager;
    private RemoteRoomDataSource mRemoteRoomDataSource;
    private RecentRoomDataSource mRecentRoomDataManager;
    private SellRoomDataSource mLocalSellRoomManager;

    public static RoomRepository getInstance() {
        if (INSTANCE == null) {
            synchronized (RoomRepository.class) {
                INSTANCE = new RoomRepository();
            }
        }
        return INSTANCE;
    }

    private RoomRepository() {

        mLocalRoomDataManager = FavoriteRoomImpl.getInstance();
        mRemoteRoomDataSource = RoomDataImpl.getInstance();
        mRecentRoomDataManager = LocalRecentRoomImpl.getInstance();
        mLocalSellRoomManager = SellRoomImpl.getInstance();
    }

    @Override
    public String createKey() {
        return mRemoteRoomDataSource.createKey();
    }

    @Override
    public void uploadRoomImage(@NonNull String uuid, @NonNull List<Uri> imageList, @NonNull OnSuccessListener<List<String>> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mRemoteRoomDataSource.uploadRoomImage(uuid, imageList, onSuccessListener, onFailedListener);
    }

    @Override
    public void setRoom(@NonNull String key, @NonNull Room room, @Nullable OnSuccessListener<Void> onSuccessListener, @Nullable OnFailedListener onFailedListener) {
        mRemoteRoomDataSource.setRoom(key, room, success -> {
                    setSellRoom(room);
                    onSuccessListener.onSuccess(success);
                }
                , onFailedListener);
    }

    @Override
    public void getRoom(@NonNull String key, @Nullable OnSuccessListener<Room> onSuccessListener, @Nullable OnFailedListener onFailedListener) {
        mRemoteRoomDataSource.getRoom(key, onSuccessListener, onFailedListener);
    }

    @Override
    public void delete(@NonNull String key, @NonNull Room room, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailedListener onFailedListener) {
        mRemoteRoomDataSource.delete(key, room, success -> {
                    deleteSellRoom(room);
                    mRecentRoomDataManager.deleteRoom(room);
                    onSuccessListener.onSuccess(success);
                }
                , onFailedListener);
    }

    @Override
    public void setFavoriteRoom(Room room) {
        mLocalRoomDataManager.setFavoriteRoom(room);
    }

    @Override
    public void deleteFavoriteRoom(Room room) {
        mLocalRoomDataManager.deleteFavoriteRoom(room);
    }

    @Override
    public List<Room> getFavoriteRoomList() {
        return mLocalRoomDataManager.getFavoriteRoomList();
    }

    @Override
    public void deleteFavoriteRoom() {
        mLocalRoomDataManager.deleteFavoriteRoom();
    }

    @Override
    public Room getFavoriteRoom(String key) {
        return mLocalRoomDataManager.getFavoriteRoom(key);
    }

    @Override
    public void updateRoom(Room room) {
        mLocalRoomDataManager.updateRoom(room);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setRecentRoom(Room room) {
        mRecentRoomDataManager.setRecentRoom(room);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Room> getRecentRoom() {
        return mRecentRoomDataManager.getRecentRoom();
    }

    @Override
    public void deleteRecentRoomList() {
        mRecentRoomDataManager.deleteRecentRoomList();
    }

    @Override
    public void deleteRoom(Room room) {
        mRecentRoomDataManager.deleteRoom(room);
    }

    @Override
    public void setSellRoom(Room room) {
        mLocalSellRoomManager.setSellRoom(room);
    }

    @Override
    public void deleteSellRoom(Room room) {
        mLocalSellRoomManager.deleteSellRoom(room);
    }

    @Override
    public List<Room> getSellRoomList() {
        return mLocalSellRoomManager.getSellRoomList();
    }

    @Override
    public void deleteSellRoom() {
        mLocalSellRoomManager.deleteSellRoom();
    }

    @Override
    public Room getSellRoom(String key) {
        return mLocalSellRoomManager.getSellRoom(key);
    }

}
