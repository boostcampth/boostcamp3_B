package com.swsnack.catchhouse.viewmodel.postviewmodel;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.model.ExpectedPrice;
import com.swsnack.catchhouse.data.model.Room;
import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.DataDataSource;
import com.swsnack.catchhouse.util.StringUtil;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import static com.swsnack.catchhouse.util.StringUtil.getStringFromResource;

public class PostViewModel extends ReactiveViewModel {

    public final MutableLiveData<String> mExpectedPrice = new MutableLiveData<>();
    public final MutableLiveData<String> mOptionTag = new MutableLiveData<>();
    public final MutableLiveData<String> mNickName = new MutableLiveData<>();
    public final MutableLiveData<String> mGender = new MutableLiveData<>();
    public final MutableLiveData<Bitmap> mProfile = new MutableLiveData<>();
    public final MutableLiveData<Boolean> mIsWriter = new MutableLiveData<>();

    public MutableLiveData<Room> room;
    private MutableLiveData<Boolean> mIsFavorite;
    private ViewModelListener mListener;

    PostViewModel(DataDataSource dataManager, APIManager apiManager, ViewModelListener listener) {
        super(dataManager, apiManager);
        init();
        room = new MutableLiveData<>();
        mIsFavorite = new MutableLiveData<>();
        mListener = listener;
    }

    //TODO: 1
    public void deleteMyPost() {

    }

    private void getProfileFromUri(Uri uri) {
        mListener.isWorking();
        getDataManager()
                .getProfile(uri,
                        bitmap -> {
                            mListener.isFinished();
                            mProfile.setValue(bitmap);
                        },
                        error -> mListener.onError(getStringFromResource(R.string.snack_failed_load_image)));
    }

    private void init() {
        mExpectedPrice.setValue("");
        mOptionTag.setValue("");
        mNickName.setValue("");
        mGender.setValue("");
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
                .getFavoriteRoom(room.getValue().getKey()) != null) {
            mIsFavorite.setValue(true);
            getDataManager().updateRoom(room.getValue());
        } else {
            mIsFavorite.setValue(false);
        }
    }

    public void setRoomData(Room roomData) {
        room.setValue(roomData);
        checkFavoriteRoom();
        visitNewRoom();

        mIsWriter.setValue(checkIsWriter(roomData.getUuid()));
        ExpectedPrice expectedPrice =
                new ExpectedPrice(roomData.getPrice(), roomData.getFrom(), roomData.getTo());
        mExpectedPrice.setValue(expectedPrice.getExpectedPrice());

        mOptionTag.setValue(
                createOptionString(
                        roomData.isOptionStandard(),
                        roomData.isOptionGender(),
                        roomData.isOptionPet(),
                        roomData.isOptionSmoking()
                )
        );

        getDataManager()
                .getUserAndListeningForChanging(roomData.getUuid(),
                        user -> {
                            if (user == null) {
                                return;
                            }
                            mNickName.setValue(user.getNickName());
                            mGender.setValue(user.getGender());
                            if (user.getProfile() != null) {
                                getProfileFromUri(Uri.parse(user.getProfile()));
                            }
                        },
                        error -> mListener.onError(getStringFromResource(R.string.snack_database_exception)));
    }

    public void addOrRemoveFavoriteRoom(View v) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            mListener.onError(StringUtil.getStringFromResource(R.string.not_singed));
            return;
        }

        mListener.isWorking();

        if (!mIsFavorite.getValue()) {
            getDataManager()
                    .setFavoriteRoom(room.getValue());
            mIsFavorite.setValue(true);
            mListener.isFinished();
        } else {
            getDataManager()
                    .deleteFavoriteRoom(room.getValue());
            mIsFavorite.setValue(false);
            mListener.isFinished();

        }
    }

    public void onClickDeleteButton(View view) {
        Room deletingRoom = room.getValue();
        deletingRoom.setDeleted(true);

        getDataManager().setRoom(deletingRoom.getKey(), deletingRoom,
                __ -> mListener.onSuccess(""),
                error -> mListener.onError(error.getMessage())
        );
    }

    private void visitNewRoom() {
        getDataManager()
                .setRecentRoom(room.getValue());
    }

    public LiveData<Boolean> isFavorite() {
        return mIsFavorite;
    }

    private boolean checkIsWriter(String uuid) {
        String myKey = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return TextUtils.equals(uuid, myKey);
    }
}
