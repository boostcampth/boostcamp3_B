package com.swsnack.catchhouse.data.source.location;

import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import androidx.annotation.NonNull;

public interface LocationDataSource {

    void uploadLocationData(@NonNull String uuid, @NonNull Address address,
                            @NonNull OnSuccessListener<String> onSuccessListener,
                            @NonNull OnFailedListener onFailedListener);
}
