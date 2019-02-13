package com.swsnack.catchhouse.data.db.rooms;

import android.support.annotation.NonNull;

import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.db.rooms.remote.RoomsRemoteData;
import com.swsnack.catchhouse.data.pojo.Filter;
import com.swsnack.catchhouse.data.pojo.RoomData;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class RoomsRepository {

    private RoomsRemoteData mRoomsRemote;

    public static RoomsRepository getInstance() {
        return RepositoryHelper.INSTANCE;
    }

    private static class RepositoryHelper {
        private static final RoomsRepository INSTANCE = new RoomsRepository();
    }

    private RoomsRepository() {
        mRoomsRemote = RoomsRemoteData.getInstance();
    }

    @NonNull
    public Single<List<TMapPOIItem>> getPOIFromRepository(String keyword) {
        return mRoomsRemote.getPOIFromRemote(keyword);
    }

    @NonNull
    public Single<List<RoomData>> getNearRoomListFromRepository(double latitude, double longitude, double distance) {
        return mRoomsRemote.getNearRoomListFromRemote(latitude, longitude, distance);
    }

}
