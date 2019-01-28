package com.boostcampa2.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.data.userdata.UserRepository;
import com.boostcampa2.catchhouse.data.userdata.pojo.User;
import com.boostcampa2.catchhouse.viewmodel.ReactiveViewModel;
import com.boostcampa2.catchhouse.viewmodel.ViewModelListener;
import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserViewModel extends ReactiveViewModel {

    private Application mAppContext;
    private UserRepository mRepository;
    private ViewModelListener mListener;
    private MutableLiveData<FirebaseUser> mFirebaseUser;
    private User mUser;
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
        this.mFirebaseUser = new MutableLiveData<>();
        this.mEmail = new MutableLiveData<>();
        this.mPassword = new MutableLiveData<>();
        this.mNickName = new MutableLiveData<>();
        this.mGender = new MutableLiveData<>();
        this.mProfile = new MutableLiveData<>();
    }

    public void getBitmapFromData(Uri uri) {
        mListener.isWorking();
        getCompositeDisposable().add(
                Single.create(subscriber -> {
                    Bitmap bitmap = Glide.with(mAppContext)
                            .asBitmap()
                            .load(uri)
                            .submit()
                            .get();
                    subscriber.onSuccess(bitmap);
                })
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(bitmap -> {
                            mProfile.setValue((Bitmap) bitmap);
                            mListener.isFinished();
                        }, error -> mListener.onError(error)));
    }

    public GoogleSignInClient requestGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mAppContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(mAppContext, gso);
    }

    public void signUpFirebaseWithGoogle(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount account = task.getResult();
        mUser = new User(account.getDisplayName());
        if (account.getPhotoUrl() != null) {
            mUser.setProfile(account.getPhotoUrl().toString());
        }
        handlingSignUpData(GoogleAuthProvider.getCredential(account.getIdToken(), null));
    }

    public LoginManager requestFacebookSignIn(CallbackManager callbackManager) {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mListener.isWorking();
                getDetailDataFromFaceBook(loginResult);
            }

            @Override
            public void onCancel() {
                mListener.isFinished();
            }

            @Override
            public void onError(FacebookException error) {
                mListener.isFinished();
                mListener.onError(error);
            }
        });
        return loginManager;
    }

    public void signUpWithEmail() {
        mListener.isWorking();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mEmail.getValue(), mPassword.getValue())
                .addOnSuccessListener(authResult -> {
                    mUser = new User(mEmail.getValue(), mNickName.getValue(), mGender.getValue());
                    mFirebaseUser.setValue(authResult.getUser());
                    getCompositeDisposable().add(mRepository.setUserToRemote(mFirebaseUser.getValue().getUid(), mUser)
                            .subscribe(() -> mListener.isFinished(), error -> mListener.onError(error)));
                }).addOnFailureListener(error -> mListener.onError(error));
    }

    private void getDetailDataFromFaceBook(LoginResult loginResult) {
        mListener.isWorking();
        getCompositeDisposable().add(mRepository.getDetailInfoFromRemote(loginResult.getAccessToken())
                .subscribeOn(Schedulers.io())
                .subscribe(info -> mUser = info));
        handlingSignUpData(FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken()));
    }

    private void handlingSignUpData(AuthCredential credential) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential).addOnSuccessListener(success -> {
            String uuid = auth.getCurrentUser().getUid();
            getCompositeDisposable().add(mRepository.setUserToRemote(uuid, mUser)
                    .subscribe(() -> {
                        mListener.isFinished();
                        mFirebaseUser.setValue(auth.getCurrentUser());
                    }, error -> mListener.onError(error)));
        })
                .addOnFailureListener(error -> {
                    mListener.isFinished();
                    mListener.onError(error);
                });
    }

    public LiveData<FirebaseUser> getUserInfo() {
        return mFirebaseUser;
    }

    public void setGender(String gender) {
        mGender.setValue(gender);
    }

    public LiveData<String> getGender() {
        return mGender;
    }
}
