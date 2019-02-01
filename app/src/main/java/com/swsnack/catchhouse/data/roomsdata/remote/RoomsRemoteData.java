package com.swsnack.catchhouse.data.roomsdata.remote;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.roomsdata.RoomsDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public class RoomsRemoteData implements RoomsDataSource {
    TMapData mTMapData = new TMapData();

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
        return Single.defer(() -> Single.create(subscribe -> mTMapData.findAllPOI(keyword, new TMapData.FindAllPOIListenerCallback() {

            @Override
            public void onFindAllPOI(@NonNull ArrayList<TMapPOIItem> arrayList) {
                if(arrayList.size() > 0) {
                    subscribe.onSuccess(arrayList);
                } else {
                    subscribe.onError(new RuntimeException("Can't FindAllPOI(No Data)"));
                }
            }
        })));
    }

}