package com.swsnack.catchhouse.data.chattingdata;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.data.chattingdata.model.Chatting;
import com.swsnack.catchhouse.data.chattingdata.model.Message;

import java.util.List;
import java.util.Map;

public interface ChattingManager {

    void getChattingRoom(@NonNull String destinationUuid,
                         @NonNull OnSuccessListener<String> onSuccessListener,
                         @NonNull OnFailureListener onFailureListener);

    void getChattingList(@NonNull OnSuccessListener<List<Chatting>> onSuccessListener,
                         @NonNull OnFailureListener onFailureListener);

    void cancelChattingModelObserving();

    void listeningForChangedChatMessage(@NonNull String chatRoomId,
                                        @NonNull OnSuccessListener<List<Message>> onSuccessListener,
                                        @NonNull OnFailureListener onFailureListener);

    void cancelMessageModelObserving();

    void setChattingRoom(@NonNull String destinationUuid,
                         @NonNull OnSuccessListener<String> onSuccessListener,
                         @NonNull OnFailureListener onFailureListener);

    void setChatMessage(int messagesLength,
                        @Nullable String roomUid,
                        @NonNull String destinationUid,
                        @NonNull String content,
                        @NonNull OnSuccessListener<String> onSuccessListener,
                        @NonNull OnFailureListener onFailureListener);

}
