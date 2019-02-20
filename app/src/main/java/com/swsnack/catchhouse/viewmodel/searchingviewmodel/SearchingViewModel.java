package com.swsnack.catchhouse.viewmodel.searchingviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.CircleOverlay;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.swsnack.catchhouse.Constant;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.data.model.RoomCard;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.data.model.Filter;
import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.DataSource;
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
    private MutableLiveData<List<Address>> mAddressList = new MutableLiveData<>(

    );
    private MutableLiveData<Boolean> mFinish = new MutableLiveData<>(); // 주소검색 끝났을때
    private MutableLiveData<List<Room>> mRoomList = new MutableLiveData<>();
    private MutableLiveData<List<Room>> mRoomCardList = new MutableLiveData<>();

    /* Naver Map */
    private MutableLiveData<List<Marker>> mMarkerList = new MutableLiveData<>();
    private MutableLiveData<LatLngBounds> mPosition = new MutableLiveData<>();
    private MutableLiveData<NaverMap> mNaverMap = new MutableLiveData<>();
    private MutableLiveData<Boolean> mCardShow = new MutableLiveData<>();
    private MutableLiveData<CircleOverlay> mCircle = new MutableLiveData<>();
    private MutableLiveData<Marker> mSelectedMarker = new MutableLiveData<>();
    private MutableLiveData<InfoWindow> mSelectedInfo = new MutableLiveData<>();

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


    SearchingViewModel(Application application, DataSource dataManager, APIManager apiManager, ViewModelListener listener) {
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

        getCompositeDisposable().add(getDataManager().getNearRoomList(filter)
                .doOnSubscribe(__ -> {
                    mListener.isWorking();
                    removeAllOverlay();
                    CircleOverlay circleOverlay = new CircleOverlay();
                    circleOverlay.setCenter(new LatLng(latitude, longitude));
                    circleOverlay.setRadius(filter.getDistance()*1000);
                    circleOverlay.setColor(ContextCompat.getColor(mAppContext,R.color.colorTransYellow));
                    circleOverlay.setOutlineColor(ContextCompat.getColor(mAppContext,R.color.colorTransOrange));
                    circleOverlay.setOutlineWidth(5);
                    mCircle.postValue(circleOverlay);
                })
                .doAfterTerminate(() -> {
                    mCardShow.postValue(true);
                })
                .doAfterSuccess(__ -> mListener.onSuccess(Constant.SuccessKey.SEARCH_SUCCESS))
                .subscribe(roomDataList -> {

                    Log.v("csh", "Single Success");
                    mRoomList.postValue(roomDataList);
                    Log.v("csh", "test:" + mRoomCardList.getValue());

                    //////////////////////
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(new LatLng(latitude, longitude))
                            .build();

                    List<Marker> markerList = new ArrayList<>();
                    for (int i = 0; i < roomDataList.size(); i++) {
                        Room room = roomDataList.get(i);
                        LatLng latLng = new LatLng(room.getLatitude(), room.getLongitude());
                        Marker marker = new Marker(latLng);

                        marker.setTag(room);
                        //marker.setCaptionText(room.getAddressName());
                        markerList.add(marker);
                        bounds = bounds.expand(latLng);
                        Log.v("csh","테스트:"+latLng.latitude+","+latLng.longitude);
                    }
                    mPosition.postValue(bounds);
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
                    //Toast.makeText(mAppContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    mRoomCardList.postValue(new ArrayList<>());
                    mListener.onError(throwable.getMessage());
                }));
    }

    public void onClickMarker(Marker marker, InfoWindow infoWindow) {
        if(mCardShow.getValue() == false) {
            mCardShow.postValue(true);
            mSelectedMarker.postValue(marker);

            if(mSelectedInfo.getValue() != null) {
                Log.v("csh", "지움");
                mSelectedInfo.getValue().close();
            }
            mSelectedInfo.postValue(infoWindow);

        }
        //Log.v("csh","마커:"+data);
    }

    public void onUpdateMap() {
        if(mNaverMap.getValue() != null) {
            CameraPosition cameraPosition = mNaverMap.getValue().getCameraPosition();
            LatLng latLng = cameraPosition.target;
            updateData(latLng.latitude, latLng.longitude);
        }
    }

    public void setCardShow(boolean value) {
        mCardShow.postValue(value);
    }


    private void removeAllOverlay() {
        if(mMarkerList.getValue() != null) {
            for (int i = 0; i < mMarkerList.getValue().size(); i++) {
                Marker marker = mMarkerList.getValue().get(i);
                marker.setMap(null);
            }
        }

        if(mCircle.getValue() != null) {
            mCircle.getValue().setMap(null);
        }
    }

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

    public LiveData<LatLngBounds> getPosition() { return this.mPosition; }

    public LiveData<Boolean> isCardShow() { return this.mCardShow; }

    public void setFilterUpdate(boolean value) {
        this.mFilterUpdate.postValue(value);
    }

    public LiveData<CircleOverlay> getCircle() {
        return this.mCircle;
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

    public LiveData<Marker> getSelectedMarker() {
        return this.mSelectedMarker;
    }

    public LiveData<InfoWindow> getSelectedInfo() {
        return this.mSelectedInfo;
    }

    public LiveData<Boolean> isShowingCardView() {
        return mCardShow;
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mNaverMap.postValue(naverMap);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setZoomControlEnabled(true);
        uiSettings.setCompassEnabled(true);

        Log.v("csh","onMapReady!!!!!!!!!!!!!!!!!!!");

        // 최초 업데이트
        CameraPosition cameraPosition = naverMap.getCameraPosition();
        LatLng latLng = cameraPosition.target;
        updateData(latLng.latitude, latLng.longitude);
    }




}
