package com.swsnack.catchhouse.data.db.searching;

import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.data.pojo.Filter;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

public interface SearchingDataManager {

    @NonNull
    Single<List<TMapPOIItem>> getPOIList(@NonNull String keyword);

    @NonNull
    Single<List<Room>> getNearRoomList(@NonNull Filter filter);
}
