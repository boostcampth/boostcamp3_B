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

    void getChattingRoom(@NonNull String uuid,
                         @NonNull String destinationUuid,
                         @NonNull OnSuccessListener<String> onSuccessListener,
                         @NonNull OnFailureListener onFailureListener);

    void getChattingList(@NonNull String uuid,
                         @NonNull OnSuccessListener<List<Chatting>> onSuccessListener,
                         @NonNull OnFailureListener onFailureListener);

    void cancelChattingModelObserving();

    void getChatMessage(@NonNull String chatRoomId,
                        @NonNull OnSuccessListener<List<Message>> onSuccessListener,
                        @NonNull OnFailureListener onFailureListener);

    void cancelMessageModelObserving();

    void setChattingRoom(@NonNull Chatting chatting,
                         @NonNull OnSuccessListener<Void> onSuccessListener,
                         @NonNull OnFailureListener onFailureListener);

    void setChatMessage(int messagesLength,
                        @NonNull String roomUid,
                        @NonNull String content,
                        @NonNull OnSuccessListener<Void> onSuccessListener,
                        @NonNull OnFailureListener onFailureListener);
}
