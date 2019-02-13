package com.swsnack.catchhouse.viewmodel.searchviewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Geocoder;
import android.util.Log;

import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.db.chatting.remote.RemoteChattingManager;
import com.swsnack.catchhouse.data.db.location.remote.AppLocationDataManager;
import com.swsnack.catchhouse.data.db.room.remote.AppRoomDataManager;
import com.swsnack.catchhouse.data.db.rooms.RoomsRepository;
import com.swsnack.catchhouse.data.pojo.Address;
import com.swsnack.catchhouse.data.db.user.remote.AppUserDataManager;
import com.swsnack.catchhouse.data.pojo.Filter;
import com.swsnack.catchhouse.data.pojo.RoomData;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ReactiveViewModel {
    private Application mAppContext;
    private RoomsRepository mRepository;
    private ViewModelListener mListener;
    private MutableLiveData<String> mKeyword;
    private MutableLiveData<List<Address>> mAddressList;
    private MutableLiveData<Boolean> mFinish; // 주소검색 끝났을때
    private List<Geocoder> getList;
    private MutableLiveData<List<RoomData>> mRoomDataList;

    /* FILTER */
    public MutableLiveData<Boolean> mFilterUpdate; // 필터 업데이트
    public MutableLiveData<String> mFilterPriceFrom;
    public MutableLiveData<String> mFilterPriceTo;
    public MutableLiveData<String> mFilterDateFrom;
    public MutableLiveData<String> mFilterDateTo;
    public MutableLiveData<Double> mFilterLatitude;
    public MutableLiveData<Double> mFilterLongitude;
    public MutableLiveData<Integer> mFilterDistance;
    public MutableLiveData<Boolean> mFilterOptionStandard;
    public MutableLiveData<Boolean> mFilterOptionGender;
    public MutableLiveData<Boolean> mFilterOptionPet;
    public MutableLiveData<Boolean> mFilterOptionSmoking;


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
        mAddressList = new MutableLiveData<>();
        mFinish = new MutableLiveData<>();
        mRoomDataList = new MutableLiveData<>();

        /* Filter */
        mFilterUpdate = new MutableLiveData<>();
        mFilterPriceFrom = new MutableLiveData<>();
        mFilterPriceTo = new MutableLiveData<>();
        mFilterDateFrom = new MutableLiveData<>();
        mFilterDateTo = new MutableLiveData<>();
        mFilterLatitude = new MutableLiveData<>();
        mFilterLongitude = new MutableLiveData<>();
        mFilterDistance = new MutableLiveData<>();
        mFilterOptionStandard = new MutableLiveData<>();
        mFilterOptionGender = new MutableLiveData<>();
        mFilterOptionPet = new MutableLiveData<>();
        mFilterOptionSmoking = new MutableLiveData<>();

        mFilterUpdate.setValue(false);
        mFilterPriceFrom.setValue("");
        mFilterPriceTo.setValue("");
        mFilterDateFrom.setValue("");
        mFilterDateTo.setValue("");
        mFilterLatitude.setValue(0.0);
        mFilterLongitude.setValue(0.0);
        mFilterDistance.setValue(10);
        mFilterOptionStandard.setValue(false);
        mFilterOptionGender.setValue(false);
        mFilterOptionPet.setValue(false);
        mFilterOptionSmoking.setValue(false);

        mKeyword.setValue("강남");
        mFinish.setValue(false);


    }

    public String getKeyword() {
        if (mKeyword.getValue() == null) {
            return "";
        }
        return mKeyword.getValue();
    }

    public void setKeyword(String keyword) {
        mKeyword.setValue(keyword);
    }

    //public Single<List<Address>> searchAddress() {
    public void searchAddress() {
        getCompositeDisposable().add(mRepository.getPOIFromRepository(getKeyword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> mListener.isWorking())
                .doAfterTerminate(() -> {
                    mListener.isFinished();
                    mFinish.postValue(true);
                })
                .toObservable()
                .flatMap(Observable::fromIterable)
                .map(item -> new Address(item.name, item.getPOIAddress().replace("null", ""), item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude()))
                .toList()
                .subscribe(addressList -> {
                    mAddressList.postValue(addressList);
                }, throwable -> {
                    Log.v("csh", "address fail");
                }));
        /*
        return mRepository.getPOIFromRepository(getKeyword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> mListener.isWorking())
                .doAfterTerminate(() -> mListener.isFinished())
                .toObservable()
                .flatMap(Observable::fromIterable)
                .map(item -> new Address(item.name, item.getPOIAddress().replace("null", ""), item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude()))
                .toList()
                .subscribe( addressList -> {

                },throwable -> {

                })
                ;*/
    }

    public void updateData(String keyword, double latitude, double longitude) {
        mRepository.getNearRoomListFromRepository(latitude, longitude, 10)
                .subscribe(roomDataList -> {
                    Log.v("csh", "Single Success");
                    mRoomDataList.postValue(roomDataList);
                },throwable -> {
                    Log.v("csh", "Single Error");
                });
    }

    public LiveData<List<Address>> getAddressList() {
        return this.mAddressList;
    }

    public void setAddressList(List<Address> list) {
        mAddressList.setValue(list);
    }

    public LiveData<Boolean> getFinish() {
        return this.mFinish;
    }

    public void setFinish(boolean finish) {
        this.mFinish.setValue(finish);
    }

    public LiveData<List<RoomData>> getRoomDataList() { return this.mRoomDataList; }


}
