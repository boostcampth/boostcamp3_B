package com.swsnack.catchhouse.viewmodel.roomsviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.util.DateCalculator;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RoomsViewModel extends ReactiveViewModel {

    private static final String TAG = RoomsViewModel.class.getSimpleName();

    private Application mAppContext;
    private ViewModelListener mListener;
    public final MutableLiveData<List<Uri>> mImageList = new MutableLiveData<>();
    public final MutableLiveData<String> mPrice = new MutableLiveData<>();
    public final MutableLiveData<String> mFromDate = new MutableLiveData<>();
    public final MutableLiveData<String> mToDate = new MutableLiveData<>();
    public final MutableLiveData<String> mExpectedPrice = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionStandard = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionPet = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionGender = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mOptionSmoking = new MutableLiveData<>();
    // public final MutableLiveData<Address> mAddress = new MutableLiveData<>();
    public final MutableLiveData<String> mTitle = new MutableLiveData<>();
    public final MutableLiveData<String> mContent = new MutableLiveData<>();

    RoomsViewModel(Application application, DataManager dataManager, ViewModelListener listener) {
        super(dataManager);
        mAppContext = application;
        mListener = listener;

        init();
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

        if (isPriceAndDateValid()) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                Date fromDate = formatter.parse(mFromDate.getValue());
                Date toDate = formatter.parse(mToDate.getValue());
                diffDay = DateCalculator.getDiffDate(fromDate, toDate);

                DecimalFormat myFormatter = new DecimalFormat("###,###");
                mExpectedPrice.setValue(
                        myFormatter.format(Integer.parseInt(mPrice.getValue()) * diffDay) +
                                "원" + "  (" + diffDay + "박)"
                );
            } catch (Exception e) {
                Log.e(TAG, "parse error", e);
            }
        }
    }

    public void onSelectImage(List<Uri> uriList) {
        List<Uri> currentList = mImageList.getValue();

        mListener.isWorking();
        for (Uri uri : uriList) {
            if (currentList != null && !currentList.contains(uri)) {
                currentList.add(uri);
            }
        }

        mImageList.setValue(currentList);
    }

    public void onSearchAddress() {
        return;
    }
    private void init() {
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
}
