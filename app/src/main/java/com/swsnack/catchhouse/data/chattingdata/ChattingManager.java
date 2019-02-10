package com.swsnack.catchhouse.data.chattingdata;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.chattingdata.pojo.Chatting;
import com.swsnack.catchhouse.data.chattingdata.pojo.Message;

import java.util.List;

public interface ChattingManager {

    void getChattingRoom(@NonNull String uuid, @NonNull String destinationUuid, @NonNull OnSuccessListener<String> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void getChattingList(@NonNull String uuid, @NonNull OnSuccessListener<List<Chatting>> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void getChatMessage(@NonNull String chatRoomId, @NonNull ValueEventListener valueEventListener);

    void setChattingRoom(@NonNull Chatting chattingUser, @NonNull OnSuccessListener<Void> onSuccessListener, @NonNull OnFailureListener onFailureListener);

    void setChatMessage(@NonNull Message message, @NonNull ValueEventListener valueEventListener);
}
