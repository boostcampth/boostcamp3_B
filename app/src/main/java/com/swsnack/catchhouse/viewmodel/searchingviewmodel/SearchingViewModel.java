package com.swsnack.catchhouse.viewmodel.searchingviewmodel;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;
import android.widget.Toast;

import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.data.model.Filter;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchingViewModel extends ReactiveViewModel {
    private Application mAppContext;
    private ViewModelListener mListener;
    private MutableLiveData<String> mKeyword;
    private MutableLiveData<List<Address>> mAddressList;
    private MutableLiveData<Boolean> mFinish; // 주소검색 끝났을때
    private MutableLiveData<List<Room>> mRoomList;

    /* FILTER */
    public MutableLiveData<Boolean> mFilterUpdate; // 필터 업데이트
    public MutableLiveData<String> mFilterPriceFrom;
    public MutableLiveData<String> mFilterPriceTo;
    public MutableLiveData<String> mFilterDateFrom;
    public MutableLiveData<String> mFilterDateTo;

    public MutableLiveData<Integer> mFilterDistance;
    public MutableLiveData<Boolean> mFilterOptionStandard;
    public MutableLiveData<Boolean> mFilterOptionGender;
    public MutableLiveData<Boolean> mFilterOptionPet;
    public MutableLiveData<Boolean> mFilterOptionSmoking;


    SearchingViewModel(Application application, DataManager dataManager, APIManager apiManager, ViewModelListener listener) {
        super(dataManager, apiManager);
        mAppContext = application;
        mListener = listener;
        mKeyword = new MutableLiveData<>();
        mAddressList = new MutableLiveData<>();
        mFinish = new MutableLiveData<>();
        mRoomList = new MutableLiveData<>();

        /* Filter */
        mFilterUpdate = new MutableLiveData<>();
        mFilterPriceFrom = new MutableLiveData<>();
        mFilterPriceTo = new MutableLiveData<>();
        mFilterDateFrom = new MutableLiveData<>();
        mFilterDateTo = new MutableLiveData<>();

        mFilterDistance = new MutableLiveData<>();
        mFilterOptionStandard = new MutableLiveData<>();
        mFilterOptionGender = new MutableLiveData<>();
        mFilterOptionPet = new MutableLiveData<>();
        mFilterOptionSmoking = new MutableLiveData<>();

        mFilterPriceFrom.setValue("");
        mFilterPriceTo.setValue("");
        mFilterDateFrom.setValue("");
        mFilterDateTo.setValue("");

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

    public void searchAddress() {
        getCompositeDisposable().add(getDataManager().getPOIList(getKeyword())
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
    }

    public void updateData(double latitude, double longitude) {

        Filter filter = new Filter(
                getFilterPriceFrom(), getFilterPriceTo(), getFilterDateFrom(),
                getFilterDateTo(), latitude, longitude,
                getFilterDistance(), isFilterOptionStandard(), isFilterOptionGender(),
                isFilterOptionPet(), isFilterOptionSmoking());

        getDataManager().getNearRoomList(filter)
                .doOnSubscribe(__ -> mListener.isWorking())
                .doAfterTerminate(() -> {
                    mListener.isFinished();
                })
                .subscribe(roomDataList -> {
                    Log.v("csh", "Single Success");
                    mRoomList.postValue(roomDataList);
                    Toast.makeText(mAppContext, "검색 완료", Toast.LENGTH_SHORT).show();
                },throwable -> {
                    Log.v("csh", "Single Error" + throwable.getMessage());
                    Toast.makeText(mAppContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
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

    public LiveData<List<Room>> getRoomList() { return this.mRoomList; }

    public LiveData<Boolean> getFilterUpdate() { return this.mFilterUpdate; }

    public void setFilterUpdate(boolean value) {
        this.mFilterUpdate.postValue(value);
    }

    public String getFilterPriceFrom() {
        String ret = mFilterPriceFrom.getValue();
        if(ret == null)
            return "0";
        return ret;
    }

    public String getFilterPriceTo() {
        String ret = mFilterPriceTo.getValue();
        if(ret == null)
            return "0";
        return ret;
    }

    public String getFilterDateFrom() {
        String ret = mFilterDateFrom.getValue();
        if(ret == null)
            return "0";
        return ret;
    }

    public String getFilterDateTo() {
        String ret = mFilterDateTo.getValue();
        if(ret == null)
            return "0";
        return ret;
    }

    public int getFilterDistance() {
        return mFilterDistance.getValue();
    }

    public boolean isFilterOptionStandard() {
        return mFilterOptionStandard.getValue();
    }

    public boolean isFilterOptionGender() {
        return mFilterOptionGender.getValue();
    }

    public boolean isFilterOptionPet() {
        return mFilterOptionPet.getValue();
    }

    public boolean isFilterOptionSmoking() {
        return mFilterOptionSmoking.getValue();
    }


}
