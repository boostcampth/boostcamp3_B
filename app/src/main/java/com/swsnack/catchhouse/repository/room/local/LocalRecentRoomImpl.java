package com.swsnack.catchhouse.repository.room.local;

import com.swsnack.catchhouse.data.db.AppDataCache;
import com.swsnack.catchhouse.data.model.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LocalRecentRoomImpl implements RecentRoomDataSource {

    private HashMap<Room, Long> mRecentRoomCache;
    private static LocalRecentRoomImpl INSTANCE;

    public static LocalRecentRoomImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (LocalRecentRoomImpl.class) {
                INSTANCE = new LocalRecentRoomImpl();
            }
        }
        return INSTANCE;
    }

    private LocalRecentRoomImpl() {
        mRecentRoomCache = AppDataCache.getInstance().getRecentRoomCache();
    }

    @Override
    public void setRecentRoom(Room room) {
        if (mRecentRoomCache.size() < 5) {
            mRecentRoomCache.put(room, new Date().getTime());
            return;
        }
        List<Room> sortingList = sortByTimeStampRoom();
        mRecentRoomCache.remove(sortingList.get(0));
        setRecentRoom(room);
    }


    @Override
    public List<Room> getRecentRoom() {
        List<Room> reversedSortedRoom = sortByTimeStampRoom();
        Collections.reverse(reversedSortedRoom);
        return reversedSortedRoom;
    }

    @Override
    public void deleteRecentRoomList() {
        mRecentRoomCache.clear();
    }

    private List<Room> sortByTimeStampRoom() {
        List<Room> sortingList = new ArrayList<>(mRecentRoomCache.keySet());
        Collections.sort(sortingList, (oldRoom, newRoom) ->
                Objects.requireNonNull(mRecentRoomCache.get(oldRoom)).compareTo(Objects.requireNonNull(mRecentRoomCache.get(newRoom))));
        return sortingList;
    }
}
