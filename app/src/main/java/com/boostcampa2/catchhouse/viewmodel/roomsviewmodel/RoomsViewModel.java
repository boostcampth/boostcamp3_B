package com.boostcampa2.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;

import com.boostcampa2.catchhouse.data.roomsdata.RoomsRepository;
import com.boostcampa2.catchhouse.data.roomsdata.pojo.Room;
import com.boostcampa2.catchhouse.viewmodel.ReactiveViewModel;
import com.boostcampa2.catchhouse.viewmodel.ViewModelListener;

import java.util.List;

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
