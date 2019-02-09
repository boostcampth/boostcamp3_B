package com.swsnack.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.data.DataManager;
import com.swsnack.catchhouse.data.userdata.pojo.User;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import static com.swsnack.catchhouse.constants.Constants.FacebookData.GENDER;
import static com.swsnack.catchhouse.constants.Constants.FacebookData.NAME;
import static com.swsnack.catchhouse.constants.Constants.UserStatus.DELETE_USER_SUCCESS;
import static com.swsnack.catchhouse.constants.Constants.UserStatus.SIGN_IN_SUCCESS;
import static com.swsnack.catchhouse.constants.Constants.UserStatus.SIGN_UP_SUCCESS;
import static com.swsnack.catchhouse.constants.Constants.UserStatus.UPDATE_NICK_NAME_SUCCESS;
import static com.swsnack.catchhouse.constants.Constants.UserStatus.UPDATE_PASSWORD_SUCCESS;
import static com.swsnack.catchhouse.constants.Constants.UserStatus.UPDATE_PROFILE_SUCCESS;

public class UserViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private ViewModelListener mListener;
    private Uri mProfileUri;
    private MutableLiveData<String> mGender;
    public MutableLiveData<Boolean> mIsSigned;
    public MutableLiveData<String> mEmail;
    public MutableLiveData<String> mPassword;
    public MutableLiveData<String> mNickName;
    public MutableLiveData<Bitmap> mProfile;

    UserViewModel(Application application, DataManager dataManager, ViewModelListener listener) {
        super(dataManager);
        this.mAppContext = application;
        this.mListener = listener;
        this.mGender = new MutableLiveData<>();
        this.mIsSigned = new MutableLiveData<>();
        this.mEmail = new MutableLiveData<>();
        this.mPassword = new MutableLiveData<>();
        this.mNickName = new MutableLiveData<>();
        this.mProfile = new MutableLiveData<>();
        mIsSigned.setValue(false);
    }

    public void setGender(String gender) {
        mGender.setValue(gender);
    }

    public LiveData getGender() {
        return mGender;
    }

    private String getStringFromResource(int stringResource) {
        return mAppContext.getString(stringResource);
    }

    public void getProfileFromUri(Uri uri) {
        mListener.isWorking();
        mProfileUri = uri;
        getDataManager()
                .getProfile(uri, new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        mListener.onError(getStringFromResource(R.string.snack_failed_load_image));
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        mListener.isFinished();
                        mProfile.setValue(resource);
                        return true;
                    }
                });
    }

    public void getUser() {
        mIsSigned.setValue(true);
        getDataManager()
                .getUserAndListeningForChanging(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        user -> {
                            if (user == null) {
                                return;
                            }
                            mEmail.setValue(user.getEMail());
                            mNickName.setValue(user.getNickName());
                            mGender.setValue(user.getGender());
                            if (user.getProfile() != null) {
                                getProfileFromUri(Uri.parse(user.getProfile()));
                            }
                            mIsSigned.setValue(true);
                        },
                        error -> mListener.onError(getStringFromResource(R.string.snack_database_exception)));
    }

    public void signInWithGoogle(Intent data) {
        mListener.isWorking();
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount account = task.getResult();
        User user = new User(account.getDisplayName());
        if (account.getPhotoUrl() != null) {
            user.setProfile(account.getPhotoUrl().toString());
        }
        signUpWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null), user);
    }

    public void signInWithFacebook(LoginResult loginResult, Uri uri) {
        mListener.isWorking();
        getDataManager()
                .getUserInfoFromFacebook(loginResult.getAccessToken(),
                        (result, response) -> {
                            if (result == null) {
                                mListener.onError(getStringFromResource(R.string.snack_not_found_info));
                                return;
                            }
                            User user = new User(result.optString(NAME), result.optString(GENDER));
                            user.setProfile(uri.toString());
                            signUpWithCredential(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()), user);
                        });
    }

    private void signUpWithCredential(AuthCredential authCredential, User user) {
        getDataManager()
                .signUpAndSetUser(authCredential,
                        user,
                        result -> mListener.onSuccess(SIGN_IN_SUCCESS),
                        error -> mListener.onError(getStringFromResource(R.string.snack_fb_sign_up_failed)));
    }

    public void signUpWithEmail(View v) {
        if (mPassword.getValue() == null || mPassword.getValue().length() < 6) {
            mListener.onError(getStringFromResource(R.string.snack_short_password));
            return;
        }

        if (mEmail.getValue() == null || mPassword.getValue() == null ||
                mNickName.getValue() == null || mGender.getValue() == null ||
                mEmail.getValue().trim().equals("") || mPassword.getValue().trim().equals("") ||
                mNickName.getValue().trim().equals("") || mGender.getValue() == null) {
            mListener.onError(getStringFromResource(R.string.snack_fill_info));
            return;
        }

        mListener.isWorking();

        User user = new User(mEmail.getValue(), mNickName.getValue(), mGender.getValue());

        getDataManager()
                .signUpAndSetUser(mPassword.getValue(),
                        user,
                        mProfileUri,
                        result -> mListener.onSuccess(SIGN_UP_SUCCESS),
                        error -> {
                            if (error instanceof FirebaseAuthUserCollisionException) {
                                mListener.onError(getStringFromResource(R.string.snack_already_exist_email));
                                return;
                            }
                            mListener.onError(getStringFromResource(R.string.snack_error_occured));
                        });
    }

    public void signInWithEmail(View v) {
        if (mEmail.getValue() == null || mPassword.getValue() == null
                || mEmail.getValue().trim().equals("") || mPassword.getValue().trim().equals("")) {
            mListener.onError(getStringFromResource(R.string.snack_fill_info));
            return;
        }

        if (mPassword.getValue() == null || mPassword.getValue().length() < 6) {
            mListener.onError(getStringFromResource(R.string.snack_short_password));
            return;
        }
        mListener.isWorking();

        getDataManager()
                .signIn(mEmail.getValue(),
                        mPassword.getValue(),
                        authResult -> {
                            mListener.onSuccess(SIGN_IN_SUCCESS);
                            mIsSigned.setValue(true);
                        },
                        error -> {
                            if (error instanceof FirebaseAuthInvalidUserException
                                    || error instanceof FirebaseAuthInvalidCredentialsException) {
                                mListener.onError(getStringFromResource(R.string.snack_invalid_user));
                            }
                        });
    }

    public void deleteUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            mListener.onError(getStringFromResource(R.string.snack_fb_not_signed_user));
            return;
        }
        mListener.isWorking();

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getDataManager()
                .deleteUserAll(uuid, deleteResult -> {
                    mListener.onSuccess(DELETE_USER_SUCCESS);
                    mIsSigned.setValue(false);
                }, error -> mListener.onError(getStringFromResource(R.string.snack_error_occured)));
    }

    public void changeNickName(String changeNickName) {
        if (changeNickName.equals(mNickName.getValue())) {
            mListener.onError(getStringFromResource(R.string.snack_same_nick_name));
            return;
        }

        getDataManager()
                .updateNickName(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        changeNickName,
                        success -> mListener.onSuccess(UPDATE_NICK_NAME_SUCCESS),
                        error -> mListener.onError(getStringFromResource(R.string.snack_duplicate_nick_name)));
    }

    public void updatePassword(@NonNull String oldPassword, @NonNull String newPassword) {
        getDataManager()
                .updatePassword(oldPassword, newPassword,
                        result -> mListener.onSuccess(UPDATE_PASSWORD_SUCCESS),
                        error -> mListener.onError(getStringFromResource(R.string.snack_error_update_password)));
    }

    public void updateProfile(Uri uri) {
        mListener.isWorking();
        getDataManager().
                updateProfile(FirebaseAuth.getInstance().getCurrentUser().getUid(), uri,
                        result -> mListener.onSuccess(UPDATE_PROFILE_SUCCESS),
                        error -> mListener.onError(getStringFromResource(R.string.snack_update_profile_failed)));
    }
}
