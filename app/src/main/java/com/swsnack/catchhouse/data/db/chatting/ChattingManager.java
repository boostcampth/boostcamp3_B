package com.swsnack.catchhouse.data.db.chatting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.Message;

import java.util.List;

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
