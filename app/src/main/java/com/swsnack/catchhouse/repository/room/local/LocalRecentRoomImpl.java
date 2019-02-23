package com.swsnack.catchhouse.repository.room.local;

import android.os.Build;

import com.swsnack.catchhouse.data.db.AppDataCache;
import com.swsnack.catchhouse.data.model.Room;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class LocalRecentRoomImpl implements AppDataCache<Room>, RecentRoomDataSource {

    private Map<Room, Long> mRecentRoomCache;
    private static LocalRecentRoomImpl INSTANCE;

    public static LocalRecentRoomImpl getInstance() {
        if(INSTANCE == null) {
            synchronized (LocalRecentRoomImpl.class) {
                INSTANCE = new LocalRecentRoomImpl();
            }
        }
        return INSTANCE;
    }

    private LocalRecentRoomImpl() {
        mRecentRoomCache = new HashMap<>(getCacheItemSize());
    }

    @Override
    public int getCacheItemSize() {
        return 5;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setRecentRoom(@NonNull Room room) {
        if(mRecentRoomCache.size() < getCacheItemSize()) {
            mRecentRoomCache.put(room, new Date().getTime());
            return;
        }

        mRecentRoomCache.remove(ascSortByTimeStamp().get(0));
        setRecentRoom(room);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public List<Room> getRecentRoom() {
        return desSortByTimeStamp();
    }

    @Override
    public void deleteRecentRoomList() {
        mRecentRoomCache.clear();
    }

    @Override
    public void deleteRoom(@NonNull Room room) {
        mRecentRoomCache.remove(room);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Room> ascSortByTimeStamp() {
        Comparator<Room> comparator = (r1, r2) ->
                Objects.requireNonNull(mRecentRoomCache.get(r1))
                        .compareTo(Objects.requireNonNull(mRecentRoomCache.get(r2)));

        return mRecentRoomCache.keySet().stream()
                .sorted(comparator).collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<Room> desSortByTimeStamp() {
        Comparator<Room> comparator = (r1, r2) ->
                Objects.requireNonNull(mRecentRoomCache.get(r1))
                        .compareTo(Objects.requireNonNull(mRecentRoomCache.get(r2)));

        Comparator<Room> reverse = comparator.reversed();

        return mRecentRoomCache.keySet().stream()
                .sorted(reverse).collect(Collectors.toList());
    }
}
