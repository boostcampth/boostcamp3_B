package com.swsnack.catchhouse.viewmodel.searchviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.db.chatting.remote.RemoteChattingManager;
import com.swsnack.catchhouse.data.db.location.remote.AppLocationDataManager;
import com.swsnack.catchhouse.data.db.room.remote.AppRoomDataManager;
import com.swsnack.catchhouse.data.db.rooms.RoomsRepository;
import com.swsnack.catchhouse.data.pojo.Address;
import com.swsnack.catchhouse.data.db.user.remote.AppUserDataManager;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ReactiveViewModel {
    private Application mAppContext;
    private RoomsRepository mRepository;
    private ViewModelListener mListener;
    private MutableLiveData<List<Address>> mAddressList;
    public MutableLiveData<String> mKeyword;

    SearchViewModel(Application application, RoomsRepository repository, APIManager apiManager, ViewModelListener listener) {
        super(AppDataManager.getInstance(AppUserDataManager.getInstance(),
                RemoteChattingManager.getInstance(),
                AppRoomDataManager.getInstance(),
                AppLocationDataManager.getInstance()),
                apiManager);

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
                .toObservable()
                .flatMap(Observable::fromIterable)
                .map(item -> new Address(item.name, item.getPOIAddress().replace("null", ""), item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude()))
                .toList();
    }

}
