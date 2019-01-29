package com.boostcampa2.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.boostcampa2.catchhouse.constants.Constants;
import com.boostcampa2.catchhouse.data.userdata.UserRepository;
import com.boostcampa2.catchhouse.data.userdata.pojo.User;
import com.boostcampa2.catchhouse.util.DataConverter;
import com.boostcampa2.catchhouse.viewmodel.ReactiveViewModel;
import com.boostcampa2.catchhouse.viewmodel.ViewModelListener;
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

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private UserRepository mRepository;
    private ViewModelListener mListener;
    private byte[] mProfileByteArray;
    public MutableLiveData<String> mEmail;
    public MutableLiveData<String> mPassword;
    public MutableLiveData<String> mNickName;
    private MutableLiveData<String> mGender;
    public MutableLiveData<Bitmap> mProfile;

    UserViewModel(Application application, UserRepository repository, ViewModelListener listener) {
        super();
        this.mAppContext = application;
        this.mRepository = repository;
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
                                                error -> mListener.onError(error))),
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
        authWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null), user);
    }

    public void signInWithFacebook(LoginResult loginResult, Uri uri) {
        mListener.isWorking();
        getCompositeDisposable()
                .add(mRepository.getDetailInfoFromRemote(loginResult.getAccessToken())
                        .subscribeOn(Schedulers.io())
                        .subscribe(user -> {
                            user.setProfile(uri.toString());
                            authWithCredential(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()), user);
                        }));
    }

    private void authWithCredential(AuthCredential credential, User user) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential)
                .addOnSuccessListener(success -> {
                    String uuid = auth.getCurrentUser().getUid();
                    setUserToRemote(uuid, user, Constants.UserStatus.SIGN_IN_SUCCESS);
                })
                .addOnFailureListener(error -> mListener.onError(error));
    }

    private void setUserToRemote(String uid, User user, String userStatusFlag) {
        getCompositeDisposable().add(mRepository.setUserToRemote(uid, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mListener.onSuccess(userStatusFlag),
                        error -> {
                            FirebaseAuth.getInstance().getCurrentUser().delete();
                            FirebaseStorage.getInstance().getReference().child(uid).delete();
                            mListener.onError(error);
                        }));
    }

    private void setProfileAndWriteToRemote(String uid, User user) {
        getCompositeDisposable().add(
                mRepository.saveProfileAndGetUrl(uid, mProfileByteArray)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(path -> {
                                    user.setProfile(path.toString());
                                    setUserToRemote(uid, user, Constants.UserStatus.SIGN_UP_SUCCESS);
                                },
                                error -> {
                                    mListener.onError(error);
                                    FirebaseAuth.getInstance().getCurrentUser().delete();
                                }));
    }

    public void signUpWithEmail() {
        mListener.isWorking();
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(mEmail.getValue(), mPassword.getValue())
                .addOnSuccessListener(authResult -> {
                    String uid = authResult.getUser().getUid();
                    User user = new User(mEmail.getValue(), mNickName.getValue(), mGender.getValue());
                    if (mProfile.getValue() != null) {
                        //Firebase 인증 성공시, profile image가 있는지 확인,
                        //확인 후, storage 저장, 그 후 storage의 경로와 함께 유저 정보 db 저장
                        setProfileAndWriteToRemote(uid, user);
                    } else {
                        //profile image 가 없다면, db에 바로 유저 정보 저장
                        setUserToRemote(uid, user, Constants.UserStatus.SIGN_UP_SUCCESS);
                    }
                }).addOnFailureListener(error -> mListener.onError(error));
    }

    public void signInWithEmail() {
        mListener.isWorking();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail.getValue(), mPassword.getValue())
                .addOnSuccessListener(authResult -> mListener.onSuccess(Constants.UserStatus.SIGN_IN_SUCCESS))
                .addOnFailureListener(error -> mListener.onError(error));
    }

    public void setGender(String gender) {
        mGender.setValue(gender);
    }

    public LiveData<String> getGender() {
        return mGender;
    }
}
