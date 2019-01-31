package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import android.view.View;

import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RoomsViewModel extends ReactiveViewModel {
    private Application mAppContext;
    private RoomsRepository mRepository;
    private ViewModelListener mListener;
    private MutableLiveData<List<Address>> mAddressList;
    public MutableLiveData<String> mKeyword;

    RoomsViewModel(Application application, RoomsRepository repository, ViewModelListener listener) {
        super();
        mAppContext = application;
        mRepository = repository;
        mListener = listener;
        mAddressList = new MutableLiveData<>();
        mKeyword = new MutableLiveData<>();

        List<Address> list = new ArrayList<>();

        mAddressList.postValue(list);
    }

    public MutableLiveData<List<Address>> getAddressList() {
        return mAddressList;
    }


    public void searchAddress() {

        mListener.isWorking();
        getCompositeDisposable().add(mRepository.getPOIFromRemote(mKeyword.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tMapPOIItems -> {
                    List<Address> tempList = new ArrayList<>();
                    for(int i = 0; i < tMapPOIItems.size(); i++) {
                        TMapPOIItem item = tMapPOIItems.get(i);
                        tempList.add(new Address(item.name, item.getPOIAddress().replace("null", ""), item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude()));
                    }
                    mAddressList.postValue(tempList);
                    mListener.isFinished();
                }, throwable -> {
                    Log.d("csh", "Error:" + throwable.getMessage());
                    mListener.isFinished();
                }
        ));
        Log.v("csh", "key:"+mKeyword.getValue().toString());
    }

}
