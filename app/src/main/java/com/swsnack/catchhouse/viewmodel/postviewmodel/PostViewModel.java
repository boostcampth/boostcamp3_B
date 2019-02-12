package com.swsnack.catchhouse.viewmodel.postviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.roomsdata.pojo.Room;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private ViewModelListener mListener;

    public final MutableLiveData<List<String>> mImageList = new MutableLiveData<>();

    PostViewModel(DataManager dataManager, ViewModelListener listener) {
        super(dataManager);
        mAppContext = AppApplication.getAppContext();
        mListener = listener;

        mImageList.setValue(new ArrayList<>());
    }

    public void setInitData(Room room) {
        mImageList.setValue(room.getImages());
    }


}
