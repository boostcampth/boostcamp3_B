package com.boostcampa2.catchhouse.view.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.boostcampa2.catchhouse.R;
import com.boostcampa2.catchhouse.data.roomsdata.RoomsRepository;
import com.boostcampa2.catchhouse.data.userdata.UserRepository;
import com.boostcampa2.catchhouse.databinding.ActivityBottomNavBinding;
import com.boostcampa2.catchhouse.view.BaseActivity;
import com.boostcampa2.catchhouse.view.fragments.MapFragment;
import com.boostcampa2.catchhouse.view.fragments.HomeFragment;
import com.boostcampa2.catchhouse.view.fragments.SignInFragment;
import com.boostcampa2.catchhouse.viewmodel.ViewModelListener;
import com.boostcampa2.catchhouse.viewmodel.roomsviewmodel.RoomsViewModel;
import com.boostcampa2.catchhouse.viewmodel.roomsviewmodel.RoomsViewModelFactory;
import com.boostcampa2.catchhouse.viewmodel.userviewmodel.UserViewModel;
import com.boostcampa2.catchhouse.viewmodel.userviewmodel.UserViewModelFactory;

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
        Toast.makeText(this, throwable.toString(), Toast.LENGTH_SHORT).show();
        Log.d("에러", "onError: " + throwable.toString());
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

        createViewModels();
        mDisposable = new CompositeDisposable();
        mFragmentManager = getSupportFragmentManager();
        getBinding().bnavHomeActivity.setItemIconTintList(null);
        getBinding().bnavHomeActivity.setOnNavigationItemSelectedListener(v -> {
            onNavItemSelected(v);
            return true;
        });

        mFragmentManager.beginTransaction().add(R.id.fl_home_container, new HomeFragment()).commit();
    }

    private void createViewModels() {
        createViewModel(UserViewModel.class, new UserViewModelFactory(getApplication(), UserRepository.getInstance(), this));
        createViewModel(RoomsViewModel.class, new RoomsViewModelFactory(getApplication(), RoomsRepository.getInstance(), this));

    }

    private void onNavItemSelected(MenuItem item) {
        mDisposable.add(Single.just(item)
                .map(MenuItem::getItemId)
                .subscribe(id -> {
                    switch (id) {
                        case R.id.action_home:
                            /* handle here: replace fragment on home btn Clicked */
                            mFragmentManager.beginTransaction().replace(R.id.fl_home_container, new HomeFragment()).commit();
                            break;
                        case R.id.action_map:
                            mFragmentManager.beginTransaction().replace(R.id.fl_home_container, new MapFragment()).commit();
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

    @Override
    public void onClicked() {
        getBinding().bnavHomeActivity.setSelectedItemId(R.id.action_map);
    }

}
