package com.swsnack.catchhouse.data.source.searching.remote;

import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.Constant;
import com.swsnack.catchhouse.data.model.Filter;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.data.source.searching.SearchingDataSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_LOCATION;
import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_ROOM;

public class SearchingDataImpl implements SearchingDataSource {

    private static SearchingDataImpl INSTANCE;
    private TMapData mTMapData;
    private DatabaseReference mRefLocation;
    private DatabaseReference mRefRoom;
    private GeoFire mGeoFire;
    private int cnt;
    private boolean isWorking;

    public static synchronized SearchingDataImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SearchingDataImpl();
        }
        return INSTANCE;
    }

    private SearchingDataImpl() {
        mTMapData = new TMapData();
        mRefLocation = FirebaseDatabase.getInstance().getReference().child(DB_LOCATION);
        mRefRoom = FirebaseDatabase.getInstance().getReference().child(DB_ROOM);
        mGeoFire = new GeoFire(mRefLocation);
        cnt = 0;
        isWorking = false;
    }

    @NonNull
    public Single<List<TMapPOIItem>> getPOIList(@NonNull String keyword) {
        return Single.create(subscribe -> mTMapData.findAllPOI(keyword, arrayList -> {
            if (arrayList.size() > 0) {
                subscribe.onSuccess(arrayList);
            } else {
                subscribe.onError(new RuntimeException(Constant.MSG_ERROR_GET_ADDRESS));
            }
        }));
    }

    private boolean isCorrect(Filter filter, Room room) {

        if (room.isDeleted() == true) {
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        if (filter.getDateFrom() != null && filter.getDateTo() != null) {
            try {
                Date dateRoomFrom = formatter.parse(room.getFrom());
                Date dateRoomTo = formatter.parse(room.getTo());
                Date dateFilterFrom = formatter.parse(filter.getDateFrom());
                Date dateFilterTo = formatter.parse(filter.getDateTo());
                if (dateFilterFrom.getTime() > dateRoomFrom.getTime() || dateFilterTo.getTime() < dateRoomTo.getTime()) {
                    return false;
                }
            } catch (Exception e) {
            }
        }



/*
        if (filter.getPriceFrom() != null && filter.getPriceTo() != null) {
            if (filter.getPriceFrom() != "" && filter.getPriceTo() != "") {
                if (Integer.parseInt(room.getPrice()) < Integer.parseInt(filter.getPriceFrom().replaceAll(",", "")) && Integer.parseInt(room.getPrice()) > Integer.parseInt(filter.getPriceTo().replaceAll(",", ""))) {

                    return false;
                }
            }
        }*/

        if(filter.getPriceFrom() != null) {
            if(filter.getPriceFrom() != "") {
                if(Integer.parseInt(filter.getPriceFrom().replaceAll(",", "")) > Integer.parseInt(room.getPrice())) {
                    return false;
                }
            }
        }

        if(filter.getPriceTo() != null) {
            if(filter.getPriceTo() != "") {
                if(Integer.parseInt(filter.getPriceTo().replaceAll(",", "")) < Integer.parseInt(room.getPrice())) {
                    return false;
                }
            }
        }

        if (filter.isOptionGender() == true && room.isOptionGender() != filter.isOptionGender()) {
            return false;
        }
        if (filter.isOptionPet() == true && room.isOptionPet() != filter.isOptionPet()) {
            return false;
        }
        if (filter.isOptionSmoking() == true && room.isOptionSmoking() != filter.isOptionSmoking()) {
            return false;
        }
        if (filter.isOptionStandard() == true && room.isOptionStandard() != filter.isOptionStandard()) {
            return false;
        }

        return true;

    }

    private void finishSearch() {
        cnt = 0;
        isWorking = false;
    }

    @NonNull
    public Single<List<Room>> getNearRoomList(@NonNull Filter filter) {
        if (cnt > 0 || isWorking == true) {
            Log.v("csh", "getNearRoomListFromRemote 이미 진행중인 작업 있음");
            return Single.error(new RuntimeException("이미 작업중입니다."));
        }
        isWorking = true;

        GeoQuery geoQuery = mGeoFire.queryAtLocation(new GeoLocation(filter.getLatitude(), filter.getLongitude()), filter.getDistance());
        List<Room> roomList = new ArrayList<>();

        return Single.create(subscribe -> geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                cnt++;

                mRefRoom.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Room room = dataSnapshot.getValue(Room.class);
                        Log.d("여기", dataSnapshot.getKey());
                        room.setKey(dataSnapshot.getKey());
                        room.setLatitude(location.latitude);
                        room.setLongitude(location.longitude);
                        if (isCorrect(filter, room) == true) {


                            roomList.add(room);
                        } else {
                            Log.v("csh", "필터 엘스");
                        }

                        if (--cnt <= 0) {
                            finishSearch();
                            if (roomList.size() == 0) {
                                subscribe.onError(new RuntimeException("일치하는 매물 정보가 없습니다."));
                            }
                            subscribe.onSuccess(roomList);
                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
                        finishSearch();
                        subscribe.onError(new RuntimeException("Can't read  a data"));
                    }
                });
            }

            @Override
            public void onKeyExited(String key) {
                finishSearch();
                subscribe.onError(new RuntimeException("Data is deleted"));
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                finishSearch();
                subscribe.onError(new RuntimeException("Data is moved"));
            }

            @Override
            public void onGeoQueryReady() {
                Log.v("csh", "onGeoQueryReady");
                if (cnt == 0) {
                    finishSearch();
                    subscribe.onError(new RuntimeException("일치하는 매물 정보가 없습니다."));
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                finishSearch();
                subscribe.onError(new RuntimeException("일치하는 매물 정보가 없습니다."));
            }
        }));
    }
}
