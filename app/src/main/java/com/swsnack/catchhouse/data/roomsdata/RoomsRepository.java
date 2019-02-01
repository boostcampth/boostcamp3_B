package com.swsnack.catchhouse.data.roomsdata;

import com.swsnack.catchhouse.data.roomsdata.remote.RoomsRemoteData;

public class RoomsRepository {

    private RoomsRemoteData mRoomsRemote;

    public static RoomsRepository getInstance() {
        return RepositoryHelper.INSTANCE;
    }

    private static class RepositoryHelper {
        private static final RoomsRepository INSTANCE = new RoomsRepository();
    }

    private RoomsRepository() { mRoomsRemote = RoomsRemoteData.getInstance(); }



}
