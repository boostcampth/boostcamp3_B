package com.swsnack.catchhouse.data.db.location;

import androidx.annotation.NonNull;

import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.pojo.Address;

public interface LocationDataManager {

    void uploadLocationData(@NonNull String uuid, @NonNull Address address,
                            @NonNull OnSuccessListener<String> onSuccessListener,
                            @NonNull OnFailedListener onFailedListener);
}
