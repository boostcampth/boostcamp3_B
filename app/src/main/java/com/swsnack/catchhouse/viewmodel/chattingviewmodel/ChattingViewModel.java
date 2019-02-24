package com.swsnack.catchhouse.viewmodel.chattingviewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.DataSource;
import com.swsnack.catchhouse.data.model.Chatting;
import com.swsnack.catchhouse.data.model.Message;
import com.swsnack.catchhouse.data.model.User;
import com.swsnack.catchhouse.util.StringUtil;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.List;
import java.util.Map;

import static com.swsnack.catchhouse.Constant.SuccessKey.SEND_MESSAGE_SUCCESS;

public class ChattingViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private ViewModelListener mListener;
    private Boolean mIsListeningForChangedMessage;
    private String mRoomUid;
    private MutableLiveData<List<Chatting>> mChattingList;
    private MutableLiveData<List<Message>> mMessageList;
    private MutableLiveData<User> mDestinationUserData;

    ChattingViewModel(DataSource dataManager, APIManager apiManager, ViewModelListener bottomNavListener) {
        super(dataManager, apiManager);
        this.mAppContext = AppApplication.getAppContext();
        this.mListener = bottomNavListener;
        this.mIsListeningForChangedMessage = false;
        this.mChattingList = new MutableLiveData<>();
        this.mMessageList = new MutableLiveData<>();
        this.mDestinationUserData = new MutableLiveData<>();
    }

    public void getChattingRoomList() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        getDataManager()
                .listeningChattingListChanged(list -> mChattingList.setValue(list),
                        error -> mListener.onError(StringUtil.getStringFromResource(R.string.snack_database_exception)));
    }

    public void cancelChattingListChangingListening() {
        getDataManager()
                .cancelObservingChattingList();
    }

    public void getUser(Map<String, Boolean> users, OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener) {
        for (String uuid :users.keySet()) {
            if (!uuid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                getDataManager()
                        .getUserFromSingleSnapShot(uuid,
                                user -> {
                                    if (user != null) {
                                        user.setUuid(uuid);
                                        onSuccessListener.onSuccess(user);
                                        return;
                                    }
                                    onFailureListener.onFailure(new RuntimeException());
                                },
                                onFailureListener::onFailure);
                return;
            }
        }
    }

    public void getStoredMessage(String destinationUuid) {
        getDataManager()
                .getChattingRoom(destinationUuid,
                        roomId -> {
                            mRoomUid = roomId;
                            getNewMessage();
                        },
                        error -> mListener.onError(StringUtil.getStringFromResource(R.string.snack_failed_load_list)));
    }

    public void getNewMessage() {
        getDataManager()
                .listeningChatMessageChanged(mRoomUid,
                        messageList -> {
                            mMessageList.setValue(messageList);
                            mIsListeningForChangedMessage = true;
                        },
                        error -> mListener.onError(StringUtil.getStringFromResource(R.string.snack_failed_load_list)));
    }

    public void cancelChangingMessagesListening() {
        getDataManager()
                .cancelMessageModelObserving();
        mIsListeningForChangedMessage = false;
        mMessageList.setValue(null);
    }

    public void sendNewMessage(int messagesLength, String content) {
        getDataManager()
                .setChatMessage(messagesLength, mRoomUid, mDestinationUserData.getValue().getUuid(), content,
                        roomUid -> {
                            mRoomUid = roomUid;
                            mListener.onSuccess(SEND_MESSAGE_SUCCESS);
                            if (!mIsListeningForChangedMessage) {
                                getNewMessage();
                            }
                        },
                        error -> mListener.onError(StringUtil.getStringFromResource(R.string.snack_failed_send_message)));
    }

    public LiveData<List<Chatting>> getChattingList() {
        return this.mChattingList;
    }

    public void setChattingList(List<Chatting> list) {
        this.mChattingList.setValue(list);
    }

    public LiveData<List<Message>> getChattingMessage() {
        return mMessageList;
    }

    public void setChattingMessage(Chatting chatting) {
        if (chatting != null) {
            mRoomUid = chatting.getRoomUid();
            if (chatting.getMessages() != null) {
                this.mMessageList.setValue(chatting.getMessages());
            }
        }
    }

    public LiveData<User> getDestinationUserData() {
        return mDestinationUserData;
    }

    public void setDestinationUserData(User user) {
        this.mDestinationUserData.setValue(user);
    }

    public void setDestinationUuid(String destinationUuid) {
        getDataManager()
                .getUserFromSingleSnapShot(destinationUuid,
                        user -> {
                            user.setUuid(destinationUuid);
                            mDestinationUserData.setValue(user);
                        },
                        error -> mListener.onError(StringUtil.getStringFromResource(R.string.snack_failed_load_list)));
    }
}