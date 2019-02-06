package com.swsnack.catchhouse.view.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.constants.Constants;
import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.data.userdata.api.AppAPIManager;
import com.swsnack.catchhouse.data.userdata.remote.AppUserDataManager;
import com.swsnack.catchhouse.databinding.ActivityBottomNavBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.view.fragments.HomeFragment;
import com.swsnack.catchhouse.view.fragments.MapFragment;
import com.swsnack.catchhouse.view.fragments.MyPageFragment;
import com.swsnack.catchhouse.view.fragments.SignInFragment;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;
import com.swsnack.catchhouse.viewmodel.roomsviewmodel.RoomsViewModelFactory;
import com.swsnack.catchhouse.viewmodel.searchviewmodel.SearchViewModel;
import com.swsnack.catchhouse.viewmodel.searchviewmodel.SearchViewModelFactory;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModelFactory;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class BottomNavActivity extends BaseActivity<ActivityBottomNavBinding> implements ViewModelListener, HomeFragment.OnSearchButtonListener {

    private FragmentManager mFragmentManager;
    private CompositeDisposable mDisposable;

    @Override
    protected int getLayout() {
        return R.layout.activity_bottom_nav;
    }

    @Override
    public void onError(String errorMessage) {
        unFreezeUI();
        showSnackMessage(errorMessage);
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
                mFragmentManager.beginTransaction().replace(R.id.fl_bottom_nav_container, new MyPageFragment(), MyPageFragment.class.getName()).commit();
                break;
            case Constants.UserStatus.DELETE_USER_SUCCESS:
                mFragmentManager.beginTransaction().replace(R.id.fl_bottom_nav_container, new SignInFragment(), SignInFragment.class.getName()).commit();
                break;
            case Constants.UserStatus.UPDATE_PASSWORD_SUCCESS:
                showSnackMessage(getString(R.string.snack_re_sign_in));
                mFragmentManager.beginTransaction().replace(R.id.fl_bottom_nav_container, new SignInFragment(), SignInFragment.class.getName()).commit();
                break;
            case Constants.UserStatus.UPDATE_PROFILE_SUCCESS:
                showSnackMessage(getString(R.string.snack_update_profile_success));
                break;
            case Constants.UserStatus.UPDATE_NICK_NAME_SUCCESS:
                showSnackMessage(getString(R.string.snack_change_nick_name_success));
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
        createViewModel(UserViewModel.class, new UserViewModelFactory(getApplication(),
                AppDataManager.getInstance(AppAPIManager.getInstance(), AppUserDataManager.getInstance(getApplication())),
                this));
        createViewModel(RoomsViewModel.class, new RoomsViewModelFactory(getApplication(), RoomsRepository.getInstance(), this));
        createViewModel(SearchViewModel.class, new SearchViewModelFactory(getApplication(), RoomsRepository.getInstance(), this));
    }

    private void onNavItemSelected(MenuItem item) {
        mDisposable.add(Single.just(item)
                .map(MenuItem::getItemId)
                .subscribe(id -> {
                    switch (id) {
                        case R.id.action_home:
                            // FIXME FragmentManager로 fragment를 replace하는것보다는 ViewPager를 활용해서 index를 변경하는 패턴으로 수정해주세요
                            // 이러한 방식으로 하는경우 버튼을 누를때마다 Fragment가 새로 만들어집니다.
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
                            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                mFragmentManager.beginTransaction().replace(R.id.fl_bottom_nav_container, new SignInFragment(), SignInFragment.class.getName()).commit();
                                return;
                            }
                            mFragmentManager.beginTransaction().replace(R.id.fl_bottom_nav_container, new MyPageFragment(), MyPageFragment.class.getName()).commit();
                            break;
                    }
                }));
    }

    @Override
    protected void onStart() {
        super.onStart();
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
