package com.swsnack.catchhouse.data.roomsdata;

import android.support.annotation.NonNull;

import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.roomsdata.remote.RoomsRemoteData;

import java.util.List;

import io.reactivex.Single;

public class RoomsRepository {

    private RoomsRemoteData mRoomsRemote;

    public static RoomsRepository getInstance() {
        return RepositoryHelper.INSTANCE;
    }

    private static class RepositoryHelper {
        private static final RoomsRepository INSTANCE = new RoomsRepository();
    }

    private RoomsRepository() { mRoomsRemote = RoomsRemoteData.getInstance(); }

    @NonNull
    public Single<List<TMapPOIItem>> getPOIFromRemote(String keyword) {
        return mRoomsRemote.getAddressList(keyword);
    }

}
