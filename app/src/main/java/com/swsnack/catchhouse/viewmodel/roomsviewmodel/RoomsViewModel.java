package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;

import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;
import java.util.ArrayList;

public class RoomsViewModel extends ReactiveViewModel {
    private Application mAppContext;
    private RoomsRepository mRepository;
    private ViewModelListener mListener;
    public final MutableLiveData<ArrayList<Uri>> mUriList;

    // private List<Room> mRoomList;

    RoomsViewModel(Application application, RoomsRepository repository, ViewModelListener listener) {
        super();
        mAppContext = application;
        mRepository = repository;
        mListener = listener;
        mUriList = new MutableLiveData<>();
    }

    public void gallerySelectionResult(ArrayList<Uri> uriList) {
        ArrayList<Uri> data;

        if (mUriList.getValue() == null) {
            data = uriList;
        } else {
            data = mUriList.getValue();
            for (Uri uri : uriList) {
                if (!data.contains(uri)) {
                    data.add(uri);
                }
            }
        }

        mUriList.postValue(data);

    }
}
