package com.swsnack.catchhouse.data.roomsdata;

import com.skt.Tmap.TMapPOIItem;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public interface RoomsDataSource {

    @NonNull
    Single<List<TMapPOIItem>> getAddressList(String keyword);
}