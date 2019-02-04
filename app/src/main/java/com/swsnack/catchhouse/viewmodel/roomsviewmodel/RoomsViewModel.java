package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.skt.Tmap.TMapPOIItem;
import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;
import com.swsnack.catchhouse.data.userdata.api.AppAPIManager;
import com.swsnack.catchhouse.data.userdata.remote.AppUserDataManager;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RoomsViewModel extends ReactiveViewModel {
    private Application mAppContext;
    private RoomsRepository mRepository;
    private ViewModelListener mListener;
    public List<byte[]> mBitmapBytesArray;

    public final MutableLiveData<List<Uri>> mUriList = new MutableLiveData<>();
    public final MutableLiveData<String> mRoomValue = new MutableLiveData<>();
    public final MutableLiveData<List<Address>> mAddressList = new MutableLiveData<>();
    public final MutableLiveData<String> mKeyword = new MutableLiveData<>();
    public final MutableLiveData<String> mAddress = new MutableLiveData<>();


    RoomsViewModel(Application application, RoomsRepository repository, ViewModelListener listener) {
        super(AppDataManager.getInstance(AppAPIManager.getInstance(), AppUserDataManager.getInstance()));
        mAppContext = application;
        mRepository = repository;
        mListener = listener;
        mBitmapBytesArray = new ArrayList<>();
    }

    public void onClickDeleteButton(int position) {
        List<Uri> data = mUriList.getValue();
        data.remove(position);
        mUriList.postValue(data);
    }

    public void gallerySelectionResult(List<Uri> uriList) {
        List<Uri> data;

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

        for (int i = data.size(); i > 9; i--) {
            data.remove(i - 1);
        }

        getByteListFromUri(data);
        mUriList.postValue(data);
    }

    public void getAddressList(View v) {
        searchAddress();
    }

    public void setAddress(int position) {
        mAddress.setValue(mAddressList.getValue().get(position).getName());
        mKeyword.setValue("");
        mAddressList.setValue(null);
    }

    private void getByteListFromUri(List<Uri> uris) {
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

    private void searchAddress() {
        String keyword;

        mListener.isWorking();
        keyword = mKeyword.getValue();
        try {
            getCompositeDisposable().add(mRepository.getPOIFromRemote(keyword)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(tMapPOIItems -> {
                                List<Address> tempList = new ArrayList<>();
                                for (int i = 0; i < tMapPOIItems.size(); i++) {
                                    TMapPOIItem item = tMapPOIItems.get(i);
                                    tempList.add(new Address(item.name, item.getPOIAddress().replace("null", ""), item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude()));
                                }
                                mAddressList.postValue(tempList);
                                mListener.isFinished();
                            }, throwable -> {
                                mListener.isFinished();
                                mListener.onError(throwable);
                            }
                    ));
        } catch (Exception e) {
            mListener.onError(e);
        }
    }
}
