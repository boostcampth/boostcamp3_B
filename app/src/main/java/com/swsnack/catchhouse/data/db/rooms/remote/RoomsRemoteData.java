package com.swsnack.catchhouse.data.db.rooms.remote;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.Constant;
import com.swsnack.catchhouse.data.db.rooms.RoomsDataSource;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class RoomsRemoteData implements RoomsDataSource {
    private TMapData mTMapData = new TMapData();

    private static class RoomsRemoteDataHelper {
        private static final RoomsRemoteData INSTANCE = new RoomsRemoteData();
    }

    public static RoomsRemoteData getInstance() {
        return RoomsRemoteDataHelper.INSTANCE;
    }

    private RoomsRemoteData() {

    }

    @NonNull
    @Override
    public Single<List<TMapPOIItem>> getAddressList(String keyword) {
        return Single.create(subscribe -> mTMapData.findAllPOI(keyword, arrayList -> {
            if(arrayList.size() > 0) {
                subscribe.onSuccess(arrayList);
            } else {
                subscribe.onError(new RuntimeException(Constant.MSG_ERROR_GET_ADDRESS));
            }
        }));
    }
}

