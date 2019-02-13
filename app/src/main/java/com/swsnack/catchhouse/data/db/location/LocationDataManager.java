package com.swsnack.catchhouse.data.db.location;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.swsnack.catchhouse.data.pojo.Address;

public interface LocationDataManager {

    void uploadLocationData(@NonNull String uuid, @NonNull Address address,
                            @NonNull OnSuccessListener<String> onSuccessListener,
                            @NonNull OnFailureListener onFailureListener);
}
