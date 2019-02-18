package com.swsnack.catchhouse.repository.location.remote;

import androidx.annotation.NonNull;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.FirebaseDatabase;
import com.swsnack.catchhouse.repository.location.LocationDataManager;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;
import com.swsnack.catchhouse.data.model.Address;

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
                                   @NonNull OnFailedListener onFailedListener) {

        GeoLocation geoLocation = new GeoLocation(address.getLatitude(), address.getLongitude());

        gf.setLocation(uuid, geoLocation, (key, error) -> {
                    if (error == null) {
                        onSuccessListener.onSuccess(key);
                    } else {
                        onFailedListener.onFailed(new RuntimeException("error"));
                    }
                }
        );
    }
}
