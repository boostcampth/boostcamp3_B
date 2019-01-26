package com.boostcampa2.catchhouse.viewmodel.userviewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.viewmodel.ReactiveViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class UserViewModel extends ReactiveViewModel {

    private Application mAppContext;
    public MutableLiveData<FirebaseUser> mFirebaseUser;
    public MutableLiveData<String> mEmail;
    public MutableLiveData<String> mPassword;

    UserViewModel(Application application) {
        super();
        mAppContext = application;
        mFirebaseUser = new MutableLiveData<>();
        mEmail = new MutableLiveData<>();
    }

    public GoogleSignInClient getGoogleSignUpInfo() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mAppContext.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(mAppContext, gso);
    }

    public void handleSignIn(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        GoogleSignInAccount account = task.getResult();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null))
                .addOnSuccessListener(user -> mFirebaseUser.setValue(user.getUser()))
                .addOnFailureListener(user -> mFirebaseUser.setValue(null));
    }

    public LiveData<FirebaseUser> getUserInfo() {
        return mFirebaseUser;
    }


}
