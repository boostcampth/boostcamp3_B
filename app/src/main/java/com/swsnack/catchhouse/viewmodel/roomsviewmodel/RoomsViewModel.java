package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;

import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.data.userdata.UserDataManager;
import com.swsnack.catchhouse.data.userdata.api.AppAPIManager;
import com.swsnack.catchhouse.data.userdata.remote.AppUserDataManager;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

public class RoomsViewModel extends ReactiveViewModel {
    private Application mAppContext;
    private RoomsRepository mRepository;
    private ViewModelListener mListener;
    // private List<Room> mRoomList;

    RoomsViewModel(Application application, RoomsRepository repository, ViewModelListener listener) {
        super(AppDataManager.getInstance(AppAPIManager.getInstance(), AppUserDataManager.getInstance()));
        mAppContext = application;
        mRepository = repository;
        mListener = listener;
    }

}
