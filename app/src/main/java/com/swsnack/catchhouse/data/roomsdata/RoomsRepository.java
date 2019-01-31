package com.swsnack.catchhouse.data.roomsdata;

import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;
import com.swsnack.catchhouse.data.roomsdata.remote.RoomsRemoteData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

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
