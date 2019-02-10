package com.swsnack.catchhouse.view.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapters.ViewPagerAdapter;
import com.swsnack.catchhouse.constants.Constants;
import com.swsnack.catchhouse.data.AppDataManager;
import com.swsnack.catchhouse.data.chattingdata.remote.RemoteChattingManager;
import com.swsnack.catchhouse.data.roomsdata.RoomsRepository;
import com.swsnack.catchhouse.data.userdata.api.AppAPIManager;
import com.swsnack.catchhouse.data.userdata.remote.AppUserDataManager;
import com.swsnack.catchhouse.databinding.ActivityBottomNavBinding;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.view.fragments.ChatListFragment;
import com.swsnack.catchhouse.view.fragments.HomeFragment;
import com.swsnack.catchhouse.view.fragments.MapFragment;
import com.swsnack.catchhouse.view.fragments.MyPageFragment;
import com.swsnack.catchhouse.view.fragments.SignFragment;
import com.swsnack.catchhouse.view.fragments.SignInFragment;
import com.swsnack.catchhouse.viewmodel.ViewModelListener;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModelFactory;
import com.swsnack.catchhouse.viewmodel.searchviewmodel.SearchViewModel;
import com.swsnack.catchhouse.viewmodel.searchviewmodel.SearchViewModelFactory;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class BottomNavActivity extends BaseActivity<ActivityBottomNavBinding> implements ViewModelListener, HomeFragment.OnSearchButtonListener {

    private FragmentManager mFragmentManager;
    private CompositeDisposable mDisposable;
    private OnViewPagerChangedListener mViewPagerListener;

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
                mFragmentManager.beginTransaction().replace(R.id.fl_sign_container, new MyPageFragment(), MyPageFragment.class.getName()).commit();
                break;
            case Constants.UserStatus.DELETE_USER_SUCCESS:
                mFragmentManager.beginTransaction().replace(R.id.fl_sign_container, new SignInFragment(), SignInFragment.class.getName()).commit();
                break;
            case Constants.UserStatus.UPDATE_PASSWORD_SUCCESS:
                showSnackMessage(getString(R.string.snack_re_sign_in));
                mFragmentManager.beginTransaction().replace(R.id.fl_sign_container, new SignInFragment(), SignInFragment.class.getName()).commit();
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

        init();

        getBinding().bottomNav.setItemIconTintList(null);
        getBinding().bottomNav.setOnNavigationItemSelectedListener(v -> {
            onNavItemSelected(v);
            return true;
        });
    }

    private void createViewModels() {
        createViewModel(UserViewModel.class, new UserViewModelFactory(getApplication(),
                AppDataManager.getInstance(AppAPIManager.getInstance(), AppUserDataManager.getInstance(), RemoteChattingManager.getInstance()),
                this));
        createViewModel(SearchViewModel.class, new SearchViewModelFactory(getApplication(), RoomsRepository.getInstance(), this));
        createViewModel(ChattingViewModel.class, new ChattingViewModelFactory(this));
    }


    private void onNavItemSelected(MenuItem item) {
        mDisposable.add(Single.just(item)
                .map(MenuItem::getItemId)
                .subscribe(id -> {
                    switch (id) {
                        case R.id.action_home:
                            getBinding().vpBottomNav.setCurrentItem(0);
                            break;
                        case R.id.action_map:
                            getBinding().vpBottomNav.setCurrentItem(1);
                            break;
                        case R.id.action_message:
                            getBinding().vpBottomNav.setCurrentItem(2);
                            break;
                        case R.id.action_my_page:
                            getBinding().vpBottomNav.setCurrentItem(3);
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

    private void init() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mFragmentManager);
        List<Fragment> list = new ArrayList<>();

        list.add(new HomeFragment());
        //FIXME MapFragment에서 inflating 과정 중, NPE 발생합니다. 수정 부탁드려요
        list.add(new MapFragment());
        list.add(new ChatListFragment());
        list.add(new SignFragment());

        getBinding().vpBottomNav.setAdapter(viewPagerAdapter);
        getBinding().vpBottomNav.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        getBinding().bottomNav.setSelectedItemId(R.id.action_home);
                        break;
                    case 1:
                        getBinding().bottomNav.setSelectedItemId(R.id.action_map);
                        break;
                    case 2:
                        getBinding().bottomNav.setSelectedItemId(R.id.action_message);
                        mViewPagerListener.onViewPagerChanged();
                        break;
                    case 3:
                        getBinding().bottomNav.setSelectedItemId(R.id.action_my_page);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPagerAdapter.setItems(list);
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

    public void setViewPagerListener(OnViewPagerChangedListener onViewPagerChangedListener) {
        this.mViewPagerListener = onViewPagerChangedListener;
    }
}
