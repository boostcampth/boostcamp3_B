package com.swsnack.catchhouse.data.db.searching.remote;

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
import com.swsnack.catchhouse.data.db.searching.SearchingDataManager;
import com.swsnack.catchhouse.data.pojo.Room;
import com.swsnack.catchhouse.data.pojo.RoomData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_LOCATION;
import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_ROOM;

public class AppSearchingDataManager implements SearchingDataManager {

    private static AppSearchingDataManager INSTANCE;
    private TMapData mTMapData;
    private DatabaseReference mRefLocation;
    private DatabaseReference mRefRoom;
    private GeoFire mGeoFire;
    private int cnt;

    public static synchronized AppSearchingDataManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new AppSearchingDataManager();
        }
        return INSTANCE;
    }

    public AppSearchingDataManager() {
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

    @NonNull
    public Single<List<RoomData>> getNearRoomList(@NonNull double latitude,
                                                            @NonNull double longitude,
                                                            @NonNull double distance) {
        if(cnt>0) {
            Log.v("csh","getNearRoomListFromRemote 이미 진행중인 작업 있음");
            return null;
        }

        GeoQuery geoQuery = mGeoFire.queryAtLocation(new GeoLocation(latitude, longitude), distance);
        List<RoomData> roomDataList = new ArrayList<>();

        return Single.create(subscribe -> geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                cnt++;

                mRefRoom.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.v("csh","데이터 들어옴");
                        Room room = dataSnapshot.getValue(Room.class);

                        Glide
                                .with(AppApplication.getAppContext())
                                .asBitmap()
                                .load(room.getImages().get(0))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        roomDataList.add(new RoomData(room.getPrice(), room.getFrom(), room.getTo(), room.getTitle(), room.getContent(),
                                                room.getImages(), room.getUUID(), room.getAddress(), room.getAddressName(), location.latitude, location.longitude,
                                                room.getSize(), room.isOptionStandard(), room.isOptionGender(), room.isOptionPet(), room.isOptionSmoking(), resource));
                                        Log.v("csh", "데이터 추가됨");
                                        if (--cnt == 0) {
                                            subscribe.onSuccess(roomDataList);
                                        }
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
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                subscribe.onError(new RuntimeException("일치하는 매물 정보가 없습니다."));
            }
        }));
    }

}
