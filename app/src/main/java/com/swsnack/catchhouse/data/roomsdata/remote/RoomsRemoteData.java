package com.swsnack.catchhouse.data.roomsdata.remote;

import com.swsnack.catchhouse.data.roomsdata.RoomsDataSource;

public class RoomsRemoteData implements RoomsDataSource {

    private static class RoomsRemoteDataHelper {
        private static final RoomsRemoteData INSTANCE = new RoomsRemoteData();
    }

    public static RoomsRemoteData getInstance() {
        return RoomsRemoteDataHelper.INSTANCE;
    }

    private RoomsRemoteData() {

    }


}
