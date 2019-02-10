package com.swsnack.catchhouse.viewmodel.chattingviewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.chattingdata.pojo.Chatting;
import com.swsnack.catchhouse.data.userdata.pojo.User;
import com.swsnack.catchhouse.util.StringUtil;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.view.activities.BottomNavListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattingViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private BottomNavListener mListener;
    private MutableLiveData<List<Chatting>> mChattingList;

    ChattingViewModel(DataManager dataManager, BottomNavListener bottomNavListener) {
        super(dataManager);
        this.mAppContext = AppApplication.getAppContext();
        this.mListener = bottomNavListener;
        this.mChattingList = new MutableLiveData<>();
    }

    public void getChattingRoomList() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }

        getDataManager()
                .getChattingList(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        list -> mChattingList.setValue(list),
                        error -> mListener.onError(StringUtil.getStringFromResource(R.string.snack_database_exception)));
    }

    public void setChattingRoom() {
        Map<String, Boolean> users = new HashMap<>();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            return;
        }
        mListener.isWorking();

        /* dummy data for testing*/
        users.put(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
        users.put("RvXHEN2wXAMDQRvjQkgrfL0rWOC3", true);

        Chatting chatting = new Chatting(users);
        getDataManager()
                .setChattingRoom(chatting,
                        success -> mListener.isFinished(),
                        error -> mListener.onError(StringUtil.getStringFromResource(R.string.snack_failed_make_chattingRoom)));
    }

    public void getUser(int position, OnSuccessListener<User> onSuccessListener, OnFailureListener onFailureListener) {

        for (String uuid : mChattingList.getValue().get(position).getUsers().keySet()) {
            if (!uuid.equals(FirebaseAuth.getInstance().getCurrentUser().toString())) {
                getDataManager()
                        .getUserFromSingleSnapShot(uuid,
                                onSuccessListener,
                                onFailureListener);
                return;
            }
        }

    }

    public LiveData<List<Chatting>> getChattingList() {
        return this.mChattingList;
    }

    public void setChattingList(List<Chatting> list) {
        this.mChattingList.setValue(list);
    }
}