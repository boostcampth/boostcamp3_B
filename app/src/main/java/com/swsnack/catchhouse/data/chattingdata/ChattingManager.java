package com.swsnack.catchhouse.data.chattingdata;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.chattingdata.model.Chatting;
import com.swsnack.catchhouse.data.chattingdata.model.Message;

import java.util.List;
import java.util.Map;

public interface ChattingManager {

    void getChattingRoom(@NonNull String uuid, @NonNull String destinationUuid, @NonNull OnSuccessListener<String> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void getChattingList(@NonNull String uuid, @NonNull OnSuccessListener<List<Chatting>> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void removeChattingListListener();

    void getChatMessage(@NonNull String chatRoomId,
                        @NonNull OnSuccessListener<List<Message>> onSuccessListener,
                        @NonNull OnFailureListener onFailureListener);

    void setChattingRoom(@NonNull Chatting chattingUser, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void setChatMessage(@NonNull String roomUid,
                        @NonNull Message message,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailureListener onFailureListener);
}
