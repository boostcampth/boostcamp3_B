package com.swsnack.catchhouse.repository.searching.remote;

import android.graphics.Bitmap;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.Constant;
import com.swsnack.catchhouse.data.model.Filter;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.repository.searching.SearchingDataSource;

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

    public static synchronized SearchingDataImpl getInstance() {
        if(INSTANCE == null) {
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

        if(room.isDeleted()==true) {
            return false;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        if(filter.getDateFrom() != null && filter.getDateTo() != null) {
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

        Log.v("csh","filter.getPriceFrom()"+filter.getPriceFrom());
        if(filter.getPriceFrom() != null && filter.getPriceTo() != null) {
            if(filter.getPriceFrom() != "" && filter.getPriceTo() != "") {
                if (Integer.parseInt(room.getPrice()) < Integer.parseInt(filter.getPriceFrom().replaceAll(",","")) && Integer.parseInt(room.getTo()) > Integer.parseInt(filter.getPriceTo().replaceAll(",",""))) {
                    return false;
                }
            }
        }

        if(room.isOptionGender() != filter.isOptionGender()) {
            return false;
        }
        if(room.isOptionPet() != filter.isOptionPet()) {
            return false;
        }
        if(room.isOptionSmoking() != filter.isOptionSmoking()) {
            return false;
        }
        if(room.isOptionStandard() != filter.isOptionStandard()) {
            return false;
        }

        return true;

    }

    @NonNull
    public Single<List<Room>> getNearRoomList(@NonNull Filter filter) {
        if(cnt>0) {
            Log.v("csh","getNearRoomListFromRemote 이미 진행중인 작업 있음");
            return null;
        }

        GeoQuery geoQuery = mGeoFire.queryAtLocation(new GeoLocation(filter.getLatitude(), filter.getLongitude()), filter.getDistance());
        List<Room> roomList = new ArrayList<>();

        return Single.create(subscribe -> geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                cnt++;

                mRefRoom.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.v("csh","데이터 들어옴");
                        Room room = dataSnapshot.getValue(Room.class);
                        room.setKey(dataSnapshot.getKey());
                        room.setLatitude(location.latitude);
                        room.setLongitude(location.longitude);

                        Glide
                                .with(AppApplication.getAppContext())
                                .asBitmap()
                                .load(room.getImages().get(0))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                                        room.setImage(resource);

                                        if(isCorrect(filter, room) == true) {
                                            roomList.add(room);
                                        }

                                        Log.v("csh", "데이터 추가됨");
                                        if (--cnt == 0) {
                                            subscribe.onSuccess(roomList);
                                        }

                                        /*

                                        roomDataList.add(new RoomData(dataSnapshot.getKey(), room.getPrice(), room.getFrom(), room.getTo(), room.getTitle(), room.getContent(),
                                                room.getImages(), room.getUuid(), room.getAddress(), room.getAddressName(), location.latitude, location.longitude,
                                                room.getSize(), room.isOptionStandard(), room.isOptionGender(), room.isOptionPet(), room.isOptionSmoking(), resource));
                                        Log.v("csh", "데이터 추가됨");
                                        if (--cnt == 0) {
                                            subscribe.onSuccess(roomDataList);
                                        }*/
                                    }

                                });
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {
                        subscribe.onError(new RuntimeException("Can't read  a data"));
                    }
                });
            }

            @Override
            public void onKeyExited(String key) {
                Log.v("csh","onKeyExited");
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                Log.v("csh","onKeyMoved");
            }

            @Override
            public void onGeoQueryReady() {
                Log.v("csh","onGeoQueryReady");
                if(cnt==0) {
                    subscribe.onError(new RuntimeException("No Data"));
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                subscribe.onError(new RuntimeException("일치하는 매물 정보가 없습니다."));
            }
        }));
    }

}
