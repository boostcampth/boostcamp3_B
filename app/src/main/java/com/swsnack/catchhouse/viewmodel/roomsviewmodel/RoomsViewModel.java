package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;

import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

public class RoomsViewModel extends ReactiveViewModel {
    private Application mAppContext;
    private RoomsRepository mRepository;
    private ViewModelListener mListener;
    // private List<Room> mRoomList;

    RoomsViewModel(Application application, RoomsRepository repository, ViewModelListener listener) {
        super();
        mAppContext = application;
        mRepository = repository;
        mListener = listener;
    }

}
