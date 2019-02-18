package com.swsnack.catchhouse.repository.location;

import androidx.annotation.NonNull;

import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;
import com.swsnack.catchhouse.data.model.Address;

public interface LocationDataSource {

    void uploadLocationData(@NonNull String uuid, @NonNull Address address,
                            @NonNull OnSuccessListener<String> onSuccessListener,
                            @NonNull OnFailedListener onFailedListener);
}
