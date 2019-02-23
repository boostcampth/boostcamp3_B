package com.swsnack.catchhouse.repository.room.remote;

import android.net.Uri;

import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface RemoteRoomDataSource {

    String createKey();

    void uploadRoomImage(@NonNull String uuid, @NonNull List<Uri> imageList,
                         @NonNull OnSuccessListener<List<String>> onSuccessListener,
                         @NonNull OnFailedListener onFailedListener);

    void setRoom(@NonNull Room room,
                 @NonNull OnSuccessListener<Void> onSuccessListener,
                 @NonNull OnFailedListener onFailedListener);

    void getRoom(@NonNull String key,
                 @NonNull OnSuccessListener<Room> onSuccessListener,
                 @NonNull OnFailedListener onFailedListener);

    //FIXME 용현's 가 추가함. 확인 부탁이요. Key는 필요 없을 것 같아요. 내부에서 동작하게 하면 Room객체만 던져줘도 될것 같습니다. 나중에 수정 부탁드려요
    void delete(@NonNull Room room,
                @NonNull OnSuccessListener<Void> onSuccessListener,
                @NonNull OnFailedListener onFailedListener);
}
