package com.swsnack.catchhouse.data.locationdata.remote;

import android.support.annotation.NonNull;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.swsnack.catchhouse.data.locationdata.LocationDataManager;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;

import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_LOCATION;

public class AppLocationDataManager implements LocationDataManager {

    private GeoFire gf;

    private static AppLocationDataManager INSTANCE;

    public static synchronized AppLocationDataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AppLocationDataManager();
        }
        return INSTANCE;
    }

    private AppLocationDataManager() {
        gf = new GeoFire(FirebaseDatabase.getInstance().getReference().child(DB_LOCATION));
    }

    @Override
    public void uploadLocationData(@NonNull String uuid, @NonNull Address address,
                                   @NonNull OnSuccessListener<String> onSuccessListener,
                                   @NonNull OnFailureListener onFailureListener) {

        GeoLocation geoLocation = new GeoLocation(address.getLatitude(), address.getLongitude());

        gf.setLocation(uuid, geoLocation, (key, error) -> {
                    if (error == null) {
                        onSuccessListener.onSuccess(key);
                    } else {
                        onFailureListener.onFailure(new RuntimeException("error"));
                    }
                }
        );

    }
}
