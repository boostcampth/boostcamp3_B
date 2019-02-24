package com.swsnack.catchhouse.data.source.recentroom;

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

public class RecentRoomDao implements AppDataCache<Room> {

    private Map<Room, Long> mRecentRoomCache;
    private static RecentRoomDao INSTANCE;

    public static RecentRoomDao getInstance() {
        if (INSTANCE == null) {
            synchronized (RecentRoomDao.class) {
                INSTANCE = new RecentRoomDao();
            }
        }
        return INSTANCE;
    }

    private RecentRoomDao() {
        mRecentRoomCache = new HashMap<>(getCacheItemSize());
    }

    @Override
    public int getCacheItemSize() {
        return 5;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setRecentRoom(@NonNull Room room) {
        if (mRecentRoomCache.size() < getCacheItemSize()) {
            mRecentRoomCache.put(room, new Date().getTime());
            return;
        }

        mRecentRoomCache.remove(ascSortByTimeStamp().get(0));
        setRecentRoom(room);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    public List<Room> getRecentRoom() {
        return desSortByTimeStamp();
    }

    public void deleteRecentRoomList() {
        mRecentRoomCache.clear();
    }

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
