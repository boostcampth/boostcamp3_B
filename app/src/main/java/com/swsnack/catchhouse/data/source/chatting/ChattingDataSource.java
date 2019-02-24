package com.swsnack.catchhouse.data.source.chatting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.Message;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;

import java.util.List;

public interface ChattingDataSource {

    void getChattingRoom(@NonNull String destinationUuid,
                         @NonNull OnSuccessListener<String> onSuccessListener,
                         @NonNull OnFailedListener onFailedListener);

    void listeningChattingListChanged(@NonNull OnSuccessListener<List<Chatting>> onSuccessListener,
                                      @NonNull OnFailedListener onFailedListener);

    void cancelObservingChattingList();

    void listeningChatMessageChanged(@NonNull String chatRoomId,
                                     @NonNull OnSuccessListener<List<Message>> onSuccessListener,
                                     @NonNull OnFailedListener onFailedListener);

    void cancelMessageModelObserving();

    void setChattingRoom(@NonNull String destinationUuid,
                         @NonNull OnSuccessListener<String> onSuccessListener,
                         @NonNull OnFailedListener onFailedListener);

    void setChatMessage(int messagesLength,
                        @Nullable String roomUid,
                        @NonNull String destinationUid,
                        @NonNull String content,
                        @NonNull OnSuccessListener<String> onSuccessListener,
                        @NonNull OnFailedListener onFailedListener);

}
