package com.boostcampa2.catchhouse.view.activities;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.databinding.ActivityBottomNavBinding;
import com.boostcampa2.catchhouse.view.BaseActivity;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class BottomNavActivity extends BaseActivity<ActivityBottomNavBinding, ViewModel, ViewModelProvider.Factory> {

    private FragmentManager mFragmentManager;
    private CompositeDisposable mDisposable;

    @Override
    protected int setLayout() {
        return R.layout.activity_bottom_nav;
    }

    @Override
    protected Class<ViewModel> setViewModel() {
        /* this activity is not using ViewModel */
        return null;
    }

    @Nullable
    @Override
    protected ViewModelProvider.Factory setViewModelFactory() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDisposable = new CompositeDisposable();
        mFragmentManager = getSupportFragmentManager();
        getBinding().bnavHomeActivity.setItemIconTintList(null);
        getBinding().bnavHomeActivity.setOnNavigationItemSelectedListener(v -> {
            onNavItemSelected(v);
            return true;
        });
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
                            /* handle here: replace fragment on myPage btn Clicked */
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
