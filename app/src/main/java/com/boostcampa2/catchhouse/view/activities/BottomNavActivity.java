package com.boostcampa2.catchhouse.view.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.data.userdata.UserRepository;
import com.boostcampa2.catchhouse.databinding.ActivityBottomNavBinding;
import com.boostcampa2.catchhouse.view.BaseActivity;
import com.boostcampa2.catchhouse.view.fragments.SignInFragment;
import com.boostcampa2.catchhouse.viewmodel.ViewModelListener;
import com.boostcampa2.catchhouse.viewmodel.userviewmodel.UserViewModel;
import com.boostcampa2.catchhouse.viewmodel.userviewmodel.UserViewModelFactory;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class BottomNavActivity extends BaseActivity<ActivityBottomNavBinding> implements ViewModelListener {

    private FragmentManager mFragmentManager;
    private CompositeDisposable mDisposable;

    @Override
    protected int setLayout() {
        return R.layout.activity_bottom_nav;
    }

    @Override
    public void onError(Throwable throwable) {
        Toast.makeText(this, throwable.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isWorking() {
        Toast.makeText(this, "일합니다.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isFinished() {
        Toast.makeText(this, "끝", Toast.LENGTH_SHORT).show();
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
        createViewModel(UserViewModel.class, new UserViewModelFactory(getApplication(), UserRepository.getInstance(), this));
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
