package com.swsnack.catchhouse.viewmodel.searchingviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.data.model.RoomCard;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.data.model.Filter;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SearchingViewModel extends ReactiveViewModel implements OnMapReadyCallback {
    private Application mAppContext;
    private ViewModelListener mListener;
    private MutableLiveData<String> mKeyword = new MutableLiveData<>();
    private MutableLiveData<List<Address>> mAddressList = new MutableLiveData<>();
    private MutableLiveData<Boolean> mFinish = new MutableLiveData<>(); // 주소검색 끝났을때
    private MutableLiveData<List<Room>> mRoomList = new MutableLiveData<>();
    private MutableLiveData<List<Room>> mRoomCardList = new MutableLiveData<>();

    /* Naver Map */
    private MutableLiveData<List<Marker>> mMarkerList = new MutableLiveData<>();
    private MutableLiveData<LatLng> mPosition = new MutableLiveData<>();
    private MutableLiveData<NaverMap> mNaverMap = new MutableLiveData<>();
    private MutableLiveData<Boolean> mCardShow = new MutableLiveData<>();

    /* FILTER */
    public MutableLiveData<Boolean> mFilterUpdate = new MutableLiveData<>(); // 필터 업데이트
    public MutableLiveData<String> mFilterPriceFrom = new MutableLiveData<>();
    public MutableLiveData<String> mFilterPriceTo = new MutableLiveData<>();
    public MutableLiveData<String> mFilterDateFrom = new MutableLiveData<>();
    public MutableLiveData<String> mFilterDateTo = new MutableLiveData<>();

    public MutableLiveData<Integer> mFilterDistance = new MutableLiveData<>();
    public MutableLiveData<Boolean> mFilterOptionStandard = new MutableLiveData<>();
    public MutableLiveData<Boolean> mFilterOptionGender = new MutableLiveData<>();
    public MutableLiveData<Boolean> mFilterOptionPet = new MutableLiveData<>();
    public MutableLiveData<Boolean> mFilterOptionSmoking = new MutableLiveData<>();


    SearchingViewModel(Application application, DataManager dataManager, APIManager apiManager, ViewModelListener listener) {
        super(dataManager, apiManager);
        mAppContext = application;
        mListener = listener;

        mFilterPriceFrom.setValue("");
        mFilterPriceTo.setValue("");
        mFilterDateFrom.setValue("");
        mFilterDateTo.setValue("");

        mFilterDistance.setValue(10);
        mFilterOptionStandard.setValue(false);
        mFilterOptionGender.setValue(false);
        mFilterOptionPet.setValue(false);
        mFilterOptionSmoking.setValue(false);

        mAddressList.setValue(new ArrayList<>());
        mRoomCardList.setValue(new ArrayList<>());

        mKeyword.setValue("강남");
        mFinish.setValue(false);
        mCardShow.setValue(false);

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
                .doOnSubscribe(__ -> {
                    mListener.isWorking();
                    mFinish.postValue(true);
                })
                .doAfterTerminate(() -> {
                    mListener.isFinished();
                    //mFinish.postValue(true);
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
                .doOnSubscribe(__ -> {
                    mListener.isWorking();
                    //removeAllMarker();
                })
                .doAfterTerminate(() -> {
                    mListener.isFinished();
                    LatLng latLng = new LatLng(latitude, longitude);
                    mPosition.postValue(latLng); // Move map
                    mCardShow.postValue(true);
                })
                .subscribe(roomDataList -> {
                    Log.v("csh", "Single Success");
                    mRoomList.postValue(roomDataList);

                    Toast.makeText(mAppContext, "검색 완료", Toast.LENGTH_SHORT).show();

                    Log.v("csh", "test:" + mRoomCardList.getValue());


                    //////////////////////
                    List<Marker> markerList = new ArrayList<>();
                    for (int i = 0; i < roomDataList.size(); i++) {
                        Room room = roomDataList.get(i);
                        Marker marker = new Marker(new LatLng(room.getLatitude(), room.getLongitude()));
                        /*
                        InfoWindow infoWindow = new InfoWindow();
                        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(mAppContext) {
                            @NonNull
                            @Override
                            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                                Log.v("csh","infoWindow");
                                return room.getSize()+"평";
                            }
                        });
                        infoWindow.open(marker);*/
                        marker.setTag(room);
                        markerList.add(marker);
                    }
                    mMarkerList.postValue(markerList);
                    //////////////////////

                    /* Todo:Rxjava로 변환해야함 */
                    List<RoomCard> roomCardList = new ArrayList<>();
                    for (int i = 0; i < roomDataList.size(); i++) {
                        String uri = "";
                        if (roomDataList.get(i).getImages().get(0) != null) {
                            uri = roomDataList.get(i).getImages().get(0);
                        }


                        /* TODO : 가격 필터 기간에 곱해야함 */
                        roomCardList.add(new RoomCard(uri,
                                roomDataList.get(i).getTitle(),
                                roomDataList.get(i).getContent(),
                                roomDataList.get(i).getAddress(),
                                roomDataList.get(i).getAddressName(),
                                roomDataList.get(i).getPrice(),
                                roomDataList.get(i).getSize(),
                                roomDataList.get(i).getUuid()));

                    }
                    mRoomCardList.postValue(roomDataList);
                }, throwable -> {
                    Log.v("csh", "Single Error" + throwable.getMessage());
                    Toast.makeText(mAppContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void onClickMarker(String data) {
        Log.v("csh","마커:"+data);
    }



    public void setCardShow(boolean value) {
        mCardShow.postValue(value);
    }

    /*
    private void removeAllMarker() {
        for (int i = 0; i < mMarkerList.getValue().size(); i++) {
            Marker marker = mMarkerList.getValue().get(i);
            marker.setMap(null);
        }
    }*/

    public LiveData<List<Address>> getAddressList() {
        return this.mAddressList;
    }

    public void setAddressList(List<Address> list) {
        mAddressList.setValue(list);
    }

    public LiveData<List<Room>> getRoomCardList() {
        return this.mRoomCardList;
    }

    public void setRoomCardList(List<Room> list) {
        mRoomCardList.setValue(list);
    }

    public LiveData<Boolean> getFinish() {
        return this.mFinish;
    }

    public void setFinish(boolean finish) {
        this.mFinish.setValue(finish);
    }

    public LiveData<List<Room>> getRoomList() {
        return this.mRoomList;
    }

    public LiveData<Boolean> getFilterUpdate() {
        return this.mFilterUpdate;
    }

    public LiveData<List<Marker>> getMarkerList() { return this.mMarkerList; }

    public LiveData<LatLng> getPosition() { return this.mPosition; }

    public LiveData<Boolean> isCardShow() { return this.mCardShow; }

    public void setFilterUpdate(boolean value) {
        this.mFilterUpdate.postValue(value);
    }

    public String getFilterPriceFrom() {
        String ret = mFilterPriceFrom.getValue();
        if (ret == null)
            return "0";
        return ret;
    }

    public String getFilterPriceTo() {
        String ret = mFilterPriceTo.getValue();
        if (ret == null)
            return "0";
        return ret;
    }

    public String getFilterDateFrom() {
        String ret = mFilterDateFrom.getValue();
        if (ret == null)
            return "0";
        return ret;
    }

    public String getFilterDateTo() {
        String ret = mFilterDateTo.getValue();
        if (ret == null)
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


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mNaverMap.postValue(naverMap);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(true);
        uiSettings.setCompassEnabled(true);

        Log.v("csh","onMapReady!!!!!!!!!!!!!!!!!!!");
        /*
        mNaverMap = naverMap;
        Log.v("csh", "onMapReadyyyyyyyyyyyyyyyyy");
        Log.v("csh 1",""+naverMap.toString());

        getBinding().nmMap.getMapAsync(nm -> {
            Log.v("csh 1",""+nm.toString());
            UiSettings uiSettings = nm.getUiSettings();
            uiSettings.setZoomControlEnabled(false);
            uiSettings.setCompassEnabled(true);
        });*/


    }



}
