package com.swsnack.catchhouse.data.source.searching;

import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.model.Filter;
import com.swsnack.catchhouse.data.model.Room;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public interface SearchingDataSource {

    @NonNull
    Single<List<TMapPOIItem>> getPOIList(@NonNull String keyword);

    @NonNull
    Single<List<Room>> getNearRoomList(@NonNull Filter filter);
}
