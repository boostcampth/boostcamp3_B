package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import android.util.Log;
import android.view.View;

import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RoomsViewModel extends ReactiveViewModel {
    private Application mAppContext;
    private RoomsRepository mRepository;
    private ViewModelListener mListener;
    public final MutableLiveData<ArrayList<Uri>> mUriList;
    public List<byte[]> mBitmapBytesArray;
    private MutableLiveData<List<Address>> mAddressList;
    public MutableLiveData<String> mKeyword;

    // private List<Room> mRoomList;
    RoomsViewModel(Application application, RoomsRepository repository, ViewModelListener listener) {
        super();
        mAppContext = application;
        mRepository = repository;
        mListener = listener;
        mUriList = new MutableLiveData<>();
        mBitmapBytesArray = new ArrayList<>();
        mAddressList = new MutableLiveData<>();
        mKeyword = new MutableLiveData<>();
        mKeyword.setValue("");

        List<Address> list = new ArrayList<>();

        mAddressList.postValue(list);
    }

    public void onClickDeleteButton(int position) {
        ArrayList<Uri> data = mUriList.getValue();
        data.remove(position);
        mUriList.postValue(data);
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

        for(int i = data.size(); i > 9; i--) {
            data.remove(i - 1);
        }

        getByteArrayListFromUri(data);
        mUriList.postValue(data);
    }

    public MutableLiveData<List<Address>> getAddressList() {
        return mAddressList;
    }

    public Address getAddress(int position) {
        if(mAddressList.getValue() == null) {
            return new Address();
        }
        return mAddressList.getValue().get(position);
    }

    public String getKeyword() {
        if(mKeyword.getValue() == null) {
            return "";
        }
        return mKeyword.getValue();
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
        Log.v("csh", "key:"+getKeyword());
    }

    private void getByteArrayListFromUri(ArrayList<Uri> uris) {
        mListener.isWorking();

        getCompositeDisposable().add(
                Observable.fromIterable(uris)
                        .map(uri -> {
                                    Bitmap bitmap = Glide
                                            .with(mAppContext)
                                            .asBitmap()
                                            .apply(new RequestOptions().override(340, 324))
                                            .load(uri)
                                            .submit()
                                            .get();
                                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);

                                    return outputStream.toByteArray();
                                }
                        )
                        .collect(() ->
                                new ArrayList<byte[]>(), (bytes, bt) -> bytes.add(bt)
                        )
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(bitmapBytes -> {
                                    mBitmapBytesArray = bitmapBytes;
                                    mListener.onSuccess("success__" + Integer.toString(mBitmapBytesArray.size()));
                                }
                                , error -> mListener.onError(error)
                        )
        );
    }
}
