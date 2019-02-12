package com.swsnack.catchhouse.viewmodel.postviewmodel;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.swsnack.catchhouse.AppApplication;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.roomdata.model.ExpectedPrice;
import com.swsnack.catchhouse.data.roomsdata.pojo.Room;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private ViewModelListener mListener;

    public final MutableLiveData<List<String>> mImageList = new MutableLiveData<>();
    public final MutableLiveData<String> mExpectedPrice = new MutableLiveData<>();
    public final MutableLiveData<String> mOptionTag = new MutableLiveData<>();

    PostViewModel(DataManager dataManager, ViewModelListener listener) {
        super(dataManager);
        mAppContext = AppApplication.getAppContext();
        mListener = listener;

        init();
    }

    public void setInitData(Room room) {
        mImageList.setValue(room.getImages());
        ExpectedPrice expectedPrice =
                new ExpectedPrice(room.getPrice(), room.getFrom(), room.getTo());

        mExpectedPrice.setValue(expectedPrice.onChangePriceAndInterval());

        mOptionTag.setValue(
                createOptionString(
                        room.isOptionStandard(),
                        room.isOptionGender(),
                        room.isOptionPet(),
                        room.isOptionSmoking()
                )
        );
    }

    private void init() {
        mImageList.setValue(new ArrayList<>());
        mExpectedPrice.setValue("");
        mOptionTag.setValue("");
    }

    private String createOptionString(boolean std, boolean gender, boolean pet, boolean smoking) {
        String tag = "";

        if (std) {
            tag += "#기본옵션 ";
        }

        if (gender) {
            tag += "#동일성별 ";
        }

        if (pet) {
            tag += "#반려동물가능 ";
        }

        if (smoking) {
            tag += "#흡연가능 ";
        }

        return tag;
    }

}
