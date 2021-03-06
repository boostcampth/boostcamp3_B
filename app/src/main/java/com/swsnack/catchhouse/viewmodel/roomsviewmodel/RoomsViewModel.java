package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;
import android.net.Uri;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.model.Address;
import com.swsnack.catchhouse.data.model.ExpectedPrice;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.DataSource;
import com.swsnack.catchhouse.repository.OnFailedListener;
import com.swsnack.catchhouse.repository.OnSuccessListener;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.swsnack.catchhouse.Constant.WriteException.ERROR_EMPTY_PRICE;
import static com.swsnack.catchhouse.Constant.WriteException.ERROR_EMPTY_ROOM_SIZE;
import static com.swsnack.catchhouse.Constant.WriteException.ERROR_EMPTY_TITLE;

import static com.swsnack.catchhouse.Constant.WriteException.ERROR_NO_SELECTION_ADDRESS;
import static com.swsnack.catchhouse.Constant.WriteException.ERROR_NO_SELECTION_DATE;
import static com.swsnack.catchhouse.Constant.WriteException.ERROR_NO_SELECTION_IMAGE;

public class RoomsViewModel extends ReactiveViewModel {

    private static final String TAG = RoomsViewModel.class.getSimpleName();

    private Application mAppContext;
    private ViewModelListener mListener;
    private DataSource mDataManager;

    public final MutableLiveData<List<Address>> mSearchResultList = new MutableLiveData<>();
    public final MutableLiveData<List<Uri>> mImageList = new MutableLiveData<>();
    public final MutableLiveData<String> mPrice = new MutableLiveData<>();
    public final MutableLiveData<String> mFromDate = new MutableLiveData<>();
    public final MutableLiveData<String> mToDate = new MutableLiveData<>();
    public final MutableLiveData<String> mExpectedPrice = new MutableLiveData<>();
    public final MutableLiveData<String> mSize = new MutableLiveData<>();
    public final MutableLiveData<Address> mAddress = new MutableLiveData<>();
    public final MutableLiveData<String> mTitle = new MutableLiveData<>();
    public final MutableLiveData<String> mContent = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionStd = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionGender = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionPet = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionSmoking = new MutableLiveData<>();

    public final MutableLiveData<Room> room = new MutableLiveData<>();

    private String myKey = "";
    private ExpectedPrice ep;

    RoomsViewModel(Application application, DataSource dataManager, APIManager apiManager, ViewModelListener listener) {
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
                .subscribe(list -> {
                            mSearchResultList.postValue(list);
                            mListener.isFinished();
                        }
                        , exception ->
                                mListener.onError(mAppContext.getString(R.string.network_error))
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

    public void onClickPost() {
        String validResult = isRoomDataValid();

        if (!TextUtils.isEmpty(validResult)) {
            mListener.onError(validResult);
            return;
        }

        mListener.isWorking();

        OnFailedListener errorHandler = error -> {
            mListener.isFinished();
            mListener.onError(mAppContext.getString(R.string.network_error));
        };

        if (TextUtils.isEmpty(myKey)) {
            myKey = getDataManager().createKey();
        }

        mDataManager.uploadRoomImage(myKey, mImageList.getValue(),
                uriList -> push(myKey, uriList,
                        __ -> mDataManager.uploadLocationData(myKey, mAddress.getValue(),
                                ___ -> {
                                    mListener.isFinished();
                                    mListener.onSuccess("");
                                }, errorHandler)
                        , errorHandler)
                , errorHandler);
    }

    private void init() {
        mSearchResultList.setValue(new ArrayList<>());

        mPrice.setValue("");
        mFromDate.setValue(mAppContext.getString(R.string.tv_write_date));
        mToDate.setValue(mAppContext.getString(R.string.tv_write_date));
        mExpectedPrice.setValue(mAppContext.getString(R.string.tv_write_expected_value_default));
        mTitle.setValue("");
        mContent.setValue("");
        mOptionStd.setValue(false);
        mOptionGender.setValue(false);
        mOptionPet.setValue(false);
        mOptionSmoking.setValue(false);
        mImageList.setValue(new ArrayList<>());
        ep = new ExpectedPrice("", "", "");
    }

    private String isRoomDataValid() {
        String defaultDate = mAppContext.getString(R.string.tv_write_date);

        if (mImageList.getValue() != null && mImageList.getValue().size() == 0) {
            return ERROR_NO_SELECTION_IMAGE;

        } else if (TextUtils.isEmpty(mPrice.getValue())) {
            return ERROR_EMPTY_PRICE;

        } else if (TextUtils.equals(mFromDate.getValue(), defaultDate) ||
                TextUtils.equals(mToDate.getValue(), defaultDate) ||
                ep.getDiffDays() <= 0) {
            return ERROR_NO_SELECTION_DATE;

        } else if (mAddress.getValue() == null) {
            return ERROR_NO_SELECTION_ADDRESS;

        } else if (TextUtils.isEmpty(mSize.getValue())) {
            return ERROR_EMPTY_ROOM_SIZE;

        } else if (TextUtils.isEmpty(mTitle.getValue())) {
            return ERROR_EMPTY_TITLE;

        } else {
            return "";
        }
    }

    private void push(String key, List<String> urls,
                      OnSuccessListener<Void> onSuccessListener,
                      OnFailedListener onFailedListener) {

        String UUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        room.setValue(new Room(
                        key,
                        mPrice.getValue(),
                        mFromDate.getValue(),
                        mToDate.getValue(),
                        mTitle.getValue(),
                        mContent.getValue(),
                        urls,
                        UUID,
                        mAddress.getValue().getAddress(),
                        mAddress.getValue().getName(),
                        mSize.getValue(),
                        mOptionStd.getValue(),
                        mOptionGender.getValue(),
                        mOptionPet.getValue(),
                        mOptionSmoking.getValue(),
                        mAddress.getValue().getLatitude(),
                        mAddress.getValue().getLongitude(),
                        false
                )
        );

        getDataManager().setRoom(key, room.getValue(), onSuccessListener, onFailedListener);
    }

    private Single<List<Address>> searchAddress(String keyword) {
        return getDataManager().getPOIList(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(__ -> mListener.isWorking())
                .doOnSuccess(__ -> mListener.isFinished())
                .doAfterTerminate(() -> mListener.isFinished())
                .toObservable()
                .flatMap(Observable::fromIterable)
                .map(item -> new Address(item.name,
                        item.getPOIAddress().replace("null", ""),
                        item.getPOIPoint().getLongitude(), item.getPOIPoint().getLatitude()))
                .toList();
    }

    public void setRoomData(Room room) {
        if (room.getKey() != null) {
            myKey = room.getKey();
        }
        mPrice.setValue(room.getPrice());
        mFromDate.setValue(room.getFrom());
        mToDate.setValue(room.getTo());
        mOptionStd.setValue(room.isOptionStandard());
        mOptionGender.setValue(room.isOptionGender());
        mOptionPet.setValue(room.isOptionPet());
        mOptionSmoking.setValue(room.isOptionSmoking());
        mAddress.setValue(new Address(room.getAddressName(), room.getAddress()
                , room.getLongitude(), room.getLatitude()));
        mSize.setValue(room.getSize());
        mTitle.setValue(room.getTitle());
        mContent.setValue(room.getContent());
    }
}
