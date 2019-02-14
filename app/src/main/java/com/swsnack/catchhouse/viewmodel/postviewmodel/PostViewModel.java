package com.swsnack.catchhouse.viewmodel.postviewmodel;

import androidx.lifecycle.MutableLiveData;


import com.swsnack.catchhouse.data.APIManager;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.pojo.Room;
import com.swsnack.catchhouse.data.model.ExpectedPrice;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ReactiveViewModel {

    public final MutableLiveData<List<String>> mImageList = new MutableLiveData<>();
    public final MutableLiveData<String> mExpectedPrice = new MutableLiveData<>();
    public final MutableLiveData<String> mOptionTag = new MutableLiveData<>();

    PostViewModel(DataManager dataManager, APIManager apiManager, ViewModelListener listener) {
        super(dataManager, apiManager);

        init();
    }

    public void setInitRoomData(Room room) {
        mImageList.setValue(room.getImages());
        ExpectedPrice expectedPrice =
                new ExpectedPrice(room.getPrice(), room.getFrom(), room.getTo());

        mExpectedPrice.setValue(expectedPrice.getExpectedPrice());

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
