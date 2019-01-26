package com.boostcampa2.catchhouse.view.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.databinding.ActivityBottomNavBinding;
import com.boostcampa2.catchhouse.view.BaseActivity;
import com.boostcampa2.catchhouse.view.fragments.SignInFragment;
import com.boostcampa2.catchhouse.viewmodel.userviewmodel.UserViewModel;
import com.boostcampa2.catchhouse.viewmodel.userviewmodel.UserViewModelFactory;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class BottomNavActivity extends BaseActivity<ActivityBottomNavBinding> {

    private FragmentManager mFragmentManager;
    private CompositeDisposable mDisposable;

    @Override
    protected int setLayout() {
        return R.layout.activity_bottom_nav;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createViewModes();
        mDisposable = new CompositeDisposable();
        mFragmentManager = getSupportFragmentManager();
        getBinding().bnavHomeActivity.setItemIconTintList(null);
        getBinding().bnavHomeActivity.setOnNavigationItemSelectedListener(v -> {
            onNavItemSelected(v);
            return true;
        });

    }

    private void createViewModes() {
        createViewModel(UserViewModel.class, new UserViewModelFactory(getApplication()));
    }

    private void onNavItemSelected(MenuItem item) {
        mDisposable.add(Single.just(item)
                .map(MenuItem::getItemId)
                .subscribe(id -> {
                    switch (id) {
                        case R.id.action_home:
                            /* handle here: replace fragment on home btn Clicked */
                            break;
                        case R.id.action_map:
                            /* handle here: replace fragment on map btn Clicked */
                            break;
                        case R.id.action_message:
                            /* handle here: replace fragment on message btn Clicked */
                            break;
                        case R.id.action_my_page:
                            mFragmentManager.beginTransaction().replace(R.id.fl_home_container, new SignInFragment()).commit();
                            break;
                    }
                }));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.dispose();
    }
}
