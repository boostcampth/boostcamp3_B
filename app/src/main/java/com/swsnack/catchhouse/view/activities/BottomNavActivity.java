package com.swsnack.catchhouse.view.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.constants.Constants;
import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.data.userdata.UserRepository;
import com.swsnack.catchhouse.databinding.ActivityBottomNavBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.view.fragments.MapFragment;
import com.swsnack.catchhouse.view.fragments.HomeFragment;
import com.swsnack.catchhouse.view.fragments.SignInFragment;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModelFactory;
import com.swsnack.catchhouse.viewmodel.searchviewmodel.SearchViewModel;
import com.swsnack.catchhouse.viewmodel.searchviewmodel.SearchViewModelFactory;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModelFactory;
import com.bumptech.glide.load.engine.GlideException;
import com.facebook.FacebookException;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class BottomNavActivity extends BaseActivity<ActivityBottomNavBinding> implements ViewModelListener, HomeFragment.OnSearchButtonListener {

    private FragmentManager mFragmentManager;
    private CompositeDisposable mDisposable;

    @Override
    protected int setLayout() {
        return R.layout.activity_bottom_nav;
    }

    @Override
    public void onError(Throwable throwable) {
        unFreezeUI();
        if (throwable instanceof FirebaseAuthInvalidCredentialsException) {
            Snackbar.make(getBinding().getRoot(), R.string.snack_invalid_user, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (throwable instanceof FirebaseAuthUserCollisionException) {
            Snackbar.make(getBinding().getRoot(), R.string.snack_already_exist_email, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (throwable instanceof GlideException) {
            Snackbar.make(getBinding().getRoot(), R.string.snack_failed_load_image, Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (throwable instanceof FacebookException || throwable instanceof GoogleAuthException) {
            Snackbar.make(getBinding().getRoot(), R.string.snack_fb_sign_in_failed, Snackbar.LENGTH_SHORT).show();
            return;
        }

        Snackbar.make(getBinding().getRoot(), R.string.snack_failed_sign_up, Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void onSuccess(String success) {
        unFreezeUI();
        switch (success) {
            case Constants.UserStatus.SIGN_UP_SUCCESS:
                mFragmentManager.popBackStack();
                break;
            case Constants.UserStatus.SIGN_IN_SUCCESS:
                /*handle here : when sign in success replace fragment to my page*/
                Snackbar.make(getBinding().getRoot(), "로그인 성공", Snackbar.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void isWorking() {
        freezeUI();
    }

    @Override
    public void isFinished() {
        unFreezeUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModels();
        mDisposable = new CompositeDisposable();
        mFragmentManager = getSupportFragmentManager();
        getBinding().bottomNav.setItemIconTintList(null);
        getBinding().bottomNav.setOnNavigationItemSelectedListener(v -> {
            onNavItemSelected(v);
            return true;
        });
        mFragmentManager.beginTransaction().add(R.id.fl_bottom_nav_container, new HomeFragment()).commit();
    }

    private void createViewModels() {
        createViewModel(UserViewModel.class, new UserViewModelFactory(getApplication(), UserRepository.getInstance(), this));
        createViewModel(RoomsViewModel.class, new RoomsViewModelFactory(getApplication(), RoomsRepository.getInstance(), this));
        createViewModel(SearchViewModel.class, new SearchViewModelFactory(getApplication(), RoomsRepository.getInstance(), this));
    }

    private void onNavItemSelected(MenuItem item) {
        mDisposable.add(Single.just(item)
                .map(MenuItem::getItemId)
                .subscribe(id -> {
                    switch (id) {
                        case R.id.action_home:
                            /* handle here: replace fragment on home btn Clicked */
                            mFragmentManager.beginTransaction().replace(R.id.fl_bottom_nav_container, new HomeFragment()).commit();
                            break;
                        case R.id.action_map:
                            mFragmentManager.beginTransaction().replace(R.id.fl_bottom_nav_container, new MapFragment()).commit();
                            break;
                        case R.id.action_message:
                            /* handle here: replace fragment on message btn Clicked */
                            break;
                        case R.id.action_my_page:
                            mFragmentManager.beginTransaction().replace(R.id.fl_bottom_nav_container, new SignInFragment(), SignInFragment.class.getName()).commit();
                            break;
                    }
                }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            /* User is logined. hadle here*/
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.dispose();
    }

    private void freezeUI() {
        getBinding().pgBottomNav.setVisibility(View.VISIBLE);
        getBinding().getRoot().setAlpha(0.6f);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void unFreezeUI() {
        getBinding().pgBottomNav.setVisibility(View.GONE);
        getBinding().getRoot().setAlpha(1.0f);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onClicked() {
        getBinding().bottomNav.setSelectedItemId(R.id.action_map);
    }

}
