package com.swsnack.catchhouse.viewmodel.postviewmodel;

import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.DataManager;
import com.swsnack.catchhouse.data.model.ExpectedPrice;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.util.DataConverter;
import com.swsnack.catchhouse.util.StringUtil;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PostViewModel extends ReactiveViewModel {

    public final MutableLiveData<List<String>> mImageList = new MutableLiveData<>();
    public final MutableLiveData<String> mExpectedPrice = new MutableLiveData<>();
    private final MutableLiveData<String> mOptionTag = new MutableLiveData<>();
    private MutableLiveData<Room> mRoom;
    private MutableLiveData<Boolean> mIsFavorite;
    private ViewModelListener mListener;

    PostViewModel(DataManager dataManager, APIManager apiManager, ViewModelListener listener) {
        super(dataManager, apiManager);
        init();
        mRoom = new MutableLiveData<>();
        mIsFavorite = new MutableLiveData<>();
        mListener = listener;
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

    private void checkFavoriteRoom() {
        if (getDataManager()
                .getFavoriteRoom(mRoom.getValue().getKey()) != null) {
            mIsFavorite.setValue(true);
            getDataManager().updateRoom(DataConverter.convertToRoomEntity(mRoom.getValue()));
        } else {
            mIsFavorite.setValue(false);
        }
    }

    public void setRoomData(Room roomData) {
        mRoom.setValue(roomData);
        checkFavoriteRoom();
        visitNewRoom();
    }

    public void addOrRemoveFavoriteRoom(View v) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            mListener.onError(StringUtil.getStringFromResource(R.string.not_singed));
            return;
        }

        if (!mIsFavorite.getValue()) {
            getDataManager()
                    .setFavoriteRoom(DataConverter.convertToRoomEntity(mRoom.getValue()));
            mIsFavorite.setValue(true);
        } else {
            getDataManager()
                    .deleteFavoriteRoom(DataConverter.convertToRoomEntity(mRoom.getValue()));
            mIsFavorite.setValue(false);
        }
    }

    public void visitNewRoom() {
        getDataManager()
                .setRecentRoom(mRoom.getValue());
    }

    public LiveData<Boolean> isFavorite() {
        return mIsFavorite;
    }

}
