package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.asynctask.ConvertImageTask;
import com.swsnack.catchhouse.data.db.rooms.RoomsRepository;
import com.swsnack.catchhouse.data.listener.OnFailedListener;
import com.swsnack.catchhouse.data.listener.OnSuccessListener;
import com.swsnack.catchhouse.data.model.ExpectedPrice;
import com.swsnack.catchhouse.data.pojo.Address;
import com.swsnack.catchhouse.data.pojo.Room;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.swsnack.catchhouse.Constant.PostException.EMPTY_PRICE_FIELD;
import static com.swsnack.catchhouse.Constant.PostException.EMPTY_ROOM_IMAGE;
import static com.swsnack.catchhouse.Constant.PostException.EMPTY_TITLE_FIELD;
import static com.swsnack.catchhouse.Constant.PostException.NOT_SELECTION_DATE;

public class RoomsViewModel extends ReactiveViewModel {

    private static final String TAG = RoomsViewModel.class.getSimpleName();

    private Application mAppContext;
    private ViewModelListener mListener;
    private DataManager mDataManager;

    public final MutableLiveData<List<Address>> mSearchResultList = new MutableLiveData<>();
    public final MutableLiveData<List<Uri>> mImageList = new MutableLiveData<>();

    public final MutableLiveData<String> mPrice = new MutableLiveData<>();
    public final MutableLiveData<String> mFromDate = new MutableLiveData<>();
    public final MutableLiveData<String> mToDate = new MutableLiveData<>();
    public final MutableLiveData<String> mExpectedPrice = new MutableLiveData<>();

    public final MutableLiveData<String> mSize = new MutableLiveData<>();
    public final MutableLiveData<Address> mAddress = new MutableLiveData<>();
    public final MutableLiveData<String> mTitle = new MutableLiveData<>();
    public final MutableLiveData<String> mContent = new MutableLiveData<>();            //

    private ExpectedPrice ep;

    RoomsViewModel(Application application, DataManager dataManager, APIManager apiManager, ViewModelListener listener) {
        super(dataManager, apiManager);
        mAppContext = application;
        mListener = listener;
        mDataManager = dataManager;
        init();
    }

    public void onSelectImage(List<Uri> uriList) {
        List<Uri> currentList = mImageList.getValue();

        for (Uri uri : uriList) {
            if (currentList != null && !currentList.contains(uri)) {
                currentList.add(uri);
            }
        }

        mImageList.setValue(currentList);
    }

    public void onClickDeleteButton(int position) {
        List<Uri> data = mImageList.getValue();

        if (data != null) {
            data.remove(position);
            mImageList.setValue(data);
        }
    }

    public void onChangePriceAndPeriod() {
        String expectedPrice = ep.update(
                mPrice.getValue(),
                mFromDate.getValue(),
                mToDate.getValue()
        );
        mExpectedPrice.setValue(expectedPrice);
    }

    public void onSearchAddress(String keyword) {
        getCompositeDisposable().add(searchAddress(keyword)
                .subscribe(list ->
                                mSearchResultList.postValue(list)
                        , exception ->
                                mListener.onError("error")
                )
        );
    }

    public void onSelectAddress(int position) {
        if (mSearchResultList.getValue() != null) {
            List<Address> addressList = mSearchResultList.getValue();

            mAddress.setValue(addressList.get(position));

            addressList.clear();
            mSearchResultList.setValue(addressList);
        }
    }

    public void onClickPost(boolean std, boolean gender, boolean pet, boolean smoking) {
        String validResult = isRoomDataValid();

        if (!TextUtils.isEmpty(validResult)) {
            mListener.onError("error");
            return;
        }

        mListener.isWorking();

        OnFailedListener errorHandler = error -> {
            mListener.isFinished();
            mListener.onError("error");
        };

        mDataManager.createKey(
                key -> convert(
                        imageByte -> mDataManager.uploadRoomImage(key, imageByte,
                                urlList -> push(key, urlList, std, gender, pet, smoking,
                                        __ -> mDataManager.uploadLocationData(key, mAddress.getValue(),
                                                ___ -> {
                                                    mListener.isFinished();
                                                    mListener.onSuccess("Success");
                                                }
                                                , errorHandler)
                                        , errorHandler
                                ), errorHandler
                        ), errorHandler
                ), errorHandler
        );
    }

    private void init() {
        mSearchResultList.setValue(new ArrayList<>());

        mPrice.setValue("");
        mFromDate.setValue(mAppContext.getString(R.string.tv_write_date));
        mToDate.setValue(mAppContext.getString(R.string.tv_write_date));
        mExpectedPrice.setValue(mAppContext.getString(R.string.tv_write_expected_value_default));
        mTitle.setValue("");
        mContent.setValue("");

        mImageList.setValue(new ArrayList<>());
        ep = new ExpectedPrice("", "", "");
    }

    private String isRoomDataValid() {
        String defaultDate = mAppContext.getString(R.string.tv_write_date);

        if (mImageList.getValue() != null && mImageList.getValue().size() == 0) {
            return EMPTY_ROOM_IMAGE;
        } else if (TextUtils.isEmpty(mPrice.getValue())) {
            return EMPTY_PRICE_FIELD;
        } else if (TextUtils.equals(mFromDate.getValue(), defaultDate) ||
                TextUtils.equals(mToDate.getValue(), defaultDate)) {
            return NOT_SELECTION_DATE;
        } else if (TextUtils.isEmpty(mTitle.getValue())) {
            return EMPTY_TITLE_FIELD;
        } else {
            return "";
        }
    }

    private void convert(OnSuccessListener<List<byte[]>> onSuccessListener,
                         OnFailedListener onFailedListener) {
        AsyncTask<Uri, Void, List<byte[]>> mTask;
        mTask = new ConvertImageTask(mAppContext, onSuccessListener, onFailedListener);
        List<Uri> u = mImageList.getValue();
        mTask.execute(u.toArray(new Uri[0]));
    }

    private void push(String key, List<String> urls,
                      boolean std, boolean gender, boolean pet, boolean smoking,
                      OnSuccessListener<Void> onSuccessListener,
                      OnFailedListener onFailedListener) {

        String UUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Room room = new Room(
                mPrice.getValue(),
                mFromDate.getValue(),
                mToDate.getValue(),
                mTitle.getValue(),
                mContent.getValue(),
                urls,
                UUID,
                mSize.getValue(),
                mAddress.getValue().getAddress(),
                mAddress.getValue().getName(),
                std,
                gender,
                pet,
                smoking
        );
        getDataManager().uploadRoomData(key, room, onSuccessListener, onFailedListener);
    }

    private Single<List<Address>> searchAddress(String keyword) {
        return RoomsRepository.getInstance().getPOIFromRemote(keyword)
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
