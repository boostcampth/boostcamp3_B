package com.swsnack.catchhouse.data.source.location.remote;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.FirebaseDatabase;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;
import com.swsnack.catchhouse.data.source.location.LocationDataSource;

import androidx.annotation.NonNull;

import static com.swsnack.catchhouse.Constant.FirebaseKey.DB_LOCATION;

public class RemoteLocationImpl implements LocationDataSource {

    private GeoFire gf;

    private static RemoteLocationImpl INSTANCE;

    public static synchronized RemoteLocationImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteLocationImpl();
        }
        return INSTANCE;
    }

    private RemoteLocationImpl() {
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
