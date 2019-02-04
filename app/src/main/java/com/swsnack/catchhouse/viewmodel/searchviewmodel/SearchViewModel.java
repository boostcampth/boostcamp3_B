package com.swsnack.catchhouse.viewmodel.searchviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.util.Log;

import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;
import com.swsnack.catchhouse.data.userdata.api.AppAPIManager;
import com.swsnack.catchhouse.data.userdata.remote.AppUserDataManager;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ReactiveViewModel {
    private Application mAppContext;
    private RoomsRepository mRepository;
    private ViewModelListener mListener;
    public MutableLiveData<String> mKeyword;

    SearchViewModel(Application application, RoomsRepository repository, ViewModelListener listener) {
        super(AppDataManager.getInstance(AppAPIManager.getInstance(), AppUserDataManager.getInstance()));
        mAppContext = application;
        mRepository = repository;
        mListener = listener;
        mKeyword = new MutableLiveData<>();
        mKeyword.setValue("asd");
    }

    private String getKeyword() {
        if (mKeyword.getValue() == null) {
            return "";
        }
        return mKeyword.getValue();
    }

    public void setKeyword(String keyword) {
        mKeyword.setValue(keyword);
    }


    public Single<List<Address>> searchAddress() {
        return mRepository.getPOIFromRemote(getKeyword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> mListener.isWorking())
                .doAfterTerminate(() -> mListener.isFinished())
                .map(tMapPOIItems -> {
                    List<Address> tempList = new ArrayList<>();
                    for (int i = 0; i < tMapPOIItems.size(); i++) {
                        TMapPOIItem item = tMapPOIItems.get(i);
                        tempList.add(new Address(item.name, item.getPOIAddress().replace("null", ""), item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude()));
                    }
                    return tempList;
                });
    }
}
