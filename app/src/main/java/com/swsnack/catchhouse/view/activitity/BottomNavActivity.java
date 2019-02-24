package com.swsnack.catchhouse.view.activitity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.skt.Tmap.TMapTapi;
import com.swsnack.catchhouse.Constant;
import com.swsnack.catchhouse.R;
import com.swsnack.catchhouse.adapter.ViewPagerAdapter;
import com.swsnack.catchhouse.databinding.ActivityBottomNavBinding;
import com.swsnack.catchhouse.repository.APIManager;
import com.swsnack.catchhouse.repository.AppDataSource;
import com.swsnack.catchhouse.view.BaseActivity;
import com.swsnack.catchhouse.view.fragment.ChatListFragment;
import com.swsnack.catchhouse.view.fragment.MyPageFragment;
import com.swsnack.catchhouse.view.fragment.SearchFragment;
import com.swsnack.catchhouse.view.fragment.SignFragment;
import com.swsnack.catchhouse.view.fragment.SignInFragment;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModel;
import com.swsnack.catchhouse.viewmodel.chattingviewmodel.ChattingViewModelFactory;
import com.swsnack.catchhouse.viewmodel.searchingviewmodel.SearchingViewModel;
import com.swsnack.catchhouse.viewmodel.searchingviewmodel.SearchingViewModelFactory;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModel;
import com.swsnack.catchhouse.viewmodel.userviewmodel.UserViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

import static com.swsnack.catchhouse.Constant.ParcelableData.BOTTOM_NAVIGATION_POSITION;

public class BottomNavActivity extends BaseActivity<ActivityBottomNavBinding> {

    private FragmentManager mFragmentManager;
    private CompositeDisposable mDisposable;
    private OnViewPagerChangedListener mViewPagerListener;

    @Override
    protected int getLayout() {
        return R.layout.activity_bottom_nav;
    }

    @Override
    public void onSuccess(String success) {
        super.onSuccess(success);
        switch (success) {
            case Constant.SuccessKey.SIGN_UP_SUCCESS:
                mFragmentManager.popBackStack();
                break;
            case Constant.SuccessKey.SIGN_IN_SUCCESS:
                /*handle here : when sign in success replace fragment to my page*/
                mFragmentManager.beginTransaction().replace(R.id.fl_sign_container, new MyPageFragment(), MyPageFragment.class.getName()).commit();
                getBinding().vpBottomNav.setCurrentItem(0);
                break;
            case Constant.SuccessKey.SIGN_OUT_SUCCESS:
                mFragmentManager.beginTransaction().replace(R.id.fl_sign_container, new SignInFragment(), SignInFragment.class.getName()).commit();
                getBinding().vpBottomNav.setCurrentItem(0);
                break;
            case Constant.SuccessKey.DELETE_USER_SUCCESS:
                mFragmentManager.beginTransaction().replace(R.id.fl_sign_container, new SignInFragment(), SignInFragment.class.getName()).commit();
                getBinding().vpBottomNav.setCurrentItem(0);
                break;
            case Constant.SuccessKey.UPDATE_PASSWORD_SUCCESS:
                showSnackMessage(getString(R.string.snack_re_sign_in));
                mFragmentManager.beginTransaction().replace(R.id.fl_sign_container, new SignInFragment(), SignInFragment.class.getName()).commit();
                getBinding().vpBottomNav.setCurrentItem(0);
                break;
            case Constant.SuccessKey.UPDATE_PROFILE_SUCCESS:
                showSnackMessage(getString(R.string.snack_update_profile_success));
                break;
            case Constant.SuccessKey.UPDATE_NICK_NAME_SUCCESS:
                showSnackMessage(getString(R.string.snack_change_nick_name_success));
                break;
            case Constant.SuccessKey.SEARCH_SUCCESS:
                showSnackMessage(getString(R.string.snack_search_success));
                break;
        }
    }

    @Override
    protected void freezeUI() {
        super.freezeUI();
        getBinding().pgBottomNav.setVisibility(View.VISIBLE);
    }

    @Override
    protected void unFreezeUI() {
        super.unFreezeUI();
        getBinding().pgBottomNav.setVisibility(View.GONE);
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
        createViewModel(UserViewModel.class, new UserViewModelFactory(AppDataSource.getInstance(),
                APIManager.getInstance(),
                this));
        createViewModel(SearchingViewModel.class, new SearchingViewModelFactory(getApplication(),
                AppDataSource.getInstance(), APIManager.getInstance(), this));
        createViewModel(ChattingViewModel.class, new ChattingViewModelFactory(this));
    }


    private void onNavItemSelected(MenuItem item) {
        mDisposable.add(Single.just(item)
                .map(MenuItem::getItemId)
                .subscribe(id -> {
                    switch (id) {
                        case R.id.action_my_page:
                            getBinding().vpBottomNav.setCurrentItem(0);
                            break;
                        case R.id.action_map:
                            getBinding().vpBottomNav.setCurrentItem(1);
                            break;
                        case R.id.action_message:
                            getBinding().vpBottomNav.setCurrentItem(2);
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

        list.add(new SignFragment());
        list.add(new SearchFragment());
        list.add(new ChatListFragment());

        getBinding().vpBottomNav.setAdapter(viewPagerAdapter);
        getBinding().vpBottomNav.setOffscreenPageLimit(3);
        getBinding().vpBottomNav.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        getBinding().bottomNav.setSelectedItemId(R.id.action_my_page);
                        break;
                    case 1:
                        getBinding().bottomNav.setSelectedItemId(R.id.action_map);
                        break;
                    case 2:
                        getBinding().bottomNav.setSelectedItemId(R.id.action_message);
                        mViewPagerListener.onViewPagerChanged();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



        viewPagerAdapter.setItems(list);
        int position = getIntent().getIntExtra(BOTTOM_NAVIGATION_POSITION, 1);
        getBinding().vpBottomNav.setCurrentItem(position);
    }

    public void setViewPagerListener(OnViewPagerChangedListener onViewPagerChangedListener) {
        this.mViewPagerListener = onViewPagerChangedListener;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
