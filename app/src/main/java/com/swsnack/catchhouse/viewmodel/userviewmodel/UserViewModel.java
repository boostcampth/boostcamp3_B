package com.swsnack.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.storage.FirebaseStorage;
import com.swsnack.catchhouse.constants.Constants;
import com.swsnack.catchhouse.data.userdata.pojo.User;
import com.swsnack.catchhouse.util.DataConverter;
import com.swsnack.catchhouse.viewmodel.ReactiveViewModel;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.IN_SUFFICIENT_INFO;
import static com.swsnack.catchhouse.constants.Constants.ExceptionReason.SHORT_PASSWORD;

public class UserViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private ViewModelListener mListener;
    private byte[] mProfileByteArray;
    public MutableLiveData<String> mEmail;
    public MutableLiveData<String> mPassword;
    public MutableLiveData<String> mNickName;
    private MutableLiveData<String> mGender;
    public MutableLiveData<Bitmap> mProfile;

    UserViewModel(Application application, ViewModelListener listener) {
        super();
        this.mAppContext = application;
        this.mListener = listener;
        this.mEmail = new MutableLiveData<>();
        this.mPassword = new MutableLiveData<>();
        this.mNickName = new MutableLiveData<>();
        this.mGender = new MutableLiveData<>();
        this.mProfile = new MutableLiveData<>();
    }

    public void getBitmapAndByteArrayFromUri(Uri uri) {
        mListener.isWorking();
        getCompositeDisposable().add(
                Single.create(subscriber -> {
                    Bitmap bitmap = Glide.with(mAppContext)
                            .asBitmap()
                            .load(uri)
                            .submit()
                            .get();
                    mProfile.postValue(bitmap);
                    subscriber.onSuccess(DataConverter.getScaledBitmap(bitmap));
                })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bitmap -> getCompositeDisposable().add(DataConverter.getByteArray((Bitmap) bitmap)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(byteArray -> {
                                                    mProfileByteArray = byteArray;
                                                    mListener.isFinished();
                                                },
                                                mListener::onError)),
                                error -> mListener.onError(error)));
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
        getCompositeDisposable()
                .add(getDataManager()
                        .facebookUserProfile(loginResult.getAccessToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            user.setProfile(uri.toString());
                            signUpWithCredential(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()), user);
                        }));
    }

    private void signUpWithCredential(AuthCredential authCredential, User user) {
        getCompositeDisposable()
                .add(getDataManager()
                        .firebaseSignUp(authCredential)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(uuid -> setUser(uuid, user, Constants.UserStatus.SIGN_IN_SUCCESS),
                                mListener::onError));
    }

    public void signUpWithEmail(View v) {
        if (mPassword.getValue().length() < 6) {
            mListener.onError(new InSufficientException(SHORT_PASSWORD));
            return;
        }
        if (mEmail.getValue().trim().equals("")
                && mPassword.getValue().trim().equals("")
                && mNickName.getValue().trim().equals("")
                && mGender.getValue() == null) {
            mListener.onError(new InSufficientException(IN_SUFFICIENT_INFO));
            return;
        }
        mListener.isWorking();
        getCompositeDisposable()
                .add(getDataManager()
                        .firebaseSignUp(mEmail.getValue(), mPassword.getValue())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(uuid -> {
                            User user = new User(mEmail.getValue(), mNickName.getValue(), mGender.getValue());
                            if (mProfileByteArray != null) {
                                setProfile(uuid, user);
                                return;
                            }
                            setUser(uuid, user, Constants.UserStatus.SIGN_UP_SUCCESS);
                        }, mListener::onError));
    }

    private void setUser(String uuid, User user, String userStatusFlag) {
        getCompositeDisposable()
                .add(getDataManager()
                        .setUser(uuid, user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> mListener.onSuccess(userStatusFlag),
                                error -> {
                                    mListener.onError(error);
                                    FirebaseAuth.getInstance().getCurrentUser().delete();
                                    FirebaseStorage.getInstance().getReference(Constants.FirebaseKey.STORAGE_PROFILE).child(uuid).delete();
                                }));
    }

    private void setProfile(String uuid, User user) {
        getCompositeDisposable()
                .add(getDataManager()
                        .setProfile(uuid, mProfileByteArray)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(path -> {
                                    user.setProfile(path);
                                    setUser(uuid, user, Constants.UserStatus.SIGN_UP_SUCCESS);
                                },
                                error -> {
                                    mListener.onError(error);
                                    FirebaseAuth.getInstance().getCurrentUser().delete();
                                }));
    }

    public void signInWithEmail(View v) {
        if (mEmail.getValue().trim().equals("") && mPassword.getValue().trim().equals("")) {
            mListener.onError(new InSufficientException(Constants.ExceptionReason.IN_SUFFICIENT_INFO));
            return;
        }

        if (mPassword.getValue().length() < 6) {
            mListener.onError(new InSufficientException(SHORT_PASSWORD));
            return;
        }
        mListener.isWorking();
        getCompositeDisposable()
                .add(getDataManager()
                        .firebaseSignIn(mEmail.getValue(), mPassword.getValue())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> mListener.onSuccess(Constants.UserStatus.SIGN_IN_SUCCESS),
                                mListener::onError));
    }

    public void setGender(String gender) {
        mGender.setValue(gender);
    }

    public LiveData<String> getGender() {
        return mGender;
    }
}
