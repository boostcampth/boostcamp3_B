package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.asynctask.ConvertImageTask;
import com.swsnack.catchhouse.data.roomsdata.pojo.Address;
import com.swsnack.catchhouse.data.roomsdata.pojo.Room;
import com.swsnack.catchhouse.util.DateCalculator;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.swsnack.catchhouse.constants.Constants.PostException.EMPTY_PRICE_FIELD;
import static com.swsnack.catchhouse.constants.Constants.PostException.EMPTY_ROOM_IMAGE;
import static com.swsnack.catchhouse.constants.Constants.PostException.EMPTY_TITLE_FIELD;
import static com.swsnack.catchhouse.constants.Constants.PostException.NOT_SELECTION_DATE;

public class RoomsViewModel extends ReactiveViewModel {

    private static final String TAG = RoomsViewModel.class.getSimpleName();

    private Application mAppContext;
    private ViewModelListener mListener;
    private DataManager mDataManager;

    public final MutableLiveData<List<Address>> mSearchResultList = new MutableLiveData<>();
    public final MutableLiveData<String> mKeyword = new MutableLiveData<>();

    public final MutableLiveData<List<Uri>> mImageList = new MutableLiveData<>();

    public final MutableLiveData<String> mPrice = new MutableLiveData<>();
    public final MutableLiveData<String> mFromDate = new MutableLiveData<>();
    public final MutableLiveData<String> mToDate = new MutableLiveData<>();
    public final MutableLiveData<String> mExpectedPrice = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionStandard = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionPet = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionGender = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionSmoking = new MutableLiveData<>();
    public final MutableLiveData<Address> mAddress = new MutableLiveData<>();
    public final MutableLiveData<String> mTitle = new MutableLiveData<>();
    public final MutableLiveData<String> mContent = new MutableLiveData<>();

    RoomsViewModel(Application application, DataManager dataManager, ViewModelListener listener) {
        super(dataManager);
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

    public void onSelectFromDate(int year, int month, int day) {
        mFromDate.setValue(year + "-" +
                DateCalculator.refineDate(month) + "-" +
                DateCalculator.refineDate(day)
        );
        onChangePriceAndInterval();
    }

    public void onSelectToDate(int year, int month, int day) {
        mToDate.setValue(year + "-" +
                DateCalculator.refineDate(month) + "-" +
                DateCalculator.refineDate(day)
        );
        onChangePriceAndInterval();
    }

    public void onChangePriceAndInterval() {
        int diffDay;
        String price = mPrice.getValue();

        if (isPriceAndDateValid()) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                Date fromDate = formatter.parse(mFromDate.getValue());
                Date toDate = formatter.parse(mToDate.getValue());
                diffDay = DateCalculator.getDiffDate(fromDate, toDate);

                DecimalFormat myFormatter = new DecimalFormat("###,###");
                mExpectedPrice.setValue(
                        myFormatter.format(Integer.parseInt(price) * diffDay) +
                                "원" + "  (" + diffDay + "박)"
                );
            } catch (Exception e) {
                Log.e(TAG, "parse error", e);
            }
        }
    }

    public void onSearchAddress() {
        List<Address> addressList = mSearchResultList.getValue();
        String keyword = mKeyword.getValue();

        if (addressList != null) {
            addressList.clear();
            // replace api call
            addressList.add(new Address("test1", "1", 1, 1));
            addressList.add(new Address("test2", "2", 1, 1));
            addressList.add(new Address("test3", "3", 1, 1));
        }

        mSearchResultList.setValue(addressList);
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
            mListener.onError("error");
            return;
        }

        mListener.isWorking();

        OnFailureListener errorHandler = error -> {
            mListener.isFinished();
            mListener.onError("error");
        };

        mDataManager.createKey(
                key -> convert(
                        imageByte -> mDataManager.uploadRoomImage(key, imageByte,
                                urlList -> push(key, urlList,
                                        __ -> {
                                            mListener.isFinished();
                                            mListener.onSuccess("Success");
                                        }
                                        , errorHandler
                                ), errorHandler
                        ), errorHandler
                ), errorHandler);
    }

    private void init() {
        mKeyword.setValue("");
        mSearchResultList.setValue(new ArrayList<>());

        mPrice.setValue("");
        mFromDate.setValue(mAppContext.getString(R.string.tv_write_date));
        mToDate.setValue(mAppContext.getString(R.string.tv_write_date));
        mExpectedPrice.setValue(mAppContext.getString(R.string.tv_write_expected_value_default));
        mOptionStandard.setValue(false);
        mOptionPet.setValue(false);
        mOptionGender.setValue(false);
        mOptionSmoking.setValue(false);
        mTitle.setValue("");
        mContent.setValue("");

        mImageList.setValue(new ArrayList<>());
    }

    private boolean isPriceAndDateValid() {
        String defaultDate = mAppContext.getString(R.string.tv_write_date);

        return (!TextUtils.equals(mFromDate.getValue(), defaultDate) &&
                !TextUtils.equals(mToDate.getValue(), defaultDate) &&
                !TextUtils.isEmpty(mPrice.getValue())
        );
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
                         OnFailureListener onFailureListener) {
        AsyncTask<Uri, Void, List<byte[]>> mTask;
        mTask = new ConvertImageTask(mAppContext, onSuccessListener, onFailureListener);
        List<Uri> u = mImageList.getValue();
        mTask.execute(u.toArray(new Uri[0]));
    }

    private void push(String key, List<String> urls,
                      OnSuccessListener<Void> onSuccessListener,
                      OnFailureListener onFailureListener) {

        String UUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Room room = new Room(
                mPrice.getValue(),
                mFromDate.getValue(),
                mToDate.getValue(),
                mTitle.getValue(),
                mContent.getValue(),
                urls,
                UUID,
                mAddress.getValue(),
                mOptionStandard.getValue(),
                mOptionGender.getValue(),
                mOptionPet.getValue(),
                mOptionSmoking.getValue()
        );
        getDataManager().uploadRoomData(key, room, onSuccessListener, onFailureListener);
    }
}
